package com.wuxp.codegen.model;


import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 通用的代码生成 method 元数据
 *
 * @author wuxp
 */
@EqualsAndHashCode(exclude = {"declaringClassMeta", "returnTypes", "params", "typeVariables", "paramAnnotations"}, callSuper = true)
@Data
@Accessors(chain = true)
public class CommonCodeGenMethodMeta extends CommonBaseMeta {

    /**
     * 原目标 Method
     */
    private Method source;

    private JavaMethodMeta javaMethodMeta;

    /**
     * 方法所在的类
     */
    private JavaClassMeta declaringClassMeta;

    /**
     * 类型参数, 泛型
     */
    protected CommonCodeGenClassMeta[] typeVariables;

    /**
     * 返回值类型 大于一个表示有泛型泛型
     */
    private CommonCodeGenClassMeta[] returnTypes;

    /**
     * 参数列表
     *
     * @key 参数名称
     * @value 参数类型描述
     */
    private Map<String, CommonCodeGenClassMeta> params;

    /**
     * 方法依赖的类对象
     */
    private List<CommonCodeGenClassMeta> dependencies;

    /**
     * 参数上定义的注解
     */
    private Map<String, CommonCodeGenAnnotation[]> paramAnnotations;

    /**
     * 注解
     */
    private CommonCodeGenAnnotation[] annotations;

    /**
     * @param name 参数名称
     * @return 是否需要该参数
     */
    public boolean isRequiredParameter(String name) {
        CommonCodeGenClassMeta parameter = getParameter(name);
        if (parameter == null) {
            return false;
        }
        if (isCollectionMapParams(parameter)) {
            return true;
        }

        return !ObjectUtils.isEmpty(parameter.getFieldMetas());
    }

    /**
     * @param name 参数名称
     * @return 是否为可选参数
     */
    public boolean isOptionalParameter(String name) {
        CommonCodeGenClassMeta parameter = getParameter(name);
        if (parameter == null) {
            return true;
        }

        if (isCollectionMapParams(parameter)) {
            return false;
        }

        if (ObjectUtils.isEmpty(parameter.getFieldMetas())) {
            return true;
        }

        // 参数中所有的字段都不是必填
        return Arrays.stream(parameter.getFieldMetas())
                .noneMatch(filedMeta -> Boolean.TRUE.equals(filedMeta.getRequired()));
    }

    /**
     * @param name 参数名称
     * @return 参数类型名称
     */
    public String getParameterTypeName(String name) {
        CommonCodeGenClassMeta parameter = getParameter(name);
        if (parameter == null) {
            return "";
        }
        return parameter.getFinallyClassName();
    }

    private CommonCodeGenClassMeta getParameter(String name) {
        if (CollectionUtils.isEmpty(params)) {
            return null;
        }
        return params.get(name);
    }


    /**
     * 是否为 集合 或 Map 参数
     */
    private boolean isCollectionMapParams(CommonCodeGenClassMeta parameter) {
        Class<?> clazz = parameter.getSource();
        if (clazz == null) {
            return false;
        }
        return JavaTypeUtils.isCollection(clazz) || JavaTypeUtils.isArrayMark(clazz) || JavaTypeUtils.isMap(clazz);
    }

}
