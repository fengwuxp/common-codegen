package com.wuxp.codegen.languages;

import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * java To 其他语言类型定义映射
 *
 * @author wuxp
 */
public class LanguageTypeDefinitionMappedParser {

    private final Map<Class<?>, CommonCodeGenClassMeta> typeDefinitionMappings = new LinkedHashMap<>();

    private final Map<Class<?>, Class<?>[]> classMappings = new LinkedHashMap<>();

    public List<CommonCodeGenClassMeta> parse(Class<?>... classes) {
        return Arrays.stream(classes)
                .map(this::mapping)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private List<CommonCodeGenClassMeta> mapping(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        Class<?>[] classes = classMappings.get(clazz);
        if (classes == null) {
            return null;
        }
        return Arrays.stream(classes)
                .map(this::mappingTypeDefinition)
                .collect(Collectors.toList());
    }

    private CommonCodeGenClassMeta mappingTypeDefinition(Class<?> clazz) {
        if (clazz == null) {
            return null;
        }
        return typeDefinitionMappings.get(clazz);
    }

    /**
     * 配置 java 和 其他语言的类型映射
     *
     * @param clazzType      java 类型
     * @param typeDefinition 用于生成的其他语言类型描述
     * @return LanguageTypeDefinitionMappedParser
     */
    public LanguageTypeDefinitionMappedParser configureTypeDefinitionMapping(Class<?> clazzType, CommonCodeGenClassMeta typeDefinition) {
        if (typeDefinition == null) {
            return this;
        }
        Assert.notNull(clazzType, "mapping class is not null");
        typeDefinitionMappings.put(clazzType, typeDefinition);
        return this;
    }

    /**
     * 配置 java
     *
     * @param clazzType     java 类型
     * @param classMappings 转换的 java 类型
     * @return LanguageTypeDefinitionMappedParser
     */
    public LanguageTypeDefinitionMappedParser configureJavaTypeMapping(Class<?> clazzType, Class<?>... classMappings) {
        if (classMappings == null) {
            return this;
        }
        Assert.notNull(clazzType, "mapping class is not null");
        this.classMappings.put(clazzType, classMappings);
        return this;
    }

}
