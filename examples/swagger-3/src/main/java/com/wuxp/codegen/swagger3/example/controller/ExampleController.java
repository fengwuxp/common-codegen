package com.wuxp.codegen.swagger3.example.controller;

import com.wuxp.codegen.swagger3.example.ExampleDto;
import com.wuxp.codegen.swagger3.example.evt.ExampleDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wxup
 */
@RestController
@RequestMapping("/example_cms")
@Tag(name = "example_cms", description = "example_cms")
@Slf4j
public class ExampleController {


  @GetMapping("get_num")
  public List<Integer> getNums(Integer num) {

    return Arrays.asList(num, num + 1);
  }

  @GetMapping("get_maps")
  public List<Map<Integer, String>> getMaps(Integer num) {

    Map<Integer, String> map = new HashMap<>();
    map.put(num, "num");
    return Arrays.asList(map);
  }

  @GetMapping("get_map")
  public Map<String, Integer> getMap() {
    Map<String, Integer> map = new HashMap<>();
    map.put("num", 1);
    return map;
  }

  @GetMapping("get_map_2")
  public Map<String, List<Boolean>> getMap2() {
    Map<String, List<Boolean>> map = new HashMap<>();
    map.put("num", Arrays.asList(false, true));
    return map;
  }

  @GetMapping("get_map_3/{test_id}")
  public Map<String, List<Boolean>> getMap3(@PathVariable("test_id") String id) {
    Map<String, List<Boolean>> map = new HashMap<>();
    map.put("num", Arrays.asList(false, true));
    return map;
  }

  @GetMapping("get_map_4/{test_id}")
  public Map<String, List<Boolean>> getMap4(ExampleDTO dto) {
    Map<String, List<Boolean>> map = new HashMap<>();
    map.put("num", Arrays.asList(false, true));
    return map;
  }

  @GetMapping("get_map_5/{test_id}")
  public Map<String, List<Boolean>> getMap5(ExampleDto dto) {
    Map<String, List<Boolean>> map = new HashMap<>();
    map.put("num", Arrays.asList(false, true));
    return map;
  }
}
