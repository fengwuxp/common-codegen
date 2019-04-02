package com.wuxp.codegen.dragon.strategy;

import com.wuxp.codegen.core.strategy.AbstractPackageMapStrategy;
import com.wuxp.codegen.dragon.path.PathResolve;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Map;


/**
 * typeScript 映射策略
 */
@Slf4j
public class TypescriptPackageMapStrategy extends AbstractPackageMapStrategy {


    public TypescriptPackageMapStrategy(Map<String, String> packageNameMap) {
        super(packageNameMap);
    }

    @Override
    public String convert(Class<?> clazz) {
        String path = super.convert(clazz);

        if (!StringUtils.hasText(path)) {
            log.warn("{}转换后的导入的路径为空", clazz.getName());
            return clazz.getSimpleName();
        }

        String convertClassName = this.convertClassName(path.replaceAll("\\.", PathResolve.RIGHT_SLASH));
        if (convertClassName.startsWith(PathResolve.RIGHT_SLASH)) {
            return convertClassName;
        }
        return PathResolve.RIGHT_SLASH + convertClassName;

    }


}
