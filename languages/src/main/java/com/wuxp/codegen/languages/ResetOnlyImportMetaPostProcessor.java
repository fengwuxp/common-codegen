package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.CodeGenElementMatcher;
import com.wuxp.codegen.core.parser.enhance.LanguageDefinitionPostProcessor;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.util.JavaTypeUtils;
import org.springframework.core.Ordered;

import java.util.Set;

/**
 * 标记匹配一些类只需要导入，不进行生成
 * @author wuxp
 */
public class ResetOnlyImportMetaPostProcessor implements LanguageDefinitionPostProcessor<CommonCodeGenClassMeta> {

    private final CodeGenElementMatcher<Class<?>> classCodeGenElementMatcher;

    public ResetOnlyImportMetaPostProcessor(Set<Class<?>> ignoreClasses) {
        this(ignoreClasses::contains);
    }

    public ResetOnlyImportMetaPostProcessor(CodeGenElementMatcher<Class<?>> classCodeGenElementMatcher) {
        this.classCodeGenElementMatcher = classCodeGenElementMatcher;
    }

    @Override
    public void postProcess(CommonCodeGenClassMeta meta) {
        Class<?> source = meta.getSource();
        if (source == null) {
            return;
        }
        boolean matches = classCodeGenElementMatcher.matches(source);
        if (matches) {
            meta.setNeedGenerate(false);
            meta.setNeedImport(true);
        }
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return JavaTypeUtils.isAssignableFrom(clazz, CommonCodeGenClassMeta.class);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
