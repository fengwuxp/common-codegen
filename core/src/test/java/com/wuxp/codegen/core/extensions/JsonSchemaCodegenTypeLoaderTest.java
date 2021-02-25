package com.wuxp.codegen.core.extensions;

import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.core.util.PathResolveUtils;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.LanguageDescription;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

class JsonSchemaCodegenTypeLoaderTest {

    String dirPath = String.join(File.separator, PathResolveUtils.relative(System.getProperty("user.dir"),"../model"), "src", "main", "resources", "codegen-meta-extensions");

    PackageMapStrategy packageMapStrategy = new PackageMapStrategy() {
        @Override
        public String convert(Class<?> clazz) {
            return null;
        }

        @Override
        public String convertClassName(Class<?> clazz) {
            return null;
        }

        @Override
        public String genPackagePath(String[] uris) {
            return null;
        }
    };

    @Test
    void testLoadTypescript() {

        JsonSchemaCodegenTypeLoader jsonSchemaCodegenTypeLoader = new JsonSchemaCodegenTypeLoader(dirPath, LanguageDescription.TYPESCRIPT, packageMapStrategy);
        List<CommonCodeGenClassMeta> genClassMetas = jsonSchemaCodegenTypeLoader.load();
        Assertions.assertEquals(3, genClassMetas.size());
    }

    @Test
    void testLoadDart() {
        JsonSchemaCodegenTypeLoader jsonSchemaCodegenTypeLoader = new JsonSchemaCodegenTypeLoader(dirPath, LanguageDescription.DART, packageMapStrategy);
        List<CommonCodeGenClassMeta> genClassMetas = jsonSchemaCodegenTypeLoader.load();
        Assertions.assertEquals(3, genClassMetas.size());
    }

    @Test
    void testLoadJava() {
        JsonSchemaCodegenTypeLoader jsonSchemaCodegenTypeLoader = new JsonSchemaCodegenTypeLoader(dirPath, LanguageDescription.JAVA, packageMapStrategy);
        List<CommonCodeGenClassMeta> genClassMetas = jsonSchemaCodegenTypeLoader.load();
        Assertions.assertEquals(3, genClassMetas.size());
    }
}