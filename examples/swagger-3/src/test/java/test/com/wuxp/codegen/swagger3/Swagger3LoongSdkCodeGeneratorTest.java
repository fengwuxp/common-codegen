package test.com.wuxp.codegen.swagger3;

import com.wuxp.codegen.starter.LoongSdkCodeGenerator;
import org.junit.jupiter.api.Test;

class Swagger3LoongSdkCodeGeneratorTest {

    @Test
    void testCodegen() {
        LoongSdkCodeGenerator LoongSdkCodeGenerator = new LoongSdkCodeGenerator("com.wuxp.codegen.swagger3.**.controller");
        LoongSdkCodeGenerator.generate();
    }
}
