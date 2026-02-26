package test.com.wuxp.codegen.swagger3;


import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        Swagger3FeignSdkCodegenDartTest.class,
        Swagger3FeignSdkCodegenFeignClientTest.class,
        Swagger3FeignSdkCodegenSpringHttpClientTest.class,
        Swagger3FeignSdkCodegenRetrofitTest.class,
        Swagger3FeignSdkCodegenAxiosTest.class,
        Swagger3FeignSdkCodegenTypescriptTest.class,
        Swagger3FeignSdkCodegenUmiRequestTest.class
})
public class AllSwagger3CodegenSdkTests {

}
