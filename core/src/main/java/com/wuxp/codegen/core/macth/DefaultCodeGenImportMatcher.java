package com.wuxp.codegen.core.macth;

import com.wuxp.codegen.core.CodeGenImportMatcher;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.*;

/**
 * 默认的 {@link CodeGenImportMatcher}匹配器，通过匹配包名和类名
 *
 * @author wuxp
 */
public class DefaultCodeGenImportMatcher implements CodeGenImportMatcher {

    private final static PathMatcher MATCHER = new AntPathMatcher();

    /**
     * 只需要导入包的包名列表或着类的全类名
     * 支持ant匹配
     */
    public final Set<String> onlyImportPackages;

    public final Set<Class> onlyImportClasses;

    public static DefaultCodeGenImportMatcher of(String... onlyImportPackages) {
        return new DefaultCodeGenImportMatcher(new HashSet<>(Arrays.asList(onlyImportPackages)), Collections.emptySet());
    }

    public static DefaultCodeGenImportMatcher of(Class... onlyImportClasses) {
        return new DefaultCodeGenImportMatcher(Collections.emptySet(), new HashSet<>(Arrays.asList(onlyImportClasses)));
    }

    public static DefaultCodeGenImportMatcher of(Collection<String> onlyImportPackages, Collection<Class> onlyImportClasses) {
        return new DefaultCodeGenImportMatcher(new HashSet<>(onlyImportPackages), new HashSet<>(onlyImportClasses));
    }


    private DefaultCodeGenImportMatcher(Set<String> onlyImportPackages, Set<Class> onlyImportClasses) {
        this.onlyImportPackages = onlyImportPackages;
        this.onlyImportClasses = onlyImportClasses;
    }

    @Override
    public boolean match(Class<?> clazz) {
        if (onlyImportClasses != null) {
            boolean match = onlyImportClasses.stream().anyMatch(aClass -> aClass.equals(clazz));
            if (match) {
                return true;
            }
        }
        return onlyImportPackages.stream().anyMatch(name -> clazz.getName().startsWith(name) || MATCHER.match(name, clazz.getName()));
    }
}
