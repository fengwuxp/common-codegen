package com.wuxp.codegen.languages.ts;


import com.wuxp.codegen.core.mapping.BaseTypeMapping;
import com.wuxp.codegen.core.mapping.TypeMapping;
import com.wuxp.codegen.core.processor.GenericParserProcessor;
import com.wuxp.codegen.languages.AbstractLanguageParser;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.languages.typescript.TypeScriptClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypeScriptFieldMeta;
import com.wuxp.codegen.model.languages.typescript.TypeScriptMethodMeta;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 将java class meta 解析为 typescript元数据信息
 */
public class TypeScriptGenericParser extends AbstractLanguageParser<TypeScriptClassMeta, TypeScriptMethodMeta, TypeScriptFieldMeta> {


    protected GenericParserProcessor<JavaFieldMeta, TypeScriptFieldMeta> fieldMetaGenericParserProcessor;


    private TypeMapping<TypeScriptClassMeta> typeMapping = new TypeScriptTypeMapping();

    private GenericParserProcessor genericParserProcessor;


    @Override
    public TypeScriptClassMeta parse(JavaClassMeta source) {


        TypeScriptClassMeta.builder()
                .classType(source.getClassType())
                .isAbstract(source.getIsAbstract())
                .interfaces(Arrays.stream(source.getInterfaces())
                        .map(this::parseSupper)
                        .collect(Collectors.toList())
                        .toArray(new TypeScriptClassMeta[]{}))
                .superClass(this.parseSupper(source.getSuperClass()))
                .fieldMetas(this.converterFieldMetas(source.getFieldMetas()))
                .methodMetas(this.converterMethodMetas(source.getMethodMetas()))
                .dependencyList(this.fetchDependencies(source.getDependencyList()))
                .build();


        return null;
    }


    @Override
    protected TypeScriptFieldMeta[] converterFieldMetas(JavaFieldMeta[] fieldMetas) {

        Arrays.stream(fieldMetas).map(m -> {


//            TypeScriptFieldMeta fieldMeta = TypeScriptFieldMeta.builder()
//                    .classType(this.typeMapping.mapping(m.getTypes()))
//                    .build();


            return null;
        });


        return null;
    }


    @Override
    protected TypeScriptMethodMeta[] converterMethodMetas(JavaMethodMeta[] methodMetas) {

        return null;
    }

    @Override
    protected Set<TypeScriptClassMeta> fetchDependencies(Set<Class<?>> classes) {
        return null;
    }
}
