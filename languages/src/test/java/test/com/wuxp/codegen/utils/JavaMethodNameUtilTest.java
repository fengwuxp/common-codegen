package test.com.wuxp.codegen.utils;


import com.wuxp.codegen.utils.JavaMethodNameUtil;
import junit.framework.TestCase;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class JavaMethodNameUtilTest extends TestCase {


    public void testHumpToLine() {

        System.out.println(JavaMethodNameUtil.humpToLine("MemberDTO"));
        System.out.println(JavaMethodNameUtil.humpToLine("TestVO"));
        System.out.println(JavaMethodNameUtil.humpToLine("OrderVo"));
        System.out.println(JavaMethodNameUtil.humpToLine("OrderGoodsMemberDo"));
        System.out.println(JavaMethodNameUtil.humpToLine("OrderGoodsMemberDTO"));
        System.out.println(JavaMethodNameUtil.humpToLine("OrderGoodsMemberDto"));
    }

}
