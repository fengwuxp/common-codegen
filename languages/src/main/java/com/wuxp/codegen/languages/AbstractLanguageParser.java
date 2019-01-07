package com.wuxp.codegen.languages;

import com.wuxp.codegen.annotation.processor.AnnotationMate;
import com.wuxp.codegen.annotation.processor.AnnotationProcessor;
import com.wuxp.codegen.annotation.processor.javax.NotNullProcessor;
import com.wuxp.codegen.annotation.processor.javax.PatternProcessor;
import com.wuxp.codegen.annotation.processor.javax.SizeProcessor;
import com.wuxp.codegen.annotation.processor.spring.RequestMappingProcessor;
import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.CodeGenFilter;
import com.wuxp.codegen.core.filter.FilterClassByLibrary;
import com.wuxp.codegen.core.parser.GenericParser;
import com.wuxp.codegen.core.parser.JavaClassParser;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.model.*;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


/**
 * 抽象的语言解释器
 *
 * @param <C> 类
 * @param <M> 方法
 * @param <F> 属性
 */
@Slf4j
public abstract class AbstractLanguageParser<C extends CommonCodeGenClassMeta,
        M extends CommonCodeGenMethodMeta,
        F extends CommonCodeGenFiledMeta> implements LanguageParser<C> {


    /**
     * 类被处理的次数
     */
    protected static final Map<Class<?>, Integer> HANDLE_COUNT = new ConcurrentHashMap<>();

    /**
     * 处理结果缓存
     */
    protected static final Map<Class<?>, Object> HANDLE_RESULT_CACHE = new ConcurrentHashMap<>();


    /**
     * annotationProcessorMap
     */
    protected static final Map<Class<? extends Annotation>, AnnotationProcessor> ANNOTATION_PROCESSOR_MAP = new LinkedHashMap<>();


    /**
     * java类的解析器
     * 默认解析所有的属性 方法
     */
    protected GenericParser<JavaClassMeta, Class<?>> javaParser = new JavaClassParser(false);

    /**
     * 包名映射策略
     */
    protected PackageMapStrategy packageMapStrategy;

    /**
     * 代码检查者
     */
    protected Collection<CodeDetect> codeDetects;

    /**
     * 过滤jar中的依赖
     */
    protected CodeGenFilter<Class<?>> filterClassByLibrary = new FilterClassByLibrary();


    /**
     * 生成匹配策略
     */
    protected CodeGenMatchingStrategy genMatchingStrategy;


    static {
        ANNOTATION_PROCESSOR_MAP.put(NotNull.class, new NotNullProcessor());
        ANNOTATION_PROCESSOR_MAP.put(Size.class, new SizeProcessor());
        ANNOTATION_PROCESSOR_MAP.put(Pattern.class, new PatternProcessor());
        RequestMappingProcessor mappingProcessor = new RequestMappingProcessor();
        ANNOTATION_PROCESSOR_MAP.put(RequestMapping.class, mappingProcessor);
        ANNOTATION_PROCESSOR_MAP.put(GetMapping.class, mappingProcessor);
        ANNOTATION_PROCESSOR_MAP.put(PostMapping.class, mappingProcessor);
        ANNOTATION_PROCESSOR_MAP.put(DeleteMapping.class, mappingProcessor);
        ANNOTATION_PROCESSOR_MAP.put(PutMapping.class, mappingProcessor);
        ANNOTATION_PROCESSOR_MAP.put(PatchMapping.class, mappingProcessor);
    }

    public AbstractLanguageParser(PackageMapStrategy packageMapStrategy,
                                  CodeGenMatchingStrategy genMatchingStrategy,
                                  Collection<CodeDetect> codeDetects) {
        this.packageMapStrategy = packageMapStrategy;
        this.genMatchingStrategy = genMatchingStrategy;
        this.codeDetects = codeDetects;
    }

    public AbstractLanguageParser(GenericParser<JavaClassMeta, Class<?>> javaParser,
                                  PackageMapStrategy packageMapStrategy,
                                  CodeGenMatchingStrategy genMatchingStrategy,
                                  Collection<CodeDetect> codeDetects) {
        this(packageMapStrategy, genMatchingStrategy, codeDetects);
        this.javaParser = javaParser;
    }


    /**
     * 检查代码是否服务自定义的规则
     *
     * @param source
     */
    protected void detectJavaCode(JavaClassMeta source) {

        //尝试检查代码
        if (this.codeDetects == null) {
            return;
        }

        this.codeDetects.forEach(codeDetect -> {
            codeDetect.detect(source);
        });
    }

    /**
     * 从缓存中获取解过
     *
     * @param clazz
     * @return
     */
    protected C getResultToLocalCache(Class<?> clazz) {

        return (C) HANDLE_RESULT_CACHE.get(clazz);
    }

    /**
     * 通过注解获取注释
     *
     * @param annotations
     * @return
     */
    protected List<String> generateComments(Annotation[] annotations) {
        if (annotations == null || annotations.length == 0) {
            return new ArrayList<>();
        }

        return Arrays.stream(annotations).map(annotation -> {
            //将javax的验证注解转换为注释
            AnnotationProcessor processor = ANNOTATION_PROCESSOR_MAP.get(annotation.annotationType());
            if (processor == null) {
                return null;
            }

            return processor.process(annotation).toComment();
        }).filter(StringUtils::hasText)
                .collect(Collectors.toList());
    }

    /**
     * 通过类类型来获取注释
     *
     * @param classes
     * @param isMethod
     * @return
     */
    protected List<String> generateComments(Class<?>[] classes, boolean isMethod) {
        if (classes == null || classes.length == 0) {
            return new ArrayList<>();
        }


        return Arrays.stream(classes).map(clazz -> (isMethod ? "返回值" : "") + "在java中的类型为：" + clazz.getSimpleName()).collect(Collectors.toList());
    }


    /**
     * 将class列表装换为名称字符串
     *
     * @param classes
     * @return
     */
    protected String classToNamedString(Class<?>[] classes) {
        if (classes == null) {
            return "";
        }

        return Arrays.stream(classes).map(Class::getName).collect(Collectors.joining(","));
    }

    /**
     * 转换注解列表
     *
     * @param annotations
     * @param annotationOwner 注解持有者
     * @return
     */
    protected CommonCodeGenAnnotation[] converterAnnotations(Annotation[] annotations, Object annotationOwner) {
        if (annotations == null || annotations.length == 0) {
            return new CommonCodeGenAnnotation[]{};
        }

        return Arrays.stream(annotations).map(annotation -> {
            AnnotationProcessor<AnnotationMate, Annotation> processor = ANNOTATION_PROCESSOR_MAP.get(annotation.annotationType());
            if (processor == null) {
                return null;
            }

            AnnotationMate annotationMate = processor.process(annotation);
            CommonCodeGenAnnotation toAnnotation = annotationMate.toAnnotation();

            if (toAnnotation != null) {
                this.enhancedProcessingAnnotation(toAnnotation, annotationMate, annotationOwner);
            }

            return toAnnotation;
        }).filter(Objects::nonNull)
                .toArray(CommonCodeGenAnnotation[]::new);
    }


    /**
     * 转换属性列表
     *
     * @param javaFieldMetas
     * @param classMeta
     * @return
     */
    protected abstract F[] converterFieldMetas(JavaFieldMeta[] javaFieldMetas, JavaClassMeta classMeta);


    /**
     * 增强处理 filed
     *
     * @param fieldMeta
     * @param javaFieldMeta
     * @param classMeta
     */
    protected abstract void enhancedProcessingField(F fieldMeta, JavaFieldMeta javaFieldMeta, JavaClassMeta classMeta);


    /**
     * 转换方法列表
     *
     * @param javaMethodMetas
     * @param classMeta
     * @return
     */
    protected abstract M[] converterMethodMetas(JavaMethodMeta[] javaMethodMetas, JavaClassMeta classMeta);


    /**
     * 增强处理 method
     *
     * @param methodMeta
     * @param javaMethodMeta
     * @param classMeta
     */
    protected abstract void enhancedProcessingMethod(M methodMeta, JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta);


    /**
     * 增强处理 annotation
     *
     * @param codeGenAnnotation
     * @param annotation
     * @param annotationOwner
     */
    protected abstract void enhancedProcessingAnnotation(CommonCodeGenAnnotation codeGenAnnotation, AnnotationMate annotation, Object annotationOwner);

    /**
     * 抓取依赖列表
     *
     * @param dependencies
     * @return
     */
    protected Map<String, C> fetchDependencies(Set<Class<?>> dependencies) {
        if (dependencies == null || dependencies.size() == 0) {
            return new HashMap<>();
        }

        return dependencies.stream()
                .filter(this.filterClassByLibrary::filter)
                .filter(Objects::nonNull)
                .map(this::parse)
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(CommonBaseMeta::getName, v -> v));
    }

    /**
     * 解析超类
     *
     * @param clazz
     * @return
     */
    protected C parseSupper(Class<?> clazz) {

        return this.parse(clazz);
    }

}
