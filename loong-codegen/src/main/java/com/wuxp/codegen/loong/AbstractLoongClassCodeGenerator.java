package com.wuxp.codegen.loong;

import com.wuxp.codegen.core.ClassCodeGenerator;
import com.wuxp.codegen.core.CodeGenerateAsyncTaskFuture;
import com.wuxp.codegen.core.UnifiedResponseExplorer;
import com.wuxp.codegen.core.config.CodegenConfigHolder;
import com.wuxp.codegen.core.event.CodeGenEvent;
import com.wuxp.codegen.core.event.CodeGenEventListener;
import com.wuxp.codegen.core.event.CodeGenEventPublisher;
import com.wuxp.codegen.core.exception.CodegenRuntimeException;
import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.meta.util.JavaMethodNameUtils;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.enums.ClassType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.wuxp.codegen.core.event.CodeGenEventListener.EVENT_CODEGEN_META_TAG_NAME;

/**
 * @author wuxp
 */
@Slf4j
public abstract class AbstractLoongClassCodeGenerator implements ClassCodeGenerator, CodeGenEventPublisher {

    private static final int MAX_CODEGEN_LOOP_COUNT = 100;

    /**
     * spring 的包扫描组件
     */
    private final ClassPathScanningCandidateComponentProvider classPathScanningCandidateComponentProvider;

    /**
     * 要进行生成的源代码包名列表，支持 ant path
     */
    private final String[] scanPackages;

    private final LanguageTypeDefinitionParser<? extends CommonCodeGenClassMeta> languageTypeDefinitionParser;

    /**
     * 模板处理策略
     */
    private final TemplateStrategy<CommonCodeGenClassMeta> templateStrategy;

    /**
     * 统一响应对象的探测
     */
    private final UnifiedResponseExplorer unifiedResponseExplorer;

    private final CodeGenerateAsyncTaskFuture codeGenerateAsyncTaskFuture;

    private CodeGenEvent.CodeGenEventStatus codeGenEventStatus = CodeGenEvent.CodeGenEventStatus.SCAN_CODEGEN;

    protected AbstractLoongClassCodeGenerator(String[] scanPackages,
                                              LanguageTypeDefinitionParser<? extends CommonCodeGenClassMeta> languageTypeDefinitionParser,
                                              TemplateStrategy<CommonCodeGenClassMeta> templateStrategy,
                                              UnifiedResponseExplorer unifiedResponseExplorer) {
        this.scanPackages = scanPackages;
        this.languageTypeDefinitionParser = languageTypeDefinitionParser;
        this.templateStrategy = templateStrategy;
        this.unifiedResponseExplorer = unifiedResponseExplorer;
        this.codeGenerateAsyncTaskFuture = CombineCodeGenerateAsyncTaskFuture.getInstance();
        this.classPathScanningCandidateComponentProvider = initComponentProvider();

    }

    private ClassPathScanningCandidateComponentProvider initComponentProvider() {
        ClassPathScanningCandidateComponentProvider result = new ClassPathScanningCandidateComponentProvider(false);
        result.addIncludeFilter(new AnnotationTypeFilter(Controller.class));
        result.addIncludeFilter(new AnnotationTypeFilter(RestController.class));
        result.addExcludeFilter(new AnnotationTypeFilter(ControllerAdvice.class));
        result.addExcludeFilter(new AnnotationTypeFilter(RestControllerAdvice.class));
        configureComponentProvider(result);
        return result;
    }

    protected abstract void configureComponentProvider(ClassPathScanningCandidateComponentProvider componentProvider);

    /**
     * 需要额外的生成代码
     */
    protected abstract Set<CommonCodeGenClassMeta> getIncludeClassMetas();

    protected abstract boolean isEnableFieldUnderlineStyle();

    protected abstract CodeGenEventListener getCodeGenEventListener();

    @Override
    public void generate() {
        generate(this.scanPackages());
    }

    @Override
    public void generate(Collection<Class<?>> classes) {
        try {
            this.loopGenerate(classes).get();
        } catch (InterruptedException exception) {
            log.error("线程被中断，message={}", exception.getMessage(), exception);
            Thread.currentThread().interrupt();
        } catch (ExecutionException exception) {
            throw new CodegenRuntimeException(exception);
        } finally {
            // clear config
            CodegenConfigHolder.clear();
        }
    }

    @Override
    public void publishEvent(Object event) {
        CodeGenEventListener eventListener = getCodeGenEventListener();
        if (eventListener == null) {
            log.warn("没有获取到 CodeGenEventListener");
        } else {
            eventListener.onApplicationEvent((CodeGenEvent) event);
        }
    }

