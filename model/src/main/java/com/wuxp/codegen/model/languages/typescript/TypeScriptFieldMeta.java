package com.wuxp.codegen.model.languages.typescript;

import lombok.Builder;
import lombok.Data;


/**
 * typescript成员变量描述
 */
@Data
@Builder
public class TypeScriptFieldMeta extends TypeScriptBaseMeta {


    //是否必须存在
    private Boolean required;

    //是否只读
    private Boolean readonly;

    //类型
    private TypeScriptClassMeta classType;

    //在有泛型时候的类类型名称
    private String genericClassName;

}
