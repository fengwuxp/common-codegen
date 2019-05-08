package com.wuxp.codegen.mapping;

import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.*;


/**
 * 处理java Type的类型映射
 */
@Slf4j
public class JavaTypeMapping extends CommonTypeMapping<JavaCodeGenClassMeta> {


    static {

        //设置基础的数据类型映射
        BASE_TYPE_MAPPING.put(Object.class, JavaCodeGenClassMeta.OBJECT);
        BASE_TYPE_MAPPING.put(Date.class, JavaCodeGenClassMeta.DATE);
        BASE_TYPE_MAPPING.put(void.class, JavaCodeGenClassMeta.VOID);

        BASE_TYPE_MAPPING.put(Boolean.class, JavaCodeGenClassMeta.BOOLEAN);
        BASE_TYPE_MAPPING.put(boolean.class, JavaCodeGenClassMeta.BOOLEAN);
        BASE_TYPE_MAPPING.put(String.class, JavaCodeGenClassMeta.STRING);
        BASE_TYPE_MAPPING.put(CharSequence.class, JavaCodeGenClassMeta.CHAR_SEQUENCE);
        BASE_TYPE_MAPPING.put(char.class, JavaCodeGenClassMeta.CHAR);

        BASE_TYPE_MAPPING.put(Number.class, JavaCodeGenClassMeta.NUMBER);
        BASE_TYPE_MAPPING.put(BigDecimal.class, JavaCodeGenClassMeta.BIG_DECIMAL);
        BASE_TYPE_MAPPING.put(double.class, JavaCodeGenClassMeta.DOUBLE);
        BASE_TYPE_MAPPING.put(Double.class, JavaCodeGenClassMeta.DOUBLE);
        BASE_TYPE_MAPPING.put(Float.class, JavaCodeGenClassMeta.FLOAT);
        BASE_TYPE_MAPPING.put(float.class, JavaCodeGenClassMeta.FLOAT);
        BASE_TYPE_MAPPING.put(long.class, JavaCodeGenClassMeta.LONG);
        BASE_TYPE_MAPPING.put(Long.class, JavaCodeGenClassMeta.LONG);
        BASE_TYPE_MAPPING.put(Integer.class, JavaCodeGenClassMeta.INTEGER);
        BASE_TYPE_MAPPING.put(int.class, JavaCodeGenClassMeta.INTEGER);
        BASE_TYPE_MAPPING.put(Short.class, JavaCodeGenClassMeta.SHORT);
        BASE_TYPE_MAPPING.put(short.class, JavaCodeGenClassMeta.SHORT);
        BASE_TYPE_MAPPING.put(Byte.class, JavaCodeGenClassMeta.BYTE);
        BASE_TYPE_MAPPING.put(byte.class, JavaCodeGenClassMeta.BYTE);

        BASE_TYPE_MAPPING.put(Map.class, JavaCodeGenClassMeta.MAP);
        BASE_TYPE_MAPPING.put(Set.class, JavaCodeGenClassMeta.SET);
        BASE_TYPE_MAPPING.put(List.class, JavaCodeGenClassMeta.LIST);
        BASE_TYPE_MAPPING.put(Collection.class, JavaCodeGenClassMeta.COLLECTION);

    }


    public JavaTypeMapping(LanguageParser<JavaCodeGenClassMeta> languageParser) {
        super(languageParser);
    }


    @Override
    protected JavaCodeGenClassMeta getAnyOrObjectType() {
        return JavaCodeGenClassMeta.OBJECT;
    }

    @Override
    protected JavaCodeGenClassMeta newCommonCodedInstance() {
        return new JavaCodeGenClassMeta();
    }
}
