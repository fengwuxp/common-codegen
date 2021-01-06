package test.com.wuxp.codegen.swagger3;



import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;


@RunWith(Suite.class)
@SuiteClasses({
    Swagger3FeignSdkCodegenDartTest.class,
    Swagger3FeignSdkCodegenFeignClientTest.class,
    Swagger3FeignSdkCodegenRetrofitTest.class,
    Swagger3FeignSdkCodegenTypescriptTest.class,
    Swagger3FeignSdkCodegenUmiRequestTest.class
})
public class AllSwagger3CodegenSdkTest {

}
