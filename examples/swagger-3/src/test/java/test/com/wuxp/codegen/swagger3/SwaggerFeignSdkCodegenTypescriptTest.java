package test.com.wuxp.codegen.swagger3;

import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.dragon.DragonSimpleTemplateStrategy;
import com.wuxp.codegen.dragon.strategy.TypescriptPackageMapStrategy;
import com.wuxp.codegen.enums.CodeRuntimePlatform;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.TemplateFileVersion;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.model.mapping.AbstractTypeMapping;
import com.wuxp.codegen.swagger3.Swagger3CodeGenerator;
import com.wuxp.codegen.swagger3.Swagger3FeignSdkGenMatchingStrategy;
import com.wuxp.codegen.swagger3.builder.Swagger3FeignTypescriptCodegenBuilder;
import com.wuxp.codegen.swagger3.example.controller.OrderController;
import com.wuxp.codegen.swagger3.example.resp.PageInfo;
import com.wuxp.codegen.swagger3.example.resp.ServiceQueryResponse;
import com.wuxp.codegen.swagger3.example.resp.ServiceResponse;
import com.wuxp.codegen.swagger3.languages.Swagger3FeignSdkTypescriptParser;
import com.wuxp.codegen.templates.FreemarkerTemplateLoader;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.io.File;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 测试swagger 生成  typescript的 feign api sdk
 */
@Slf4j
public class SwaggerFeignSdkCodegenTypescriptTest {


    private CodeGenerator codeGenerator;

    @Before
    public void before() {

        //设置基础数据类型的映射关系
        AbstractTypeMapping.setBaseTypeMapping(ServiceQueryResponse.class, TypescriptClassMeta.PROMISE);
        AbstractTypeMapping.setBaseTypeMapping(ServiceResponse.class, TypescriptClassMeta.PROMISE);

        //自定义的类型映射
        AbstractTypeMapping.setCustomizeJavaTypeMapping(ServiceQueryResponse.class, new Class<?>[]{ServiceResponse.class, PageInfo.class});

        //包名映射关系
        Map<String, String> packageMap = new LinkedHashMap<>();

        //控制器的包所在
        packageMap.put("com.wuxp.codegen.swagger3.example.controller", "services");
        //其他类（DTO、VO等）所在的包
        packageMap.put("com.wuxp.codegen.swagger3.example", "");

        //实例化包名映射策略
        PackageMapStrategy packageMapStrategy = new TypescriptPackageMapStrategy(packageMap);

        //实例化语言解析器
        LanguageParser languageParser = new Swagger3FeignSdkTypescriptParser(packageMapStrategy, new Swagger3FeignSdkGenMatchingStrategy(), null);

        String language = LanguageDescription.TYPESCRIPT.getName();

        //实例化模板加载器
        HashMap<String, Object> sharedVariables = new HashMap<>();
        sharedVariables.put("codeRuntimePlatform", CodeRuntimePlatform.BROWSER);
        FreemarkerTemplateLoader templateLoader = new FreemarkerTemplateLoader(LanguageDescription.TYPESCRIPT, sharedVariables);


        String[] outPaths = {"codegen-result", language.toLowerCase(), "src", "api"};

        TemplateStrategy<CommonCodeGenClassMeta> templateStrategy = new DragonSimpleTemplateStrategy(
                templateLoader,
                Paths.get(System.getProperty("user.dir")).resolveSibling(String.join(File.separator, outPaths)).toString(),
                LanguageDescription.TYPESCRIPT.getSuffixName(), true);

        //要进行生成的源代码包名列表
        String[] packagePaths = {"com.wuxp.codegen.swagger3.example.controller"};

        //创建代码生成器
        this.codeGenerator = new Swagger3CodeGenerator(packagePaths, languageParser, templateStrategy, false);
    }


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
//        packageMap.put("com.wuxp.codegen.swagger3.controller", "services");
        packageMap.put("com.wuxp.codegen.swagger3.**.controller", "{0}services");
        packageMap.put("com.wuxp.codegen.swagger3.**.evt", "evt");
        packageMap.put("com.wuxp.codegen.swagger3.**.domain", "domain");
        packageMap.put("com.wuxp.codegen.swagger3.**.resp", "resp");
        packageMap.put("com.wuxp.codegen.swagger3.**.enums", "enums");
        //其他类（DTO、VO等）所在的包
//        packageMap.put("com.wuxp.codegen.swagger3.example", "");

        String language = LanguageDescription.TYPESCRIPT.getName();
        String[] outPaths = {"codegen-result", language.toLowerCase(), "src", "api"};

        //要进行生成的源代码包名列表
        String[] packagePaths = {"com.wuxp.codegen.swagger3.**.controller"};


        Map<String, Object> classNameTransformers = new HashMap<>();
        classNameTransformers.put(OrderController.class.getSimpleName(), "OrderFeignClient");

        Swagger3FeignTypescriptCodegenBuilder.builder()
                .baseTypeMapping(baseTypeMapping)
                .languageDescription(LanguageDescription.TYPESCRIPT)
                .customJavaTypeMapping(customTypeMapping)
                .packageMapStrategy(new TypescriptPackageMapStrategy(packageMap, classNameTransformers))
                .outPath(Paths.get(System.getProperty("user.dir")).resolveSibling(String.join(File.separator, outPaths)).toString())
                .scanPackages(packagePaths)
                .templateFileVersion(TemplateFileVersion.V_2_0_0)
                .isDeletedOutputDirectory(true)
                .buildCodeGenerator()
                .generate();

    }


}
