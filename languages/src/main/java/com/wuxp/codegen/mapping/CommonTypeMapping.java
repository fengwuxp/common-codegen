package com.wuxp.codegen.mapping;

import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.helper.GrabGenericVariablesHelper;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.model.mapping.AbstractTypeMapping;
import com.wuxp.codegen.model.utils.JavaTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.stream.Collectors;


/**
 * 处理common的类型映射
 */
@Slf4j
public class CommonTypeMapping<C extends CommonCodeGenClassMeta> extends AbstractTypeMapping<C> {


    protected LanguageParser<C> languageParser;


    public CommonTypeMapping(LanguageParser<C> languageParser) {
        this.languageParser = languageParser;
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


        //1. 类型转换，如果是简单的java类型，则尝试做装换
        //2. 处理枚举类型
        //3. 循环获取泛型
        //4. 处理复杂的数据类型（自定义的java类）
        List<C> classMetas = (List<C>) Arrays.stream(classes)
                .filter(Objects::nonNull)
                .map(customizeJavaTypeMapping::mapping)
                .flatMap(Collection::stream)
                .map(clazz -> {
                    List<Class<?>> list = new ArrayList<>();
                    if (clazz.isArray()) {
                        list = this.handleArray(clazz);
                    } else {
                        list.add(clazz);
                    }
                    return list;
                })
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .map(this::mapping)
                .filter(Objects::nonNull)
                .map(commonCodeGenClassMeta -> (C) commonCodeGenClassMeta)
                .collect(Collectors.toList());

        //匹配泛型的个数是否充足，不足的用any补足
        int index = 0;
        boolean hasGeneric = false;
        for (; index < classMetas.size(); index++) {
            //查找是否存在泛型的元数据
            if (classMetas.get(index).getGenericDescription() == null) {
                continue;
            }
            hasGeneric = true;
            break;
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


    protected C getAnyOrObjectType() {

        return null;
    }


    /**
     * 获取类型映射
     *
     * @param clazz
     * @return
     */
    protected C mapping(Class<?> clazz) {

        if (clazz.isArray()) {
            //数组
            C array = this.languageParser.getLanguageMetaInstanceFactory().newClassInstance();
            BeanUtils.copyProperties( CommonCodeGenClassMeta.ARRAY,array);
            array.setTypeVariables(new CommonCodeGenClassMeta[]{this.mapping(clazz.getComponentType())});
            return array;

        }

        C commonCodeGenClassMeta = (C) baseTypeMapping.mapping(clazz);
        if (commonCodeGenClassMeta != null) {
            return commonCodeGenClassMeta;
        }

        Class<?> upConversionType = this.tryUpConversionType(clazz);
        if (upConversionType != null) {
            return (C) baseTypeMapping.mapping(upConversionType);
        } else {
            //尝试用本类型去获取一次映射关系
            C mapping = (C) baseTypeMapping.mapping(clazz);
            if (mapping != null) {
                return mapping;
            }
        }

        C mapping = (C) customizeTypeMapping.mapping(clazz);
        if (mapping != null) {
            return mapping;
        }


        if (JavaTypeUtil.isNoneJdkComplex(clazz) || clazz.isEnum()) {
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

    protected List<Class<?>> handleArray(Class<?> clazz) {
        List<Class<?>> list = new ArrayList<>();
        //数组
        list.add(List.class);
        list.add(clazz.getComponentType());
        return list;
    }


    protected C newCommonCodedInstance() {
        return (C) new CommonCodeGenClassMeta();
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
