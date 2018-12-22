package com.wuxp.codegen.model.languages.typescript;

import java.util.Map;

/**
 * typescript的装饰器
 */
public class TypeScriptDecorator {

    //名称
    private String name;

    //所在包的路径
    private String packagePath;

    /**
     * 如果是对象参数 key为参数的名称
     * 如果是参数列表，key为参数的位置
     */
    private Map<String,String> params;

}
