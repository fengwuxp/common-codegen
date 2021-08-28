package com.wuxp.codegen.loong;

import com.wuxp.codegen.core.UnifiedResponseExplorer;
import com.wuxp.codegen.core.event.CodeGenEventListener;
import com.wuxp.codegen.core.parser.LanguageTypeDefinitionParser;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import lombok.Setter;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
public class LoongDefaultCodeGenerator extends AbstractLoongCodeGenerator {

    /**
     * 忽略的包
     */
    private Set<String> ignorePackages;

    /**
     * 需要忽略的类
     */
    private Set<Class<?>> ignoreClasses;

    /**
     * 额外导入的类
     */
    private Set<Class<?>> includeClasses;

    /**
     * 启用下划线风格
     */
    private Boolean enableFieldUnderlineStyle = Boolean.FALSE;

    /**
     * 需要额外的生成代码
     */
    private Set<CommonCodeGenClassMeta> includeClassMetas;

    private CodeGenEventListener codeGenEventListener;

    public LoongDefaultCodeGenerator(String[] scanPackages,
                                     LanguageTypeDefinitionParser<? extends CommonCodeGenClassMeta> languageTypeDefinitionParser,
                                     TemplateStrategy<CommonCodeGenClassMeta> templateStrategy,
                                     UnifiedResponseExplorer unifiedResponseExplorer) {
        super(scanPackages, languageTypeDefinitionParser, templateStrategy, unifiedResponseExplorer);
    }


    @Override
    protected CodeGenEventListener getCodeGenEventListener() {
        return codeGenEventListener;
    }

    @Override
    protected void configureComponentProvider(ClassPathScanningCandidateComponentProvider componentProvider) {
        if (ignorePackages != null) {
            componentProvider.addExcludeFilter(new ClassNameTypeFilter(ignorePackages));
        }
        if (ignoreClasses != null) {
            componentProvider.addExcludeFilter(new ClassNameTypeFilter(ignoreClasses.stream().map(Class::getName).collect(Collectors.toSet())));
        }
    }

    @Override
    protected Set<CommonCodeGenClassMeta> getIncludeClassMetas() {
        return includeClassMetas == null ? Collections.emptySet() : includeClassMetas;
    }

    @Override
    protected boolean isEnableFieldUnderlineStyle() {
        return enableFieldUnderlineStyle;
    }

    @Override
    protected Set<Class<?>> scanPackages() {
        Set<Class<?>> classes = super.scanPackages();
        if (this.includeClasses != null) {
            classes.addAll(includeClasses);
        }
        return classes;
    }

    private static class ClassNameTypeFilter implements TypeFilter {

        private final Set<String> classNames;

        private final PathMatcher pathMatcher = new AntPathMatcher();

        public ClassNameTypeFilter(Set<String> classNames) {
            this.classNames = classNames;
        }

        @Override
        public boolean match(MetadataReader metadataReader, MetadataReaderFactory metadataReaderFactory) throws IOException {
            return this.classNames.stream()
                    .filter(StringUtils::hasLength)
                    .anyMatch(className -> matches(className, metadataReader.getClassMetadata().getClassName()));
        }

        private boolean matches(String pattern, String className) {
            if (pathMatcher.isPattern(pattern)) {
                return pathMatcher.match(pattern, className);
            } else {
                return pattern.startsWith(className);
            }
        }
    }
}
