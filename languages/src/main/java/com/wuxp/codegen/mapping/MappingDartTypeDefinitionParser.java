package com.wuxp.codegen.mapping;

import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.model.languages.dart.DartClassMeta;
import org.reactivestreams.Publisher;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class MappingDartTypeDefinitionParser extends AbstractMappingTypeDefinitionParser<DartClassMeta> {

    private static final Map<Class<?>, DartClassMeta> DART_DEFAULT_BASE_MAPPING = new LinkedHashMap<>();

    static {
        //设置基础的数据类型映射
        DART_DEFAULT_BASE_MAPPING.put(Object.class, DartClassMeta.OBJECT);
        DART_DEFAULT_BASE_MAPPING.put(Date.class, DartClassMeta.DATE);
        DART_DEFAULT_BASE_MAPPING.put(LocalDate.class, DartClassMeta.DATE);
        DART_DEFAULT_BASE_MAPPING.put(LocalDateTime.class, DartClassMeta.DATE);
        DART_DEFAULT_BASE_MAPPING.put(Boolean.class, DartClassMeta.BOOL);
        DART_DEFAULT_BASE_MAPPING.put(String.class, DartClassMeta.STRING);
        DART_DEFAULT_BASE_MAPPING.put(Number.class, DartClassMeta.NUM);
        DART_DEFAULT_BASE_MAPPING.put(Long.class, DartClassMeta.INT);
        DART_DEFAULT_BASE_MAPPING.put(Integer.class, DartClassMeta.INT);
        DART_DEFAULT_BASE_MAPPING.put(Short.class, DartClassMeta.INT);
        DART_DEFAULT_BASE_MAPPING.put(Byte.class, DartClassMeta.INT);
        DART_DEFAULT_BASE_MAPPING.put(double.class, DartClassMeta.DOUBLE);
        DART_DEFAULT_BASE_MAPPING.put(float.class, DartClassMeta.DOUBLE);
        DART_DEFAULT_BASE_MAPPING.put(long.class, DartClassMeta.INT);
        DART_DEFAULT_BASE_MAPPING.put(short.class, DartClassMeta.INT);
        DART_DEFAULT_BASE_MAPPING.put(byte.class, DartClassMeta.INT);
        DART_DEFAULT_BASE_MAPPING.put(Map.class, DartClassMeta.BUILT_MAP);
        DART_DEFAULT_BASE_MAPPING.put(Set.class, DartClassMeta.BUILT_SET);
        DART_DEFAULT_BASE_MAPPING.put(List.class, DartClassMeta.BUILT_LIST);

        // 由于built_collection 没有导出BuiltIterable 先转化为 BuiltList
        DART_DEFAULT_BASE_MAPPING.put(Collection.class, DartClassMeta.BUILT_LIST);
        DART_DEFAULT_BASE_MAPPING.put(void.class, DartClassMeta.VOID);
        DART_DEFAULT_BASE_MAPPING.put(Void.class, DartClassMeta.VOID);

        //文件上传
        DART_DEFAULT_BASE_MAPPING.put(CommonsMultipartFile.class, DartClassMeta.FILE);
        DART_DEFAULT_BASE_MAPPING.put(InputStreamResource.class, DartClassMeta.FILE);

        // TODO support rxDart、reactor
        DART_DEFAULT_BASE_MAPPING.put(Publisher.class, DartClassMeta.FUTURE);
    }

    public static AbstractMappingTypeDefinitionParserBuilder<DartClassMeta> builder(LanguageTypeDefinitionParser<DartClassMeta> delegate) {
        return new AbstractMappingTypeDefinitionParserBuilder<DartClassMeta>(delegate, DART_DEFAULT_BASE_MAPPING) {
            @Override
            public MappingDartTypeDefinitionParser build() {
                return new MappingDartTypeDefinitionParser(this);
            }
        };
    }

    private MappingDartTypeDefinitionParser(AbstractMappingTypeDefinitionParserBuilder<DartClassMeta> builder) {
        super(builder.getDelegate(), new MappingTypeDefinitionParser<>(builder.getTypeMappings(), builder.getJavaTypeMappings()));
    }
}
