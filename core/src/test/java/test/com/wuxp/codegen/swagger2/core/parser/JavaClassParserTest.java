package test.com.wuxp.codegen.swagger2.core.parser;

import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.wuxp.codegen.core.parser.JavaClassParser.JAVA_CLASS_PARSER;

/**
 * JavaClassParser Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十二月 16, 2018</pre>
 */
@Slf4j
class JavaClassParserTest {


    @Test
    void testParse() throws Exception {
        JavaClassMeta classMeta = JAVA_CLASS_PARSER.parse(TestJavaClassParserSimple.class);
        Assertions.assertEquals(classMeta.getSuperClass(), Object.class);
        Assertions.assertTrue(classMeta.getSuperTypeVariables().isEmpty());

        JavaClassMeta enumMeta = JAVA_CLASS_PARSER.parse(TestEnum.class);
        Assertions.assertEquals(enumMeta.getSuperClass(), Enum.class);
    }


    @AllArgsConstructor
    @Getter
    enum TestEnum {
        A("test");
        private String desc;


    }
}

class TestJavaClassParserSimple {

    public void func1(List<Integer> list) {

    }

    public Map func2() {
        return Collections.emptyMap();
    }


    public Map<String, String> func3() {
        return Collections.emptyMap();
    }


    private Set<Map<String, List<Map>>> func4() {
        return Collections.emptySet();
    }

}
