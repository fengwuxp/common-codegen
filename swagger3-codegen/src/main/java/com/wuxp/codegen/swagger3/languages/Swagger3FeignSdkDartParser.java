package com.wuxp.codegen.swagger3.languages;

import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.core.strategy.PackageNameConvertStrategy;
import com.wuxp.codegen.languages.AbstractDartParser;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 基于 open api3 生成 feign sdk的 dart的 parser
 *
 * @author wxup
 */
@Slf4j
public class Swagger3FeignSdkDartParser extends AbstractDartParser {


    public Swagger3FeignSdkDartParser(PackageNameConvertStrategy packageMapStrategy,
                                      CodeGenMatchingStrategy genMatchingStrategy,
                                      Collection<CodeDetect> codeDetects,
                                      Map<Class<?>, List<String>> ignoreFields) {
        super(packageMapStrategy, genMatchingStrategy, codeDetects, ignoreFields);

    }


}
