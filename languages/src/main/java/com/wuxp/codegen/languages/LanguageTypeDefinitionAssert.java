package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.exception.CodegenRuntimeException;
import com.wuxp.codegen.meta.annotations.factories.spring.RequestMappingMetaFactory;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;

/**
 * 语言类型定义断言工具
 *
 * @author wuxp
 */
public final class LanguageTypeDefinitionAssert {

    private LanguageTypeDefinitionAssert() {
        throw new AssertionError();
    }

    public static void noneMethodOverload(Collection<JavaMethodMeta> methodMetas) {
        // 判断是否存在方法名称是否相同
        for (JavaMethodMeta codegenMethod : methodMetas) {
            Optional<JavaMethodMeta> overloadMethod = findOverloadMethod(methodMetas, codegenMethod);
            overloadMethod.ifPresent(javaMethodMeta -> {
                String className = javaMethodMeta.getMethod().getDeclaringClass().getName();
                // 不允许方法重载
                throw new CodegenRuntimeException(MessageFormat.format("类{0}下的的方法{1}出现重载", className, codegenMethod.getName()));
            });
        }
    }

    /**
     * 检查spring mvc 控制器的方法
     *
     * @param javaMethodMeta java方法元数据描述
     */
    public static void isValidSpringWebMethod(final JavaMethodMeta javaMethodMeta) {
        Class<?> declaringClass = getDeclaringClass(javaMethodMeta);
        if (!AnnotationUtils.isCandidateClass(declaringClass, Arrays.asList(Controller.class, RestController.class))) {
            return;
        }

        // 检查控制器方法是否合法
        Assert.isTrue(javaMethodMeta.getAccessPermission().isPublic(), String.format("%s的方法，%s不是公有方法", declaringClass.getName(), javaMethodMeta.getName()));
        Assert.isTrue(!javaMethodMeta.getIsStatic(), String.format("%s的方法，%s是静态的", declaringClass.getName(), javaMethodMeta.getName()));

        RequestMappingMetaFactory.RequestMappingMate requestMappingMate = findRequestMappingMate(javaMethodMeta);
        if (isUseRequestBody(javaMethodMeta, requestMappingMate.consumes())) {
            RequestMethod requestMethod = requestMappingMate.getRequestMethod();
            Assert.isTrue(RequestMappingMetaFactory.isSupportRequestBody(requestMethod),
                    String.format("%s的方法，%s，请求方法%s，不支持RequestBody", declaringClass.getName(), javaMethodMeta.getName(), requestMethod.name()));
        }
    }

    private static boolean isUseRequestBody(JavaMethodMeta javaMethodMeta, String[] consumes) {
        if (ObjectUtils.isEmpty(consumes)) {
            return javaMethodMeta.getParamAnnotations()
                    .values()
                    .stream()
                    .map(Arrays::asList)
                    .flatMap(Collection::stream)
                    .anyMatch(annotation -> RequestBody.class.equals(annotation.annotationType()));
        } else {
            // TODO consume 类型
            return StringUtils.hasText(consumes[0]);
        }
    }

    private static Optional<JavaMethodMeta> findOverloadMethod(Collection<JavaMethodMeta> methodMetas, JavaMethodMeta codegenMethod) {
        return methodMetas.stream()
                .filter(m1 -> m1.getName().equals(codegenMethod.getName()) && !m1.equals(codegenMethod))
                .findFirst();
    }

    private static Class<?> getDeclaringClass(JavaMethodMeta javaMethodMeta) {
        return javaMethodMeta.getMethod().getDeclaringClass();
    }

    private static RequestMappingMetaFactory.RequestMappingMate findRequestMappingMate(JavaMethodMeta javaMethodMeta) {
        return Arrays.stream(javaMethodMeta.getAnnotations())
                .map(AnnotationMetaFactoryHolder::getAnnotationMeta)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(RequestMappingMetaFactory.RequestMappingMate.class::isInstance)
                .map(RequestMappingMetaFactory.RequestMappingMate.class::cast)
                .findFirst()
                .orElseThrow(() -> new CodegenRuntimeException(String.format("%s的方法，%s RequestMapping 注解不存在", getDeclaringClass(javaMethodMeta).getName(), javaMethodMeta.getName())));
    }

}
