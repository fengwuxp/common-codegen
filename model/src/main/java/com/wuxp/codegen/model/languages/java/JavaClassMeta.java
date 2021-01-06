package com.wuxp.codegen.model.languages.java;

import com.wuxp.codegen.model.MatchApiServiceClass;
import com.wuxp.codegen.model.enums.ClassType;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 类的元数据
 *
 * @author wuxp
 */
@Data
@Accessors(chain = true)
public class JavaClassMeta extends JavaBaseMeta implements MatchApiServiceClass {

    /**
     * 属于api 服务的注解
     */
    private static final Set<Class<? extends Annotation>> API_SERVICE_ANNOTATIONS = new LinkedHashSet<>();

    static {
        JavaClassMeta.API_SERVICE_ANNOTATIONS.add(Controller.class);
        JavaClassMeta.API_SERVICE_ANNOTATIONS.add(RequestMapping.class);
        JavaClassMeta.API_SERVICE_ANNOTATIONS.add(RestController.class);
    }

    /**
     * 属性类型 如果有泛型则有多个
     *
     * @key 类型，父类，接口，本身
     */
    private Map<Class<?>, Class<?>[]> superTypeVariables;


    private ClassType classType;

    /**
     * 当前类的 Class 实例
     */
    private Class<?> clazz;

    /**
     * 包名+类名
     */
    private String className;

    /**
     * 是否为抽象类
     */
    private Boolean isAbstract;

    /**
     * 成员变量列表
     */
    private JavaFieldMeta[] fieldMetas;

    /**
     * 成员方法列表
     */
    private JavaMethodMeta[] methodMetas;

    /**
     * 依赖类的列表
     */
    private Set<Class<?>> dependencyList;

    /**
     * 继承的接口列表
     */
    private Class<?>[] interfaces;

    /**
     * 父类
     */
    private Class<?> superClass;


    /**
     * 是否为spring的控制器
     *
     * @return
     */
    @Override
    public boolean isApiServiceClass() {
        return this.existAnnotation(JavaClassMeta.API_SERVICE_ANNOTATIONS.toArray(new Class[]{}));
    }

    public static void addApiServiceAnnotations(Class<? extends Annotation> clazz) {
        JavaClassMeta.API_SERVICE_ANNOTATIONS.add(clazz);
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
