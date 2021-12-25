package com.wuxp.codegen.core;

import org.springframework.util.StringUtils;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wuxp
 */
public class CombineCodeGenCommentExtractor implements CodeGenCommentExtractor {

    private final Collection<CodeGenCommentExtractor> codeGenCommentEnhancers;

    private CombineCodeGenCommentExtractor(Collection<CodeGenCommentExtractor> codeGenCommentEnhancers) {
        this.codeGenCommentEnhancers = codeGenCommentEnhancers;
    }

    public static CombineCodeGenCommentExtractor of(CodeGenCommentExtractor... codeGenCommentEnhancers) {
        return of(Arrays.asList(codeGenCommentEnhancers));
    }

    public static CombineCodeGenCommentExtractor of(Collection<CodeGenCommentExtractor> codeGenCommentEnhancers) {
        return new CombineCodeGenCommentExtractor(codeGenCommentEnhancers);
    }

    @Override
    public List<String> toComments(AnnotatedElement element) {
        return codeGenCommentEnhancers.stream()
                .map(codeGenCommentEnhancer -> codeGenCommentEnhancer.toComments(element))
                .flatMap(Collection::stream)
                .filter(StringUtils::hasText)
                .map(this::removeLineBreak)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public String toComment(AnnotatedElement element) {
        return String.join("\n", toComments(element));
//        return String.join(",", toComments(element));
    }

    @Override
    public String toComment(Class<?> clazz) {
        return codeGenCommentEnhancers.stream()
                .map(codeGenCommentEnhancer -> codeGenCommentEnhancer.toComments(clazz))
                .flatMap(Collection::stream)
                .filter(StringUtils::hasText)
                .map(this::removeLineBreak)
                .distinct()
                .collect(Collectors.joining(","));
    }

    @Override
    public String toComment(Field field) {
        return codeGenCommentEnhancers.stream()
                .map(codeGenCommentEnhancer -> codeGenCommentEnhancer.toComments(field))
                .flatMap(Collection::stream)
                .filter(StringUtils::hasText)
                .map(this::removeLineBreak)
                .distinct()
                .collect(Collectors.joining(","));
    }

    @Override
    public String toComment(Parameter parameter) {
        return codeGenCommentEnhancers.stream()
                .map(codeGenCommentEnhancer -> codeGenCommentEnhancer.toComments(parameter))
                .flatMap(Collection::stream)
                .filter(StringUtils::hasText)
                .map(this::removeLineBreak)
                .distinct()
                .collect(Collectors.joining(","));
    }

    @Override
    public String toComment(Method method) {
        return codeGenCommentEnhancers.stream()
                .map(codeGenCommentEnhancer -> codeGenCommentEnhancer.toComments(method))
                .flatMap(Collection::stream)
                .filter(StringUtils::hasText)
                .map(this::removeLineBreak)
                .distinct()
                .collect(Collectors.joining(","));
    }

    private String removeLineBreak(String comment) {
        return comment.replace("\n", "")
                .replace("\r", "")
                .replace("\t", "");
    }
}
