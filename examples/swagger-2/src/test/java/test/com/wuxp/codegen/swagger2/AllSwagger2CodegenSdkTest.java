package test.com.wuxp.codegen.swagger2;

import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectClasses({
        Swagger2FeignSdkCodegenDartTest.class,
        Swagger2FeignSdkCodegenAxiosTest.class,
        Swagger2FeignSdkCodegenFeignClientTest.class,
        Swagger2FeignSdkCodegenRetrofitTest.class,
        Swagger2FeignSdkCodegenTypescriptTest.class,
        Swagger2FeignSdkCodegenUmiRequestTest.class
})
public class AllSwagger2CodegenSdkTest {

}
