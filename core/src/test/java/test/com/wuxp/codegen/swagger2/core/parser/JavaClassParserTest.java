package test.com.wuxp.codegen.swagger2.core.parser;

import com.wuxp.codegen.core.parser.GenericParser;
import com.wuxp.codegen.core.parser.JavaClassParser;
import com.wuxp.codegen.model.languages.java.JavaClassMeta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.wuxp.codegen.core.parser.JavaClassParser.JAVA_CLASS_PARSER;

/**
 * JavaClassParser Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十二月 16, 2018</pre>
 */
@Slf4j
public class JavaClassParserTest {


    protected GenericParser<JavaClassMeta, Class<?>> genericParser = JAVA_CLASS_PARSER;

    public void before() throws Exception {
    }

    public void after() throws Exception {
    }

    /**
     * Method: parse(Class<?> source)
     */
    @Test
    public void testParse() throws Exception {
//        JavaClassMeta classMeta = genericParser.parse(TestJavaClassParserSimple.class);
//        System.out.println(classMeta);
        JavaClassMeta enumMeta = genericParser.parse(TestEnum.class);
        System.out.println(enumMeta);
    }

    /**
     * Method: getFields(Class<?> clazz, boolean onlyPublic)
     */
    @Test
    public void testGetFields() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: getMethods(Class<?> clazz, boolean onlyPublic)
     */
    @Test
    public void testGetMethods() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: getClassAnnotationMap(Annotation[] annotations)
     */
    @Test
    public void testGetClassAnnotationMap() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: getAssessPermission(int modifiers, JavaBaseMeta meta)
     */
    @Test
    public void testGetAssessPermission() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: genericsToClassType(ResolvableType resolvableType)
     */
    @Test
    public void testGenericsToClassType() throws Exception {
//TODO: Test goes here...
    }

    /**
     * Method: fetchDependencies(Class<?> clazz, JavaFieldMeta[] fieldMetas, JavaMethodMeta[] methodMetas)
     */
    @Test
    public void testFetchDependencies() throws Exception {
//TODO: Test goes here...
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

    ;

    public void func2() {

    }

    ;

    public void func3() {

    }

    ;

    private void func4() {

    }

    ;
}
