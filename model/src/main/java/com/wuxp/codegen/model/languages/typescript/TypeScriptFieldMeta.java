package com.wuxp.codegen.model.languages.typescript;


import com.wuxp.codegen.model.CommonMetaBuilder;
import lombok.Data;


/**
 * typescript成员变量描述
 */
@Data
public class TypeScriptFieldMeta extends TypeScriptBaseMeta {


    //是否必须存在
    private Boolean required;

    //是否只读
    private Boolean readonly;

    //类型
    private TypeScriptClassMeta classType;

    //在有泛型时候的类类型名称
    private String genericClassName;

    private TypeScriptFieldMeta() {
    }

    public static TypeScriptFieldMetaBuilder builder() {
        return new TypeScriptFieldMetaBuilder();
    }

    public static class TypeScriptFieldMetaBuilder implements CommonMetaBuilder<TypeScriptFieldMeta> {


        @Override
        public TypeScriptFieldMeta build() {
            return null;
        }
    }

}
