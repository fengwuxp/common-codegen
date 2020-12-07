package com.wuxp.codegen.swagger2.languages;

import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.languages.AbstractDartParser;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 基于swagger2的生成 feign sdk的 dart的 parser
 * @author wuxp
 */
public class Swagger2FeignSdkDartParser extends AbstractDartParser {


    public Swagger2FeignSdkDartParser(PackageMapStrategy packageMapStrategy,
                                      CodeGenMatchingStrategy genMatchingStrategy,
                                      Collection<CodeDetect> codeDetects,
                                      Map<Class<?>, List<String>> ignoreFields) {
        super(packageMapStrategy, genMatchingStrategy, codeDetects, ignoreFields);

    }
}
