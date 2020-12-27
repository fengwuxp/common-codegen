package test.com.wuxp.codegen.processor;

import com.wuxp.codegen.annotation.processor.AnnotationProcessor;
import com.wuxp.codegen.annotation.processor.javax.NotNullProcessor;
import com.wuxp.codegen.annotation.processor.spring.RequestMappingProcessor;
import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.CodegenBuilder;
import com.wuxp.codegen.core.config.CodegenGlobalConfig;
import org.junit.Test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;


public class RequestMappingProcessorTest {


    AnnotationProcessor<RequestMappingProcessor.RequestMappingMate, Annotation> annotationProcessor = new RequestMappingProcessor();

    @Test
    public void testProcess() {
        CodegenGlobalConfig codegenGlobalConfig = CodegenBuilder.CODEGEN_GLOBAL_CONFIG;
        codegenGlobalConfig.setProviderType(ClientProviderType.SPRING_CLOUD_OPENFEIGN);
        TestController controller = new TestController();

        Method[] methods = controller.getClass().getMethods();

        Arrays.stream(methods)
                .filter(method -> "helloWord".equals(method.getName()))
                .forEach(method -> {
                    Annotation[] annotations = method.getAnnotations();

                    Arrays.stream(annotations).forEach(annotation -> {
                        RequestMappingProcessor.RequestMappingMate mappingMate = annotationProcessor.process(annotation);
                        System.out.println(mappingMate.toAnnotation(method));
                        System.out.println(mappingMate.annotationType().getSimpleName());
                    });

                });
    }

    @Test
    public void testNotNullProcess() throws Exception {
        TestController controller = new TestController();
        Field field = controller.getClass().getField("name");
        NotNull annotation = field.getAnnotation(NotNull.class);
        NotNullProcessor.NotNullMate notNullMate = new NotNullProcessor()
                .process(annotation);
        String message = notNullMate.message();
        System.out.println("message: " + message);
        System.out.println("comment: " + notNullMate.toComment(field));
    }

    public static class TestController {

        @NotNull
        public String name;


        @RequestMapping(value = "hell/word", method = RequestMethod.POST)
        public String helloWord() {

            return "";
        }

        @GetMapping("get_hello")
        public String getHello() {

            return "";
        }
    }
}


