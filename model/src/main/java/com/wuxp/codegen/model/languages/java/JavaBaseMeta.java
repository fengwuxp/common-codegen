package com.wuxp.codegen.model.languages.java;

import com.wuxp.codegen.model.CommonBaseMeta;
import lombok.Data;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Data
public class JavaBaseMeta extends CommonBaseMeta {


    //注解
    protected Annotation[] annotations;


    /**
     * 是否有列表中的某个annotation
     *
     * @param classes 注解列表
     * @return
     */
    public boolean existAnnotation(Class<? extends Annotation>... classes) {

        for (Class<? extends Annotation> clazz : classes) {
            if (this.existAnnotation(clazz)) {
                return true;
            }
        }
        return false;
    }

    public boolean existAnnotation(Class<? extends Annotation> clazz) {

        return this.findAnnotation(clazz).collect(Collectors.toList()).size() > 0;
    }

    public <T extends Annotation> Stream<T> findAnnotation(Class<T> clazz) {

        return Arrays.stream(this.annotations)
                .filter(a -> a.getClass().equals(clazz)).map(c -> (T) c);
    }

    public <T extends Annotation> T getAnnotation(Class<T> clazz) {
        List<T> collects = this.findAnnotation(clazz).collect(Collectors.toList());
        return collects.size() > 0 ? collects.get(0) : null;
    }

}
