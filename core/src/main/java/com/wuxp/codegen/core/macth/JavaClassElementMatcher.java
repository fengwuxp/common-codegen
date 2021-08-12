package com.wuxp.codegen.core.macth;

import java.util.*;

/**
 * @author wuxp
 */
public class JavaClassElementMatcher implements CodeGenTypeElementMatcher {

    /**
     * 需要全局忽略的包名列表
     */
    private static final List<String> DEFAULT_IGNORE_PATTERNS = new ArrayList<>();

    // org.springframework.core.io.InputStreamResource

    static {
        DEFAULT_IGNORE_PATTERNS.add("org.springframework.");
        DEFAULT_IGNORE_PATTERNS.add("org.slf4j.");
        DEFAULT_IGNORE_PATTERNS.add("org.apache.");
        DEFAULT_IGNORE_PATTERNS.add("org.freemarker.");
        DEFAULT_IGNORE_PATTERNS.add("org.hibernate.");
        DEFAULT_IGNORE_PATTERNS.add("org.jetbrains.");
        DEFAULT_IGNORE_PATTERNS.add("org.jodd.");
        DEFAULT_IGNORE_PATTERNS.add("org.apache.");
        DEFAULT_IGNORE_PATTERNS.add("lombok.");
        DEFAULT_IGNORE_PATTERNS.add("javax.persistence.");
        DEFAULT_IGNORE_PATTERNS.add("javax.servlet.");
        DEFAULT_IGNORE_PATTERNS.add("java.security.");
        DEFAULT_IGNORE_PATTERNS.add("java.text.");
        DEFAULT_IGNORE_PATTERNS.add("java.io.");
        DEFAULT_IGNORE_PATTERNS.add("java.time.");
        DEFAULT_IGNORE_PATTERNS.add("java.util.");
        DEFAULT_IGNORE_PATTERNS.add("sun.");
        DEFAULT_IGNORE_PATTERNS.add("com.google.");
        DEFAULT_IGNORE_PATTERNS.add("com.alibaba.");
        DEFAULT_IGNORE_PATTERNS.add("com.alipay.");
        DEFAULT_IGNORE_PATTERNS.add("com.baidu.");
        DEFAULT_IGNORE_PATTERNS.add("com.github.");
        DEFAULT_IGNORE_PATTERNS.add("reactor.");
        DEFAULT_IGNORE_PATTERNS.add("org.reactivestreams");
        DEFAULT_IGNORE_PATTERNS.add("io.reactivex.");
        DEFAULT_IGNORE_PATTERNS.add("com.wuxp.basic.");

        /**
         * 文件上传
         */
        DEFAULT_IGNORE_PATTERNS.add("org.springframework.web.multipart.commons.CommonsMultipartFile");
    }

    private final ExcludeCodeGenTypeElementMatcher excludeMatcher;

    private final IncludeCodeGenTypeElementMatcher includeMatcher;

    private JavaClassElementMatcher(Set<String> includePackages, Set<Class<?>> includeClasses, Set<String> ignorePackages, Set<Class<?>> ignoreClasses) {
        this.includeMatcher = IncludeCodeGenTypeElementMatcher.of(includePackages, includeClasses);
        this.excludeMatcher = ExcludeCodeGenTypeElementMatcher.of(getIgnorePackages(ignorePackages), ignoreClasses);
    }


    private Set<String> getIgnorePackages(Set<String> ignorePackages) {
        Set<String> result = new HashSet<>(DEFAULT_IGNORE_PATTERNS);
        result.addAll(ignorePackages);
        return result;
    }

    public static JavaClassElementMatcherBuilder builder() {
        return new JavaClassElementMatcherBuilder();
    }

    @Override
    public boolean matches(Class<?> source) {
        if (includeMatcher.matches(source)) {
            return true;
        }
        return !excludeMatcher.matches(source);
    }

    public static class JavaClassElementMatcherBuilder {
        private final Set<String> ignorePackages;

        private final Set<Class<?>> ignoreClasses;

        private final Set<String> includePackages;

        private final Set<Class<?>> includeClasses;

        public JavaClassElementMatcherBuilder() {
            this.ignorePackages = new HashSet<>();
            this.ignoreClasses = new HashSet<>();
            this.includePackages = new HashSet<>();
            this.includeClasses = new HashSet<>(Collections.singletonList(
                    Date.class
            ));
        }

        public JavaClassElementMatcherBuilder ignorePackages(Collection<String> ignorePackages) {
            this.ignorePackages.addAll(ignorePackages);
            return this;
        }

        public JavaClassElementMatcherBuilder ignorePackages(String... ignorePackages) {
            this.ignorePackages.addAll(Arrays.asList(ignorePackages));
            return this;
        }

        public JavaClassElementMatcherBuilder ignoreClasses(Collection<Class<?>> ignoreClasses) {
            this.ignoreClasses.addAll(ignoreClasses);
            return this;
        }

        public JavaClassElementMatcherBuilder ignoreClasses(Class<?>... ignoreClasses) {
            this.ignoreClasses.addAll(Arrays.asList(ignoreClasses));
            return this;
        }

        public JavaClassElementMatcherBuilder includePackages(Collection<String> includePackages) {
            this.includePackages.addAll(includePackages);
            return this;
        }

        public JavaClassElementMatcherBuilder includePackages(String... includePackages) {
            this.includePackages.addAll(Arrays.asList(includePackages));
            return this;
        }

        public JavaClassElementMatcherBuilder includeClasses(Collection<Class<?>> includeClasses) {
            this.includeClasses.addAll(includeClasses);
            return this;
        }

        public JavaClassElementMatcherBuilder includeClasses(Class<?>... includeClasses) {
            this.includeClasses.addAll(Arrays.asList(includeClasses));
            return this;
        }

        public JavaClassElementMatcher build() {
            return new JavaClassElementMatcher(includePackages, includeClasses, ignorePackages, ignoreClasses);
        }
    }
}
