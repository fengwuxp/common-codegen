package test.com.wuxp.codegen.javaparser;


import com.github.javaparser.utils.ParserCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import com.wuxp.codegen.core.util.PathResolver;
import lombok.extern.slf4j.Slf4j;;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

@Slf4j
public class JavaParserTest {


    @Test
    void testSourceRoot() {
        String userDir = System.getProperty("user.dir");
        String path = PathResolver.resolve(userDir, "../");
        ParserCollectionStrategy parserCollectionStrategy = new ParserCollectionStrategy();
        ProjectRoot root = parserCollectionStrategy.collect(Paths.get(path));
        log.info("{}", root);
        Assertions.assertNotNull(root.getSourceRoots());

    }


}
