package test.com.wuxp.codegen.swagger2;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.languages.typescript.UmiRequestMethodDefinitionPostProcessor;
import com.wuxp.codegen.loong.strategy.TypescriptPackageMapStrategy;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.swagger2.builder.Swagger2FeignTypescriptCodegenBuilder;
import com.wuxp.codegen.swagger2.example.resp.PageInfo;
import com.wuxp.codegen.swagger2.example.resp.ServiceQueryResponse;
import com.wuxp.codegen.swagger2.example.resp.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 测试 swagger 生成  typescript 的 axios sdk
 */
@Slf4j
class Swagger2FeignSdkCodegenAxiosTest {

    @Test
    void testCodeGenTypescriptApiByStater() throws Exception {

        //包名映射关系
        Map<String, String> packageMap = new LinkedHashMap<>();

        //控制器的包所在
        packageMap.put("com.wuxp.codegen.swagger2.**.controller", "{0}services");
        packageMap.put("com.wuxp.codegen.swagger2.**.evt", "evt");
        packageMap.put("com.wuxp.codegen.swagger2.**.domain", "domain");
        packageMap.put("com.wuxp.codegen.swagger2.**.resp", "resp");
        packageMap.put("com.wuxp.codegen.swagger2.**.enums", "enums");
        packageMap.put("test.com.wuxp.codegen.typescript", "enums");

        //要进行生成的源代码包名列表
        String[] packagePaths = {"com.wuxp.codegen.swagger2.**.controller"};

        LanguageDescription language = LanguageDescription.TYPESCRIPT;
        ClientProviderType clientProviderType = ClientProviderType.AXIOS;

        Swagger2FeignTypescriptCodegenBuilder.builder()
                .languageDescription(language)
                .clientProviderType(clientProviderType)
                // 基础类型映射
                .typeMappings(ServiceQueryResponse.class, TypescriptClassMeta.PROMISE)
                .typeMappings(ServiceResponse.class, TypescriptClassMeta.PROMISE)
                // 自定义的类型映射
                .customJavaTypeMapping(ServiceQueryResponse.class, new Class<?>[]{ServiceResponse.class, PageInfo.class})
                .packageMapStrategy(new TypescriptPackageMapStrategy(packageMap))
                .outPath(Swagger2AssertCodegenResultUtil.getOutPath(language, clientProviderType))
                .scanPackages(packagePaths)
                .elementParsePostProcessors(new UmiRequestMethodDefinitionPostProcessor())
                .buildCodeGenerator()
                .generate();

        Swagger2AssertCodegenResultUtil.assertGenerate(language, clientProviderType);

    }

}
