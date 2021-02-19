package com.wuxp.codegen.server.config;

import com.wuxp.codegen.server.enums.SourcecodeRepositoryType;
import com.wuxp.codegen.server.vcs.JGitSourcecodeRepository;
import com.wuxp.codegen.server.vcs.ScmAccessorPropertiesProvider;
import com.wuxp.codegen.server.vcs.SourcecodeRepository;
import com.wuxp.codegen.server.vcs.SvnKitSourcecodeRepository;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author wuxp
 */
@Configuration
public class SourcecodeRepositoryConfig {

    public static final String REPOSITORY_PREFIX = "loong.codegen";

    static final String REPOSITORY_HEADER_NAME = "X-Repository-Code";

    private final ScmAccessorPropertiesProvider scmAccessorPropertiesProvider;

    public SourcecodeRepositoryConfig(ScmAccessorPropertiesProvider scmAccessorPropertiesProvider) {
        this.scmAccessorPropertiesProvider = scmAccessorPropertiesProvider;
    }

    @RequestScope
    @Bean
    public SourcecodeRepository sourcecodeRepository() {
        SourcecodeRepositoryProperties properties = getCurrentSourcecodeRepositoryProperties();
        Assert.notNull(properties, "not found current request properties");
        if (SourcecodeRepositoryType.GIT.equals(properties.getType())) {
            return new JGitSourcecodeRepository(properties);
        }
        return new SvnKitSourcecodeRepository(properties);
    }

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager();
    }


    private SourcecodeRepositoryProperties getCurrentSourcecodeRepositoryProperties() {
        HttpServletRequest request = getHttpServletRequest();
        List<SourcecodeRepositoryProperties> repositories = scmAccessorPropertiesProvider.getRepositoryProperties();
        if (request == null) {
            return repositories.get(0);
        }
        String repositoryCode = request.getHeader(REPOSITORY_HEADER_NAME);
        if (repositoryCode == null) {
            return repositories.get(0);
        }
        return repositories.stream()
                .filter(properties -> repositoryCode.equals(properties.getCode()))
                .findFirst()
                .orElse(null);
    }

    private HttpServletRequest getHttpServletRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();

        if (requestAttributes == null) {
            return null;
        }
        return ((ServletRequestAttributes) requestAttributes).getRequest();
    }

}
