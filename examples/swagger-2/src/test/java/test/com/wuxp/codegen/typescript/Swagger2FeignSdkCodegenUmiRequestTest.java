package test.com.wuxp.codegen.typescript;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.dragon.strategy.TypescriptPackageMapStrategy;
import com.wuxp.codegen.languages.typescript.UmiRequestEnhancedProcessor;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 测试swagger 生成  typescript的 umi request  sdk
 */
@Slf4j
public class Swagger2FeignSdkCodegenUmiRequestTest {

    @Test
    public void testCodeGenTypescriptApiByStater() {

        //设置基础数据类型的映射关系
        Map<Class<?>, CommonCodeGenClassMeta> baseTypeMapping = new HashMap<>();
        baseTypeMapping.put(ServiceQueryResponse.class, TypescriptClassMeta.PROMISE);
        baseTypeMapping.put(ServiceResponse.class, TypescriptClassMeta.PROMISE);

        //自定义的类型映射
        Map<Class<?>, Class<?>[]> customTypeMapping = new HashMap<>();
        customTypeMapping.put(ServiceQueryResponse.class, new Class<?>[]{ServiceResponse.class, PageInfo.class});

        //包名映射关系
        Map<String, String> packageMap = new LinkedHashMap<>();

        //控制器的包所在
//        packageMap.put("com.wuxp.codegen.swagger2.controller", "services");
        packageMap.put("com.wuxp.codegen.swagger2.**.controller", "{0}services");
        packageMap.put("com.wuxp.codegen.swagger2.**.evt", "evt");
        packageMap.put("com.wuxp.codegen.swagger2.**.domain", "domain");
        packageMap.put("com.wuxp.codegen.swagger2.**.resp", "resp");
        packageMap.put("com.wuxp.codegen.swagger2.**.enums", "enums");
        packageMap.put("test.com.wuxp.codegen.typescript", "enums");
        //其他类（DTO、VO等）所在的包
//        packageMap.put("com.wuxp.codegen.swagger2.example", "");

        String language = LanguageDescription.TYPESCRIPT.getName();
        String[] outPaths = {"codegen-result", language.toLowerCase(), ClientProviderType.UMI_REQUEST.name().toLowerCase(), "swagger2", "src", "api"};

        //要进行生成的源代码包名列表
        String[] packagePaths = {"com.wuxp.codegen.swagger2.**.controller"};

        Swagger2FeignTypescriptCodegenBuilder.builder()
                .baseTypeMapping(baseTypeMapping)
                .languageDescription(LanguageDescription.TYPESCRIPT)
                .clientProviderType(ClientProviderType.UMI_REQUEST)
                .customJavaTypeMapping(customTypeMapping)
                .packageMapStrategy(new TypescriptPackageMapStrategy(packageMap))
                .outPath(Paths.get(System.getProperty("user.dir")).resolveSibling(String.join(File.separator, outPaths)).toString())
                .scanPackages(packagePaths)
                .otherCodegenClassMetas(TypescriptClassMeta.ENUM)
                .sharedVariables("enumImportPath", "../" + TypescriptClassMeta.ENUM.getName())
                .languageEnhancedProcessor(new UmiRequestEnhancedProcessor())
                .buildCodeGenerator()
                .generate();

    }

}
