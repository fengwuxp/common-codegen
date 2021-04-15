package com.wuxp.codegen.loong;

import com.wuxp.codegen.core.UnifiedResponseExplorer;
import com.wuxp.codegen.core.config.CodegenConfigHolder;
import com.wuxp.codegen.core.parser.JavaClassParser;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.languages.AbstractLanguageParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
import com.wuxp.codegen.model.mapping.BaseTypeMapping;
import com.wuxp.codegen.model.mapping.TypeMapping;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.stream.Collectors;

import static com.wuxp.codegen.core.parser.JavaClassParser.JAVA_CLASS_ON_PUBLIC_PARSER;
import static com.wuxp.codegen.model.constant.SpringAnnotationClassConstant.SPRING_MAPPING_ANNOTATIONS;

/**
 * 统一响应对象探测和自动映射
 *
 * @author wuxp
 */
@Slf4j
public abstract class AbstractUnifiedResponseExplorer implements UnifiedResponseExplorer {

    private static final JavaClassParser CLASS_PARSER = JAVA_CLASS_ON_PUBLIC_PARSER;

    private final LanguageParser languageParser;

    public AbstractUnifiedResponseExplorer(LanguageParser languageParser) {
        this.languageParser = languageParser;
    }

    @Override
    public void probe(Collection<Class<?>> classes) {
        setBasePackage(classes);
        setUnifiedResponseTypeMapping(classes);

    }

    /**
     * 设置基础的包名，实现不太准确
     *
     * @param classes 扫描到类
     */
    protected void setBasePackage(Collection<Class<?>> classes) {
        Optional<String> optional = classes.stream()
                .map(Class::getPackage)
                .filter(Objects::nonNull)
                .map(Package::getName)
                .map(name -> name.split("\\."))
                .findFirst()
                .map(names -> String.join(".", Arrays.asList(names).subList(0, 3)));
        if (optional.isPresent()) {
            List<String> basePackages = CodegenConfigHolder.getConfig().getBasePackages();
            if (basePackages.isEmpty()) {
                String basePackage = optional.get();
                log.info("项目的基础包名为: {}", basePackage);
                basePackages.add(basePackage);
            }
        }
    }

    /**
     * 设置全局响应对象
     *
     * @param classes 扫描到类
     */
    protected void setUnifiedResponseTypeMapping(Collection<Class<?>> classes) {
        AbstractLanguageParser languageParser = (AbstractLanguageParser) this.languageParser;
        TypeMapping typeMapping = languageParser.getLanguageTypeMapping().getCombineTypeMapping();
        if (typeMapping == null) {
            return;
        }
        List<? extends Class<?>> returnTypes = classes.stream()
                .map(CLASS_PARSER::parse)
                .map(JavaClassMeta::getMethodMetas)
                .map(Arrays::asList)
                .flatMap(Collection::stream)
                .filter(javaMethodMeta -> javaMethodMeta.existAnnotation(SPRING_MAPPING_ANNOTATIONS))
                .map(JavaMethodMeta::getReturnType)
                .map(clazzList -> clazzList[0])
                .distinct()
                .collect(Collectors.toList());
        if (returnTypes.isEmpty()) {
            return;
        }
        if (returnTypes.size() > 2) {
            log.warn("控制器的方法返回值的类型超过2个，type size={}", returnTypes.size());
            return;
        }
        if (typeMapping instanceof BaseTypeMapping) {
            Class<?> unifiedResponseType = returnTypes.get(0);
            CommonCodeGenClassMeta responseType = CodegenConfigHolder.getConfig().getUnifiedResponseType();
            if (responseType != null) {
                ((BaseTypeMapping<?>) typeMapping).tryAddTypeMapping(unifiedResponseType, responseType);
            }
        }

    }
}

