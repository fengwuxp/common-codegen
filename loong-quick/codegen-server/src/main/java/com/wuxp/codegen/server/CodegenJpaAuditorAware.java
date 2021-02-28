package com.wuxp.codegen.server;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

/**
 * @author wuxp
 */
@Slf4j
public class CodegenJpaAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        try {
            UserDetails user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return Optional.ofNullable(user.getUsername());
        } catch (Exception exception) {
            log.warn("获取当前登录用户失败：{}", exception.getMessage(), exception);
            return Optional.empty();
        }
    }
}
