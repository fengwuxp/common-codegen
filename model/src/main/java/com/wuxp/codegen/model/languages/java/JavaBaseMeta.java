package com.wuxp.codegen.model.languages.java;

import com.wuxp.codegen.model.CommonBaseMeta;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.util.Map;

@Data
public class JavaBaseMeta extends CommonBaseMeta {


    //注解
    protected Map<String/*注解*/, Annotation> annotations;



}
