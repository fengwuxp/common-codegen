package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.languages.factory.TypescriptLanguageMetaInstanceFactory;
import com.wuxp.codegen.mapping.TypescriptTypeMapping;
import com.wuxp.codegen.model.CommonBaseMeta;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptFieldMate;
import com.wuxp.codegen.model.utils.JavaTypeUtil;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

import static com.wuxp.codegen.model.mapping.AbstractTypeMapping.customizeJavaTypeMapping;


/**
 * 抽象的typescript parser
 */
@Slf4j
public abstract class AbstractTypescriptParser extends AbstractLanguageParser<TypescriptClassMeta, CommonCodeGenMethodMeta, TypescriptFieldMate> {


    public AbstractTypescriptParser(PackageMapStrategy packageMapStrategy,
                                    CodeGenMatchingStrategy genMatchingStrategy,
                                    Collection<CodeDetect> codeDetects) {
        super(new TypescriptLanguageMetaInstanceFactory(),
                packageMapStrategy,
                genMatchingStrategy,
                codeDetects);
        this.typeMapping = new TypescriptTypeMapping(this);
    }

    {

        //根据java 类进行匹配
        codeGenMatchers.add(clazz -> clazz.isEnum() || JavaTypeUtil.isNoneJdkComplex(clazz) || clazz.isAnnotation());
    }

    @Override
    protected TypescriptFieldMate converterField(JavaFieldMeta javaFieldMeta, JavaClassMeta classMeta) {

        TypescriptFieldMate typescriptFieldMate = super.converterField(javaFieldMeta, classMeta);

        if (typescriptFieldMate == null) {
            return null;
        }

        //是否必填
        typescriptFieldMate.setRequired(javaFieldMeta.existAnnotation(NotNull.class, NotBlank.class, NotEmpty.class));

//        this.enhancedProcessingField(typescriptFieldMate, javaFieldMeta, classMeta);

        return typescriptFieldMate;
    }


    @Override
    protected CommonCodeGenMethodMeta converterMethod(JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta, TypescriptClassMeta codeGenClassMeta) {
        CommonCodeGenMethodMeta commonCodeGenMethodMeta = super.converterMethod(javaMethodMeta, classMeta, codeGenClassMeta);
        if (commonCodeGenMethodMeta == null) {
            return null;
        }

        //处理返回值
        List<TypescriptClassMeta> mapping = this.typeMapping.mapping(javaMethodMeta.getReturnType());

        if (!mapping.contains(TypescriptClassMeta.PROMISE)) {
            mapping.add(0, TypescriptClassMeta.PROMISE);
        }

        if (mapping.size() > 0) {
            //域对象类型描述
            commonCodeGenMethodMeta.setReturnTypes(mapping.toArray(new CommonCodeGenClassMeta[]{}));
        } else {
            //解析失败
            throw new RuntimeException(String.format("解析类 %s 上的方法 %s 的返回值类型 %s 失败",
                    classMeta.getClassName(),
                    javaMethodMeta.getName(),
                    this.classToNamedString(javaMethodMeta.getReturnType())));
        }


        //增强处理
        this.enhancedProcessingMethod(commonCodeGenMethodMeta, javaMethodMeta, classMeta);

        return commonCodeGenMethodMeta;
    }


}
