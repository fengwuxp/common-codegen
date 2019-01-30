package com.wuxp.codegen.dragon.model.languages.java;

import com.wuxp.codegen.dragon.model.CommonBaseMeta;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class JavaBaseMeta extends CommonBaseMeta {


    //注解
    protected Annotation[] annotations;

    /**
     * 类型参数, 泛型
     */
    protected Type[] typeVariables;

    /**
     * 类型参数的个数
     */
    protected Integer typeVariableNum = 0;


    /**
     * 是否有列表中的某个annotation
     *
     * @param classes 注解列表
     * @return
     */
    @SafeVarargs
    public final boolean existAnnotation(Class<? extends Annotation>... classes) {

        for (Class<? extends Annotation> clazz : classes) {
            if (this.existAnnotation(clazz)) {
                return true;
            }
        }
        return false;
    }

    private boolean existAnnotation(Class<? extends Annotation> clazz) {

        return this.findAnnotation(clazz).collect(Collectors.toList()).size() > 0;
    }

    private <T extends Annotation> Stream<T> findAnnotation(Class<T> clazz) {

        return Arrays.stream(this.annotations)
                .filter(a -> a.annotationType().equals(clazz))
                .map(c -> (T) c);
    }

    public <T extends Annotation> T getAnnotation(Class<T> clazz) {
        List<T> collects = this.findAnnotation(clazz).collect(Collectors.toList());
        return collects.size() > 0 ? collects.get(0) : null;
    }

}
