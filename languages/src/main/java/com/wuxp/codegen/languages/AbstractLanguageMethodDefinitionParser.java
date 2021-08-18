package com.wuxp.codegen.languages;

import com.wuxp.codegen.comment.LanguageCommentDefinitionDescriber;
import com.wuxp.codegen.core.parser.LanguageMethodDefinitionParser;
import com.wuxp.codegen.core.strategy.PackageNameConvertStrategy;
import com.wuxp.codegen.core.util.ToggleCaseUtils;
import com.wuxp.codegen.meta.annotations.factories.NamedAnnotationMate;
import com.wuxp.codegen.meta.util.RequestMappingUtils;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.enums.AccessPermission;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.languages.java.JavaParameterMeta;
import com.wuxp.codegen.model.mapping.JavaArrayClassTypeMark;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Parameter;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wuxp
 */
public abstract class AbstractLanguageMethodDefinitionParser<M extends CommonCodeGenMethodMeta> extends DelegateLanguagePublishParser implements LanguageMethodDefinitionParser<M> {

    /**
     * 默认合并后的参数名称
     */
    private static final String DEFAULT_MARGE_PARAMS_NAME = "req";

    /**
     * 包名映射策略
     */
    private final PackageNameConvertStrategy packageMapStrategy;

    protected AbstractLanguageMethodDefinitionParser(LanguageTypeDefinitionPublishParser<? extends CommonCodeGenClassMeta> languageElementDefinitionEventParser,
                                                     PackageNameConvertStrategy packageMapStrategy) {
        super(languageElementDefinitionEventParser);
        this.packageMapStrategy = packageMapStrategy;
    }

    @Override
    public M parse(JavaMethodMeta methodMeta) {
        LanguageTypeDefinitionAssert.isValidSpringWebMethod(methodMeta);
        // method转换
        M result = parseMethodInner(methodMeta);
        result.setAnnotations(parseAnnotatedElement(methodMeta.getMethod()))
                .setSource(methodMeta.getMethod())
                .setDeclaringClassMeta(methodMeta.getDeclaringClassMeta())
                .setReturnTypes(getReturnTypes(methodMeta))
                .setAccessPermission(methodMeta.getAccessPermission())
                .setComments(extractComments(methodMeta))
                .setName(methodMeta.getName());
        return result;
    }

    /**
     * @return 是否需要合并方法的请求参数
     */
    protected boolean isMargeMethodParams() {
        return false;
    }

    private M parseMethodInner(JavaMethodMeta methodMeta) {
        if (this.isMargeMethodParams()) {
            return margeParamsAndParseMethod(methodMeta);
        } else {
            return parseMethod(methodMeta);
        }
    }


    /**
     * 方法参数处理流程
     * 1: 参数过滤（过滤掉控制器方法中servlet相关的参数等等）
     * 2：转换参数上的注解
     */
    private M parseMethod(JavaMethodMeta methodMeta) {

        final Map<String/*参数名称*/, CommonCodeGenClassMeta/*参数类型描述*/> codeGenParams = new LinkedHashMap<>();
        final Map<String/*参数名称*/, CommonCodeGenAnnotation[]> codeGenParamAnnotations = new LinkedHashMap<>();

        // 遍历参数列表进行转换
        for (Map.Entry<String, Class<?>[]> entry : methodMeta.getParams().entrySet()) {
            String parameterName = entry.getKey();
            getParameterMeta(methodMeta, parameterName, entry.getValue()).ifPresent(parameterMeta -> {
                codeGenParams.put(parameterName, parameterMeta);
                codeGenParamAnnotations.put(parameterName, parameterMeta.getAnnotations());
            });
        }
        M result = newElementInstance();
        result.setParams(codeGenParams)
                .setParamAnnotations(codeGenParamAnnotations);
        return result;
    }

    private Optional<CommonCodeGenClassMeta> getParameterMeta(JavaMethodMeta methodMeta, String parameterName, Class<?>[] parameterTypes) {
        Optional<CommonCodeGenClassMeta> optionalMeta = this.publishParseOfNullable(parameterTypes[0]);
        return optionalMeta.map(result -> {
            CommonCodeGenClassMeta classMeta = new CommonCodeGenClassMeta();
            BeanUtils.copyProperties(result, classMeta);
            classMeta.setTypeVariables(getCodegenClassMetas(parameterTypes));
            classMeta.setAnnotations(parseAnnotatedElement(methodMeta.getParameters().get(parameterName)));
            return classMeta;
        });
    }

