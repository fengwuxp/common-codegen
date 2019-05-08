package com.wuxp.codegen.mapping;

import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.model.mapping.CustomizeJavaTypeMapping;
import com.wuxp.codegen.helper.GrabGenericVariablesHelper;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.model.mapping.AbstractTypeMapping;
import com.wuxp.codegen.model.mapping.BaseTypeMapping;
import com.wuxp.codegen.model.mapping.TypeMapping;
import com.wuxp.codegen.model.utils.JavaTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.*;


/**
 * 处理typescript的类型映射
 */
@Slf4j
public class TypescriptTypeMapping extends CommonTypeMapping<TypescriptClassMeta> {


    static {

        //设置基础的数据类型映射
        BASE_TYPE_MAPPING.put(Object.class, TypescriptClassMeta.ANY);
        BASE_TYPE_MAPPING.put(Date.class, TypescriptClassMeta.DATE);
        BASE_TYPE_MAPPING.put(Boolean.class, TypescriptClassMeta.BOOLEAN);
        BASE_TYPE_MAPPING.put(String.class, TypescriptClassMeta.STRING);
        BASE_TYPE_MAPPING.put(Number.class, TypescriptClassMeta.NUMBER);
        BASE_TYPE_MAPPING.put(double.class, TypescriptClassMeta.NUMBER);
        BASE_TYPE_MAPPING.put(float.class, TypescriptClassMeta.NUMBER);
        BASE_TYPE_MAPPING.put(long.class, TypescriptClassMeta.NUMBER);
        BASE_TYPE_MAPPING.put(short.class, TypescriptClassMeta.NUMBER);
        BASE_TYPE_MAPPING.put(byte.class, TypescriptClassMeta.NUMBER);
//        BASE_TYPE_MAPPING.put(Map.class, TypescriptClassMeta.MAP);
        BASE_TYPE_MAPPING.put(Map.class, TypescriptClassMeta.RECORD);
        BASE_TYPE_MAPPING.put(Set.class, TypescriptClassMeta.SET);
        BASE_TYPE_MAPPING.put(List.class, TypescriptClassMeta.ARRAY);
        BASE_TYPE_MAPPING.put(Collection.class, TypescriptClassMeta.ARRAY);
        BASE_TYPE_MAPPING.put(void.class, TypescriptClassMeta.VOID);

    }


    public TypescriptTypeMapping(LanguageParser<TypescriptClassMeta> languageParser) {
        super(languageParser);
    }


    @Override
    protected TypescriptClassMeta getAnyOrObjectType() {
        return TypescriptClassMeta.ANY;
    }

    @Override
    protected TypescriptClassMeta newCommonCodedInstance() {
        return new TypescriptClassMeta();
    }
}
