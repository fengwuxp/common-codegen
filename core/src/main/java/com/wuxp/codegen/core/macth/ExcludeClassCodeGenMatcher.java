package com.wuxp.codegen.core.macth;

import com.wuxp.codegen.core.CodeGenMatcher;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * 只匹配指定的的包名，匹配成功的将会被排除 {@link CodeGenMatcher}
 *
 * @author wxup
 */
@Slf4j
public class ExcludeClassCodeGenMatcher extends AbstractCodeGenMatcher {

  public static ExcludeClassCodeGenMatcher of(String... excludePackages) {
    return new ExcludeClassCodeGenMatcher(new HashSet<>(Arrays.asList(excludePackages)), Collections.emptySet());
  }

  public static ExcludeClassCodeGenMatcher of(Class... excludeClasses) {
    return new ExcludeClassCodeGenMatcher(Collections.emptySet(), new HashSet<>(Arrays.asList(excludeClasses)));
  }

  public static ExcludeClassCodeGenMatcher of(Collection<String> excludePackages, Collection<Class> excludeClasses) {
    return new ExcludeClassCodeGenMatcher(new HashSet<>(excludePackages), new HashSet<>(excludeClasses));
  }

  private ExcludeClassCodeGenMatcher(Set<String> matchPackages, Set<Class> matchClasses) {
    super(matchPackages, matchClasses);
  }

  @Override
  public boolean match(Class<?> clazz) {
    return !super.match(clazz);
  }
}
