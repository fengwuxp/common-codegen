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
import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.core.utils.ToggleCaseUtil;
import com.wuxp.codegen.model.*;
import com.wuxp.codegen.model.enums.AccessPermission;
import com.wuxp.codegen.model.enums.ClassType;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.model.mapping.TypeMapping;
import com.wuxp.codegen.model.utils.JavaTypeUtil;
import com.wuxp.codegen.utils.JavaMethodNameUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
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
        this.languageMetaInstanceFactory = languageMetaInstanceFactory;
        this.packageMapStrategy = packageMapStrategy;
        this.genMatchingStrategy = genMatchingStrategy;
        if (codeDetects != null) {
            this.codeDetects = codeDetects;
        }

    }


    public AbstractLanguageParser(GenericParser<JavaClassMeta, Class<?>> javaParser,
                                  LanguageMetaInstanceFactory<C, M, F> languageMetaInstanceFactory,
                                  PackageMapStrategy packageMapStrategy,
                                  CodeGenMatchingStrategy genMatchingStrategy,
                                  Collection<CodeDetect> codeDetects) {
        this(languageMetaInstanceFactory, packageMapStrategy, genMatchingStrategy, codeDetects);
        if (javaParser != null) {
            this.javaParser = javaParser;
        }

    }

    @Override
    public C parse(Class<?> source) {
        //符合匹配规则，或非集合类型和Map的的子类进行

        if (!JavaTypeUtil.isNoneJdkComplex(source)) {
            List<C> results = this.typeMapping.mapping(source);
            if (!results.isEmpty()) {
                return results.get(0);
            }
        }

        if (!this.isMatchGenCodeRule(source) ||
                JavaTypeUtil.isMap(source) ||
                JavaTypeUtil.isCollection(source)) {
            return null;
        }

        if (HANDLE_COUNT.containsKey(source)) {
            //标记某个类被处理的次数如果超过2次，从缓存中返回
            if (HANDLE_COUNT.get(source) > 5) {
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
        if (javaClassMeta.isApiServiceClass()) {
            //要生成的服务，判断是否需要生成
            if (!this.genMatchingStrategy.isMatchClazz(javaClassMeta)) {
                //跳过
                log.warn("跳过类{}", source.getName());
                return null;
            }
        }

        C meta = this.getResultToLocalCache(source);

        if (meta != null) {
            return meta;
        }

        //检查代码
        this.detectJavaCode(javaClassMeta);

        meta = this.languageMetaInstanceFactory.newClassInstance();
        meta.setSource(source);
        meta.setName(this.packageMapStrategy.convertClassName(source.getSimpleName()));
        meta.setPackagePath(this.packageMapStrategy.convert(source));
        meta.setClassType(javaClassMeta.getClassType());
        meta.setAccessPermission(javaClassMeta.getAccessPermission());
        meta.setTypeVariables(Arrays.stream(javaClassMeta.getTypeVariables())
                .map(type -> {
                    if (type instanceof Class) {
                        return this.parse((Class) type);
                    } else if (type instanceof TypeVariable) {
                        return TypescriptClassMeta.TYPE_VARIABLE;
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toArray(CommonCodeGenClassMeta[]::new));


        String genericDescription = Arrays.stream(meta.getTypeVariables())
                .map(CommonBaseMeta::getName)
                .collect(Collectors.joining(","));
        if (StringUtils.hasText(genericDescription)) {
            meta.setGenericDescription(MessageFormat.format("<{0}>", genericDescription));
        }


        Class<?> javaClassSuperClass = javaClassMeta.getSuperClass();
        if (!Object.class.equals(javaClassSuperClass)){
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

        if (count == 1) {
            if (javaClassMeta.isApiServiceClass()) {
                //spring的控制器  生成方法列表
                meta.setMethodMetas(this.converterMethodMetas(javaClassMeta.getMethodMetas(), javaClassMeta, meta)
                        .toArray(new CommonCodeGenMethodMeta[]{}));
            } else {
                // 普通的java bean DTO  生成属性列表
                meta.setFiledMetas(this.converterFieldMetas(javaClassMeta.getFieldMetas(), javaClassMeta)
                        .toArray(new CommonCodeGenFiledMeta[]{}));
            }
        }

        //依赖处理
        final Map<String, C> metaDependencies = meta.getDependencies() == null ? new LinkedHashMap<>() : (Map<String, C>) meta.getDependencies();

        if (count == 1) {
            //依赖列表
            Set<Class<?>> dependencyList = javaClassMeta.getDependencyList();
            if (javaClassMeta.isApiServiceClass()) {
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

        //处理超类上面的类型变量
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
                        if (JavaTypeUtil.isNoneJdkComplex(clazz)) {
                            metaDependencies.put(typeVariable.getName(), typeVariable);
                        }
                        return typeVariable;
                    })
                    .filter(Objects::nonNull)
                    .toArray(CommonCodeGenClassMeta[]::new);
            superTypeVariables.put(typescriptClassMeta.getName(), typeVariables);
        });

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

        HANDLE_RESULT_CACHE.put(source, meta);
        return meta;
    }

    @Override
    public LanguageMetaInstanceFactory getLanguageMetaInstanceFactory() {
        return this.languageMetaInstanceFactory;
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

        int size = this.codeGenMatchers.size();

        //必须满足所有的匹配器才能进行生成
        int result = (int) codeGenMatchers.stream()
                .map(codeGenMatcher -> codeGenMatcher.match(clazz))
                .filter(r -> r)
                .count();
        if (result == size) {
            log.debug("符合生成条件的类：{}", clazz.getName());
        }
        return result == size;
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

        return (C) HANDLE_RESULT_CACHE.get(clazz);
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
        return Arrays.stream(classes).map(clazz -> (isMethod ? "返回值" : "") + "在java中的类型为：" + clazz.getSimpleName()).collect(Collectors.toList());
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

        return Arrays.stream(classes).map(Class::getName).collect(Collectors.joining(","));
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


        final List<String> fieldNameList = Arrays.stream(javaFieldMetas)
                .map(CommonBaseMeta::getName)
                .collect(Collectors.toList());

        boolean isEnum = classMeta.getClazz().isEnum();

        List<JavaFieldMeta> fieldMetas = new ArrayList<>(Arrays.asList(javaFieldMetas));

        if (!isEnum) {
            //如果是java bean 需要合并get方法
            // 找出java bean中不存在属性定义的get或 is方法
            fieldMetas.addAll(Arrays.stream(classMeta.getMethodMetas())
                    .filter(javaMethodMeta -> {
                        //匹配getXX 或isXxx方法
                        return JavaMethodNameUtil.isGetMethodOrIsMethod(javaMethodMeta.getName());
                    })
                    .filter(javaMethodMeta -> Boolean.FALSE.equals(javaMethodMeta.getIsStatic()) || Boolean.FALSE.equals(javaMethodMeta.getIsAbstract()))
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
                                .setAccessPermission(AccessPermission.PUBLIC)
                                .setName(JavaMethodNameUtil.replaceGetOrIsPrefix(methodMeta.getName()))
                                .setIsStatic(false)
                                .setIsFinal(false);


                        return fieldMeta;
                    }).collect(Collectors.toList()));
        }

        return fieldMetas.stream()
                .filter(javaFieldMeta -> {
                    //过滤掉非枚举的静态变量
                    if (isEnum) {
                        return true;
                    }
                    return Boolean.FALSE.equals(javaFieldMeta.getIsStatic());
                })
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
        if (!isEnum) {
            comments.addAll(this.generateComments(javaFieldMeta.getTypes(), false));
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
        Collection<C> classMetaMappings = this.typeMapping.mapping(javaFieldMeta.getTypes());

        //从泛型中解析
        Type[] typeVariables = javaFieldMeta.getTypeVariables();
        if (typeVariables != null && typeVariables.length > 0) {

            classMetaMappings.addAll(Arrays.stream(typeVariables)
                    .filter(Objects::nonNull)
                    .map(Type::getTypeName).map(name -> {
                        C classInstance = this.languageMetaInstanceFactory.newClassInstance();
                        BeanUtils.copyProperties(this.languageMetaInstanceFactory.getTypeVariableInstance(), classInstance);
                        classInstance.setName(name);
                        return classInstance;
                    }).collect(Collectors.toList()));
        }

        if (classMetaMappings.size() > 0) {
            //域对象类型描述
            fieldInstance.setFiledTypes(classMetaMappings.toArray(new CommonCodeGenClassMeta[]{}));
        } else {

            //解析失败
            throw new RuntimeException(String.format("解析类 %s 上的属性 %s 的类型 %s 失败",
                    classMeta.getClassName(),
                    javaFieldMeta.getName(),
                    this.classToNamedString(javaFieldMeta.getTypes())));
        }


        //TODO 注解转化

        //增强处理
        this.enhancedProcessingField(fieldInstance, javaFieldMeta, classMeta);

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
        return Arrays.stream(javaMethodMetas)
                .filter(javaMethodMeta -> Boolean.FALSE.equals(javaMethodMeta.getIsStatic()))
                .filter(javaMethodMeta -> this.genMatchingStrategy.isMatchMethod(javaMethodMeta))
                .map(methodMeta -> this.converterMethod(methodMeta, classMeta, codeGenClassMeta))
                .distinct()
                .collect(Collectors.toList());


    }


    protected M converterMethod(JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta, C codeGenClassMeta) {
        if (classMeta == null) {
            return null;
        }

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
        Map<String, Class<?>[]> effectiveParams = new LinkedHashMap<>();
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
        //参数的元数据类型信息
        final C argsClassMeta = this.languageMetaInstanceFactory.newClassInstance();


        effectiveParams.forEach((key, classes) -> {

            Class<?> clazz = classes[0];
            if (JavaTypeUtil.isNoneJdkComplex(clazz)) {
                //非jdk中的复杂对象
                C commonCodeGenClassMeta = this.parse(clazz);
                if (commonCodeGenClassMeta != null) {
                    BeanUtils.copyProperties(commonCodeGenClassMeta, argsClassMeta);
                }
            } else if (clazz.isEnum()) {
                //枚举
                F fieldMate = this.languageMetaInstanceFactory.newFieldInstance();
                commonCodeGenFiledMetas.add(fieldMate);

            } else {

                Set<Class<?>> otherDependencies = new HashSet<>();

                if (clazz.isArray()) {
                    //数组
                    otherDependencies.add(clazz.getComponentType());
                } else if (JavaTypeUtil.isCollection(clazz)) {
                    //集合
                    otherDependencies.addAll(Arrays.asList(classes));
                } else if (JavaTypeUtil.isMap(clazz)) {
                    //map
                    otherDependencies.addAll(Arrays.asList(classes));
                } else if (JavaTypeUtil.isJavaBaseType(clazz)) {
                    //简单数据类型
                } else {
                    log.warn("未处理的类型{}", clazz.getName());
                }

                this.fetchDependencies(otherDependencies).forEach((k, v) -> {
                    Map<String, C> dependencies = (Map<String, C>) argsClassMeta.getDependencies();
                    dependencies.put(k, v);
                });

                //注释
                Annotation[] annotations = javaMethodMeta.getParamAnnotations().get(key);

                //TODO 参数是否必须 是否为控制器  是否存在javax的验证注解、或者spring mvc相关注解 required=true 或者是swagger注解
//                    Arrays.stream(annotations).macth(annotation -> {
//                        annotation.annotationType().equals(Reque)
//                        return true;
//                    })
                JavaFieldMeta javaFieldMeta = new JavaFieldMeta();
                javaFieldMeta.setTypes(classes)
                        .setIsTransient(false)
                        .setIsVolatile(false);
                javaFieldMeta.setAccessPermission(AccessPermission.PUBLIC);
                javaFieldMeta.setAnnotations(annotations);
                javaFieldMeta.setName(key);
                F commonCodeGenFiledMeta = this.converterField(javaFieldMeta, classMeta);

                if (commonCodeGenFiledMeta != null) {
                    this.enhancedProcessingField(commonCodeGenFiledMeta, javaFieldMeta, classMeta);
                    commonCodeGenFiledMetas.add(commonCodeGenFiledMeta);
                }
            }

        });
        //3: 重组，使用第二步得到的列表构建一个信息的 TypescriptClassMeta对象，类型为typescript的interface
        argsClassMeta.setClassType(ClassType.INTERFACE);
        if (argsClassMeta.getFiledMetas() != null) {
            Arrays.asList(argsClassMeta.getFiledMetas()).forEach(genFiledMeta -> {
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
        argsClassMeta.setFiledMetas(commonCodeGenFiledMetas.toArray(new CommonCodeGenFiledMeta[]{}));
        if (!StringUtils.hasText(argsClassMeta.getName())) {
            //没有复杂对象的参数
            String name = MessageFormat.format("{0}Req", ToggleCaseUtil.toggleFirstChart(genMethodMeta.getName()));
            argsClassMeta.setName(name);
            argsClassMeta.setPackagePath(this.packageMapStrategy.genPackagePath(new String[]{"req", name}));
            //这个时候没有依赖
            argsClassMeta.setAnnotations(new CommonCodeGenAnnotation[]{});
            argsClassMeta.setComments(new String[]{"合并方法参数生成的类"});
        } else {
            boolean hasComplex = false, hasSimple = false;
            for (Class[] classes : effectiveParams.values()) {
                Class<?> clazz = classes[0];
                if (JavaTypeUtil.isNoneJdkComplex(clazz)) {
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
        //加入依赖列表
        Map<String, C> dependencies = (Map<String, C>) codeGenClassMeta.getDependencies();


        dependencies.put(argsClassMeta.getName(), argsClassMeta);

        LinkedHashMap<String, CommonCodeGenClassMeta> params = new LinkedHashMap<>();
        codeGenClassMeta.setDependencies(dependencies);
        //请求参数名称，固定为req
        params.put("req", argsClassMeta);
        genMethodMeta.setParams(params);

        //增强处理
        this.enhancedProcessingMethod(genMethodMeta, javaMethodMeta, classMeta);

        return genMethodMeta;
    }

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
    protected abstract void enhancedProcessingAnnotation(CommonCodeGenAnnotation codeGenAnnotation, AnnotationMate annotation, Object annotationOwner);


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

}
