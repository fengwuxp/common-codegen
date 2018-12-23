package com.wuxp.codegen.core;


import com.wuxp.codegen.model.CommonCodeGenClassMeta;

/**
 * 代码生成的适配器，可以根据不同的自定义规则进行代码生成
 */
public interface CodeGenAdapter {


    CommonCodeGenClassMeta codeGen(Class<?> clazz);
}
