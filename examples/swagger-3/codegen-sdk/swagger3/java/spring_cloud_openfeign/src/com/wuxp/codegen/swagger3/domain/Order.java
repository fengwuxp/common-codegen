package com.wuxp.codegen.swagger3.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.*;
import com.fasterxml.jackson.annotation.JsonFormat;
        import com.wuxp.codegen.swagger3.domain.User;
        import java.util.Date;
        import com.wuxp.codegen.swagger3.domain.BaseInfo;

@Data
@Accessors(chain = true)
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
