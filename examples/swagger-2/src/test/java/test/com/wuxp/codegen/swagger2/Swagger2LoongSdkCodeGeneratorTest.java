package test.com.wuxp.codegen.swagger2;

import com.wuxp.codegen.starter.LoongCodeGenerator;
import org.junit.jupiter.api.Test;

class Swagger2LoongSdkCodeGeneratorTest {

    @Test
    void testCodegen() {
        LoongCodeGenerator LoongSdkCodeGenerator = new LoongCodeGenerator("com.wuxp.codegen.swagger2.**.controller");
        LoongSdkCodeGenerator.setOutputPath("../");
        LoongSdkCodeGenerator.generate();
    }
}
