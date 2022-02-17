package com.wuxp.codegen.loong.freemarker;

import com.wuxp.codegen.loong.path.PathResolve;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 相对路径解析
 *
 * @author wuxp
 */
@Slf4j
public final class PathResolveMethod implements TemplateMethodModelEx {

    private static final String DEFAULT_PATH = "api";

    private final PathResolve pathResolve = new PathResolve();

    @Override
    @SuppressWarnings("unchecked")
    public Object exec(List arguments) {
        Assert.notEmpty(arguments, "arguments is null or is empty");
        List<String> paths = ((List<Object>) arguments).stream()
                .filter(Objects::nonNull)
                .map(SimpleScalar.class::cast)
                .map(SimpleScalar::getAsString)
                .collect(Collectors.toList());

        String path = DEFAULT_PATH;
        int size = paths.size();
        if (size == 3) {
            // 路径组成等于 3 则做分隔
            path = paths.get(0);
            paths = paths.subList(1, size);
        }
        return pathResolve.relativizeResolve(path, paths.toArray(new String[]{}));
    }
}
