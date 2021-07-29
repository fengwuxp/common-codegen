package com.wuxp.codegen.core.macth;

import java.util.*;

public class IncludeCodeGenTypeElementMatcher extends AbstractCodeGenTypeElementMatcher {

    private IncludeCodeGenTypeElementMatcher(Set<String> matchPackages, Set<Class<?>> matchClasses) {
        super(matchPackages, matchClasses);
    }

    public static IncludeCodeGenTypeElementMatcher of(String... includePackages) {
        return new IncludeCodeGenTypeElementMatcher(new HashSet<>(Arrays.asList(includePackages)), Collections.emptySet());
    }

    public static IncludeCodeGenTypeElementMatcher of(Class<?>... includeClasses) {
        return new IncludeCodeGenTypeElementMatcher(Collections.emptySet(), new HashSet<>(Arrays.asList(includeClasses)));
    }

    public static IncludeCodeGenTypeElementMatcher of(Collection<String> includePackages, Collection<Class<?>> includeClasses) {
        return new IncludeCodeGenTypeElementMatcher(new HashSet<>(includePackages), new HashSet<>(includeClasses));
    }
}
