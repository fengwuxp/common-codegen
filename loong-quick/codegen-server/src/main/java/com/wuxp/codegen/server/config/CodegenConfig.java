package com.wuxp.codegen.server.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.wuxp.codegen.server.CodegenJpaAuditorAware;
import com.wuxp.codegen.server.enums.SourcecodeRepositoryType;
import com.wuxp.codegen.server.plugins.CodegenPluginExecuteStrategy;
import com.wuxp.codegen.server.plugins.MavenCodegenPluginExecuteStrategy;
import com.wuxp.codegen.server.repositories.ScmInfoRepository;
import com.wuxp.codegen.server.scope.CodegenTaskContextHolder;
import com.wuxp.codegen.server.task.CodegenTaskProvider;
import com.wuxp.codegen.server.task.ScmCodegenTaskProvider;
import com.wuxp.codegen.server.vcs.JGitSourcecodeRepository;
import com.wuxp.codegen.server.vcs.ScmAccessorPropertiesProvider;
import com.wuxp.codegen.server.vcs.SourcecodeRepository;
import com.wuxp.codegen.server.vcs.SvnKitSourcecodeRepository;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.support.SimpleThreadScope;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;
import org.springframework.util.ConcurrentReferenceHashMap;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wuxp
 */
@Configuration
@EnableCaching
public class CodegenConfig implements DisposableBean {

    public static final String REPOSITORY_PREFIX = "loong.codegen";

    private static final String THREAD_SCOPE_BEAN_NAME = "thread";

    private final Map<String, SourcecodeRepository> sourcecodeRepositoryCaches = new ConcurrentReferenceHashMap<>(8);

    private ScmAccessorPropertiesProvider scmAccessorPropertiesProvider;

    @Bean
    public ScmAccessorPropertiesProvider scmAccessorPropertiesProvider(ScmInfoRepository scmInfoRepository, SourcecodeRepositoryPropertiesConfig repositoryPropertiesConfig) {
        this.scmAccessorPropertiesProvider = new ScmAccessorPropertiesProvider(scmInfoRepository, repositoryPropertiesConfig);
        return scmAccessorPropertiesProvider;
    }


    /**
     * 注册自定义thread scope bean
     *
     * @return
     */
    @Bean
    public CustomScopeConfigurer threadScope() {
        CustomScopeConfigurer customScopeConfigurer = new CustomScopeConfigurer();
        customScopeConfigurer.addScope(THREAD_SCOPE_BEAN_NAME, new SimpleThreadScope());
        return customScopeConfigurer;
    }

    @Scope(value = THREAD_SCOPE_BEAN_NAME, proxyMode = ScopedProxyMode.TARGET_CLASS)
    @Bean
    public SourcecodeRepository sourcecodeRepository() {
        SourcecodeRepositoryProperties properties = getCurrentSourcecodeRepositoryProperties();
        Assert.notNull(properties, "not found current request properties");
        return sourcecodeRepositoryCaches.computeIfAbsent(properties.getCode(), key -> {
            if (SourcecodeRepositoryType.GIT.equals(properties.getType())) {
                return new JGitSourcecodeRepository(properties);
            }
            return new SvnKitSourcecodeRepository(properties);
        });
    }

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
        caffeineCacheManager.setCaffeine(Caffeine.newBuilder()
                // 设置最后一次写入或访问后经过固定时间过期
                .expireAfterAccess(5, TimeUnit.MINUTES)
                // 初始的缓存空间大小
                .initialCapacity(128)
                // 缓存的最大条数
                .maximumSize(1024));
        return caffeineCacheManager;
    }

    /**
     * 用于执行代码生成任务的执行器
     *
     * @return ThreadPoolTaskExecutor
     */
    @Bean(name = "codegenTaskExecutor")
    public ThreadPoolTaskExecutor codegenTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(2);
        threadPoolTaskExecutor.setMaxPoolSize(4);
        threadPoolTaskExecutor.setKeepAliveSeconds(60);
        threadPoolTaskExecutor.setThreadNamePrefix("codegen-task");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.setTaskDecorator(runnable -> {
            // 在线程发生切换的时候，在2个线程间传递scmCode
            Optional<String> optional = CodegenTaskContextHolder.getScmCode();
            return () -> {
                optional.ifPresent(CodegenTaskContextHolder::setScmCode);
                try {
                    runnable.run();
                } finally {
                    CodegenTaskContextHolder.removeScmCode();
                }
            };
        });
        return threadPoolTaskExecutor;
    }

    @Bean
    public CodegenTaskProvider codegenTaskProvider(SourcecodeRepository sourcecodeRepository,
                                                   CodegenPluginExecuteStrategy codegenPluginExecuteStrategy,
                                                   @Qualifier(value = "codegenTaskExecutor") AsyncTaskExecutor taskExecutor) {
        return new ScmCodegenTaskProvider(sourcecodeRepository, codegenPluginExecuteStrategy, taskExecutor);
    }

    @Bean
    public CodegenPluginExecuteStrategy codegenPluginExecuteStrategy() {
        return new MavenCodegenPluginExecuteStrategy();
    }

    @Bean
    public AuditorAware<String> codegenJpaAuditorAware() {
        return new CodegenJpaAuditorAware();
    }

    @Override
    public void destroy() throws Exception {
        sourcecodeRepositoryCaches.clear();
    }

    private SourcecodeRepositoryProperties getCurrentSourcecodeRepositoryProperties() {
        Optional<String> optional = CodegenTaskContextHolder.getScmCode();
        List<SourcecodeRepositoryProperties> repositories = scmAccessorPropertiesProvider.getRepositoryProperties();
        if (!optional.isPresent()) {
            return repositories.get(0);
        }
        String repositoryCode = optional.get();
        return repositories.stream()
                .filter(properties -> repositoryCode.equals(properties.getCode()))
                .findFirst()
                .orElse(null);
    }


}
