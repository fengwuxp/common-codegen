package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.parser.GenericParser;
import com.wuxp.codegen.core.parser.JavaClassParser;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.annotation.processor.AnnotationProcessor;
import com.wuxp.codegen.annotation.processor.javax.NotNullProcessor;
import com.wuxp.codegen.annotation.processor.javax.PatternProcessor;
import com.wuxp.codegen.annotation.processor.javax.SizeProcessor;
import lombok.extern.slf4j.Slf4j;

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
public abstract class AbstractLanguageParser<C, M, F> implements GenericParser<C, JavaClassMeta> {

    protected GenericParser<JavaClassMeta, Class<?>> javaParser = new JavaClassParser(true);


    /**
     * 已经被处理过的class的缓存
     * key    class
     * value  处理结果
     */
    protected static final Map<Class<?>, JavaClassMeta> HANDLE_CLASS_CACHE = new ConcurrentHashMap<>();


    /**
     * annotationProcessorMap
     */
    protected static final Map<Class<? extends Annotation>, AnnotationProcessor> ANNOTATION_PROCESSOR_MAP = new LinkedHashMap<>();


    static {
        ANNOTATION_PROCESSOR_MAP.put(NotNull.class, new NotNullProcessor());
        ANNOTATION_PROCESSOR_MAP.put(Size.class, new SizeProcessor());
        ANNOTATION_PROCESSOR_MAP.put(Pattern.class, new PatternProcessor());
    }

    /**
     * 处理依赖列表
     *
     * @param dependencies
     * @return
     */
    protected C handleDependencies(Set<Class<?>> dependencies) {

        if (dependencies == null || dependencies.size() == 0) {
            return null;
        }

        dependencies.stream().map(clazz -> {
            JavaClassMeta meta = HANDLE_CLASS_CACHE.get(clazz);
            if (meta == null) {
                //判断是否被处理过，解决循环依赖的问题

                meta = this.javaParser.parse(clazz);
                HANDLE_CLASS_CACHE.put(clazz, meta);
            }
            return meta;

        }).filter(Objects::nonNull)
                .map(this::parse);


        return null;
    }

    /**
     * 通过注解获取注释
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
        }).filter(Objects::nonNull).collect(Collectors.toList());

    }

    /**
     * 转换属性列表
     *
     * @param javaFieldMetas
     * @return
     */
    protected abstract F[] converterFieldMetas(JavaFieldMeta[] javaFieldMetas);

    /**
     * 转换方法列表
     *
     * @param javaMethodMetas
     * @return
     */
    protected abstract M[] converterMethodMetas(JavaMethodMeta[] javaMethodMetas);


    /**
     * 抓取依赖列表
     *
     * @param classes
     * @return
     */
    protected abstract Set<C> fetchDependencies(Set<Class<?>> classes);

    /**
     * 解析超类
     *
     * @param clazz
     * @return
     */
    protected C parseSupper(Class<?> clazz) {

        return this.parse(this.javaParser.parse(clazz));
    }
}
