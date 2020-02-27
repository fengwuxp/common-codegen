package com.wuxp.codegen.languages.factory;

import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

@Slf4j
public class JavaLanguageMetaInstanceFactory implements
        LanguageParser.LanguageMetaInstanceFactory<JavaCodeGenClassMeta, CommonCodeGenMethodMeta, CommonCodeGenFiledMeta> {

    @Override
    public JavaCodeGenClassMeta newClassInstance() {
        return new JavaCodeGenClassMeta();
    }


    @Override
    public JavaCodeGenClassMeta getTypeVariableInstance() {
        JavaCodeGenClassMeta javaCodeGenClassMeta = new JavaCodeGenClassMeta();
        BeanUtils.copyProperties(JavaCodeGenClassMeta.TYPE_VARIABLE, javaCodeGenClassMeta);
        return javaCodeGenClassMeta;
    }
}
