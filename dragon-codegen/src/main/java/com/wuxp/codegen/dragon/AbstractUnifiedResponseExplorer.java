package com.wuxp.codegen.dragon;

import static com.wuxp.codegen.model.constant.SpringAnnotationClassConstant.SPRING_MAPPING_ANNOTATIONS;

import com.wuxp.codegen.core.UnifiedResponseExplorer;
import com.wuxp.codegen.core.config.CodegenConfigHolder;
import com.wuxp.codegen.core.parser.JavaClassParser;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.languages.AbstractLanguageParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.mapping.BaseTypeMapping;
import com.wuxp.codegen.model.mapping.TypeMapping;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * 统一响应对象探测和自动映射
 *
 * @author wuxp
 */
@Slf4j
public abstract class AbstractUnifiedResponseExplorer implements UnifiedResponseExplorer {

  private final static JavaClassParser CLASS_PARSER = new JavaClassParser(true);

  private final LanguageParser languageParser;


  public AbstractUnifiedResponseExplorer(LanguageParser languageParser) {
    this.languageParser = languageParser;
  }

  @Override
  public void probe(Set<Class<?>> classes) {
    AbstractLanguageParser languageParser = (AbstractLanguageParser) this.languageParser;
    TypeMapping typeMapping = languageParser.getLanguageTypeMapping().getBaseTypeMapping();
    if (typeMapping == null) {
      return;
    }

    List<? extends Class<?>> returnTypes = classes.stream()
        .map(CLASS_PARSER::parse)
        .map(JavaClassMeta::getMethodMetas)
        .map(Arrays::asList)
        .flatMap(Collection::stream)
        .filter(javaMethodMeta -> javaMethodMeta.existAnnotation(SPRING_MAPPING_ANNOTATIONS))
        .map(JavaMethodMeta::getReturnType)
        .map(clazzList -> clazzList[0])
        .distinct()
        .collect(Collectors.toList());
    if (returnTypes.size() > 2) {
      log.warn("控制器的方法返回值的类型超过2个，type size={}", returnTypes.size());
      return;
    }
    if (typeMapping instanceof BaseTypeMapping) {
      setUnifiedResponseTypeMapping(returnTypes, (BaseTypeMapping) typeMapping);
    }
  }

  protected void setUnifiedResponseTypeMapping(List<? extends Class<?>> returnTypes, BaseTypeMapping baseTypeMapping) {
    Class<?> unifiedResponseType = returnTypes.get(0);
    CommonCodeGenClassMeta responseType = CodegenConfigHolder.getConfig().getUnifiedResponseType();
    if (responseType != null) {
      baseTypeMapping.tryAddTypeMapping(unifiedResponseType, responseType);
    }
  }
}

