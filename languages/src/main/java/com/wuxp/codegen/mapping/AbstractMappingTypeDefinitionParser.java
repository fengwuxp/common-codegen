package com.wuxp.codegen.mapping;

import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.languages.DelegateLanguageTypeDefinitionParser;
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
public abstract class AbstractMappingTypeDefinitionParser<C extends CommonCodeGenClassMeta> extends DelegateLanguageTypeDefinitionParser<C> {

    private final MappingTypeDefinitionParser<C> mappingTypeDefinitionParser;

    protected AbstractMappingTypeDefinitionParser(LanguageTypeDefinitionParser<C> delegate, MappingTypeDefinitionParser<C> mappingTypeDefinitionParser) {
        super(delegate);
        this.mappingTypeDefinitionParser = mappingTypeDefinitionParser;
    }

    @Override
    public C parse(Class<?> source) {
        if (JavaArrayClassTypeMark.class.equals(source)) {
            // 标记的数据数组类型
            C arrayType = this.newElementInstance();
            BeanUtils.copyProperties(CommonCodeGenClassMeta.ARRAY, arrayType);
            return arrayType;
        }

        C result = this.mappingTypeDefinitionParser.parse(source);
        if (result == null) {
            result = getDelegate().parse(source);
        }
        return result;
    }
}
