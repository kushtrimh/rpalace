package org.kushtrimhajrizi.rpalace.oauth.client.reddit;

import org.kushtrimhajrizi.rpalace.security.user.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

@RestController
public class OAuth2ClientController {

    private final UserService userService;

    public OAuth2ClientController(UserService userService ) {
        this.userService = userService;
    }

    @RequestMapping(value = "/oauth2/callback", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<String> oauth2Callback(HttpServletRequest request) {
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('SCOPE_user')")
    @RequestMapping(value = "/oauth/reddit", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> oauthPage(HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/oauth2/authorization/reddit"));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
