package test.com.wuxp.swagger.controller;

import io.swagger.annotations.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import test.com.wuxp.swagger.evt.TestMethodApiEvt;
import test.com.wuxp.swagger.resp.ServiceQueryResponse;
import test.com.wuxp.swagger.resp.ServiceResponse;
import test.com.wuxp.swagger.resp.TestMethodDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("swagger")
@Api(value = "这是一个标准的swagger controller", tags = {
        "这是一个标准的swagger controller，将覆盖value中的声明"
})
public class StandardSwaggerController {


    @ApiOperation("测试方法")
    @RequestMapping("test_method")
    public ServiceResponse<String> testMethod(@RequestBody TestMethodApiEvt evt, HttpSession session, HttpServletRequest request) {

        return null;
    }

    @ApiOperation(value = "测试查询方法", tags = "分组标签")
    @RequestMapping("test_{name}")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "age", value = "年龄")
    })
    public ServiceQueryResponse<TestMethodDTO> testQuery(@ApiParam(value = "查询名称", required = true) @PathVariable("name") String name,
                                                         Integer age) {

        return null;
    }
}
