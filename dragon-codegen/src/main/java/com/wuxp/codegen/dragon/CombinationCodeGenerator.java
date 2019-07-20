package com.wuxp.codegen.dragon;


import com.wuxp.codegen.core.CodeGenerator;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

/**
 * 组合生成模式
 */
@Slf4j
public class CombinationCodeGenerator implements CodeGenerator {

    private CodeGenerator[] codeGenerators;

    public CombinationCodeGenerator(CodeGenerator[] codeGenerators) {
        this.codeGenerators = codeGenerators;
    }

    @Override
    public void generate() {

        Arrays.stream(codeGenerators).forEach(CodeGenerator::generate);
    }
}
