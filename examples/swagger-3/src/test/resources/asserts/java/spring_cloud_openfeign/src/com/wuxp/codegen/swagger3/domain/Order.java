package com.wuxp.codegen.swagger3.domain;

import lombok.Data;
import javax.validation.constraints.*;
        import com.wuxp.codegen.swagger3.domain.User;
        import java.util.Date;
        import com.wuxp.codegen.swagger3.domain.BaseInfo;

@Data
public class  Order extends BaseInfo<Long,String> {

            /**
                * ，默认值：，示例输入：
                *字段在java中的类型为：String
            **/
        private String sn;

            /**
                *字段在java中的类型为：User
            **/
        private User user;

            /**
                *字段在java中的类型为：Date
            **/
        private Date addTime;

}
