package com.wuxp.codegen.mapping;

import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.mapping.JavaArrayClassTypeMark;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

/**
 * 通过自定义的类型映射解析
 *
 * @param <C>
 */
@Slf4j
public abstract class AbstractMappingTypeDefinitionParser<C extends CommonCodeGenClassMeta> implements LanguageTypeDefinitionParser<C> {

    private final MappingTypeDefinitionParser<C> mappingTypeDefinitionParser;

    protected AbstractMappingTypeDefinitionParser(MappingTypeDefinitionParser<C> mappingTypeDefinitionParser) {
        this.mappingTypeDefinitionParser = mappingTypeDefinitionParser;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C parse(Class<?> source) {
        if (JavaArrayClassTypeMark.class.equals(source)) {
            // 标记的数据数组类型
            C arrayType = (C) new CommonCodeGenClassMeta();
            BeanUtils.copyProperties(CommonCodeGenClassMeta.ARRAY, arrayType);
            return arrayType;
        }

        return this.mappingTypeDefinitionParser.parse(source);
    }

    public MappingTypeDefinitionParser<C> getMappingTypeDefinitionParser() {
        return mappingTypeDefinitionParser;
    }
}
