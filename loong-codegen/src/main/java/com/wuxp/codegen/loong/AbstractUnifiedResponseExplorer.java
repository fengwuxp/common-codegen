package com.wuxp.codegen.loong;

import com.wuxp.codegen.core.UnifiedResponseExplorer;
import com.wuxp.codegen.core.config.CodegenConfigHolder;
import com.wuxp.codegen.core.parser.JavaClassParser;
import com.wuxp.codegen.mapping.MappingTypeDefinitionParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.JavaMethodMeta;
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

    private final MappingTypeDefinitionParser<? extends CommonCodeGenClassMeta> mappingTypeDefinitionParser;

    protected AbstractUnifiedResponseExplorer(MappingTypeDefinitionParser<? extends CommonCodeGenClassMeta> mappingTypeDefinitionParser) {
        this.mappingTypeDefinitionParser = mappingTypeDefinitionParser;
    }

    @Override
    public void probe(Collection<Class<?>> apiClasses) {
        setBasePackages(apiClasses);
        Class<?> unifiedResponseType = getUnifiedResponseType(apiClasses);
        if (unifiedResponseType != null) {
            mappingTypeDefinitionParser.putTypeMapping(unifiedResponseType, CodegenConfigHolder.getConfig().getUnifiedResponseType());
        }
    }

    /**
     * 设置基础的包名，实现不太准确
     */
    protected void setBasePackages(Collection<Class<?>> apiClasses) {
        Optional<String> apiBasePackages = tryGetApiBasePackages(apiClasses);
        if (!apiBasePackages.isPresent()) {
            return;
        }
        List<String> basePackages = CodegenConfigHolder.getConfig().getBasePackages();
        if (basePackages.isEmpty()) {
            String basePackage = apiBasePackages.get();
            if (log.isInfoEnabled()) {
                log.info("项目的基础包名为: {}", basePackage);
            }
            basePackages.add(basePackage);
        }
    }

    private Optional<String> tryGetApiBasePackages(Collection<Class<?>> apiClasses) {
        return apiClasses.stream()
                .map(Class::getPackage)
                .filter(Objects::nonNull)
                .map(Package::getName)
                .map(name -> name.split("\\."))
                .findFirst()
                .map(names -> String.join(".", Arrays.asList(names).subList(0, 3)));
    }

    /**
     * 设置全局响应对象
     *
     * @param classes 扫描到类
     */
    protected Class<?> getUnifiedResponseType(Collection<Class<?>> classes) {

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
            return null;
        }
        if (returnTypes.size() > 2) {
            log.warn("控制器的方法返回值的类型超过2个，type size={}", returnTypes.size());
            return null;
        }
        return returnTypes.get(0);
    }
}

