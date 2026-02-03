package com.wuxp.codegen.languages;

import com.wuxp.codegen.comment.LanguageCommentDefinitionDescriber;
import com.wuxp.codegen.core.parser.LanguageFieldDefinitionParser;
import com.wuxp.codegen.meta.util.CodegenAnnotationUtils;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.util.ObjectUtils;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.util.*;

/**
 * @author wuxp
 */
@Slf4j
public abstract class AbstractLanguageFieldDefinitionParser<F extends CommonCodeGenFiledMeta> extends DelegateLanguagePublishParser implements LanguageFieldDefinitionParser<F> {

    private static final Set<Class<? extends Annotation>> NOT_NULL_ANNOTATION_TYPES = new HashSet<>(
            Arrays.asList(NotNull.class, NotBlank.class, NotEmpty.class)
    );

    protected AbstractLanguageFieldDefinitionParser(LanguageTypeDefinitionPublishParser<? extends CommonCodeGenClassMeta> publishSourceParser) {
        super(publishSourceParser);
    }

    @Override
    public F parse(JavaFieldMeta fieldMeta) {
        CommonCodeGenClassMeta[] filedTypes = getFiledTypes(fieldMeta);
        if (ObjectUtils.isEmpty(filedTypes)) {
            return null;
        }
        F result = newElementInstance();
        result.setFiledTypes(filedTypes);
        result.setSource(fieldMeta.getField());
        result.setFieldMeta(fieldMeta);
        result.setDeclaringClassMeta(fieldMeta.getDeclaringClassMeta());
        result.setName(fieldMeta.getName());
        result.setAccessPermission(fieldMeta.getAccessPermission());
        result.setEnumConstant(fieldMeta.getIsEnumConstant());
        result.setComments(extractComments(fieldMeta));
        result.setAnnotations(parseAnnotatedElement(fieldMeta.getAnnotatedElement()));
        result.setTypeVariables(getFieldTypeVariables(fieldMeta).toArray(new CommonCodeGenClassMeta[0]));
        if (log.isInfoEnabled()) {
            log.info("*** field {}  {}", fieldMeta.getField(), Arrays.asList(result.getComments()));
        }
        result.setRequired(isRequired(fieldMeta));
        return result;
    }

    private String[] extractComments(JavaFieldMeta fieldMeta) {
        // 注解转注释
        List<String> comments = LanguageCommentDefinitionDescriber.extractComments(fieldMeta.getField());
        comments.addAll(LanguageCommentDefinitionDescriber.extractComments(ElementType.FIELD, fieldMeta.getTypes()));
        return comments.toArray(new String[]{});
    }

    private CommonCodeGenClassMeta[] getFiledTypes(JavaFieldMeta fieldMeta) {
        List<CommonCodeGenClassMeta> results = new ArrayList<>(this.publishParse(Arrays.asList(fieldMeta.getTypes())));
        List<CommonCodeGenClassMeta> fieldTypeVariables = getFieldTypeVariables(fieldMeta);
        if (!fieldTypeVariables.isEmpty() && results.size() >= fieldTypeVariables.size()) {
            // 存在泛型描述对象，将默认的泛型描述移除
            int size = results.size() - fieldTypeVariables.size();
            results = new ArrayList<>(results).subList(0, size);
        }
        results.addAll(fieldTypeVariables);
        return results.toArray(new CommonCodeGenClassMeta[0]);
    }

    @NonNull
    private List<CommonCodeGenClassMeta> getFieldTypeVariables(JavaFieldMeta fieldMeta) {
        if (fieldMeta.getTypeVariables() == null) {
            return Collections.emptyList();
        }
        return parseTypes(Arrays.asList(fieldMeta.getTypeVariables()));
    }

    @SuppressWarnings("unchecked")
    private boolean isRequired(JavaFieldMeta filedMeta) {
        if (ObjectUtils.isEmpty(filedMeta.getAnnotations())) {
            return false;
        }
        return CodegenAnnotationUtils.existAnnotations(filedMeta.getAnnotations(), NOT_NULL_ANNOTATION_TYPES.toArray(new Class[0]))
                || CodegenAnnotationUtils.matchAnnotationsAttribute(filedMeta.getAnnotations(), "required", true);
    }
}
