package test.com.wuxp.codegen.swagger2.core.parser;

import com.wuxp.codegen.core.util.PathResolver;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PathResolverTest {


    @Test
    void testRelative() {
        Assertions.assertEquals(".", PathResolver.relative("/api/b/c", "/api/b/c", "/"));
        Assertions.assertEquals("../c", PathResolver.relative("/api/b/c", "/api/b/d", "/"));
        Assertions.assertEquals("../c/d", PathResolver.relative("/api/b/c/d", "/api/b/d", "/"));
        Assertions.assertEquals("../../c/d", PathResolver.relative("/api/b/c/d", "/api/b/d/e", "/"));
        Assertions.assertEquals("../../c", PathResolver.relative("/api/b/c/", "/api/b/d/e", "/"));
        Assertions.assertEquals("../..", PathResolver.relative("/api/b/", "/api/b/d/e", "/"));
        Assertions.assertEquals("../../..", PathResolver.relative("/api/", "/api/b/d/e", "/"));
        Assertions.assertEquals("c/d", PathResolver.relative("/api/b/c/d", "/api/b", "/"));
        Assertions.assertEquals("../../api/b/c/d", PathResolver.relative("/api/b/c/d", "/cpi/b", "/"));
        Assertions.assertEquals("../../api/b/c/d", PathResolver.relative("/api/b/c/d", "/cpi/b", "/"));
        Assertions.assertEquals(".", PathResolver.relative("/api/b/c/d", "/api/b/c/../c/d", "/"));
        Assertions.assertEquals("../../b/c/d", PathResolver.relative("/api/b/c/d", "/api/b/../d/./../c/d", "/"));
    }

}
