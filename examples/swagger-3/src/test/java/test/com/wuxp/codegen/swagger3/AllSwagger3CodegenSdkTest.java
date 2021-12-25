package test.com.wuxp.codegen.swagger3;


import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectClasses({
        Swagger3FeignSdkCodegenDartTest.class,
        Swagger3FeignSdkCodegenFeignClientTest.class,
        Swagger3FeignSdkCodegenRetrofitTest.class,
        Swagger3FeignSdkCodegenTypescriptTest.class,
        Swagger3FeignSdkCodegenUmiRequestTest.class
})
public class AllSwagger3CodegenSdkTest {

}
