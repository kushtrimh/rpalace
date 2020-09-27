package org.kushtrimhajrizi.rpalace.oauth.client.reddit;

import org.kushtrimhajrizi.rpalace.security.user.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.net.URI;

@PreAuthorize("hasAuthority('SCOPE_user')")
@RestController
public class OAuth2ClientController {

    private final OAuth2TokenService oAuth2TokenService;
    private final UserService userService;

    public OAuth2ClientController(OAuth2TokenService oAuth2TokenService,
                                  UserService userService ) {
        this.oAuth2TokenService = oAuth2TokenService;
        this.userService = userService;
    }

    @RequestMapping(value = "/oauth/reddit", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> oauthPage(HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/oauth2/authorization/reddit"));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
