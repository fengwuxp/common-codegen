package test.com.wuxp.codegen.swagger2;

import com.wuxp.codegen.starter.DragonSdkCodeGenerator;
import org.junit.jupiter.api.Test;

class Swagger2DragonSdkCodeGeneratorTest {

    @Test
    void testCodegen() {

        DragonSdkCodeGenerator dragonSdkCodegenerator = new DragonSdkCodeGenerator("com.wuxp.codegen.swagger2.**.controller");

        dragonSdkCodegenerator.generate();
    }
}
