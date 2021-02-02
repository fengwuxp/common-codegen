package com.wuxp.codegen.swagger3.example;

import com.github.javaparser.ast.body.TypeDeclaration;
import com.wuxp.codegen.SourceCodeProvider;
import com.wuxp.codegen.swagger3.example.maven.controller.UserController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class SourceCodeProviderTest {

    @Test
    void testGetTypeDeclaration() {
        SourceCodeProvider sourceCodeProvider = new SourceCodeProvider();
        Optional<TypeDeclaration> typeDeclaration = sourceCodeProvider.getTypeDeclaration(UserController.class);
        Assertions.assertTrue(typeDeclaration.isPresent());
    }
}
