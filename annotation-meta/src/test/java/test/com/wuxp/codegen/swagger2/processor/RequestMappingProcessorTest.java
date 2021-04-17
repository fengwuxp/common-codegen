package test.com.wuxp.codegen.swagger2.processor;

import com.wuxp.codegen.annotation.processors.AnnotationMetaFactory;
import com.wuxp.codegen.annotation.processors.javax.NotNullMetaFactory;
import com.wuxp.codegen.annotation.processors.spring.RequestMappingMetaFactory;
import com.wuxp.codegen.core.ClientProviderType;
import com.wuxp.codegen.core.config.CodegenConfig;
import com.wuxp.codegen.core.config.CodegenConfigHolder;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.constraints.NotNull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;


class RequestMappingProcessorTest {


    AnnotationMetaFactory<RequestMappingMetaFactory.RequestMappingMate, Annotation> annotationMetaFactory = new RequestMappingMetaFactory();

    @Test
    void testProcess() {

        CodegenConfig codegenConfig = CodegenConfig.builder()
                .providerType(ClientProviderType.SPRING_CLOUD_OPENFEIGN)
                .build();
        CodegenConfigHolder.setConfig(codegenConfig);
        TestController controller = new TestController();

        Method[] methods = controller.getClass().getMethods();

        Arrays.stream(methods)
                .filter(method -> "helloWord".equals(method.getName()))
                .forEach(method -> {
                    Annotation[] annotations = method.getAnnotations();

                    Arrays.stream(annotations).forEach(annotation -> {
                        RequestMappingMetaFactory.RequestMappingMate mappingMate = annotationMetaFactory.factory(annotation);
                        System.out.println(mappingMate.toAnnotation(method));
                        System.out.println(mappingMate.annotationType().getSimpleName());
                    });

                });
    }

    @Test
    void testNotNullProcess() throws Exception {
        TestController controller = new TestController();
        Field field = controller.getClass().getField("name");
        NotNull annotation = field.getAnnotation(NotNull.class);
        NotNullMetaFactory.NotNullMate notNullMate = new NotNullMetaFactory().factory(annotation);
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


