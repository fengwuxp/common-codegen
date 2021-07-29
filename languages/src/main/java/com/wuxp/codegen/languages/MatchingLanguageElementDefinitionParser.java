package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.CodeGenElementMatcher;
import com.wuxp.codegen.core.macth.DispatchCodeGenElementMatcher;
import com.wuxp.codegen.core.parser.LanguageElementDefinitionParser;
import com.wuxp.codegen.model.CommonBaseMeta;

/**
 * @author wuxp
 */
public class MatchingLanguageElementDefinitionParser<C extends CommonBaseMeta> implements LanguageElementDefinitionParser<C, Object> {

    private final LanguageElementDefinitionParser<C, Object> delegate;

    @SuppressWarnings("rawtypes")
    private final CodeGenElementMatcher codeGenElementMatcher;

    @SuppressWarnings("unchecked")
    public MatchingLanguageElementDefinitionParser(LanguageElementDefinitionParser<C, ?> delegate) {
        this.delegate = (LanguageElementDefinitionParser<C, Object>) delegate;
        this.codeGenElementMatcher = new DispatchCodeGenElementMatcher();
    }

    @Override
    @SuppressWarnings("unchecked")
    public C parse(Object source) {
        if (this.codeGenElementMatcher.matches(source)) {
            return delegate.parseOfNullable(source).orElse(null);
        }
        return null;
    }
}
