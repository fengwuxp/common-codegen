package test.com.wuxp.codegen.swagger3;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.languages.typescript.UmiRequestEnhancedProcessor;
import com.wuxp.codegen.loong.strategy.TypescriptPackageMapStrategy;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.swagger3.builder.Swagger3FeignTypescriptCodegenBuilder;
import com.wuxp.codegen.swagger3.example.maven.controller.OrderController;
import com.wuxp.codegen.swagger3.example.maven.resp.PageInfo;
import com.wuxp.codegen.swagger3.example.maven.resp.ServiceQueryResponse;
import com.wuxp.codegen.swagger3.example.maven.resp.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 测试swagger 生成  typescript的 umi request sdk
 */
@Slf4j
public class Swagger3FeignSdkCodegenUmiRequestTest {


    @Test
    public void testCodeGenTypescriptApiByStater() {

        //包名映射关系
        Map<String, String> packageMap = new LinkedHashMap<>();

        //控制器的包所在
//        packageMap.put("com.wuxp.codegen.swagger3.controller", "services");
        packageMap.put("com.wuxp.codegen.swagger3.**.controller", "{0}services");
        packageMap.put("com.wuxp.codegen.swagger3.**.evt", "evt");
        packageMap.put("com.wuxp.codegen.swagger3.**.domain", "domain");
        packageMap.put("com.wuxp.codegen.swagger3.**.resp", "resp");
        packageMap.put("com.wuxp.codegen.swagger3.**.enums", "enums");
        //其他类（DTO、VO等）所在的包
//        packageMap.put("com.wuxp.codegen.swagger3.example", "");

        String language = LanguageDescription.TYPESCRIPT.getName();
        String[] outPaths = {"codegen-result", language.toLowerCase(), ClientProviderType.UMI_REQUEST.name().toLowerCase(), "swagger3", "src",
                "api"};

        //要进行生成的源代码包名列表
        String[] packagePaths = {"com.wuxp.codegen.swagger3.**.controller"};

        Map<String, Object> classNameTransformers = new HashMap<>();
        classNameTransformers.put(OrderController.class.getSimpleName(), "OrderFeignClient");

        Swagger3FeignTypescriptCodegenBuilder.builder()
                .languageDescription(LanguageDescription.TYPESCRIPT)
                .clientProviderType(ClientProviderType.UMI_REQUEST)
                //设置基础数据类型的映射关系
                .baseTypeMapping(ServiceQueryResponse.class, TypescriptClassMeta.PROMISE)
                .baseTypeMapping(ServiceResponse.class, TypescriptClassMeta.PROMISE)
                //自定义的类型映射
                .customJavaTypeMapping(ServiceQueryResponse.class, new Class<?>[]{ServiceResponse.class, PageInfo.class})
                .packageMapStrategy(new TypescriptPackageMapStrategy(packageMap, classNameTransformers))
                .outPath(Paths.get(System.getProperty("user.dir")).resolveSibling(String.join(File.separator, outPaths)).toString())
                .scanPackages(packagePaths)
                .otherCodegenClassMetas(TypescriptClassMeta.ENUM)
                .sharedVariables("enumImportPath", "../" + TypescriptClassMeta.ENUM.getName())
                .languageEnhancedProcessors(new UmiRequestEnhancedProcessor())
                .isDeletedOutputDirectory(true)
                .buildCodeGenerator()
                .generate();

    }


}
