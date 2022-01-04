package com.wuxp.codegen.swagger2.domain;

import lombok.Data;

import javax.validation.constraints.NotNull;

      import com.wuxp.codegen.swagger2.domain.User;
      import java.util.Date;
      import com.wuxp.codegen.swagger2.domain.BaseInfo;

  /**
      * 订单
  **/


@Data
public class  Order extends BaseInfo<Long> {

          /**
              *属性说明：sn，示例输入：order_sn_199223
              *字段在java中的类型为：String
            **/
        private String sn;
          /**
              *属性说明：下单用户，示例输入：
              *字段在java中的类型为：User
            **/
        private User user;
          /**
              *字段在java中的类型为：Date
            **/
        private Date addTime;
}
