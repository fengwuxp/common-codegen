package com.wuxp.codegen.server.scope;

import com.wuxp.codegen.server.task.CodegenTaskException;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 用于从线程上下文或者{@link RequestAttributes}请求上下文中获取 scm_code
 *
 * @author wuxp
 */
public final class CodegenTaskContextHolder {

    /**
     * 仓库代码标识请求头
     */
    private static final String REPOSITORY_HEADER_NAME = "Loong-Codegen-Repository-Name";

    /**
     * 源代码仓库 code
     */
    private static final ThreadLocal<String> SOURCE_CODE_REPOSITORY_NAME_HOLDER = new ThreadLocal<>();

    private CodegenTaskContextHolder() {
        throw new AssertionError();
    }

    public static void setSourceCodeRepositoryName(String name) {
        SOURCE_CODE_REPOSITORY_NAME_HOLDER.set(name);
    }

    public static String getSourceCodeRepositoryName() {
        String code = SOURCE_CODE_REPOSITORY_NAME_HOLDER.get();
        if (code == null) {
            // 如果不存在在尝试从请求头中获取
            HttpServletRequest httpServletRequest = getHttpServletRequest();
            setSourceCodeRepositoryName(httpServletRequest.getHeader(REPOSITORY_HEADER_NAME));
            code = SOURCE_CODE_REPOSITORY_NAME_HOLDER.get();
        }
        if (code == null) {
            throw new CodegenTaskException("获取 scm code 失败");
        }
        return code;
    }

    public static void removeSourceCodeRepositoryName() {
        SOURCE_CODE_REPOSITORY_NAME_HOLDER.remove();
    }

    private static HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        Assert.notNull(requestAttributes, "获取 RequestAttributes 失败");
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

}
