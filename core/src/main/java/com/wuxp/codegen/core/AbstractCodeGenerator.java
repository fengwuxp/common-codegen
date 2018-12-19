package com.wuxp.codegen.core;


import com.wuxp.codegen.core.parser.GenericParser;
import com.wuxp.codegen.core.parser.JavaClassParser;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import lombok.extern.slf4j.Slf4j;

/**
 * abstract code generator
 */
@Slf4j
public class AbstractCodeGenerator implements CodeGenerator {


    protected GenericParser<JavaClassMeta, Class<?>> javaParser = new JavaClassParser(true);


    @Override
    public void generate() {

    }
}
