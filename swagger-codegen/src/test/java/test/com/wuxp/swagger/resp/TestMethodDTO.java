package test.com.wuxp.swagger.resp;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel("测试的方法响应")
public class TestMethodDTO {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("age")
    private Short age;

    @ApiModelProperty("flag")
    private Boolean flag;

    @ApiModelProperty("生日")
    private Date birthDay;
}
