package com.wuxp.codegen.languages;

import com.wuxp.codegen.annotation.processors.AnnotationMate;
import com.wuxp.codegen.annotation.processors.AnnotationProcessor;
import com.wuxp.codegen.annotation.processors.javax.NotNullProcessor;
import com.wuxp.codegen.annotation.processors.javax.PatternProcessor;
import com.wuxp.codegen.annotation.processors.javax.SizeProcessor;
import com.wuxp.codegen.annotation.processors.spring.*;
import com.wuxp.codegen.comment.SourceCodeCommentEnhancer;
import com.wuxp.codegen.core.*;
import com.wuxp.codegen.core.config.CodegenConfigHolder;
import com.wuxp.codegen.core.exception.CodegenRuntimeException;
import com.wuxp.codegen.core.macth.PackageNameCodeGenMatcher;
import com.wuxp.codegen.core.parser.GenericParser;
import com.wuxp.codegen.core.parser.JavaClassParser;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.parser.enhance.LanguageEnhancedProcessor;
import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.core.strategy.CombineTypeDescStrategy;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.core.util.ToggleCaseUtils;
import com.wuxp.codegen.enums.EnumCommentEnhancer;
import com.wuxp.codegen.mapping.AbstractLanguageTypeMapping;
import com.wuxp.codegen.model.*;
import com.wuxp.codegen.model.constant.MappingAnnotationPropNameConstant;
import com.wuxp.codegen.model.constant.TypescriptFeignMediaTypeConstant;
import com.wuxp.codegen.model.enums.AccessPermission;
import com.wuxp.codegen.model.enums.ClassType;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.languages.java.JavaParameterMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptFieldMate;
import com.wuxp.codegen.model.mapping.JavaArrayClassTypeMark;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import com.wuxp.codegen.types.SimpleCombineTypeDescStrategy;
import com.wuxp.codegen.util.JavaMethodNameUtils;
import com.wuxp.codegen.util.SpringControllerFilterUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * 抽象的语言解释器
 *
 * @param <C> 类
 * @param <M> 方法
 * @param <F> 属性
 * @author wxup
 */
