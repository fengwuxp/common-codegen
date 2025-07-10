package com.wuxp.codegen.swagger3.example.controller;


import com.wuxp.codegen.swagger3.example.domain.Order;
import com.wuxp.codegen.swagger3.example.domain.User;
import com.wuxp.codegen.swagger3.example.evt.CreateOrderEvt;
import com.wuxp.codegen.swagger3.example.resp.ExampleDTO;
import com.wuxp.codegen.swagger3.example.evt.QueryOrderEvt;
import com.wuxp.codegen.swagger3.example.resp.PageInfo;
import com.wuxp.codegen.swagger3.example.resp.ServiceQueryResponse;
import com.wuxp.codegen.swagger3.example.resp.ServiceResponse;
import com.wuxp.codegen.swagger3.example.services.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


@RestController
@RequestMapping(value = "/order")
public class OrderController extends BaseController<String> {

    static Map<Long, User> users = Collections.synchronizedMap(new HashMap<Long, User>());

    @Autowired
    private UserService userService;

    @GetMapping(value = {"getOrder"})
    public List<Order> getOrder(@RequestParam String[] names, @RequestHeader("My-Ids") List<Integer> ids, Set<Order> moneys) {
        return Collections.EMPTY_LIST;
    }

    @RequestMapping(method = RequestMethod.GET)
    public PageInfo<Order> queryOrder2(QueryOrderEvt evt) {
        return new PageInfo<Order>();
    }

    @RequestMapping(method = RequestMethod.POST)
    public PageInfo<Order> queryOrder(@RequestBody QueryOrderEvt evt) {
        return new PageInfo<Order>();
    }

    @RequestMapping(value = "queryOrder3", method = RequestMethod.POST)
    public PageInfo<Order> queryOrder3(@RequestBody QueryOrderEvt[] evt) {
        return new PageInfo<Order>();
    }

    @RequestMapping(value = "queryOrder4", method = RequestMethod.POST)
    public PageInfo<Order> queryOrder4(@RequestBody Set<QueryOrderEvt> evt) {
        return new PageInfo<Order>();
    }

    @RequestMapping(value = "queryOrder5", method = RequestMethod.POST)
    public PageInfo<Order> queryOrder5(@RequestBody Map<String, QueryOrderEvt> evt) {
        return new PageInfo<Order>();
    }

    @RequestMapping(value = "queryOrder6", method = RequestMethod.POST)
    public PageInfo<Order> queryOrder6(@RequestBody List<QueryOrderEvt> evt, @Parameter(hidden = true) Long memberId) {
        return new PageInfo<Order>();
    }

    @PostMapping(value = {"queryOrder_2"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ServiceQueryResponse<Order> queryOrder_2(@RequestParam(name = "order_id", required = false) Long oderId, String sn) {

        return new ServiceQueryResponse<>();
    }

    @PostMapping(value = {"queryPage"}, consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ServiceResponse<PageInfo<Order>> queryPage(String id, @Schema(hidden = true) Long memberId) {

        return new ServiceResponse<>();
    }


    @GetMapping(value = {"createOrder"})
    public ServiceResponse<Long> createOrder(/*@RequestBody*/ CreateOrderEvt evt) {

        return new ServiceResponse<>();
    }

    @PostMapping(value = {"hello"})
    public ServiceResponse hello() {

        return new ServiceResponse<>();
    }

    @DeleteMapping("/delete")
    public ServiceResponse delete(ExampleDTO dto) {

        return new ServiceResponse<>();
    }

}
