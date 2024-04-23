package test.com.wuxp.codegen.swagger3;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.model.LanguageDescription;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static com.wuxp.codegen.core.constant.Constants.DEFAULT_CODEGEN_DIR;

public final class Swagger3AssertCodegenResultUtil {

    private final static String SWAGGER_VERSION = "swagger3";

    private final static String MODEL_DIR = "swagger-3";

    private final static String ASSERT_DIR = "asserts";

    public Swagger3AssertCodegenResultUtil() {
        throw new AssertionError();
    }

    public static void main(String[] args) throws Exception {
        syncCodegenResult();
    }

    public static void assertGenerate(LanguageDescription languageDescription, ClientProviderType clientProviderType) throws Exception {
        String[] extensions = {languageDescription.getSuffixName()};
        List<File> files = getFiles(getOutPath(languageDescription, clientProviderType), extensions);
        List<File> assertFiles = getFiles(getAssertPath(languageDescription, clientProviderType), extensions);
        Assertions.assertEquals(files.size(), assertFiles.size());
        for (int i = 0; i < files.size(); i++) {
            assertEquals(assertFiles.get(i), files.get(i));
        }
        deleteOutputDir();
    }

    private static void deleteOutputDir() throws IOException {
        String baseOutPath = getPath(new String[]{
                System.getProperty("user.dir"),
                DEFAULT_CODEGEN_DIR,
        });
        FileUtils.deleteDirectory(new File(baseOutPath));
    }

    private static List<File> getFiles(String languageDescription, String[] extensions) {
        return new ArrayList<>(FileUtils.listFiles(new File(languageDescription), extensions, true));
    }

    public static String getOutPath(LanguageDescription languageDescription, ClientProviderType clientProviderType) {
        String[] outPaths = {
                System.getProperty("user.dir"),
                DEFAULT_CODEGEN_DIR,
                SWAGGER_VERSION,
                languageDescription.getName().toLowerCase(),
                clientProviderType.name().toLowerCase(),
                "src"
        };
        return Paths.get(String.join(File.separator, outPaths)).toString();
    }

    private static String getAssertPath(LanguageDescription languageDescription, ClientProviderType clientProviderType) {
        URL resource = Swagger3AssertCodegenResultUtil.class.getResource("/");
        Assertions.assertNotNull(resource);
        String[] outPaths = {
                resource.getPath(),
                ASSERT_DIR,
                languageDescription.getName().toLowerCase(),
                clientProviderType.name().toLowerCase(),
                "src"
        };
        return getPath(outPaths);
    }

    private static void assertEquals(File expected, File actual) throws Exception {
        Assertions.assertEquals(
                FileUtils.readFileToString(expected, StandardCharsets.UTF_8),
                FileUtils.readFileToString(actual, StandardCharsets.UTF_8),
                String.format("%s not eq %s", expected.getPath(), actual.getPath())
        );
    }

    private static void syncCodegenResult() throws Exception {

        String[] outPaths = {
                // main 方法下为项目根路径
                System.getProperty("user.dir"),
                "examples",
                MODEL_DIR,
                DEFAULT_CODEGEN_DIR,
                SWAGGER_VERSION,
        };
        String outBasePath = getPath(outPaths);

        copyToTestResources(outBasePath);
        copyToTestClasspath(outBasePath);
    }

    private static void copyToTestResources(String outBasePath) throws IOException {
        String[] assertResourcePaths = {
                // main 方法下为项目根路径
                System.getProperty("user.dir"),
                "examples",
                MODEL_DIR,
                "src",
                "test",
                "resources",
                "asserts",
        };
        String assertsPath = getPath(assertResourcePaths);

        File destDir = new File(assertsPath);
        FileUtils.deleteDirectory(destDir);

        // copy to resources
        FileUtils.copyDirectory(new File(outBasePath), destDir);
    }

    private static void copyToTestClasspath(String outBasePath) throws IOException {
        URL resource = Swagger3AssertCodegenResultUtil.class.getResource("/");
        Assertions.assertNotNull(resource);
        String[] assertResourcePaths = {
                resource.getPath(),
                "asserts",
        };
        String assertsPath = getPath(assertResourcePaths);

        File destDir = new File(assertsPath);
        FileUtils.deleteDirectory(destDir);

        FileUtils.copyDirectory(new File(outBasePath), destDir);
    }

    private static String getPath(String[] paths) {
        return Paths.get(String.join(File.separator, paths)).toString();
    }
}
