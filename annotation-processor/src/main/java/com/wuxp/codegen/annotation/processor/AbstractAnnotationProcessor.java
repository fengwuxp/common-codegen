package com.wuxp.codegen.annotation.processor;


import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;


/**
 * 抽象的注解处理器
 *
 * @param <A>
 * @param <T>
 */
public abstract class AbstractAnnotationProcessor<A extends Annotation, T extends AnnotationMate<A>>
        implements AnnotationProcessor<T, A> {


    /**
     * 返回一个代理的元数据对象
     *
     * @param annotation
     * @param clazz
     * @return
     */
    protected T newProxyMate(Annotation annotation, Class<?> clazz) {
        if (annotation == null) {
            return null;
        }

        final Method[] methods = annotation.getClass().getMethods();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
        enhancer.setCallback((MethodInterceptor) (o, method, args, methodProxy) -> {
            Optional<Method> optionalMethod = Arrays.stream(methods).filter(m -> method.getName().equals(m.getName())).findFirst();

            if (optionalMethod.isPresent()) {
                //返回注解中的内容
                return method.invoke(annotation, args);
            }

            return methodProxy.invokeSuper(o, args);
        });

        return (T) enhancer.create();

    }
}
