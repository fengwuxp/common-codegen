package com.wuxp.codegen.loong;


import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.TaskWaiter;
import com.wuxp.codegen.core.UnifiedResponseExplorer;
import com.wuxp.codegen.core.config.CodegenConfigHolder;
import com.wuxp.codegen.core.event.CodeGenPublisher;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.meta.util.JavaMethodNameUtils;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.enums.ClassType;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Controller;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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

    private static final int MAX_CODEGEN_LOOP_COUNT = 100;

    /**
     * spring 的包扫描组件
     */
    protected ClassPathScanningCandidateComponentProvider classPathScanningCandidateComponentProvider = new ClassPathScanningCandidateComponentProvider(false);


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


    /**
     * 在结束前需要等待的任务
     */
    protected Collection<TaskWaiter> taskWaiters;

    /**
     * 统一响应对象的探测
     */
    protected UnifiedResponseExplorer unifiedResponseExplorer;

    /**
     * 需要额外的生成代码
     */
    protected Set<CommonCodeGenClassMeta> otherCodegenClassMetas;


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

            classPathScanningCandidateComponentProvider
                    .addExcludeFilter((metadataReader, metadataReaderFactory) -> Arrays.stream(this.ignoreClasses)
                            .filter(Objects::nonNull)
                            .map(clazz -> metadataReader.getClassMetadata().getClassName().equals(clazz.getName()))
                            .filter(b -> b)
                            .findFirst()
                            .orElse(false));

        }

        this.enableFieldUnderlineStyle = enableFieldUnderlineStyle;
        this.codeGenPublisher = codeGenPublisher;
        this.unifiedResponseExplorer = new LoongUnifiedResponseExplorer(null);

        classPathScanningCandidateComponentProvider.addIncludeFilter(new AnnotationTypeFilter(Controller.class));
        classPathScanningCandidateComponentProvider.addIncludeFilter(new AnnotationTypeFilter(RestController.class));
        classPathScanningCandidateComponentProvider.addExcludeFilter(new AnnotationTypeFilter(ControllerAdvice.class));
        classPathScanningCandidateComponentProvider.addExcludeFilter(new AnnotationTypeFilter(RestControllerAdvice.class));
    }

    @Override
    public void generate() {
        Set<Class<?>> classes = this.scanPackages();
        this.tryLoopGenerate(classes);
        // clear config
        CodegenConfigHolder.clear();
    }

    /**
     * 需要调用者执行 {@link CodegenConfigHolder#clear()}
     *
     * @param services 需要生成的接口
     */
    public void generateByClasses(Class<?>... services) {
        this.tryLoopGenerate(new HashSet<>(Arrays.asList(services)));
    }


    /**
     * 尝试做循环生成
     *
     * @param classes 需要生成的类列表
     */
    protected void tryLoopGenerate(Collection<Class<?>> classes) {
        if (unifiedResponseExplorer != null) {
            unifiedResponseExplorer.probe(classes);
        }
        Set<CommonCodeGenClassMeta> commonCodeGenClassMetas = parseCodegenMetas(classes);
        if (otherCodegenClassMetas != null) {
            commonCodeGenClassMetas.addAll(otherCodegenClassMetas);
        }
        int i = 0;
        for (; ; ) {
            log.warn("循环生成，第{}次", i);
            commonCodeGenClassMetas = onceGenerate(commonCodeGenClassMetas);
            if (commonCodeGenClassMetas.isEmpty() || i > MAX_CODEGEN_LOOP_COUNT) {
                break;
            }
            i++;
        }
        if (codeGenPublisher != null) {
            codeGenPublisher.sendCodeGenEnd();
            if (codeGenPublisher.supportPark()) {
                long maxParkNanos = codeGenPublisher.getMaxParkNanos();
                if (maxParkNanos > 0) {
                    LockSupport.parkNanos(maxParkNanos);
                } else {
                    LockSupport.park();
                }
            }
        }

        if (CollectionUtils.isEmpty(taskWaiters)) {
            return;
        }
        // 等待所有的任务完成
        for (TaskWaiter taskWaiter : taskWaiters) {
            if (taskWaiter == null) {
                continue;
            }
            taskWaiter.waitTaskCompleted();
        }
    }


    protected Set<CommonCodeGenClassMeta> onceGenerate(Set<CommonCodeGenClassMeta> commonCodeGenClassMetas) {
        final CodeGenPublisher codeGenPublisher = this.codeGenPublisher;
        final boolean needSendEvent = codeGenPublisher != null;
        return commonCodeGenClassMetas.stream()
                .filter(Objects::nonNull)
                .filter(this::hasExistMember)
                .map(commonCodeGenClassMeta -> {
                    //模板处理，生成服务
                    if (Boolean.TRUE.equals(enableFieldUnderlineStyle) && commonCodeGenClassMeta.getFieldMetas() != null) {
                        //将方法参数字段名称设置为下划线
                        Arrays.stream(commonCodeGenClassMeta.getFieldMetas())
                                .forEach(commonCodeGenFiledMeta -> commonCodeGenFiledMeta
                                        .setName(JavaMethodNameUtils.humpToLine(commonCodeGenFiledMeta.getName())));
                    }

                    Map<String, ? extends CommonCodeGenClassMeta> dependencies = commonCodeGenClassMeta.getDependencies();
                    Map<String, CommonCodeGenClassMeta> needImportDependencies = new LinkedHashMap<>();
                    Collection<? extends CommonCodeGenClassMeta> values = dependencies.values();
                    //过滤掉不需要导入的依赖
                    dependencies.forEach((key, val) -> {
                        if (needImportDependencies(val)) {
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
                            this.codeGenPublisher.sendCodeGen(commonCodeGenClassMeta);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        if (needSendEvent) {
                            this.codeGenPublisher.sendCodeGenError(e, commonCodeGenClassMeta);
                        }
                    }
                    return values;
                })
                .flatMap(Collection::stream)
                .filter(CommonCodeGenClassMeta::getNeedGenerate)
                .collect(Collectors.toSet());
    }

    private boolean needImportDependencies(CommonCodeGenClassMeta val) {
        return (val.isNeedImport() || this.hasExistMember(val)) && hasPackagePath(val);
    }

    protected Set<CommonCodeGenClassMeta> parseCodegenMetas(Collection<Class<?>> classes) {
        return classes.stream()
                .map(clazz -> this.languageParser.parse(clazz))
                .filter(Objects::nonNull)
                .filter(this::hasExistMember)
                .collect(Collectors.toSet());
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
     * 是否存在成员
     *
     * @param commonCodeGenClassMeta 用于生成代码的类描述对象
     * @return <code>true</code> 存在成员（方法或字段）需要进行生成
     */
    private boolean hasExistMember(CommonCodeGenClassMeta commonCodeGenClassMeta) {
        if (ClassType.ENUM.equals(commonCodeGenClassMeta.getClassType())) {
            return true;
        }
        if (Boolean.TRUE.equals(commonCodeGenClassMeta.getNeedGenerate())) {
            return true;
        }
        boolean notMethod = commonCodeGenClassMeta.getMethodMetas() == null || commonCodeGenClassMeta.getMethodMetas().length == 0;
        if (ClassType.INTERFACE.equals(commonCodeGenClassMeta.getClassType()) && notMethod) {
            return false;
        }

        boolean notFiled = commonCodeGenClassMeta.getFieldMetas() == null || commonCodeGenClassMeta.getFieldMetas().length == 0;
        return !(notFiled && notMethod);
    }

    protected boolean hasPackagePath(CommonCodeGenClassMeta commonCodeGenClassMeta) {
        return StringUtils.hasText(commonCodeGenClassMeta.getPackagePath());
    }


    /**
     * 过滤掉重复的字段
     *
     * @param meta 用于生成代码的类描述对象
     */
    private void filterDuplicateFields(CommonCodeGenClassMeta meta) {
        CommonCodeGenFiledMeta[] fieldMetas = meta.getFieldMetas();
        if (fieldMetas == null) {
            return;
        }
        Map<String, Integer> countMap = new HashMap<>(fieldMetas.length);
        CommonCodeGenFiledMeta[] commonCodeGenFiledMetas = Arrays.stream(fieldMetas)
                .filter(commonCodeGenFiledMeta -> {
                    String name = commonCodeGenFiledMeta.getName();
                    int i = countMap.getOrDefault(name, 0);
                    if (i == 0) {
                        countMap.put(name, ++i);
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
     * @param meta 用于生成代码的类描述对象
     */
    private void removeInvalidDependencies(CommonCodeGenClassMeta meta) {
        Set<Class<?>> effectiveDependencies = new HashSet<>();
        // 移除无效的依赖
        if (meta.getFieldMetas() != null) {
            CommonCodeGenFiledMeta[] fieldMetas = meta.getFieldMetas();
            effectiveDependencies.addAll(Arrays.stream(fieldMetas)
                    .map(CommonCodeGenFiledMeta::getFiledTypes)
                    .filter(Objects::nonNull)
                    .map(Arrays::asList)
                    .flatMap(Collection::stream)
                    .map(CommonCodeGenClassMeta::getSource)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet()));
        }
        if (meta.getMethodMetas() != null) {
            effectiveDependencies.addAll(Arrays.stream(meta.getMethodMetas())
                    .map(CommonCodeGenMethodMeta::getReturnTypes)
                    .filter(Objects::nonNull)
                    .map(Arrays::asList)
                    .flatMap(Collection::stream)
                    .map(CommonCodeGenClassMeta::getSource)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet()));
            effectiveDependencies.addAll(Arrays.stream(meta.getMethodMetas())
                    .map(CommonCodeGenMethodMeta::getParams)
                    .filter(Objects::nonNull)
                    .map(Arrays::asList)
                    .flatMap(Collection::stream)
                    .map(Map::values)
                    .flatMap(Collection::stream)
                    .map(CommonCodeGenClassMeta::getSource)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet()));
        }

        CommonCodeGenClassMeta superClass = meta.getSuperClass();
        if (superClass != null) {
            effectiveDependencies.add(superClass.getSource());
        }
        CommonCodeGenClassMeta[] interfaces = meta.getInterfaces();
        if (interfaces != null) {
            Arrays.asList(interfaces).forEach(commonCodeGenClassMeta -> effectiveDependencies.add(commonCodeGenClassMeta.getSource()));
        }

        Map<String, CommonCodeGenClassMeta> newDependencies = new LinkedHashMap<>();
        Map<String, ? extends CommonCodeGenClassMeta> dependencies = meta.getDependencies();
        dependencies.forEach((key, value) -> {
            Class<?> aClass = value.getSource();
            // 排除掉无效的依赖
            boolean isExclude = aClass != null && !effectiveDependencies.contains(aClass);
            if (isExclude) {
                return;
            }
            newDependencies.put(key, value);
        });
        meta.setDependencies(newDependencies);
    }

    public AbstractCodeGenerator otherCodegenClassMetas(Set<CommonCodeGenClassMeta> otherCodegenClassMetas) {
        this.otherCodegenClassMetas = otherCodegenClassMetas;
        return this;
    }

    public AbstractCodeGenerator taskWaiters(Collection<TaskWaiter> taskWaiters) {
        this.taskWaiters = taskWaiters;
        return this;
    }
}
