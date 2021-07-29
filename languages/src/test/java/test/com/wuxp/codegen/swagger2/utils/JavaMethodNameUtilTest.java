package test.com.wuxp.codegen.swagger2.utils;


import com.wuxp.codegen.meta.util.JavaMethodNameUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class JavaMethodNameUtilTest {


    @Test
    void testHumpToLine() {

        Assertions.assertEquals("member_dto",JavaMethodNameUtils.humpToLine("MemberDTO"));
        Assertions.assertEquals("test_vo",JavaMethodNameUtils.humpToLine("TestVO"));
        Assertions.assertEquals("order_vo",JavaMethodNameUtils.humpToLine("OrderVo"));
        Assertions.assertEquals("order_goods_member_do",JavaMethodNameUtils.humpToLine("OrderGoodsMemberDo"));
        Assertions.assertEquals("order_goods_member_dto",JavaMethodNameUtils.humpToLine("OrderGoodsMemberDTO"));
        Assertions.assertEquals("order_goods_member_dto",JavaMethodNameUtils.humpToLine("OrderGoodsMemberDto"));
    }

}
