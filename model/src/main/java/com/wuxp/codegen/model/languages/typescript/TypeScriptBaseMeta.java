package com.wuxp.codegen.model.languages.typescript;

import com.wuxp.codegen.model.CommonBaseMeta;
import lombok.Data;

import java.util.Map;


/**
 * typeScript meta
 */
@Data
public class TypeScriptBaseMeta extends CommonBaseMeta {



    //注解
    protected Map<String/*注解*/, TypeScriptDecorator> annotations;


}
