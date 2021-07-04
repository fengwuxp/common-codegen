package com.wuxp.codegen.languages;

import com.wuxp.codegen.annotations.LanguageAnnotationParser;
import com.wuxp.codegen.comment.LanguageCommentDefinitionDescriber;
import com.wuxp.codegen.core.parser.JavaClassParser;
import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
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
import com.wuxp.codegen.model.util.JavaTypeUtils;
import com.wuxp.codegen.reactive.ReactorTypeSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.TypeVariable;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.wuxp.codegen.core.parser.JavaClassParser.JAVA_CLASS_PARSER;

/**
 * @author wuxp
 */
@Slf4j
public abstract class AbstractLanguageTypeDefinitionParser<C extends CommonCodeGenClassMeta> implements LanguageTypeDefinitionParser<C> {

    /**
     * java类的解析器 默认解析所有的属性 方法
     */
    private final JavaClassParser javaParser;

    private final CacheLanguageTypeDefinitionParser<C> cacheLanguageTypeDefinitionParser;

    /**
     * 包名映射策略
     */
    private final PackageMapStrategy packageMapStrategy;

    protected AbstractLanguageTypeDefinitionParser(PackageMapStrategy packageMapStrategy) {
        this.packageMapStrategy = packageMapStrategy;
        this.javaParser = JAVA_CLASS_PARSER;
        this.cacheLanguageTypeDefinitionParser = new CacheLanguageTypeDefinitionParser<>(this);
    }

    @Override
    public C parse(Class<?> source) {
        return cacheLanguageTypeDefinitionParser.parseOfNullable(source).orElseGet(() -> this.parseInner(source));
    }


    private C parseInner(Class<?> source) {
        JavaClassMeta classMeta = javaParser.parse(source);
        preProcess(classMeta);
        C result = newCodeGenClassMetaAndPutCache(source);
        result.setName(this.packageMapStrategy.convertClassName(source));
        result.setPackagePath(this.packageMapStrategy.convert(source));
        result.setClassType(classMeta.getClassType());
        result.setAccessPermission(classMeta.getAccessPermission());
        result.setTypeVariables(getTypeVariables(classMeta));
        result.setGenericDescription(getGenericDescription(result.getTypeVariables()));
        result.setComments(extractComments(classMeta));
        result.setAnnotations(LanguageAnnotationParser.getInstance().parse(source));
        result.setSuperClass(getSupperClassMeta(classMeta));
        result.setSuperTypeVariables(getSuperTypeGenericTypeVariables(classMeta));
        result.setMethodMetas(getCodegenMethodMetas(classMeta));
        result.setFieldMetas(getCodegenFiledMetas(classMeta));
        result.setDependencies(getClassDependencies(classMeta));
        return postProcess(result);
    }

    private void preProcess(JavaClassMeta classMeta) {
        // 增加对响应式编程的支持
        ReactorTypeSupport.handle(classMeta);
        // 加入对 spring 的特别处理
        SpringControllerFilterUtils.filterMethods(classMeta);
    }

    private C newCodeGenClassMetaAndPutCache(Class<?> source) {
        C result = newInstance();
        result.setSource(source);
        cacheLanguageTypeDefinitionParser.put(result);
        return result;
    }

