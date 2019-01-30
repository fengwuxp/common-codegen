package test.com.wuxp.codegen.core.parser;

import com.wuxp.codegen.dragon.core.parser.GenericParser;
import com.wuxp.codegen.dragon.core.parser.JavaClassParser;
import com.wuxp.codegen.dragon.model.enums.AccessPermission;
import com.wuxp.codegen.dragon.model.languages.java.JavaClassMeta;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * JavaClassParser Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>十二月 16, 2018</pre>
 */
public class JavaClassParserTest {


    protected GenericParser<JavaClassMeta, Class<?>> genericParser = new JavaClassParser(true);

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: parse(Class<?> source)
     */
    @Test
    public void testParse() throws Exception {


        JavaClassMeta classMeta = genericParser.parse(JavaClassMeta.class);
        JavaClassMeta javaClassMeta = genericParser.parse(AccessPermission.class);
        System.out.println(classMeta);
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


} 
