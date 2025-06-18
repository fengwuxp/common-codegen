package com.wuxp.codegen;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.javadoc.Javadoc;
import com.github.javaparser.javadoc.JavadocBlockTag;
import com.github.javaparser.javadoc.description.JavadocDescriptionElement;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

/**
 * use junit5 test
 */
@Slf4j
class SourceCodeProviderTests {


    private final SourceCodeProvider sourceCodeProvider = new SourceCodeProvider();

    /**
     * test enum
     */
    enum TestEnum {
        test_v1,
        test_v2;
    }

    @Test
    void getTypeDeclaration() {
        Optional<ClassOrInterfaceDeclaration> typeDeclaration = sourceCodeProvider.getTypeDeclaration(SourceCodeProviderTests.class);
        Assertions.assertTrue(typeDeclaration.isPresent());
    }

    @Test
    void getClassDeclaration() {
        Optional<ClassOrInterfaceDeclaration> classDeclaration = sourceCodeProvider.getInterfaceDeclaration(SourceCodeProviderTests.class);
        Assertions.assertFalse(classDeclaration.isPresent());
    }

    @Test
    void getEnumDeclaration() {
        Optional<EnumDeclaration> annotationDeclaration = sourceCodeProvider.getEnumDeclaration(TestEnum.class);
        Assertions.assertTrue(annotationDeclaration.isPresent());
    }

    @Test
    void getAnnotationDeclaration() {
        Optional<AnnotationDeclaration> annotationDeclaration = sourceCodeProvider.getAnnotationDeclaration(Test.class);
        Assertions.assertTrue(annotationDeclaration.isPresent());
    }

    @Test
    void getCompilationUnit() {
        Optional<CompilationUnit> compilationUnit = sourceCodeProvider.getCompilationUnit(SourceCodeProviderTests.class);
        Assertions.assertTrue(compilationUnit.isPresent());
    }

    @Test
    void getFieldDeclaration() throws Exception{
        Optional<FieldDeclaration> fieldDeclaration = sourceCodeProvider.getFieldDeclaration(ExampleSourceObject.class.getField("name"));
        Assertions.assertTrue(fieldDeclaration.isPresent());
    }

    @Test
    void getMethodDeclaration() throws Exception{
        Optional<MethodDeclaration> methodDeclaration = sourceCodeProvider.getMethodDeclaration(ExampleSourceObject.class.getMethod("setName", String.class));
        Assertions.assertTrue(methodDeclaration.isPresent());
        MethodDeclaration declaration = methodDeclaration.get();
        Optional<Comment> commentOptional = declaration.getComment();
        Assertions.assertTrue(commentOptional.isPresent());
        Comment comment = commentOptional.get();
        Javadoc javadoc = comment.asJavadocComment().parse();
        List<JavadocBlockTag> blockTags = javadoc.getBlockTags();
        Assertions.assertNotNull(blockTags);
        List<JavadocDescriptionElement> elements = javadoc.getDescription().getElements();
        Assertions.assertNotNull(elements);
    }

    @Test
    void getMethodParameter() throws Exception{
        Method getName = ExampleSourceObject.class.getMethod("setName", String.class);
        Optional<Parameter> methodParameter = sourceCodeProvider.getMethodParameter(getName.getParameters()[0]);
        Assertions.assertTrue(methodParameter.isPresent());
    }

    @Test
    void getEnumConstantDeclaration()throws Exception{
        Optional<EnumConstantDeclaration> constantDeclaration = sourceCodeProvider.getEnumConstantDeclaration(TestEnum.class.getField("test_v1"));
        Assertions.assertTrue(constantDeclaration.isPresent());
    }
}
