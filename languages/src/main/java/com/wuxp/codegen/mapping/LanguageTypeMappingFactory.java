package com.wuxp.codegen.mapping;

import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.LanguageDescription;
import lombok.Builder;

import java.util.Map;

/**
 * 用于构建 不同语言的{@link AbstractLanguageTypeMapping}
 *
 * @author wuxp
 */
@Builder
public final class LanguageTypeMappingFactory {

    private final LanguageParser languageParser;
    /**
     * 基础类型映射器
     */
    private final Map<Class<?>, CommonCodeGenClassMeta> baseTypeMapping;

    /**
     * 自定义的类型映射器
     */
    private final Map<Class<?>, CommonCodeGenClassMeta> customizeTypeMapping;

    /**
     * 自定义的java类型映射
     */
    private final Map<Class<?>, Class<?>[]> customizeJavaMapping;


    /**
     * 语言描述
     */
    private final LanguageDescription languageDescription;


    public AbstractLanguageTypeMapping factory() {
        switch (languageDescription) {
            case DART:
                return new DartTypeMapping(languageParser, baseTypeMapping, customizeTypeMapping, customizeJavaMapping);
            case JAVA:
            case JAVA_ANDROID:
                return new JavaTypeMapping(languageParser, baseTypeMapping, customizeTypeMapping, customizeJavaMapping);
            case TYPESCRIPT:
                return new TypescriptTypeMapping(languageParser, baseTypeMapping, customizeTypeMapping, customizeJavaMapping);
            default:
                throw new RuntimeException("not support language");
        }
    }
}
