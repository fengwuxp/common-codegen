package test.com.wuxp.codegen.swagger2;

import com.wuxp.codegen.starter.LoongSdkCodeGenerator;
import org.junit.jupiter.api.Test;

class Swagger2LoongSdkCodeGeneratorTest {

    @Test
    void testCodegen() {
        LoongSdkCodeGenerator LoongSdkCodeGenerator = new LoongSdkCodeGenerator("com.wuxp.codegen.swagger2.**.controller");
        LoongSdkCodeGenerator.setOutputPath("../");
        LoongSdkCodeGenerator.generate();
    }
}
