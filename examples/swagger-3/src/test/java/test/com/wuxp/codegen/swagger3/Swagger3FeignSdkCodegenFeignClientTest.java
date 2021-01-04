package test.com.wuxp.codegen.swagger3;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.parser.enhance.CombineLanguageEnhancedProcessor;
import com.wuxp.codegen.dragon.strategy.JavaPackageMapStrategy;
import com.wuxp.codegen.languages.java.SpringCloudFeignClientEnhancedProcessor;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import com.wuxp.codegen.model.mapping.AbstractTypeMapping;
import com.wuxp.codegen.swagger3.builder.Swagger3FeignJavaCodegenBuilder;
import com.wuxp.codegen.swagger3.example.controller.HelloController;
import com.wuxp.codegen.swagger3.example.controller.OrderController;
import com.wuxp.codegen.swagger3.example.resp.PageInfo;
import com.wuxp.codegen.swagger3.example.resp.ServiceQueryResponse;
import com.wuxp.codegen.swagger3.example.resp.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class Swagger3FeignSdkCodegenFeignClientTest {


    @Test
    public void testCodeGenFeignClientByStater() {


        //设置基础数据类型的映射关系
        Map<Class<?>, CommonCodeGenClassMeta> baseTypeMapping = new HashMap<>();

        AbstractTypeMapping.setBaseTypeMapping(CommonsMultipartFile.class, JavaCodeGenClassMeta.FILE);
//        AbstractTypeMapping.setCustomizeJavaTypeMapping(CommonsMultipartFile.class, new Class[]{File.class});


        //自定义的类型映射
        Map<Class<?>, Class<?>[]> customTypeMapping = new HashMap<>();
        customTypeMapping.put(ServiceQueryResponse.class, new Class<?>[]{ServiceResponse.class, PageInfo.class});

        //包名映射关系
        Map<String, String> packageMap = new LinkedHashMap<>();

        //控制器的包所在
        packageMap.put("com.wuxp.codegen.swagger3.example.controller", "com.wuxp.codegen.swagger3.clients");
        //其他类（DTO、VO等）所在的包
        String basePackageName = "com.wuxp.codegen.swagger3";
        packageMap.put("com.wuxp.codegen.swagger3.example", basePackageName);

        String language = LanguageDescription.JAVA.getName();
        String[] outPaths = {"codegen-result", language.toLowerCase(), ClientProviderType.SPRING_CLOUD_OPENFEIGN.name().toLowerCase(), "swagger3", "src"};

        //要进行生成的源代码包名列表
        String[] packagePaths = {"com.wuxp.codegen.swagger3.example.controller"};

        JavaPackageMapStrategy packageMapStrategy = new JavaPackageMapStrategy(packageMap, basePackageName);
        packageMapStrategy.setFileNamSuffix("FeignClient");
        CombineLanguageEnhancedProcessor languageEnhancedProcessor = CombineLanguageEnhancedProcessor.of(
                SpringCloudFeignClientEnhancedProcessor.builder().name("exampleService").url("${test.feign.url}").decode404(false).build());
        Swagger3FeignJavaCodegenBuilder.builder()
                .useRxJava(true)
                .build()
                .baseTypeMapping(baseTypeMapping)
                .languageDescription(LanguageDescription.JAVA)
                .clientProviderType(ClientProviderType.SPRING_CLOUD_OPENFEIGN)
                .customJavaTypeMapping(customTypeMapping)
                .packageMapStrategy(packageMapStrategy)
                .outPath(Paths.get(System.getProperty("user.dir")).resolveSibling(String.join(File.separator, outPaths)).toString())
                .scanPackages(packagePaths)
                .ignoreClasses(new Class[]{HelloController.class, OrderController.class})
                .isDeletedOutputDirectory(false)
                .languageEnhancedProcessors(languageEnhancedProcessor)
                .buildCodeGenerator()
                .generate();

    }

}
