package com.oak.codegen;


import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.scanner.StaticClassPathScanner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * abstract code generator
 */
@Slf4j
public abstract class AbstractCodeGenerator implements CodeGenerator {

    /**
     * 要进行生成的源代码包名列表
     */
    protected String[] packagePaths;

    protected LanguageParser<CommonCodeGenClassMeta> languageParser;




    @Override
    public void generate() {
        this.scanPackages().stream()
                .map(this.languageParser::parse)
                .forEach(commonCodeGenClassMeta -> {
                    //模板处理器

                });
    }

    /**
     * 包扫描 获的需要生成的类
     *
     * @return
     */
    protected List<Class<?>> scanPackages() {

        return StaticClassPathScanner.scan(packagePaths)
                .filterByAnnotation(Controller.class, RestController.class)
                .getClasses();
    }
}
