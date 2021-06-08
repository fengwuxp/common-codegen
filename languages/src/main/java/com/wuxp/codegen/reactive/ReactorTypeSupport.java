package com.wuxp.codegen.reactive;

import com.wuxp.codegen.core.util.ClassLoaderUtils;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import org.reactivestreams.Publisher;

import java.util.Arrays;
import java.util.Map;

/**
 * 支持处理 {@link reactor.core.publisher.Flux} {@link reactor.core.publisher.Mono} 类型
 *
 * <p>
 * 1：自动探测是否支持对响应式编程的处理
 * 2：移除参数中的 reactor 类型
 * </p>
 *
 * @author wuxp
 * @see org.reactivestreams.Publisher
 */
public final class ReactorTypeSupport {

    private static final Boolean CODEGEN_SUPPORT_REACTIVE;

    static {
        boolean support;
        try {
            ClassLoaderUtils.loadClass("org.springframework.web.reactive.HandlerMapping");
            support = true;
        } catch (ClassNotFoundException ignore) {
            support = "true".equals(System.getProperty("codegen.support.reactive"));
        }
        CODEGEN_SUPPORT_REACTIVE = support;
    }

    private ReactorTypeSupport() {
    }

    public static void handle(JavaClassMeta javaClassMeta) {
        if (!codegenIsSupportReactive()) {
            return;
        }
        if (javaClassMeta == null) {
            return;
        }
        JavaMethodMeta[] methodMetas = javaClassMeta.getMethodMetas();
        if (methodMetas == null) {
            return;
        }
        Arrays.asList(methodMetas).forEach(ReactorTypeSupport::handleParams);
    }

    public static boolean codegenIsSupportReactive() {
        return Boolean.TRUE.equals(CODEGEN_SUPPORT_REACTIVE);
    }

    private static void handleParams(JavaMethodMeta javaMethodMeta) {
        Map<String, Class<?>[]> params = javaMethodMeta.getParams();
        params.forEach(((key, val) -> {
            if (val == null || val.length == 0) {
                return;
            }
            boolean isPublisherObject = JavaTypeUtils.isAssignableFrom(val[0], Publisher.class);
            if (isPublisherObject) {
                // 如果参数中存在 被reactor类型标记的参数，移除reactor的类型
                params.put(key, Arrays.copyOfRange(val, 1, val.length));
            }
        }));
    }
}
