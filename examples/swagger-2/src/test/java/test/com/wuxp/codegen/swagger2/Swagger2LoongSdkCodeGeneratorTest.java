package test.com.wuxp.codegen.swagger2;

import com.wuxp.codegen.starter.LoongCodeGenerator;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.Collection;

import static com.wuxp.codegen.core.constant.Constants.DEFAULT_CODEGEN_DIR;

class Swagger2LoongSdkCodeGeneratorTest {

    @Test
    void testCodegen() throws Exception {
        LoongCodeGenerator LoongSdkCodeGenerator = new LoongCodeGenerator("com.wuxp.codegen.swagger2.**.controller");
        LoongSdkCodeGenerator.setOutputPath(getOutPath());
        LoongSdkCodeGenerator.generate();

        Collection<File> files = FileUtils.listFiles(new File(getOutPath()), null, true);
        Assertions.assertFalse(files.isEmpty());
        FileUtils.deleteDirectory(new File(getBseOutPath()));
    }

    private String getOutPath() {
        String[] outPaths = {
                getBseOutPath(),
                "loong"
        };
        return Paths.get(String.join(File.separator, outPaths)).toString();
    }

    private String getBseOutPath() {
        String[] outPaths = {
                System.getProperty("user.dir"),
                DEFAULT_CODEGEN_DIR,
        };
        return Paths.get(String.join(File.separator, outPaths)).toString();
    }
}
