package com.wuxp.codegen.comment;

import com.wuxp.codegen.comment.CodeGenCommentExtractorFactory;
import com.wuxp.codegen.core.CodeGenCommentExtractor;
import org.springframework.util.ObjectUtils;

import java.lang.annotation.ElementType;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wuxp
 */
public final class LanguageCommentDefinitionDescriber {

    private LanguageCommentDefinitionDescriber() {
        throw new AssertionError();
    }

    /**
     * 通过注解获取注释
     *
     * @param annotationOwner 注解所有者
     * @return 注释列表
     */
    public static List<String> extractComments(AnnotatedElement annotationOwner) {
        return CodeGenCommentExtractorFactory.getInstance().factory(annotationOwner.getAnnotations()).toComments(annotationOwner);
    }

    /**
     * 通过类类型来获取注释
     *
     * @param elementType 元素类型
     * @param classes     类列表
     * @return 注释列表
     */
    public static List<String> extractComments(ElementType elementType, Class<?>[] classes) {
        if (ObjectUtils.isEmpty(classes)) {
            return new ArrayList<>();
        }
        CodeGenCommentExtractor codeGenCommentExtractor = CodeGenCommentExtractorFactory.getInstance().factory(elementType);
        return Arrays.stream(classes)
                .filter(Objects::nonNull)
                .map(codeGenCommentExtractor::toComment)
                .collect(Collectors.toList());
    }
}
