package com.wuxp.codegen.model.languages.java;

import com.wuxp.codegen.model.CommonBaseMeta;
import com.wuxp.codegen.model.enums.AccessPermission;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;


/**
 * @author wuxp
 */
@Getter
@Setter
@Accessors(chain = true)
public class JavaBaseMeta extends CommonBaseMeta {


    //注解
    protected Annotation[] annotations;

    /**
     * 类型参数, 泛型
     */
    protected Type[] typeVariables;


    protected JavaBaseMeta() {
    }

    protected JavaBaseMeta(int modifiers) {
        this.setAccessPermission(AccessPermission.valueOfModifiers(modifiers));
        this.setIsStatic(Modifier.isStatic(modifiers));
        this.setIsFinal(Modifier.isFinal(modifiers));
    }


    /**
     * 是否有列表中的某个annotation
     *
     * @param classes 注解列表
     * @return if return <code>true</code> 表示注解存在
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

        return this.findAnnotation(clazz).isPresent();
    }

    private <T extends Annotation> Optional<T> findAnnotation(Class<T> clazz) {

        if (this.annotations == null) {
            return Optional.empty();
        }

        return Arrays.stream(this.annotations)
                .filter(a -> a.annotationType().equals(clazz))
                .map(c -> (T) c)
                .findFirst();
    }

    public <T extends Annotation> Optional<T> getAnnotation(Class<T> clazz) {
        return this.findAnnotation(clazz);
    }

    /**
     * 获取泛型参数的个数
     */
    public int getTypeVariableNum() {
        return typeVariables == null ? 0 : typeVariables.length;
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
                Objects.equals(getTypeVariableNum(), that.getTypeVariableNum());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), getTypeVariableNum());
        result = 31 * result + Arrays.hashCode(annotations);
        result = 31 * result + Arrays.hashCode(typeVariables);
        return result;
    }
}
