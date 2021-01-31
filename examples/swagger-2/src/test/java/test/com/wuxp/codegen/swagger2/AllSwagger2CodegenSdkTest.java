package test.com.wuxp.codegen.swagger2;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
        Swagger2FeignSdkCodegenDartTest.class,
        Swagger2FeignSdkCodegenFeignClientTest.class,
        Swagger2FeignSdkCodegenRetrofitTest.class,
        Swagger2FeignSdkCodegenTypescriptTest.class,
        Swagger2FeignSdkCodegenUmiRequestTest.class
})
public class AllSwagger2CodegenSdkTest {

}
