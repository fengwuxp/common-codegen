package com.wuxp.codegen.server.controller;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.wuxp.codegen.server.constant.WebApiConstants.WEB_API_V1_PREFIX;

@RestController
@RequestMapping(WEB_API_V1_PREFIX + "/authentication")
public class AuthenticationController {

    /**
     * 返回当前登录用户
     * @param authentication
     * @return
     */
    @GetMapping("/details")
    public UserDetails getCurrentUserDetails(Authentication authentication) {
        return (UserDetails) authentication.getPrincipal();
    }
}
