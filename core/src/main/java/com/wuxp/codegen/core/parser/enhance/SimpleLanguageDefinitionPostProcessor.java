package com.wuxp.codegen.core.parser.enhance;

import com.wuxp.codegen.model.CommonBaseMeta;

/**
 * @author wuxp
 */
public interface SimpleLanguageDefinitionPostProcessor<C extends CommonBaseMeta> extends LanguageDefinitionPostProcessor<C> {

    @Override
    default C postProcess(C meta) {
        DispatchLanguageDefinitionPostProcessor<C> dispatcher = DispatchLanguageDefinitionPostProcessor.getInstance();
        return dispatcher.postProcess(meta);
    }
}
