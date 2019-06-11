package test.com.wuxp.codegen.typescript;

import com.wuxp.codegen.dragon.strategy.JavaPackageMapStrategy;
import com.wuxp.codegen.dragon.strategy.TypescriptPackageMapStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.swagger2.builder.Swagger2FeignJavaCodegenBuilder;
import com.wuxp.codegen.swagger2.builder.Swagger2FeignTypescriptCodegenBuilder;
import com.wuxp.codegen.swagger2.example.resp.PageInfo;
import com.wuxp.codegen.swagger2.example.resp.ServiceQueryResponse;
import com.wuxp.codegen.swagger2.example.resp.ServiceResponse;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SwaggerFeignSdkCodegenAndroidTest {


    @Test
    public void testCodeGenAndroidApiByStater() {

        //设置基础数据类型的映射关系
        Map<Class<?>, CommonCodeGenClassMeta> baseTypeMapping = new HashMap<>();


        //自定义的类型映射
        Map<Class<?>, Class<?>[]> customTypeMapping = new HashMap<>();
        customTypeMapping.put(ServiceQueryResponse.class, new Class<?>[]{ServiceResponse.class, PageInfo.class});

        //包名映射关系
        Map<String, String> packageMap = new LinkedHashMap<>();

        //控制器的包所在
        packageMap.put("com.wuxp.codegen.swagger2.controller", "com.wuxp.codegen.swagger2.services");
        //其他类（DTO、VO等）所在的包
        String basePackageName = "com.wuxp.codegen.swagger2";
        packageMap.put("com.wuxp.codegen.swagger2.example", basePackageName);

        String language = LanguageDescription.JAVA_ANDROID.getName();
        String[] outPaths = {"codegen-result", language.toLowerCase(), "src"};

        //要进行生成的源代码包名列表
        String[] packagePaths = {"com.wuxp.codegen.swagger2.example.controller"};

        Swagger2FeignJavaCodegenBuilder.builder()
                .build()
                .baseTypeMapping(baseTypeMapping)
                .languageDescription(LanguageDescription.JAVA_ANDROID)
                .customJavaTypeMapping(customTypeMapping)
                .packageMapStrategy(new JavaPackageMapStrategy(packageMap,basePackageName))
                .outPath(Paths.get(System.getProperty("user.dir")).resolveSibling(String.join(File.separator, outPaths)).toString())
                .scanPackages(packagePaths)
                .isDeletedOutputDirectory(false)
                .buildCodeGenerator()
                .generate();

    }
}
