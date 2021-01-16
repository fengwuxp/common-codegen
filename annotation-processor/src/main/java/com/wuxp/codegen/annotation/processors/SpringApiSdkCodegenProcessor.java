package com.wuxp.codegen.annotation.processors;

import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.CodegenBuilder;
import com.wuxp.codegen.dragon.AbstractCodeGenerator;
import com.wuxp.codegen.starter.DragonSdkCodeGenerator;
import com.wuxp.codegen.util.FileUtils;

import javax.annotation.processing.*;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.io.File;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;


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

    private final DragonSdkCodeGenerator dragonSdkCodegenerator = new DragonSdkCodeGenerator();


    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        // 删除原本的输出目录
        String baseOutPath = DragonSdkCodeGenerator.getBaseOutPath();
        File file = new File(baseOutPath);
        if (file.exists() && file.isDirectory()) {
            FileUtils.deleteDirectory(baseOutPath);
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Messager messager = this.processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "开始执行" + SpringApiSdkCodegenProcessor.class.getSimpleName());
        Collection<CodeGenerator> codeGenerators = dragonSdkCodegenerator.getCodeGeneratorBuilders()
                .stream()
                .map(CodegenBuilder::buildCodeGenerator)
                .collect(Collectors.toList());
        for (TypeElement typeElement : annotations) {
            String className = typeElement.getQualifiedName().toString();
            try {
                Class<? extends Annotation> entityAnnotationType = (Class<? extends Annotation>) Class.forName(className);
                processAnnotations(roundEnv.getElementsAnnotatedWith(entityAnnotationType), codeGenerators);
            } catch (ClassNotFoundException e) {
                this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, String.format("%s  can't found class %s", getClass().getSimpleName(), className));
            }
        }
        return false;
    }

    protected void processAnnotations(Set<? extends Element> elementList, Collection<CodeGenerator> codeGenerators) {
        Messager messager = this.processingEnv.getMessager();
        for (Element element : elementList) {
            //只支持对类，接口，注解的处理，对字段不做处理
            if (!element.getKind().isClass() && !element.getKind().isInterface()) {
                continue;
            }
            TypeElement typeElement = (TypeElement) element;
            Class<?> aClass = getClassForType(typeElement);
            codeGen(aClass, codeGenerators);
            messager.printMessage(Diagnostic.Kind.NOTE, "扫描到的类：" + typeElement.getQualifiedName().toString());
        }
    }

    protected void codeGen(Class<?> aClass, Collection<CodeGenerator> codeGenerators) {
        if (aClass == null) {
            return;
        }
        codeGenerators.forEach(codeGenerator -> {
            if (codeGenerator instanceof AbstractCodeGenerator) {
                ((AbstractCodeGenerator) codeGenerator).dragonGenerate(aClass);
            }
        });
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
