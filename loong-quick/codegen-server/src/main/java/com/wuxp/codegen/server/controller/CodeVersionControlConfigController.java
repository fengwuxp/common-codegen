package com.wuxp.codegen.server.controller;

import com.wuxp.codegen.server.entities.CodeVersionControlConfig;
import com.wuxp.codegen.server.repositories.CodeVersionControlConfigRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.wuxp.codegen.server.constant.WebApiConstants.WEB_API_V1_PREFIX;

/**
 * @author wuxp
 */
@Tag(name = "VCS Manager", description = "源代仓库配置")
@Slf4j
@RestController
@RequestMapping(WEB_API_V1_PREFIX + "/vcs-configs")
@Validated
public class CodeVersionControlConfigController {

    private final CodeVersionControlConfigRepository codeVersionControlConfigRepository;

    public CodeVersionControlConfigController(CodeVersionControlConfigRepository codeVersionControlConfigRepository) {
        this.codeVersionControlConfigRepository = codeVersionControlConfigRepository;
    }

    @Operation(description = "获取所有的源代仓库配置列表")
    @GetMapping()
    public List<CodeVersionControlConfig> getAllConfigs() {
        return codeVersionControlConfigRepository.findAll();
    }

    @Operation(description = "获取源代仓库配置配置详情")
    @GetMapping("/{id}")
    public HttpEntity<CodeVersionControlConfig> findById(@PathVariable("id") Long id) {
        return ResponseEntity.of(codeVersionControlConfigRepository.findById(id));
    }

    @Operation(description = "保存源代仓库配置")
    @PostMapping()
    public void save(@Valid @RequestBody CodeVersionControlConfig req) {
        codeVersionControlConfigRepository.save(req);
    }

    @Operation(description = "删除源代仓库配置")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        codeVersionControlConfigRepository.deleteById(id);
    }
}
