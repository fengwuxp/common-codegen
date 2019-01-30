package test.com.wuxp.codegen.typescript;

import com.wuxp.codegen.dragon.DragonCodeGenerator;
import com.wuxp.codegen.dragon.DragonSimpleTemplateStrategy;
import com.wuxp.codegen.dragon.strategy.TypescriptPackageMapStrategy;
import com.wuxp.codegen.dragon.core.CodeGenerator;
import com.wuxp.codegen.dragon.core.parser.LanguageParser;
import com.wuxp.codegen.dragon.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.dragon.core.strategy.TemplateStrategy;
import com.wuxp.codegen.swagger2.example.resp.PageInfo;
import com.wuxp.codegen.swagger2.example.resp.ServiceQueryResponse;
import com.wuxp.codegen.swagger2.example.resp.ServiceResponse;
import com.wuxp.codegen.dragon.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.dragon.model.LanguageDescription;
import com.wuxp.codegen.dragon.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.dragon.model.mapping.AbstractTypeMapping;
import com.wuxp.codegen.dragon.swagger2.Swagger2CodeGenMatchingStrategy;
import com.wuxp.codegen.dragon.swagger2.languages.TypescriptParser;
import com.wuxp.codegen.dragon.templates.FreemarkerTemplateLoader;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 测试swagger 生成 typescript的 api sdk
 */
@Slf4j
public class SwaggerCodegenTypescriptTest {


    private CodeGenerator codeGenerator;

    @Before
    public void before() {

        //设置基础数据类型的映射关系
        AbstractTypeMapping.BASE_TYPE_MAPPING.put(ServiceQueryResponse.class, TypescriptClassMeta.PROMISE);
        AbstractTypeMapping.BASE_TYPE_MAPPING.put(ServiceResponse.class, TypescriptClassMeta.PROMISE);

        //自定义的类型映射
        AbstractTypeMapping.CUSTOMIZE_TYPE_MAPPING.put(ServiceQueryResponse.class, new Class<?>[]{ServiceResponse.class, PageInfo.class});

        //包名映射关系
        Map<String, String> packageMap = new LinkedHashMap<>();

        //控制器的包所在
        packageMap.put("com.wuxp.codegen.swagger2.controller", "services");
        //其他类（DTO、VO等）所在的包
        packageMap.put("com.wuxp.codegen.swagger2.example", "");

        //实例化包名映射策略
        PackageMapStrategy packageMapStrategy = new TypescriptPackageMapStrategy(packageMap);

        //实例化语言解析器
        LanguageParser languageParser = new TypescriptParser(packageMapStrategy, new Swagger2CodeGenMatchingStrategy(), null);

        String language = LanguageDescription.TYPESCRIPT.getName();

        //实例化模板加载器
        FreemarkerTemplateLoader templateLoader = new FreemarkerTemplateLoader(language);

        TemplateStrategy<CommonCodeGenClassMeta> templateStrategy = new DragonSimpleTemplateStrategy(
                templateLoader,
                Paths.get(System.getProperty("user.dir")).resolveSibling("codegen-result\\src\\" + language.toLowerCase()+"\\api").toString(),
                LanguageDescription.TYPESCRIPT.getSuffixName());

        //要进行生成的源代码包名列表
        String[] packagePaths = {"com.wuxp.codegen.swagger2.example.controller"};

        //创建代码生成器
        this.codeGenerator = new DragonCodeGenerator(packagePaths, languageParser, templateStrategy);
    }


    @Test
    public void testCodeGenApi() {

        //生成
        codeGenerator.generate();

    }
}
