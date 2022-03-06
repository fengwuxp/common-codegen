package test.com.wuxp.codegen.swagger3;

import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.loong.strategy.TypescriptPackageMapStrategy;
import com.wuxp.codegen.meta.annotations.factories.spring.RequestMappingMetaFactory;
import com.wuxp.codegen.meta.enums.AuthenticationType;
import com.wuxp.codegen.model.LanguageDescription;
import com.wuxp.codegen.model.languages.dart.DartClassMeta;
import com.wuxp.codegen.swagger3.builder.Swagger3FeignDartCodegenBuilder;
import com.wuxp.codegen.swagger3.example.controller.OrderController;
import com.wuxp.codegen.swagger3.example.evt.BaseQueryEvt;
import com.wuxp.codegen.swagger3.example.resp.PageInfo;
import com.wuxp.codegen.swagger3.example.resp.ServiceQueryResponse;
import com.wuxp.codegen.swagger3.example.resp.ServiceResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.*;

/**
 * 测试swagger 生成  dart的 feign api sdk
 */
@Slf4j
class Swagger3FeignSdkCodegenDartTest {


    @Test
    void testCodeGenDartApiSdk() throws Exception {

        //包名映射关系
        Map<String, String> packageMap = new LinkedHashMap<>();

        packageMap.put("com.wuxp.codegen.swagger3.**.controller", "{0}services");
        packageMap.put("com.wuxp.codegen.swagger3.**.evt", "evt");
        packageMap.put("com.wuxp.codegen.swagger3.**.domain", "domain");
        packageMap.put("com.wuxp.codegen.swagger3.**.resp", "resp");
        packageMap.put("com.wuxp.codegen.swagger3.**.enums", "enums");

        //要进行生成的源代码包名列表
        String[] packagePaths = {"com.wuxp.codegen.swagger3.**.controller"};

        Map<String, Object> classNameTransformers = new HashMap<>();
        classNameTransformers.put(OrderController.class.getSimpleName(), "OrderFeignClient");

        Map<Class<?>, List<String>> ignoreFields = new HashMap<>();
        ignoreFields.put(BaseQueryEvt.class, Collections.singletonList("queryPage"));

        RequestMappingMetaFactory.addAuthenticationTypePaths(AuthenticationType.NONE, new String[]{
                "/example_cms/get_**"
        });

        LanguageDescription language = LanguageDescription.DART;
        ClientProviderType clientProviderType = ClientProviderType.DART_FEIGN;

        Swagger3FeignDartCodegenBuilder.builder()
                //设置基础数据类型的映射关系
                .baseTypeMapping(ServiceQueryResponse.class, DartClassMeta.FUTURE)
                .baseTypeMapping(ServiceResponse.class, DartClassMeta.FUTURE)
                //自定义的类型映射
                .customJavaTypeMapping(ServiceQueryResponse.class, new Class<?>[]{ServiceResponse.class, PageInfo.class})
                .packageMapStrategy(new TypescriptPackageMapStrategy(packageMap, classNameTransformers))
                .outPath(Swagger3AssertCodegenResultUtil.getOutPath(language, clientProviderType))
                .scanPackages(packagePaths)
                .isDeletedOutputDirectory(true)
                .ignoreFieldNames(ignoreFields)
                .buildCodeGenerator()
                .generate();

        Swagger3AssertCodegenResultUtil.assertGenerate(language, clientProviderType);
    }


}
