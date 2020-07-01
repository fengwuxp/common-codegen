package com.wuxp.codegen.model;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

/**
 * 通用的代码生成method元数据
 *
 * @author wuxp
 */
@Data
@Accessors(chain = true)
public class CommonCodeGenMethodMeta extends CommonBaseMeta {

    /**
     * 返回值类型
     * 大于一个表示有泛型泛型
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
     * 参数上定义的注解
     */
    private Map<String, CommonCodeGenAnnotation[]> paramAnnotations;

    /**
     * 注解
     */
    private CommonCodeGenAnnotation[] annotations;

    /**
     * 类型参数, 泛型
     */
    protected CommonCodeGenClassMeta[] typeVariables;


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
        CommonCodeGenMethodMeta that = (CommonCodeGenMethodMeta) o;
        return Arrays.equals(returnTypes, that.returnTypes) &&
                Objects.equals(params, that.params) &&
                Objects.equals(paramAnnotations, that.paramAnnotations) &&
                Arrays.equals(annotations, that.annotations) &&
                Arrays.equals(typeVariables, that.typeVariables);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(super.hashCode(), params, paramAnnotations);
        result = 31 * result + Arrays.hashCode(returnTypes);
        result = 31 * result + Arrays.hashCode(annotations);
        result = 31 * result + Arrays.hashCode(typeVariables);
        return result;
    }
}
