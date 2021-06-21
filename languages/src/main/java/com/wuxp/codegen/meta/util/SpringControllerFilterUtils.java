package com.wuxp.codegen.meta.util;

import com.wuxp.codegen.meta.annotations.factories.spring.RequestMappingMetaFactory;
import com.wuxp.codegen.core.parser.JavaClassParser;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.wuxp.codegen.core.parser.JavaClassParser.JAVA_CLASS_ON_PUBLIC_PARSER;
import static com.wuxp.codegen.model.constant.SpringAnnotationClassConstant.SPRING_CONTROLLER_ANNOTATIONS;
import static com.wuxp.codegen.model.constant.SpringAnnotationClassConstant.SPRING_MAPPING_ANNOTATIONS;

/**
 * 过滤spring controller 的方法或属性
 *
 * @author wuxp
 */
@Slf4j
public final class SpringControllerFilterUtils {


    private static final JavaClassParser JAVA_PARSER = JAVA_CLASS_ON_PUBLIC_PARSER;

    private SpringControllerFilterUtils() {
    }

    /**
     * 过滤方法
     *
     * @param classMeta 类元数据
     */
    public static void filterMethods(JavaClassMeta classMeta) {
        //判断是否为spring的控制器
        Class<?> clazz = classMeta.getClazz();
        boolean isSpringController = Arrays.stream(SPRING_CONTROLLER_ANNOTATIONS)
                .map(aClass -> clazz.getAnnotation(aClass) != null)
                .filter(b -> b).findFirst()
                .orElse(false);
        if (!isSpringController) {
            return;
        }

        //抓取超类的方法
        List<JavaMethodMeta> methodMetas = Arrays.stream(classMeta.getMethodMetas()).collect(Collectors.toList());
        Class<?> superclass = clazz.getSuperclass();
        boolean isNoneSupper = superclass == null || Object.class.equals(superclass);
        if (!isNoneSupper) {
            JavaMethodMeta[] javaMethodMetas = JAVA_PARSER.parse(superclass).getMethodMetas();
            methodMetas.addAll(Arrays.asList(javaMethodMetas));
        }

        classMeta.setMethodMetas(getStringJavaMethodMetaMap(methodMetas)
                .stream()
                //过滤掉非mapping的方法
                .filter(javaMethodMeta -> findSpringControllerMappingAnnotation(javaMethodMeta).isPresent())
                .toArray(JavaMethodMeta[]::new));
    }

    /**
     * 过滤掉控制器中请求uri和请求方法相同的方法
     *
     * @param methodMetas java 方法元数据列表
     * @return 方法元数据
     */
    private static List<JavaMethodMeta> getStringJavaMethodMetaMap(List<JavaMethodMeta> methodMetas) {
        Map<String/*请求方法加上方法名称或RequestMapping的value*/, JavaMethodMeta> javaMethodMetaMap = new LinkedHashMap<>();

        //过滤路径相同的方法
        methodMetas.stream()
                .filter(Objects::nonNull)
                .forEach(javaMethodMeta -> {
                    Optional<RequestMappingMetaFactory.RequestMappingMate> requestMappingAnnotation = RequestMappingUtils.findRequestMappingAnnotation(javaMethodMeta.getAnnotations());
                    if (!requestMappingAnnotation.isPresent()) {
                        return;
                    }
                    RequestMappingMetaFactory.RequestMappingMate requestMappingMate = requestMappingAnnotation.get();
                    String[] value = requestMappingMate.value();
                    RequestMethod[] methods = requestMappingMate.method();
                    if (methods.length == 0) {
                        methods = new RequestMethod[]{RequestMethod.GET, RequestMethod.POST};
                    }
                    String name = value.length == 0 ? javaMethodMeta.getName() : value[0];
                    name = StringUtils.hasText(name) ? name : javaMethodMeta.getName();
                    javaMethodMetaMap.put(MessageFormat.format("{0}@{1}",
                            Arrays.stream(methods)
                                    .map(Enum::name)
                                    .collect(Collectors.joining("@")), name),
                            javaMethodMeta);
                });
        if (javaMethodMetaMap.size() > 0) {
            return new ArrayList<>(javaMethodMetaMap.values());
        }
        return methodMetas;
    }

    /**
     * 查找 spring controller mapping注解
     *
     * @param javaMethodMeta java 方法元数据
     * @return 注解
     */
    private static Optional<? extends Annotation> findSpringControllerMappingAnnotation(JavaMethodMeta javaMethodMeta) {
        return Arrays.stream(SPRING_MAPPING_ANNOTATIONS)
                .map(javaMethodMeta::getAnnotation)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .findFirst();
    }
}
