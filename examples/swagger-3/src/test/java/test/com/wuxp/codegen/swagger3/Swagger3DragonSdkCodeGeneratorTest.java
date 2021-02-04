package test.com.wuxp.codegen.swagger3;

import com.wuxp.codegen.starter.DragonSdkCodeGenerator;
import org.junit.jupiter.api.Test;

class Swagger3DragonSdkCodeGeneratorTest {

    @Test
    void testCodegen() {
        DragonSdkCodeGenerator dragonSdkCodegenerator = new DragonSdkCodeGenerator("com.wuxp.codegen.swagger3.**.controller");
        dragonSdkCodegenerator.generate();
    }
}
