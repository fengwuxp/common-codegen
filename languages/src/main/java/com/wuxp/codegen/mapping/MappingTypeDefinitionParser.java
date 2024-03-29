package com.wuxp.codegen.mapping;

import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Slf4j
public class MappingTypeDefinitionParser<C extends CommonCodeGenClassMeta> implements LanguageTypeDefinitionParser<C> {

    /**
     * 类型映射器
     */
    private final Map<Class<?>, C> typeMappings;

    private final Supplier<C> elementInstanceSupllier;


    public MappingTypeDefinitionParser(Map<Class<?>, C> typeMappings, Supplier<C> elementInstanceSupllier) {
        this.typeMappings = typeMappings;
        this.elementInstanceSupllier = elementInstanceSupllier;
    }


    @Override
    public C newElementInstance() {
        return this.elementInstanceSupllier.get();
    }

    @Override
    public C parse(Class<?> source) {
        return Stream.of(source)
                .filter(Objects::nonNull)
                .map(this::mappingType)
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    public void putTypeMapping(Class<?> clazz, C mappingMeta) {
        this.typeMappings.putIfAbsent(clazz, mappingMeta);
    }

    /**
     * 获取类型映射
     *
     * @param clazz 类类型实例
     * @return 映射后的类型
     */
    private C mappingType(Class<?> clazz) {
        Assert.isTrue(!clazz.isArray(), String.format("not support array type,class name=%s", clazz.getName()));
        C meta = mappingTypeDefinition(clazz).orElseGet(() -> mappingUpConversionType(clazz));
        if (meta == null) {
            log.warn("Not Found clazz {} mapping type", clazz.getName());
            return null;
        }
        C result = newElementInstance();
        BeanUtils.copyProperties(meta, result);
        result.setSource(clazz);
        return result;
    }


    private C mappingUpConversionType(Class<?> clazz) {
        return this.tryUpConversionType(clazz).flatMap(this::mappingTypeDefinition).orElse(null);
    }

    private Optional<C> mappingTypeDefinition(Class<?> clazz) {
        return Optional.ofNullable(typeMappings.get(clazz));
    }


    /**
     * 尝试向上转换类型
     *
     * @param clazz 类类型实例
     * @return 超类对象
     */
    private Optional<Class<?>> tryUpConversionType(Class<?> clazz) {
        if (JavaTypeUtils.isNumber(clazz)) {
            //数值类型
            return Optional.of(Number.class);
        } else if (JavaTypeUtils.isString(clazz)) {
            return Optional.of(String.class);
        } else if (JavaTypeUtils.isBoolean(clazz)) {
            return Optional.of(Boolean.class);
        } else if (JavaTypeUtils.isDate(clazz)) {
            return Optional.of(Date.class);
        } else if (JavaTypeUtils.isVoid(clazz)) {
            return Optional.of(void.class);
        } else if (JavaTypeUtils.isSet(clazz)) {
            return Optional.of(Set.class);
        } else if (JavaTypeUtils.isList(clazz)) {
            return Optional.of(List.class);
        } else if (JavaTypeUtils.isCollection(clazz)) {
            return Optional.of(Collection.class);
        } else if (JavaTypeUtils.isMap(clazz)) {
            return Optional.of(Map.class);
        }
        return Optional.empty();
    }
}
