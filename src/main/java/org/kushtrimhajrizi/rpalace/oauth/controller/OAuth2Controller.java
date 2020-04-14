package org.kushtrimhajrizi.rpalace.oauth.controller;

import org.kushtrimhajrizi.rpalace.entity.DefaultUser;
import org.kushtrimhajrizi.rpalace.exception.UserAlreadyHasToken;
import org.kushtrimhajrizi.rpalace.exception.UserDoesNotExist;
import org.kushtrimhajrizi.rpalace.oauth.OAuth2TokenService;

import org.kushtrimhajrizi.rpalace.oauth.interceptor.OAuth2RestTemplateInterceptor;
import org.kushtrimhajrizi.rpalace.service.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;
import java.util.Optional;

@RestController
public class OAuth2Controller {

    private final OAuth2TokenService oAuth2TokenService;
    private final UserService userService;
    private final OAuth2RestTemplateInterceptor restTemplateInterceptor;

    public OAuth2Controller(OAuth2TokenService oAuth2TokenService,
                            UserService userService,
                            OAuth2RestTemplateInterceptor restTemplateInterceptor) {
        this.oAuth2TokenService = oAuth2TokenService;
        this.userService = userService;
        this.restTemplateInterceptor = restTemplateInterceptor;
    }

    @RequestMapping(value = "/oauth", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> oauthPage(HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/oauth2/authorization/reddit"));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }


    @GetMapping("/oauth/callback")
    public String redirectUri(Authentication authentication) throws UserDoesNotExist, UserAlreadyHasToken {
        Optional<String> token = oAuth2TokenService.getToken();
        if (token.isPresent()) {
            if (authentication.getPrincipal() instanceof DefaultUser defaultUser && defaultUser.getToken() == null) {
                userService.setToken(defaultUser.getId(), token.get());
            }
        }
        return token.orElse("No Token"); // Temporary
    }
}
