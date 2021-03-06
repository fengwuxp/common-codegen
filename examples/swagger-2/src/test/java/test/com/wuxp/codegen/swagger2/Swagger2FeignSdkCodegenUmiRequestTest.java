package test.com.wuxp.codegen.swagger2;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.languages.typescript.UmiRequestEnhancedProcessor;
import com.wuxp.codegen.loong.strategy.TypescriptPackageMapStrategy;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.swagger2.builder.Swagger2FeignTypescriptCodegenBuilder;
import com.wuxp.codegen.swagger2.example.resp.PageInfo;
import com.wuxp.codegen.swagger2.example.resp.ServiceQueryResponse;
import com.wuxp.codegen.swagger2.example.resp.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 测试swagger 生成  typescript的 umi request  sdk
 */
@Slf4j
public class Swagger2FeignSdkCodegenUmiRequestTest {

    @Test
    public void testCodeGenTypescriptApiByStater() {

        //包名映射关系
        Map<String, String> packageMap = new LinkedHashMap<>();

        //控制器的包所在
        packageMap.put("com.wuxp.codegen.swagger2.**.controller", "{0}services");
        packageMap.put("com.wuxp.codegen.swagger2.**.evt", "evt");
        packageMap.put("com.wuxp.codegen.swagger2.**.domain", "domain");
        packageMap.put("com.wuxp.codegen.swagger2.**.resp", "resp");
        packageMap.put("com.wuxp.codegen.swagger2.**.enums", "enums");
        packageMap.put("test.com.wuxp.codegen.typescript", "enums");

        String language = LanguageDescription.TYPESCRIPT.getName();
        String[] outPaths = {"codegen-result", language.toLowerCase(), ClientProviderType.UMI_REQUEST.name().toLowerCase(), "swagger2", "src",
                "api"};

        //要进行生成的源代码包名列表
        String[] packagePaths = {"com.wuxp.codegen.swagger2.**.controller"};

        Swagger2FeignTypescriptCodegenBuilder.builder()
                .languageDescription(LanguageDescription.TYPESCRIPT)
                .clientProviderType(ClientProviderType.UMI_REQUEST)
                // 基础类型映射
                .baseTypeMapping(ServiceQueryResponse.class, TypescriptClassMeta.PROMISE)
                .baseTypeMapping(ServiceResponse.class, TypescriptClassMeta.PROMISE)
                // 自定义的类型映射
                .customJavaTypeMapping(ServiceQueryResponse.class, new Class<?>[]{ServiceResponse.class, PageInfo.class})
                .packageMapStrategy(new TypescriptPackageMapStrategy(packageMap))
                .outPath(Paths.get(System.getProperty("user.dir")).resolveSibling(String.join(File.separator, outPaths)).toString())
                .scanPackages(packagePaths)
//                .otherCodegenClassMetas(TypescriptClassMeta.ENUM)
//                .sharedVariables("enumImportPath", "../" + TypescriptClassMeta.ENUM.getName())
                .languageEnhancedProcessors(new UmiRequestEnhancedProcessor())
                .buildCodeGenerator()
                .generate();

    }

}
