package com.wuxp.codegen.core;

import com.wuxp.codegen.core.config.CodegenConfigHolder;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;

/**
 * 匹配 api service class
 *
 * @author wuxp
 */
public interface ApiServiceClassMatcher {


    /**
     * 是否为一个api service 需要进行生成
     *
     * @param javaClassMeta java 类解析后的元数据
     * @return if return <code>true</code>表示为 api server class
     */
    @SuppressWarnings("unchecked")
    default boolean isApiServiceClass(JavaClassMeta javaClassMeta) {
        if (javaClassMeta == null) {
            return false;
        }
        return javaClassMeta.existAnnotation(CodegenConfigHolder.getConfig().getApiMarkedAnnotations().toArray(new Class[0]));
    }
}
