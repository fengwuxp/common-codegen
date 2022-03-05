package com.wuxp.codegen.swagger2.model.evt;

import lombok.Data;
import javax.validation.constraints.*;
        import com.wuxp.codegen.swagger2.model.evt.BaseQueryEvt;

    /**
        * 测试的API接口方法一的请求参数
    **/
@Data
public class  QueryOrderEvt extends BaseQueryEvt {

            /**
                *属性说明：订单sn，示例输入：test method
                *sn 约束条件：输入字符串的最小长度为：0，输入字符串的最大长度为：50
                *字段在java中的类型为：String
            **/
                @Size({
                    max=50
                })
        private String sn;

            /**
                *属性说明：id列表，示例输入：
                *字段在java中的类型为：数组
                *字段在java中的类型为：int
            **/
        private Integer[] ids;

}
