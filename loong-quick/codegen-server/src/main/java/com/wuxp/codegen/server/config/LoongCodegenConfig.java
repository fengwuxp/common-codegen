package com.wuxp.codegen.server.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.wuxp.codegen.server.CodegenJpaAuditorAware;
import com.wuxp.codegen.server.codegen.SdkCodeManager;
import com.wuxp.codegen.server.codegen.TemporaryFileSdkCodeManager;
import com.wuxp.codegen.server.enums.SourcecodeRepositoryType;
import com.wuxp.codegen.server.plugins.CodegenPluginExecuteStrategy;
import com.wuxp.codegen.server.plugins.MavenCodegenPluginExecuteStrategy;
import com.wuxp.codegen.server.repositories.CodeVersionControlConfigRepository;
import com.wuxp.codegen.server.scope.CodegenTaskContextHolder;
import com.wuxp.codegen.server.task.CodegenTaskException;
import com.wuxp.codegen.server.task.CodegenTaskService;
import com.wuxp.codegen.server.task.SourceCodeManagerTaskService;
import com.wuxp.codegen.server.vcs.*;
import org.apache.logging.log4j.util.Supplier;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wuxp
 */
@Configuration
@EnableCaching
public class LoongCodegenConfig implements DisposableBean {

    public static final String REPOSITORY_PREFIX = "loong.codegen";

    private static final String THREAD_SCOPE_BEAN_NAME = "thread";

    private static final String CODEGEN_TASK_EXECUTOR_BEAN_NAME = "codegenTaskExecutor";

    private final Map<String, SourcecodeRepository> sourcecodeRepositoryCaches = new ConcurrentReferenceHashMap<>(8);

    @Bean
    public SourceCodeRepositoryAccessPropertiesSupplier scmAccessorPropertiesProvider(CodeVersionControlConfigRepository codeVersionControlConfigRepository, LoongCodegenProperties codegenProperties) {
        return new SourceCodeRepositoryAccessPropertiesSupplier(codeVersionControlConfigRepository, codegenProperties.getRepositories());
    }

    /**
     * 注册自定义thread scope bean
     *
     * @return
     */
    @Bean
    public CustomScopeConfigurer customThreadScope() {
        CustomScopeConfigurer customScopeConfigurer = new CustomScopeConfigurer();
        customScopeConfigurer.addScope(THREAD_SCOPE_BEAN_NAME, new SimpleThreadScope());
        return customScopeConfigurer;
    }

    @Scope(value = THREAD_SCOPE_BEAN_NAME, proxyMode = ScopedProxyMode.TARGET_CLASS)
    @Bean
    public SourcecodeRepository sourcecodeRepository(Supplier<List<SourceCodeRepositoryAccessProperties>> supplier) {
        SourceCodeRepositoryAccessProperties properties = getCurrentSourceCodeRepositoryAccessProperties(supplier);
        Assert.notNull(properties, "not found current request properties");
        return sourcecodeRepositoryCaches.computeIfAbsent(properties.getName(), key -> {
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
    @Bean(name = CODEGEN_TASK_EXECUTOR_BEAN_NAME)
    public ThreadPoolTaskExecutor codegenTaskExecutor() {
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(2);
        threadPoolTaskExecutor.setMaxPoolSize(4);
        threadPoolTaskExecutor.setKeepAliveSeconds(60);
        threadPoolTaskExecutor.setThreadNamePrefix("codegen-task");
        threadPoolTaskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        threadPoolTaskExecutor.setTaskDecorator(runnable -> {
            // 在线程发生切换的时候，在2个线程间传递scmCode
            String code = CodegenTaskContextHolder.getSourceCodeRepositoryName();
            return () -> {
                CodegenTaskContextHolder.setSourceCodeRepositoryName(code);
                try {
                    runnable.run();
                } finally {
                    CodegenTaskContextHolder.removeSourceCodeRepositoryName();
                }
            };
        });
        return threadPoolTaskExecutor;
    }

    @Bean
    public CodegenTaskService codegenTaskService(SourcecodeRepository sourcecodeRepository,
                                                 CodegenPluginExecuteStrategy codegenPluginExecuteStrategy,
                                                 @Qualifier(value = CODEGEN_TASK_EXECUTOR_BEAN_NAME) AsyncTaskExecutor taskExecutor) {
        return new SourceCodeManagerTaskService(sourcecodeRepository, codegenPluginExecuteStrategy, taskExecutor);
    }

    @Bean
    public CodegenPluginExecuteStrategy codegenPluginExecuteStrategy() {
        return new MavenCodegenPluginExecuteStrategy();
    }

    @Bean
    public SdkCodeManager zipCodegenFileManageStrategy(SourcecodeRepository sourcecodeRepository,
                                                       CodegenPluginExecuteStrategy codegenPluginExecuteStrategy,
                                                       @Value("${loong.codegen.sdk.tempdir:${java.io.tmpdir}codegen/sdk/temp}") String uploadTempDir) {
        return new TemporaryFileSdkCodeManager(sourcecodeRepository, codegenPluginExecuteStrategy, uploadTempDir);
    }

    @Bean
    public AuditorAware<String> codegenJpaAuditorAware() {
        return new CodegenJpaAuditorAware();
    }

    @Override
    public void destroy() {
        sourcecodeRepositoryCaches.clear();
    }

    private SourceCodeRepositoryAccessProperties getCurrentSourceCodeRepositoryAccessProperties(Supplier<List<SourceCodeRepositoryAccessProperties>> supplier) {
        String name = CodegenTaskContextHolder.getSourceCodeRepositoryName();
        return supplier.get()
                .stream()
                .filter(properties -> name.equals(properties.getName()))
                .findFirst()
                .orElseThrow(() -> new CodegenTaskException(String.format("not found name = %s 的源代码仓库配置", name)));
    }


}
