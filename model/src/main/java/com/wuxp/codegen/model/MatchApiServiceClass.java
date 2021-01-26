package com.wuxp.codegen.model;

import com.wuxp.codegen.model.languages.java.JavaClassMeta;

/**
 * 匹配 api service class
 *
 * @author wuxp
 */
public interface MatchApiServiceClass {


    /**
     * 是否为一个api service 需要进行生成
     *
     * @return
     */
    boolean isApiServiceClass(JavaClassMeta javaClassMeta);
}
