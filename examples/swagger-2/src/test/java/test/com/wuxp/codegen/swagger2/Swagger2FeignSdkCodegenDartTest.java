package test.com.wuxp.codegen.swagger2;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.meta.annotations.factories.spring.RequestMappingMetaFactory;
import com.wuxp.codegen.meta.enums.AuthenticationType;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.dart.DartClassMeta;
import com.wuxp.codegen.swagger2.builder.Swagger2FeignDartCodegenBuilder;
import com.wuxp.codegen.swagger2.example.evt.BaseQueryEvt;
import com.wuxp.codegen.swagger2.example.resp.PageInfo;
import com.wuxp.codegen.swagger2.example.resp.ServiceQueryResponse;
import com.wuxp.codegen.swagger2.example.resp.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
class Swagger2FeignSdkCodegenDartTest {


    @Test
    void testCodeGenDartApiByStater() throws Exception {

        //要进行生成的源代码包名列表
        String[] packagePaths = {"com.wuxp.codegen.swagger2.**.controller"};

        Map<Class<?>, List<String>> ignoreFields = new HashMap<>();
        ignoreFields.put(BaseQueryEvt.class, Collections.singletonList("queryPage"));

        Map<DartClassMeta, List<String>> typeAlias = new HashMap<>();
        typeAlias.put(DartClassMeta.BUILT_LIST, Collections.singletonList("PageInfo"));

        RequestMappingMetaFactory.addAuthenticationTypePaths(AuthenticationType.NONE, new String[]{
                "/example_cms/get_**"
        });

        LanguageDescription language = LanguageDescription.DART;
        ClientProviderType clientProviderType = ClientProviderType.DART_FEIGN;

        Swagger2FeignDartCodegenBuilder.builder()
                .typeAlias(typeAlias)
                //设置基础数据类型的映射关系
                .baseTypeMapping(ServiceQueryResponse.class, DartClassMeta.FUTURE)
                .baseTypeMapping(ServiceResponse.class, DartClassMeta.FUTURE)
                //自定义的类型映射
                .customJavaTypeMapping(ServiceQueryResponse.class, new Class<?>[]{ServiceResponse.class, PageInfo.class})
                .outPath(Swagger2AssertCodegenResultUtil.getOutPath(language, clientProviderType))
                .scanPackages(packagePaths)
                .isDeletedOutputDirectory(true)
                .ignoreFieldNames(ignoreFields)
                .buildCodeGenerator()
                .generate();

        Swagger2AssertCodegenResultUtil.assertGenerate(language, clientProviderType);
    }


}
