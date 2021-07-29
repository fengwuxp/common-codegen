package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.exception.CodegenRuntimeException;
import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.languages.factory.TypescriptLanguageMetaInstanceFactory;
import com.wuxp.codegen.meta.annotations.factories.AnnotationMate;
import com.wuxp.codegen.meta.annotations.factories.spring.RequestMappingMetaFactory;
import com.wuxp.codegen.meta.util.RequestMappingUtils;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.constant.MappingAnnotationPropNameConstant;
import com.wuxp.codegen.model.constant.TypescriptFeignMediaTypeConstant;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptFieldMate;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 抽象的typescript parser
 *
 * @author wxup
 */
@Slf4j
public abstract class AbstractTypescriptParser extends
        AbstractLanguageParser<TypescriptClassMeta, CommonCodeGenMethodMeta, TypescriptFieldMate> {


    public AbstractTypescriptParser(PackageMapStrategy packageMapStrategy,
                                    CodeGenMatchingStrategy genMatchingStrategy,
                                    Collection<CodeDetect> codeDetects) {
        super(new TypescriptLanguageMetaInstanceFactory(),
                packageMapStrategy,
                genMatchingStrategy,
                codeDetects);
        //根据java 类进行匹配
        codeGenMatchers.add(clazz -> clazz.isEnum() || JavaTypeUtils.isNoneJdkComplex(clazz) || clazz.isAnnotation());
    }

    @Override
    public TypescriptClassMeta parse(Class<?> source) {
        TypescriptClassMeta result = super.parse(source);
        if (result == null) {
            return null;
        }
        handleEnumTypes(result);
        return result;
    }

    private void handleEnumTypes(TypescriptClassMeta result) {
        if (result.getEnumConstants() != null) {
            String enumTypes = Arrays.stream(result.getEnumConstants())
                    .map(CommonCodeGenFiledMeta::getName)
                    .map(name -> String.format("'%s'", name))
                    .collect(Collectors.joining(" | "));
            result.setEnumTypes(enumTypes);
            result.setNeedImport(false);
        }
    }

    @Override
    protected TypescriptFieldMate converterField(JavaFieldMeta javaFieldMeta, JavaClassMeta classMeta) {
        TypescriptFieldMate typescriptFieldMate = super.converterField(javaFieldMeta, classMeta);
        if (typescriptFieldMate == null) {
            return null;
        }
        //是否必填
        typescriptFieldMate.setRequired(javaFieldMeta.existAnnotation(NotNull.class, NotBlank.class, NotEmpty.class));
        return typescriptFieldMate;
    }


    @Override
    protected CommonCodeGenMethodMeta converterMethod(JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta,
                                                      TypescriptClassMeta codeGenClassMeta) {

        CommonCodeGenMethodMeta commonCodeGenMethodMeta = super.converterMethod(javaMethodMeta, classMeta, codeGenClassMeta);
        if (commonCodeGenMethodMeta == null) {
            return null;
        }

        if (!methodReturnTypeIsFile(javaMethodMeta)) {
            //处理返回值
            Class<?>[] returnTypes = javaMethodMeta.getReturnType();
            Class<?> mapClazz = Arrays.stream(returnTypes)
                    .filter(JavaTypeUtils::isMap)
                    .findAny()
                    .orElse(null);

            List<Class<?>> newTypes = Arrays.stream(returnTypes).collect(Collectors.toList());
            //处理map 类型的对象
            if (mapClazz != null) {
                int length = newTypes.size();
                for (int i = 0; i < length; i++) {
                    if (newTypes.get(i).equals(mapClazz)) {
                        int i1 = i + 1;
                        if (i1 >= length) {
                            //没有设置 Map 的key value的泛型
                            log.warn(MessageFormat.format("处理类：{0}上的方法：{1},发现非预期的情况", classMeta.getClassName(), javaMethodMeta.getName()));
                            newTypes.add(Object.class);
                            newTypes.add(Object.class);
                        }
                        Class<?> keyClazz = newTypes.get(i1);
                        if (!JavaTypeUtils.isJavaBaseType(keyClazz)) {
                            // TODO 如果map的key不是基础数据类
                            log.error("类 {} 的 {} 方法的返回值Map类型的key不是基础数据类型或字符串", classMeta.getName(), javaMethodMeta.getName());
                        }
                        break;
                    }
                }
            }
            Class[] newReturnTypes = newTypes.toArray(new Class[0]);
            List<TypescriptClassMeta> mapping = this.languageTypeMapping.mapping(newReturnTypes);
            if (newTypes.size() > returnTypes.length) {
                //返回值类型列表发生变化，重新计算返回值类型
                returnTypes = newReturnTypes;
                javaMethodMeta.setReturnType(newReturnTypes);
                mapping = this.languageTypeMapping.mapping(newReturnTypes);
                commonCodeGenMethodMeta.setReturnTypes(mapping.toArray(new CommonCodeGenClassMeta[0]));
            }
            // 移除所有的 Promise Type
            mapping.remove(TypescriptClassMeta.PROMISE);
            if (mapping.isEmpty()) {
                //解析失败
                throw new CodegenRuntimeException(String.format("解析类 %s 上的方法 %s 的返回值类型 %s 失败",
                        classMeta.getClassName(),
                        javaMethodMeta.getName(),
                        this.classToNamedString(returnTypes)));
            }
            //域对象类型描述
            commonCodeGenMethodMeta.setReturnTypes(mapping.toArray(new CommonCodeGenClassMeta[]{}));
        }
        //将需要导入的加入依赖列表
        Arrays.stream(commonCodeGenMethodMeta.getReturnTypes())
                .filter(CommonCodeGenClassMeta::getNeedImport)
                .forEach(returnType -> ((Map<String, CommonCodeGenClassMeta>) codeGenClassMeta.getDependencies()).put(returnType.getName(), returnType));

        //增强处理
        this.enhancedProcessingMethod(commonCodeGenMethodMeta, javaMethodMeta, classMeta);

        return commonCodeGenMethodMeta;
    }


    @Override
    protected void enhancedProcessingClass(TypescriptClassMeta methodMeta, JavaClassMeta classMeta) {

    }

    @Override
    protected void enhancedProcessingMethod(CommonCodeGenMethodMeta methodMeta, JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta) {

    }

    /**
     * 增强处理注解
     *
     * @param codeGenAnnotation
     * @param annotation
     * @param annotationOwner
     */
    @Override
    protected void enhancedProcessingAnnotation(CommonCodeGenAnnotation codeGenAnnotation, AnnotationMate annotation, Object annotationOwner) {
        if (annotationOwner instanceof Method) {
            Optional<RequestMappingMetaFactory.RequestMappingMate> optionalRequestMappingMate = RequestMappingUtils.findRequestMappingAnnotation(new Annotation[]{annotation});
            //spring的mapping注解
            if (optionalRequestMappingMate.isPresent()) {
                Method method = (Method) annotationOwner;
                if (optionalRequestMappingMate.get().isGetMethod()) {
                    // GET请求 不需要在而外的处理
                    return;
                }
                String produces = codeGenAnnotation.getNamedArguments().get(MappingAnnotationPropNameConstant.PRODUCES);
                if (StringUtils.hasText(produces)) {
                    return;
                } else {
                    codeGenAnnotation.getNamedArguments().remove(MappingAnnotationPropNameConstant.PRODUCES);
                }
                //判断方法参数是否有RequestBody注解
                Optional<RequestBody> optionalRequestBody = RequestMappingUtils.findRequestBody(Arrays.stream(method.getParameterAnnotations())
                        .filter(Objects::nonNull)
                        .filter(annotations -> annotations.length > 0)
                        .map(Arrays::asList)
                        .flatMap(Collection::stream).toArray(Annotation[]::new));
                if (optionalRequestBody.isPresent()) {
                    produces = TypescriptFeignMediaTypeConstant.APPLICATION_JSON_UTF8;
                } else {
                    // 如果没有 RequestBody 则认为是已表单的方式提交的参数
                    // 是spring的Mapping注解
                    produces = TypescriptFeignMediaTypeConstant.FORM_DATA;
                }
                if (!StringUtils.hasText(produces)) {
                    return;
                }
                codeGenAnnotation.getNamedArguments().put(MappingAnnotationPropNameConstant.PRODUCES, produces);
            }
        }
    }

    @Override
    protected boolean needMargeMethodParams() {
        return true;
    }
}
