package com.wuxp.codegen.swagger3.evt;

import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.*;
        import com.wuxp.codegen.swagger3.enums.Sex;
        import java.util.Map;
        import com.wuxp.codegen.swagger3.evt.BaseEvt;

@Data
@Accessors(chain = true)
public class  CreateOrderEvt extends BaseEvt {

            /**
                *sn 约束条件：输入字符串的最小长度为：0，输入字符串的最大长度为：50
                *  ，默认值：，示例输入：
                *字段在java中的类型为：String
            **/
                @Size(
                    max=50
                )
        private String sn;

            /**
                *字段在java中的类型为：Map
                *字段在java中的类型为：Sex
                *字段在java中的类型为：String
            **/
        private Map<Sex,String> test;

}
