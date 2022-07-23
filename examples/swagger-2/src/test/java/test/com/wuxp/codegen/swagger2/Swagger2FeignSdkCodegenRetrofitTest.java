package test.com.wuxp.codegen.swagger2;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.config.CodegenConfig;
import com.wuxp.codegen.core.config.CodegenConfigHolder;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.java.codegen.JavaCodeGenClassMeta;
import com.wuxp.codegen.swagger2.builder.Swagger2FeignJavaCodegenBuilder;
import com.wuxp.codegen.swagger2.example.resp.PageInfo;
import com.wuxp.codegen.swagger2.example.resp.ServiceQueryResponse;
import com.wuxp.codegen.swagger2.example.resp.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;

@Slf4j
class Swagger2FeignSdkCodegenRetrofitTest {

    @Test
    void testCodeGenRetrofitApiByStater() throws Exception {

        //要进行生成的源代码包名列表
        String[] packagePaths = {"com.wuxp.codegen.swagger2.example.controller"};
        CodegenConfig codegenConfig = CodegenConfig.builder().basePackages(Collections.singletonList("com.wuxp.codegen.swagger2")).build();
        CodegenConfigHolder.setConfig(codegenConfig);

        ClientProviderType clientProviderType = ClientProviderType.RETROFIT;
        LanguageDescription language = LanguageDescription.JAVA;

        Swagger2FeignJavaCodegenBuilder.builder()
                .useRxJava(true)
                .build()
                // 基础类型映射
                .baseTypeMapping(MultipartFile.class, JavaCodeGenClassMeta.FILE)
                //自定义的类型映射
                .customJavaTypeMapping(ServiceQueryResponse.class, new Class<?>[]{ServiceResponse.class, PageInfo.class})
                .languageDescription(language)
                .clientProviderType(clientProviderType)
                .outPath(Swagger2AssertCodegenResultUtil.getOutPath(LanguageDescription.JAVA, ClientProviderType.RETROFIT))
                .scanPackages(packagePaths)
                .isDeletedOutputDirectory(false)
                .enableFieldUnderlineStyle(true)
                .buildCodeGenerator()
                .generate();

        Swagger2AssertCodegenResultUtil.assertGenerate(LanguageDescription.JAVA, ClientProviderType.RETROFIT);
    }

}
