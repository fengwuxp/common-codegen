package com.wuxp.codegen.mapping;

import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.model.mapping.JavaArrayClassTypeMark;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


/**
 * 处理typescript的类型映射
 *
 * @author wuxp
 */
@Slf4j
public class TypescriptTypeMapping extends AbstractLanguageTypeMapping<TypescriptClassMeta> {

  private static final Map<Class<?>, CommonCodeGenClassMeta> TYPESCRIPT_DEFAULT_BASE_MAPPING = new LinkedHashMap<>();

  static {

    //设置基础的数据类型映射
    TYPESCRIPT_DEFAULT_BASE_MAPPING.put(Object.class, TypescriptClassMeta.ANY);
    TYPESCRIPT_DEFAULT_BASE_MAPPING.put(Date.class, TypescriptClassMeta.NUMBER);
    TYPESCRIPT_DEFAULT_BASE_MAPPING.put(LocalDateTime.class, TypescriptClassMeta.NUMBER);
    TYPESCRIPT_DEFAULT_BASE_MAPPING.put(LocalDate.class, TypescriptClassMeta.NUMBER);
    TYPESCRIPT_DEFAULT_BASE_MAPPING.put(Boolean.class, TypescriptClassMeta.BOOLEAN);
    TYPESCRIPT_DEFAULT_BASE_MAPPING.put(String.class, TypescriptClassMeta.STRING);
    TYPESCRIPT_DEFAULT_BASE_MAPPING.put(Long.class, TypescriptClassMeta.STRING);
    TYPESCRIPT_DEFAULT_BASE_MAPPING.put(BigInteger.class, TypescriptClassMeta.STRING);
    TYPESCRIPT_DEFAULT_BASE_MAPPING.put(Number.class, TypescriptClassMeta.NUMBER);
    TYPESCRIPT_DEFAULT_BASE_MAPPING.put(double.class, TypescriptClassMeta.NUMBER);
    TYPESCRIPT_DEFAULT_BASE_MAPPING.put(float.class, TypescriptClassMeta.NUMBER);
    TYPESCRIPT_DEFAULT_BASE_MAPPING.put(long.class, TypescriptClassMeta.STRING);
    TYPESCRIPT_DEFAULT_BASE_MAPPING.put(short.class, TypescriptClassMeta.NUMBER);
    TYPESCRIPT_DEFAULT_BASE_MAPPING.put(byte.class, TypescriptClassMeta.NUMBER);

    TYPESCRIPT_DEFAULT_BASE_MAPPING.put(Map.class, TypescriptClassMeta.RECORD);
    TYPESCRIPT_DEFAULT_BASE_MAPPING.put(Set.class, TypescriptClassMeta.ARRAY);
    TYPESCRIPT_DEFAULT_BASE_MAPPING.put(List.class, TypescriptClassMeta.ARRAY);
    TYPESCRIPT_DEFAULT_BASE_MAPPING.put(Collection.class, TypescriptClassMeta.ARRAY);
    TYPESCRIPT_DEFAULT_BASE_MAPPING.put(void.class, TypescriptClassMeta.VOID);
    TYPESCRIPT_DEFAULT_BASE_MAPPING.put(Void.class, TypescriptClassMeta.VOID);


    //文件上传
    TYPESCRIPT_DEFAULT_BASE_MAPPING.put(CommonsMultipartFile.class, TypescriptClassMeta.BROWSER_FILE);
    TYPESCRIPT_DEFAULT_BASE_MAPPING.put(InputStreamResource.class, TypescriptClassMeta.BROWSER_FILE);
    TYPESCRIPT_DEFAULT_BASE_MAPPING.put(JavaArrayClassTypeMark.class, TypescriptClassMeta.JAVA_ARRAY_CLASS_TYPE_MARK);

    // reactor TODO support rxJs
    TYPESCRIPT_DEFAULT_BASE_MAPPING.put(Publisher.class, TypescriptClassMeta.PROMISE);
  }


  public TypescriptTypeMapping(LanguageParser<TypescriptClassMeta> languageParser, Map<Class<?>, CommonCodeGenClassMeta> baseTypeMappingMap,
      Map<Class<?>, CommonCodeGenClassMeta> customizeTypeMappingMap, Map<Class<?>, Class<?>[]> customizeJavaMappingMap) {
    super(languageParser, baseTypeMappingMap, customizeTypeMappingMap, customizeJavaMappingMap);
  }

  @Override
  protected Map<Class<?>, CommonCodeGenClassMeta> getBaseTypeMappingMap() {
    return TYPESCRIPT_DEFAULT_BASE_MAPPING;
  }

  @Override
  protected TypescriptClassMeta getAnyOrObjectType() {
    return TypescriptClassMeta.ANY;
  }

  @Override
  protected TypescriptClassMeta newCommonCodedInstance() {
    return new TypescriptClassMeta();
  }
}
