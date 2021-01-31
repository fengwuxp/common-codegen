package com.wuxp.codegen.swagger3.example;

import com.wuxp.codegen.starter.DragonSdkCodeGenerator;
import org.junit.jupiter.api.Test;

public class Swagger3DragonSdkCodeGeneratorTest {

    @Test
    public void testCodegen() {

        DragonSdkCodeGenerator dragonSdkCodegenerator = new DragonSdkCodeGenerator("com.wuxp.codegen.swagger3.**.controller");

        dragonSdkCodegenerator.generate();
    }
}
