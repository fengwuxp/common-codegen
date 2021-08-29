package com.wuxp.codegen.core.event;

import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import org.springframework.context.ApplicationListener;

import java.util.Collections;
import java.util.Set;

public interface CodeGenEventListener extends ApplicationListener<CodeGenEvent> {

    String EVENT_CODEGEN_META_TAG_NAME = "eventGenMeta";

    String TEMPLATE_PATH_TAG_NAME = "codegenTemplatePath";


    default Set<CommonCodeGenClassMeta> getEventCodeGenMetas() {
        return Collections.emptySet();
    }
}
