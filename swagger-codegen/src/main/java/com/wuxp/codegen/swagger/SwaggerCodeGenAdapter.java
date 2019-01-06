package com.wuxp.codegen.swagger;

import com.wuxp.codegen.core.CodeGenAdapter;
import com.wuxp.codegen.core.parser.GenericParser;
import com.wuxp.codegen.core.parser.JavaClassParser;
import com.wuxp.codegen.languages.AbstractLanguageParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import lombok.extern.slf4j.Slf4j;


/**
 * 根据swagger的注解规则生成代码
 */
@Slf4j
public class SwaggerCodeGenAdapter implements CodeGenAdapter {

    protected GenericParser<JavaClassMeta, Class<?>> javaParser = new JavaClassParser(true);


    protected AbstractLanguageParser<CommonCodeGenClassMeta, CommonCodeGenMethodMeta, CommonCodeGenFiledMeta> abstractLanguageParser;


    @Override
    public CommonCodeGenClassMeta codeGen(Class<?> clazz) {

        if (clazz == null) {
            return null;
        }


        return this.abstractLanguageParser.parse(clazz);
    }
}
