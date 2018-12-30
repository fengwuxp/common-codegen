package com.wuxp.codegen.languages;

import com.wuxp.codegen.core.CodeDetect;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.model.mapping.TypeMapping;
import com.wuxp.codegen.mapping.TypescriptTypeMapping;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;


/**
 * 抽象的typescript parser
 */
@Slf4j
public abstract class AbstractTypescriptParser extends AbstractLanguageParser<TypescriptClassMeta, CommonCodeGenMethodMeta, CommonCodeGenFiledMeta> {


    /**
     * 映射java类和typeScript类之间的关系
     */
    protected TypeMapping<Class<?>, Collection<TypescriptClassMeta>> typescriptTypeMapping = new TypescriptTypeMapping(this);

    public AbstractTypescriptParser(PackageMapStrategy packageMapStrategy, Collection<CodeDetect> codeDetects) {
        super(packageMapStrategy, codeDetects);
    }
}
