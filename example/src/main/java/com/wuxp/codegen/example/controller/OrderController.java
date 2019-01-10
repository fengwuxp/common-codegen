package com.wuxp.codegen.example.controller;


import com.wuxp.codegen.example.domain.Order;
import com.wuxp.codegen.example.domain.User;
import com.wuxp.codegen.example.evt.CreateOrderEvt;
import com.wuxp.codegen.example.evt.QueryOrderEvt;
import com.wuxp.codegen.example.resp.PageInfo;
import com.wuxp.codegen.example.resp.ServiceQueryResponse;
import com.wuxp.codegen.example.resp.ServiceResponse;
import io.swagger.annotations.*;
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
    @RequestMapping(method = RequestMethod.GET)
    public PageInfo<Order> queryOrder(@RequestBody QueryOrderEvt evt) {
        return new PageInfo<Order>();
    }


    @ApiOperation(value = "获取订单列表", notes = "")
    @PostMapping(value = {"queryOrder2"})
    public ServiceQueryResponse<Order> queryOrder2(@ApiParam("订单id")
                                                   @RequestParam(name = "order_id", required = false) Long oderId,
                                                   @ApiParam(value = "订单号", required = false) String sn) {

        return new ServiceQueryResponse<>();
    }

    @ApiOperation(value = "查询分页", notes = "")
    @PostMapping(value = {"queryPage"})
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
                    @ApiImplicitParam(name = "evt", value = "创建订单", required = true, dataType = "CreateOrderEvt"),
            }
    )
    public ServiceResponse<Long> createOrder(@RequestBody CreateOrderEvt evt) {

        return new ServiceResponse<>();
    }

}