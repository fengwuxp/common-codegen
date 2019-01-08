package com.wuxp.codegen.model;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 匹配 api service class
 */
public interface MatchApiServiceClass {

    Set<Class<? extends Annotation>> API_SERVICE_ANNOTATIONS = new LinkedHashSet<>();


    boolean isApiServiceClass();
}