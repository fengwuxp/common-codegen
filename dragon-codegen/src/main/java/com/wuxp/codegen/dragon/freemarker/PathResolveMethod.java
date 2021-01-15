package com.wuxp.codegen.dragon.freemarker;

import com.wuxp.codegen.dragon.path.PathResolve;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 相对路径解析
 */
@Slf4j
public class PathResolveMethod implements TemplateMethodModelEx {

  private PathResolve pathResolve = new PathResolve();

  @Override
  public Object exec(List arguments) throws TemplateModelException {
    if (arguments.size() == 0) {
      throw new RuntimeException("arguments size is 0");
    }
    List<String> paths = ((List<Object>) arguments).stream()
        .filter(Objects::nonNull)
        .map(a -> ((SimpleScalar) a).getAsString())
        .collect(Collectors.toList());

    String path = "api";
    int size = paths.size();
    if (size == 3) {
      path = paths.get(0);
      paths = paths.subList(1, size);
    }
    return pathResolve.relativizeResolve(path, paths.toArray(new String[]{}));
  }
}
