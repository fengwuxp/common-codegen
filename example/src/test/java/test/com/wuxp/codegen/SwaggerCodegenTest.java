package test.com.wuxp.codegen;

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
 * 测试swagger 生成
 */
@Slf4j
public class SwaggerCodegenTest {


    private CodeGenerator codeGenerator;

    @Before
    public void before() {


        AbstractTypeMapping.BASE_TYPE_MAPPING.put(ServiceQueryResponse.class, TypescriptClassMeta.PROMISE);
        AbstractTypeMapping.BASE_TYPE_MAPPING.put(ServiceResponse.class, TypescriptClassMeta.PROMISE);

        AbstractTypeMapping.CUSTOMIZE_TYPE_MAPPING.put(ServiceQueryResponse.class,new Class<?>[]{ServiceResponse.class, PageInfo.class});

        Map<String, String> packageMap = new LinkedHashMap<>();
        //控制器的包所在
        packageMap.put("com.wuxp.codegen.example.controller", "services");

        //其他类（DTO）所在的包
        packageMap.put("com.wuxp.codegen.example", "");

        PackageMapStrategy packageMapStrategy = new TypescriptPackageMapStrategy(packageMap);

        LanguageParser languageParser = new TypescriptParser(packageMapStrategy, new SwaggerCodeGenMatchingStrategy(), null);

        FreemarkerTemplateLoader templateLoader = new FreemarkerTemplateLoader(LanguageDescription.TYPESCRIPT.getName());
        TemplateStrategy<CommonCodeGenClassMeta> templateStrategy = new OAKSimpleTemplateStrategy(
                templateLoader, Paths.get(System.getProperty("user.dir")).resolveSibling("web-example\\src\\api").toString(),
                LanguageDescription.TYPESCRIPT.getSuffixName());

        String[] packagePaths = {"com.wuxp.codegen.example.controller"};

        this.codeGenerator = new OAKCodeGenerator(packagePaths, languageParser, templateStrategy);
    }

    protected GenericParser<JavaClassMeta, Class<?>> genericParser = new JavaClassParser(false);

    @Test
    public void testCodeGenApi() {

        JavaClassMeta parse = genericParser.parse(User.class);

        codeGenerator.generate();

    }
}
