package com.wuxp.codegen.annotation.processors;

import com.wuxp.codegen.annotation.enums.OpenApiType;
import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.dragon.AbstractCodeGenerator;
import com.wuxp.codegen.dragon.strategy.JavaPackageMapStrategy;
import com.wuxp.codegen.dragon.strategy.TypescriptPackageMapStrategy;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import com.wuxp.codegen.swagger2.builder.Swagger2FeignDartCodegenBuilder;
import com.wuxp.codegen.swagger2.builder.Swagger2FeignJavaCodegenBuilder;
import com.wuxp.codegen.swagger2.builder.Swagger2FeignTypescriptCodegenBuilder;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.File;
import java.lang.annotation.Annotation;
import java.nio.file.Paths;
import java.util.*;


/**
 * 基于Spring Web的sdk生成
 *
 * @author wuxp
 */
@SupportedAnnotationTypes(value = {
        "org.springframework.stereotype.Controller",
        "org.springframework.web.bind.annotation.RestController"
})
public class SpringApiSdkCodegenProcessor extends AbstractProcessor {


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
            OPEN_API_TYPE = OpenApiType.CUSTOM;
        }
    }


    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Messager messager = this.processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.ERROR, "开始执行SpringApiSdkCodegenProcessor");
        for (TypeElement typeElement : annotations) {
            String className = typeElement.getQualifiedName().toString();
            try {
                Class<? extends Annotation> entityAnnotationType = (Class<? extends Annotation>) Class.forName(className);
                processAnnotations(roundEnv.getElementsAnnotatedWith(entityAnnotationType));
            } catch (ClassNotFoundException e) {
                this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, String.format("%s  can't found class %s", getClass().getSimpleName(), className));
            }
        }
        return false;
    }

    protected void processAnnotations(Set<? extends Element> elementList) {
        Messager messager = this.processingEnv.getMessager();

        Collection<CodeGenerator> codeGenerators = this.getCodeGenerators();
        for (Element element : elementList) {
            //只支持对类，接口，注解的处理，对字段不做处理
            if (!element.getKind().isClass() && !element.getKind().isInterface()) {
                continue;
            }
            TypeElement typeElement = (TypeElement) element;
            Class<?> aClass = getClassForType(typeElement);
            codeGen(aClass, codeGenerators);
            messager.printMessage(Diagnostic.Kind.ERROR, "扫描到的类：" + typeElement.getQualifiedName().toString());
        }
    }

    protected void codeGen(Class<?> aClass, Collection<CodeGenerator> codeGenerators) {
        if (aClass == null) {
            return;
        }
        codeGenerators.forEach(codeGenerator -> {
            ((AbstractCodeGenerator) codeGenerator).dragonGenerate(aClass);
        });
    }

    protected Collection<CodeGenerator> getCodeGenerators() {
        switch (OPEN_API_TYPE) {
            case SWAGGER_2:
                return getCodeSwagger2Generators();
            case SWAGGER_3:
            case CUSTOM:
            default:
                return Collections.emptyList();
        }
    }

    protected Collection<CodeGenerator> getCodeSwagger2Generators() {


        List<CodeGenerator> codeGenerators = new ArrayList<>();
        codeGenerators.add(Swagger2FeignTypescriptCodegenBuilder.builder()
                .languageDescription(LanguageDescription.TYPESCRIPT)
                .clientProviderType(ClientProviderType.TYPESCRIPT_FEIGN)
                .packageMapStrategy(new TypescriptPackageMapStrategy(new HashMap<>()))
                .outPath(Paths.get(System.getProperty("user.dir")).resolveSibling(String.join(File.separator, getOutPaths(ClientProviderType.TYPESCRIPT_FEIGN))).toString())
                .buildCodeGenerator());

        codeGenerators.add(Swagger2FeignTypescriptCodegenBuilder.builder()
                .languageDescription(LanguageDescription.TYPESCRIPT)
                .clientProviderType(ClientProviderType.UMI_REQUEST)
                .packageMapStrategy(new TypescriptPackageMapStrategy(new HashMap<>()))
                .outPath(Paths.get(System.getProperty("user.dir")).resolveSibling(String.join(File.separator, getOutPaths(ClientProviderType.UMI_REQUEST))).toString())
                .buildCodeGenerator());
        codeGenerators.add(
                Swagger2FeignDartCodegenBuilder.builder()
                        //设置基础数据类型的映射关系
                        //自定义的类型映射
                        .packageMapStrategy(new TypescriptPackageMapStrategy(new HashMap<>()))
                        .outPath(Paths.get(System.getProperty("user.dir")).resolveSibling(String.join(File.separator, getOutPaths(ClientProviderType.DART_FEIGN))).toString())
                        .buildCodeGenerator());
        codeGenerators.add(
                Swagger2FeignJavaCodegenBuilder.builder()
                        .build()
                        //设置基础数据类型的映射关系
                        .baseTypeMapping(CommonsMultipartFile.class, JavaCodeGenClassMeta.FILE)
                        //自定义的类型映射
                        .languageDescription(LanguageDescription.JAVA)
                        .clientProviderType(ClientProviderType.SPRING_CLOUD_OPENFEIGN)
                        .packageMapStrategy(new JavaPackageMapStrategy(new HashMap<>(), ""))
                        .outPath(Paths.get(System.getProperty("user.dir")).resolveSibling(String.join(File.separator, getOutPaths(ClientProviderType.SPRING_CLOUD_OPENFEIGN))).toString())
                        .buildCodeGenerator());
        codeGenerators.add(
                Swagger2FeignJavaCodegenBuilder.builder()
                        .useRxJava(true)
                        .build()
                        // 基础类型映射
                        .baseTypeMapping(CommonsMultipartFile.class, JavaCodeGenClassMeta.FILE)
                        //自定义的类型映射
                        .languageDescription(LanguageDescription.JAVA_ANDROID)
                        .clientProviderType(ClientProviderType.RETROFIT)
                        .packageMapStrategy(new JavaPackageMapStrategy(new HashMap<>(), ""))
                        .outPath(Paths.get(System.getProperty("user.dir")).resolveSibling(String.join(File.separator, getOutPaths(ClientProviderType.RETROFIT))).toString())
                        .isDeletedOutputDirectory(false)
                        .enableFieldUnderlineStyle(true)
                        .buildCodeGenerator());

        return codeGenerators;
    }

    protected String[] getOutPaths(ClientProviderType type) {

        return new String[]{"codegen-result", "annotation-processor", OPEN_API_TYPE.name().toLowerCase(), type.name().toLowerCase(), "src"};
    }

    private Class<?> getClassForType(TypeElement typeElement) {
        try {
            return Class.forName(typeElement.getQualifiedName().toString());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
