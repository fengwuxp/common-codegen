package test.com.wuxp.swagger.evt;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@ApiModel("测试的API接口方法一的请求参数")
public class TestMethodApiEvt extends BaseEvt {

    @ApiModelProperty(value = "方法名称", example = "test method")
    @Size(max = 50)
    String methodName;

    @ApiModelProperty(value = "次数", example = "1")
    @NotNull()
    @Max(100)
    @Min(0)
    Integer count;
}
