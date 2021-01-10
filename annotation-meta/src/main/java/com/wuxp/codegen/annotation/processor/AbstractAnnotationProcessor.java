package com.wuxp.codegen.annotation.processor;


import com.wuxp.codegen.annotation.ClientAnnotationProvider;
import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.config.CodegenConfigHolder;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.transform.AnnotationCodeGenTransformer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 抽象的注解处理器
 *
 * @param <A>
 * @param <T>
 * @author wuxp
 */
@Slf4j
public abstract class AbstractAnnotationProcessor<A extends Annotation, T extends AnnotationMate> implements AnnotationProcessor<T, A> {


    private static final Map<ClientProviderType, ClientAnnotationProvider> ANNOTATION_PROVIDERS = new ConcurrentHashMap<>(8);

    /**
     * client provider type和AnnotationCodeGenTransformer的对应关系
     *
     * @key ClientProviderType
     * @value {
     * key: 注解类型
     * value: AnnotationCodeGenTransformer
     * }
     */
    private static final Map<ClientProviderType, Map<Class<? extends Annotation>, AnnotationCodeGenTransformer<CommonCodeGenAnnotation, AnnotationMate>>>
            CLIENT_PROVIDER_TYPE_ANNOTATION_TRANSFORMERS = new HashMap<>(32);


    /**
     * 返回一个代理的元数据对象
     *
     * @param annotation
     * @param clazz
     * @return
     */
    protected T newProxyMate(Annotation annotation, Class<? extends AnnotationMate> clazz) {
        if (annotation == null) {
            return null;
        }
        clazz = tryGetAnnotationType(annotation, clazz);
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
        enhancer.setUseFactory(true);
        enhancer.setUseCache(true);
        enhancer.setCallback(new ProxyAnnotationMethodInterceptor(annotation));

        Constructor<?>[] constructors = clazz.getConstructors();

        boolean hasEmptyConstructor = Arrays.stream(constructors).anyMatch(constructor -> constructor.getParameters().length == 0);

        if (hasEmptyConstructor) {
            return (T) enhancer.create();
        }
        return (T) enhancer.create(new Class[]{annotation.annotationType()}, new Object[]{annotation});

    }

    private Class<? extends AnnotationMate> tryGetAnnotationType(Annotation annotation, Class<? extends AnnotationMate> clazz) {
        ClientProviderType clientProviderType = CodegenConfigHolder.getConfig().getProviderType();
        if (clientProviderType == null) {
            return clazz;
        }
        ClientAnnotationProvider clientAnnotationProvider = ANNOTATION_PROVIDERS.get(clientProviderType);
        if (clientAnnotationProvider == null) {
            return clazz;
        }
        Class<? extends AnnotationMate> annotationType = clientAnnotationProvider.getAnnotation(annotation.annotationType());
        if (annotationType == null) {
            if (log.isInfoEnabled()) {
                log.info("client provider type={},annotation type={} must not null", clientProviderType.name(), annotation.annotationType());
            }
            return clazz;
        } else {
            return annotationType;
        }
    }


    public static void registerAnnotationProvider(ClientProviderType type, ClientAnnotationProvider annotationProvider) {
        ANNOTATION_PROVIDERS.put(type, annotationProvider);
    }

    public static void registerAnnotationTransformer(ClientProviderType type, Class<? extends Annotation> annotationType, AnnotationCodeGenTransformer transformer) {
        Map<Class<? extends Annotation>, AnnotationCodeGenTransformer<CommonCodeGenAnnotation, AnnotationMate>> transformerMap
                = CLIENT_PROVIDER_TYPE_ANNOTATION_TRANSFORMERS.computeIfAbsent(type, (key) -> new HashMap<>(8));
        transformerMap.put(annotationType, transformer);
    }


    public static <A extends AnnotationMate> AnnotationCodeGenTransformer<CommonCodeGenAnnotation, A> getAnnotationTransformer(ClientProviderType type, Class<? extends Annotation> annotationType) {
        Map<Class<? extends Annotation>, AnnotationCodeGenTransformer<CommonCodeGenAnnotation, AnnotationMate>> transformerMap = CLIENT_PROVIDER_TYPE_ANNOTATION_TRANSFORMERS.get(type);
        if (transformerMap == null) {
            return null;
        }
        return (AnnotationCodeGenTransformer<CommonCodeGenAnnotation, A>) transformerMap.get(annotationType);
    }


    private static class ProxyAnnotationMethodInterceptor implements MethodInterceptor {

        /**
         * 注解实例
         */
        private final Annotation annotation;

        /**
         * 注解的方法列表
         */
        private final Method[] annotationMethods;


        private ProxyAnnotationMethodInterceptor(Annotation annotation) {
            this.annotation = annotation;
            this.annotationMethods = annotation.getClass().getMethods();
        }

        @Override
        public Object intercept(Object annotationMate, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
            //查找方法是否在注解

            Optional<Method> optionalMethod = Arrays.stream(annotationMethods)
                    .filter(m -> {
                        boolean returnTypeIsEq = method.getReturnType().equals(m.getReturnType());
                        boolean methodNameIsEq = method.getName().equals(m.getName());
                        boolean parameterLenIsEq = m.getParameters().length == method.getParameters().length;
                        return returnTypeIsEq && parameterLenIsEq && methodNameIsEq;
                    })
                    .findFirst();
            if (optionalMethod.isPresent()) {
                //注解中的方法
                return optionalMethod.get().invoke(annotation, args);
            }

            int modifiers = method.getModifiers();
            if (Modifier.isAbstract(modifiers)) {
                //抽象方法
                return null;
            }

            Object result = methodProxy.invokeSuper(annotationMate, args);
            if (modifiers == 1) {
                if (log.isInfoEnabled()) {
                    log.info("注解：{}，默认方法：{}的执行结果：{}",
                            annotation.annotationType().getSimpleName(),
                            method.getName(),
                            result);
                }
            }
            return result;
        }
    }
}
