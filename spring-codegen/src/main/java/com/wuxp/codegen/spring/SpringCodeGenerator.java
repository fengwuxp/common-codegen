package com.wuxp.codegen.spring;

import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.dragon.AbstractCodeGenerator;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import com.wuxp.codegen.spring.annotations.ColumnProcessor;
import com.wuxp.codegen.spring.annotations.TableProcessor;
import com.wuxp.codegen.spring.model.JavaSpringCodeGenClassMeta;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Column;
import javax.persistence.Table;

import java.util.*;
import java.util.stream.Collectors;

import static com.wuxp.codegen.languages.AbstractLanguageParser.ANNOTATION_PROCESSOR_MAP;

@Slf4j
@Setter
public class SpringCodeGenerator extends AbstractCodeGenerator {

    private static final String GROUP_TAG = "ENTITY_GROUP";

    static {
        //添加jpa相关的注解处理器
        ANNOTATION_PROCESSOR_MAP.put(Column.class, new ColumnProcessor());
        ANNOTATION_PROCESSOR_MAP.put(Table.class, new TableProcessor());
    }

    private Map<String/*tag name*/, JavaSpringCodeGenClassMeta> groups = Collections.emptyMap();


    public SpringCodeGenerator(String[] packagePaths, LanguageParser<CommonCodeGenClassMeta> languageParser, TemplateStrategy<CommonCodeGenClassMeta> templateStrategy, boolean enableFieldUnderlineStyle) {
        super(packagePaths, languageParser, templateStrategy, enableFieldUnderlineStyle);
        this.setClassPathScanningCandidateComponentProvider(new JapEntityClassPathScanningCandidateComponentProvider());
    }

    public SpringCodeGenerator(String[] packagePaths, Set<String> ignorePackages, Class<?>[] includeClasses, Class<?>[] ignoreClasses, LanguageParser<CommonCodeGenClassMeta> languageParser, TemplateStrategy<CommonCodeGenClassMeta> templateStrategy, boolean enableFieldUnderlineStyle) {
        super(packagePaths, ignorePackages, includeClasses, ignoreClasses, languageParser, templateStrategy, enableFieldUnderlineStyle);
        this.setClassPathScanningCandidateComponentProvider(new JapEntityClassPathScanningCandidateComponentProvider());
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


        if (groups.isEmpty()) {
            throw new RuntimeException("请设置groups");
        }
        Map<String, List<CommonCodeGenClassMeta>> map = new HashMap<>();
        commonCodeGenClassMetas.forEach(commonCodeGenClassMeta -> {
            String tag = commonCodeGenClassMeta.getTag(GROUP_TAG);
            List<CommonCodeGenClassMeta> javaCodeGenClassMetas = map.computeIfAbsent(tag, k -> new ArrayList<>());
            javaCodeGenClassMetas.add(commonCodeGenClassMeta);
        });

        Map<String, JavaSpringCodeGenClassMeta> groups = this.groups;

        map.forEach((key, values) -> {

            groups.get(key).setJavaCodeGenClassMetas(
                    values.stream()
                            .map(commonCodeGenClassMeta -> (JavaCodeGenClassMeta) commonCodeGenClassMeta)
                            .toArray(JavaCodeGenClassMeta[]::new));
        });

        TemplateStrategy templateStrategy = this.templateStrategy;
        groups.forEach((key,javaSpringCodeGenClassMeta)->{
            templateStrategy.build(javaSpringCodeGenClassMeta);
        });

    }
}
