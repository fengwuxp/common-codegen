package com.wuxp.codegen.mapping;

import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.languages.dart.DartClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.model.mapping.AbstractTypeMapping;
import com.wuxp.codegen.model.mapping.JavaArrayClassTypeMark;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.*;


/**
 * 处理dart的类型映射
 *
 * @author wxup
 */
@Slf4j
public class DartTypeMapping extends CommonTypeMapping<DartClassMeta> {


    static {

        //设置基础的数据类型映射
        AbstractTypeMapping.setBaseTypeMapping(Object.class, DartClassMeta.OBJECT);
        AbstractTypeMapping.setBaseTypeMapping(Date.class, DartClassMeta.DATE);
        AbstractTypeMapping.setBaseTypeMapping(Boolean.class, DartClassMeta.BOOL);
        AbstractTypeMapping.setBaseTypeMapping(String.class, DartClassMeta.STRING);
        AbstractTypeMapping.setBaseTypeMapping(Number.class, DartClassMeta.NUM);
        AbstractTypeMapping.setBaseTypeMapping(double.class, DartClassMeta.DOUBLE);
        AbstractTypeMapping.setBaseTypeMapping(float.class, DartClassMeta.DOUBLE);
        AbstractTypeMapping.setBaseTypeMapping(long.class, DartClassMeta.INT);
        AbstractTypeMapping.setBaseTypeMapping(short.class, DartClassMeta.INT);
        AbstractTypeMapping.setBaseTypeMapping(byte.class, DartClassMeta.INT);
        AbstractTypeMapping.setBaseTypeMapping(Map.class, DartClassMeta.BUILT_MAP);
        AbstractTypeMapping.setBaseTypeMapping(Set.class, DartClassMeta.BUILT_SET);
        AbstractTypeMapping.setBaseTypeMapping(List.class, DartClassMeta.BUILT_LIST);
        AbstractTypeMapping.setBaseTypeMapping(Collection.class, DartClassMeta.BUILT_ITERABLE);
        AbstractTypeMapping.setBaseTypeMapping(void.class, DartClassMeta.VOID);

        //文件上传
        AbstractTypeMapping.setBaseTypeMapping(CommonsMultipartFile.class, DartClassMeta.FILE);
//        AbstractTypeMapping.setBaseTypeMapping(JavaArrayClassTypeMark.class, TypescriptClassMeta.JAVA_ARRAY_CLASS_TYPE_MARK);

    }


    @Override
    protected DartClassMeta mapping(Class<?> clazz) {
        if (JavaArrayClassTypeMark.class.equals(clazz)) {
            // 标记的数据数组类型
            DartClassMeta array = this.languageParser.getLanguageMetaInstanceFactory().newClassInstance();
            BeanUtils.copyProperties(DartClassMeta.BUILT_LIST, array);
            return array;
        }
        return super.mapping(clazz);
    }

    public DartTypeMapping(LanguageParser<DartClassMeta> languageParser) {
        super(languageParser);
    }


    @Override
    protected DartClassMeta getAnyOrObjectType() {
        return DartClassMeta.OBJECT;
    }

    @Override
    protected DartClassMeta newCommonCodedInstance() {
        return new DartClassMeta();
    }
}
