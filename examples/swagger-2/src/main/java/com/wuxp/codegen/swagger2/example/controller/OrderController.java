package com.wuxp.codegen.swagger2.example.controller;


import com.wuxp.codegen.swagger2.example.domain.Order;
import com.wuxp.codegen.swagger2.example.domain.User;
import com.wuxp.codegen.swagger2.example.enums.Sex;
import com.wuxp.codegen.swagger2.example.evt.CreateOrderEvt;
import com.wuxp.codegen.swagger2.example.evt.QueryOrderEvt;
import com.wuxp.codegen.swagger2.example.resp.PageInfo;
import com.wuxp.codegen.swagger2.example.resp.ServiceQueryResponse;
import com.wuxp.codegen.swagger2.example.resp.ServiceResponse;
import com.wuxp.codegen.swagger2.example.services.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;


@Api("订单服务")
@RestController
@RequestMapping(value = "/order")
public class OrderController extends BaseController<String> {

    static Map<Long, User> users = Collections.synchronizedMap(new HashMap<Long, User>());

    @Autowired
    private UserService userService;

    @ApiOperation(value = "获取订单列表", notes = "")
    @GetMapping(value = {"get_order"})
    public List<Order> getOrder(@ModelAttribute("text") String text, @RequestHeader String[] names, @CookieValue("my_ids") List<Integer> ids, Set<Order> moneys) {
        return Collections.EMPTY_LIST;
    }

    @ApiOperation(value = "获取订单列表", notes = "")
    @GetMapping(value = {"get_order_32"})
    public List<Order> getOrder32(@RequestParam(name = "names") String[] names) {
        return Collections.EMPTY_LIST;
    }


    @ApiOperation(value = "获取订单列表", notes = "")
    @RequestMapping(method = RequestMethod.GET, value = "/queryOrder")
    public PageInfo<Order> queryOrder(QueryOrderEvt evt) {
        return new PageInfo<Order>();
    }

    @ApiOperation(value = "获取订单列表", notes = "")
    @RequestMapping(method = RequestMethod.GET)
    public Page<Order> pageBySpringData(QueryOrderEvt evt) {
        return new PageImpl<>(Collections.emptyList());
    }


    @ApiOperation(value = "获取订单列表", notes = "")
    @PostMapping(value = {"queryOrder2"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ServiceQueryResponse<Order> queryOrder2(@ApiParam("订单id")
                                                   @RequestParam(name = "order_id", required = false) Long oderId,
                                                   @ApiParam(value = "订单号", required = false) String sn,
                                                   @ApiParam(value = "用户id", hidden = true) @CookieValue Long memberId) {

        return new ServiceQueryResponse<>();
    }

    @ApiOperation(value = "查询分页", notes = "")
    @PostMapping(value = {"queryPage"}, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "id", value = "订单", required = true, dataType = "String"),
            }
    )
    public ServiceResponse<PageInfo<Order>> queryPage(String id) {

        return new ServiceResponse<>();
    }


    @ApiOperation(value = "创建订单", notes = "")
    @PostMapping(value = {"createOrder"})
    @ApiImplicitParams(
            {
                    @ApiImplicitParam(name = "evt", value = "创建订单", required = true),
            }
    )
    public ServiceResponse<Long> createOrder(@RequestBody CreateOrderEvt evt) {

        return new ServiceResponse<>();
    }


    @ApiOperation(value = "test hello", notes = "")
    @PostMapping(value = {"hello"})
    public ServiceResponse hello() {

        return new ServiceResponse<>();
    }

    @ApiOperation(value = "test hello", notes = "非必填参数测试")
    @PostMapping(value = {"hello_2"})
    public ServiceQueryResponse hello2(@RequestParam(required = false, defaultValue = "test") @ApiParam("test") String name) {

        return new ServiceQueryResponse<>();
    }

    @ApiOperation(value = "test hello_3", notes = "非必填参数测试")
    @PostMapping(value = {"hello_3"})
    public ServiceQueryResponse<String> hello3(@RequestParam(required = false, defaultValue = "test") @ApiParam("test") String name) {

        return new ServiceQueryResponse<>();
    }

    @ApiOperation(value = "test hello", notes = "测试方法名称=delete")
    @DeleteMapping(value = {"hello_delete"})
    public void delete(@RequestParam(required = false) String id) {

    }

    @RequestMapping("/testEnumNames")
    public Map<Sex, Sex> testEnumNames() {
        return Collections.emptyMap();
    }

    @RequestMapping("/testEnumNames2")
    public Map<String, Sex> testEnumNames2() {
        return Collections.emptyMap();
    }

    @RequestMapping("/testEnumNames3")
    public Map<Sex, Integer> testEnumNames3() {
        return Collections.emptyMap();
    }

    @RequestMapping("/testIgnore")
    @ApiIgnore
    public void testIgnore() {

    }
}
