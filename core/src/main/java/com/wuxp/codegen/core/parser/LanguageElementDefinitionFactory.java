package com.wuxp.codegen.core.parser;

import com.wuxp.codegen.model.CommonBaseMeta;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wuxp
 */
public interface LanguageElementDefinitionFactory<C extends CommonBaseMeta> {

    /**
     * @return 实例化一个 Element Meta Object
     */
    C newInstance();

    /**
     * 生成一个类型变量
     * {@link java.lang.reflect.TypeVariable}
     *
     * @return
     */
    <M extends CommonCodeGenClassMeta> M newTypeVariableInstance();

    default <M extends CommonCodeGenClassMeta> M parseTypeVariable(Type type) {
        M classInstance = newTypeVariableInstance();
        classInstance.setName(type.getTypeName());
        classInstance.setGenericDescription(type.getTypeName());
        return classInstance;
    }

    @SuppressWarnings("unchecked")
    default <M extends CommonCodeGenClassMeta> List<M> parseTypeVariables(Collection<? extends Type> typeVariables) {
        return typeVariables.stream()
                .filter(Objects::nonNull)
                .map(this::parseTypeVariable)
                .map(element -> (M) element)
                .collect(Collectors.toList());
    }

}
