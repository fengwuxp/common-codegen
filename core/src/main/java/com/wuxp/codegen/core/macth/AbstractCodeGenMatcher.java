package com.wuxp.codegen.core.macth;

import com.wuxp.codegen.core.CodeGenMatcher;
import java.util.Set;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

/**
 * 匹配时只匹配指定的包或者类，对于包名支持ant 匹配
 *
 * @author wuxp
 */
public abstract class AbstractCodeGenMatcher implements CodeGenMatcher {

  protected final static PathMatcher PATH_MATCHER = new AntPathMatcher();

  protected final Set<String> matchPackages;

  protected final Set<Class> matchClasses;

  public AbstractCodeGenMatcher(Set<String> matchPackages, Set<Class> matchClasses) {
    this.matchPackages = matchPackages;
    this.matchClasses = matchClasses;
  }

  @Override
  public boolean match(Class<?> clazz) {
    if (matchClasses != null) {
      boolean match = matchClasses.stream().anyMatch(aClass -> aClass.equals(clazz));
      if (match) {
        return true;
      }
    }
    return matchPackages.stream().anyMatch(name -> clazz.getName().startsWith(name) || PATH_MATCHER.match(name, clazz.getName()));
  }
}
