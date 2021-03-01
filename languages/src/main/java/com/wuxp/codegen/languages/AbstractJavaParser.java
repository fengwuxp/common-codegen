package com.wuxp.codegen.languages;


import com.wuxp.codegen.annotation.processors.AnnotationMate;
import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.exception.CodegenRuntimeException;
import com.wuxp.codegen.core.parser.GenericParser;
import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.languages.factory.JavaLanguageMetaInstanceFactory;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 抽象的java parser
 *
 * @author wuxp
 */
@Slf4j
public abstract class AbstractJavaParser extends
        AbstractLanguageParser<JavaCodeGenClassMeta, CommonCodeGenMethodMeta, CommonCodeGenFiledMeta> {

    /**
     * 使用异步的方式
     */
    protected boolean useAsync = false;

    public AbstractJavaParser(PackageMapStrategy packageMapStrategy,
                              CodeGenMatchingStrategy genMatchingStrategy,
                              Collection<CodeDetect> codeDetects) {
        this(null,
                new JavaLanguageMetaInstanceFactory(),
                packageMapStrategy,
                genMatchingStrategy,
                codeDetects);
    }

    public AbstractJavaParser(PackageMapStrategy packageMapStrategy,
                              CodeGenMatchingStrategy genMatchingStrategy,
                              Collection<CodeDetect> codeDetects,
                              boolean useAsync) {
        this(null,
                new JavaLanguageMetaInstanceFactory(),
                packageMapStrategy,
                genMatchingStrategy,
                codeDetects);
        this.useAsync = useAsync;
    }


    {

        codeGenMatchers.add(clazz -> {
            if (clazz == null) {
                return false;
            }
            Package aPackage = clazz.getPackage();
            if (aPackage == null) {
                return false;
            }
            return !aPackage.getName().startsWith("java.lang");

        });

    }

    public AbstractJavaParser(GenericParser<JavaClassMeta, Class<?>> javaParser,
                              LanguageMetaInstanceFactory<JavaCodeGenClassMeta, CommonCodeGenMethodMeta, CommonCodeGenFiledMeta> languageMetaInstanceFactory,
                              PackageMapStrategy packageMapStrategy,
                              CodeGenMatchingStrategy genMatchingStrategy,
                              Collection<CodeDetect> codeDetects) {
        super(javaParser, languageMetaInstanceFactory, packageMapStrategy, genMatchingStrategy, codeDetects);
    }


    @Override
    protected CommonCodeGenFiledMeta converterField(JavaFieldMeta javaFieldMeta, JavaClassMeta classMeta) {
        CommonCodeGenFiledMeta commonCodeGenFiledMeta = super.converterField(javaFieldMeta, classMeta);

        if (commonCodeGenFiledMeta == null) {
            return null;
        }

        return commonCodeGenFiledMeta;
    }

    @Override
    protected void enhancedProcessingClass(JavaCodeGenClassMeta methodMeta, JavaClassMeta classMeta) {

    }

    @Override
    protected void enhancedProcessingField(CommonCodeGenFiledMeta fieldMeta, JavaFieldMeta javaFieldMeta, JavaClassMeta classMeta) {

    }


    @Override
    protected void enhancedProcessingAnnotation(CommonCodeGenAnnotation codeGenAnnotation, AnnotationMate annotation,
                                                Object annotationOwner) {

    }

    @Override
    protected CommonCodeGenMethodMeta converterMethod(JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta,
                                                      JavaCodeGenClassMeta codeGenClassMeta) {

        CommonCodeGenMethodMeta commonCodeGenMethodMeta = super.converterMethod(javaMethodMeta, classMeta, codeGenClassMeta);
        if (commonCodeGenMethodMeta == null) {
            return null;
        }

        //处理返回值
        Class<?>[] methodMetaReturnType = javaMethodMeta.getReturnType();
        List<JavaCodeGenClassMeta> returnTypes = Arrays.stream(commonCodeGenMethodMeta.getReturnTypes()).map(returnClass -> (JavaCodeGenClassMeta) returnClass).collect(Collectors.toList());
        if (!methodReturnTypeIsFile(javaMethodMeta)) {
            returnTypes = this.languageTypeMapping.mapping(methodMetaReturnType);
        }
        if (this.useAsync) {
            // 使用异步处理
            if (!returnTypes.contains(JavaCodeGenClassMeta.RX_JAVA2_OBSERVABLE)) {
                returnTypes.add(0, JavaCodeGenClassMeta.RX_JAVA2_OBSERVABLE);
            }
        }
        if (returnTypes.isEmpty()) {
            //解析失败
            throw new CodegenRuntimeException(String.format("解析类 %s 上的方法 %s 的返回值类型 %s 失败",
                    classMeta.getClassName(),
                    javaMethodMeta.getName(),
                    this.classToNamedString(methodMetaReturnType)));
        } else {
            //域对象类型描述
            commonCodeGenMethodMeta.setReturnTypes(returnTypes.toArray(new CommonCodeGenClassMeta[]{}));

        }
        returnTypes.stream()
                .filter(CommonCodeGenClassMeta::getNeedImport)
                .forEach(returnType -> ((Map<String, JavaCodeGenClassMeta>) codeGenClassMeta.getDependencies()).put(returnType.getName(), returnType));

        //增强处理
        this.enhancedProcessingMethod(commonCodeGenMethodMeta, javaMethodMeta, classMeta);

        return commonCodeGenMethodMeta;
    }

    @Override
    protected void enhancedProcessingMethod(CommonCodeGenMethodMeta methodMeta, JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta) {

    }


}
