package com.wuxp.codegen.server.scope;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author wuxp
 */
public final class CodegenTaskContextHolder {

    /**
     * 参考代码标识请求头
     */
    private static final String REPOSITORY_HEADER_NAME = "X-Repository-Code";

    private static final ThreadLocal<String> SCM_CODE_HOLDER = new ThreadLocal<>();

    private CodegenTaskContextHolder() {
    }


    public static void setScmCode(String scmCode) {
        SCM_CODE_HOLDER.set(scmCode);
    }

    public static Optional<String> getScmCode() {
        String taskId = SCM_CODE_HOLDER.get();
        if (taskId == null) {
            // 如果不存在在尝试从请求头中获取
            getHttpServletRequest().ifPresent(request -> setScmCode(request.getHeader(REPOSITORY_HEADER_NAME)));
            taskId = SCM_CODE_HOLDER.get();
        }
        return Optional.ofNullable(taskId);
    }

    public static void removeScmCode() {
        SCM_CODE_HOLDER.remove();
    }

    private static Optional<HttpServletRequest> getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return Optional.empty();
        }
        return Optional.of(((ServletRequestAttributes) requestAttributes).getRequest());
    }

}
