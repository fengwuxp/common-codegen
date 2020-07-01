package com.wuxp.codegen.languages;

import com.wuxp.codegen.annotation.processor.AnnotationMate;
import com.wuxp.codegen.annotation.processor.AnnotationProcessor;
import com.wuxp.codegen.annotation.processor.javax.NotNullProcessor;
import com.wuxp.codegen.annotation.processor.javax.PatternProcessor;
import com.wuxp.codegen.annotation.processor.javax.SizeProcessor;
import com.wuxp.codegen.annotation.processor.spring.RequestMappingProcessor;
import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.CodeGenMatcher;
import com.wuxp.codegen.core.macth.PackageNameCodeGenMatcher;
import com.wuxp.codegen.core.parser.GenericParser;
import com.wuxp.codegen.core.parser.JavaClassParser;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.parser.enhance.LanguageEnhancedProcessor;
import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.core.strategy.CombineTypeDescStrategy;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.core.utils.ToggleCaseUtil;
import com.wuxp.codegen.model.*;
import com.wuxp.codegen.model.constant.MappingAnnotationPropNameConstant;
import com.wuxp.codegen.model.constant.TypescriptFeignMediaTypeConstant;
import com.wuxp.codegen.model.enums.AccessPermission;
import com.wuxp.codegen.model.enums.ClassType;
import com.wuxp.codegen.model.languages.dart.DartClassMeta;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptFieldMate;
import com.wuxp.codegen.model.mapping.JavaArrayClassTypeMark;
import com.wuxp.codegen.model.mapping.TypeMapping;
import com.wuxp.codegen.model.utils.JavaTypeUtil;
import com.wuxp.codegen.types.SimpleCombineTypeDescStrategy;
import com.wuxp.codegen.utils.JavaMethodNameUtil;
import com.wuxp.codegen.utils.SpringControllerFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.wuxp.codegen.model.mapping.AbstractTypeMapping.*;


/**
 * 抽象的语言解释器
 *
 * @param <C> 类
 * @param <M> 方法
 * @param <F> 属性
 */
