package com.wuxp.codegen.languages.java;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.config.CodegenConfigHolder;
import com.wuxp.codegen.core.parser.enhance.LanguageDefinitionPostProcessor;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wuxp
 */
public class JavaCodeGenMetaPostProcessor implements LanguageDefinitionPostProcessor<JavaCodeGenClassMeta> {

    /**
     * 使用异步的方式
     */
    private final boolean useRxJava;

    public JavaCodeGenMetaPostProcessor(boolean useRxJava) {
        this.useRxJava = useRxJava;
    }

    @Override
    public void postProcess(JavaCodeGenClassMeta meta) {
        Arrays.asList(meta.getMethodMetas()).forEach(this::postMethodMeta);
    }

    private void postMethodMeta(CommonCodeGenMethodMeta methodMeta) {
        List<CommonCodeGenClassMeta> returnTypes = addRxJavaReturnTypes(methodMeta.getReturnTypes());
        methodMeta.setReturnTypes(returnTypes.toArray(new CommonCodeGenClassMeta[0]));
    }

    private List<CommonCodeGenClassMeta> addRxJavaReturnTypes(CommonCodeGenClassMeta[] returnTypes) {
        List<CommonCodeGenClassMeta> results = new ArrayList<>(Arrays.asList(returnTypes));
        if (!ClientProviderType.RETROFIT.equals(CodegenConfigHolder.getConfig().getProviderType())) {
            return results;
        }
        // 使用异步处理
        if (this.useRxJava && !results.contains(JavaCodeGenClassMeta.RX_JAVA2_OBSERVABLE)) {
            results.add(0, JavaCodeGenClassMeta.RX_JAVA2_OBSERVABLE);
        }
        return results;
    }
}
