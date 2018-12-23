package test.com.wuxp.codegen.processor;

import com.wuxp.codegen.annotation.processor.AnnotationProcessor;
import com.wuxp.codegen.annotation.processor.spring.RequestMappingProcessor;
import org.junit.Test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;


public class RequestMappingProcessorTest {


    AnnotationProcessor<RequestMappingProcessor.RequestMappingMate> annotationProcessor = new RequestMappingProcessor();

    @Test
    public void testProcess() {

        TestController controller = new TestController();

        Method[] methods = controller.getClass().getMethods();

        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();

            Arrays.stream(annotations).forEach(annotation -> {
                RequestMappingProcessor.RequestMappingMate mappingMate = annotationProcessor.process(annotation);
                System.out.println(mappingMate);
            });

        }

    }

    public static class TestController {


        @RequestMapping("hell/word")
        public String helloWord() {

            return "";
        }

        @GetMapping("get_hello")
        public String getHello() {

            return "";
        }
    }
}


