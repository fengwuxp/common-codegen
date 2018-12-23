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


    public boolean hasAnnotation(Class<? extends Annotation>... classes) {

        for (Class<? extends Annotation> clazz : classes) {
            if (this.hasAnnotation(clazz)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasAnnotation(Class<? extends Annotation> clazz) {

        return this.findAnnotation(clazz).length > 0;
    }

    public Annotation[] findAnnotation(Class<? extends Annotation> clazz) {

        return Arrays.stream(this.annotations)
                .filter(a -> a.getClass().equals(clazz))
                .collect(Collectors.toList())
                .toArray(new Annotation[]{});
    }

    public Annotation getAnnotation(Class<? extends Annotation> clazz) {
        Annotation[] annotations = this.findAnnotation(clazz);
        return annotations.length > 0 ? annotations[0] : null;
    }

}
