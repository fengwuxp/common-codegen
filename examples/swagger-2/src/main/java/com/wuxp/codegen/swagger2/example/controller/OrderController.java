package com.wuxp.codegen.swagger2.example.controller;


import com.wuxp.codegen.swagger2.example.domain.Order;
import com.wuxp.codegen.swagger2.example.domain.User;
import com.wuxp.codegen.swagger2.example.evt.CreateOrderEvt;
import com.wuxp.codegen.swagger2.example.evt.QueryOrderEvt;
import com.wuxp.codegen.swagger2.example.resp.PageInfo;
import com.wuxp.codegen.swagger2.example.resp.ServiceQueryResponse;
import com.wuxp.codegen.swagger2.example.resp.ServiceResponse;
import com.wuxp.codegen.swagger2.example.services.UserService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

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
    public List<Order> getOrder(@ModelAttribute("text") String text, String[] names, List<Integer> ids, Set<Order> moneys) {
        return Collections.EMPTY_LIST;
    }

    @ApiOperation(value = "获取订单列表", notes = "")
    @RequestMapping(method = RequestMethod.GET)
    public PageInfo<Order> queryOrder(QueryOrderEvt evt) {
        return new PageInfo<Order>();
    }


    @ApiOperation(value = "获取订单列表", notes = "")
    @PostMapping(value = {"queryOrder2"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ServiceQueryResponse<Order> queryOrder2(@ApiParam("订单id")
                                                   @RequestParam(name = "order_id", required = false) Long oderId,
                                                   @ApiParam(value = "订单号", required = false) String sn,
                                                   @ApiParam(value = "用户id", hidden = true) Long memberId) {

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
    public ServiceResponse hello2(@RequestParam(required = false, defaultValue = "test") @ApiParam("test") String name) {

        return new ServiceResponse<>();
    }

    @ApiOperation(value = "test hello", notes = "测试方法名称=delete")
    @DeleteMapping(value = {"hello_delete"})
    public void delete(@RequestParam(required = false) String id) {

    }
}
