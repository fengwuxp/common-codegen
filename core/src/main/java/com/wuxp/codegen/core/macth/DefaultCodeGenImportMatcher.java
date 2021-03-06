package com.wuxp.codegen.core.macth;

import com.wuxp.codegen.core.CodeGenImportMatcher;

import java.util.*;

/**
 * 默认的 {@link CodeGenImportMatcher}匹配器，通过匹配包名和类名
 *
 * @author wuxp
 */
public class DefaultCodeGenImportMatcher extends AbstractCodeGenMatcher implements CodeGenImportMatcher {

    private DefaultCodeGenImportMatcher(Set<String> matchPackages, Set<Class> matchClasses) {
        super(matchPackages, matchClasses);
    }

    public static DefaultCodeGenImportMatcher of(String... onlyImportPackages) {
        return new DefaultCodeGenImportMatcher(new HashSet<>(Arrays.asList(onlyImportPackages)), Collections.emptySet());
    }

    public static DefaultCodeGenImportMatcher of(Class... onlyImportClasses) {
        return new DefaultCodeGenImportMatcher(Collections.emptySet(), new HashSet<>(Arrays.asList(onlyImportClasses)));
    }

    public static DefaultCodeGenImportMatcher of(Collection<String> onlyImportPackages, Collection<Class> onlyImportClasses) {
        return new DefaultCodeGenImportMatcher(new HashSet<>(onlyImportPackages), new HashSet<>(onlyImportClasses));
    }
}
