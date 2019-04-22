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
import com.wuxp.codegen.model.mapping.CustomizeJavaTypeMapping;
import com.wuxp.codegen.model.mapping.TypeMapping;
import com.wuxp.codegen.model.utils.JavaTypeUtil;
import com.wuxp.codegen.utils.JavaMethodNameUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.wuxp.codegen.model.mapping.AbstractTypeMapping.CUSTOMIZE_JAVA_TYPE_MAPPING;


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
    protected static final Map<Class<? extends Annotation>, AnnotationProcessor> ANNOTATION_PROCESSOR_MAP = new LinkedHashMap<>();


    /**
     * java类的解析器
     * 默认解析所有的属性 方法
     */
    protected GenericParser<JavaClassMeta, Class<?>> javaParser = new JavaClassParser(false);

    /**
     * 自定义的类型映射
     */
    protected TypeMapping<Class<?>, List<Class<?>>> customizeJavaTypeMapping = new CustomizeJavaTypeMapping(CUSTOMIZE_JAVA_TYPE_MAPPING);

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
        codeGenMatchers.add(clazz -> JavaTypeUtil.isNoneJdkComplex(clazz) || clazz.isAnnotation());

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

    public AbstractLanguageParser(PackageMapStrategy packageMapStrategy,
                                  CodeGenMatchingStrategy genMatchingStrategy,
                                  Collection<CodeDetect> codeDetects) {
        this.packageMapStrategy = packageMapStrategy;
        this.genMatchingStrategy = genMatchingStrategy;
        this.codeDetects = codeDetects;
    }

    public AbstractLanguageParser(GenericParser<JavaClassMeta, Class<?>> javaParser,
                                  PackageMapStrategy packageMapStrategy,
                                  CodeGenMatchingStrategy genMatchingStrategy,
                                  Collection<CodeDetect> codeDetects) {
        this(packageMapStrategy, genMatchingStrategy, codeDetects);
        this.javaParser = javaParser;
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
                .map(this.customizeJavaTypeMapping::mapping)
                .flatMap(Collection::stream)
                .filter(this::isMatchGenCodeRule)
                .filter(Objects::nonNull)
                .map(this::parse)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toMap(CommonBaseMeta::getName, v -> v));
    }


}
