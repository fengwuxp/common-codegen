package com.wuxp.codegen.mapping;

import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import com.wuxp.codegen.model.mapping.JavaArrayClassTypeMark;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class MappingJavaTypeDefinitionParser extends AbstractMappingTypeDefinitionParser<JavaCodeGenClassMeta> {

    private static final Map<Class<?>, JavaCodeGenClassMeta> JAVA_BASE_MAPPINGS = new LinkedHashMap<>();

    static {
        //设置基础的数据类型映射
        JAVA_BASE_MAPPINGS.put(Object.class, JavaCodeGenClassMeta.OBJECT);
        JAVA_BASE_MAPPINGS.put(Date.class, JavaCodeGenClassMeta.DATE);
        JAVA_BASE_MAPPINGS.put(LocalDate.class, JavaCodeGenClassMeta.LOCAL_DATE);
        JAVA_BASE_MAPPINGS.put(LocalDateTime.class, JavaCodeGenClassMeta.LOCAL_DATE_TIME);
        JAVA_BASE_MAPPINGS.put(void.class, JavaCodeGenClassMeta.VOID);

        JAVA_BASE_MAPPINGS.put(Boolean.class, JavaCodeGenClassMeta.BOOLEAN);
        JAVA_BASE_MAPPINGS.put(boolean.class, JavaCodeGenClassMeta.BOOLEAN);
        JAVA_BASE_MAPPINGS.put(String.class, JavaCodeGenClassMeta.STRING);
        JAVA_BASE_MAPPINGS.put(CharSequence.class, JavaCodeGenClassMeta.CHAR_SEQUENCE);
        JAVA_BASE_MAPPINGS.put(char.class, JavaCodeGenClassMeta.CHAR);

        JAVA_BASE_MAPPINGS.put(Number.class, JavaCodeGenClassMeta.NUMBER);
        JAVA_BASE_MAPPINGS.put(BigDecimal.class, JavaCodeGenClassMeta.BIG_DECIMAL);
        JAVA_BASE_MAPPINGS.put(double.class, JavaCodeGenClassMeta.DOUBLE);
        JAVA_BASE_MAPPINGS.put(Double.class, JavaCodeGenClassMeta.DOUBLE);
        JAVA_BASE_MAPPINGS.put(Float.class, JavaCodeGenClassMeta.FLOAT);
        JAVA_BASE_MAPPINGS.put(float.class, JavaCodeGenClassMeta.FLOAT);
        JAVA_BASE_MAPPINGS.put(long.class, JavaCodeGenClassMeta.LONG);
        JAVA_BASE_MAPPINGS.put(Long.class, JavaCodeGenClassMeta.LONG);
        JAVA_BASE_MAPPINGS.put(Integer.class, JavaCodeGenClassMeta.INTEGER);
        JAVA_BASE_MAPPINGS.put(int.class, JavaCodeGenClassMeta.INTEGER);
        JAVA_BASE_MAPPINGS.put(Short.class, JavaCodeGenClassMeta.SHORT);
        JAVA_BASE_MAPPINGS.put(short.class, JavaCodeGenClassMeta.SHORT);
        JAVA_BASE_MAPPINGS.put(Byte.class, JavaCodeGenClassMeta.BYTE);
        JAVA_BASE_MAPPINGS.put(byte.class, JavaCodeGenClassMeta.BYTE);

        JAVA_BASE_MAPPINGS.put(Map.class, JavaCodeGenClassMeta.MAP);
        JAVA_BASE_MAPPINGS.put(Set.class, JavaCodeGenClassMeta.SET);
        JAVA_BASE_MAPPINGS.put(List.class, JavaCodeGenClassMeta.LIST);
        JAVA_BASE_MAPPINGS.put(Collection.class, JavaCodeGenClassMeta.COLLECTION);
        JAVA_BASE_MAPPINGS.put(JavaArrayClassTypeMark.class, JavaCodeGenClassMeta.JAVA_ARRAY_CLASS_TYPE_MARK);
        JAVA_BASE_MAPPINGS.put(CommonsMultipartFile.class, JavaCodeGenClassMeta.FILE);
        JAVA_BASE_MAPPINGS.put(InputStreamResource.class, JavaCodeGenClassMeta.FILE);

        // TODO support rxJava、reactor
        JAVA_BASE_MAPPINGS.put(Flux.class, JavaCodeGenClassMeta.REACTOR_FLUX);
        JAVA_BASE_MAPPINGS.put(Mono.class, JavaCodeGenClassMeta.REACTOR_MONO);
    }

    public static AbstractMappingTypeDefinitionParserBuilder<JavaCodeGenClassMeta> builder(LanguageTypeDefinitionParser<JavaCodeGenClassMeta> delegate) {
        return new AbstractMappingTypeDefinitionParserBuilder<JavaCodeGenClassMeta>(delegate, JAVA_BASE_MAPPINGS) {
            @Override
            public MappingJavaTypeDefinitionParser build() {
                return new MappingJavaTypeDefinitionParser(this);
            }
        };
    }

    private MappingJavaTypeDefinitionParser(AbstractMappingTypeDefinitionParserBuilder<JavaCodeGenClassMeta> builder) {
        super(builder.getDelegate(), new MappingTypeDefinitionParser<>(builder.getTypeMappings(), builder.getJavaTypeMappings()));
    }


}
