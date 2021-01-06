package com.wuxp.codegen.mapping;

import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.helper.GrabGenericVariablesHelper;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.mapping.BaseTypeMapping;
import com.wuxp.codegen.model.mapping.CustomizeJavaTypeMapping;
import com.wuxp.codegen.model.mapping.JavaArrayClassTypeMark;
import com.wuxp.codegen.model.mapping.TypeMapping;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.ResolvableType;


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
  protected final TypeMapping<Class<?>, ? extends CommonCodeGenClassMeta> baseTypeMapping;

  /**
   * 自定义的类型映射器
   */
  protected final TypeMapping<Class<?>, ? extends CommonCodeGenClassMeta> customizeTypeMapping;

  /**
   * 自定义的java类型映射
   */
  protected final TypeMapping<Class<?>, List<Class<?>>> customizeJavaTypeMapping;

  /**
   * 语言处理解析器
   */
  protected final LanguageParser<C> languageParser;


  public AbstractLanguageTypeMapping(LanguageParser<C> languageParser,
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

    //数组
    //boolean isArray = JavaArrayClassType.class.equals(lastClazz);
    //1. 类型转换，如果是简单的java类型，则尝试做装换
    //2. 处理枚举类型
    //3. 循环获取泛型
    //4. 处理复杂的数据类型（自定义的java类）
    List<C> classMetas = Arrays.stream(classes)
        .filter(Objects::nonNull)
        .map(customizeJavaTypeMapping::mapping)
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

  /**
   * 获取不同语言和java基础类型的映射关系
   *
   * @return
   */
  protected abstract Map<Class<?>, CommonCodeGenClassMeta> getBaseTypeMappingMap();

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

    if (JavaArrayClassTypeMark.class.equals(clazz)) {
      // 标记的数据数组类型
      C array = this.languageParser.getLanguageMetaInstanceFactory().newClassInstance();
      BeanUtils.copyProperties(CommonCodeGenClassMeta.ARRAY, array);
      return array;
    }
    if (clazz.isArray()) {
      //数组
//            C array = this.languageParser.getLanguageMetaInstanceFactory().newClassInstance();
//            BeanUtils.copyProperties(CommonCodeGenClassMeta.ARRAY, array);
//
//            // 计算数组类型的深度
//            Class<?> componentType = clazz.getComponentType();
//            List<String> componentTypes = new ArrayList<>();
//            while (componentType.isArray()) {
//                componentTypes.add("[]");
//                componentType = componentType.getComponentType();
//            }
//            array.setTypeVariables(new CommonCodeGenClassMeta[]{this.mapping(componentType)});
//            if (!componentTypes.isEmpty()) {
//                // 如果数组的维度大于一，重新生成数组类型的名称
//                array.setName(MessageFormat.format("{0}{1}", ARRAY_TYPE_NAME_PREFIX, String.join("", componentTypes)));
//            }
//            return array;
      throw new RuntimeException("not support array type");
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

  protected List<Class<?>> handleArray(Class<?> clazz) {
    List<Class<?>> list = new ArrayList<>();
    //数组
    list.add(clazz);
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
    if (JavaTypeUtils.isNumber(clazz)) {
      //数值类型
      return Number.class;
    } else if (JavaTypeUtils.isString(clazz)) {
      return String.class;
    } else if (JavaTypeUtils.isBoolean(clazz)) {
      return Boolean.class;
    } else if (JavaTypeUtils.isDate(clazz)) {
      return Date.class;
    } else if (JavaTypeUtils.isVoid(clazz)) {
      return void.class;
    } else if (JavaTypeUtils.isSet(clazz)) {
      return Set.class;
    } else if (JavaTypeUtils.isList(clazz)) {
      return List.class;
    } else if (JavaTypeUtils.isCollection(clazz)) {
      return Collection.class;
    } else if (JavaTypeUtils.isMap(clazz)) {
      return Map.class;
    }
    return null;
  }

  /**
   * 获取类类型及其泛型
   *
   * @param clazz
   * @return
   */
  protected Class<?>[] genericsToClassType(Class<?> clazz) {
    ResolvableType resolvableType = ResolvableType.forClass(clazz);
    while (resolvableType.isArray()) {
      resolvableType = resolvableType.getComponentType();
    }
    Class<?>[] classes = this.genericsToClassType(resolvableType);
    return classes;

  }

  /**
   * 获取类类型及其泛型
   *
   * @param resolvableType
   * @return
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

  public TypeMapping<Class<?>, ? extends CommonCodeGenClassMeta> getBaseTypeMapping() {
    return baseTypeMapping;
  }

  public TypeMapping<Class<?>, ? extends CommonCodeGenClassMeta> getCustomizeTypeMapping() {
    return customizeTypeMapping;
  }

  public TypeMapping<Class<?>, List<Class<?>>> getCustomizeJavaTypeMapping() {
    return customizeJavaTypeMapping;
  }


}
