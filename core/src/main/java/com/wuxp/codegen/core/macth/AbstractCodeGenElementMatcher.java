package com.wuxp.codegen.core.macth;

import com.wuxp.codegen.core.CodeGenElementMatcher;
import org.springframework.util.ObjectUtils;

import java.util.Set;

public class AbstractCodeGenElementMatcher<S> implements CodeGenElementMatcher<S> {

    private final Set<S> matchSources;

    public AbstractCodeGenElementMatcher(Set<S> matchClasses) {
        this.matchSources = matchClasses;
    }

    @Override
    public boolean matches(S source) {
        if (ObjectUtils.isEmpty(matchSources)) {
            return false;
        }
        return matchSources.stream().anyMatch(matchSource -> matchSource.equals(source));
    }
}
