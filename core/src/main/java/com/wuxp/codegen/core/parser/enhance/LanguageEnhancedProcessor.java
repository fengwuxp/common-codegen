package com.wuxp.codegen.core.parser.enhance;

import com.wuxp.codegen.core.CodeGenMatcher;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * 解析元数据时的增强处理 增强处理
 *
 * @author wxup
 */
public interface LanguageEnhancedProcessor<C extends CommonCodeGenClassMeta, M extends CommonCodeGenMethodMeta, F extends CommonCodeGenFiledMeta> {

    /**
     * 增强处理 class
     *
     * @param classMeta     用于生成的类元数据
     * @param javaClassMeta java class meta
     * @return 用于生成的类元数据
     */
    default C enhancedProcessingClass(C classMeta, JavaClassMeta javaClassMeta) {
        return classMeta;
    }

    /**
     * 增强处理方法
     *
     * @param methodMeta     用于生成的方法元数据
     * @param javaMethodMeta java method meta
     * @param classMeta      java class meta
     * @return 用于生成的类元数据
     */
    default M enhancedProcessingMethod(M methodMeta, JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta) {
        return methodMeta;
    }


    /**
     * 增强处理 filed
     *
     * @param fieldMeta     用于生成的field元数据
     * @param javaFieldMeta java class meta
     * @param classMeta     java class meta
     * @return 用于生成的field元数据
     */
    default F enhancedProcessingField(F fieldMeta, JavaFieldMeta javaFieldMeta, JavaClassMeta classMeta) {
        return fieldMeta;
    }

    /**
     * 增强处理 filed
     *
     * @param codeGenAnnotation 用于生成的注解元数据
     * @param annotation        java Annotation meta
     * @param annotationOwner   注解owner
     * @return 用于生成的注解元数据
     */
    default CommonCodeGenAnnotation enhancedProcessingAnnotation(CommonCodeGenAnnotation codeGenAnnotation, Annotation annotation,
                                                                 Object annotationOwner) {
        return codeGenAnnotation;
    }

    /**
     * 设置匹配器链
     *
     * @param codeGenMatchers 代码生成匹配器
     */
    default void setCodeGenMatchers(List<CodeGenMatcher> codeGenMatchers) {
    }

    static <C extends CommonCodeGenClassMeta, M extends CommonCodeGenMethodMeta, F extends CommonCodeGenFiledMeta> LanguageEnhancedProcessor<C, M, F> getNoneInstance() {
        return new LanguageEnhancedProcessor<C, M, F>() {
        };
    }


}
