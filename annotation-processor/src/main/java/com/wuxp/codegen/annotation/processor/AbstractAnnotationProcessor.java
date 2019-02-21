package com.wuxp.codegen.annotation.processor;


import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Optional;


/**
 * 抽象的注解处理器
 *
 * @param <A>
 * @param <T>
 */
@Slf4j
public abstract class AbstractAnnotationProcessor<A extends Annotation, T extends AnnotationMate<A>>
        implements AnnotationProcessor<T, A> {


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

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(clazz);
        enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE);
        enhancer.setUseFactory(true);
        enhancer.setUseCache(true);
        enhancer.setCallback(new ProxyAnnotationMethodInterceptor(annotation));

        return (T) enhancer.create();

    }


    private class ProxyAnnotationMethodInterceptor implements MethodInterceptor {

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
                log.info("注解：{}，默认方法：{}的执行结果：{}",
                        annotation.annotationType().getSimpleName(),
                        method.getName(),
                        result);
            }
            return result;
        }
    }
}
