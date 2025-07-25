package com.wuxp.codegen.meta.annotations.factories;

import com.wuxp.codegen.core.ClassCodeGenerator;
import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.CodegenBuilder;
import com.wuxp.codegen.core.config.CodegenConfig;
import com.wuxp.codegen.core.config.CodegenConfigHolder;
import com.wuxp.codegen.core.util.ClassLoaderUtils;
import com.wuxp.codegen.core.util.CodegenFileUtils;
import com.wuxp.codegen.starter.LoongCodeGenerator;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * 基于Spring Web 的sdk生成
 *
 * @author wuxp
 */
@SupportedAnnotationTypes(value = {
        "org.springframework.stereotype.Controller",
        "org.springframework.web.bind.annotation.RestController"
})
public class SpringApiSdkCodegenProcessor extends AbstractProcessor {

    private final LoongCodeGenerator loongSdkCodeGenerator = new LoongCodeGenerator();

    public SpringApiSdkCodegenProcessor() {
        // TODO 初始化配置
        CodegenConfigHolder.setConfig(CodegenConfig.builder().build());
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        // 删除原本的输出目录
        String baseOutPath = LoongCodeGenerator.getBaseOutputPath();
        CodegenFileUtils.deleteDirectory(baseOutPath);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Messager messager = this.processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "开始执行" + SpringApiSdkCodegenProcessor.class.getSimpleName());
        Collection<CodeGenerator> codeGenerators = loongSdkCodeGenerator.getCodeGeneratorBuilders()
                .stream()
                .map(CodegenBuilder::buildCodeGenerator)
                .collect(Collectors.toList());
        for (TypeElement typeElement : annotations) {
            String className = typeElement.getQualifiedName().toString();
            try {
                Class<? extends Annotation> entityAnnotationType = ClassLoaderUtils.loadClass(className);
                processAnnotations(roundEnv.getElementsAnnotatedWith(entityAnnotationType), codeGenerators);
            } catch (ClassNotFoundException e) {
                this.processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, String.format("%s  can't found class %s",
                        getClass().getSimpleName(), className));
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
            if (codeGenerator instanceof ClassCodeGenerator) {
                ((ClassCodeGenerator) codeGenerator).generate(Collections.singletonList(aClass));
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
