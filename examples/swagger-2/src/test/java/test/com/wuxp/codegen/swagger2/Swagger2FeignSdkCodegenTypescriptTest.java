package test.com.wuxp.codegen.swagger2;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.loong.strategy.TypescriptPackageMapStrategy;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.swagger2.builder.Swagger2FeignTypescriptCodegenBuilder;
import com.wuxp.codegen.swagger2.example.controller.BaseController;
import com.wuxp.codegen.swagger2.example.evt.BaseEvt;
import com.wuxp.codegen.swagger2.example.resp.PageInfo;
import com.wuxp.codegen.swagger2.example.resp.ServiceQueryResponse;
import com.wuxp.codegen.swagger2.example.resp.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 测试swagger 生成  typescript的 feign api sdk
 */
@Slf4j
class Swagger2FeignSdkCodegenTypescriptTest {

    @Test
    void testCodeGenTypescriptApiByStater() throws Exception {
        codegen(ClientProviderType.TYPESCRIPT_FEIGN);
        codegen(ClientProviderType.TYPESCRIPT_FEIGN_FUNC);
    }

    private static void codegen( ClientProviderType clientProviderType) throws Exception {
        // 包名映射关系
        Map<String, String> packageMap = new LinkedHashMap<>();
        packageMap.put("com.wuxp.codegen.swagger2.**.controller", "{0}services");
        packageMap.put("com.wuxp.codegen.swagger2.**.evt", "evt");
        packageMap.put("com.wuxp.codegen.swagger2.**.domain", "domain");
        packageMap.put("com.wuxp.codegen.swagger2.**.resp", "resp");
        packageMap.put("com.wuxp.codegen.swagger2.**.enums", "enums");

        //要进行生成的源代码包名列表
        String[] packagePaths = {"com.wuxp.codegen.swagger2.**.controller"};
        LanguageDescription language = LanguageDescription.TYPESCRIPT;
        Swagger2FeignTypescriptCodegenBuilder.builder(true)
                .languageDescription(language)
                .clientProviderType(clientProviderType)
                // 自定义的类型映射
                .customJavaTypeMapping(ServiceQueryResponse.class, new Class<?>[]{ServiceResponse.class, PageInfo.class})
                // 基础类型映射
                .typeMappings(ServiceQueryResponse.class, TypescriptClassMeta.PROMISE)
                .typeMappings(ServiceResponse.class, TypescriptClassMeta.PROMISE)
                .packageMapStrategy(new TypescriptPackageMapStrategy(packageMap))
                .outPath(Swagger2AssertCodegenResultUtil.getOutPath(language, clientProviderType))
                .scanPackages(packagePaths)
                .buildCodeGenerator()
                .generate();
        Swagger2AssertCodegenResultUtil.assertGenerate(language, clientProviderType);
    }


}
