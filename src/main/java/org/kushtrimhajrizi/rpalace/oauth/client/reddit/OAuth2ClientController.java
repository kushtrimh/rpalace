package org.kushtrimhajrizi.rpalace.oauth.client.reddit;

import org.kushtrimhajrizi.rpalace.exception.ClientRegistrationTokenException;
import org.kushtrimhajrizi.rpalace.oauth.client.registration.ClientRegistrationToken;
import org.kushtrimhajrizi.rpalace.oauth.client.registration.ClientRegistrationTokenService;
import org.kushtrimhajrizi.rpalace.security.user.User;
import org.kushtrimhajrizi.rpalace.security.user.UserService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URI;

@RestController
public class OAuth2ClientController {

    private UserService userService;
    private ClientRegistrationTokenService clientRegistrationTokenService;

    public OAuth2ClientController(UserService userService,
                                  ClientRegistrationTokenService clientRegistrationTokenService) {
        this.userService = userService;
        this.clientRegistrationTokenService = clientRegistrationTokenService;
    }

    @RequestMapping(value = "/oauth2/callback", method = {RequestMethod.GET, RequestMethod.POST})
    public RedirectView oauth2Callback(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        ClientRegistrationToken clientRegistrationToken =
                clientRegistrationTokenService.getClientRegistrationToken(user)
                .orElseThrow(() -> new ClientRegistrationTokenException(
                        "Client registration token not found on callback for " + user.getId()));
        clientRegistrationTokenService.delete(user);
        SecurityContextHolder.clearContext();
        RedirectView redirect = new RedirectView();
        redirect.setUrl(clientRegistrationToken.getReturnUrl());
        return redirect;
    }

    @PreAuthorize("hasAuthority('SCOPE_user')")
    @RequestMapping(value = "/oauth/reddit", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<?> oauthPage(HttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/oauth2/authorization/reddit"));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
