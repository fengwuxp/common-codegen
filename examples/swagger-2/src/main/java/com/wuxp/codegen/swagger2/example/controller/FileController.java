package com.wuxp.codegen.swagger2.example.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;

/**
 * @author wuxp
 */
@Controller
@RequestMapping("/file")
public class FileController {


    @RequestMapping("/download")
    public HttpEntity<InputStreamResource> download(String name) {
        return ResponseEntity.notFound().build();
    }

    @RequestMapping(value = "/download_2",produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public void download2(String name, HttpServletResponse response) {

    }

}
