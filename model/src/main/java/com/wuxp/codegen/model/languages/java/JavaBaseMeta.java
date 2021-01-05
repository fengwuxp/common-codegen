package com.wuxp.codegen.model.languages.java;

import com.wuxp.codegen.model.CommonBaseMeta;
import lombok.Data;
import lombok.experimental.Accessors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @author wuxp
 */
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

        if (this.annotations == null) {
            return Collections.EMPTY_LIST.stream();
        }

        return Arrays.stream(this.annotations)
                .filter(a -> a.annotationType().equals(clazz))
                .map(c -> (T) c);
    }

    public <T extends Annotation> T getAnnotation(Class<T> clazz) {
        List<T> collects = this.findAnnotation(clazz).collect(Collectors.toList());
        return collects.size() > 0 ? collects.get(0) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        JavaBaseMeta that = (JavaBaseMeta) o;
        return Arrays.equals(annotations, that.annotations) &&
                Arrays.equals(typeVariables, that.typeVariables) &&
                Objects.equals(typeVariableNum, that.typeVariableNum);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), typeVariableNum);
        result = 31 * result + Arrays.hashCode(annotations);
        result = 31 * result + Arrays.hashCode(typeVariables);
        return result;
    }
}
