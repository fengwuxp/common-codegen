package com.wuxp.codegen.spring.languages;

import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.parser.GenericParser;
import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.languages.AbstractJavaParser;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.Collection;
import java.util.Map;


/**
 * 解析 jap 实体对象
 */
@Slf4j
public class SpringCodegenJavaParser extends AbstractJavaParser {


    public SpringCodegenJavaParser(PackageMapStrategy packageMapStrategy, CodeGenMatchingStrategy genMatchingStrategy, Collection<CodeDetect> codeDetects) {
        super(packageMapStrategy, genMatchingStrategy, codeDetects);
    }

    public SpringCodegenJavaParser(GenericParser<JavaClassMeta, Class<?>> javaParser, LanguageMetaInstanceFactory<JavaCodeGenClassMeta, CommonCodeGenMethodMeta, CommonCodeGenFiledMeta> languageMetaInstanceFactory, PackageMapStrategy packageMapStrategy, CodeGenMatchingStrategy genMatchingStrategy, Collection<CodeDetect> codeDetects) {
        super(javaParser, languageMetaInstanceFactory, packageMapStrategy, genMatchingStrategy, codeDetects);
    }


    @Override
    public JavaCodeGenClassMeta parse(Class<?> source) {
        return super.parse(source);
    }


    @Override
    protected void enhancedProcessingClass(JavaCodeGenClassMeta methodMeta, JavaClassMeta classMeta) {
        super.enhancedProcessingClass(methodMeta, classMeta);

    }

    @Override
    protected CommonCodeGenFiledMeta converterField(JavaFieldMeta javaFieldMeta, JavaClassMeta classMeta) {
        CommonCodeGenFiledMeta commonCodeGenFiledMeta = super.converterField(javaFieldMeta, classMeta);
        Map<String, Object> tags = commonCodeGenFiledMeta.getTags();
        if (javaFieldMeta.existAnnotation(ManyToOne.class, OneToMany.class)) {
            // TODO 映射关系
//            commonCodeGenFiledMeta.setName()
        }

        return commonCodeGenFiledMeta;
    }
}
