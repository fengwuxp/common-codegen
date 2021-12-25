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

import java.io.File;
import java.nio.file.Paths;
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


    protected PathMatcher pathMatcher = new AntPathMatcher();

    @Test
     void testCodeGenTypescriptApiByStater() {

        //包名映射关系
        Map<String, String> packageMap = new LinkedHashMap<>();

        //控制器的包所在
//        packageMap.put("com.wuxp.codegen.swagger2.controller", "services");
        packageMap.put("com.wuxp.codegen.swagger2.**.controller", "{0}services");
        packageMap.put("com.wuxp.codegen.swagger2.**.evt", "evt");
        packageMap.put("com.wuxp.codegen.swagger2.**.domain", "domain");
        packageMap.put("com.wuxp.codegen.swagger2.**.resp", "resp");
        packageMap.put("com.wuxp.codegen.swagger2.**.enums", "enums");
        //其他类（DTO、VO等）所在的包
//        packageMap.put("com.wuxp.codegen.swagger2.example", "");

        String language = LanguageDescription.TYPESCRIPT.getName();
        String[] outPaths = {"codegen-result", language.toLowerCase(), ClientProviderType.TYPESCRIPT_FEIGN.name().toLowerCase(), "swagger2",
                "src", "api"};

        //要进行生成的源代码包名列表
        String[] packagePaths = {"com.wuxp.codegen.swagger2.**.controller"};

        Swagger2FeignTypescriptCodegenBuilder.builder()
                .languageDescription(LanguageDescription.TYPESCRIPT)
                .clientProviderType(ClientProviderType.TYPESCRIPT_FEIGN)
                // 自定义的类型映射
                .customJavaTypeMapping(ServiceQueryResponse.class, new Class<?>[]{ServiceResponse.class, PageInfo.class})
                // 基础类型映射
                .baseTypeMapping(ServiceQueryResponse.class, TypescriptClassMeta.PROMISE)
                .baseTypeMapping(ServiceResponse.class, TypescriptClassMeta.PROMISE)
                .packageMapStrategy(new TypescriptPackageMapStrategy(packageMap))
                .outPath(Paths.get(System.getProperty("user.dir")).resolveSibling(String.join(File.separator, outPaths)).toString())
                .scanPackages(packagePaths)
                .buildCodeGenerator()
                .generate();

    }

    @Test
     void testAntPathMatcher() {
        String name = BaseController.class.getName();
        String name1 = BaseEvt.class.getName();

        boolean pattern = pathMatcher.isPattern("com.wuxp.codegen.swagger2.**");
        boolean b = pathMatcher.match("com.wuxp.codegen.swagger2.**.controller**", name);
        boolean b3 = pathMatcher.match("com.wuxp.codegen.swagger2.example.controller**", name);
        String extractPathWithinPattern = pathMatcher.extractPathWithinPattern("com.wuxp.codegen.swagger2.**.controller**", name);
        Map<String, String> map = pathMatcher.extractUriTemplateVariables("com.wuxp.codegen.swagger2.**.controller**", name);
        Comparator<String> patternComparator = pathMatcher.getPatternComparator("com.wuxp.codegen.swagger2.**.controller**");
        boolean b2 = pathMatcher.match("com.wuxp.codegen.swagger2.**.controller**", name1);
        Assertions.assertTrue(b);
        Assertions.assertFalse(b2);

        System.out.println(name);
        Pattern pattern1 = Pattern.compile("com.wuxp.codegen.swagger2\\.+?(.*)\\.controller");

        Matcher matcher = pattern1.matcher(name);
        System.out.println(matcher.groupCount());
        while (matcher.find()) {
            String group = matcher.group();
            System.out.println(group);
        }


        String[] strings = "com.wuxp.codegen.swagger2.**.controller**".split("\\*\\*");

        String s = name.replaceAll(strings[0], "");
        s = s.substring(0, s.indexOf(strings[1]));
        System.out.println(s);

    }

}
