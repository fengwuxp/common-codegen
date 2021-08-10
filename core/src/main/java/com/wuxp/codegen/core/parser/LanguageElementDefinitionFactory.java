package com.wuxp.codegen.core.parser;

import com.wuxp.codegen.model.CommonBaseMeta;

/**
 * @author wuxp
 */
public interface LanguageElementDefinitionFactory<C extends CommonBaseMeta> {

    /**
     * @return 实例化一个 Element Meta Object
     */
   default C newElementInstance(){
       throw new UnsupportedOperationException("un support newElementInstance");
   }
}
