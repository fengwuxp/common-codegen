package com.wuxp.codegen.dragon;


import com.wuxp.codegen.core.CodeGenerator;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

/**
 * 组合生成模式
 *
 * @author wuxp
 */
@Slf4j
public class CombinationCodeGenerator implements CodeGenerator {

  private final CodeGenerator[] codeGenerators;

  public CombinationCodeGenerator(CodeGenerator[] codeGenerators) {
    this.codeGenerators = codeGenerators;
  }

  @Override
  public void generate() {

    Arrays.stream(codeGenerators).forEach(CodeGenerator::generate);
  }
}
