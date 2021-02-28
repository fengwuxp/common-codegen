package test.com.wuxp.codegen.swagger2;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.config.CodegenConfig;
import com.wuxp.codegen.core.config.CodegenConfigHolder;
import com.wuxp.codegen.core.parser.JavaClassParser;
import com.wuxp.codegen.enums.EnumCommentEnhancer;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import com.wuxp.codegen.swagger2.builder.Swagger2FeignJavaCodegenBuilder;
import com.wuxp.codegen.swagger2.example.domain.User;
import com.wuxp.codegen.swagger2.example.resp.PageInfo;
import com.wuxp.codegen.swagger2.example.resp.ServiceQueryResponse;
import com.wuxp.codegen.swagger2.example.resp.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class Swagger2FeignSdkCodegenRetrofitTest {


    @Test
    public void testCodeGenRetrofitApiByStater() {

        //包名映射关系
        Map<String, String> packageMap = new LinkedHashMap<>();

        //控制器的包所在
        packageMap.put("com.wuxp.codegen.swagger2.example.controller", "com.wuxp.codegen.swagger2.retrofits");
        //其他类（DTO、VO等）所在的包
        String basePackageName = "com.wuxp.codegen.swagger2";
        packageMap.put("com.wuxp.codegen.swagger2.example", basePackageName);

        String language = LanguageDescription.JAVA_ANDROID.getName();
        String[] outPaths = {"codegen-result", language.toLowerCase(), ClientProviderType.RETROFIT.name().toLowerCase(), "swagger2", "src"};

        //要进行生成的源代码包名列表
        String[] packagePaths = {"com.wuxp.codegen.swagger2.example.controller"};
        CodegenConfig codegenConfig = CodegenConfig.builder().basePackages(Collections.singletonList("com.wuxp.codegen.swagger2")).build();
        CodegenConfigHolder.setConfig(codegenConfig);
        Swagger2FeignJavaCodegenBuilder.builder()
                .useRxJava(true)
                .build()
                // 基础类型映射
                .baseTypeMapping(CommonsMultipartFile.class, JavaCodeGenClassMeta.FILE)
                //自定义的类型映射
                .customJavaTypeMapping(ServiceQueryResponse.class, new Class<?>[]{ServiceResponse.class, PageInfo.class})
                .languageDescription(LanguageDescription.JAVA_ANDROID)
                .clientProviderType(ClientProviderType.RETROFIT)
                .languageEnhancedProcessors(new EnumCommentEnhancer())
                .outPath(Paths.get(System.getProperty("user.dir")).resolveSibling(String.join(File.separator, outPaths)).toString())
                .scanPackages(packagePaths)
                .isDeletedOutputDirectory(false)
                .enableFieldUnderlineStyle(true)
                .buildCodeGenerator()
                .generate();

    }

    @Test
    public void testJavaParser() {

        JavaClassMeta parse = new JavaClassParser(false).parse(User.class);

        log.debug("{}", parse);
    }
}
