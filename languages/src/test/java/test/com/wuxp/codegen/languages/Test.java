package test.com.wuxp.codegen.languages;

import com.wuxp.codegen.core.utils.ToggleCaseUtil;

import java.util.regex.Pattern;

public class Test {

    public static void main(String[] args) {
        String regex = "^get[A-Z]+\\w*";
        System.out.println(Pattern.matches(regex, "getName1"));
        System.out.println(Pattern.matches(regex, "getname"));
        String methodMetaName = "isName1Ab";
        if (Pattern.matches(regex, methodMetaName)) {
            methodMetaName = methodMetaName.substring(3);
        } else {
            methodMetaName = methodMetaName.substring(2);
        }

        methodMetaName = ToggleCaseUtil.toggleFirstChart(methodMetaName);
        System.out.println(methodMetaName);
//        assert Pattern.matches(regex, "getName") == true;
//        assert Pattern.matches(regex, "getname") == false;
    }
}
