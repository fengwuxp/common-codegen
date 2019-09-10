package com.wuxp.codegen.mapping;

import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import com.wuxp.codegen.model.mapping.AbstractTypeMapping;
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
        AbstractTypeMapping.setBaseTypeMapping(Object.class, JavaCodeGenClassMeta.OBJECT);
        AbstractTypeMapping.setBaseTypeMapping(Date.class, JavaCodeGenClassMeta.DATE);
        AbstractTypeMapping.setBaseTypeMapping(void.class, JavaCodeGenClassMeta.VOID);

        AbstractTypeMapping.setBaseTypeMapping(Boolean.class, JavaCodeGenClassMeta.BOOLEAN);
        AbstractTypeMapping.setBaseTypeMapping(boolean.class, JavaCodeGenClassMeta.BOOLEAN);
        AbstractTypeMapping.setBaseTypeMapping(String.class, JavaCodeGenClassMeta.STRING);
        AbstractTypeMapping.setBaseTypeMapping(CharSequence.class, JavaCodeGenClassMeta.CHAR_SEQUENCE);
        AbstractTypeMapping.setBaseTypeMapping(char.class, JavaCodeGenClassMeta.CHAR);

        AbstractTypeMapping.setBaseTypeMapping(Number.class, JavaCodeGenClassMeta.NUMBER);
        AbstractTypeMapping.setBaseTypeMapping(BigDecimal.class, JavaCodeGenClassMeta.BIG_DECIMAL);
        AbstractTypeMapping.setBaseTypeMapping(double.class, JavaCodeGenClassMeta.DOUBLE);
        AbstractTypeMapping.setBaseTypeMapping(Double.class, JavaCodeGenClassMeta.DOUBLE);
        AbstractTypeMapping.setBaseTypeMapping(Float.class, JavaCodeGenClassMeta.FLOAT);
        AbstractTypeMapping.setBaseTypeMapping(float.class, JavaCodeGenClassMeta.FLOAT);
        AbstractTypeMapping.setBaseTypeMapping(long.class, JavaCodeGenClassMeta.LONG);
        AbstractTypeMapping.setBaseTypeMapping(Long.class, JavaCodeGenClassMeta.LONG);
        AbstractTypeMapping.setBaseTypeMapping(Integer.class, JavaCodeGenClassMeta.INTEGER);
        AbstractTypeMapping.setBaseTypeMapping(int.class, JavaCodeGenClassMeta.INTEGER);
        AbstractTypeMapping.setBaseTypeMapping(Short.class, JavaCodeGenClassMeta.SHORT);
        AbstractTypeMapping.setBaseTypeMapping(short.class, JavaCodeGenClassMeta.SHORT);
        AbstractTypeMapping.setBaseTypeMapping(Byte.class, JavaCodeGenClassMeta.BYTE);
        AbstractTypeMapping.setBaseTypeMapping(byte.class, JavaCodeGenClassMeta.BYTE);

        AbstractTypeMapping.setBaseTypeMapping(Map.class, JavaCodeGenClassMeta.MAP);
        AbstractTypeMapping.setBaseTypeMapping(Set.class, JavaCodeGenClassMeta.SET);
        AbstractTypeMapping.setBaseTypeMapping(List.class, JavaCodeGenClassMeta.LIST);
        AbstractTypeMapping.setBaseTypeMapping(Collection.class, JavaCodeGenClassMeta.COLLECTION);

    }


    public JavaTypeMapping(LanguageParser<JavaCodeGenClassMeta> languageParser) {
        super(languageParser);
    }

    @Override
    protected List<Class<?>> handleArray(Class<?> clazz) {
        List<Class<?>> list = new ArrayList<>();
        //数组
        list.add(clazz);
        return list;
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
