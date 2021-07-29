package com.wuxp.codegen.core.macth;

import java.util.*;

public class ExcludeCodeGenTypeElementMatcher extends AbstractCodeGenTypeElementMatcher {

    private ExcludeCodeGenTypeElementMatcher(Set<String> matchPackages, Set<Class<?>> matchClasses) {
        super(matchPackages, matchClasses);
    }

    public static ExcludeCodeGenTypeElementMatcher of(String... includePackages) {
        return new ExcludeCodeGenTypeElementMatcher(new HashSet<>(Arrays.asList(includePackages)), Collections.emptySet());
    }

    public static ExcludeCodeGenTypeElementMatcher of(Class<?>... includeClasses) {
        return new ExcludeCodeGenTypeElementMatcher(Collections.emptySet(), new HashSet<>(Arrays.asList(includeClasses)));
    }

    public static ExcludeCodeGenTypeElementMatcher of(Collection<String> includePackages, Collection<Class<?>> includeClasses) {
        return new ExcludeCodeGenTypeElementMatcher(new HashSet<>(includePackages), new HashSet<>(includeClasses));
    }

    @Override
    public boolean matches(Class<?> clazz) {
        return !super.matches(clazz);
    }
}
