package com.wuxp.codegen.languages.java;

import com.wuxp.codegen.core.parser.enhance.LanguageEnhancedProcessor;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.lang.annotation.Annotation;
import java.util.*;

/**
 * 忽略被某些注解标记的参数 默认会忽略被spring mvc Attribute相关的注解标记的参数
 *
 * @author wuxp
 * @see RequestAttribute
 * @see SessionAttribute
 * @see SessionAttributes
 * @see ModelAttribute
 */
public class IgnoreParamsByAnnotationLanguageEnhancedProcessor implements
    LanguageEnhancedProcessor<CommonCodeGenClassMeta, CommonCodeGenMethodMeta, CommonCodeGenFiledMeta> {


  private static final List<Class<? extends Annotation>> ATTRIBUTE_ANNOTATIONS = Arrays.asList(
      RequestAttribute.class,
      SessionAttribute.class,
      SessionAttributes.class,
      ModelAttribute.class
  );

  private final Set<Class<? extends Annotation>> ignoreAnnotations;

  public static IgnoreParamsByAnnotationLanguageEnhancedProcessor of(Class<? extends Annotation>... annotationTypes) {
    return new IgnoreParamsByAnnotationLanguageEnhancedProcessor(Arrays.asList(annotationTypes));
  }

  public static IgnoreParamsByAnnotationLanguageEnhancedProcessor of(Collection<Class<? extends Annotation>> ignoreAnnotations) {
    return new IgnoreParamsByAnnotationLanguageEnhancedProcessor(ignoreAnnotations);
  }

  private IgnoreParamsByAnnotationLanguageEnhancedProcessor(Collection<Class<? extends Annotation>> ignoreAnnotations) {
    ignoreAnnotations.addAll(ATTRIBUTE_ANNOTATIONS);
    this.ignoreAnnotations = new HashSet<>(ignoreAnnotations);
  }

  @Override
  public CommonCodeGenMethodMeta enhancedProcessingMethod(CommonCodeGenMethodMeta methodMeta, JavaMethodMeta javaMethodMeta,
      JavaClassMeta classMeta) {

    Set<String> needRemoveNames = new HashSet<>();
    javaMethodMeta.getParamAnnotations().forEach((name, annotations) -> {
      for (Annotation annotation : annotations) {
        if (ignoreAnnotations.contains(annotation.annotationType())) {
          needRemoveNames.add(name);
          break;
        }
      }
    });
    Map<String, CommonCodeGenClassMeta> params = methodMeta.getParams();
    Map<String, CommonCodeGenAnnotation[]> paramAnnotations = methodMeta.getParamAnnotations();
    for (String name : needRemoveNames) {
      params.remove(name);
      paramAnnotations.remove(name);
    }
    return methodMeta;
  }
}
