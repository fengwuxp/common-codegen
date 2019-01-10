package com.oaknt.codegen;


import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * abstract code generator
 */
@Slf4j
public abstract class AbstractCodeGenerator implements CodeGenerator {

    protected static final ClassPathScanningCandidateComponentProvider CANDIDATE_COMPONENT_PROVIDER = new ClassPathScanningCandidateComponentProvider(true);

    static {
        CANDIDATE_COMPONENT_PROVIDER.addIncludeFilter(new AnnotationTypeFilter(Api.class));
        CANDIDATE_COMPONENT_PROVIDER.addExcludeFilter(new AnnotationTypeFilter(ApiIgnore.class));
    }

    /**
     * 要进行生成的源代码包名列表
     */
    protected String[] packagePaths;

    /**
     * 语言解析器
     */
    protected LanguageParser<CommonCodeGenClassMeta> languageParser;


    /**
     * 模板处理策略
     */
    protected TemplateStrategy<CommonCodeGenClassMeta> templateStrategy;


    public AbstractCodeGenerator(String[] packagePaths, LanguageParser<CommonCodeGenClassMeta> languageParser, TemplateStrategy<CommonCodeGenClassMeta> templateStrategy) {
        this.packagePaths = packagePaths;
        this.languageParser = languageParser;
        this.templateStrategy = templateStrategy;
    }

    @Override
    public void generate() {
        this.scanPackages().stream()
                .map(this.languageParser::parse)
                .filter(Objects::nonNull)
                .filter(commonCodeGenClassMeta -> {
                    boolean notMethod = commonCodeGenClassMeta.getMethodMetas() == null || commonCodeGenClassMeta.getMethodMetas().length == 0;
                    boolean notFiled = commonCodeGenClassMeta.getFiledMetas() == null || commonCodeGenClassMeta.getFiledMetas().length == 0;
                    return !(notFiled && notMethod);
                })
                .map(commonCodeGenClassMeta -> {
                    //模板处理，生成服务
                    this.templateStrategy.build(commonCodeGenClassMeta);
                    return commonCodeGenClassMeta.getDependencies().values();
                }).flatMap(Collection::stream)
                .map(commonCodeGenClassMeta -> {
                    //模板处理，生成目控制器或服务的依赖（DTO）
                    this.templateStrategy.build(commonCodeGenClassMeta);
                    return commonCodeGenClassMeta.getDependencies().values();
                })
                .flatMap(Collection::stream)
                .forEach(commonCodeGenClassMeta -> {
                    //模板处理，生成DTO的依赖（其他DTO或枚举）
                    this.templateStrategy.build(commonCodeGenClassMeta);
                });
    }

    /**
     * 包扫描 获的需要生成的类
     *
     * @return
     */
    protected Set<Class<?>> scanPackages() {


        Set<Class<?>> classes = Arrays.stream(packagePaths)
                .map(CANDIDATE_COMPONENT_PROVIDER::findCandidateComponents)
                .flatMap(Collection::stream).map(BeanDefinition::getBeanClassName).map(className -> {
                    try {
                        return Thread.currentThread().getContextClassLoader().loadClass(className);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).filter(Objects::nonNull)
                .collect(Collectors.toSet());


        log.debug("共扫描到{}个类文件", classes.size());
        return classes;
    }
}
