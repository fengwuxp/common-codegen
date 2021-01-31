package com.wuxp.codegen.swagger3.example.maven.controller;


import com.wuxp.codegen.swagger3.example.maven.domain.Order;
import com.wuxp.codegen.swagger3.example.maven.domain.User;
import com.wuxp.codegen.swagger3.example.maven.evt.CreateOrderEvt;
import com.wuxp.codegen.swagger3.example.maven.evt.ExampleDTO;
import com.wuxp.codegen.swagger3.example.maven.evt.QueryOrderEvt;
import com.wuxp.codegen.swagger3.example.maven.resp.PageInfo;
import com.wuxp.codegen.swagger3.example.maven.resp.ServiceQueryResponse;
import com.wuxp.codegen.swagger3.example.maven.resp.ServiceResponse;
import com.wuxp.codegen.swagger3.example.maven.services.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping(value = "/order")
public class OrderController extends BaseController<String> {

  static Map<Long, User> users = Collections.synchronizedMap(new HashMap<Long, User>());

  @Autowired
  private UserService userService;

  @GetMapping(value = {"getOrder"})
  public List<Order> getOrder(@RequestParam String[] names, @RequestHeader List<Integer> ids, Set<Order> moneys) {
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


  @PostMapping(value = {"queryOrder2"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
  public ServiceQueryResponse<Order> queryOrder2(@RequestParam(name = "order_id", required = false) Long oderId, String sn) {

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

  //    @PostMapping(value = {"hello"})
  public ServiceResponse public_hello() {

    return new ServiceResponse<>();
  }

  @DeleteMapping("/delete")
  public ServiceResponse delete(ExampleDTO dto) {

    return new ServiceResponse<>();
  }

//    @PostMapping(value = {"private_hello"})
//    private ServiceResponse private_hello() {
//
//        return new ServiceResponse<>();
//    }

}
