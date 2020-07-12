package com.wuxp.codegen.dragon;


import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.event.CodeGenPublisher;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.enums.ClassType;
import com.wuxp.codegen.utils.JavaMethodNameUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.Collectors;


/**
 * abstract code generator
 *
 * @author wxup
 */
@Slf4j
@Setter
public abstract class AbstractCodeGenerator implements CodeGenerator {

    /**
     * spring 的包扫描组件
     */
    protected ClassPathScanningCandidateComponentProvider classPathScanningCandidateComponentProvider = new ClassPathScanningCandidateComponentProvider(true);


    /**
     * 要进行生成的源代码包名列表
     */
    protected String[] packagePaths;

    /**
     * 忽略的包
     */
    protected Set<String> ignorePackages;

    /**
     * 额外导入的类
     */
    protected Class<?>[] includeClasses;

    /**
     * 需要忽略的类
     */
    protected Class<?>[] ignoreClasses;


    /**
     * 语言解析器
     */
    protected LanguageParser<CommonCodeGenClassMeta> languageParser;


    /**
     * 模板处理策略
     */
    protected TemplateStrategy<CommonCodeGenClassMeta> templateStrategy;


    protected PathMatcher pathMatcher = new AntPathMatcher();

    /**
     * 启用下划线风格
     */
    protected Boolean enableFieldUnderlineStyle;


    /**
     * 生成事件发送者
     */
    protected CodeGenPublisher codeGenPublisher;


    public AbstractCodeGenerator(String[] packagePaths,
                                 LanguageParser<CommonCodeGenClassMeta> languageParser,
                                 TemplateStrategy<CommonCodeGenClassMeta> templateStrategy,
                                 boolean enableFieldUnderlineStyle) {
        this(packagePaths, null, null, null, languageParser, templateStrategy, enableFieldUnderlineStyle, null);
    }

    public AbstractCodeGenerator(String[] packagePaths,
                                 LanguageParser<CommonCodeGenClassMeta> languageParser,
                                 TemplateStrategy<CommonCodeGenClassMeta> templateStrategy,
                                 boolean enableFieldUnderlineStyle,
                                 CodeGenPublisher codeGenPublisher) {
        this(packagePaths, null, null, null, languageParser, templateStrategy, enableFieldUnderlineStyle, codeGenPublisher);
    }


    public AbstractCodeGenerator(String[] packagePaths,
                                 Set<String> ignorePackages,
                                 Class<?>[] includeClasses,
                                 Class<?>[] ignoreClasses,
                                 LanguageParser<CommonCodeGenClassMeta> languageParser,
                                 TemplateStrategy<CommonCodeGenClassMeta> templateStrategy,
                                 boolean enableFieldUnderlineStyle,
                                 CodeGenPublisher codeGenPublisher) {
        this.packagePaths = packagePaths;
        this.includeClasses = includeClasses;
        this.languageParser = languageParser;
        this.templateStrategy = templateStrategy;

        if (ignorePackages != null) {
            //排除的包
            this.ignorePackages = ignorePackages.stream()
                    .filter(Objects::nonNull)
                    .map(s -> {
                        if (this.pathMatcher.isPattern(s)) {
                            return s;
                        } else {
                            return MessageFormat.format("{0}**", s);
                        }
                    }).collect(Collectors.toSet());

            classPathScanningCandidateComponentProvider.addExcludeFilter((metadataReader, metadataReaderFactory) -> this.ignorePackages.stream()
                    .map(s -> this.pathMatcher.match(s, metadataReader.getClassMetadata().getClassName()))
                    .filter(b -> b)
                    .findFirst()
                    .orElse(false));
        }

        if (ignoreClasses != null) {
            //排除的的类
            this.ignoreClasses = ignoreClasses;

            classPathScanningCandidateComponentProvider.addExcludeFilter((metadataReader, metadataReaderFactory) -> Arrays.stream(this.ignoreClasses)
                    .filter(Objects::nonNull)
                    .map(clazz -> metadataReader.getClassMetadata().getClassName().equals(clazz.getName()))
                    .filter(b -> b)
                    .findFirst()
                    .orElse(false));

        }

        this.enableFieldUnderlineStyle = enableFieldUnderlineStyle;
        this.codeGenPublisher = codeGenPublisher;
    }

