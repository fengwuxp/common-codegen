package com.wuxp.codegen.swagger3.evt;

import lombok.Data;
import lombok.experimental.Accessors;
import javax.validation.constraints.*;
        import com.wuxp.codegen.swagger3.evt.BaseEvt;

@Data
@Accessors(chain = true)
public class  BaseQueryEvt extends BaseEvt {

            /**
                *字段在java中的类型为：Integer
            **/
         Integer querySize;

            /**
                *字段在java中的类型为：Integer
            **/
         Integer queryPage;

}
