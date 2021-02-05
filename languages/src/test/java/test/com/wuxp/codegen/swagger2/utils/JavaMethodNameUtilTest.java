package test.com.wuxp.codegen.swagger2.utils;


import com.wuxp.codegen.util.JavaMethodNameUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class JavaMethodNameUtilTest {


    @Test
    void testHumpToLine() {

        System.out.println(JavaMethodNameUtils.humpToLine("MemberDTO"));
        System.out.println(JavaMethodNameUtils.humpToLine("TestVO"));
        System.out.println(JavaMethodNameUtils.humpToLine("OrderVo"));
        System.out.println(JavaMethodNameUtils.humpToLine("OrderGoodsMemberDo"));
        System.out.println(JavaMethodNameUtils.humpToLine("OrderGoodsMemberDTO"));
        System.out.println(JavaMethodNameUtils.humpToLine("OrderGoodsMemberDto"));
    }

}
