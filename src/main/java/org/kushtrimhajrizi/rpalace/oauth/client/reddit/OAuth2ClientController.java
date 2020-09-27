package org.kushtrimhajrizi.rpalace.oauth.client;

import org.kushtrimhajrizi.rpalace.security.user.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;

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
}
