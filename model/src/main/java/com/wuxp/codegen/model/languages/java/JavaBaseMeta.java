package com.wuxp.codegen.model.languages.java;

import com.wuxp.codegen.model.CommonBaseMeta;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.stream.Collectors;


@Data
public class JavaBaseMeta extends CommonBaseMeta {


    //注解
    protected Annotation[] annotations;


    public boolean hasAnnotation(Annotation annotation) {

        return this.findAnnotation(annotation).length > 0;
    }

    public Annotation[] findAnnotation(Annotation annotation) {

        return Arrays.stream(this.annotations)
                .filter(a -> a.equals(annotation))
                .collect(Collectors.toList())
                .toArray(new Annotation[]{});
    }

    public Annotation getAnnotation(Annotation annotation) {
        Annotation[] annotations = this.findAnnotation(annotation);
        return annotations.length > 0 ? annotations[0] : null;
    }

}
