package test.com.wuxp.codegen.swagger3;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.languages.typescript.UmiRequestMethodDefinitionPostProcessor;
import com.wuxp.codegen.loong.strategy.TypescriptPackageMapStrategy;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.swagger3.builder.Swagger3FeignTypescriptCodegenBuilder;
import com.wuxp.codegen.swagger3.example.resp.PageInfo;
import com.wuxp.codegen.swagger3.example.resp.ServiceQueryResponse;
import com.wuxp.codegen.swagger3.example.resp.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 测试swagger 生成  typescript的 axios request sdk
 */
@Slf4j
class Swagger3FeignSdkCodegenAxiosTest {


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

        LanguageDescription language = LanguageDescription.TYPESCRIPT;
        ClientProviderType clientProviderType = ClientProviderType.AXIOS;

        Swagger3FeignTypescriptCodegenBuilder.builder()
                .languageDescription(language)
                .clientProviderType(clientProviderType)
                // 基础类型映射
                .typeMappings(ServiceQueryResponse.class, TypescriptClassMeta.PROMISE)
                .typeMappings(ServiceResponse.class, TypescriptClassMeta.PROMISE)
                // 自定义的类型映射
                .customJavaTypeMapping(ServiceQueryResponse.class, new Class<?>[]{ServiceResponse.class, PageInfo.class})
                .packageMapStrategy(new TypescriptPackageMapStrategy(packageMap))
                .outPath(Swagger3AssertCodegenResultUtil.getOutPath(language, clientProviderType))
                .scanPackages(packagePaths)
                .elementParsePostProcessors(new UmiRequestMethodDefinitionPostProcessor())
                .buildCodeGenerator()
                .generate();
        Swagger3AssertCodegenResultUtil.assertGenerate(language, clientProviderType);
    }


}
