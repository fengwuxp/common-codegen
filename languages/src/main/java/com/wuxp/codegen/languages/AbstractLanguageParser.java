package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.CodeGenMatcher;
import com.wuxp.codegen.core.macth.PackageNameCodeGenMatcher;
import com.wuxp.codegen.core.parser.JavaClassParser;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.annotation.processor.AnnotationMate;
import com.wuxp.codegen.annotation.processor.AnnotationProcessor;
import com.wuxp.codegen.annotation.processor.javax.NotNullProcessor;
import com.wuxp.codegen.annotation.processor.javax.PatternProcessor;
import com.wuxp.codegen.annotation.processor.javax.SizeProcessor;
import com.wuxp.codegen.annotation.processor.spring.RequestMappingProcessor;
import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.parser.GenericParser;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.CodeGenMatchingStrategy;
import com.wuxp.codegen.model.*;
import com.wuxp.codegen.model.enums.AccessPermission;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
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
import java.lang.reflect.Method;
import java.lang.reflect.TypeVariable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.wuxp.codegen.model.mapping.AbstractTypeMapping.customizeJavaTypeMapping;
import static com.wuxp.codegen.model.mapping.AbstractTypeMapping.customizeTypeMapping;


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
        codeGenMatchers.add(clazz -> clazz.isEnum() || JavaTypeUtil.isNoneJdkComplex(clazz) || clazz.isAnnotation());

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


    public AbstractLanguageParser(LanguageMetaInstanceFactory languageMetaInstanceFactory,
                                  PackageMapStrategy packageMapStrategy,
                                  CodeGenMatchingStrategy genMatchingStrategy,
                                  Collection<CodeDetect> codeDetects) {
        this.languageMetaInstanceFactory = languageMetaInstanceFactory;
        this.packageMapStrategy = packageMapStrategy;
        this.genMatchingStrategy = genMatchingStrategy;
        this.codeDetects = codeDetects;
    }


    public AbstractLanguageParser(GenericParser<JavaClassMeta, Class<?>> javaParser,
                                  LanguageMetaInstanceFactory languageMetaInstanceFactory,
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
        if (!this.isMatchGenCodeRule(source) ||
                JavaTypeUtil.isMap(source) ||
                JavaTypeUtil.isCollection(source)) {
            return null;
        }

        if (HANDLE_COUNT.containsKey(source)) {
            //标记某个类被处理的次数如果超过2次，从缓存中返回
            if (HANDLE_COUNT.get(source) > 2) {
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
        Class<?> javaClassSuperClass = javaClassMeta.getSuperClass();
        C commonCodeGenClassMeta = this.parse(javaClassSuperClass);
        if (javaClassSuperClass != null && commonCodeGenClassMeta == null) {
            if (log.isDebugEnabled()) {
                log.debug("超类 {} 解析处理失败或被忽略", javaClassMeta.getClassName());
            }
        }
        meta.setSuperClass(commonCodeGenClassMeta);

        //类上的注释
        meta.setComments(this.generateComments(source.getAnnotations(), source).toArray(new String[]{}));
        //类上的注解
        meta.setAnnotations(this.converterAnnotations(source.getAnnotations(), javaClassMeta.getClass()));

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
                    .filter(javaMethodMeta -> Boolean.FALSE.equals(javaMethodMeta.getIsStatic()) && Boolean.FALSE.equals(javaMethodMeta.getIsAbstract()))
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
                .map(javaFieldMeta -> this.converterField(javaFieldMeta, classMeta))
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());

    }

    ;


    /**
     * 转换属性
     *
     * @param javaFieldMeta
     * @param classMeta
     * @return
     */
    protected abstract F converterField(JavaFieldMeta javaFieldMeta, JavaClassMeta classMeta);

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


    protected abstract M converterMethod(JavaMethodMeta javaMethodMetas, JavaClassMeta classMeta, C codeGenClassMeta);

    /**
     * 增强处理 method
     *
     * @param methodMeta
     * @param javaMethodMeta
     * @param classMeta
     */
    protected abstract void enhancedProcessingMethod(M methodMeta, JavaMethodMeta javaMethodMeta, JavaClassMeta classMeta);


    /**
     * 增强处理 annotation
     *
     * @param codeGenAnnotation
     * @param annotation
     * @param annotationOwner
     */
    protected void enhancedProcessingAnnotation(CommonCodeGenAnnotation codeGenAnnotation, AnnotationMate annotation, Object annotationOwner) {
        if (annotationOwner instanceof Class) {

        }
        if (annotationOwner instanceof Method) {
            //spring的mapping注解
            if (annotation.annotationType().getSimpleName().endsWith("Mapping")) {

                Method method = (Method) annotationOwner;
                //判断方法参数是否有RequestBody注解
                List<Annotation> annotationList = Arrays.stream(method.getParameterAnnotations())
                        .filter(Objects::nonNull)
                        .filter(annotations -> annotations.length > 0)
                        .map(Arrays::asList)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
                boolean hasRequestBodyAnnotation = annotationList.toArray().length > 0 && annotationList.stream().allMatch(a -> RequestBody.class.equals(a.annotationType()));

                if (!hasRequestBodyAnnotation) {
                    //如果没有则认为是已表单的方式提交的参数
                    //是spring的Mapping注解
                    String produces = codeGenAnnotation.getNamedArguments().get("produces");
                    if (produces == null) {
                        produces = "[MediaType.FORM_DATA]";
                    }
                    codeGenAnnotation.getNamedArguments().put("produces", produces);
                }
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


        return dependencies.stream()
                .map(customizeJavaTypeMapping::mapping)
                .flatMap(Collection::stream)
                .filter(this::isMatchGenCodeRule)
                .filter(Objects::nonNull)
                .map(this::parse)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toMap(CommonBaseMeta::getName, v -> v));
    }

}
