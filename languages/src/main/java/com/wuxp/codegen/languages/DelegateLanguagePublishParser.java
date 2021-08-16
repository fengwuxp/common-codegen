package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.parser.LanguageAnnotationParser;
import com.wuxp.codegen.core.parser.LanguageElementDefinitionPublishSourceParser;
import com.wuxp.codegen.model.CommonBaseMeta;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;

import java.lang.reflect.AnnotatedElement;

public abstract class DelegateLanguagePublishParser implements LanguageElementDefinitionPublishSourceParser, LanguageAnnotationParser {

    private final LanguageTypeDefinitionPublishParser<? extends CommonCodeGenClassMeta> publishSourceParser;

    private JavaTypeMapper javaTypeMapper;

    protected DelegateLanguagePublishParser(LanguageTypeDefinitionPublishParser<? extends CommonCodeGenClassMeta> publishSourceParser) {
        this.publishSourceParser = publishSourceParser;
    }

    @Override
    public <C extends CommonBaseMeta> C publishParse(Object source) {
        return getDelegatePublishParser().publishParse(source);
    }

    @Override
    public CommonCodeGenAnnotation[] parseAnnotatedElement(AnnotatedElement annotationOwner) {
        return getDelegatePublishParser().parseAnnotatedElement(annotationOwner);
    }

    public LanguageTypeDefinitionPublishParser<? extends CommonCodeGenClassMeta> getDelegatePublishParser() {
        return publishSourceParser;
    }

    protected Class<?>[] mappingClasses(Class<?>... sources) {
        if (javaTypeMapper == null) {
            return sources;
        }
        return javaTypeMapper.mappingClasses(sources);
    }

    public void setJavaTypeMapper(JavaTypeMapper javaTypeMapper) {
        this.javaTypeMapper = javaTypeMapper;
    }
}