@Slf4j
public abstract class AbstractLanguageParser<C extends CommonCodeGenClassMeta,
        M extends CommonCodeGenMethodMeta,
        F extends CommonCodeGenFiledMeta> implements LanguageParser<C>, ApiServiceClassMatcher {

    /**
     * 默认合并后的参数名称
     */
    public static final String DEFAULT_MARGE_PARAMS_NAME = "req";

    /**
     * annotationProcessorMap
     */
    public static final Map<Class<? extends Annotation>, AnnotationProcessor> ANNOTATION_PROCESSOR_MAP = new LinkedHashMap<>();

    static {
        ANNOTATION_PROCESSOR_MAP.put(NotNull.class, new NotNullProcessor());
        ANNOTATION_PROCESSOR_MAP.put(Size.class, new SizeProcessor());
        ANNOTATION_PROCESSOR_MAP.put(Pattern.class, new PatternProcessor());

        RequestMappingProcessor mappingProcessor = new RequestMappingProcessor();
        ANNOTATION_PROCESSOR_MAP.put(RequestMapping.class, mappingProcessor);
        ANNOTATION_PROCESSOR_MAP.put(GetMapping.class, mappingProcessor);
        ANNOTATION_PROCESSOR_MAP.put(PostMapping.class, mappingProcessor);
        ANNOTATION_PROCESSOR_MAP.put(DeleteMapping.class, mappingProcessor);
        ANNOTATION_PROCESSOR_MAP.put(PutMapping.class, mappingProcessor);
        ANNOTATION_PROCESSOR_MAP.put(PatchMapping.class, mappingProcessor);

        ANNOTATION_PROCESSOR_MAP.put(CookieValue.class, new CookieValueProcessor());
        ANNOTATION_PROCESSOR_MAP.put(RequestBody.class, new RequestBodyProcessor());
        ANNOTATION_PROCESSOR_MAP.put(RequestHeader.class, new RequestHeaderProcessor());
        ANNOTATION_PROCESSOR_MAP.put(RequestParam.class, new RequestParamProcessor());
        ANNOTATION_PROCESSOR_MAP.put(RequestPart.class, new RequestPartProcessor());
        ANNOTATION_PROCESSOR_MAP.put(PathVariable.class, new PathVariableProcessor());
    }

    /**
     * 类被处理的次数
     */
    protected final Map<Class<?>, Integer> handleCountMap = new HashMap<>(128);
    /**
     * 处理结果缓存
     */
    protected final Map<Class<?>, Object> handleResultCacheMap = new HashMap<>(128);
    /**
     * java类的解析器 默认解析所有的属性 方法
     */
    protected GenericParser<JavaClassMeta, Class<?>> javaParser = new JavaClassParser(false);
    /**
     * 语言元数据对象的工厂
     */
    protected LanguageMetaInstanceFactory<C, M, F> languageMetaInstanceFactory;
    /**
     * 映射java类型和其他语言类型之间的关系
     */
    protected AbstractLanguageTypeMapping<C> languageTypeMapping;
    /**
     * 包名映射策略
     */
    protected PackageMapStrategy packageMapStrategy;
    /**
     * 代码检查者
     */
    protected Collection<CodeDetect> codeDetects;
    /**
     * 生成匹配策略
     */
    protected CodeGenMatchingStrategy genMatchingStrategy;
    /**
     * 匹配需要生成的类匹配器链
     */
    protected List<CodeGenMatcher> codeGenMatchers = new ArrayList<>();
    protected List<CodeGenImportMatcher> codeGenImportMatchers = new ArrayList<>();
    protected LanguageEnhancedProcessor<C, M, F> languageEnhancedProcessor = LanguageEnhancedProcessor.NONE;
    protected SourceCodeCommentEnhancer sourceCodeCommentEnhancer = new SourceCodeCommentEnhancer();
    protected EnumCommentEnhancer enumCommentEnhancer = new EnumCommentEnhancer(sourceCodeCommentEnhancer);
    protected CombineTypeDescStrategy combineTypeDescStrategy = new SimpleCombineTypeDescStrategy();

    {

        // 根据是否为spring的组件进行匹配
        codeGenMatchers.add(clazz -> {
            Annotation service = clazz.getAnnotation(Service.class);
            Annotation clazzAnnotation = clazz.getAnnotation(Component.class);
            // 不是spring的组件
            return service == null && clazzAnnotation == null;
        });
    }

    public AbstractLanguageParser(LanguageMetaInstanceFactory<C, M, F> languageMetaInstanceFactory,
                                  PackageMapStrategy packageMapStrategy,
                                  CodeGenMatchingStrategy genMatchingStrategy,
                                  Collection<CodeDetect> codeDetects) {
        this(languageMetaInstanceFactory, packageMapStrategy, genMatchingStrategy, codeDetects, Collections.emptyList());
    }


    public AbstractLanguageParser(LanguageMetaInstanceFactory<C, M, F> languageMetaInstanceFactory,
                                  PackageMapStrategy packageMapStrategy,
                                  CodeGenMatchingStrategy genMatchingStrategy,
                                  Collection<CodeDetect> codeDetects,
                                  Collection<CodeGenMatcher> includeCodeGenMatchers) {
        this.languageMetaInstanceFactory = languageMetaInstanceFactory;
        this.packageMapStrategy = packageMapStrategy;
        this.genMatchingStrategy = genMatchingStrategy;
        if (codeDetects != null) {
            this.codeDetects = codeDetects;
        }
        if (includeCodeGenMatchers != null) {
            this.addCodeGenMatchers(includeCodeGenMatchers.toArray(new CodeGenMatcher[0]));
        }
    }


    public AbstractLanguageParser(GenericParser<JavaClassMeta, Class<?>> javaParser,
                                  LanguageMetaInstanceFactory<C, M, F> languageMetaInstanceFactory,
                                  PackageMapStrategy packageMapStrategy,
                                  CodeGenMatchingStrategy genMatchingStrategy,
                                  Collection<CodeDetect> codeDetects) {
        this(languageMetaInstanceFactory, packageMapStrategy, genMatchingStrategy, codeDetects, Collections.emptyList());
        if (javaParser != null) {
            this.javaParser = javaParser;
        }

    }

    @Override
    public C parse(Class<?> source) {
        //符合匹配规则，或非集合类型和Map的的子类进行
        if (source == null) {
            return null;
        }

        if (!this.isMatchGenCodeRule(source) ||
                JavaTypeUtils.isMap(source) ||
                JavaTypeUtils.isCollection(source)) {
            return null;
        }

        if (!JavaTypeUtils.isNoneJdkComplex(source)) {
            List<C> results = this.languageTypeMapping.mapping(source);
            if (!results.isEmpty()) {
                return results.get(0);
            }
        }
        if (source.isEnum()) {
            //枚举出来
            List<C> results = this.languageTypeMapping.mapping(Enum.class);
            if (!results.isEmpty()) {
                return results.get(0);
            }
        }

        if (handleCountMap.containsKey(source)) {
            //标记某个类被处理的次数如果超过2次，从缓存中返回
            if (handleCountMap.get(source) > 1) {
                return this.getResultToLocalCache(source);
            } else {
                handleCountMap.put(source, handleCountMap.get(source) + 1);
            }
        } else {
            handleCountMap.put(source, 1);
        }
        Integer count = handleCountMap.get(source);

        CommonCodeGenClassMeta mapping = languageTypeMapping.getCustomizeTypeMapping().mapping(source);

        if (mapping != null) {
            handleResultCacheMap.put(source, mapping);
            return (C) mapping;
        } else {
            mapping = languageTypeMapping.getBaseTypeMapping().mapping(source);
            if (mapping != null) {
                handleResultCacheMap.put(source, mapping);
                return (C) mapping;
            }
        }

        JavaClassMeta javaClassMeta = this.javaParser.parse(source);
        // 加入对spring的特别处理
        SpringControllerFilterUtils.filterMethods(javaClassMeta);

        boolean isApiServiceClass = this.matches(javaClassMeta);
        if (isApiServiceClass) {
            // 要生成的服务，判断是否需要生成
            if (!this.genMatchingStrategy.isMatchClazz(javaClassMeta)) {
                //跳过
                log.warn("跳过类{}", source.getName());
                return null;
            }
        }

        C meta = this.getResultToLocalCache(source);
        if (meta != null/* && (meta.getFieldMetas() != null || meta.getMethodMetas() != null)*/) {
            // TODO 由于循环依赖导致，对象还没有初始化完成
            return meta;
        }
        //检查代码
        this.detectJavaCode(javaClassMeta);
        meta = this.languageMetaInstanceFactory.newClassInstance();
        // 防止由于递归调用导致的初始化未完成，照成重新初始化
        handleResultCacheMap.put(source, meta);
        meta.setSource(source);
        meta.setName(this.packageMapStrategy.convertClassName(source));
        meta.setPackagePath(this.packageMapStrategy.convert(source));
        meta.setClassType(javaClassMeta.getClassType());
        meta.setAccessPermission(javaClassMeta.getAccessPermission());
        // 处理泛型变量
        CommonCodeGenClassMeta[] classTypeVariables = Arrays.stream(javaClassMeta.getTypeVariables())
                .map(type -> {
                    if (type instanceof Class) {
                        return this.parse((Class) type);
                    } else if (type instanceof TypeVariable) {
                        CommonCodeGenClassMeta typeVariable = getLanguageMetaInstanceFactory().getTypeVariableInstance();
                        String typeName = type.getTypeName();
                        typeVariable.setName(typeName);
                        typeVariable.setGenericDescription(typeName);
                        return typeVariable;
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toArray(CommonCodeGenClassMeta[]::new);
        meta.setTypeVariables(classTypeVariables);

        // 生成类上的泛型描述
        String genericDescription = Arrays.stream(classTypeVariables)
                .map(CommonBaseMeta::getName)
                .collect(Collectors.joining(","));
        if (StringUtils.hasText(genericDescription)) {
            meta.setGenericDescription(MessageFormat.format("<{0}>", genericDescription));
        }

        // 处理超类
        Class<?> javaClassSuperClass = javaClassMeta.getSuperClass();
        if (!Object.class.equals(javaClassSuperClass)) {
            //不是object
            C commonCodeGenClassMeta = this.parse(javaClassSuperClass);
            if (javaClassSuperClass != null && commonCodeGenClassMeta == null) {
                if (log.isDebugEnabled()) {
                    log.debug("超类 {} 解析处理失败或被忽略", javaClassMeta.getClassName());
                }
            }
            meta.setSuperClass(commonCodeGenClassMeta);
        }

        //类上的注释
        meta.setComments(this.generateComments(source.getAnnotations(), source).toArray(new String[]{}));
        //类上的注解
        meta.setAnnotations(this.converterAnnotations(source.getAnnotations(), source));
        boolean isFirst = count == 1;
        if (isFirst && isApiServiceClass) {
            //spring的控制器  生成方法列表
            meta.setMethodMetas(this.converterMethodMetas(javaClassMeta.getMethodMetas(), javaClassMeta, meta)
                    .toArray(new CommonCodeGenMethodMeta[]{}));
        }

        boolean needGenFields = !isApiServiceClass && (isFirst || meta.getFieldMetas() == null);
        if (needGenFields) {
            // 普通的java bean DTO  生成属性列表
            meta.setFieldMetas(this.converterFieldMetas(javaClassMeta.getFieldMetas(), javaClassMeta)
                    .stream()
                    .filter(Objects::nonNull)
                    .toArray(CommonCodeGenFiledMeta[]::new));
        }

        //依赖处理
        final Map<String, C> metaDependencies =
                meta.getDependencies() == null ? new LinkedHashMap<>() : (Map<String, C>) meta.getDependencies();
        if (isFirst) {
            //依赖列表
            Set<Class<?>> dependencyList = javaClassMeta.getDependencyList();
            if (isApiServiceClass) {
                dependencyList = dependencyList.stream().
                        filter(Objects::nonNull)
                        //忽略所有接口的依赖
                        .filter(clazz -> !clazz.isInterface())
                        //忽略超类的依赖
                        .filter(clazz -> !clazz.equals(javaClassSuperClass))
                        .collect(Collectors.toSet());
            }
            Map<String, C> dependencies = this.fetchDependencies(dependencyList);
            dependencies.forEach(metaDependencies::put);
        }

        Map<String/*类型，父类，接口，本身*/, CommonCodeGenClassMeta[]> superTypeVariables = new LinkedHashMap<>();

        // 处理超类上面的类型变量
        javaClassMeta.getSuperTypeVariables().forEach((superClazz, val) -> {
            if (val == null || val.length == 0) {
                return;
            }
            //处理超类
            C typescriptClassMeta = this.parse(superClazz);
            if (typescriptClassMeta == null) {
                return;
            }

            //处理超类上的类型变量 例如 A<T,E> extends B<C<T>,E> 这种情况
            CommonCodeGenClassMeta[] typeVariables = Arrays.stream(val)
                    .map(clazz -> {
                        C typeVariable = this.parse(clazz);
                        if (typeVariable == null) {
                            List<C> cs = this.languageTypeMapping.mapping(clazz);
                            if (cs.isEmpty()) {
                                return null;
                            } else {
                                typeVariable = cs.get(0);
                            }
                        }
                        if (JavaTypeUtils.isNoneJdkComplex(clazz)) {
                            metaDependencies.put(typeVariable.getName(), typeVariable);
                        }
                        return typeVariable;
                    })
                    .filter(Objects::nonNull)
                    .toArray(CommonCodeGenClassMeta[]::new);
            superTypeVariables.put(typescriptClassMeta.getName(), typeVariables);
        });

        //如果依赖中包含自身 则排除，用于打断循环依赖
        metaDependencies.remove(source.getSimpleName());
        meta.setDependencies(metaDependencies);
        meta.setSuperTypeVariables(superTypeVariables);

        //当超类不为空，且超类的类型变量不为空的时候，重新设置一下超类的类型变量
        if (meta.getSuperClass() != null && superTypeVariables.size() > 0) {
            CommonCodeGenClassMeta[] supperClassTypeVariables = superTypeVariables.get(meta.getSuperClass().getName());
            CommonCodeGenClassMeta superClass = meta.getSuperClass();

            //做一次值复制，防止改变缓存中的值
            CommonCodeGenClassMeta newSupperClass = new CommonCodeGenClassMeta();
            BeanUtils.copyProperties(superClass, newSupperClass);
            newSupperClass.setTypeVariables(supperClassTypeVariables);
            meta.setSuperClass(newSupperClass);
        }
        this.languageEnhancedProcessor.enhancedProcessingClass(meta, javaClassMeta);
        // 增强处理类
        this.enhancedProcessingClass(meta, javaClassMeta);
        if (this.tryMatchOnlyImportClasses(source)) {
            // 只需要导入类
            meta.setNeedGenerate(false);
            meta.setNeedImport(true);
            if (CodegenConfigHolder.getConfig().isJava()) {
                meta.setPackagePath(source.getName());
            }
        }
        return meta;
    }


    @Override
    public LanguageMetaInstanceFactory getLanguageMetaInstanceFactory() {
        return this.languageMetaInstanceFactory;
    }

    @Override
    public void addCodeGenMatchers(CodeGenMatcher... codeGenMatchers) {
        List<CodeGenMatcher> genMatchers = Arrays.asList(codeGenMatchers);
        this.codeGenMatchers.addAll(genMatchers.stream()
                .filter(item -> !CodeGenImportMatcher.class.isAssignableFrom(item.getClass()))
                .collect(Collectors.toList()));
        this.codeGenImportMatchers.addAll(genMatchers.stream()
                .filter(item -> item instanceof CodeGenImportMatcher)
                .map(codeGenMatcher -> (CodeGenImportMatcher) codeGenMatcher)
                .collect(Collectors.toList()));
    }


    /**
     * 是否匹配生成的规则
     *
     * @param clazz java 类
     * @return <code>true</code> 匹配生成规则
     */
    protected boolean isMatchGenCodeRule(Class<?> clazz) {
        if (clazz == null) {
            return false;
        }

        //必须满足所有的匹配器才能进行生成
        boolean result = codeGenMatchers.stream()
                .allMatch(codeGenMatcher -> codeGenMatcher.match(clazz));
        if (result && log.isDebugEnabled()) {
            log.debug("符合生成条件的类：{}", clazz.getName());
        }
        return result;
    }

    /**
     * 检查代码是否服务自定义的规则
     *
     * @param source java 类元数据
     */
    protected void detectJavaCode(JavaClassMeta source) {

        //尝试检查代码
        if (this.codeDetects == null) {
            return;
        }

        this.codeDetects.forEach(codeDetect -> {
            codeDetect.detect(source);
        });
    }

    /**
     * 从缓存中获取解过
     *
     * @param clazz 类类型
     * @return 返回来自缓存的解析对象
     */
    protected C getResultToLocalCache(Class<?> clazz) {

        C c = (C) handleResultCacheMap.get(clazz);
        if (c == null) {
            return null;
        }
//        // 做深copy
//        C target = this.languageMetaInstanceFactory.newClassInstance();
//        BeanUtils.copyProperties(c, target);
//        target.setDependencies(new LinkedHashMap<>(c.getDependencies()));
//        return target;
        return c;
    }

    /**
     * 通过注解获取注释
     *
     * @param annotations 组件列表
     * @param owner       注解所有者
     * @return 注释列表
     */
    protected List<String> generateComments(Annotation[] annotations, AnnotatedElement owner) {
        if (annotations == null || annotations.length == 0) {
            return new ArrayList<>();
        }

        List<CodeGenCommentEnhancer> codeGenCommentEnhancers = Arrays.stream(annotations)
                .map(annotation -> {
                    //将javax的验证注解转换为注释
                    AnnotationProcessor processor = ANNOTATION_PROCESSOR_MAP.get(annotation.annotationType());
                    if (processor == null) {
                        return null;
                    }
                    return processor.process(annotation);
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        codeGenCommentEnhancers.add(sourceCodeCommentEnhancer);
        codeGenCommentEnhancers.add(enumCommentEnhancer);
        return codeGenCommentEnhancers.stream()
                .map(codeGenCommentEnhancer -> codeGenCommentEnhancer.toComments(owner))
                .flatMap(Collection::stream)
                .filter(StringUtils::hasText)
                .distinct()
                .collect(Collectors.toList());
    }

    /**
     * 通过类类型来获取注释
     *
     * @param classes  类列表
     * @param isMethod 是否为方法
     * @return 注释列表
     */
    protected List<String> generateComments(Class<?>[] classes, boolean isMethod) {
        if (classes == null || classes.length == 0) {
            return new ArrayList<>();
        }
        return Arrays.stream(classes)
                .filter(Objects::nonNull)
                .map(clazz -> {
                    String simpleName = clazz.equals(JavaArrayClassTypeMark.class) ? "数组" : clazz.getSimpleName();
                    return MessageFormat.format("{0}在java中的类型为：{1}", isMethod ? "返回值" : "", simpleName);
                }).collect(Collectors.toList());
    }


    /**
     * 将class列表装换为名称字符串
     *
     * @param classes 类列表
     * @return 使用','连接后的字符串
     */
    protected String classToNamedString(Class<?>[] classes) {
        if (classes == null) {
            return "";
        }
        return Arrays.stream(classes)
                .filter(Objects::nonNull)
                .map(Class::getName).collect(Collectors.joining(","));
    }

    /**
     * 转换注解列表
     *
     * @param annotations     注解列表
     * @param annotationOwner 注解持有者
     * @return 用于生成的注解列表
     */
    protected CommonCodeGenAnnotation[] converterAnnotations(Annotation[] annotations, Object annotationOwner) {
        if (annotations == null || annotations.length == 0) {
            return new CommonCodeGenAnnotation[]{};
        }

        final List<CommonCodeGenAnnotation> emptyList = Collections.emptyList();

        return Arrays.stream(annotations)
                .map(annotation -> {
                    AnnotationProcessor<AnnotationMate, Annotation> processor = ANNOTATION_PROCESSOR_MAP.get(annotation.annotationType());
                    if (processor == null) {
                        return emptyList;
                    }
                    AnnotationMate annotationMate = processor.process(annotation);
                    CommonCodeGenAnnotation toAnnotation = annotationMate.toAnnotation(annotationOwner);
                    if (toAnnotation == null) {
                        return emptyList;
                    }
                    List<CommonCodeGenAnnotation> associatedAnnotations =
                            toAnnotation.getAssociatedAnnotations() == null ? Collections.emptyList() : toAnnotation.getAssociatedAnnotations();
                    List<CommonCodeGenAnnotation> toAnnotations = new ArrayList<>(associatedAnnotations.size() + 1);
                    toAnnotations.add(toAnnotation);
                    toAnnotations.addAll(associatedAnnotations);
                    this.languageEnhancedProcessor.enhancedProcessingAnnotation(toAnnotation, annotation, annotationOwner);
                    // 只对主的CommonCodeGenAnnotation进行增强处理
                    this.enhancedProcessingAnnotation(toAnnotation, annotationMate, annotationOwner);
                    return toAnnotations;
                })
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .distinct()
                .toArray(CommonCodeGenAnnotation[]::new);
    }


    /**
     * 转换属性列表
     *
     * @param javaFieldMetas 方法元数据列表
     * @param classMeta      类元数据
     * @return 用于生成的方法列表
     */
    protected List<F> converterFieldMetas(JavaFieldMeta[] javaFieldMetas, JavaClassMeta classMeta) {
        if (javaFieldMetas == null) {
            return Collections.EMPTY_LIST;
        }

        boolean isEnum = classMeta.getClazz().isEnum();
        List<JavaFieldMeta> fieldMetas = Arrays.stream(javaFieldMetas)
                //过滤掉非枚举的静态变量
                .filter(javaFieldMeta -> isEnum || Boolean.FALSE.equals(javaFieldMeta.getIsStatic()))
                .filter(javaFieldMeta -> Boolean.FALSE.equals(javaFieldMeta.getIsTransient()))
                .collect(Collectors.toList());

        final List<String> fieldNameList = fieldMetas.stream()
                .map(CommonBaseMeta::getName)
                .collect(Collectors.toList());

        if (!isEnum) {
            //如果是java bean 需要合并get方法
            // 找出java bean中不存在属性定义的get或 is方法
            fieldMetas.addAll(Arrays.stream(classMeta.getMethodMetas())
                    //过滤掉本地方法
                    .filter(javaMethodMeta -> !Boolean.TRUE.equals(javaMethodMeta.getIsNative()))
                    .filter(javaMethodMeta -> {
                        //匹配getXX 或isXxx方法
                        return JavaMethodNameUtils.isGetMethodOrIsMethod(javaMethodMeta.getName());
                    })
                    .filter(
                            javaMethodMeta -> Boolean.FALSE.equals(javaMethodMeta.getIsStatic()) && Boolean.FALSE.equals(javaMethodMeta.getIsAbstract())
                                    && Boolean.FALSE.equals(javaMethodMeta.getIsTransient()))
                    .filter(javaMethodMeta -> javaMethodMeta.getReturnType() != null && javaMethodMeta.getReturnType().length > 0)
                    .filter(javaMethodMeta -> {
                        //属性是否已经存在
                        return !fieldNameList.contains(JavaMethodNameUtils.replaceGetOrIsPrefix(javaMethodMeta.getName()));
                    })
                    .map(methodMeta -> {
                        //从get方法或is方法中生成field

                        //mock javaField
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
                    }).collect(Collectors.toList()));
        }

        return fieldMetas.stream()
                .map(javaFieldMeta -> this.converterField(javaFieldMeta, classMeta))
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

    }


    protected F converterField(JavaFieldMeta javaFieldMeta, JavaClassMeta classMeta) {

        if (javaFieldMeta == null) {
            return null;
        }

        if (!this.genMatchingStrategy.isMatchField(javaFieldMeta)) {
            return null;
        }

        Class<?> clazz = classMeta.getClazz();
        boolean isEnum = clazz.isEnum();
        if (javaFieldMeta.getIsStatic() && !isEnum) {
            //不处理静态类型的字段
            return null;
        }
        F fieldInstance = this.languageMetaInstanceFactory.newFieldInstance();

        fieldInstance.setName(javaFieldMeta.getName());
        fieldInstance.setAccessPermission(javaFieldMeta.getAccessPermission());
        fieldInstance.setEnumConstant(javaFieldMeta.getIsEnumConstant());
        //注释来源于注解和java的类类型
        List<String> comments;
        if (javaFieldMeta instanceof JavaParameterMeta) {
            comments = this.generateComments(javaFieldMeta.getAnnotations(), ((JavaParameterMeta) javaFieldMeta).getParameter());
        } else {
            comments = this.generateComments(javaFieldMeta.getAnnotations(), javaFieldMeta.getField());
        }
        Class<?>[] types = javaFieldMeta.getTypes();
        if (isEnum) {
            if (comments.isEmpty()) {
                comments.addAll(enumCommentEnhancer.toComments(javaFieldMeta.getField()));
            }
            if (comments.isEmpty()) {
                log.warn("枚举{}没有加上描述相关的注解或注释", clazz.getName());
            }
        } else {
            comments.addAll(this.generateComments(types, false));
        }

        //注解
        fieldInstance.setComments(comments.toArray(new String[]{}));

        //注解
        fieldInstance.setAnnotations(this.converterAnnotations(javaFieldMeta.getAnnotations(), javaFieldMeta.getField()));

        //field 类型类别
        Collection<C> classMetaMappings = this.languageTypeMapping.mapping(types);
        //从泛型中解析
        Type[] typeVariables = javaFieldMeta.getTypeVariables();
        if (typeVariables != null && typeVariables.length > 0) {
            List<C> collect = Arrays.stream(typeVariables)
                    .filter(Objects::nonNull)
                    .map(Type::getTypeName).map(name -> {
                        C classInstance = this.languageMetaInstanceFactory.newClassInstance();
                        BeanUtils.copyProperties(this.languageMetaInstanceFactory.getTypeVariableInstance(), classInstance);
                        classInstance.setName(name);
                        classInstance.setGenericDescription(name);
                        return classInstance;
                    }).collect(Collectors.toList());

            if (!collect.isEmpty() && classMetaMappings.size() >= collect.size()) {
                // 存在泛型描述对象，将默认的泛型描述移除
                int size = classMetaMappings.size() - collect.size();
                classMetaMappings = new ArrayList<>(classMetaMappings).subList(0, size);
            }
            classMetaMappings.addAll(collect);
        }
        if (classMetaMappings.isEmpty()) {
            //解析失败
            throw new RuntimeException(String.format("解析类 %s 上的属性 %s 的类型 %s 失败",
                    classMeta.getClassName(),
                    javaFieldMeta.getName(),
                    this.classToNamedString(types)));
        }

        //域对象类型描述
        fieldInstance.setFiledTypes(classMetaMappings.toArray(new CommonCodeGenClassMeta[]{}));
        fieldInstance = this.languageEnhancedProcessor.enhancedProcessingField(fieldInstance, javaFieldMeta, classMeta);
        if (fieldInstance != null) {
            //增强处理
            this.enhancedProcessingField(fieldInstance, javaFieldMeta, classMeta);
        }

        return fieldInstance;
    }


    /**
     * 增强处理 filed
     *
     * @param fieldMeta     待用于字段元数据
     * @param javaFieldMeta java 字段元数据
     * @param classMeta     java类元数据
     */
    protected void enhancedProcessingField(F fieldMeta, JavaFieldMeta javaFieldMeta, JavaClassMeta classMeta) {

    }


    /**
     * 转换方法列表
     *
     * @param javaMethodMetas  java 方法元数据信息列表
     * @param classMeta        java 类元数据信息
     * @param codeGenClassMeta 用于生成的类元数据信息
     * @return 用于生成的方法元数据信息列表
     */
    protected List<M> converterMethodMetas(JavaMethodMeta[] javaMethodMetas, JavaClassMeta classMeta, C codeGenClassMeta) {
        if (javaMethodMetas == null) {
            return Collections.emptyList();
        }
        List<M> codegenMethods = Arrays.stream(javaMethodMetas)
                .filter(javaMethodMeta -> Boolean.FALSE.equals(javaMethodMeta.getIsStatic()))
                .filter(javaMethodMeta -> Boolean.FALSE.equals(javaMethodMeta.getIsTransient()))
                .filter(javaMethodMeta -> this.genMatchingStrategy.isMatchMethod(javaMethodMeta))
                .map(methodMeta -> this.converterMethod(methodMeta, classMeta, codeGenClassMeta))
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        if (!CodegenConfigHolder.getConfig().isServerClass(classMeta.getClazz())){
            return codegenMethods;
        }
        // 判断是否存在方法名称是否相同
        for (M m : codegenMethods) {
            Optional<M> optional = codegenMethods.stream()
                    .filter(m1 -> m1.getName().equals(m.getName()) && !m1.equals(m))
                    .findFirst();
            if (optional.isPresent()){
                // 不允许方法重载
                throw new CodegenRuntimeException(MessageFormat.format("类{0}下的的方法{1}出现重载", classMeta.getClassName(), m.getName()));
            }
        }
        return codegenMethods;


    }

    /**
     * 处理方法转换 1：处理方法上的注解 2：处理方法返回值 3: 处理方法参数
     *
     * @param javaMethodMeta   java 方法元数据信息
     * @param classMeta        java 类元数据信息
     * @param codeGenClassMeta 用于生成的类元数据信息
     * @return 用于生成的方法元数据信息
     */
    protected M converterMethod(JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta, C codeGenClassMeta) {
        if (classMeta == null) {
            return null;
        }
        checkSpringMvcMethod(javaMethodMeta, classMeta);
        M methodCodeMeta;
        if (this.needMargeMethodParams()) {
            // 需要合并参数
            methodCodeMeta = converterMethodAndMargeParams(javaMethodMeta, classMeta, codeGenClassMeta);
        } else {
            methodCodeMeta = converterMethodHandle(javaMethodMeta, classMeta, codeGenClassMeta);
        }
        methodCodeMeta = this.languageEnhancedProcessor.enhancedProcessingMethod(methodCodeMeta, javaMethodMeta, classMeta);
        //增强处理
        this.enhancedProcessingMethod(methodCodeMeta, javaMethodMeta, classMeta);
        return methodCodeMeta;
    }


    /**
     * 转换方法处理
     *
     * @param javaMethodMeta   java 方法元数据信息
     * @param classMeta        类元数据信息
     * @param codeGenClassMeta 生成元数据系信息
     * @return
     */
    protected M converterMethodHandle(JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta, C codeGenClassMeta) {

        M genMethodMeta = this.languageMetaInstanceFactory.newMethodInstance();
        //method转换
        genMethodMeta.setAccessPermission(classMeta.getAccessPermission());
        // 注解转注释
        List<String> comments = this.generateComments(javaMethodMeta.getAnnotations(), javaMethodMeta.getMethod());
        comments.addAll(this.generateComments(javaMethodMeta.getReturnType(), true));
        genMethodMeta.setComments(comments.toArray(new String[]{}));
        genMethodMeta.setName(javaMethodMeta.getName());
        //处理方法上的相关注解
        genMethodMeta.setAnnotations(this.converterAnnotations(javaMethodMeta.getAnnotations(), javaMethodMeta.getMethod()));

        //处理返回值
        Class<?>[] methodMetaReturnType = javaMethodMeta.getReturnType();
        List<C> returnTypes = this.languageTypeMapping.mapping(methodMetaReturnType);
        genMethodMeta.setReturnTypes(returnTypes.toArray(new CommonCodeGenClassMeta[0]));

        Map<String, CommonCodeGenClassMeta> dependencies = (Map<String, CommonCodeGenClassMeta>) codeGenClassMeta.getDependencies();
        returnTypes.forEach(item -> dependencies.put(item.getName(), item));

        /**
         * 方法参数处理流程
         * 1: 参数过滤（过滤掉控制器方法中servlet相关的参数等等）
         * 2：转换参数上的注解
         */
        Map<String, Class<?>[]> methodMetaParams = javaMethodMeta.getParams();
        //有效的参数
        final Map<String, Class<?>[]> effectiveParams = new LinkedHashMap<>();
        methodMetaParams.forEach((key, classes) -> {
            Class<?>[] array = Arrays.stream(classes)
                    .filter(this.getPackageNameCodeGenMatcher()::match)
                    .toArray(Class<?>[]::new);
            if (array.length == 0) {
                return;
            }
            effectiveParams.put(key, array);
        });

        final Map<String/*参数名称*/, CommonCodeGenClassMeta/*参数类型描述*/> codeGenParams = new LinkedHashMap<>();
        final Map<String/*参数名称*/, CommonCodeGenAnnotation[]> codeGenParamAnnotations = new LinkedHashMap<>();

        final Map<String, Annotation[]> paramAnnotations = javaMethodMeta.getParamAnnotations();
        Map<String, Parameter> parameters = javaMethodMeta.getParameters();

        // 遍历参数列表进行转换
        effectiveParams.forEach((key, classes) -> {
            Parameter parameter = parameters.get(key);
            if (!this.genMatchingStrategy.isMatchParameter(javaMethodMeta, parameter)) {
                return;
            }
            C genClassInstance = this.languageMetaInstanceFactory.newClassInstance();
            List<C> paramTypes = this.languageTypeMapping.mapping(classes);
            Class<?> paramType = classes[0];
            genClassInstance.setTypeVariables(paramTypes.toArray(new CommonCodeGenClassMeta[0]));
            // 注解
            Annotation[] annotations = paramAnnotations.get(key);
            CommonCodeGenAnnotation[] commonCodeGenAnnotations = this.converterAnnotations(annotations, parameter);
            genClassInstance.setClassType(ClassType.CLASS)
                    .setSource(paramType)
                    .setAnnotations(commonCodeGenAnnotations)
                    .setIsAbstract(false)
                    .setAccessPermission(AccessPermission.PUBLIC)
                    .setIsFinal(false)
                    .setIsStatic(false)
                    .setName(paramType.getSimpleName());
            C result = this.parse(paramType);
            if (result == null || result.getFieldMetas() == null) {
                genClassInstance.setFieldMetas(new CommonCodeGenFiledMeta[0]);
            } else {
                genClassInstance.setDependencies(result.getDependencies());
                genClassInstance.setFieldMetas(result.getFieldMetas());
            }
            codeGenParams.put(key, genClassInstance);
            codeGenParamAnnotations.put(key, commonCodeGenAnnotations);
        });

        genMethodMeta.setParams(codeGenParams)
                .setParamAnnotations(codeGenParamAnnotations);

        return genMethodMeta;
    }

    /**
     * 方法转换 并且将多个参数合并为一个对象参数
     * <p>
     * 1：所有的参数都是简单对象
     * 2：所有的参数都是复杂对象
     * 3：二者皆有
     * </p>
     *
     * @param javaMethodMeta   java 方法元数据信息
     * @param classMeta        java 类元数据信息
     * @param codeGenClassMeta 用于生成的类元数据信息
     * @return 用于生成的方法元数据信息
     */
    protected M converterMethodAndMargeParams(JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta, C codeGenClassMeta) {
        M genMethodMeta = this.languageMetaInstanceFactory.newMethodInstance();
        //method转换
        genMethodMeta.setAccessPermission(classMeta.getAccessPermission());
        //注解转注释
        List<String> comments = this.generateComments(javaMethodMeta.getAnnotations(), javaMethodMeta.getMethod());
        comments.addAll(this.generateComments(javaMethodMeta.getReturnType(), true));
        genMethodMeta.setComments(comments.toArray(new String[]{}));
        genMethodMeta.setName(javaMethodMeta.getName());

        //处理方法上的相关注解
        genMethodMeta.setAnnotations(this.converterAnnotations(javaMethodMeta.getAnnotations(), javaMethodMeta.getMethod()));

        // 用于缓存合并的参数filedList
        final Set<F> commonCodeGenFiledMetas = new LinkedHashSet<>();
        final Map<String/*参数名称*/, CommonCodeGenAnnotation[]> codeGenParamAnnotations = new LinkedHashMap<>();
        // 参数的元数据类型信息
        final C argsClassMeta = this.languageMetaInstanceFactory.newClassInstance();

        /**
         * 有效的参数
         * @key 参数名称
         * @value 类型列表
         */
        final Map<String, Class<?>[]> effectiveParams = new LinkedHashMap<>();
        Map<String, Parameter> parameters = javaMethodMeta.getParameters();
        javaMethodMeta.getParams().forEach((key, classes) -> {
            Class<?>[] array = Arrays.stream(classes)
                    .filter(this.getPackageNameCodeGenMatcher()::match)
                    .toArray(Class<?>[]::new);
            if (array.length == 0) {
                return;
            }
            if (!this.genMatchingStrategy.isMatchParameter(javaMethodMeta, parameters.get(key))) {
                return;
            }
            effectiveParams.put(key, array);
        });
        int effectiveParamsSize = effectiveParams.size();
        // 合并复杂参数
        boolean hasComplexParams = margeComplexParamsFiled(commonCodeGenFiledMetas, effectiveParams);

        if (hasComplexParams && effectiveParamsSize == 1) {
            M methodHandle = converterMethodHandle(javaMethodMeta, classMeta, codeGenClassMeta);
            Map<String, CommonCodeGenClassMeta> params = methodHandle.getParams();
            String name = params.keySet().toArray(new String[0])[0];
            CommonCodeGenClassMeta genClassMeta = params.remove(name);
            params.put(DEFAULT_MARGE_PARAMS_NAME, genClassMeta);
            return methodHandle;
        }
        // 合并简单参数
        margeSimpleParams(javaMethodMeta, classMeta, effectiveParams, commonCodeGenFiledMetas, codeGenParamAnnotations, parameters);

        argsClassMeta.setFieldMetas(commonCodeGenFiledMetas.toArray(new CommonCodeGenFiledMeta[]{}));
        // 没有复杂对象的参数，为了防止重复名称，使用类名加方法名称
        String name = MessageFormat.format("{0}{1}Req",
                this.packageMapStrategy.convertClassName(classMeta.getClazz()),
                ToggleCaseUtils.toggleFirstChart(genMethodMeta.getName()));
        argsClassMeta.setName(name);
        argsClassMeta.setPackagePath(this.packageMapStrategy.genPackagePath(new String[]{DEFAULT_MARGE_PARAMS_NAME, name}));
        argsClassMeta.setAnnotations(new CommonCodeGenAnnotation[]{});
        argsClassMeta.setComments(new String[]{"合并方法参数生成的类"});

        // 判断当前参数是否为数组或集合类型 当前仅当只有一个复杂对象作为参数 且为数组或集合类的情况
        if (effectiveParamsSize == 1) {
            effectiveParams.values()
                    .forEach(paramClasses -> {
                        Class<?> paramClass = paramClasses[0];
                        CommonCodeGenClassMeta[] commonCodeGenClassMetas = languageTypeMapping.mapping(paramClasses)
                                .stream().map(c -> (CommonCodeGenClassMeta) c)
                                .toArray(CommonCodeGenClassMeta[]::new);
                        if (JavaArrayClassTypeMark.class.equals(paramClass)) {
                            //数组
                            argsClassMeta.setTypeVariables(commonCodeGenClassMetas);
                        } else if (JavaTypeUtils.isCollection(paramClass)) {
                            //集合
                            argsClassMeta.setTypeVariables(commonCodeGenClassMetas);
                        } else if (JavaTypeUtils.isMap(paramClass)) {
                            //map
                            argsClassMeta.setTypeVariables(commonCodeGenClassMetas);
                        } else {
                            log.warn("未处理的类型{}", paramClass.getName());
                        }
                    });
        }

        //加入依赖列表
        final Map<String, C> dependencies = (Map<String, C>) codeGenClassMeta.getDependencies();

        // 合并依赖
        Set<Class<?>> otherDependencies = effectiveParams.values()
                .stream()
                .map(Arrays::asList)
                .flatMap(Collection::stream)
                .filter(clazz -> !JavaArrayClassTypeMark.class.equals(clazz))
                .collect(Collectors.toSet());
        Map<String, C> argsClassMetaDependencies = this.fetchDependencies(otherDependencies);
        argsClassMeta.setDependencies(argsClassMetaDependencies);
        dependencies.putAll(argsClassMetaDependencies);
        dependencies.put(argsClassMeta.getName(), argsClassMeta);
        codeGenClassMeta.setDependencies(dependencies);
        LinkedHashMap<String, CommonCodeGenClassMeta> params = new LinkedHashMap<>();
        if (argsClassMeta.getTypeVariables() != null && effectiveParamsSize == 1
                && argsClassMeta.getTypeVariables().length > 0) {
            // 存在泛型合并名称
            String combineName = combineTypeDescStrategy.combine(argsClassMeta.getTypeVariables());
            argsClassMeta.setName(combineName);
        }
        //请求参数名称，固定为req
        params.put(DEFAULT_MARGE_PARAMS_NAME, argsClassMeta);
        genMethodMeta.setParams(params).setParamAnnotations(codeGenParamAnnotations);

        return genMethodMeta;
    }

    // 合并简单参数
    private void margeSimpleParams(JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta, Map<String, Class<?>[]> effectiveParams, Set<F> commonCodeGenFiledMetas, Map<String, CommonCodeGenAnnotation[]> codeGenParamAnnotations, Map<String, Parameter> parameters) {

        effectiveParams.forEach((key, classes) -> {
            // 注解
            Annotation[] paramAnnotations = javaMethodMeta.getParamAnnotations().get(key);
            // mock一个Java参数对象
            JavaParameterMeta javaParameterMeta = new JavaParameterMeta();
            javaParameterMeta.setTypes(classes)
                    .setIsTransient(false)
                    .setIsVolatile(false);
            javaParameterMeta.setAccessPermission(AccessPermission.PUBLIC);
            javaParameterMeta.setAnnotations(paramAnnotations);
            javaParameterMeta.setName(key);
            javaParameterMeta.setParameter(parameters.get(key));

            F commonCodeGenFiledMeta = this.converterField(javaParameterMeta, classMeta);
            if (commonCodeGenFiledMeta == null) {
                return;
            }
            if (commonCodeGenFiledMeta.getAnnotations() != null) {
                codeGenParamAnnotations.put(key, commonCodeGenFiledMeta.getAnnotations());
            }
            this.enhancedProcessingField(commonCodeGenFiledMeta, javaParameterMeta, classMeta);
            Class<?> clazz = classes[0];
            boolean isSimpleType = clazz == JavaArrayClassTypeMark.class || JavaTypeUtils.isAnArray(clazz) || JavaTypeUtils.isJavaBaseType(clazz) || clazz.isEnum();
            if (isSimpleType) {
                // 增加非复杂对象的参数
                commonCodeGenFiledMetas.add(commonCodeGenFiledMeta);
            }
            if (paramAnnotations == null || paramAnnotations.length == 0) {
                return;
            }
            String name = "";
            RequestParam requestParam = findSpringParamAnnotation(paramAnnotations, RequestParam.class);
            if (requestParam != null && StringUtils.hasText(requestParam.name())) {
                name = requestParam.name();
            }
            PathVariable pathVariable = findSpringParamAnnotation(paramAnnotations, PathVariable.class);
            if (pathVariable != null) {
                if (StringUtils.hasText(pathVariable.name())) {
                    name = pathVariable.name();
                }
                if (StringUtils.hasText(pathVariable.value())) {
                    name = pathVariable.value();
                }
            }

            if (StringUtils.hasText(name)) {
                commonCodeGenFiledMeta.setName(name);
            }
            if (commonCodeGenFiledMeta instanceof TypescriptFieldMate) {
                TypescriptFieldMate codeGenFiledMeta = (TypescriptFieldMate) commonCodeGenFiledMeta;
                if (!Boolean.TRUE.equals(codeGenFiledMeta.getRequired())) {
                    // 是否必填
                    if (requestParam != null) {
                        codeGenFiledMeta.setRequired(requestParam.required());
                    }

                    if (pathVariable != null) {
                        codeGenFiledMeta.setRequired(pathVariable.required());
                    }
                }
            }
        });
    }


    /**
     * 合并复杂参数的字段
     *
     * @param commonCodeGenFiledMetas 参数Filed List
     * @param effectiveParams         有效的参数列表
     * @return 是否存在复杂类型的参数
     */
    private boolean margeComplexParamsFiled(Set<F> commonCodeGenFiledMetas, Map<String, Class<?>[]> effectiveParams) {
        return effectiveParams.values().stream()
                .map(classes -> Arrays.stream(classes)
                        .filter(clazz -> !JavaArrayClassTypeMark.class.equals(clazz))
                        .filter(clazz -> !clazz.isEnum())
                        //非jdk中的复杂对象
                        .filter(JavaTypeUtils::isNoneJdkComplex)
                        .map(clazz -> {
                            C commonCodeGenClassMeta = this.parse(clazz);
                            if (commonCodeGenClassMeta == null) {
                                return false;
                            }
                            CommonCodeGenFiledMeta[] fieldMetas = commonCodeGenClassMeta.getFieldMetas();
                            if (fieldMetas == null) {
                                return false;
                            }
                            commonCodeGenFiledMetas.addAll(Arrays.stream(fieldMetas)
                                    .map(c -> (F) c)
                                    .collect(Collectors.toList()));
                            return true;
                        }))
                .flatMap(Stream::distinct)
                .collect(Collectors.toSet())
                .contains(Boolean.TRUE);


    }


    /**
     * 增强处理 class
     *
     * @param methodMeta 用于生成的方法元数据
     * @param classMeta  java 类元数据
     */
    protected abstract void enhancedProcessingClass(C methodMeta, JavaClassMeta classMeta);

    /**
     * 增强处理 method
     *
     * @param methodMeta
     * @param javaMethodMeta
     * @param classMeta
     */
    protected abstract void enhancedProcessingMethod(M methodMeta, JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta);


    /**
     * 增强处理注解
     *
     * @param codeGenAnnotation
     * @param annotation
     * @param annotationOwner
     */
    protected void enhancedProcessingAnnotation(CommonCodeGenAnnotation codeGenAnnotation, AnnotationMate annotation,
                                                Object annotationOwner) {
        if (annotationOwner instanceof Class) {

        }
        if (annotationOwner instanceof Method) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
//            RequestMethod httpMethod = RequestMethod.GET;
//            if (annotation instanceof RequestMappingProcessor.RequestMappingMate) {
//                httpMethod = ((RequestMappingProcessor.RequestMappingMate) annotation).getRequestMethod();
//            }
            //spring的mapping注解
            if (annotationType.getSimpleName().endsWith("Mapping")) {
                Method method = (Method) annotationOwner;
                //判断方法参数是否有RequestBody注解
                List<Annotation> annotationList = Arrays.stream(method.getParameterAnnotations())
                        .filter(Objects::nonNull)
                        .filter(annotations -> annotations.length > 0)
                        .map(Arrays::asList)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());

                String produces = codeGenAnnotation.getNamedArguments().get(MappingAnnotationPropNameConstant.PRODUCES);
                if (StringUtils.hasText(produces)) {
                    return;
                } else {
                    codeGenAnnotation.getNamedArguments().remove(MappingAnnotationPropNameConstant.PRODUCES);
                }

                //是否启用默认的 produces
//                boolean enableDefaultProduces = true;
//                if (!enableDefaultProduces) {
//                    return;
//                }
                boolean hasRequestBodyAnnotation = annotationList.size() > 0 && annotationList.stream()
                        .anyMatch(paramAnnotation -> RequestBody.class.equals(paramAnnotation.annotationType()));
                if (hasRequestBodyAnnotation) {
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


    /**
     * 抓取依赖列表
     *
     * @param dependencies
     * @return
     */
    protected Map<String, C> fetchDependencies(Set<Class<?>> dependencies) {

        if (dependencies == null || dependencies.size() == 0) {
            return new HashMap<>();
        }

        List<Class<?>> classList = dependencies.stream()
                .map(languageTypeMapping.getCustomizeJavaTypeMapping()::mapping)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .filter(this::isMatchGenCodeRule)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        Set<C> classMetas = classList.stream()
                .map(this::parse)
                .filter(Objects::nonNull)
                .map(c -> {
                    Map<String, ? extends CommonCodeGenClassMeta> cDependencies = c.getDependencies();
                    List<C> cs = new ArrayList<>((Collection<C>) cDependencies.values());
                    cs.add(c);
                    return cs;
                })
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .filter(CommonCodeGenClassMeta::getNeedImport)
                .collect(Collectors.toSet());

        classList.stream()
                .map(this.languageTypeMapping::mapping)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .filter(CommonCodeGenClassMeta::getNeedImport)
                .forEach(classMetas::add);

        Map<String, C> map = new HashMap<>();
        classMetas.forEach(c -> {
            map.put(c.getName(), c);
        });

        return map;

    }

    /**
     * 是否需要合并方法的请求参数
     *
     * @return
     */
    protected boolean needMargeMethodParams() {
        return false;
    }

    /**
     * 获取用于通过包名匹配的代码匹配器
     *
     * @return PackageNameCodeGenMatcher
     */
    protected CodeGenMatcher getPackageNameCodeGenMatcher() {
        Optional<CodeGenMatcher> optionalCodeGenMatcher = this.codeGenMatchers.stream()
                .filter(codeGenMatcher -> codeGenMatcher instanceof PackageNameCodeGenMatcher)
                .findFirst();
        if (!optionalCodeGenMatcher.isPresent()) {
            throw new RuntimeException("PackageNameCodeGenMatcher not found");
        }
        return optionalCodeGenMatcher.get();
    }

    /**
     * 尝试匹配是否Wie只需要导入的类
     *
     * @return <code>true</code> 改类只需要导入
     */
    protected boolean tryMatchOnlyImportClasses(Class clazz) {
        return this.codeGenImportMatchers.stream()
                .anyMatch(codeGenMatcher -> codeGenMatcher.match(clazz));

    }

    /**
     * 查找 Spring Param 相关的 Annotation
     *
     * @param annotations        注解列表
     * @param findAnnotationType 查找的注解类型
     * @return findAnnotationType类型注解的实例
     */
    private <T extends Annotation> T findSpringParamAnnotation(Annotation[] annotations, Class<T> findAnnotationType) {
        return Arrays.stream(annotations)
                .filter(annotation -> annotation.annotationType().equals(findAnnotationType))
                .map(annotation -> (T) annotation)
                .findFirst()
                .orElse(null);
    }

    /**
     * 检查spring mvc 控制器的方法
     *
     * @param javaMethodMeta java方法元数据描述
     * @param classMeta      java类元数据描述
     */
    private void checkSpringMvcMethod(JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta) {
        if (!classMeta.existAnnotation(Controller.class, RestController.class)) {
            return;
        }
        // 检查控制器方法是否合法
        if (!AccessPermission.PUBLIC.equals(javaMethodMeta.getAccessPermission())) {
            //
            throw new CodegenRuntimeException(classMeta.getClassName() + "的方法，" + javaMethodMeta.getName() + "是静态的或非公有方法");
        }
        RequestMappingProcessor.RequestMappingMate requestMappingMate = Arrays.stream(javaMethodMeta.getAnnotations())
                .map(annotation -> {
                    AnnotationProcessor<AnnotationMate, Annotation> processor = ANNOTATION_PROCESSOR_MAP.get(annotation.annotationType());
                    if (processor == null) {
                        return null;
                    }
                    return processor.process(annotation);
                })
                .filter(Objects::nonNull)
                .filter(annotationMate -> annotationMate instanceof RequestMappingProcessor.RequestMappingMate)
                .map(annotationMate -> (RequestMappingProcessor.RequestMappingMate) annotationMate)
                .findFirst()
                .orElse(null);

        if (requestMappingMate == null) {
            return;
        }

        RequestMethod requestMethod = requestMappingMate.getRequestMethod();
        String[] consumes = requestMappingMate.consumes();
        boolean hasRequestBody = javaMethodMeta.getParamAnnotations()
                .values()
                .stream()
                .map(Arrays::asList)
                .flatMap(Collection::stream)
                .filter(annotation -> RequestBody.class.equals(annotation.annotationType()))
                .findFirst()
                .orElse(null) != null;
        if (!hasRequestBody) {
            hasRequestBody = consumes.length > 0 && StringUtils.hasText(consumes[0]);
        }
        boolean isSupportRequestBody = RequestMappingProcessor.isSupportRequestBody(requestMethod);
        if (!isSupportRequestBody && hasRequestBody) {
            // get 请求不支持 RequestBody
            throw new RuntimeException(String.format("请求方法%s不支持RequestBody", requestMethod.name()));
        }
    }


    @Override
    public void setLanguageEnhancedProcessor(LanguageEnhancedProcessor languageEnhancedProcessor) {
        languageEnhancedProcessor.setCodeGenMatchers(this.codeGenMatchers);
        this.languageEnhancedProcessor = languageEnhancedProcessor;
    }

    @Override
    public List<CodeGenMatcher> getCodeGenMatchers() {
        return this.codeGenMatchers;
    }

    public AbstractLanguageTypeMapping<C> getLanguageTypeMapping() {
        return languageTypeMapping;
    }

    public void setLanguageTypeMapping(AbstractLanguageTypeMapping<C> languageTypeMapping) {
        this.languageTypeMapping = languageTypeMapping;
    }
}
