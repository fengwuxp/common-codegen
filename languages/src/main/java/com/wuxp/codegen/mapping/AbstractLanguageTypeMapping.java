package com.wuxp.codegen.mapping;

import com.wuxp.codegen.core.exception.CodegenRuntimeException;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.helper.GrabGenericVariablesHelper;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.mapping.BaseTypeMapping;
import com.wuxp.codegen.model.mapping.CustomizeJavaTypeMapping;
import com.wuxp.codegen.model.mapping.JavaArrayClassTypeMark;
import com.wuxp.codegen.model.mapping.TypeMapping;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.ResolvableType;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 不同语言的类型映射的抽象类，需要提供语言的基础类型映射 {@link #baseTypeMapping} 自定义类型映射{@link #customizeTypeMapping}和 java 类型映射扩展{@link #customizeJavaTypeMapping}
 *
 * @author wuxp
 */
@Slf4j
public abstract class AbstractLanguageTypeMapping<C extends CommonCodeGenClassMeta> implements TypeMapping<Class<?>, List<C>> {


    /**
     * 基础类型映射器
     */
    protected final TypeMapping<Class<?>, C> baseTypeMapping;

    /**
     * 自定义的类型映射器
     */
    protected final TypeMapping<Class<?>, C> customizeTypeMapping;

    /**
     * 自定义的java类型映射
     */
    protected final TypeMapping<Class<?>, List<Class<?>>> customizeJavaTypeMapping;

    /**
     * 语言处理解析器
     */
    protected final LanguageParser<C> languageParser;


    protected AbstractLanguageTypeMapping(LanguageParser<C> languageParser,
                                          Map<Class<?>, CommonCodeGenClassMeta> baseTypeMappingMap,
                                          Map<Class<?>, CommonCodeGenClassMeta> customizeTypeMappingMap,
                                          Map<Class<?>, Class<?>[]> customizeJavaMappingMap) {
        this.languageParser = languageParser;
        Map<Class<?>, CommonCodeGenClassMeta> languageBaseTypeMappingMap = this.getBaseTypeMappingMap();
        final Map<Class<?>, CommonCodeGenClassMeta> baseTypeMap = new LinkedHashMap<>(baseTypeMappingMap);
        languageBaseTypeMappingMap.forEach((key, val) -> {
            if (!baseTypeMap.containsKey(key)) {
                baseTypeMap.put(key, val);
            }
        });
        this.baseTypeMapping = new BaseTypeMapping<>(baseTypeMap);
        this.customizeTypeMapping = new BaseTypeMapping<>(customizeTypeMappingMap);
        this.customizeJavaTypeMapping = new CustomizeJavaTypeMapping(customizeJavaMappingMap);
    }

    /**
     * @param classes 类型列表，大于一个表示有泛型
     * @return 类型描述字符串代码
     */
    @Override
    public List<C> mapping(Class<?>... classes) {

        if (classes == null || classes.length == 0) {
            return new ArrayList<>();
        }


        // 1. 类型转换，如果是简单的java类型，则尝试做装换
        // 2. 处理枚举类型
        // 3. 循环获取泛型
        // 4. 处理复杂的数据类型（自定义的java类）
        List<C> classMetas = Arrays.stream(classes)
                .filter(Objects::nonNull)
                .map(customizeJavaTypeMapping::mapping)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .map(this::mapping)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 匹配泛型的个数是否充足，不足的用any补足
        int index = 0;
        boolean hasGeneric = false;
        for (; index < classMetas.size(); index++) {
            // 查找是否存在泛型的元数据
            if (classMetas.get(index).getGenericDescription() != null) {
                hasGeneric = true;
                break;
            }
        }
        if (hasGeneric) {
            //有泛型
            CommonCodeGenClassMeta commonCodeGenClassMeta = classMetas.get(index);
            String finallyGenericDescription = commonCodeGenClassMeta.getFinallyGenericDescription();
            List<String> genericDescriptors = GrabGenericVariablesHelper.matchGenericDescriptors(finallyGenericDescription);

            //计算期望的泛型个数，和事件泛型个数的差值，+1是因为要去除本身
            int num = genericDescriptors.size() - classMetas.size() + index + 1;
            if (num > 0) {
                //泛型不够
                for (int i = 0; i < num; i++) {
                    //填充通用类型
                    classMetas.add(this.getAnyOrObjectType());
                }
            }
        }

        return classMetas;

    }

    /**
     * @return 不同语言和java基础类型的映射关系
     */
    protected abstract Map<Class<?>, CommonCodeGenClassMeta> getBaseTypeMappingMap();

    protected C getAnyOrObjectType() {

        return null;
    }


    /**
     * 获取类型映射
     *
     * @param clazz 类类型实例
     * @return 映射后的类型
     */
    protected C mapping(Class<?> clazz) {

        if (JavaArrayClassTypeMark.class.equals(clazz)) {
            // 标记的数据数组类型
            C array = this.languageParser.getLanguageMetaInstanceFactory().newClassInstance();
            BeanUtils.copyProperties(CommonCodeGenClassMeta.ARRAY, array);
            return array;
        }
        if (clazz.isArray()) {
            throw new CodegenRuntimeException("not support array type");
        }

        C commonCodeGenClassMeta = baseTypeMapping.mapping(clazz);
        if (commonCodeGenClassMeta != null) {
            return commonCodeGenClassMeta;
        }

        Optional<Class<?>> upConversionType = this.tryUpConversionType(clazz);
        if (upConversionType.isPresent()) {
            return baseTypeMapping.mapping(upConversionType.get());
        } else {
            //尝试用本类型去获取一次映射关系
            C mapping = baseTypeMapping.mapping(clazz);
            if (mapping != null) {
                return mapping;
            }
        }

        C mapping = customizeTypeMapping.mapping(clazz);
        if (mapping != null) {
            return mapping;
        }

        if (JavaTypeUtils.isNoneJdkComplex(clazz) || clazz.isEnum()) {
            //复杂的数据类型或枚举
            C meta = this.languageParser.parse(clazz);
            if (meta == null) {
                return null;
            }
            C typescriptClassMeta = this.newCommonCodedInstance();
            BeanUtils.copyProperties(meta, typescriptClassMeta);
            return typescriptClassMeta;
        } else {
            //未处理的类型
            log.warn("Not Found clazz " + clazz.getName() + " mapping type");
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    protected C newCommonCodedInstance() {
        return (C) new CommonCodeGenClassMeta();
    }

    /**
     * 尝试向上转换类型
     *
     * @param clazz 类类型实例
     * @return 超类对象
     */
    protected Optional<Class<?>> tryUpConversionType(Class<?> clazz) {
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

    /**
     * 获取类类型及其泛型
     *
     * @param clazz 类类型实例
     * @return 概率的泛型变量列表
     */
    protected Class<?>[] genericsToClassType(Class<?> clazz) {
        ResolvableType resolvableType = ResolvableType.forClass(clazz);
        while (resolvableType.isArray()) {
            resolvableType = resolvableType.getComponentType();
        }
        return this.genericsToClassType(resolvableType);
    }

    /**
     * 获取类类型及其泛型
     *
     * @param resolvableType 类型解析对象
     * @return 泛型列表
     */
    private Class<?>[] genericsToClassType(ResolvableType resolvableType) {
        ResolvableType[] generics = resolvableType.getGenerics();
        List<Class<?>> classes = new ArrayList<>();
        classes.add(resolvableType.getRawClass());
        for (ResolvableType generic : generics) {
            classes.addAll(Arrays.asList(genericsToClassType(generic)));
        }

        return classes.stream()
                .filter(Objects::nonNull)
                .toArray(Class<?>[]::new);
    }

    public TypeMapping<Class<?>, C> getCombineTypeMapping() {
        return classes -> {
            C result = customizeTypeMapping.mapping(classes);
            if (result == null) {
                return baseTypeMapping.mapping(classes);
            }
            return result;
        };
    }

    public TypeMapping<Class<?>, List<Class<?>>> getCustomizeJavaTypeMapping() {
        return customizeJavaTypeMapping;
    }


}
