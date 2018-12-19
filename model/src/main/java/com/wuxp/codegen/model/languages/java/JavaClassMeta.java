package com.wuxp.codegen.model.languages.java;

import com.wuxp.codegen.model.enums.ClassType;
import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 类的元数据
 */
@Data
@Builder
public class JavaClassMeta extends JavaBaseMeta {

    //属性类型 如果有泛型则有多个
    private Map<Class<?>, Type[]> types;

    //类类型 interface，class enum
    private ClassType classType;

    //当前类的 Class 实例
    private Class<?> clazz;

    //包名+类名
    private String className;


    //是否为抽象类
    private Boolean isAbstract;

    //成员变量列表
    private JavaFieldMeta[] fieldMetas;

    //成员方法列表
    private JavaMethodMeta[] methodMetas;

    //依赖类的列表
    private Set<Class<?>> dependencyList;

    //继承的接口列表
    private Class<?>[] interfaces;

    //父类
    private Class<?> superClass;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        JavaClassMeta classMeta = (JavaClassMeta) o;
        return classType == classMeta.classType &&
                Objects.equals(clazz, classMeta.clazz) &&
                Objects.equals(className, classMeta.className) &&
                Objects.equals(isAbstract, classMeta.isAbstract) &&
                Objects.equals(superClass, classMeta.superClass);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), classType, clazz, className, isAbstract, superClass);
    }
}
