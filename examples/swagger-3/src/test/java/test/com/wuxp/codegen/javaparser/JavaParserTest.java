package test.com.wuxp.codegen.javaparser;


import com.github.javaparser.ParseResult;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.utils.*;
import com.wuxp.codegen.core.util.PathResolver;
import com.wuxp.codegen.swagger3.example.enums.Sex;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.github.javaparser.utils.CodeGenerationUtils.mavenModuleRoot;

@Slf4j
class JavaParserTest {


    @Test
    void testSourceRoot() {
        String userDir = System.getProperty("user.dir");
        String path = PathResolver.relative(userDir, "../");
        ParserCollectionStrategy parserCollectionStrategy = new ParserCollectionStrategy();
        ProjectRoot root = parserCollectionStrategy.collect(Paths.get(path));
        log.info("{}", root);
        List<SourceRoot> sourceRoots = root.getSourceRoots();
        Assertions.assertNotNull(sourceRoots);
    }

    @Test
    void testSourceZipRoot() throws Exception {
        getSourceBySourceJar(ClassPathScanningCandidateComponentProvider.class);
        getSourceBySourceJar(Object.class);
        getSourceBySourceJar(Sex.class);
    }

    private void getSourceBySourceJar(Class<?> clazz) throws IOException {
        ProtectionDomain protectionDomain = clazz.getProtectionDomain();
        CodeSource codeSource = protectionDomain.getCodeSource();
        if (codeSource == null) {
            log.warn("not found codeSource className = {}", clazz.getName());
            return;
        }
        URL location = codeSource.getLocation();
        String classSourcePath = location.getPath();
        Assertions.assertNotNull(classSourcePath);
        if (classSourcePath.endsWith(".jar")) {
            getSourceCodeBySourceJar(clazz, classSourcePath);
        } else {
            log.info("{}", classSourcePath);
//            String path = PathResolver.resolve(classSourcePath, "../../");
            this.getSourceCodeByClassesPath(clazz);
        }


    }

    private void getSourceCodeBySourceJar(Class<?> clazz, String classSourcePath) throws IOException {
        classSourcePath = classSourcePath.replace(".jar", "-sources.jar");
        boolean exists = new File(classSourcePath).exists();
        Assertions.assertTrue(exists);
        SourceZip sourceZip = new SourceZip(Paths.get(classSourcePath));
        List<Pair<Path, ParseResult<CompilationUnit>>> results = sourceZip.parse();
        Assertions.assertNotNull(results);
        Path sourcePath = Paths.get(clazz.getName().replace(".", "/") + ".java");
        Optional<CompilationUnit> optionalCompilationUnit = results.stream()
                .filter(pathParseResultPair -> pathParseResultPair.a.equals(sourcePath))
                .map(pathParseResultPair -> pathParseResultPair.b.getResult())
                .filter(Optional::isPresent).map(Optional::get)
                .findFirst();
        Assertions.assertTrue(optionalCompilationUnit.isPresent());
        CompilationUnit compilationUnit = optionalCompilationUnit.get();
        Assertions.assertNotNull(compilationUnit);
    }

    private void getSourceCodeByClassesPath(Class<?> clazz) {
        Path mavenModuleRoot = mavenModuleRoot(clazz);
        Assertions.assertNotNull(mavenModuleRoot);
        ParserCollectionStrategy parserCollectionStrategy = new ParserCollectionStrategy();
        ProjectRoot projectRoot = parserCollectionStrategy.collect(mavenModuleRoot);
        Assertions.assertNotNull(projectRoot);
        Optional<CompilationUnit> unitOptional = projectRoot.getSourceRoots()
                .stream()
                .map(sourceRoot -> sourceRoot.parse(clazz.getPackage().getName(), clazz.getSimpleName() + ".java"))
                .filter(Objects::nonNull)
                .findFirst();
        Assertions.assertTrue(unitOptional.isPresent());
    }


}
