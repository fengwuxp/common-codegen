package com.wuxp.codegen.core.parser;

import com.wuxp.codegen.core.CodeGenClassSupportHandler;
import com.wuxp.codegen.model.CommonBaseMeta;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import org.springframework.lang.Nullable;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 用于将 {@param S} 对象解析为代码生成的模型对象
 *
 * @author wuxp
 */
public interface LanguageElementDefinitionParser<C extends CommonBaseMeta, S>
        extends LanguageElementDefinitionFactory<C>,
        LanguageElementDefinitionPublishSourceParser,
        CodeGenClassSupportHandler {

    /**
     * 解析一个 element
     *
     * @param source 待解析 {@link S} 类型 的 element
     * @return 解析结果
     * @see #parseOfNullable
     */
    @Nullable
    C parse(S source);

    /**
     * @param source 待解析 {@link S} 类型 的 element
     * @return parse result
     * @see #parse
     */
    default Optional<C> parseOfNullable(S source) {
        return Optional.ofNullable(parse(source));
    }

    @Nullable
    default CommonCodeGenClassMeta parseType(Type type) {
        return (CommonCodeGenClassMeta) this.publishParse(type);
    }

    default List<CommonCodeGenClassMeta> parseTypes(List<? extends Type> types) {
        return types.stream()
                .map(this::parseType)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }


}
