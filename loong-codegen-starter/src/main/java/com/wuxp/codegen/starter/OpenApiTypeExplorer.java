package com.wuxp.codegen.starter;

import com.wuxp.codegen.core.util.ClassLoaderUtils;
import com.wuxp.codegen.starter.enums.OpenApiType;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * open api type 支持的自动探测
 *
 * @author wuxp
 */
@Slf4j
public final class OpenApiTypeExplorer {

    private static final List<String> OPEN_API__CLASSES = Arrays.asList(
            "io.swagger.annotations.Api",
            "io.swagger.v3.oas.annotations.OpenAPIDefinition"
    );

    private static final List<OpenApiType> OPEN_API_TYPES = Arrays.asList(
            OpenApiType.SWAGGER_2,
            OpenApiType.SWAGGER_3
    );


    private static OpenApiType defaultOpenApiType;


    private OpenApiTypeExplorer() {
    }

    public static synchronized OpenApiType getDefaultOpenApiType() {
        if (defaultOpenApiType != null) {
            return defaultOpenApiType;
        }
        for (int i = 0; i < OPEN_API__CLASSES.size(); i++) {
            String className = OPEN_API__CLASSES.get(i);
            try {
                ClassLoaderUtils.loadClass(className);
                defaultOpenApiType = OPEN_API_TYPES.get(i);
            } catch (ClassNotFoundException exception) {
                if (log.isDebugEnabled()) {
                    log.debug("OpenApiTypeExplorer class not found：{}" ,className);
                }
            }
            if (defaultOpenApiType != null) {
                break;
            }
        }
        if (defaultOpenApiType == null) {
            defaultOpenApiType = OpenApiType.DEFAULT;
        }
        return defaultOpenApiType;
    }

}