@Slf4j
public abstract class AbstractLanguageParser<C extends CommonCodeGenClassMeta,
        M extends CommonCodeGenMethodMeta,
        F extends CommonCodeGenFiledMeta> implements LanguageParser<C> {


    /**
     * 类被处理的次数
     */
    protected static final Map<Class<?>, Integer> HANDLE_COUNT = new ConcurrentHashMap<>();

    /**
     * 处理结果缓存
     */
    protected static final Map<Class<?>, Object> HANDLE_RESULT_CACHE = new ConcurrentHashMap<>();


    /**
     * annotationProcessorMap
     */
    public static final Map<Class<? extends Annotation>, AnnotationProcessor> ANNOTATION_PROCESSOR_MAP = new LinkedHashMap<>();


    /**
     * java类的解析器
     * 默认解析所有的属性 方法
     */
    protected GenericParser<JavaClassMeta, Class<?>> javaParser = new JavaClassParser(false);

    /**
     * 语言元数据对象的工厂
     */
    protected LanguageMetaInstanceFactory<C, M, F> languageMetaInstanceFactory;


    /**
     * 映射java类和typeScript类之间的关系
     */
    protected TypeMapping<Class<?>, List<C>> typeMapping;

    /**
     * 包名映射策略
     */
    protected PackageMapStrategy packageMapStrategy;

    /**
     * 代码检查者
     */
    protected Collection<CodeDetect> codeDetects;

    /**
     * 根据包名进行匹配
     */
    protected CodeGenMatcher packageNameCodeGenMatcher = new PackageNameCodeGenMatcher();


    /**
     * 生成匹配策略
     */
    protected CodeGenMatchingStrategy genMatchingStrategy;

    /**
     * 匹配器链
     */
    protected List<CodeGenMatcher> codeGenMatchers = new ArrayList<>();

    protected LanguageEnhancedProcessor<C, M, F> languageEnhancedProcessor = LanguageEnhancedProcessor.NONE;

    {
        codeGenMatchers.add(this.packageNameCodeGenMatcher);

        //根据java 类进行匹配
//        codeGenMatchers.add(clazz -> clazz.isEnum() || clazz.isAnnotation());

        //根据是否为spring的组件进行匹配
        codeGenMatchers.add(clazz -> {
            Annotation service = clazz.getAnnotation(Service.class);
            Annotation clazzAnnotation = clazz.getAnnotation(Component.class);
            //不是spring的组件
            return service == null && clazzAnnotation == null;
        });
    }

    protected CombineTypeDescStrategy combineTypeDescStrategy = new SimpleCombineTypeDescStrategy();

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
    }

    public AbstractLanguageParser(LanguageMetaInstanceFactory<C, M, F> languageMetaInstanceFactory,
                                  PackageMapStrategy packageMapStrategy,
                                  CodeGenMatchingStrategy genMatchingStrategy,
                                  Collection<CodeDetect> codeDetects) {
        this(languageMetaInstanceFactory, packageMapStrategy, genMatchingStrategy, codeDetects, null);
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
            this.codeGenMatchers.addAll(includeCodeGenMatchers);
        }
    }


    public AbstractLanguageParser(GenericParser<JavaClassMeta, Class<?>> javaParser,
                                  LanguageMetaInstanceFactory<C, M, F> languageMetaInstanceFactory,
                                  PackageMapStrategy packageMapStrategy,
                                  CodeGenMatchingStrategy genMatchingStrategy,
                                  Collection<CodeDetect> codeDetects) {
        this(languageMetaInstanceFactory, packageMapStrategy, genMatchingStrategy, codeDetects, null);
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
                JavaTypeUtil.isMap(source) ||
                JavaTypeUtil.isCollection(source)) {
            return null;
        }

        if (!JavaTypeUtil.isNoneJdkComplex(source)) {
            List<C> results = this.typeMapping.mapping(source);
            if (!results.isEmpty()) {
                return results.get(0);
            }
        }
        if (source.isEnum()) {
            //枚举出来
            List<C> results = this.typeMapping.mapping(Enum.class);
            if (!results.isEmpty()) {
                return results.get(0);
            }
        }


        if (HANDLE_COUNT.containsKey(source)) {
            //标记某个类被处理的次数如果超过2次，从缓存中返回
            if (HANDLE_COUNT.get(source) > 1) {
                return this.getResultToLocalCache(source);
            } else {
                HANDLE_COUNT.put(source, HANDLE_COUNT.get(source) + 1);
            }
        } else {
            HANDLE_COUNT.put(source, 1);
        }
        Integer count = HANDLE_COUNT.get(source);

        CommonCodeGenClassMeta mapping = customizeTypeMapping.mapping(source);

        if (mapping != null) {
            HANDLE_RESULT_CACHE.put(source, mapping);
            return (C) mapping;
        } else {
            mapping = baseTypeMapping.mapping(source);
            if (mapping != null) {
                HANDLE_RESULT_CACHE.put(source, mapping);
                return (C) mapping;
            }
        }


        JavaClassMeta javaClassMeta = this.javaParser.parse(source);
        //加入对spring的特别处理
        SpringControllerFilter.filterMethods(javaClassMeta);

        boolean isApiServiceClass = javaClassMeta.isApiServiceClass();
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
        HANDLE_RESULT_CACHE.put(source, meta);
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
        final Map<String, C> metaDependencies = meta.getDependencies() == null ? new LinkedHashMap<>() : (Map<String, C>) meta.getDependencies();
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
                            List<C> cs = this.typeMapping.mapping(clazz);
                            if (cs.isEmpty()) {
                                return null;
                            } else {
                                typeVariable = cs.get(0);
                            }
                        }
                        if (JavaTypeUtil.isNoneJdkComplex(clazz)) {
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

        return meta;
    }

    @Override
    public LanguageMetaInstanceFactory getLanguageMetaInstanceFactory() {
        return this.languageMetaInstanceFactory;
    }

    @Override
    public void addCodeGenMatchers(CodeGenMatcher... codeGenMatchers) {
        this.codeGenMatchers.addAll(Arrays.asList(codeGenMatchers));
    }

    /**
     * 是否匹配生成的规则
     *
     * @param clazz
     * @return
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
     * @param source
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

        C c = (C) HANDLE_RESULT_CACHE.get(clazz);
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
     * @return
     */
    protected List<String> generateComments(Annotation[] annotations, Object owner) {
        if (annotations == null || annotations.length == 0) {
            return new ArrayList<>();
        }

        return Arrays.stream(annotations).map(annotation -> {
            //将javax的验证注解转换为注释
            AnnotationProcessor processor = ANNOTATION_PROCESSOR_MAP.get(annotation.annotationType());
            if (processor == null) {
                return null;
            }
            return processor.process(annotation).toComment(owner);
        }).filter(Objects::nonNull)
                .filter(StringUtils::hasText)
                .collect(Collectors.toList());
    }

    /**
     * 通过类类型来获取注释
     *
     * @param classes
     * @param isMethod
     * @return
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
     * @param classes
     * @return
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
     * @param annotations
     * @param annotationOwner 注解持有者
     * @return
     */
    protected CommonCodeGenAnnotation[] converterAnnotations(Annotation[] annotations, Object annotationOwner) {
        if (annotations == null || annotations.length == 0) {
            return new CommonCodeGenAnnotation[]{};
        }

        return Arrays.stream(annotations).map(annotation -> {
            AnnotationProcessor<AnnotationMate, Annotation> processor = ANNOTATION_PROCESSOR_MAP.get(annotation.annotationType());
            if (processor == null) {
                return null;
            }

            AnnotationMate annotationMate = processor.process(annotation);
            CommonCodeGenAnnotation toAnnotation = annotationMate.toAnnotation(annotationOwner);

            if (toAnnotation != null) {
                this.enhancedProcessingAnnotation(toAnnotation, annotationMate, annotationOwner);
            }

            return toAnnotation;
        }).filter(Objects::nonNull)
                .toArray(CommonCodeGenAnnotation[]::new);
    }


    /**
     * 转换属性列表
     *
     * @param javaFieldMetas
     * @param classMeta
     * @return
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
                        return JavaMethodNameUtil.isGetMethodOrIsMethod(javaMethodMeta.getName());
                    })
                    .filter(javaMethodMeta -> Boolean.FALSE.equals(javaMethodMeta.getIsStatic()) && Boolean.FALSE.equals(javaMethodMeta.getIsAbstract()) && Boolean.FALSE.equals(javaMethodMeta.getIsTransient()))
                    .filter(javaMethodMeta -> javaMethodMeta.getReturnType() != null && javaMethodMeta.getReturnType().length > 0)
                    .filter(javaMethodMeta -> {
                        //属性是否已经存在
                        return !fieldNameList.contains(JavaMethodNameUtil.replaceGetOrIsPrefix(javaMethodMeta.getName()));
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
                                .setName(JavaMethodNameUtil.replaceGetOrIsPrefix(methodMeta.getName()))
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

    ;


    protected F converterField(JavaFieldMeta javaFieldMeta, JavaClassMeta classMeta) {
        if (javaFieldMeta == null) {
            return null;
        }

        boolean isEnum = classMeta.getClazz().isEnum();
        if (javaFieldMeta.getIsStatic() && !isEnum) {
            //不处理静态类型的字段
            return null;
        }
        F fieldInstance = this.languageMetaInstanceFactory.newFieldInstance();

        fieldInstance.setName(javaFieldMeta.getName());
        fieldInstance.setAccessPermission(javaFieldMeta.getAccessPermission());

        //注释来源于注解和java的类类型
        List<String> comments = this.generateComments(javaFieldMeta.getAnnotations(), javaFieldMeta.getField());
        Class<?>[] types = javaFieldMeta.getTypes();
        if (!isEnum) {
            comments.addAll(this.generateComments(types, false));
        } else {
            if (comments.size() == 0) {
                comments.add(javaFieldMeta.getName());
                log.error("枚举没有加上描述相关的注解");
            }
        }

        //注解
        fieldInstance.setComments(comments.toArray(new String[]{}));

        //注解
        fieldInstance.setAnnotations(this.converterAnnotations(javaFieldMeta.getAnnotations(), javaFieldMeta.getField()));

        //field 类型类别
        Collection<C> classMetaMappings = this.typeMapping.mapping(types);

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
     * @param fieldMeta
     * @param javaFieldMeta
     * @param classMeta
     */
    protected abstract void enhancedProcessingField(F fieldMeta, JavaFieldMeta javaFieldMeta, JavaClassMeta classMeta);


    /**
     * 转换方法列表
     *
     * @param javaMethodMetas
     * @param classMeta
     * @return
     */
    protected List<M> converterMethodMetas(JavaMethodMeta[] javaMethodMetas, JavaClassMeta classMeta, C codeGenClassMeta) {
        if (javaMethodMetas == null) {
            return Collections.EMPTY_LIST;
        }
        List<M> codegenMethods = Arrays.stream(javaMethodMetas)
                .filter(javaMethodMeta -> Boolean.FALSE.equals(javaMethodMeta.getIsStatic()))
                .filter(javaMethodMeta -> Boolean.FALSE.equals(javaMethodMeta.getIsTransient()))
                .filter(javaMethodMeta -> this.genMatchingStrategy.isMatchMethod(javaMethodMeta))
                .map(methodMeta -> this.converterMethod(methodMeta, classMeta, codeGenClassMeta))
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

        //判断是否存在方法名称是否相同
        codegenMethods.forEach(m -> codegenMethods.stream()
                .filter(m1 -> m1.getName().equals(m.getName()) && !m1.equals(m))
                .findFirst()
                .ifPresent(m2 -> m2.setName(MessageFormat.format("override_{0}", m2.getName()))));

        return codegenMethods;


    }

    /**
     * 处理方法转换
     * 1：处理方法上的注解
     * 2：处理方法返回值
     * 3: 处理方法参数
     *
     * @param javaMethodMeta
     * @param classMeta
     * @param codeGenClassMeta
     * @return
     */
    protected M converterMethod(JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta, C codeGenClassMeta) {
        if (classMeta == null) {
            return null;
        }
        checkSpringMvcMethod(javaMethodMeta, classMeta);
        M methodCodeMeta;
        if (codeGenClassMeta instanceof DartClassMeta) {
            methodCodeMeta = converterMethodHandle(javaMethodMeta, classMeta, codeGenClassMeta);
        } else {
            // 需要合并参数
            methodCodeMeta = converterMethodAndMargeParams(javaMethodMeta, classMeta, codeGenClassMeta);
        }
        methodCodeMeta = this.languageEnhancedProcessor.enhancedProcessingMethod(methodCodeMeta, javaMethodMeta, classMeta);
        //增强处理
        this.enhancedProcessingMethod(methodCodeMeta, javaMethodMeta, classMeta);
        return methodCodeMeta;
    }

    /**
     * 检查spring mvc 控制器的方法
     *
     * @param javaMethodMeta
     * @param classMeta
     */
    private void checkSpringMvcMethod(JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta) {
        if (classMeta.getAnnotation(Controller.class) == null && classMeta.getAnnotation(RestController.class) == null) {
            return;
        }
        // 检查控制器方法是否合法
        if (!AccessPermission.PUBLIC.equals(javaMethodMeta.getAccessPermission())) {
            //
            throw new RuntimeException(classMeta.getClassName() + "的方法，" + javaMethodMeta.getName() + "是静态的或非公有方法");
        }
        RequestMappingProcessor.RequestMappingMate requestMappingMate = Arrays.stream(javaMethodMeta.getAnnotations()).map(annotation -> {
            AnnotationProcessor<AnnotationMate, Annotation> processor = ANNOTATION_PROCESSOR_MAP.get(annotation.annotationType());
            if (processor == null) {
                return null;
            }
            return processor.process(annotation);
        }).filter(Objects::nonNull)
                .filter(annotationMate -> annotationMate instanceof RequestMappingProcessor.RequestMappingMate)
                .map(annotationMate -> (RequestMappingProcessor.RequestMappingMate) annotationMate).findFirst()
                .orElse(null);

        if (requestMappingMate == null) {
            return;
        }

        boolean isGetMethod = RequestMethod.GET.equals(requestMappingMate.getRequestMethod());
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
            hasRequestBody = consumes.length > 0 && (MediaType.APPLICATION_JSON_UTF8_VALUE.equals(consumes[0]) ||
                    MediaType.APPLICATION_JSON_VALUE.equals(consumes[0]));
        }
        if (isGetMethod && hasRequestBody) {
            // get 请求不支持 RequestBody
            throw new RuntimeException(classMeta.getClassName() + "的方法，" + javaMethodMeta.getName() + "是GET请求，不支持RequestBody");
        }
