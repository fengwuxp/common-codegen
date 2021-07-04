package com.wuxp.codegen.comment;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.JavadocBlockTag;
import com.github.javaparser.javadoc.description.JavadocDescriptionElement;
import com.wuxp.codegen.SourceCodeProvider;
import com.wuxp.codegen.core.CodeGenCommentExtractor;
import com.wuxp.codegen.core.parser.JavaClassParser;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 通过source code javadoc增强代码生成的注释
 *
 * @author wuxp
 */
public class SourceCodeGenCommentExtractor implements CodeGenCommentExtractor {

    private final SourceCodeProvider sourceCodeProvider;

    public SourceCodeGenCommentExtractor() {
        sourceCodeProvider = new SourceCodeProvider();
    }

    @Override
    public String toComment(Class<?> clazz) {
        return sourceCodeProvider.getTypeDeclaration(clazz)
                .flatMap(Node::getComment)
                .map(this::getDesc)
                .orElse(null);
    }

    @Override
    public String toComment(Field field) {
        Optional<Optional<Comment>> optionalComment;
        if (field.isEnumConstant()) {
            optionalComment = sourceCodeProvider.getEnumConstantDeclaration(field).map(EnumConstantDeclaration::getComment);
        } else {
            optionalComment = sourceCodeProvider.getFieldDeclaration(field).map(FieldDeclaration::getComment);
        }
        return optionalComment
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(comment -> {
                    String result = getCommentByTag(comment, JavadocBlockTag.Type.SERIAL_FIELD);
                    if (result != null) {
                        return result;
                    }
                    return getDesc(comment);
                }).orElse(null);
    }

    @Override
    public String toComment(Parameter parameter) {
        String parameterName = JavaClassParser.getParameterName(parameter);
        return sourceCodeProvider.getMethodDeclaration((Method) parameter.getDeclaringExecutable())
                .flatMap(declaration -> declaration
                        .getComment()
                        .map(comment -> getCommentByTag(comment, JavadocBlockTag.Type.PARAM, parameterName)))
                .orElse(null);
    }

    @Override
    public String toComment(Method method) {
        return sourceCodeProvider.getMethodDeclaration(method)
                .map(MethodDeclaration::getComment)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(comment -> String.join(MULTILINE_COMMENT_TAG, this.getDesc(comment), this.getCommentByTag(comment, JavadocBlockTag.Type.RETURN)))
                .orElse(null);
    }

    /**
     * 获取描述注释
     *
     * @param comment 注释描述
     * @return 注释内容
     */
    private String getDesc(Comment comment) {
        if (comment.isJavadocComment()) {
            Javadoc javadoc = comment.asJavadocComment().parse();
            return javadoc.getDescription().getElements().stream()
                    .map(JavadocDescriptionElement::toText).collect(Collectors.joining(""));
        }
        return comment.getContent();
    }

    /**
     * 通过注解标签 名称获取注释
     *
     * @param comment 注释描述
     * @param tag     标签类型
     * @return 注释内容
     */
    private String getCommentByTag(Comment comment, JavadocBlockTag.Type tag) {
        return getCommentByTag(comment, tag, null);
    }

    /**
     * 通过注解标签 名称获取注释
     *
     * @param comment 注释描述
     * @param tag     标签类型
     * @param name    名称
     * @return 注释内容
     */
    private String getCommentByTag(Comment comment, JavadocBlockTag.Type tag, String name) {
        if (comment.isJavadocComment()) {
            Javadoc javadoc = comment.asJavadocComment().parse();
            List<JavadocBlockTag> blockTags = javadoc.getBlockTags();
            return blockTags.stream()
                    .filter(blockTag -> tag.equals(blockTag.getType()))
                    .filter(blockTag -> {
                        if (blockTag.getName().isPresent() && name != null) {
                            return blockTag.getName().get().equals(name);
                        }
                        return true;
                    })
                    .filter(blockTag-> StringUtils.hasText(blockTag.getContent().toText()))
                    .map(JavadocBlockTag::toText)
                    .findFirst()
                    .orElse("");
        }
        return "";
    }
}
