package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.CodeGenElementMatcher;
import com.wuxp.codegen.core.CodeGenImportMatcher;
import com.wuxp.codegen.core.config.CodegenConfigHolder;
import com.wuxp.codegen.core.macth.DispatchCodeGenElementMatcher;
import com.wuxp.codegen.core.parser.LanguageElementDefinitionParser;
import com.wuxp.codegen.model.CommonBaseMeta;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;

import java.lang.reflect.Type;

/**
 * @author wuxp
 */
public class MatchingLanguageElementDefinitionParser<C extends CommonBaseMeta> implements LanguageElementDefinitionParser<C, Object> {

    private final LanguageElementDefinitionParser<C, Object> delegate;

    @SuppressWarnings("rawtypes")
    private final CodeGenElementMatcher codeGenElementMatcher;

    private final CodeGenImportMatcher codeGenImportMatcher;

    @SuppressWarnings("unchecked")
    public MatchingLanguageElementDefinitionParser(LanguageElementDefinitionParser<C, ?> delegate, CodeGenImportMatcher codeGenImportMatcher) {
        this.delegate = (LanguageElementDefinitionParser<C, Object>) delegate;
        this.codeGenElementMatcher = new DispatchCodeGenElementMatcher();
        this.codeGenImportMatcher = codeGenImportMatcher;
    }

    @Override
    @SuppressWarnings("unchecked")
    public C parse(Object source) {
        if (this.codeGenElementMatcher.matches(source)) {
            return delegate.parseOfNullable(source)
                    .map(this::tyyHandleOnlyImports)
                    .orElse(null);
        }
        return null;
    }

    @Override
    public C newInstance() {
        return delegate.newInstance();
    }

    @Override
    public <M extends CommonCodeGenClassMeta> M newTypeVariableInstance() {
        return delegate.newTypeVariableInstance();
    }

    @Override
    public <M extends CommonCodeGenClassMeta> M parseTypeVariable(Type type) {
        return delegate.parseTypeVariable(type);
    }

    @SuppressWarnings("unchecked")
    private C tyyHandleOnlyImports(C result) {
        if (result instanceof CommonCodeGenClassMeta) {
            return (C) handleOnlyImports((CommonCodeGenClassMeta) result);
        }
        return result;
    }

    private CommonCodeGenClassMeta handleOnlyImports(CommonCodeGenClassMeta result) {
        if (!this.codeGenImportMatcher.match(result.getSource())) {
            return result;
        }
        // 只需要导入类
        result.setNeedGenerate(false);
        result.setNeedImport(true);
        if (CodegenConfigHolder.getConfig().isJava()) {
            result.setPackagePath(result.getName());
        }
        return result;
    }
}
