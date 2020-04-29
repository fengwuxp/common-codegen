package com.wuxp.codegen.languages;


import com.wuxp.codegen.annotation.processor.AnnotationMate;
import com.wuxp.codegen.annotation.processor.spring.*;
import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.parser.GenericParser;
import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.languages.factory.DartLanguageMetaInstanceFactory;
import com.wuxp.codegen.languages.factory.JavaLanguageMetaInstanceFactory;
import com.wuxp.codegen.mapping.DartTypeMapping;
import com.wuxp.codegen.mapping.JavaTypeMapping;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.dart.DartClassMeta;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 抽象的dart parser
 */
@Slf4j
public class AbstractDartParser extends AbstractLanguageParser<DartClassMeta, CommonCodeGenMethodMeta, CommonCodeGenFiledMeta> {


    static {
        ANNOTATION_PROCESSOR_MAP.put(CookieValue.class, new CookieValueProcessor());
        ANNOTATION_PROCESSOR_MAP.put(RequestBody.class, new RequestBodyProcessor());
        ANNOTATION_PROCESSOR_MAP.put(RequestHeader.class, new RequestHeaderProcessor());
        ANNOTATION_PROCESSOR_MAP.put(RequestParam.class, new RequestParamProcessor());
        ANNOTATION_PROCESSOR_MAP.put(RequestPart.class, new RequestPartProcessor());
    }

    public AbstractDartParser(PackageMapStrategy packageMapStrategy,
                              CodeGenMatchingStrategy genMatchingStrategy,
                              Collection<CodeDetect> codeDetects) {
        this(null,
                new DartLanguageMetaInstanceFactory(),
                packageMapStrategy,
                genMatchingStrategy,
                codeDetects);
        this.typeMapping = new DartTypeMapping(this);
    }

    public AbstractDartParser(PackageMapStrategy packageMapStrategy,
                              CodeGenMatchingStrategy genMatchingStrategy,
                              Collection<CodeDetect> codeDetects,
                              boolean useAsync) {
        this(null,
                new DartLanguageMetaInstanceFactory(),
                packageMapStrategy,
                genMatchingStrategy,
                codeDetects);
        this.typeMapping = new DartTypeMapping(this);
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

    public AbstractDartParser(GenericParser<JavaClassMeta, Class<?>> javaParser,
                              LanguageMetaInstanceFactory<DartClassMeta, CommonCodeGenMethodMeta, CommonCodeGenFiledMeta> languageMetaInstanceFactory,
                              PackageMapStrategy packageMapStrategy,
                              CodeGenMatchingStrategy genMatchingStrategy,
                              Collection<CodeDetect> codeDetects) {
        super(javaParser, languageMetaInstanceFactory, packageMapStrategy, genMatchingStrategy, codeDetects);
        this.typeMapping = new DartTypeMapping(this);

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
    protected void enhancedProcessingField(CommonCodeGenFiledMeta fieldMeta, JavaFieldMeta javaFieldMeta, JavaClassMeta classMeta) {

    }

    @Override
    protected void enhancedProcessingClass(DartClassMeta methodMeta, JavaClassMeta classMeta) {

    }

    @Override
    protected void enhancedProcessingAnnotation(CommonCodeGenAnnotation codeGenAnnotation, AnnotationMate annotation, Object annotationOwner) {

    }

    @Override
    protected CommonCodeGenMethodMeta converterMethod(JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta, DartClassMeta codeGenClassMeta) {

        if (classMeta == null) {
            return null;
        }

        CommonCodeGenMethodMeta genMethodMeta = this.languageMetaInstanceFactory.newMethodInstance();
        //method转换
        genMethodMeta.setAccessPermission(classMeta.getAccessPermission());
        //注解转注释
        List<String> comments = this.generateComments(javaMethodMeta.getAnnotations(), javaMethodMeta.getMethod());
        comments.addAll(this.generateComments(javaMethodMeta.getReturnType(), true));
        genMethodMeta.setComments(comments.toArray(new String[]{}));
        genMethodMeta.setName(javaMethodMeta.getName());

        //处理方法上的相关注解
        genMethodMeta.setAnnotations(this.converterAnnotations(javaMethodMeta.getAnnotations(), javaMethodMeta.getMethod()));


        //处理方法的参数
        //1: 参数过滤（过滤掉控制器方法中servlet相关的参数等等）
        Map<String, Class<?>[]> methodMetaParams = javaMethodMeta.getParams();
        //有效的参数
        final Map<String, Class<?>[]> effectiveParams = new LinkedHashMap<>();
        methodMetaParams.forEach((key, classes) -> {
            Class<?>[] array = Arrays.stream(classes)
                    .filter(this.packageNameCodeGenMatcher::match)
                    .toArray(Class<?>[]::new);
            if (array.length == 0) {
                return;
            }
            effectiveParams.put(key, array);
        });


        // 处理注解
        Map<String, Annotation[]> paramAnnotations = javaMethodMeta.getParamAnnotations();


        //处理返回值
        return genMethodMeta;
    }

    @Override
    protected void enhancedProcessingMethod(CommonCodeGenMethodMeta methodMeta, JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta) {

    }


}
