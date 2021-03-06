package com.wuxp.codegen.loong.freemarker;

import com.wuxp.codegen.core.exception.CodegenRuntimeException;
import com.wuxp.codegen.loong.path.PathResolve;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 相对路径解析
 *
 * @author wuxp
 */
@Slf4j
public class PathResolveMethod implements TemplateMethodModelEx {

    private final PathResolve pathResolve = new PathResolve();

    @Override
    public Object exec(List arguments) {
        if (arguments.isEmpty()) {
            throw new CodegenRuntimeException("arguments size is 0");
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