    private CommonCodeGenClassMeta[] getCodegenClassMetas(Class<?>[] classes) {
        return this.publishParse(Arrays.asList(classes))
                .stream()
                .map(CommonCodeGenClassMeta.class::cast)
                .toArray(CommonCodeGenClassMeta[]::new);
    }

    private M margeParamsAndParseMethod(JavaMethodMeta methodMeta) {

        if (isOnlySingleComplexParameter(methodMeta)) {
            // 方法只存在一个复杂参数
            return parseMethod(methodMeta);
        }

        // 用于缓存合并的参数 filedList，合并复杂参数
        final Set<CommonCodeGenFiledMeta> filedMetas = new LinkedHashSet<>(resolveComplexParameters(methodMeta));
        // 合并简单参数
        filedMetas.addAll(meagreSimpleParameters(methodMeta));

        // 参数的元数据类型信息
        final CommonCodeGenClassMeta argsClassMeta = new CommonCodeGenClassMeta();
        argsClassMeta.setFieldMetas(filedMetas.toArray(new CommonCodeGenFiledMeta[]{}));
        // 为了防止重复名称，使用类名加方法名称
        String name = MessageFormat.format("{0}{1}Req",
                this.packageMapStrategy.convertClassName(methodMeta.getMethod().getDeclaringClass()),
                ToggleCaseUtils.toggleFirstChart(methodMeta.getName()));
        argsClassMeta.setName(name);
        argsClassMeta.setPackagePath(this.packageMapStrategy.genPackagePath(new String[]{DEFAULT_MARGE_PARAMS_NAME, name}));
        argsClassMeta.setAnnotations(new CommonCodeGenAnnotation[]{});
        argsClassMeta.setComments(new String[]{"合并方法参数生成的类"});
        argsClassMeta.setDependencies(resolveCodegenDependencies(filedMetas));
        argsClassMeta.setNeedGenerate(true);
        argsClassMeta.setNeedImport(true);


        LinkedHashMap<String, CommonCodeGenClassMeta> params = new LinkedHashMap<>();
        //请求参数名称，固定为req
        params.put(DEFAULT_MARGE_PARAMS_NAME, argsClassMeta);
        M result = newElementInstance();
        result.setParams(params)
                .setParamAnnotations(resolveCodegenParamAnnotations(filedMetas));
        return result;
    }

    private Map<String, CommonCodeGenAnnotation[]> resolveCodegenParamAnnotations(Set<CommonCodeGenFiledMeta> filedMetas) {
        final Map<String/*参数名称*/, CommonCodeGenAnnotation[]> codeGenParamAnnotations = new LinkedHashMap<>();
        filedMetas.forEach(filed -> codeGenParamAnnotations.put(filed.getName(), filed.getAnnotations()));
        return codeGenParamAnnotations;
    }

    private Map<String, CommonCodeGenClassMeta> resolveCodegenDependencies(Set<CommonCodeGenFiledMeta> filedMetas) {
        return filedMetas.stream()
                .map(CommonCodeGenFiledMeta::getFiledTypes)
                .map(Arrays::asList)
                .flatMap(Collection::stream)
                .filter(meta -> Boolean.TRUE.equals(meta.getNeedGenerate()))
                .distinct()
                .collect(Collectors.toMap(CommonCodeGenClassMeta::getName, value -> value));
    }

    private boolean isOnlySingleComplexParameter(JavaMethodMeta methodMeta) {
        List<Class<?>> params = methodMeta.getParams()
                .values()
                .stream()
                .map(Arrays::asList)
                .flatMap(Collection::stream)
                .filter(clazz -> !JavaArrayClassTypeMark.class.equals(clazz))
                .collect(Collectors.toList());
        if (params.size() > 1) {
            return false;
        }
        return params.stream()
                .filter(JavaTypeUtils::isNoneJdkComplex)
                .count() == 1;
    }

