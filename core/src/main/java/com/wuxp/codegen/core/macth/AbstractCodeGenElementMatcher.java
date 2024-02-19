package com.wuxp.codegen.core.macth;

import com.wuxp.codegen.core.CodeGenElementMatcher;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.util.Objects;
import java.util.Set;

/**
 * @author wuxp
 */
public class AbstractCodeGenElementMatcher<S> implements CodeGenElementMatcher<S> {

    @Getter
    private final Set<S> matchSources;

    public AbstractCodeGenElementMatcher(Set<S> matchClasses) {
        this.matchSources = matchClasses;
    }

    @Override
    public boolean matches(S source) {
        if (ObjectUtils.isEmpty(matchSources)) {
            return false;
        }
        return matchSources.stream().anyMatch(matchSource -> Objects.equals(matchSource, source));
    }
}
