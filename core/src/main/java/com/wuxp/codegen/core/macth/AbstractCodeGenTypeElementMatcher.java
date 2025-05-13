package com.wuxp.codegen.core.macth;

import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.PathMatcher;

import java.util.Set;

/**
 * @author wuxp
 */
public class AbstractCodeGenTypeElementMatcher extends AbstractCodeGenElementMatcher<Class<?>> implements CodeGenTypeElementMatcher {

    private final PathMatcher pathMatcher;

    private final Set<String> patterns;

    protected AbstractCodeGenTypeElementMatcher(Set<String> matchPackages, Set<Class<?>> matchClasses) {
        super(matchClasses);
        Assert.notNull(matchPackages, "match packages not null");
        this.patterns = matchPackages;
        this.pathMatcher = new AntPathMatcher();
    }

    @Override
    public boolean matches(Class<?> clazz) {
        if (super.matches(clazz)) {
            return true;
        }
        return getMatchSources().stream().anyMatch(supperClaszz -> ClassUtils.isAssignable(supperClaszz, clazz)) ||
                patterns.stream().anyMatch(pattern -> match(clazz.getName(), pattern));
    }

    private boolean match(String className, String pattern) {
        if (pathMatcher.isPattern(pattern)) {
            return pathMatcher.match(pattern, className);
        } else {
            return className.startsWith(pattern);
        }
    }
}
