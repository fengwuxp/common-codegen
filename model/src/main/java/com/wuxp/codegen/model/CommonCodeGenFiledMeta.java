package com.wuxp.codegen.model;


import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Arrays;


/**
 * 通用的代码生成 filed 元数据
 *
 * @author wuxp
 */
@Data
@Accessors(chain = true)
public class CommonCodeGenFiledMeta extends CommonBaseMeta {


    /**
     * 域对象类型列表
     * 大于一个表示有泛型泛型
     */
    protected CommonCodeGenClassMeta[] filedTypes;

    /**
     * 注解
     */
    protected CommonCodeGenAnnotation[] annotations;

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
        CommonCodeGenFiledMeta that = (CommonCodeGenFiledMeta) o;
        return Arrays.equals(filedTypes, that.filedTypes) &&
                Arrays.equals(annotations, that.annotations) &&
                Arrays.equals(typeVariables, that.typeVariables);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + Arrays.hashCode(filedTypes);
        result = 31 * result + Arrays.hashCode(annotations);
        result = 31 * result + Arrays.hashCode(typeVariables);
        return result;
    }
}