    @Override
    public void generate() {
        List<CommonCodeGenClassMeta> commonCodeGenClassMetas = this.scanPackages().stream()
                .map(this.languageParser::parse)
                .filter(Objects::nonNull)
                .filter(this::filterNoneClazz)
                .collect(Collectors.toList());

        CodeGenPublisher codeGenPublisher = this.codeGenPublisher;
        final boolean needSendEvent = codeGenPublisher != null;

        int i = 0;
        for (; ; ) {
            log.warn("循环生成，第{}次", i);
            commonCodeGenClassMetas = commonCodeGenClassMetas.stream()
                    .filter(Objects::nonNull)
//                    .filter(this::filterNoneClazz)
                    .map(commonCodeGenClassMeta -> {
                        //模板处理，生成服务
                        if (Boolean.TRUE.equals(enableFieldUnderlineStyle) && commonCodeGenClassMeta.getFieldMetas() != null) {
                            //将方法参数字段名称设置为下划线
                            Arrays.stream(commonCodeGenClassMeta.getFieldMetas()).forEach(commonCodeGenFiledMeta -> {
                                commonCodeGenFiledMeta.setName(JavaMethodNameUtil.humpToLine(commonCodeGenFiledMeta.getName()));
                            });
                        }

                        Map<String, ? extends CommonCodeGenClassMeta> dependencies = commonCodeGenClassMeta.getDependencies();
                        Map<String, CommonCodeGenClassMeta> needImportDependencies = new LinkedHashMap<>();
                        Collection<? extends CommonCodeGenClassMeta> values = dependencies.values();
                        //过滤掉不需要导入的依赖
                        dependencies.forEach((key, val) -> {
                            if (val.getNeedImport()) {
                                needImportDependencies.put(key, val);
                            }
                        });

                        commonCodeGenClassMeta.setDependencies(needImportDependencies);


                        filterDuplicateFields(commonCodeGenClassMeta);
                        //移除掉不需要的依赖
                        removeInvalidDependencies(commonCodeGenClassMeta);
                        try {
                            this.templateStrategy.build(commonCodeGenClassMeta);
                            if (needSendEvent) {
                                codeGenPublisher.sendCodeGen(commonCodeGenClassMeta);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            if (needSendEvent) {
                                codeGenPublisher.sendCodeGenError(e, commonCodeGenClassMeta);
                            }
                        }
                        return values;
                    }).flatMap(Collection::stream)
                    .filter(CommonCodeGenClassMeta::getNeedGenerate)
                    .collect(Collectors.toList());
            if (commonCodeGenClassMetas.size() == 0 || i > 100) {
                break;
            }
            i++;
        }
        if (needSendEvent) {
            codeGenPublisher.sendCodeGenEnd();
            if (codeGenPublisher.supportPark()) {
                // 最多等待10秒
                LockSupport.parkNanos(10 * 1000 * 1000 * 1000);
            }

        }

    }


    /**
     * 包扫描 获的需要生成的类
     *
     * @return 需要生成的类
     */
    protected Set<Class<?>> scanPackages() {


        Set<Class<?>> classes = Arrays.stream(packagePaths)
                .map(classPathScanningCandidateComponentProvider::findCandidateComponents)
                .flatMap(Collection::stream)
                .map(BeanDefinition::getBeanClassName)
                .map(className -> {
                    try {
                        return Thread.currentThread().getContextClassLoader().loadClass(className);
                    } catch (ClassNotFoundException e) {
                        log.error("加载类失败", e);
                    }
                    return null;
                }).filter(Objects::nonNull)
                .collect(Collectors.toSet());


        if (this.includeClasses != null) {
            classes.addAll(Arrays.asList(this.includeClasses));
        }


        log.debug("共扫描到{}个类文件", classes.size());
        return classes;
    }

    /**
     * 过滤掉无效的数据
     *
     * @param commonCodeGenClassMeta
     * @return
     */
    private boolean filterNoneClazz(CommonCodeGenClassMeta commonCodeGenClassMeta) {

        boolean notMethod = commonCodeGenClassMeta.getMethodMetas() == null || commonCodeGenClassMeta.getMethodMetas().length == 0;

        if (ClassType.INTERFACE.equals(commonCodeGenClassMeta.getClassType()) && notMethod) {
            return false;
        }

        boolean notFiled = commonCodeGenClassMeta.getFieldMetas() == null || commonCodeGenClassMeta.getFieldMetas().length == 0;
        return !(notFiled && notMethod);
    }


    private void filterDuplicateFields(CommonCodeGenClassMeta meta) {
        CommonCodeGenFiledMeta[] fieldMetas = meta.getFieldMetas();
        if (fieldMetas == null) {
            return;
        }
        Map<String, Integer> count = new HashMap<>();
        CommonCodeGenFiledMeta[] commonCodeGenFiledMetas = Arrays.asList(fieldMetas).stream().filter(commonCodeGenFiledMeta -> {
            String name = commonCodeGenFiledMeta.getName();
            int i = count.getOrDefault(name, 0);
            if (i == 0) {
                count.put(name, ++i);
                return true;
            }
            // 属性重复了
            return false;
        }).toArray(CommonCodeGenFiledMeta[]::new);
        meta.setFieldMetas(commonCodeGenFiledMetas);
    }

    /**
     * 移除无效的依赖
     *
     * @param meta
     */
    private void removeInvalidDependencies(CommonCodeGenClassMeta meta) {
        Set<Class<?>> effectiveDependencies = new HashSet<>();
        // 移除无效的依赖
        if (meta.getFieldMetas() != null) {
            CommonCodeGenFiledMeta[] fieldMetas = meta.getFieldMetas();
            effectiveDependencies.addAll(Arrays.stream(fieldMetas).map(commonCodeGenFiledMeta -> commonCodeGenFiledMeta.getFiledTypes())
                    .filter(Objects::nonNull)
                    .map(Arrays::asList)
                    .flatMap(Collection::stream)
                    .map(CommonCodeGenClassMeta::getSource)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet()));
        }
        if (meta.getMethodMetas() != null) {
            effectiveDependencies.addAll(Arrays.stream(meta.getMethodMetas())
                    .map(commonCodeGenFiledMeta -> commonCodeGenFiledMeta.getReturnTypes())
                    .filter(Objects::nonNull)
                    .map(Arrays::asList)
                    .flatMap(Collection::stream)
                    .map(CommonCodeGenClassMeta::getSource)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet()));
            effectiveDependencies.addAll(Arrays.stream(meta.getMethodMetas())
                    .map(commonCodeGenFiledMeta -> commonCodeGenFiledMeta.getParams())
                    .filter(Objects::nonNull)
                    .map(Arrays::asList)
                    .flatMap(Collection::stream)
                    .map(Map::values)
                    .flatMap(Collection::stream)
                    .map(CommonCodeGenClassMeta::getSource)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet()));
        }
        Map<String, CommonCodeGenClassMeta> newDependencies = new LinkedHashMap<>();
        Map<String, ? extends CommonCodeGenClassMeta> dependencies = meta.getDependencies();
        dependencies.forEach((key, value) -> {
            Class<?> aClass = value.getSource();
            if (aClass != null) {
                if (!effectiveDependencies.contains(aClass)) {
                    return;
                }
            }

            newDependencies.put(key, value);
        });
        meta.setDependencies(newDependencies);
    }

}
