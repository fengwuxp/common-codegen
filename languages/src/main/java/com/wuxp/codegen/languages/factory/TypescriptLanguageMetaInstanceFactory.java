package com.wuxp.codegen.languages.factory;

import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptFieldMate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

@Slf4j
public class TypescriptLanguageMetaInstanceFactory implements
    LanguageParser.LanguageMetaInstanceFactory<TypescriptClassMeta, CommonCodeGenMethodMeta, TypescriptFieldMate> {

  @Override
  public TypescriptClassMeta newClassInstance() {
    return new TypescriptClassMeta();
  }

  @Override
  public TypescriptFieldMate newFieldInstance() {
    return new TypescriptFieldMate();
  }

  @Override
  public TypescriptClassMeta getTypeVariableInstance() {
    TypescriptClassMeta typescriptClassMeta = new TypescriptClassMeta();
    BeanUtils.copyProperties(CommonCodeGenClassMeta.TYPE_VARIABLE, typescriptClassMeta);
    typescriptClassMeta.setSuperClass(TypescriptClassMeta.OBJECT)
        .setNeedGenerate(false)
        .setNeedImport(false);
    return typescriptClassMeta;
  }
}
