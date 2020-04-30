package com.wuxp.codegen.languages.factory;

import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.model.CommonCodeGenFiledMeta;
import com.wuxp.codegen.model.CommonCodeGenMethodMeta;
import com.wuxp.codegen.model.languages.dart.DartClassMeta;
import com.wuxp.codegen.model.languages.dart.DartFieldMate;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

/**
 * @author wxup
 */
@Slf4j
public class DartLanguageMetaInstanceFactory implements
        LanguageParser.LanguageMetaInstanceFactory<DartClassMeta, CommonCodeGenMethodMeta, DartFieldMate> {

    @Override
    public DartClassMeta newClassInstance() {
        return new DartClassMeta();
    }

    @Override
    public DartFieldMate newFieldInstance() {
        return new DartFieldMate();
    }

    @Override
    public DartClassMeta getTypeVariableInstance() {
        DartClassMeta dartClassMeta = new DartClassMeta();
        BeanUtils.copyProperties(DartClassMeta.TYPE_VARIABLE, dartClassMeta);
        return dartClassMeta;
    }
}
