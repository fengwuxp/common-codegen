package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.parser.LanguageAnnotationParser;
import com.wuxp.codegen.core.parser.LanguageElementDefinitionPublishSourceParser;
import com.wuxp.codegen.model.CommonBaseMeta;
import com.wuxp.codegen.model.CommonCodeGenAnnotation;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.AnnotatedElement;

/**
 * 将解析委托给 {@link #delegate}
 *
 * @author wuxp
 */
@Getter
public abstract class DelegateLanguagePublishParser implements LanguageElementDefinitionPublishSourceParser, LanguageAnnotationParser {

    private final LanguageTypeDefinitionPublishParser<CommonCodeGenClassMeta> delegate;

    @Setter
    private JavaTypeMapper javaTypeMapper;

    @SuppressWarnings("unchecked")
    protected DelegateLanguagePublishParser(LanguageTypeDefinitionPublishParser<? extends CommonCodeGenClassMeta> delegate) {
        this.delegate = (LanguageTypeDefinitionPublishParser<CommonCodeGenClassMeta>) delegate;
    }

    @Override
    public <C extends CommonBaseMeta> C publishParse(Object source) {
        return getDelegatePublishParser().publishParse(source);
    }

    @Override
    public CommonCodeGenAnnotation[] parseAnnotatedElement(AnnotatedElement annotationOwner) {
        return getDelegatePublishParser().parseAnnotatedElement(annotationOwner);
    }

    public LanguageTypeDefinitionPublishParser<CommonCodeGenClassMeta> getDelegatePublishParser() {
        return delegate;
    }

    protected Class<?>[] mappingClasses(Class<?>... sources) {
        if (javaTypeMapper == null) {
            return sources;
        }
        return javaTypeMapper.mappingClasses(sources);
    }

}
