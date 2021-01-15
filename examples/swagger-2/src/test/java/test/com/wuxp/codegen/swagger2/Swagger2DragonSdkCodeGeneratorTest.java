package test.com.wuxp.codegen.swagger2;

import com.wuxp.codegen.starter.DragonSdkCodeGenerator;
import org.junit.jupiter.api.Test;

public class Swagger2DragonSdkCodeGeneratorTest {

    @Test
    public void testCodegen() {

        DragonSdkCodeGenerator dragonSdkCodegenerator = new DragonSdkCodeGenerator("com.wuxp.codegen.swagger2.**.controller");

        dragonSdkCodegenerator.generate();
    }
}
