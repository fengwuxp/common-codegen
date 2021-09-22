package com.wuxp.codegen.mapping;

import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.model.JavaArrayClassTypeMark;
import org.reactivestreams.Publisher;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class MappingTypescriptTypeDefinitionParser extends AbstractMappingTypeDefinitionParser<TypescriptClassMeta> {

    private static final Map<Class<?>, TypescriptClassMeta> TYPESCRIPT_BASE_MAPPINGS = new LinkedHashMap<>();

    static {
        //设置基础的数据类型映射
        TYPESCRIPT_BASE_MAPPINGS.put(Object.class, TypescriptClassMeta.ANY);
        TYPESCRIPT_BASE_MAPPINGS.put(Date.class, TypescriptClassMeta.NUMBER);
        TYPESCRIPT_BASE_MAPPINGS.put(LocalDateTime.class, TypescriptClassMeta.NUMBER);
        TYPESCRIPT_BASE_MAPPINGS.put(LocalDate.class, TypescriptClassMeta.NUMBER);
        TYPESCRIPT_BASE_MAPPINGS.put(Boolean.class, TypescriptClassMeta.BOOLEAN);
        TYPESCRIPT_BASE_MAPPINGS.put(String.class, TypescriptClassMeta.STRING);
        TYPESCRIPT_BASE_MAPPINGS.put(Long.class, TypescriptClassMeta.STRING);
        TYPESCRIPT_BASE_MAPPINGS.put(BigInteger.class, TypescriptClassMeta.STRING);
        TYPESCRIPT_BASE_MAPPINGS.put(Number.class, TypescriptClassMeta.NUMBER);
        TYPESCRIPT_BASE_MAPPINGS.put(double.class, TypescriptClassMeta.NUMBER);
        TYPESCRIPT_BASE_MAPPINGS.put(float.class, TypescriptClassMeta.NUMBER);
        TYPESCRIPT_BASE_MAPPINGS.put(long.class, TypescriptClassMeta.STRING);
        TYPESCRIPT_BASE_MAPPINGS.put(short.class, TypescriptClassMeta.NUMBER);
        TYPESCRIPT_BASE_MAPPINGS.put(byte.class, TypescriptClassMeta.NUMBER);

        TYPESCRIPT_BASE_MAPPINGS.put(Map.class, TypescriptClassMeta.RECORD);
        TYPESCRIPT_BASE_MAPPINGS.put(Set.class, TypescriptClassMeta.ARRAY);
        TYPESCRIPT_BASE_MAPPINGS.put(List.class, TypescriptClassMeta.ARRAY);
        TYPESCRIPT_BASE_MAPPINGS.put(Collection.class, TypescriptClassMeta.ARRAY);
        TYPESCRIPT_BASE_MAPPINGS.put(void.class, TypescriptClassMeta.VOID);
        TYPESCRIPT_BASE_MAPPINGS.put(Void.class, TypescriptClassMeta.VOID);


        //文件上传
        TYPESCRIPT_BASE_MAPPINGS.put(CommonsMultipartFile.class, TypescriptClassMeta.BROWSER_FILE);
        TYPESCRIPT_BASE_MAPPINGS.put(InputStreamResource.class, TypescriptClassMeta.BROWSER_FILE);
        TYPESCRIPT_BASE_MAPPINGS.put(JavaArrayClassTypeMark.class, TypescriptClassMeta.JAVA_ARRAY_CLASS_TYPE_MARK);

        // TODO support rxJava、reactor
        TYPESCRIPT_BASE_MAPPINGS.put(Publisher.class, TypescriptClassMeta.PROMISE);
    }

    public static AbstractMappingTypeDefinitionParserBuilder<TypescriptClassMeta> builder() {
        return new AbstractMappingTypeDefinitionParserBuilder<TypescriptClassMeta>(TYPESCRIPT_BASE_MAPPINGS) {
            @Override
            public MappingTypescriptTypeDefinitionParser build() {
                return new MappingTypescriptTypeDefinitionParser(this);
            }
        };
    }

    private MappingTypescriptTypeDefinitionParser(AbstractMappingTypeDefinitionParserBuilder<TypescriptClassMeta> builder) {
        super(new MappingTypeDefinitionParser<>(builder.getTypeMappings()));
    }
}
