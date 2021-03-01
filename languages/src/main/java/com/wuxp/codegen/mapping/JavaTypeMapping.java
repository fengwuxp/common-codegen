package com.wuxp.codegen.mapping;

import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.languages.dart.DartClassMeta;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import com.wuxp.codegen.model.mapping.JavaArrayClassTypeMark;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


/**
 * 处理java Type的类型映射
 *
 * @author wuxp
 */
@Slf4j
public class JavaTypeMapping extends AbstractLanguageTypeMapping<JavaCodeGenClassMeta> {

  private static final Map<Class<?>, CommonCodeGenClassMeta> JAVA_DEFAULT_BASE_MAPPING = new LinkedHashMap<>();

  static {

    //设置基础的数据类型映射
    JAVA_DEFAULT_BASE_MAPPING.put(Object.class, JavaCodeGenClassMeta.OBJECT);
    JAVA_DEFAULT_BASE_MAPPING.put(Date.class, JavaCodeGenClassMeta.DATE);
    JAVA_DEFAULT_BASE_MAPPING.put(LocalDate.class, JavaCodeGenClassMeta.LOCAL_DATE);
    JAVA_DEFAULT_BASE_MAPPING.put(LocalDateTime.class, JavaCodeGenClassMeta.LOCAL_DATE_TIME);
    JAVA_DEFAULT_BASE_MAPPING.put(void.class, JavaCodeGenClassMeta.VOID);

    JAVA_DEFAULT_BASE_MAPPING.put(Boolean.class, JavaCodeGenClassMeta.BOOLEAN);
    JAVA_DEFAULT_BASE_MAPPING.put(boolean.class, JavaCodeGenClassMeta.BOOLEAN);
    JAVA_DEFAULT_BASE_MAPPING.put(String.class, JavaCodeGenClassMeta.STRING);
    JAVA_DEFAULT_BASE_MAPPING.put(CharSequence.class, JavaCodeGenClassMeta.CHAR_SEQUENCE);
    JAVA_DEFAULT_BASE_MAPPING.put(char.class, JavaCodeGenClassMeta.CHAR);

    JAVA_DEFAULT_BASE_MAPPING.put(Number.class, JavaCodeGenClassMeta.NUMBER);
    JAVA_DEFAULT_BASE_MAPPING.put(BigDecimal.class, JavaCodeGenClassMeta.BIG_DECIMAL);
    JAVA_DEFAULT_BASE_MAPPING.put(double.class, JavaCodeGenClassMeta.DOUBLE);
    JAVA_DEFAULT_BASE_MAPPING.put(Double.class, JavaCodeGenClassMeta.DOUBLE);
    JAVA_DEFAULT_BASE_MAPPING.put(Float.class, JavaCodeGenClassMeta.FLOAT);
    JAVA_DEFAULT_BASE_MAPPING.put(float.class, JavaCodeGenClassMeta.FLOAT);
    JAVA_DEFAULT_BASE_MAPPING.put(long.class, JavaCodeGenClassMeta.LONG);
    JAVA_DEFAULT_BASE_MAPPING.put(Long.class, JavaCodeGenClassMeta.LONG);
    JAVA_DEFAULT_BASE_MAPPING.put(Integer.class, JavaCodeGenClassMeta.INTEGER);
    JAVA_DEFAULT_BASE_MAPPING.put(int.class, JavaCodeGenClassMeta.INTEGER);
    JAVA_DEFAULT_BASE_MAPPING.put(Short.class, JavaCodeGenClassMeta.SHORT);
    JAVA_DEFAULT_BASE_MAPPING.put(short.class, JavaCodeGenClassMeta.SHORT);
    JAVA_DEFAULT_BASE_MAPPING.put(Byte.class, JavaCodeGenClassMeta.BYTE);
    JAVA_DEFAULT_BASE_MAPPING.put(byte.class, JavaCodeGenClassMeta.BYTE);

    JAVA_DEFAULT_BASE_MAPPING.put(Map.class, JavaCodeGenClassMeta.MAP);
    JAVA_DEFAULT_BASE_MAPPING.put(Set.class, JavaCodeGenClassMeta.SET);
    JAVA_DEFAULT_BASE_MAPPING.put(List.class, JavaCodeGenClassMeta.LIST);
    JAVA_DEFAULT_BASE_MAPPING.put(Collection.class, JavaCodeGenClassMeta.COLLECTION);
    JAVA_DEFAULT_BASE_MAPPING.put(JavaArrayClassTypeMark.class, JavaCodeGenClassMeta.JAVA_ARRAY_CLASS_TYPE_MARK);
    JAVA_DEFAULT_BASE_MAPPING.put(CommonsMultipartFile.class, JavaCodeGenClassMeta.FILE);
    JAVA_DEFAULT_BASE_MAPPING.put(InputStreamResource.class, JavaCodeGenClassMeta.FILE);
  }

  public JavaTypeMapping(LanguageParser<JavaCodeGenClassMeta> languageParser,
      Map<Class<?>, CommonCodeGenClassMeta> baseTypeMappingMap,
      Map<Class<?>, CommonCodeGenClassMeta> customizeTypeMappingMap,
      Map<Class<?>, Class<?>[]> customizeJavaMappingMap) {
    super(languageParser, baseTypeMappingMap, customizeTypeMappingMap, customizeJavaMappingMap);
  }

  @Override
  protected Map<Class<?>, CommonCodeGenClassMeta> getBaseTypeMappingMap() {
    return JAVA_DEFAULT_BASE_MAPPING;
  }

  @Override
  protected JavaCodeGenClassMeta getAnyOrObjectType() {
    return JavaCodeGenClassMeta.OBJECT;
  }

  @Override
  protected JavaCodeGenClassMeta newCommonCodedInstance() {
    return new JavaCodeGenClassMeta();
  }
}
