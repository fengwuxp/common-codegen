package com.wuxp.codegen.swagger2.languages;

import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.parser.GenericParser;
import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.languages.AbstractJavaParser;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;


/**
 * 基于swagger2 生成 feign sdk的 java的 parser
 */
@Slf4j
public class Swagger2FeignSdkJavaParser extends AbstractJavaParser {


    public Swagger2FeignSdkJavaParser(PackageMapStrategy packageMapStrategy,
                                      CodeGenMatchingStrategy genMatchingStrategy,
                                      Collection<CodeDetect> codeDetects) {
        super(packageMapStrategy, genMatchingStrategy, codeDetects);
    }
}
