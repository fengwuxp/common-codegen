package test.com.wuxp.codegen.swagger3;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.loong.strategy.TypescriptPackageMapStrategy;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.swagger3.builder.Swagger3FeignTypescriptCodegenBuilder;
import com.wuxp.codegen.swagger3.example.controller.OrderController;
import com.wuxp.codegen.swagger3.example.resp.PageInfo;
import com.wuxp.codegen.swagger3.example.resp.ServiceQueryResponse;
import com.wuxp.codegen.swagger3.example.resp.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 测试swagger 生成  typescript的 umi request sdk
 */
@Slf4j
class Swagger3FeignSdkCodegenUmiRequestTest {


    @Test
    void testCodeGenTypescriptApiByStater() throws Exception {

        //包名映射关系
        Map<String, String> packageMap = new LinkedHashMap<>();

        packageMap.put("com.wuxp.codegen.swagger3.**.controller", "{0}services");
        packageMap.put("com.wuxp.codegen.swagger3.**.evt", "evt");
        packageMap.put("com.wuxp.codegen.swagger3.**.domain", "domain");
        packageMap.put("com.wuxp.codegen.swagger3.**.resp", "resp");
        packageMap.put("com.wuxp.codegen.swagger3.**.enums", "enums");

        //要进行生成的源代码包名列表
        String[] packagePaths = {"com.wuxp.codegen.swagger3.**.controller"};

        Map<String, Object> classNameTransformers = new HashMap<>();
        classNameTransformers.put(OrderController.class.getSimpleName(), "OrderFeignClient");

        LanguageDescription language = LanguageDescription.TYPESCRIPT;
        ClientProviderType clientProviderType = ClientProviderType.UMI_REQUEST;

        Swagger3FeignTypescriptCodegenBuilder.builder(true)
                .languageDescription(LanguageDescription.TYPESCRIPT)
                .clientProviderType(ClientProviderType.UMI_REQUEST)
                //设置数据类型的映射关系
                .typeMappings(ServiceQueryResponse.class, TypescriptClassMeta.PROMISE)
                .typeMappings(ServiceResponse.class, TypescriptClassMeta.PROMISE)
                //自定义的类型映射
                .customJavaTypeMapping(ServiceQueryResponse.class, new Class<?>[]{ServiceResponse.class, PageInfo.class})
                .packageMapStrategy(new TypescriptPackageMapStrategy(packageMap, classNameTransformers))
                .outPath(Swagger3AssertCodegenResultUtils.getOutPath(language, clientProviderType))
                .scanPackages(packagePaths)
                .otherCodegenClassMetas(TypescriptClassMeta.ENUM)
                .sharedVariables("enumImportPath", "../" + TypescriptClassMeta.ENUM.getName())
                .isDeletedOutputDirectory(true)
                .buildCodeGenerator()
                .generate();

        Swagger3AssertCodegenResultUtils.assertGenerate(language, clientProviderType);
    }


}
