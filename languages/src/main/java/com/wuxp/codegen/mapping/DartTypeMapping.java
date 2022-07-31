package com.wuxp.codegen.mapping;

import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.languages.dart.DartClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.model.mapping.JavaArrayClassTypeMark;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


/**
 * 处理dart的类型映射
 *
 * @author wxup
 */
@Slf4j
public class DartTypeMapping extends AbstractLanguageTypeMapping<DartClassMeta> {

  private static final Map<Class<?>, CommonCodeGenClassMeta> DART_DEFAULT_BASE_MAPPING = new LinkedHashMap<>();

  static {
    //设置基础的数据类型映射
    DART_DEFAULT_BASE_MAPPING.put(Object.class, DartClassMeta.OBJECT);
    DART_DEFAULT_BASE_MAPPING.put(Date.class, DartClassMeta.DATE);
    DART_DEFAULT_BASE_MAPPING.put(LocalDate.class, DartClassMeta.DATE);
    DART_DEFAULT_BASE_MAPPING.put(LocalDateTime.class, DartClassMeta.DATE);
    DART_DEFAULT_BASE_MAPPING.put(Boolean.class, DartClassMeta.BOOL);
    DART_DEFAULT_BASE_MAPPING.put(String.class, DartClassMeta.STRING);
    DART_DEFAULT_BASE_MAPPING.put(Number.class, DartClassMeta.NUM);
    DART_DEFAULT_BASE_MAPPING.put(Long.class, DartClassMeta.INT);
    DART_DEFAULT_BASE_MAPPING.put(Integer.class, DartClassMeta.INT);
    DART_DEFAULT_BASE_MAPPING.put(Short.class, DartClassMeta.INT);
    DART_DEFAULT_BASE_MAPPING.put(Byte.class, DartClassMeta.INT);
    DART_DEFAULT_BASE_MAPPING.put(double.class, DartClassMeta.DOUBLE);
    DART_DEFAULT_BASE_MAPPING.put(float.class, DartClassMeta.DOUBLE);
    DART_DEFAULT_BASE_MAPPING.put(long.class, DartClassMeta.INT);
    DART_DEFAULT_BASE_MAPPING.put(short.class, DartClassMeta.INT);
    DART_DEFAULT_BASE_MAPPING.put(byte.class, DartClassMeta.INT);
    DART_DEFAULT_BASE_MAPPING.put(Map.class, DartClassMeta.BUILT_MAP);
    DART_DEFAULT_BASE_MAPPING.put(Set.class, DartClassMeta.BUILT_SET);
    DART_DEFAULT_BASE_MAPPING.put(List.class, DartClassMeta.BUILT_LIST);

    // 由于built_collection 没有导出BuiltIterable 先转化为 BuiltList
    DART_DEFAULT_BASE_MAPPING.put(Collection.class, DartClassMeta.BUILT_LIST);
    DART_DEFAULT_BASE_MAPPING.put(void.class, DartClassMeta.VOID);
    DART_DEFAULT_BASE_MAPPING.put(Void.class, DartClassMeta.VOID);

    //文件上传
    DART_DEFAULT_BASE_MAPPING.put(MultipartFile.class, DartClassMeta.FILE);
    DART_DEFAULT_BASE_MAPPING.put(InputStreamResource.class, DartClassMeta.FILE);

    // reactor TODO support rxDart
    DART_DEFAULT_BASE_MAPPING.put(Publisher.class, DartClassMeta.FUTURE);
  }

  public DartTypeMapping(LanguageParser<DartClassMeta> languageParser,
      Map<Class<?>, CommonCodeGenClassMeta> baseTypeMappingMap,
      Map<Class<?>, CommonCodeGenClassMeta> customizeTypeMappingMap,
      Map<Class<?>, Class<?>[]> customizeJavaMappingMap) {
    super(languageParser, baseTypeMappingMap, customizeTypeMappingMap, customizeJavaMappingMap);
  }

  @Override
  protected DartClassMeta mapping(Class<?> clazz) {
    if (JavaArrayClassTypeMark.class.equals(clazz)) {
      // 标记的数据数组类型
      DartClassMeta array = this.languageParser.getLanguageMetaInstanceFactory().newClassInstance();
      BeanUtils.copyProperties(DartClassMeta.BUILT_LIST, array);
      return array;
    }
    return super.mapping(clazz);
  }

  @Override
  protected Map<Class<?>, CommonCodeGenClassMeta> getBaseTypeMappingMap() {
    return DART_DEFAULT_BASE_MAPPING;
  }

  @Override
  protected DartClassMeta getAnyOrObjectType() {
    return DartClassMeta.DYNAMIC;
  }

  @Override
  protected DartClassMeta newCommonCodedInstance() {
    return new DartClassMeta();
  }
}
