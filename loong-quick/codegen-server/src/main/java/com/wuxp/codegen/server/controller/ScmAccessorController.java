package com.wuxp.codegen.server.controller;

import com.wuxp.codegen.server.entities.ScmInfo;
import com.wuxp.codegen.server.repositories.ScmInfoRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author wuxp
 */
@Tag(name = "scm accessor", description = "源代码管理平台信息维护")
@Slf4j
@RestController
@RequestMapping("/scm")
public class ScmAccessorController {

    private final ScmInfoRepository scmInfoRepository;

    public ScmAccessorController(ScmInfoRepository scmInfoRepository) {
        this.scmInfoRepository = scmInfoRepository;
    }

    @Operation(description = "获取所有的源代码平台信息")
    @GetMapping("/infos")
    public List<ScmInfo> getAllScmInfos() {
        return scmInfoRepository.findAll();
    }

    @Operation(description = "保存源代码平台信息")
    @PostMapping("/info")
    public void saveScmInfo(@RequestBody ScmInfo info) {
        scmInfoRepository.save(info);
    }

    @Operation(description = "删除源代码平台信息")
    @DeleteMapping("/info/{id}")
    public void deleteScmInfo(@PathVariable Long id) {
        scmInfoRepository.deleteById(id);
    }
}
