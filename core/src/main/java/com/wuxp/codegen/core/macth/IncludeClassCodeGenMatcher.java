package com.wuxp.codegen.core.macth;

import com.wuxp.codegen.core.CodeGenMatcher;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 只匹配指定的的包名，匹配成功的才能参与生成 {@link CodeGenMatcher}
 *
 * @author wxup
 */
@Slf4j
public class IncludeClassCodeGenMatcher extends AbstractCodeGenMatcher {

    private IncludeClassCodeGenMatcher(Set<String> matchPackages, Set<Class> matchClasses) {
        super(matchPackages, matchClasses);
    }

    public static IncludeClassCodeGenMatcher of(String... includePackages) {
        return new IncludeClassCodeGenMatcher(new HashSet<>(Arrays.asList(includePackages)), Collections.emptySet());
    }

    public static IncludeClassCodeGenMatcher of(Class... includeClasses) {
        return new IncludeClassCodeGenMatcher(Collections.emptySet(), new HashSet<>(Arrays.asList(includeClasses)));
    }

    public static IncludeClassCodeGenMatcher of(Collection<String> includePackages, Collection<Class> includeClasses) {
        return new IncludeClassCodeGenMatcher(new HashSet<>(includePackages), new HashSet<>(includeClasses));
    }
}
