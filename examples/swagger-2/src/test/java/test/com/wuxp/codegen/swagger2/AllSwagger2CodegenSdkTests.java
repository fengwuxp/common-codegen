package test.com.wuxp.codegen.swagger2;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({
        Swagger2FeignSdkCodegenDartTest.class,
        Swagger2FeignSdkCodegenFeignClientTest.class,
        Swagger2FeignSdkCodegenRetrofitTest.class,
        Swagger2FeignSdkCodegenAxiosTest.class,
        Swagger2FeignSdkCodegenTypescriptTest.class,
        Swagger2FeignSdkCodegenUmiRequestTest.class
})
class AllSwagger2CodegenSdkTests {

}
