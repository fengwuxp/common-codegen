package com.wuxp.codegen.languages;

import com.wuxp.codegen.comment.LanguageCommentDefinitionDescriber;
import com.wuxp.codegen.core.config.CodegenConfigHolder;
import com.wuxp.codegen.core.parser.JavaClassParser;
import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.core.strategy.PackageNameConvertStrategy;
import com.wuxp.codegen.meta.util.JavaMethodNameUtils;
import com.wuxp.codegen.meta.util.SpringControllerFilterUtils;
import com.wuxp.codegen.model.CommonBaseMeta;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.enums.AccessPermission;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.reactive.ReactorTypeSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.wuxp.codegen.core.parser.JavaClassParser.JAVA_CLASS_PARSER;

/**
 * @author wuxp
 */
@Slf4j
public abstract class AbstractLanguageTypeDefinitionParser<C extends CommonCodeGenClassMeta> extends DelegateLanguagePublishParser
        implements LanguageTypeDefinitionParser<C> {

    /**
     * java类的解析器 默认解析所有的属性 方法
     */
    private final JavaClassParser javaParser;

    /**
     * 包名映射策略
     */
    private final PackageNameConvertStrategy packageNameConvertStrategy;

    private final CacheLanguageTypeDefinitionParser<C> cacheLanguageTypeDefinitionParser;


    protected AbstractLanguageTypeDefinitionParser(LanguageTypeDefinitionPublishParser<?> languageTypeDefinitionPublishParser, PackageNameConvertStrategy packageNameConvertStrategy) {
        super(languageTypeDefinitionPublishParser);
        this.packageNameConvertStrategy = packageNameConvertStrategy;
        this.javaParser = JAVA_CLASS_PARSER;
        this.cacheLanguageTypeDefinitionParser = new CacheLanguageTypeDefinitionParser<>(this);
    }

    @Override
    public C parse(Class<?> source) {
        return cacheLanguageTypeDefinitionParser.parseOfNullable(source).orElseGet(() -> this.parseInner(source));
    }

    private C parseInner(Class<?> source) {
        if (source == Enum.class) {
            return null;
        }
        JavaClassMeta classMeta = javaParser.parse(source);
        preProcess(classMeta);
        C result = newCodeGenClassMetaAndPutCache(source);
        result.setName(this.packageNameConvertStrategy.convertClassName(source));
        result.setPackagePath(this.packageNameConvertStrategy.convert(source));
        result.setClassType(classMeta.getClassType());
        result.setIsAbstract(classMeta.getIsAbstract());
        result.setIsStatic(classMeta.getIsStatic());
        result.setAccessPermission(classMeta.getAccessPermission());
        result.setGenericDescription(getGenericDescription(result.getTypeVariables()));
        result.setComments(extractComments(classMeta));
        result.setAnnotations(parseAnnotatedElement(source));
        result.setTypeVariables(getTypeVariables(classMeta.getTypeVariables()));
        result.setSuperClass(getSupperClassMeta(classMeta));
        result.setSuperTypeVariables(getSuperTypeGenericTypeVariables(classMeta));
        if (CodegenConfigHolder.getConfig().isServerClass(source)) {
            result.setMethodMetas(getCodegenMethodMetas(classMeta));
        } else {
            result.setFieldMetas(getCodegenFiledMetas(classMeta));
        }
        result.setNeedGenerate(true);
        return resolveAllDependencies(result);
    }

    private void preProcess(JavaClassMeta classMeta) {
        // 增加对响应式编程的支持
        ReactorTypeSupport.handle(classMeta);
        // 加入对 spring 的特别处理
        SpringControllerFilterUtils.filterMethods(classMeta);
    }

    private C newCodeGenClassMetaAndPutCache(Class<?> source) {
        C result = newElementInstance();
        result.setSource(source);
        return cacheLanguageTypeDefinitionParser.put(result);
    }

    private CommonCodeGenClassMeta[] getTypeVariables(Type[] typeVariables) {
        return Arrays.stream(typeVariables)
                .map(type -> {
                    if (type instanceof Class<?>) {
                        return publishParse(type);
                    } else if (type instanceof TypeVariable) {
                        return parseType(type);
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .map(CommonCodeGenClassMeta.class::cast)
                .toArray(CommonCodeGenClassMeta[]::new);
    }

    private String getGenericDescription(CommonCodeGenClassMeta[] typeVariables) {
        // 生成类上的泛型描述
        String genericDescription = Arrays.stream(typeVariables)
                .map(CommonBaseMeta::getName)
                .collect(Collectors.joining(","));
        if (StringUtils.hasText(genericDescription)) {
            return MessageFormat.format("<{0}>", genericDescription);
        }
        return null;
    }

    private String[] extractComments(JavaClassMeta classMeta) {
        // 注解转注释
        return LanguageCommentDefinitionDescriber.extractComments(classMeta.getClazz()).toArray(new String[]{});
    }

    private C getSupperClassMeta(JavaClassMeta classMeta) {
        // 处理超类
        Class<?> superClass = classMeta.getSuperClass();
        if (superClass == Object.class) {
            return null;
        }
        // 不是object
        C superClassMeta = this.publishParse(superClass);
        if (superClassMeta == null) {
            log.warn("超类 {} 解析处理失败或被忽略", classMeta.getClassName());
            return null;
        }

        CommonCodeGenClassMeta[] supperClassTypeVariables = getSuperTypeGenericTypeVariables(classMeta).get(superClass.getSimpleName());
        if (ObjectUtils.isEmpty(supperClassTypeVariables)) {
            return superClassMeta;
        }

        C currentSupperClassMeta = this.newElementInstance();
        // 做一次值复制，防止改变缓存中的值
        BeanUtils.copyProperties(superClassMeta, currentSupperClassMeta);
        // 超类的类型变量不为空的时候，重新设置一下超类的类型变量
        currentSupperClassMeta.setTypeVariables(supperClassTypeVariables);
        return currentSupperClassMeta;
    }

    private Map<String, CommonCodeGenClassMeta[]> getSuperTypeGenericTypeVariables(JavaClassMeta classMeta) {
        /*类型，父类，接口，本身*/
        Map<String, CommonCodeGenClassMeta[]> superTypeVariables = new LinkedHashMap<>();
        // 处理超类上面的类型变量
        classMeta.getSuperTypeVariables().forEach((superClazz, classes) -> {
            if (ObjectUtils.isEmpty(classes)) {
                return;
            }
            // 处理超类上的类型变量 例如 A<T,E> extends B<C<T>,E> 这种情况
            CommonCodeGenClassMeta[] typeVariables = getTypeVariables(classes);
            superTypeVariables.put(superClazz.getSimpleName(), typeVariables);
        });
        return superTypeVariables;
    }

    private CommonCodeGenMethodMeta[] getCodegenMethodMetas(JavaClassMeta classMeta) {
        return Arrays.stream(classMeta.getMethodMetas())
                .filter(javaMethodMeta -> Boolean.FALSE.equals(javaMethodMeta.getIsStatic()))
                .filter(javaMethodMeta -> Boolean.FALSE.equals(javaMethodMeta.getIsTransient()))
                .filter(javaMethodMeta -> AccessPermission.PUBLIC.equals(javaMethodMeta.getAccessPermission()))
                .map(this::publishParse)
                .filter(Objects::nonNull)
                .map(CommonCodeGenMethodMeta.class::cast)
                .distinct()
                .toArray(CommonCodeGenMethodMeta[]::new);
    }

    private CommonCodeGenFiledMeta[] getCodegenFiledMetas(JavaClassMeta classMeta) {
        return margeFiledMetas(classMeta)
                .stream()
                .map(this::publishParse)
                .filter(Objects::nonNull)
                .map(CommonCodeGenFiledMeta.class::cast)
                .distinct()
                .toArray(CommonCodeGenFiledMeta[]::new);
    }

    private List<JavaFieldMeta> margeFiledMetas(JavaClassMeta classMeta) {
        ArrayList<JavaFieldMeta> fieldMetas = new ArrayList<>(Arrays.asList(classMeta.getFieldMetas()));
        fieldMetas.addAll(resolveGetterMethodFiledMetas(classMeta));
        return fieldMetas;
    }

    private List<JavaFieldMeta> resolveGetterMethodFiledMetas(JavaClassMeta classMeta) {
        // 如果是java bean 需要合并get方法
        // 找出java bean中不存在属性定义的get或 is方法
        List<String> filedNames = getFiledNames(classMeta);
        return Arrays.stream(classMeta.getMethodMetas())
                // 过滤掉本地方法
                .filter(javaMethodMeta -> !Boolean.TRUE.equals(javaMethodMeta.getIsNative()))
                .filter(javaMethodMeta ->
                        // 匹配getXX 或isXxx方法
                        JavaMethodNameUtils.isGetMethodOrIsMethod(javaMethodMeta.getName())
                )
                .filter(
                        javaMethodMeta -> Boolean.FALSE.equals(javaMethodMeta.getIsStatic()) && Boolean.FALSE.equals(javaMethodMeta.getIsAbstract())
                                && Boolean.FALSE.equals(javaMethodMeta.getIsTransient()))
                .filter(javaMethodMeta -> !ObjectUtils.isEmpty(javaMethodMeta.getReturnType()))
                .filter(javaMethodMeta ->
                        // 属性是否已经存在
                        !filedNames.contains(JavaMethodNameUtils.replaceGetOrIsPrefix(javaMethodMeta.getName()))
                )
                .map(this::mockJavaFieldMeta).collect(Collectors.toList());
    }

    private List<String> getFiledNames(JavaClassMeta classMeta) {
        return Arrays.stream(classMeta.getFieldMetas())
                .map(CommonBaseMeta::getName)
                .collect(Collectors.toList());
    }

    private JavaFieldMeta mockJavaFieldMeta(JavaMethodMeta methodMeta) {
        // 从get方法或is方法中生成 field，mock javaField
        JavaFieldMeta fieldMeta = new JavaFieldMeta();
        fieldMeta.setIsVolatile(false)
                .setIsTransient(false)
                .setTypes(methodMeta.getReturnType())
                .setAnnotations(methodMeta.getAnnotations())
                .setAccessPermission(AccessPermission.PRIVATE)
                .setName(JavaMethodNameUtils.replaceGetOrIsPrefix(methodMeta.getName()))
                .setIsStatic(false)
                .setIsFinal(false);
        return fieldMeta;
    }

    private C resolveAllDependencies(C meta) {
        meta.setDependencies(getAllDependencies(meta));
        return meta;
    }

    private Map<String, ? extends CommonCodeGenClassMeta> getAllDependencies(C meta) {
        Map<String, CommonCodeGenClassMeta> result = new LinkedHashMap<>(collectDependencies(Stream.of(meta.getSuperClass())));
        result.putAll(getFieldMetaDependencies(meta));
        result.putAll(getMethodMetaDependencies(meta));
        result.putAll(getMethodParameterDependencies(meta));
        result.putAll(getTypeVariablesDependencies(meta));
        result.putAll(getSupperTypeVariablesDependencies(meta));
        result.putAll(flatMapDependencies(result));
        return result;
    }

    private Map<String, ? extends CommonCodeGenClassMeta> getFieldMetaDependencies(C meta) {
        return resolveDependencies(Arrays.stream(meta.getFieldMetas())
                .map(CommonCodeGenFiledMeta::getFiledTypes));
    }

    private Map<String, ? extends CommonCodeGenClassMeta> getMethodMetaDependencies(C meta) {
        return resolveDependencies(Arrays.stream(meta.getMethodMetas())
                .map(CommonCodeGenMethodMeta::getReturnTypes));
    }

    private Map<String, ? extends CommonCodeGenClassMeta> getMethodParameterDependencies(C meta) {
        return collectDependencies(Arrays.stream(meta.getMethodMetas())
                .map(CommonCodeGenMethodMeta::getParams)
                .filter(Objects::nonNull)
                .map(Map::values)
                .flatMap(Collection::stream));
    }

    private Map<String, ? extends CommonCodeGenClassMeta> getTypeVariablesDependencies(C meta) {
        return resolveDependencies(Arrays.stream(meta.getMethodMetas())
                .map(CommonCodeGenMethodMeta::getTypeVariables));
    }

    private Map<String, ? extends CommonCodeGenClassMeta> getSupperTypeVariablesDependencies(C meta) {
        Map<String, ? extends CommonCodeGenClassMeta[]> superTypeVariables = meta.getSuperTypeVariables();
        return resolveDependencies(superTypeVariables.values().stream());
    }

    private Map<String, ? extends CommonCodeGenClassMeta> resolveDependencies(Stream<? extends CommonCodeGenClassMeta[]> classMetaStream) {
        return collectDependencies(classMetaStream
                .filter(Objects::nonNull)
                .map(Arrays::asList)
                .flatMap(Collection::stream));
    }

    private Map<String, ? extends CommonCodeGenClassMeta> collectDependencies(Stream<? extends CommonCodeGenClassMeta> classMetaStream) {
        Set<String> names = new HashSet<>();
        return classMetaStream
                .filter(Objects::nonNull)
                .filter(meta -> {
                    if (names.contains(meta.getName())) {
                        return false;
                    }
                    names.add(meta.getName());
                    return true;
                })
                .collect(Collectors.toMap(CommonCodeGenClassMeta::getName, value -> value));
    }

    private Map<String, ? extends CommonCodeGenClassMeta> flatMapDependencies(Map<String, ? extends CommonCodeGenClassMeta> dependencies) {
        return collectDependencies(dependencies.values()
                .stream()
                .map(CommonCodeGenClassMeta::getDependencies)
                .filter(Objects::nonNull)
                .map(Map::values)
                .flatMap(Collection::stream));

    }
}
