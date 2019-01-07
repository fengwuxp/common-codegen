package com.wuxp.codegen.example.controller;


import com.wuxp.codegen.example.domain.Order;
import com.wuxp.codegen.example.domain.User;
import com.wuxp.codegen.example.evt.QueryOrderEvt;
import com.wuxp.codegen.example.resp.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@Api("订单服务")
@RestController
@RequestMapping(value = "/order")
public class OrderController {

    static Map<Long, User> users = Collections.synchronizedMap(new HashMap<Long, User>());

    @ApiOperation(value = "获取订单列表", notes = "")
    @GetMapping(value = {"getOrder"})
    public List<Order> getOrder() {
        return Collections.EMPTY_LIST;
    }

    @ApiOperation(value = "获取订单列表", notes = "")
    @RequestMapping(value = {"getOrder"}, method = RequestMethod.GET)
    public PageInfo<Order> queryOrder(@RequestBody  QueryOrderEvt evt) {
        return new PageInfo<Order>();
    }

}