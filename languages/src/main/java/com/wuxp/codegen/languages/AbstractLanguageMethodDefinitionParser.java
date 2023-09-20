package com.wuxp.codegen.languages;

import com.wuxp.codegen.comment.LanguageCommentDefinitionDescriber;
import com.wuxp.codegen.core.parser.LanguageMethodDefinitionParser;
import com.wuxp.codegen.core.strategy.CombineTypeDescStrategy;
import com.wuxp.codegen.core.strategy.PackageNameConvertStrategy;
import com.wuxp.codegen.core.util.ToggleCaseUtils;
import com.wuxp.codegen.meta.annotations.factories.NamedAnnotationMate;
import com.wuxp.codegen.meta.util.RequestMappingUtils;
import com.wuxp.codegen.model.*;
import com.wuxp.codegen.model.enums.AccessPermission;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.languages.java.JavaParameterMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import com.wuxp.codegen.types.SimpleCombineTypeDescStrategy;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
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

    private static final String MARGE_PARAMS_TAG_NAME = "margeParams";


    /**
     * 泛型合并策略
     */
    private final CombineTypeDescStrategy combineTypeDescStrategy = new SimpleCombineTypeDescStrategy();

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
                .setJavaMethodMeta(methodMeta)
                .setDeclaringClassMeta(methodMeta.getDeclaringClassMeta())
                .setReturnTypes(getReturnTypes(methodMeta))
                .setAccessPermission(methodMeta.getAccessPermission())
                .setComments(extractComments(methodMeta))
                .setName(methodMeta.getName());
        result.setDependencies(getDependencies(result));
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

    private List<CommonCodeGenClassMeta> getDependencies(M result) {
        List<CommonCodeGenClassMeta> dependencies = new ArrayList<>();
        result.getParams().values().forEach(classMeta -> {
            if (Boolean.TRUE.equals(classMeta.getTag(MARGE_PARAMS_TAG_NAME))) {
                // 合并参数
                dependencies.add(classMeta);
                dependencies.addAll(classMeta.getDependencies().values());
            } else {
                dependencies.add(this.publishParse(classMeta.getSource()));
            }
        });
        dependencies.addAll(Arrays.asList(result.getReturnTypes()));
        if (!ObjectUtils.isEmpty(result.getTypeVariables())) {
            dependencies.addAll(Arrays.asList(result.getTypeVariables()));
        }
        return dependencies;
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

        if (isArrayOrCollectComplexParams(methodMeta)) {
            return arrayOrCollectComplexParams(methodMeta);
        } else {
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
            argsClassMeta.getTags().put(MARGE_PARAMS_TAG_NAME, true);
            //请求参数名称，固定为req
            Map<String, CommonCodeGenClassMeta> params = new LinkedHashMap<>();
            params.put(DEFAULT_MARGE_PARAMS_NAME, argsClassMeta);

            M result = newElementInstance();
            result.setParams(params)
                    .setParamAnnotations(resolveCodegenParamAnnotations(filedMetas));
            return result;
        }

    }

    private M arrayOrCollectComplexParams(JavaMethodMeta methodMeta) {
        // 方法只存在一个复杂参数
        String firstPramName = this.getFirstPramName(methodMeta);
        Class<?>[] classes = getFirstPramClasses(methodMeta);

        // 参数的元数据类型信息
        List<CommonCodeGenClassMeta> metas = Arrays.stream(classes).map(this::publishParse)
                .map(CommonCodeGenClassMeta.class::cast)
                .collect(Collectors.toList());

        String genericDescription = combineTypeDescStrategy.combine(metas.toArray(new CommonCodeGenClassMeta[0]));
        final CommonCodeGenClassMeta argsClassMeta = new CommonCodeGenClassMeta();
        BeanUtils.copyProperties(metas.remove(0), argsClassMeta);
        argsClassMeta.setSource(classes[0]);
        argsClassMeta.setGenericDescription(genericDescription);
        Map<String, CommonCodeGenClassMeta> dependencies = new HashMap<>();
        metas.forEach(commonCodeGenClassMeta -> dependencies.put(commonCodeGenClassMeta.getName(), commonCodeGenClassMeta));
        argsClassMeta.setDependencies(dependencies);
        argsClassMeta.getTags().put(MARGE_PARAMS_TAG_NAME, true);
        Map<String, CommonCodeGenClassMeta> params = new LinkedHashMap<>();
        params.put(firstPramName, argsClassMeta);

        Map<String, CommonCodeGenAnnotation[]> paramAnnotations = new HashMap<>();
        paramAnnotations.put(firstPramName, this.parseAnnotatedElement(methodMeta.getParameters().get(firstPramName)));
        M result = newElementInstance();

        result.setParams(params)
                .setParamAnnotations(paramAnnotations);
        return result;
    }

    /**
     * 是否为复杂的集合或数组参数
     *
     * @param methodMeta java 方法元数据描述
     * @return if <code>true</code> 是
     */
    private boolean isArrayOrCollectComplexParams(JavaMethodMeta methodMeta) {
        if (methodMeta.getParams().size() > 1) {
            return false;
        }
        Class<?>[] classes = getFirstPramClasses(methodMeta);
        if (ObjectUtils.isEmpty(classes) || classes.length < 1) {
            return false;
        }
        if (JavaTypeUtils.isArrayMark(classes[0]) || JavaTypeUtils.isCollection(classes[0])) {
            return JavaTypeUtils.isNoneJdkComplex(classes[1]);
        }

        if (JavaTypeUtils.isMap(classes[0])) {
            Assert.isTrue(JavaTypeUtils.isNumber(classes[1]) || JavaTypeUtils.isString(classes[1]) || JavaTypeUtils.isEnum(classes[1]),
                    "map 参数的 key 只能是数字、字符或枚举");
            return JavaTypeUtils.isNoneJdkComplex(classes[2]);
        }
        return false;
    }

    private String getFirstPramName(JavaMethodMeta methodMeta) {
        Map<String, Class<?>[]> metaParams = methodMeta.getParams();
        if (metaParams.isEmpty()) {
            return null;
        }
        return metaParams.keySet().toArray(new String[0])[0];
    }

    private Class<?>[] getFirstPramClasses(JavaMethodMeta methodMeta) {
        String paramName = getFirstPramName(methodMeta);
        if (!StringUtils.hasText(paramName)) {
            return new Class<?>[0];
        }
        return methodMeta.getParams().get(paramName);
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
//                .filter(clazz -> !JavaArrayClassTypeMark.class.equals(clazz))
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
                .map(NamedAnnotationMate::name)
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
