package test.com.wuxp.codegen.swagger2;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.parser.JavaClassParser;
import com.wuxp.codegen.languages.java.SpringCloudFeignClientPostProcessor;
import com.wuxp.codegen.loong.strategy.JavaPackageMapStrategy;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import com.wuxp.codegen.swagger2.builder.Swagger2FeignJavaCodegenBuilder;
import com.wuxp.codegen.swagger2.example.domain.User;
import com.wuxp.codegen.swagger2.example.enums.ExampleEnum;
import com.wuxp.codegen.swagger2.example.resp.PageInfo;
import com.wuxp.codegen.swagger2.example.resp.ServiceQueryResponse;
import com.wuxp.codegen.swagger2.example.resp.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
class Swagger2FeignSdkCodegenFeignClientTest {


    @Test
    void testCodeGenFeignClientByStater() throws Exception {

        //包名映射关系
        Map<String, String> packageMap = new LinkedHashMap<>();

        //控制器的包所在
        packageMap.put("com.wuxp.codegen.swagger2.example.controller", "com.wuxp.codegen.swagger2.clients");
        //其他类（DTO、VO等）所在的包
        String basePackageName = "com.wuxp.codegen.swagger2";
        packageMap.put("com.wuxp.codegen.swagger2.example", basePackageName);

        //要进行生成的源代码包名列表
        String[] packagePaths = {"com.wuxp.codegen.swagger2.example.controller"};

        JavaPackageMapStrategy packageMapStrategy = new JavaPackageMapStrategy(packageMap, basePackageName);
        packageMapStrategy.setFileNamSuffix("FeignClient");

        LanguageDescription language = LanguageDescription.JAVA;
        ClientProviderType clientProviderType = ClientProviderType.SPRING_CLOUD_OPENFEIGN;

        Swagger2FeignJavaCodegenBuilder.builder()
                .build()
                //设置数据类型的映射关系
                .typeMappings(MultipartFile.class, JavaCodeGenClassMeta.FILE)
                //自定义的类型映射
                .customJavaTypeMapping(ServiceQueryResponse.class, new Class<?>[]{ServiceResponse.class, PageInfo.class})
                .languageDescription(language)
                .clientProviderType(clientProviderType)
                .packageMapStrategy(packageMapStrategy)
                .outPath(Swagger2AssertCodegenResultUtil.getOutPath(language, clientProviderType))
                .scanPackages(packagePaths)
                .isDeletedOutputDirectory(false)
                .elementParsePostProcessors(
                        SpringCloudFeignClientPostProcessor.builder()
                                .name("exampleService")
                                .url("${test.feign.url}")
                                .decode404(false)
                                .build()
                )
                .buildCodeGenerator()
                .generate();

        Swagger2AssertCodegenResultUtil.assertGenerate(language, clientProviderType);

    }

    @Test
    void testJavaParser() {
        JavaClassMeta result = new JavaClassParser(false).parse(User.class);
        Assertions.assertNotNull(result);
    }
}
