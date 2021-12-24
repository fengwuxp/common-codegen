package com.wuxp.codegen.starter;

import com.wuxp.codegen.AbstractLoongCodegenBuilder;
import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.CodegenBuilder;
import com.wuxp.codegen.core.util.PathResolveUtils;
import com.wuxp.codegen.loong.CodegenSdkUploader;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import com.wuxp.codegen.starter.enums.OpenApiType;
import com.wuxp.codegen.swagger2.builder.Swagger2FeignDartCodegenBuilder;
import com.wuxp.codegen.swagger2.builder.Swagger2FeignJavaCodegenBuilder;
import com.wuxp.codegen.swagger2.builder.Swagger2FeignTypescriptCodegenBuilder;
import com.wuxp.codegen.swagger3.builder.Swagger3FeignDartCodegenBuilder;
import com.wuxp.codegen.swagger3.builder.Swagger3FeignJavaCodegenBuilder;
import com.wuxp.codegen.swagger3.builder.Swagger3FeignTypescriptCodegenBuilder;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.util.*;

/**
 * 按照约定的规则和自动探测，创建{@link com.wuxp.codegen.core.CodeGenerator}对象
 *
 * @author wuxp
 */
@Slf4j
public final class LoongCodeGenerator implements CodeGenerator {

    private static final List<String> DEFAULT_OUT_PATHS = Arrays.asList("codegen-sdk", "loong");

    @Setter
    private String[] scanPackages;

    @Setter
    private OpenApiType openApiType;

    /**
     * 忽略的包
     */
    @Setter
    private List<String> ignorePackages = new ArrayList<>();

    /**
     * 需要忽略的类
     */
    @Setter
    private List<Class<?>> ignoreClasses = new ArrayList<>();

    /**
     * 默认输出路径为当前文件夹
     */
    @Setter
    private String outputPath = null;

    /**
     * 支持生成的{@link ClientProviderType}
     */
    @Setter
    private List<ClientProviderType> clientProviderTypes;


    public LoongCodeGenerator() {
        this(OpenApiTypeExplorer.getDefaultOpenApiType(), new String[0]);
    }

    public LoongCodeGenerator(OpenApiType openApiType) {
        this(openApiType, new String[0]);
    }

    public LoongCodeGenerator(String... scanPackages) {
        this(OpenApiTypeExplorer.getDefaultOpenApiType(), scanPackages);
    }

    public LoongCodeGenerator(OpenApiType openApiType, String... scanPackages) {
        this.scanPackages = scanPackages;
        this.openApiType = openApiType;
    }

    @Override
    public void generate() {

        if (log.isInfoEnabled()) {
            log.info("sdk codegen args: openApiType={},scanPackages={}", openApiType, scanPackages);
        }

        Collection<CodegenBuilder> codeGeneratorBuilders = getCodeGeneratorBuilders();
        if (log.isInfoEnabled()) {
            log.info("codeGeneratorBuilders：{}", codeGeneratorBuilders);
        }
        codeGeneratorBuilders.stream().map(AbstractLoongCodegenBuilder.class::cast)
                .forEach(codegenBuilder -> codegenBuilder
                        .ignoreClasses(ignoreClasses.toArray(new Class[0]))
                        .ignorePackages(ignorePackages.toArray(new String[0]))
                        .buildCodeGenerator().generate());
        // 上传sdk生成结果到服务端
        new CodegenSdkUploader(this.getCodegenBaseOutputPath()).upload();
    }

    public Collection<CodegenBuilder> getCodeGeneratorBuilders() {

        Collection<ClientProviderType> finallyClientProviderTypes = getFinallyClientProviderTypes();

        if (log.isInfoEnabled()) {
            log.info("finallyClientProviderTypes：{}", finallyClientProviderTypes);
        }

        switch (openApiType) {
            case SWAGGER_2:
                return getCodeSwagger2GeneratorBuilders();
            case SWAGGER_3:
                return getCodeSwagger3GeneratorBuilders();
            case DEFAULT:
            default:
                return Collections.emptyList();
        }
    }


