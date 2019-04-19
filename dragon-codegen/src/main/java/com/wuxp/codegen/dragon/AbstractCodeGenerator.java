package com.wuxp.codegen.dragon;


import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

import java.util.*;
import java.util.stream.Collectors;


/**
 * abstract code generator
 */
@Slf4j
public abstract class AbstractCodeGenerator implements CodeGenerator {

    /**
     * spring 的包扫描组件
     */
    protected ClassPathScanningCandidateComponentProvider classPathScanningCandidateComponentProvider = new ClassPathScanningCandidateComponentProvider(true);


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


    public AbstractCodeGenerator(String[] packagePaths,
                                 LanguageParser<CommonCodeGenClassMeta> languageParser,
                                 TemplateStrategy<CommonCodeGenClassMeta> templateStrategy) {
        this.packagePaths = packagePaths;
        this.languageParser = languageParser;
        this.templateStrategy = templateStrategy;
    }

    @Override
    public void generate() {
        List<CommonCodeGenClassMeta> commonCodeGenClassMetas = this.scanPackages().stream()
                .map(this.languageParser::parse)
                .filter(Objects::nonNull)
                .filter(commonCodeGenClassMeta -> {
                    //过滤掉无效的数据
                    boolean notMethod = commonCodeGenClassMeta.getMethodMetas() == null || commonCodeGenClassMeta.getMethodMetas().length == 0;
                    boolean notFiled = commonCodeGenClassMeta.getFiledMetas() == null || commonCodeGenClassMeta.getFiledMetas().length == 0;
                    return !(notFiled && notMethod);
                }).collect(Collectors.toList());


        int i = 0;
        for (; ; ) {
            log.warn("循环生成，第{}次", i);
            commonCodeGenClassMetas = commonCodeGenClassMetas.stream().map(commonCodeGenClassMeta -> {
                //模板处理，生成服务
                this.templateStrategy.build(commonCodeGenClassMeta);
                return commonCodeGenClassMeta.getDependencies().values();
            }).flatMap(Collection::stream).collect(Collectors.toList());
            if (commonCodeGenClassMetas.size() == 0 || i > 100) {
                break;
            }
            i++;
        }
    }


    /**
     * 包扫描 获的需要生成的类
     *
     * @return 需要生成的类
     */
    protected Set<Class<?>> scanPackages() {


        Set<Class<?>> classes = Arrays.stream(packagePaths)
                .map(classPathScanningCandidateComponentProvider::findCandidateComponents)
                .flatMap(Collection::stream).map(BeanDefinition::getBeanClassName).map(className -> {
                    try {
                        return Thread.currentThread().getContextClassLoader().loadClass(className);
                    } catch (ClassNotFoundException e) {
                        log.error("加载类失败", e);
                    }
                    return null;
                }).filter(Objects::nonNull)
                .collect(Collectors.toSet());


        log.debug("共扫描到{}个类文件", classes.size());
        return classes;
    }
}
