package com.wuxp.codegen.starter;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.CodegenBuilder;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import com.wuxp.codegen.starter.enums.OpenApiType;
import com.wuxp.codegen.swagger2.builder.Swagger2FeignDartCodegenBuilder;
import com.wuxp.codegen.swagger2.builder.Swagger2FeignJavaCodegenBuilder;
import com.wuxp.codegen.swagger2.builder.Swagger2FeignTypescriptCodegenBuilder;
import com.wuxp.codegen.swagger3.builder.Swagger3FeignDartCodegenBuilder;
import com.wuxp.codegen.swagger3.builder.Swagger3FeignJavaCodegenBuilder;
import com.wuxp.codegen.swagger3.builder.Swagger3FeignTypescriptCodegenBuilder;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

/**
 * 按照约定的规则和自动探测，创建{@link com.wuxp.codegen.core.CodeGenerator}对象
 *
 * @author wuxp
 */
public final class DragonSdkCodeGenerator implements CodeGenerator {


    private static final List<String> DEFAULT_OUT_PATHS = Arrays.asList("sdk", "dragon");

    private static final List<String> OPEN_API__CLASSES = Arrays.asList(
            "io.swagger.annotations.Api",
            "io.swagger.v3.oas.annotations.OpenAPIDefinition"
    );

    private static final List<OpenApiType> OPEN_API_TYPES = Arrays.asList(
            OpenApiType.SWAGGER_2,
            OpenApiType.SWAGGER_3
    );


    private static OpenApiType OPEN_API_TYPE;


    static {
        for (int i = 0; i < OPEN_API__CLASSES.size(); i++) {
            String className = OPEN_API__CLASSES.get(i);
            try {
                Class.forName(className);
                OPEN_API_TYPE = OPEN_API_TYPES.get(i);
            } catch (ClassNotFoundException ignored) {
            }
        }
        if (OPEN_API_TYPE == null) {
            OPEN_API_TYPE = OpenApiType.DEFAULT;
        }
    }

    private final String[] scanPackages;

    public DragonSdkCodeGenerator() {
        scanPackages = new String[0];
    }

    public DragonSdkCodeGenerator(String... scanPackages) {
        this.scanPackages = scanPackages;
    }


    @Override
    public void generate() {
        getCodeGeneratorBuilders().forEach(codegenBuilder -> {
            codegenBuilder.buildCodeGenerator().generate();
        });
    }

    public Collection<CodegenBuilder> getCodeGeneratorBuilders() {
        switch (OPEN_API_TYPE) {
            case SWAGGER_2:
                return getCodeSwagger2GeneratorBuilders();
            case SWAGGER_3:
                return getCodeSwagger3GeneratorBuilders();
            case DEFAULT:
            default:
                return Collections.emptyList();
        }
    }


    protected Collection<CodegenBuilder> getCodeSwagger2GeneratorBuilders() {


        List<CodegenBuilder> codeGenerators = new ArrayList<>();
        codeGenerators.add(Swagger2FeignTypescriptCodegenBuilder.builder()
                .scanPackages(scanPackages)
                .isDeletedOutputDirectory(false)
                .languageDescription(LanguageDescription.TYPESCRIPT)
                .clientProviderType(ClientProviderType.TYPESCRIPT_FEIGN)
                .outPath(this.getOuPath(ClientProviderType.TYPESCRIPT_FEIGN)));
        codeGenerators.add(Swagger2FeignTypescriptCodegenBuilder.builder()
                .scanPackages(scanPackages)
                .isDeletedOutputDirectory(false)
                .languageDescription(LanguageDescription.TYPESCRIPT)
                .clientProviderType(ClientProviderType.UMI_REQUEST)
                .outPath(this.getOuPath(ClientProviderType.UMI_REQUEST)));
        codeGenerators.add(
                Swagger2FeignDartCodegenBuilder.builder()
                        .scanPackages(scanPackages)
                        .isDeletedOutputDirectory(false)
                        .outPath(this.getOuPath(ClientProviderType.DART_FEIGN))
        );
        codeGenerators.add(
                Swagger2FeignJavaCodegenBuilder.builder()
                        .build()
                        .scanPackages(scanPackages)
                        .isDeletedOutputDirectory(false)
                        //设置基础数据类型的映射关系
                        .baseTypeMapping(CommonsMultipartFile.class, JavaCodeGenClassMeta.FILE)
                        //自定义的类型映射
                        .languageDescription(LanguageDescription.JAVA)
                        .clientProviderType(ClientProviderType.SPRING_CLOUD_OPENFEIGN)
                        .outPath(this.getOuPath(ClientProviderType.SPRING_CLOUD_OPENFEIGN)));
        codeGenerators.add(
                Swagger2FeignJavaCodegenBuilder.builder()
                        .useRxJava(true)
                        .build()
                        .scanPackages(scanPackages)
                        .isDeletedOutputDirectory(false)
                        // 基础类型映射
                        .baseTypeMapping(CommonsMultipartFile.class, JavaCodeGenClassMeta.FILE)
                        //自定义的类型映射
                        .languageDescription(LanguageDescription.JAVA_ANDROID)
                        .clientProviderType(ClientProviderType.RETROFIT)
                        .outPath(this.getOuPath(ClientProviderType.RETROFIT)));

        return codeGenerators;
    }