    private CompletableFuture<Void> asyncGenerate() {
        Set<Class<?>> classes = this.scanPackages();
        return this.loopGenerate(classes);
    }

    /**
     * 包扫描 获的需要生成的类
     *
     * @return 需要生成的类
     */
    protected Set<Class<?>> scanPackages() {
        Set<Class<?>> result = Arrays.stream(scanPackages)
                .map(classPathScanningCandidateComponentProvider::findCandidateComponents)
                .flatMap(Collection::stream)
                .map(BeanDefinition::getBeanClassName)
                .map(this::loadClass)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        if (log.isInfoEnabled()) {
            log.info("共扫描到{}个类文件", result.size());
        }
        return result;
    }

    private Class<?> loadClass(String className) {
        try {
            return Thread.currentThread().getContextClassLoader().loadClass(className);
        } catch (ClassNotFoundException e) {
            log.error("加载类失败", e);
        }
        return null;
    }

    /**
     * 尝试做循环生成
     *
     * @param classes 需要生成的类列表
     */
    private CompletableFuture<Void> loopGenerate(Collection<Class<?>> classes) {
        unifiedResponseExplorer.probe(classes);
        Set<CommonCodeGenClassMeta> genClassMetas = parseCodegenMetas(classes);
        genClassMetas.addAll(getIncludeClassMetas());
        int generateCount = 0;
        while (true) {
            if (log.isInfoEnabled()) {
                log.info("循环生成，第{}次", generateCount);
            }
            genClassMetas = generateAndReturnMetas(genClassMetas);
            if (genClassMetas.isEmpty() || generateCount > MAX_CODEGEN_LOOP_COUNT) {
                break;
            }
            generateCount++;
        }
        // 广播生成完成事件
        this.codeGenEventStatus = CodeGenEvent.CodeGenEventStatus.SCAN_CODEGEN_DONE;
        publishEvent(new CodeGenEvent(new CommonCodeGenClassMeta(), codeGenEventStatus));
        // 生成完成后尝试生成通过事件聚合 Metas
        this.codeGenEventStatus = CodeGenEvent.CodeGenEventStatus.EVENT_CODEGEN;
        Set<CommonCodeGenClassMeta> completedEventMetas = getCompletedEventMetas();
        generateAndReturnMetas(completedEventMetas);
        this.codeGenEventStatus = CodeGenEvent.CodeGenEventStatus.COMPLETED;
        return codeGenerateAsyncTaskFuture.future();
    }

    private Set<CommonCodeGenClassMeta> getCompletedEventMetas() {
        CodeGenEventListener codeGenEventListener = getCodeGenEventListener();
        if (codeGenEventListener == null) {
            return Collections.emptySet();
        }
        return codeGenEventListener.getEventCodeGenMetas();
    }

