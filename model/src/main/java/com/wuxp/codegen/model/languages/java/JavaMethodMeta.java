package com.wuxp.codegen.model.languages.java;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * 方法元数据信息
 *
 * @author wuxp
 */
@Data
@Accessors(chain = true)
public class JavaMethodMeta extends JavaBaseMeta {


    /**
     * 所属的方法
     */
    private Method method;

    /**
     * 返回值类型，如果有泛型则有多个 void是为null
     */
    private Class<?>[] returnType;

    /**
     * 参数列表 返回的是LinkHashMap
     *
     * @key 成员方法列表
     * @value 参数类类型，多个表示有泛型
     */
    private Map<String, Class<?>[]> params;

    /**
     * 参数上的注解
     *
     * @key 参数名称
     */
    private Map<String, Annotation[]> paramAnnotations;

    /**
     * 原始的参数列表
     *
     * @key 参数名称
     */
    private Map<String, Parameter> parameters;

    /**
     * 是否为抽象方法
     */
    private Boolean isAbstract;

    /**
     * 是否为同步方法
     */
    private Boolean isSynchronized;

    /**
     * 是否为本地方法
     */
    private Boolean isNative;

    /**
     * 是否序列化
     */
    private Boolean isTransient;


    /**
     * 方法归属的类
     */
    private Class<?> owner;

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
        JavaMethodMeta that = (JavaMethodMeta) o;
        return Objects.equals(method, that.method) &&
                Arrays.equals(returnType, that.returnType) &&
                Objects.equals(params, that.params) &&
                Objects.equals(paramAnnotations, that.paramAnnotations) &&
                Objects.equals(parameters, that.parameters) &&
                Objects.equals(isAbstract, that.isAbstract) &&
                Objects.equals(isSynchronized, that.isSynchronized) &&
                Objects.equals(isNative, that.isNative) &&
                Objects.equals(isTransient, that.isTransient) &&
                Objects.equals(owner, that.owner);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), method, params, paramAnnotations, parameters, isAbstract, isSynchronized, isNative, isTransient, owner);
        result = 31 * result + Arrays.hashCode(returnType);
        return result;
    }
}
