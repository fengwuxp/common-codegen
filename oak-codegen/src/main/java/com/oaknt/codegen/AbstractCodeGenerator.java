package com.oaknt.codegen;


import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.scanner.StaticClassPathScanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.Set;


/**
 * abstract code generator
 */
@Slf4j
public abstract class AbstractCodeGenerator implements CodeGenerator {

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
                .forEach(commonCodeGenClassMeta -> {
                    //模板处理，生成目标代码
                    this.templateStrategy.build(commonCodeGenClassMeta);
                });
    }

    /**
     * 包扫描 获的需要生成的类
     *
     * @return
     */
    protected Set<Class<?>> scanPackages() {

        Set<Class<?>> classes = StaticClassPathScanner.scan(packagePaths)
                .filterByAnnotation(Controller.class, RestController.class)
                .getClasses();
        log.debug("共扫描到{}个类文件", classes.size());
        return classes;
    }
}