    private Set<CommonCodeGenClassMeta> generateAndReturnMetas(Set<CommonCodeGenClassMeta> genClassMetas) {
        return genClassMetas.stream()
                .filter(Objects::nonNull)
                .filter(CommonCodeGenClassMeta::getNeedGenerate)
                .filter(this::canCodegen)
                .map(this::renderTemplate)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    private Collection<? extends CommonCodeGenClassMeta> renderTemplate(CommonCodeGenClassMeta meta) {
        filedNameUnderlineStyle(meta);
        filterDuplicateFields(meta);
        Collection<? extends CommonCodeGenClassMeta> result = meta.getDependencies().values();
        filterInvalidDependencies(meta);
        filterNoneImportDependencies(meta);
        try {
            this.templateStrategy.build(meta);
        } catch (Exception exception) {
            throw new CodegenRuntimeException(exception);
        }
        publishEvent(new CodeGenEvent(meta, this.codeGenEventStatus));
        return result;
    }

    private void filterNoneImportDependencies(CommonCodeGenClassMeta meta) {
        Map<String, CommonCodeGenClassMeta> needImportDependencies = new LinkedHashMap<>();
        meta.getDependencies().forEach((key, val) -> {
            if (needImportDependencies(val)) {
                // 过滤掉不需要导入的依赖
                needImportDependencies.put(key, val);
            }
        });
        meta.setDependencies(needImportDependencies);
    }

    private void filedNameUnderlineStyle(CommonCodeGenClassMeta meta) {
        // 模板处理，生成服务
        if (isEnableFieldUnderlineStyle() && meta.getFieldMetas() != null) {
            //将方法参数字段名称设置为下划线
            Arrays.stream(meta.getFieldMetas())
                    .forEach(commonCodeGenFiledMeta -> commonCodeGenFiledMeta
                            .setName(JavaMethodNameUtils.humpToLine(commonCodeGenFiledMeta.getName())));
        }
    }


    /**
     * 过滤掉重复的字段
     *
     * @param meta 用于生成代码的类描述对象
     */
    private void filterDuplicateFields(CommonCodeGenClassMeta meta) {
        CommonCodeGenFiledMeta[] fieldMetas = meta.getFieldMetas();
        if (ObjectUtils.isEmpty(fieldMetas)) {
            return;
        }
        Map<String, Integer> filedCounts = HashMap.newHashMap(fieldMetas.length);
        fieldMetas = Arrays.stream(fieldMetas)
                .filter(commonCodeGenFiledMeta -> {
                    String name = commonCodeGenFiledMeta.getName();
                    int count = filedCounts.getOrDefault(name, 0);
                    if (count == 0) {
                        filedCounts.put(name, ++count);
                        return true;
                    }
                    // 属性重复了
                    return false;
                })
                .toArray(CommonCodeGenFiledMeta[]::new);
        meta.setFieldMetas(fieldMetas);
    }

    /**
     * 移除无效的依赖
     *
     * @param meta 用于生成代码的类描述对象
     */
    private void filterInvalidDependencies(CommonCodeGenClassMeta meta) {
        Set<Class<?>> effectiveDependencies = new HashSet<>();
        // 移除无效的依赖
        if (meta.getFieldMetas() != null) {
            Set<? extends Class<?>> classes = Arrays.stream(meta.getFieldMetas())
                    .map(CommonCodeGenFiledMeta::getFiledTypes)
                    .filter(Objects::nonNull)
                    .map(Arrays::asList)
                    .flatMap(Collection::stream)
                    .map(CommonCodeGenClassMeta::getSource)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            effectiveDependencies.addAll(classes);
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
                    .map(CommonCodeGenMethodMeta::getDependencies)
                    .flatMap(Collection::stream)
                    .filter(Objects::nonNull)
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
            if (aClass == null || !effectiveDependencies.contains(aClass)) {
                // 排除掉无效的依赖
                return;
            }
            if (Objects.equals(aClass, meta.getSource())) {
                // 自己引用自己
                return;
            }
            newDependencies.put(key, value);
        });

        meta.setDependencies(newDependencies);
    }

    private Set<CommonCodeGenClassMeta> parseCodegenMetas(Collection<Class<?>> classes) {
        return classes.stream()
                .map(this.languageTypeDefinitionParser::parse)
                .filter(Objects::nonNull)
                .filter(this::canCodegen)
                .collect(Collectors.toSet());
    }

    /**
     * 是否可以用于生成
     *
     * @param classMeta 用于生成代码的类描述对象
     * @return <code>true</code> 存在成员（方法或字段）需要进行生成
     */
    private boolean canCodegen(CommonCodeGenClassMeta classMeta) {
        if (Boolean.TRUE.equals(classMeta.getTag(EVENT_CODEGEN_META_TAG_NAME))) {
            return true;
        }
        return hasExistMember(classMeta);
    }

    /**
     * 是否存在成员
     *
     * @param classMeta 用于生成代码的类描述对象
     * @return <code>true</code> 存在成员（方法或字段）需要进行生成
     */
    private boolean hasExistMember(CommonCodeGenClassMeta classMeta) {
        if (ClassType.ENUM.equals(classMeta.getClassType())) {
            return true;
        }
        if (Boolean.TRUE.equals(classMeta.getNeedGenerate())) {
            return true;
        }
        boolean notMethod = classMeta.getMethodMetas() == null || classMeta.getMethodMetas().length == 0;
        if (ClassType.INTERFACE.equals(classMeta.getClassType()) && notMethod) {
            return false;
        }

        boolean notFiled = classMeta.getFieldMetas() == null || classMeta.getFieldMetas().length == 0;
        return !(notFiled && notMethod);
    }

    private boolean needImportDependencies(CommonCodeGenClassMeta val) {
        return (val.isNeedImport() || this.canCodegen(val)) && hasPackagePath(val);
    }

    private boolean hasPackagePath(CommonCodeGenClassMeta commonCodeGenClassMeta) {
        return StringUtils.hasText(commonCodeGenClassMeta.getPackagePath());
    }


}
