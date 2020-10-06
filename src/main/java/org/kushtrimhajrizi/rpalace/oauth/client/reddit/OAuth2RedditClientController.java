package org.kushtrimhajrizi.rpalace.oauth.client.reddit;

import org.kushtrimhajrizi.rpalace.exception.ClientRegistrationTokenException;
import org.kushtrimhajrizi.rpalace.oauth.client.registration.ClientRegistrationToken;
import org.kushtrimhajrizi.rpalace.oauth.client.registration.ClientRegistrationTokenService;
import org.kushtrimhajrizi.rpalace.security.user.User;
import org.kushtrimhajrizi.rpalace.security.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

@RestController
public class OAuth2RedditClientController {

    private UserService userService;
    private ClientRegistrationTokenService clientRegistrationTokenService;
    private OAuth2RedditClient oAuth2RedditClient;

    public OAuth2RedditClientController(UserService userService,
                                        ClientRegistrationTokenService clientRegistrationTokenService,
                                        OAuth2RedditClient oAuth2RedditClient) {
        this.userService = userService;
        this.clientRegistrationTokenService = clientRegistrationTokenService;
        this.oAuth2RedditClient = oAuth2RedditClient;
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
}
