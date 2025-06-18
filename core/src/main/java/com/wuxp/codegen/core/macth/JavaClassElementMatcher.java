package com.wuxp.codegen.core.macth;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author wuxp
 */
public class JavaClassElementMatcher implements CodeGenTypeElementMatcher {

    /**
     * 需要全局忽略的包名列表
     */
    private static final List<String> DEFAULT_IGNORE_PATTERNS = new ArrayList<>();

    /**
     * 需要全局忽略的包名列表
     */
    private static final List<Class<?>> DEFAULT_INCLUDE_CLASSES = new ArrayList<>();

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
        DEFAULT_IGNORE_PATTERNS.add("java.net.");
        DEFAULT_IGNORE_PATTERNS.add("jakarta.servlet.");
        DEFAULT_IGNORE_PATTERNS.add("java.security.");
        DEFAULT_IGNORE_PATTERNS.add("java.text.");
        DEFAULT_IGNORE_PATTERNS.add("java.io.");
        DEFAULT_IGNORE_PATTERNS.add("java.time.");
        DEFAULT_IGNORE_PATTERNS.add("java.util.");
        DEFAULT_IGNORE_PATTERNS.add("java.lang.");
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
        DEFAULT_IGNORE_PATTERNS.add("org.springframework.web.multipart.commons.MultipartFile");

        DEFAULT_INCLUDE_CLASSES.add(Byte.class);
        DEFAULT_INCLUDE_CLASSES.add(Short.class);
        DEFAULT_INCLUDE_CLASSES.add(Integer.class);
        DEFAULT_INCLUDE_CLASSES.add(Long.class);
        DEFAULT_INCLUDE_CLASSES.add(Double.class);
        DEFAULT_INCLUDE_CLASSES.add(Float.class);
        DEFAULT_INCLUDE_CLASSES.add(Boolean.class);
        DEFAULT_INCLUDE_CLASSES.add(byte.class);
        DEFAULT_INCLUDE_CLASSES.add(short.class);
        DEFAULT_INCLUDE_CLASSES.add(int.class);
        DEFAULT_INCLUDE_CLASSES.add(long.class);
        DEFAULT_INCLUDE_CLASSES.add(double.class);
        DEFAULT_INCLUDE_CLASSES.add(float.class);
        DEFAULT_INCLUDE_CLASSES.add(boolean.class);
        DEFAULT_INCLUDE_CLASSES.add(BigDecimal.class);
        DEFAULT_INCLUDE_CLASSES.add(BigInteger.class);
        DEFAULT_INCLUDE_CLASSES.add(String.class);
        DEFAULT_INCLUDE_CLASSES.add(Date.class);
        DEFAULT_INCLUDE_CLASSES.add(LocalDateTime.class);
        DEFAULT_INCLUDE_CLASSES.add(LocalDate.class);

        DEFAULT_INCLUDE_CLASSES.add(Iterable.class);
        DEFAULT_INCLUDE_CLASSES.add(Collection.class);
        DEFAULT_INCLUDE_CLASSES.add(Map.class);
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
