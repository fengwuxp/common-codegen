package test.com.wuxp.codegen.swagger2;

import com.wuxp.codegen.starter.DragonSdkCodeGenerator;
import org.junit.jupiter.api.Test;

import java.io.File;

class Swagger2DragonSdkCodeGeneratorTest {

    @Test
    void testCodegen() {
        DragonSdkCodeGenerator dragonSdkCodegenerator = new DragonSdkCodeGenerator("com.wuxp.codegen.swagger2.**.controller");
        dragonSdkCodegenerator.setOutPath(System.getProperty("user.dir") + File.separator + ".." + File.separator + "target");
        dragonSdkCodegenerator.generate();
    }
}
