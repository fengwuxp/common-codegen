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
    C newElementInstance();

    /**
     * 生成一个类型变量
     * {@link java.lang.reflect.TypeVariable}
     *
     * @return 类型变量
     */
    CommonCodeGenClassMeta newTypeVariableInstance();

    /**
     * 解析类型上的类型变量 例如：A extends C<F,E,G>{}
     *
     * @return 类型变量
     */
    default CommonCodeGenClassMeta parseTypeVariable(Type type) {
        CommonCodeGenClassMeta classInstance = newTypeVariableInstance();
        classInstance.setName(type.getTypeName());
        classInstance.setGenericDescription(type.getTypeName());
        return classInstance;
    }

    default List<CommonCodeGenClassMeta> parseTypeVariables(Collection<? extends Type> typeVariables) {
        return typeVariables.stream()
                .filter(Objects::nonNull)
                .map(this::parseTypeVariable)
                .collect(Collectors.toList());
    }

}
