package com.wuxp.codegen.mapping;

import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.model.mapping.AbstractTypeMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.*;


/**
 * 处理typescript的类型映射
 */
@Slf4j
public class TypescriptTypeMapping extends CommonTypeMapping<TypescriptClassMeta> {


    static {

        //设置基础的数据类型映射
        AbstractTypeMapping.setBaseTypeMapping(Object.class, TypescriptClassMeta.ANY);
        AbstractTypeMapping.setBaseTypeMapping(Date.class, TypescriptClassMeta.DATE);
        AbstractTypeMapping.setBaseTypeMapping(Boolean.class, TypescriptClassMeta.BOOLEAN);
        AbstractTypeMapping.setBaseTypeMapping(String.class, TypescriptClassMeta.STRING);
        AbstractTypeMapping.setBaseTypeMapping(Number.class, TypescriptClassMeta.NUMBER);
        AbstractTypeMapping.setBaseTypeMapping(double.class, TypescriptClassMeta.NUMBER);
        AbstractTypeMapping.setBaseTypeMapping(float.class, TypescriptClassMeta.NUMBER);
        AbstractTypeMapping.setBaseTypeMapping(long.class, TypescriptClassMeta.NUMBER);
        AbstractTypeMapping.setBaseTypeMapping(short.class, TypescriptClassMeta.NUMBER);
        AbstractTypeMapping.setBaseTypeMapping(byte.class, TypescriptClassMeta.NUMBER);
//        AbstractTypeMapping.setBaseTypeMapping(Map.class, TypescriptClassMeta.MAP);
        AbstractTypeMapping.setBaseTypeMapping(Map.class, TypescriptClassMeta.RECORD);
        AbstractTypeMapping.setBaseTypeMapping(Set.class, TypescriptClassMeta.SET);
        AbstractTypeMapping.setBaseTypeMapping(List.class, TypescriptClassMeta.ARRAY);
        AbstractTypeMapping.setBaseTypeMapping(Collection.class, TypescriptClassMeta.ARRAY);
        AbstractTypeMapping.setBaseTypeMapping(void.class, TypescriptClassMeta.VOID);

        //文件上传
        AbstractTypeMapping.setBaseTypeMapping(CommonsMultipartFile.class, TypescriptClassMeta.BROWSER_FILE);

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
