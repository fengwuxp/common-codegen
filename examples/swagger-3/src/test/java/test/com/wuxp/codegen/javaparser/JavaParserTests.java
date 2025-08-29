package test.com.wuxp.codegen.javaparser;


import com.github.javaparser.utils.ParserCollectionStrategy;
import com.github.javaparser.utils.ProjectRoot;
import com.github.javaparser.utils.SourceRoot;
import com.wind.tools.codegen.SourceCodeProvider;
import com.wuxp.codegen.core.util.PathResolveUtils;
import com.wuxp.codegen.swagger3.example.enums.Sex;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

import java.nio.file.Paths;
import java.util.List;

@Slf4j
class JavaParserTests {

    private final SourceCodeProvider sourceCodeProvider = new SourceCodeProvider();

    @Test
    void testSourceRoot() {
        String userDir = System.getProperty("user.dir");
        String path = PathResolveUtils.relative(userDir, "../");
        ParserCollectionStrategy parserCollectionStrategy = new ParserCollectionStrategy();
        ProjectRoot root = parserCollectionStrategy.collect(Paths.get(path));
        log.info("{}", root);
        List<SourceRoot> sourceRoots = root.getSourceRoots();
        Assertions.assertNotNull(sourceRoots);
    }

    @Test
    void testSourceZipRoot() {
        Assertions.assertTrue(sourceCodeProvider.getTypeDeclaration(ClassPathScanningCandidateComponentProvider.class).isPresent());
        Assertions.assertFalse(sourceCodeProvider.getTypeDeclaration(Object.class).isPresent());
        Assertions.assertTrue(sourceCodeProvider.getTypeDeclaration(Sex.class).isPresent());
    }


}
