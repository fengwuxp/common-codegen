package com.wuxp.codegen.swagger2.languages;

import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.core.strategy.PackageNameConvertStrategy;
import com.wuxp.codegen.languages.AbstractJavaParser;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;


/**
 * 基于swagger2 生成 feign sdk的 java的 parser
 * @author wuxp
 */
@Slf4j
public class Swagger2FeignSdkJavaParser extends AbstractJavaParser {

    private final boolean enabledAndroidSqliteSupport;

    private final LanguageDescription languageDescription;

    public Swagger2FeignSdkJavaParser(PackageNameConvertStrategy packageMapStrategy,
                                      CodeGenMatchingStrategy genMatchingStrategy,
                                      Collection<CodeDetect> codeDetects,
                                      LanguageDescription languageDescription,
                                      boolean useRxJava,
                                      boolean enabledAndroidSqliteSupport) {
        super(packageMapStrategy, genMatchingStrategy, codeDetects, useRxJava);
        this.enabledAndroidSqliteSupport = enabledAndroidSqliteSupport;
        this.languageDescription = languageDescription;
    }

    @Override
    protected CommonCodeGenMethodMeta converterMethod(JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta, JavaCodeGenClassMeta codeGenClassMeta) {
//        if (!AccessPermission.PUBLIC.equals(javaMethodMeta.getAccessPermission())) {
//            return null;
//        }
//
//        if (!javaMethodMeta.existAnnotation(
//                SpringAnnotationClassConstant.SPRING_MAPPING_ANNOTATIONS
//        )) {
//            return null;
//        }
//
//        if (javaMethodMeta.existAnnotation(ApiIgnore.class)) {
//            return null;
//        }

        return super.converterMethod(javaMethodMeta, classMeta, codeGenClassMeta);
    }

    @Override
    public JavaCodeGenClassMeta parse(Class<?> source) {
        if (LanguageDescription.JAVA_ANDROID.equals(languageDescription) && this.enabledAndroidSqliteSupport) {
            // TODO  support sqlite
        }
        return super.parse(source);
    }
}
