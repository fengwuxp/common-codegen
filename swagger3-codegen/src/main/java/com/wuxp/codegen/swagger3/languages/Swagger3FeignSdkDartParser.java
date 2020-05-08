package com.wuxp.codegen.swagger3.languages;

import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.languages.AbstractDartParser;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.languages.dart.DartClassMeta;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 基于 open api3 生成 feign sdk的 dart的 parser
 */
@Slf4j
public class Swagger3FeignSdkDartParser extends AbstractDartParser {




    public Swagger3FeignSdkDartParser(PackageMapStrategy packageMapStrategy,
                                      CodeGenMatchingStrategy genMatchingStrategy,
                                      Collection<CodeDetect> codeDetects,
                                       Map<Class<?>, List<String>> ignoreFields) {
        super(packageMapStrategy, genMatchingStrategy, codeDetects,ignoreFields);

    }


}
