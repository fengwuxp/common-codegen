package test.com.wuxp.codegen;

import com.oaknt.codegen.OAKCodeGenerator;
import com.oaknt.codegen.OAKSimpleTemplateStrategy;
import com.oaknt.codegen.strategy.TypescriptPackageMapStrategy;
import com.wuxp.codegen.core.CodeGenerator;
import com.wuxp.codegen.core.parser.LanguageParser;
import com.wuxp.codegen.core.strategy.PackageMapStrategy;
import com.wuxp.codegen.core.strategy.TemplateStrategy;
import com.wuxp.codegen.model.CommonCodeGenClassMeta;
import com.wuxp.codegen.model.LanguageNames;
import com.wuxp.codegen.swagger.languages.TypescriptParser;
import com.wuxp.codegen.templates.FreemarkerTemplateLoader;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 测试swagger 生成
 */
@Slf4j
public class SwaggerCodegenTest {


    private CodeGenerator codeGenerator;

    @Before
    public void before() {


        Map<String, String> packageMap = new LinkedHashMap<>();
        packageMap.put("com.wuxp.codegen.example.controller", "services");
        packageMap.put("com.wuxp.codegen.example", "api");

        PackageMapStrategy packageMapStrategy = new TypescriptPackageMapStrategy(packageMap);

        LanguageParser languageParser = new TypescriptParser(packageMapStrategy, null);

        TemplateStrategy<CommonCodeGenClassMeta> templateStrategy = new OAKSimpleTemplateStrategy(new FreemarkerTemplateLoader(LanguageNames.TYPESCRIPT));

        String[] packagePaths = {"com.wuxp.codegen.example.controller"};

        this.codeGenerator = new OAKCodeGenerator(packagePaths, languageParser, templateStrategy);
    }


    @Test
    public void testCodeGenApi() {

        codeGenerator.generate();


    }
}
