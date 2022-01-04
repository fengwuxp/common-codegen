package test.com.wuxp.codegen.swagger3;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.loong.strategy.JavaPackageMapStrategy;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import com.wuxp.codegen.swagger3.builder.Swagger3FeignJavaCodegenBuilder;
import com.wuxp.codegen.swagger3.example.controller.HelloController;
import com.wuxp.codegen.swagger3.example.controller.OrderController;
import com.wuxp.codegen.swagger3.example.resp.PageInfo;
import com.wuxp.codegen.swagger3.example.resp.ServiceQueryResponse;
import com.wuxp.codegen.swagger3.example.resp.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
class Swagger3FeignSdkCodegenRetrofitTest {


    @Test
    void testCodeGenRetrofitApiByStater() throws Exception {

        //包名映射关系
        Map<String, String> packageMap = new LinkedHashMap<>();

        //控制器的包所在
        packageMap.put("com.wuxp.codegen.swagger3.example.controller", "com.wuxp.codegen.swagger3.retrofits");
        //其他类（DTO、VO等）所在的包
        String basePackageName = "com.wuxp.codegen.swagger3";
        packageMap.put("com.wuxp.codegen.swagger3.example", basePackageName);


        //要进行生成的源代码包名列表
        String[] packagePaths = {"com.wuxp.codegen.swagger3.example.controller"};

        LanguageDescription language = LanguageDescription.JAVA_ANDROID;
        ClientProviderType clientProviderType = ClientProviderType.RETROFIT;

        Swagger3FeignJavaCodegenBuilder.builder()
                .useRxJava(true)
                .build()
                .languageDescription(language)
                .clientProviderType(clientProviderType)
                //设置基础数据类型的映射关系
                .baseTypeMapping(CommonsMultipartFile.class, JavaCodeGenClassMeta.FILE)
                //自定义的类型映射
                .customJavaTypeMapping(ServiceQueryResponse.class, new Class<?>[]{ServiceResponse.class, PageInfo.class})
                .packageMapStrategy(new JavaPackageMapStrategy(packageMap, basePackageName))
                .outPath(Swagger3AssertCodegenResultUtil.getOutPath(language, clientProviderType))
                .scanPackages(packagePaths)
                .ignoreClasses(new Class<?>[]{HelloController.class, OrderController.class})
                .isDeletedOutputDirectory(false)
                .buildCodeGenerator()
                .generate();

        Swagger3AssertCodegenResultUtil.assertGenerate(language, clientProviderType);

    }

}
