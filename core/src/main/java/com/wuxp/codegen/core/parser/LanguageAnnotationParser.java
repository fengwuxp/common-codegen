package com.wuxp.codegen.core.parser;

import com.wuxp.codegen.model.CommonCodeGenAnnotation;

import java.lang.reflect.AnnotatedElement;

public interface LanguageAnnotationParser {

    CommonCodeGenAnnotation[] parseAnnotatedElement(AnnotatedElement annotationOwner);
}