    protected Collection<CodegenBuilder> getCodeSwagger3GeneratorBuilders() {


        List<CodegenBuilder> codeGenerators = new ArrayList<>();
        codeGenerators.add(Swagger3FeignTypescriptCodegenBuilder.builder()
                .scanPackages(scanPackages)
                .isDeletedOutputDirectory(false)
                .languageDescription(LanguageDescription.TYPESCRIPT)
                .clientProviderType(ClientProviderType.TYPESCRIPT_FEIGN)
                .outPath(this.getOuPath(ClientProviderType.TYPESCRIPT_FEIGN)));
        codeGenerators.add(Swagger3FeignTypescriptCodegenBuilder.builder()
                .scanPackages(scanPackages)
                .isDeletedOutputDirectory(false)
                .languageDescription(LanguageDescription.TYPESCRIPT)
                .clientProviderType(ClientProviderType.UMI_REQUEST)
                .outPath(this.getOuPath(ClientProviderType.UMI_REQUEST)));
        codeGenerators.add(
                Swagger3FeignDartCodegenBuilder.builder()
                        .scanPackages(scanPackages)
                        .isDeletedOutputDirectory(false)
                        .outPath(this.getOuPath(ClientProviderType.DART_FEIGN)));
        codeGenerators.add(
                Swagger3FeignJavaCodegenBuilder.builder()
                        .build()
                        .scanPackages(scanPackages)
                        .isDeletedOutputDirectory(false)
                        //设置基础数据类型的映射关系
                        .baseTypeMapping(CommonsMultipartFile.class, JavaCodeGenClassMeta.FILE)
                        //自定义的类型映射
                        .languageDescription(LanguageDescription.JAVA)
                        .clientProviderType(ClientProviderType.SPRING_CLOUD_OPENFEIGN)
                        .outPath(this.getOuPath(ClientProviderType.SPRING_CLOUD_OPENFEIGN)));
        codeGenerators.add(
                Swagger3FeignJavaCodegenBuilder.builder()
                        .useRxJava(true)
                        .build()
                        .scanPackages(scanPackages)
                        .isDeletedOutputDirectory(false)
                        // 基础类型映射
                        .baseTypeMapping(CommonsMultipartFile.class, JavaCodeGenClassMeta.FILE)
                        //自定义的类型映射
                        .languageDescription(LanguageDescription.JAVA_ANDROID)
                        .clientProviderType(ClientProviderType.RETROFIT)
                        .outPath(this.getOuPath(ClientProviderType.RETROFIT)));

        return codeGenerators;
    }

    private String getOuPath(ClientProviderType type) {

        List<String> outPaths = new ArrayList<>(DEFAULT_OUT_PATHS);
        outPaths.add(OPEN_API_TYPE.name().toLowerCase());
        outPaths.add(type.name().toLowerCase());
        if (ClientProviderType.DART_FEIGN.equals(type)) {
            outPaths.add("lib");
        }
        outPaths.add("src");
        return Paths.get(System.getProperty("user.dir")).resolveSibling(String.join(File.separator, outPaths)).toString();
    }

    public static String getBaseOutPath() {
        return Paths.get(System.getProperty("user.dir")).resolveSibling(String.join(File.separator, DEFAULT_OUT_PATHS)).toString();
    }

}
