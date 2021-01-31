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
import java.util.Arrays;
import java.util.List;

/**
 * 合并的 LanguageEnhancedProcessor
 *
 * @author wuxp
 */
public class CombineLanguageEnhancedProcessor<C extends CommonCodeGenClassMeta, M extends CommonCodeGenMethodMeta, F extends CommonCodeGenFiledMeta>
        implements LanguageEnhancedProcessor<C, M, F> {

    private final List<LanguageEnhancedProcessor> processors;

    private CombineLanguageEnhancedProcessor(List<LanguageEnhancedProcessor> processors) {
        this.processors = processors;
    }

    public static CombineLanguageEnhancedProcessor of(LanguageEnhancedProcessor... processors) {
        return new CombineLanguageEnhancedProcessor(Arrays.asList(processors));
    }

    public static CombineLanguageEnhancedProcessor of(List<LanguageEnhancedProcessor> processors) {
        return new CombineLanguageEnhancedProcessor(processors);
    }

    @Override
    public CommonCodeGenClassMeta enhancedProcessingClass(CommonCodeGenClassMeta classMeta, JavaClassMeta javaClassMeta) {
        for (LanguageEnhancedProcessor processor : processors) {
            classMeta = processor.enhancedProcessingClass(classMeta, javaClassMeta);
        }
        return classMeta;
    }

    @Override
    public CommonCodeGenMethodMeta enhancedProcessingMethod(CommonCodeGenMethodMeta methodMeta, JavaMethodMeta javaMethodMeta,
                                                            JavaClassMeta classMeta) {
        for (LanguageEnhancedProcessor processor : processors) {
            methodMeta = processor.enhancedProcessingMethod(methodMeta, javaMethodMeta, classMeta);
        }
        return methodMeta;
    }

    @Override
    public CommonCodeGenFiledMeta enhancedProcessingField(CommonCodeGenFiledMeta fieldMeta, JavaFieldMeta javaFieldMeta,
                                                          JavaClassMeta classMeta) {
        for (LanguageEnhancedProcessor processor : processors) {
            fieldMeta = processor.enhancedProcessingField(fieldMeta, javaFieldMeta, classMeta);
        }
        return fieldMeta;
    }

    @Override
    public CommonCodeGenAnnotation enhancedProcessingAnnotation(CommonCodeGenAnnotation codeGenAnnotation, Annotation annotation,
                                                                Object annotationOwner) {
        for (LanguageEnhancedProcessor processor : processors) {
            codeGenAnnotation = processor.enhancedProcessingAnnotation(codeGenAnnotation, annotation, annotationOwner);
        }
        return codeGenAnnotation;
    }

    @Override
    public void setCodeGenMatchers(List<CodeGenMatcher> codeGenMatchers) {
        for (LanguageEnhancedProcessor processor : processors) {
            processor.setCodeGenMatchers(codeGenMatchers);
        }
    }
}
