package com.wuxp.codegen.languages;

import com.wuxp.codegen.annotations.LanguageAnnotationParser;
import com.wuxp.codegen.comment.LanguageCommentDefinitionDescriber;
import com.wuxp.codegen.core.parser.LanguageFieldDefinitionParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.languages.java.JavaFieldMeta;
import lombok.extern.slf4j.Slf4j;

import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author wuxp
 */
@Slf4j
public abstract class AbstractLanguageFieldDefinitionParser<F extends CommonCodeGenFiledMeta> implements LanguageFieldDefinitionParser<F> {

    @Override
    public F parse(JavaFieldMeta fieldMeta) {
        F result = newElementInstance();
        result.setSource(fieldMeta.getField());
        result.setAccessPermission(fieldMeta.getAccessPermission());
        result.setEnumConstant(fieldMeta.getIsEnumConstant());
        result.setComments(extractComments(fieldMeta));
        result.setAnnotations(LanguageAnnotationParser.getInstance().parse(fieldMeta.getField()));
        result.setFiledTypes(getFiledTypes(fieldMeta));
        result.setTypeVariables(getFieldTypeVariables(fieldMeta).toArray(new CommonCodeGenClassMeta[0]));
        return postProcess(result);
    }

    private String[] extractComments(JavaFieldMeta fieldMeta) {
        // 注解转注释
        List<String> comments = LanguageCommentDefinitionDescriber.extractComments(fieldMeta.getField());
        comments.addAll(LanguageCommentDefinitionDescriber.extractComments(ElementType.FIELD, fieldMeta.getTypes()));
        return comments.toArray(new String[]{});
    }

    private CommonCodeGenClassMeta[] getFiledTypes(JavaFieldMeta fieldMeta) {
        List<CommonCodeGenClassMeta> results = this.dispatch(Arrays.asList(fieldMeta.getTypes()));
        List<CommonCodeGenClassMeta> fieldTypeVariables = getFieldTypeVariables(fieldMeta);
        if (!fieldTypeVariables.isEmpty() && results.size() >= fieldTypeVariables.size()) {
            // 存在泛型描述对象，将默认的泛型描述移除
            int size = results.size() - fieldTypeVariables.size();
            results = new ArrayList<>(results).subList(0, size);
        }
        results.addAll(fieldTypeVariables);
        return results.toArray(new CommonCodeGenClassMeta[0]);
    }

    private List<CommonCodeGenClassMeta> getFieldTypeVariables(JavaFieldMeta fieldMeta) {
        return parseTypeVariables(Arrays.asList(fieldMeta.getTypes()));
    }

}
