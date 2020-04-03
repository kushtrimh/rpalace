package org.kushtrimhajrizi.rpalace.oauth.config.controller;

import org.kushtrimhajrizi.rpalace.oauth.OAuth2TokenService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class OAuth2Controller {

    private OAuth2TokenService oAuth2TokenService;

    public OAuth2Controller(OAuth2TokenService oAuth2TokenService) {
        this.oAuth2TokenService = oAuth2TokenService;
    }

    @GetMapping("/login/oauth2/callback/reddit")
    public String redirectUri(@RequestParam(value = "code", required = false) String code) {
        return code;
    }
}
