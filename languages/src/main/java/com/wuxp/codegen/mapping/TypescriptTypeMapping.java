package com.wuxp.codegen.mapping;

import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.model.mapping.AbstractTypeMapping;
import com.wuxp.codegen.model.mapping.BaseTypeMapping;
import com.wuxp.codegen.model.mapping.CustomizeJavaTypeMapping;
import com.wuxp.codegen.model.mapping.TypeMapping;
import com.wuxp.codegen.model.utils.JavaTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.*;


/**
 * 处理typescript的类型映射
 */
@Slf4j
public class TypescriptTypeMapping extends AbstractTypeMapping<TypescriptClassMeta> {


    static {

        //设置基础的数据类型映射
        BASE_TYPE_MAPPING.put(Date.class, TypescriptClassMeta.DATE);
        BASE_TYPE_MAPPING.put(Boolean.class, TypescriptClassMeta.BOOLEAN);
        BASE_TYPE_MAPPING.put(String.class, TypescriptClassMeta.STRING);
        BASE_TYPE_MAPPING.put(Number.class, TypescriptClassMeta.NUMBER);
        BASE_TYPE_MAPPING.put(Map.class, TypescriptClassMeta.MAP);
        BASE_TYPE_MAPPING.put(Set.class, TypescriptClassMeta.SET);
        BASE_TYPE_MAPPING.put(List.class, TypescriptClassMeta.ARRAY);
        BASE_TYPE_MAPPING.put(Collection.class, TypescriptClassMeta.ARRAY);
        BASE_TYPE_MAPPING.put(void.class, TypescriptClassMeta.VOID);

    }

    /**
     * 基础类型映射器
     */
    protected TypeMapping<Class<?>, TypescriptClassMeta> baseTypeMapping = new BaseTypeMapping<TypescriptClassMeta>(BASE_TYPE_MAPPING);

    /**
     * 自定义的类型映射
     */
    protected TypeMapping<Class<?>, List<Class<?>>> customizeJavaTypeMapping = new CustomizeJavaTypeMapping(CUSTOMIZE_TYPE_MAPPING);


    protected LanguageParser<TypescriptClassMeta> typescriptParser;


    public TypescriptTypeMapping(LanguageParser<TypescriptClassMeta> typescriptParser) {
        this.typescriptParser = typescriptParser;
    }

    /**
     * @param classes 类型列表，大于一个表示有泛型
     * @return 类型描述字符串代码
     */
    @Override
    public List<TypescriptClassMeta> mapping(Class<?>... classes) {

        if (classes == null || classes.length == 0) {
            return new ArrayList<>();
        }

        Set<TypescriptClassMeta> classMetas = new LinkedHashSet<>(4);

        //1. 类型转换，如果是简单的java类型，则尝试做装换
        //2. 处理枚举类型
        //3. 循环获取泛型
        //4. 处理复杂的数据类型（自定义的java类）
        Arrays.stream(classes)
                .filter(Objects::nonNull)
                .map(this.customizeJavaTypeMapping::mapping)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .map(this::mapping)
                .filter(Objects::nonNull)
                .forEach(classMetas::add);
        return new ArrayList<>(classMetas);

    }


    /**
     * 获取类型映射
     * @param clazz
     * @return
     */
    protected TypescriptClassMeta mapping(Class<?> clazz) {

        Class<?> upConversionType = this.tryUpConversionType(clazz);
        if (upConversionType != null) {
            return baseTypeMapping.mapping(upConversionType);
        } else {
            //尝试用本类型去获取一次映射关系
            TypescriptClassMeta mapping = this.baseTypeMapping.mapping(clazz);
            if (mapping != null) {
                return mapping;
            }
        }


        if (JavaTypeUtil.isNoneJdkComplex(clazz) || clazz.isEnum()) {
            //复杂的数据类型或枚举
            CommonCodeGenClassMeta meta = this.typescriptParser.parse(clazz);
            if (meta == null) {
                return null;
            }
            TypescriptClassMeta typescriptClassMeta = new TypescriptClassMeta();
            BeanUtils.copyProperties(meta, typescriptClassMeta);
            return typescriptClassMeta;
        } else if (clazz.isArray()) {
            //数组
            return TypescriptClassMeta.ARRAY;
        } else {
            //未处理的类型
            log.warn("Not Found clazz " + clazz.getName() + " mapping type");
            return null;
        }
    }

    /**
     * 尝试向上转换类型
     *
     * @param clazz
     * @return
     */
    protected Class<?> tryUpConversionType(Class<?> clazz) {
        if (JavaTypeUtil.isNumber(clazz)) {
            //数值类型
            return Number.class;
        } else if (JavaTypeUtil.isString(clazz)) {
            return String.class;
        } else if (JavaTypeUtil.isBoolean(clazz)) {
            return Boolean.class;
        } else if (JavaTypeUtil.isDate(clazz)) {
            return Date.class;
        } else if (JavaTypeUtil.isVoid(clazz)) {
            return void.class;
        } else if (JavaTypeUtil.isSet(clazz)) {
            return Set.class;
        } else if (JavaTypeUtil.isList(clazz)) {
            return List.class;
        } else if (JavaTypeUtil.isCollection(clazz)) {
            return Collection.class;
        } else if (JavaTypeUtil.isMap(clazz)) {
            return Map.class;
        }
        return null;
    }
}
