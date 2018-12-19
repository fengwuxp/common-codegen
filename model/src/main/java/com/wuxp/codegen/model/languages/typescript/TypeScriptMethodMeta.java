package com.wuxp.codegen.model.languages.typescript;

import lombok.Builder;
import lombok.Data;


import java.util.Map;

/**
 * typescript成员方法描述
 */
@Data
@Builder
public class TypeScriptMethodMeta extends TypeScriptBaseMeta {


    //是否为抽象方法
    private Boolean isAbstract;

    //返回值描述
    private String returnType;

    //参数列表 返回的是LinkHashMap
    private Map<String/*参数名称*/, TypeScriptClassMeta[]> params;

}
