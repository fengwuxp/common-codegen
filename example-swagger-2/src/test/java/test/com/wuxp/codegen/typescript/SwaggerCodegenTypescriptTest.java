package test.com.wuxp.codegen.typescript;

import com.oaknt.codegen.OAKCodeGenerator;
import com.oaknt.codegen.OAKSimpleTemplateStrategy;
import com.wuxp.codegen.example.domain.User;
import com.oaknt.codegen.strategy.TypescriptPackageMapStrategy;
import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.parser.GenericParser;
import com.wuxp.codegen.core.parser.JavaClassParser;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.example.resp.PageInfo;
import com.wuxp.codegen.example.resp.ServiceQueryResponse;
import com.wuxp.codegen.example.resp.ServiceResponse;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.typescript.TypescriptClassMeta;
import com.wuxp.codegen.model.mapping.AbstractTypeMapping;
import com.wuxp.codegen.swagger.SwaggerCodeGenMatchingStrategy;
import com.wuxp.codegen.swagger.languages.TypescriptParser;
import com.wuxp.codegen.templates.FreemarkerTemplateLoader;
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
        packageMap.put("com.wuxp.codegen.example.controller", "services");
        //其他类（DTO、VO等）所在的包
        packageMap.put("com.wuxp.codegen.example", "");

        //实例化包名映射策略
        PackageMapStrategy packageMapStrategy = new TypescriptPackageMapStrategy(packageMap);

        //实例化语言解析器
        LanguageParser languageParser = new TypescriptParser(packageMapStrategy, new SwaggerCodeGenMatchingStrategy(), null);

        String language = LanguageDescription.TYPESCRIPT.getName();

        //实例化模板加载器
        FreemarkerTemplateLoader templateLoader = new FreemarkerTemplateLoader(language);

        TemplateStrategy<CommonCodeGenClassMeta> templateStrategy = new OAKSimpleTemplateStrategy(
                templateLoader,
                Paths.get(System.getProperty("user.dir")).resolveSibling("codegen-result-example\\src\\" + language.toLowerCase()+"\\api").toString(),
                LanguageDescription.TYPESCRIPT.getSuffixName());

        String[] packagePaths = {"com.wuxp.codegen.example.controller"};

        //创建代码生成器
        this.codeGenerator = new OAKCodeGenerator(packagePaths, languageParser, templateStrategy);
    }


    @Test
    public void testCodeGenApi() {

        //生成
        codeGenerator.generate();

    }
}
