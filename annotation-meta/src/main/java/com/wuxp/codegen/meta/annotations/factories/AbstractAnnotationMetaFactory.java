package com.wuxp.codegen.meta.annotations.factories;


import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.config.CodegenConfigHolder;
import com.wuxp.codegen.meta.annotations.ClientAnnotationProvider;
import com.wuxp.codegen.meta.transform.AnnotationCodeGenTransformer;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 抽象的注解处理器
 *
 * @param <A>
 * @param <T>
 * @author wuxp
 */
@Slf4j
public abstract class AbstractAnnotationMetaFactory<A extends Annotation, T extends AnnotationMate> implements AnnotationMetaFactory<T, A> {


    private static final Map<ClientProviderType, ClientAnnotationProvider> ANNOTATION_PROVIDERS = new ConcurrentHashMap<>(8);

    /**
     * {@link ClientProviderType 和 {@link AnnotationCodeGenTransformer} 的对应关系
     *
     * @key ClientProviderType
     * @value {
     * key: 注解类型
     * value: AnnotationCodeGenTransformer
     * }
     */
    private static final Map<ClientProviderType, Map<Class<? extends Annotation>, AnnotationCodeGenTransformer<? extends CommonCodeGenAnnotation, ? extends AnnotationMate>>>
            CLIENT_PROVIDER_TYPE_ANNOTATION_TRANSFORMERS = new EnumMap<>(ClientProviderType.class);


    /**
     * 返回一个代理的元数据对象
     *
     * @param annotation 注解实例对象
     * @param clazz      注解实例的元数据类型
     * @return 代理的实例对象
     */
    @SuppressWarnings("unchecked")
    protected T newProxyMate(Annotation annotation, Class<? extends AnnotationMate> clazz) {
        if (annotation == null) {
            throw new NullPointerException("annotation must not null");
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

    public static void registerAnnotationTransformer(ClientProviderType type, Class<? extends Annotation> annotationType, AnnotationCodeGenTransformer<? extends CommonCodeGenAnnotation, ? extends AnnotationMate> transformer) {
        Map<Class<? extends Annotation>, AnnotationCodeGenTransformer<? extends CommonCodeGenAnnotation, ? extends AnnotationMate>> transformerMap
                = CLIENT_PROVIDER_TYPE_ANNOTATION_TRANSFORMERS.computeIfAbsent(type, key -> new HashMap<>(8));
        transformerMap.put(annotationType, transformer);
    }


    @SuppressWarnings("unchecked")
    public static <T extends CommonCodeGenAnnotation, A extends AnnotationMate> Optional<AnnotationCodeGenTransformer<T, A>> getAnnotationTransformer(ClientProviderType type, Class<? extends Annotation> annotationType) {
        Map<Class<? extends Annotation>, AnnotationCodeGenTransformer<? extends CommonCodeGenAnnotation, ? extends AnnotationMate>> transformers = CLIENT_PROVIDER_TYPE_ANNOTATION_TRANSFORMERS.get(type);
        return Optional.ofNullable((AnnotationCodeGenTransformer<T, A>) transformers.get(annotationType) );
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
            Optional<Method> optionalMethod = matchMethodByAnnotations(method);
            if (optionalMethod.isPresent()) {
                // 注解中的方法
                return optionalMethod.get().invoke(annotation, args);
            }

            int modifiers = method.getModifiers();
            if (Modifier.isAbstract(modifiers)) {
                //抽象方法
                return null;
            }

            Object result = methodProxy.invokeSuper(annotationMate, args);
            boolean isDefaultMethod = modifiers == 1;
            // 默认方法
            if (isDefaultMethod && log.isInfoEnabled()) {
                log.info("注解：{}，默认方法：{}的执行结果：{}",
                        annotation.annotationType().getSimpleName(),
                        method.getName(),
                        result);
            }
            return result;
        }

        private Optional<Method> matchMethodByAnnotations(Method method) {
            return Arrays.stream(annotationMethods)
                    .filter(m -> method.getReturnType().equals(m.getReturnType()))
                    .filter(m -> method.getName().equals(m.getName()))
                    .filter(m -> m.getParameters().length == method.getParameters().length)
                    .findFirst();
        }
    }
}