    // 合并简单参数
    private List<CommonCodeGenFiledMeta> meagreSimpleParameters(JavaMethodMeta methodMeta) {
        List<CommonCodeGenFiledMeta> results = new ArrayList<>();
        methodMeta.getParams().forEach((parameterName, classes) -> {
            // 注解
            Parameter parameter = methodMeta.getParameters().get(parameterName);
            // mock一个Java 参数元数据对象
            JavaParameterMeta javaParameterMeta = new JavaParameterMeta();
            javaParameterMeta.setTypes(classes)
                    .setIsTransient(false)
                    .setIsVolatile(false);
            javaParameterMeta.setAccessPermission(AccessPermission.PUBLIC);
            javaParameterMeta.setAnnotations(parameter.getAnnotations());
            javaParameterMeta.setName(parameterName);
            javaParameterMeta.setParameter(methodMeta.getParameters().get(parameterName));

            CommonCodeGenFiledMeta filedMeta = this.publishParse(javaParameterMeta);
            if (filedMeta == null) {
                return;
            }
            filedMeta.setName(resolveParameterName(parameter, parameterName));
            results.add(filedMeta);
        });
        return results;
    }

    private String resolveParameterName(Parameter parameter, String defaultName) {
        return Arrays.stream(parameter.getAnnotations())
                .filter(this::isSpringWebParameterAnnotation)
                .map(AnnotationMetaFactoryHolder::getAnnotationMetaFactory)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(NamedAnnotationMate.class::isInstance)
                .map(NamedAnnotationMate.class::cast)
                .map(NamedAnnotationMate::finalyName)
                .filter(StringUtils::hasText)
                .findFirst()
                .orElse(defaultName);
    }

    private boolean isSpringWebParameterAnnotation(Annotation annotation) {
        return JavaTypeUtils.isAssignableFrom(annotation.annotationType(), RequestParam.class) ||
                JavaTypeUtils.isAssignableFrom(annotation.annotationType(), PathVariable.class) ||
                JavaTypeUtils.isAssignableFrom(annotation.annotationType(), CookieValue.class) ||
                JavaTypeUtils.isAssignableFrom(annotation.annotationType(), RequestHeader.class);
    }


    /**
     * 合并复杂参数的字段
     *
     * @return 是否存在复杂类型的参数
     */
    private List<CommonCodeGenFiledMeta> resolveComplexParameters(JavaMethodMeta methodMeta) {
        return methodMeta.getParams().values()
                .stream()
                .map(this::parseComplexParameterTypes)
                .flatMap(Collection::stream)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<List<CommonCodeGenFiledMeta>> parseComplexParameterTypes(Class<?>[] parameterTypes) {
        return Arrays.stream(parameterTypes)
                .filter(clazz -> !JavaArrayClassTypeMark.class.equals(clazz))
                .filter(clazz -> !clazz.isEnum())
                //非jdk中的复杂对象
                .filter(JavaTypeUtils::isNoneJdkComplex)
                .map(this::publishParseOfNullable)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(CommonCodeGenClassMeta.class::cast)
                .map(CommonCodeGenClassMeta::getFieldMetas)
                .map(Arrays::asList)
                .collect(Collectors.toList());
    }


    private CommonCodeGenClassMeta[] getReturnTypes(JavaMethodMeta javaMethodMeta) {
        //处理返回值
        Class<?>[] returnTypes = javaMethodMeta.getReturnType();
        if (isStreamReturnType(javaMethodMeta)) {
            returnTypes = new Class[]{InputStreamResource.class};
        } else {
            returnTypes = mappingClasses(returnTypes);
        }
        return getCodegenClassMetas(returnTypes);
    }

    private boolean isStreamReturnType(JavaMethodMeta javaMethodMeta) {
        return RequestMappingUtils.findRequestMappingAnnotation(javaMethodMeta.getMethod().getDeclaredAnnotations())
                .map(requestMappingMate -> {
                    String[] produces = requestMappingMate.produces();
                    // 文件类型
                    return Arrays.asList(produces).contains(MediaType.APPLICATION_OCTET_STREAM_VALUE);
                })
                .orElse(false);
    }

    private String[] extractComments(JavaMethodMeta methodMeta) {
        // 注解转注释
        List<String> comments = LanguageCommentDefinitionDescriber.extractComments(methodMeta.getMethod());
        comments.addAll(LanguageCommentDefinitionDescriber.extractComments(ElementType.METHOD, methodMeta.getReturnType()));
        return comments.toArray(new String[]{});
    }
}