//        if (RequestMethod.POST.equals(requestMappingMate.getRequestMethod())) {
//            // post方法需要 RequestBody
//        }
    }


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
        List<C> returnTypes = this.typeMapping.mapping(methodMetaReturnType);
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
                    .filter(this.packageNameCodeGenMatcher::match)
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
            C genClassInstance = this.languageMetaInstanceFactory.newClassInstance();
            List<C> paramTypes = this.typeMapping.mapping(classes);
            Class<?> paramType = classes[0];
            genClassInstance.setTypeVariables(paramTypes.toArray(new CommonCodeGenClassMeta[0]));
            // 注解
            Annotation[] annotations = paramAnnotations.get(key);
            CommonCodeGenAnnotation[] commonCodeGenAnnotations = this.converterAnnotations(annotations, parameters.get(key));
            genClassInstance.setClassType(ClassType.CLASS)
                    .setSource(paramType)
                    .setAnnotations(commonCodeGenAnnotations)
                    .setIsAbstract(false)
                    .setAccessPermission(AccessPermission.PUBLIC)
                    .setIsFinal(false)
                    .setIsStatic(false)
                    .setName(paramType.getSimpleName());
            codeGenParams.put(key, genClassInstance);
            codeGenParamAnnotations.put(key, commonCodeGenAnnotations);
        });

        genMethodMeta.setParams(codeGenParams)
                .setParamAnnotations(codeGenParamAnnotations);


        return genMethodMeta;
    }

    /**
     * 方法转换 并且将多个参数合并为一个对象参数
     *
     * @param javaMethodMeta
     * @param classMeta
     * @param codeGenClassMeta
     * @return
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


        //处理方法的参数
        //1: 参数过滤（过滤掉控制器方法中servlet相关的参数等等）
        Map<String, Class<?>[]> methodMetaParams = javaMethodMeta.getParams();
        //有效的参数
        final Map<String, Class<?>[]> effectiveParams = new LinkedHashMap<>();
        methodMetaParams.forEach((key, classes) -> {
            Class<?>[] array = Arrays.stream(classes)
                    .filter(this.packageNameCodeGenMatcher::match)
                    .toArray(Class<?>[]::new);
            if (array.length == 0) {
                return;
            }
            effectiveParams.put(key, array);
        });

        //2: 合并参数列表，将参数列表中的简单类型参数和复杂的类型参数合并到一个列表中
        //2.1：遍历展开参数列表

        final Set<F> commonCodeGenFiledMetas = new LinkedHashSet<>();

        // 参数的元数据类型信息
        final C argsClassMeta = this.languageMetaInstanceFactory.newClassInstance();
        int effectiveParamsSize = effectiveParams.size();
        effectiveParams.forEach((key, classes) -> {
            Optional<Boolean> isNext = Arrays.stream(classes)
                    .filter(clazz -> !JavaArrayClassTypeMark.class.equals(clazz))
                    .filter(clazz -> !clazz.isEnum())
                    .map(clazz -> {
                        if (!JavaTypeUtil.isNoneJdkComplex(clazz)) {
                            return false;
                        }
                        //非jdk中的复杂对象
                        C commonCodeGenClassMeta = this.parse(clazz);
                        if (commonCodeGenClassMeta == null) {
                            return false;
                        }
                        if (argsClassMeta.getFieldMetas() == null && effectiveParamsSize == 1) {
                            // 当只有一个有效的复杂参数依赖
                            BeanUtils.copyProperties(commonCodeGenClassMeta, argsClassMeta);
                            // 清空依赖 防止递归生成
                            argsClassMeta.setDependencies(new HashMap<>());
                            return true;
                        } else {
                            //TODO 有多个复杂类型的参数
//                            CommonCodeGenFiledMeta[] filedMetas = argsClassMeta.getFieldMetas();
//                            List<CommonCodeGenFiledMeta> collect = Arrays.stream(filedMetas)
//                                    .collect(Collectors.toList());
//                            collect.addAll(Arrays.asList(commonCodeGenClassMeta.getFieldMetas()));
//                            argsClassMeta.setFieldMetas(collect.toArray(new CommonCodeGenFiledMeta[0]));

                        }
                        return false;
                    }).filter(flag -> flag)
                    .findFirst();


            if (isNext.isPresent() && Boolean.TRUE.equals(isNext.get())) {
                return;
            }

            // 注解
            Annotation[] paramAnnotations = javaMethodMeta.getParamAnnotations().get(key);

            JavaFieldMeta javaFieldMeta = new JavaFieldMeta();
            javaFieldMeta.setTypes(classes)
                    .setIsTransient(false)
                    .setIsVolatile(false);
            javaFieldMeta.setAccessPermission(AccessPermission.PUBLIC);
            javaFieldMeta.setAnnotations(paramAnnotations);
            javaFieldMeta.setName(key);

            F commonCodeGenFiledMeta = this.converterField(javaFieldMeta, classMeta);
            if (commonCodeGenFiledMeta == null) {
                return;
            }

            this.enhancedProcessingField(commonCodeGenFiledMeta, javaFieldMeta, classMeta);
            commonCodeGenFiledMetas.add(commonCodeGenFiledMeta);
            if (paramAnnotations == null || paramAnnotations.length == 0) {
                return;
            }
            String name = "";
            //TODO 参数是否必须 是否为控制器  是否存在javax的验证注解、或者spring mvc相关注解 required=true 或者是swagger注解
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
                        codeGenFiledMeta.setRequired(true);
                    }
                }
            }

        });
        //3: 重组，使用第二步得到的列表构建一个信息的 GeneCodeClassMeta对象
//        argsClassMeta.setClassType(ClassType.INTERFACE);
        if (argsClassMeta.getFieldMetas() != null) {
            Arrays.asList(argsClassMeta.getFieldMetas()).forEach(genFiledMeta -> {
                F commonCodeGenFiledMeta = this.languageMetaInstanceFactory.newFieldInstance();
                BeanUtils.copyProperties(genFiledMeta, commonCodeGenFiledMeta);
                boolean isExist = commonCodeGenFiledMetas.stream()
                        .filter(c -> c.getName().equals(genFiledMeta.getName()))
                        .toArray().length > 0;
                if (isExist) {
                    log.error("{}方法中的参数{}在类{}中已经存在", javaMethodMeta.getName(), genFiledMeta.getName(), argsClassMeta.getPackagePath() + argsClassMeta.getName());
                } else {
                    commonCodeGenFiledMetas.add(commonCodeGenFiledMeta);
                }

            });
        }
        argsClassMeta.setFieldMetas(commonCodeGenFiledMetas.toArray(new CommonCodeGenFiledMeta[]{}));
        if (!StringUtils.hasText(argsClassMeta.getName())) {
            //没有复杂对象的参数，为了防止重复名称，使用类名加方法名称
            String name = MessageFormat.format("{0}{1}Req",
                    this.packageMapStrategy.convertClassName(classMeta.getClazz()),
                    ToggleCaseUtil.toggleFirstChart(genMethodMeta.getName()));
            argsClassMeta.setName(name);
            argsClassMeta.setPackagePath(this.packageMapStrategy.genPackagePath(new String[]{"req", name}));
            //这个时候没有依赖
            argsClassMeta.setAnnotations(new CommonCodeGenAnnotation[]{});
            argsClassMeta.setComments(new String[]{"合并方法参数生成的类"});
        } else {
            boolean hasComplex = false, hasSimple = false;
            for (Class<?>[] classes : effectiveParams.values()) {
                Optional<Class<?>> classOptional = Arrays.stream(classes).filter(clazz -> !JavaArrayClassTypeMark.class.equals(clazz))
                        .filter(clazz -> !clazz.isEnum())
                        .filter(JavaTypeUtil::isNoneJdkComplex)
                        .findFirst();
                if (classOptional.isPresent()) {
                    hasComplex = true;
                } else {
                    hasSimple = true;
                }
            }
            if (hasComplex && hasSimple) {
                //参数列表中有复杂对象，并且有额外的简单对象，将类的名称替换，使用方法的名称,重新生成过一个新的对象
                String name = MessageFormat.format("{0}Req", ToggleCaseUtil.toggleFirstChart(genMethodMeta.getName()));
                argsClassMeta.setPackagePath(argsClassMeta.getPackagePath().replace(argsClassMeta.getName(), name));
                argsClassMeta.setName(name);
            }

        }

        // 判断当前参数是否为数组或集合类型 当前仅当只有一个复杂对象作为参数 且为数组或集合类的情况
        if (effectiveParamsSize == 1) {
            effectiveParams.values().stream()
                    .filter(paramClass -> {
                        // 是否存在复杂对象
                        Optional<Class<?>> classOptional = Arrays.stream(paramClass).filter(clazz -> !JavaArrayClassTypeMark.class.equals(clazz))
                                .filter(clazz -> !clazz.isEnum())
                                .filter(JavaTypeUtil::isNoneJdkComplex)
                                .findFirst();
                        return classOptional.isPresent();
                    })
                    .forEach(paramClasses -> {
                        Class<?> paramClass = paramClasses[0];
                        CommonCodeGenClassMeta[] commonCodeGenClassMetas = typeMapping.mapping(paramClasses)
                                .stream().map(c -> (CommonCodeGenClassMeta) c)
                                .toArray(CommonCodeGenClassMeta[]::new);
                        if (JavaArrayClassTypeMark.class.equals(paramClass)) {
                            //数组
                            argsClassMeta.setTypeVariables(commonCodeGenClassMetas);
                        } else if (JavaTypeUtil.isCollection(paramClass)) {
                            //集合
                            argsClassMeta.setTypeVariables(commonCodeGenClassMetas);
                        } else if (JavaTypeUtil.isMap(paramClass)) {
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
//        argsClassMeta.setPackagePath("");
        //请求参数名称，固定为req
        params.put("req", argsClassMeta);
        genMethodMeta.setParams(params);


        return genMethodMeta;
    }


    /**
     * 增强处理 class
     *
     * @param methodMeta
     * @param classMeta
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
    protected void enhancedProcessingAnnotation(CommonCodeGenAnnotation codeGenAnnotation, AnnotationMate annotation, Object annotationOwner) {
        if (annotationOwner instanceof Class) {

        }
        if (annotationOwner instanceof Method) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            boolean isGetMethod = annotationType.equals(GetMapping.class) || annotationType.equals(RequestMapping.class);
            if (isGetMethod) {
                return;
            }
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
                    //如果没有 RequestBody 则认为是已表单的方式提交的参数
                    //是spring的Mapping注解
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
                .map(customizeJavaTypeMapping::mapping)
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
                .map(this.typeMapping::mapping)
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
     * 查找 Spring Param 相关的 Annotation
     *
     * @param annotations
     * @param annotationType
     * @return
     */
    private <T extends Annotation> T findSpringParamAnnotation(Annotation[] annotations, Class<T> annotationType) {
        return Arrays.stream(annotations)
                .filter(annotation -> annotation.annotationType().equals(annotationType))
                .map(annotation -> (T) annotation)
                .findFirst()
                .orElse(null);
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
}