    private CommonCodeGenClassMeta[] getTypeVariables(JavaClassMeta classMeta) {
        return Arrays.stream(classMeta.getTypeVariables())
                .map(type -> {
                    if (type instanceof Class<?>) {
                        return dispatch((Class<?>) type);
                    } else if (type instanceof TypeVariable) {
                        return parseTypeVariable(type);
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
        if (superClass == null || Object.class.equals(superClass) || classMeta.getClazz().isEnum()) {
            return null;
        }
        // 不是object
        C superClassMeta = this.dispatch(superClass);
        if (superClassMeta == null) {
            log.warn("超类 {} 解析处理失败或被忽略", classMeta.getClassName());
            return null;
        }

        CommonCodeGenClassMeta[] supperClassTypeVariables = getSuperTypeGenericTypeVariables(classMeta).get(superClass.getName());
        if (ObjectUtils.isEmpty(supperClassTypeVariables)) {
            return superClassMeta;
        }

        C currentSupperClassMeta = this.newInstance();
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
        classMeta.getSuperTypeVariables().forEach((superClazz, val) -> {
            if (ObjectUtils.isEmpty(val)) {
                return;
            }
            //处理超类
            C typescriptClassMeta = this.dispatch(superClazz);
            if (typescriptClassMeta == null) {
                return;
            }

            //处理超类上的类型变量 例如 A<T,E> extends B<C<T>,E> 这种情况
            CommonCodeGenClassMeta[] typeVariables = Arrays.stream(val)
                    .map(this::dispatchOfNullable)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(CommonCodeGenClassMeta.class::cast)
                    .toArray(CommonCodeGenClassMeta[]::new);
            superTypeVariables.put(typescriptClassMeta.getName(), typeVariables);
        });
        return superTypeVariables;
    }

    private Map<String, C> getClassDependencies(JavaClassMeta classMeta) {
        Map<String, C> metaDependencies = resolveDependencies(classMeta);
        this.recursionResolveDependencies(getMethodDependencies(classMeta)).forEach(metaDependencies::put);
        metaDependencies.putAll(getSupperTypeVariableDependencies(classMeta));
        // 如果依赖中包含自身 则排除，用于打断循环依赖
        metaDependencies.remove(classMeta.getClazz().getSimpleName());
        return metaDependencies;
    }

    @SuppressWarnings("unchecked")
    private Map<String, C> resolveDependencies(JavaClassMeta classMeta) {
        return classMeta.getDependencyList()
                .stream()
                .map(this::dispatch)
                .filter(Objects::nonNull)
                .map(result -> (C) result)
                .collect(Collectors.toMap(CommonBaseMeta::getName, value -> value));
    }

    private Set<Class<?>> getMethodDependencies(JavaClassMeta classMeta) {
        return JavaClassParser.fetchClassMethodDependencies(classMeta.getClazz(), classMeta.getMethodMetas()).stream().
                filter(Objects::nonNull)
                // 忽略所有接口的依赖
                .filter(clazz -> !clazz.isInterface())
                // 忽略超类的依赖
                .filter(clazz -> !clazz.equals(classMeta.getSuperClass()))
                .collect(Collectors.toSet());
    }

    @SuppressWarnings("unchecked")
    protected Map<String, C> recursionResolveDependencies(Set<Class<?>> dependencies) {
        return dependencies.stream()
                .map(this::dispatch)
                .filter(Objects::nonNull)
                .map(result -> (C) result)
                .map(meta -> {
                    List<C> dependencyMetas = new ArrayList<>((Collection<C>) meta.getDependencies().values());
                    dependencyMetas.add(meta);
                    return dependencyMetas;
                })
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .filter(CommonCodeGenClassMeta::getNeedImport)
                .distinct()
                .collect(Collectors.toMap(C::getName, value -> value));

    }

    @SuppressWarnings("unchecked")
    private Map<String, C> getSupperTypeVariableDependencies(JavaClassMeta classMeta) {
        return getSuperTypeGenericTypeVariables(classMeta).values()
                .stream()
                .map(Arrays::asList)
                .flatMap(Collection::stream)
                .filter(typeVariable -> JavaTypeUtils.isNoneJdkComplex(typeVariable.getSource()))
                .map(typeVariable -> (C) typeVariable)
                .collect(Collectors.toMap(C::getName, value -> value));
    }


    private CommonCodeGenMethodMeta[] getCodegenMethodMetas(JavaClassMeta classMeta) {
        return Arrays.stream(classMeta.getMethodMetas())
                .filter(javaMethodMeta -> Boolean.FALSE.equals(javaMethodMeta.getIsStatic()))
                .filter(javaMethodMeta -> Boolean.FALSE.equals(javaMethodMeta.getIsTransient()))
                .filter(javaMethodMeta -> AccessPermission.PUBLIC.equals(javaMethodMeta.getAccessPermission()))
                .map(this::dispatch)
                .filter(Objects::nonNull)
                .map(CommonCodeGenMethodMeta.class::cast)
                .distinct()
                .toArray(CommonCodeGenMethodMeta[]::new);
    }

    private CommonCodeGenFiledMeta[] getCodegenFiledMetas(JavaClassMeta classMeta) {
        return margeFiledMetas(classMeta)
                .stream()
                .map(this::dispatch)
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


}
