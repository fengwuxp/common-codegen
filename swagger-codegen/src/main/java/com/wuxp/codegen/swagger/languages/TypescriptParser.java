package com.wuxp.codegen.swagger.languages;

import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.languages.AbstractLanguageParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * typeScript的
 */
@Slf4j
public class TypescriptParser extends AbstractLanguageParser<CommonCodeGenClassMeta, CommonCodeGenMethodMeta, CommonCodeGenFiledMeta> {


    protected PackageMapStrategy packageMapStrategy;

    private TypescriptParser typescriptParser;

    public TypescriptParser(PackageMapStrategy packageMapStrategy) {
        this.packageMapStrategy = packageMapStrategy;
        this.typescriptParser = new TypescriptByControllerParser(packageMapStrategy);
    }

    @Override
    public CommonCodeGenClassMeta parse(JavaClassMeta source) {

        if (source.hasAnnotation(Controller.class, RestController.class, RequestMapping.class)) {
            //控制器
            return this.typescriptParser.parse(source);
        }


        return null;
    }


    @Override
    protected CommonCodeGenFiledMeta[] converterFieldMetas(JavaFieldMeta[] javaFieldMetas) {
        return new CommonCodeGenFiledMeta[0];
    }

    @Override
    protected CommonCodeGenMethodMeta[] converterMethodMetas(JavaMethodMeta[] javaMethodMetas) {
        return new CommonCodeGenMethodMeta[0];
    }

    @Override
    protected Set<CommonCodeGenClassMeta> fetchDependencies(Set<Class<?>> classes) {
        return null;
    }


}
