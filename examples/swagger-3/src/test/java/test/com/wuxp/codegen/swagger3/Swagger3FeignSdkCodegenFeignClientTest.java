package test.com.wuxp.codegen.swagger3;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.languages.ResetOnlyImportMetaPostProcessor;
import com.wuxp.codegen.loong.strategy.JavaPackageMapStrategy;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import com.wuxp.codegen.swagger3.builder.Swagger3FeignJavaCodegenBuilder;
import com.wuxp.codegen.swagger3.example.evt.QueryOrderEvt;
import com.wuxp.codegen.swagger3.example.resp.PageInfo;
import com.wuxp.codegen.swagger3.example.resp.ServiceQueryResponse;
import com.wuxp.codegen.swagger3.example.resp.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class Swagger3FeignSdkCodegenFeignClientTest {


    @Test
    public void testCodeGenFeignClientByStater() {

        //包名映射关系
        Map<String, String> packageMap = new LinkedHashMap<>();

        //控制器的包所在
        packageMap.put("com.wuxp.codegen.swagger3.example.controller", "com.wuxp.codegen.swagger3.clients");
        //其他类（DTO、VO等）所在的包
        String basePackageName = "com.wuxp.codegen.swagger3";
        packageMap.put("com.wuxp.codegen.swagger3.example", basePackageName);

        String language = LanguageDescription.JAVA.getName();
        String[] outPaths = {"codegen-result", language.toLowerCase(), ClientProviderType.SPRING_CLOUD_OPENFEIGN.name().toLowerCase(),
                "swagger3", "src"};

        //要进行生成的源代码包名列表
        String[] packagePaths = {"com.wuxp.codegen.swagger3.example.maven.controller"};

        JavaPackageMapStrategy packageMapStrategy = new JavaPackageMapStrategy(packageMap, basePackageName);
        packageMapStrategy.setFileNamSuffix("FeignClient");
        Swagger3FeignJavaCodegenBuilder.builder()
                .useRxJava(true)
                .build()
                .languageDescription(LanguageDescription.JAVA)
                .clientProviderType(ClientProviderType.SPRING_CLOUD_OPENFEIGN)
                //设置基础数据类型的映射关系
                .baseTypeMapping(CommonsMultipartFile.class, JavaCodeGenClassMeta.FILE)
                //自定义的类型映射
                .customJavaTypeMapping(ServiceQueryResponse.class, new Class<?>[]{ServiceResponse.class, PageInfo.class})
                .packageMapStrategy(packageMapStrategy)
                .outPath(Paths.get(System.getProperty("user.dir")).resolveSibling(String.join(File.separator, outPaths)).toString())
                .scanPackages(packagePaths)
//                .ignoreClasses(new Class<?>[]{HelloController.class, OrderController.class})
                .elementParsePostProcessors(new ResetOnlyImportMetaPostProcessor(source -> QueryOrderEvt.class == source))
                .isDeletedOutputDirectory(false)
                .buildCodeGenerator()
                .generate();

    }

}