    private Collection<CodegenBuilder> getCodeSwagger2GeneratorBuilders() {
        Collection<ClientProviderType> finallyClientProviderTypes = getFinallyClientProviderTypes();
        List<CodegenBuilder> codeGenerators = new ArrayList<>(8);
        if (finallyClientProviderTypes.contains(ClientProviderType.TYPESCRIPT_FEIGN)) {
            codeGenerators.add(Swagger2FeignTypescriptCodegenBuilder.builder()
                    .scanPackages(scanPackages)
                    .isDeletedOutputDirectory(false)
                    .languageDescription(LanguageDescription.TYPESCRIPT)
                    .clientProviderType(ClientProviderType.TYPESCRIPT_FEIGN)
                    .outPath(this.getCodegenOutputPath(ClientProviderType.TYPESCRIPT_FEIGN)));
        }
        if (finallyClientProviderTypes.contains(ClientProviderType.UMI_REQUEST)) {
            codeGenerators.add(Swagger2FeignTypescriptCodegenBuilder.builder()
                    .scanPackages(scanPackages)
                    .isDeletedOutputDirectory(false)
                    .languageDescription(LanguageDescription.TYPESCRIPT)
                    .clientProviderType(ClientProviderType.UMI_REQUEST)
                    .outPath(this.getCodegenOutputPath(ClientProviderType.UMI_REQUEST)));
        }
        if (finallyClientProviderTypes.contains(ClientProviderType.DART_FEIGN)) {
            codeGenerators.add(
                    Swagger2FeignDartCodegenBuilder.builder()
                            .scanPackages(scanPackages)
                            .isDeletedOutputDirectory(false)
                            .outPath(this.getCodegenOutputPath(ClientProviderType.DART_FEIGN))
            );
        }
        if (finallyClientProviderTypes.contains(ClientProviderType.SPRING_CLOUD_OPENFEIGN)) {
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
                            .outPath(this.getCodegenOutputPath(ClientProviderType.SPRING_CLOUD_OPENFEIGN)));
        }
        if (finallyClientProviderTypes.contains(ClientProviderType.RETROFIT)) {
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
                            .outPath(this.getCodegenOutputPath(ClientProviderType.RETROFIT)));
        }

        return codeGenerators;
    }

    private Collection<CodegenBuilder> getCodeSwagger3GeneratorBuilders() {
        Collection<ClientProviderType> finallyClientProviderTypes = getFinallyClientProviderTypes();
        List<CodegenBuilder> codeGenerators = new ArrayList<>(8);
        if (finallyClientProviderTypes.contains(ClientProviderType.TYPESCRIPT_FEIGN)) {
            codeGenerators.add(Swagger3FeignTypescriptCodegenBuilder.builder()
                    .scanPackages(scanPackages)
                    .isDeletedOutputDirectory(false)
                    .languageDescription(LanguageDescription.TYPESCRIPT)
                    .clientProviderType(ClientProviderType.TYPESCRIPT_FEIGN)
                    .outPath(this.getCodegenOutputPath(ClientProviderType.TYPESCRIPT_FEIGN)));
        }
        if (finallyClientProviderTypes.contains(ClientProviderType.UMI_REQUEST)) {
            codeGenerators.add(Swagger3FeignTypescriptCodegenBuilder.builder()
                    .scanPackages(scanPackages)
                    .isDeletedOutputDirectory(false)
                    .languageDescription(LanguageDescription.TYPESCRIPT)
                    .clientProviderType(ClientProviderType.UMI_REQUEST)
                    .outPath(this.getCodegenOutputPath(ClientProviderType.UMI_REQUEST)));
        }
        if (finallyClientProviderTypes.contains(ClientProviderType.DART_FEIGN)) {
            codeGenerators.add(
                    Swagger3FeignDartCodegenBuilder.builder()
                            .scanPackages(scanPackages)
                            .isDeletedOutputDirectory(false)
                            .outPath(this.getCodegenOutputPath(ClientProviderType.DART_FEIGN)));
        }
        if (finallyClientProviderTypes.contains(ClientProviderType.SPRING_CLOUD_OPENFEIGN)) {
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
                            .outPath(this.getCodegenOutputPath(ClientProviderType.SPRING_CLOUD_OPENFEIGN)));
        }
        if (finallyClientProviderTypes.contains(ClientProviderType.RETROFIT)) {
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
                            .outPath(this.getCodegenOutputPath(ClientProviderType.RETROFIT)));
        }

        return codeGenerators;
    }

    private String getCodegenBaseOutputPath() {
        String codegenOutputPath = this.getCodegenOutputPath(ClientProviderType.RETROFIT);
        return codegenOutputPath.split(String.join("", File.separator, ClientProviderType.RETROFIT.name().toLowerCase(), File.separator))[0];
    }

    private String getCodegenOutputPath(ClientProviderType type) {
        String baseDir;
        if (outputPath != null && outputPath.startsWith(File.separator)) {
            // 绝对路径
            baseDir = FilenameUtils.normalizeNoEndSeparator(outputPath);
        } else {
            // 默认输出到用户目录下
            baseDir = System.getProperty("user.dir");
        }
        List<String> outPaths = new LinkedList<>(DEFAULT_OUT_PATHS);
        outPaths.add(openApiType.name().toLowerCase());
        outPaths.add(type.name().toLowerCase());
        if (ClientProviderType.DART_FEIGN.equals(type)) {
            outPaths.add("lib");
        }
        outPaths.add("src");
        if (StringUtils.hasText(outputPath)) {
            if (outputPath.startsWith(".")) {
                // 相对路径
                baseDir = baseDir + File.separator + outputPath;
            }
            String ref = String.join(File.separator, outPaths);
            return PathResolveUtils.relative(baseDir, ref);
        } else {
            // 默认
            return Paths.get(baseDir).resolveSibling(String.join(File.separator, outPaths)).toString();
        }
    }

    private Collection<ClientProviderType> getFinallyClientProviderTypes() {
        if (clientProviderTypes == null || clientProviderTypes.isEmpty()) {
            clientProviderTypes = Arrays.asList(ClientProviderType.values());
        }
        return clientProviderTypes;
    }

    public static String getBaseOutputPath() {
        return Paths.get(System.getProperty("user.dir")).resolveSibling(String.join(File.separator, DEFAULT_OUT_PATHS)).toString();
    }


}
