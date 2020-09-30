package org.kushtrimhajrizi.rpalace.oauth.client.registration;

import org.kushtrimhajrizi.rpalace.exception.ClientRegistrationTokenException;
import org.kushtrimhajrizi.rpalace.exception.UnsupportedClientException;
import org.kushtrimhajrizi.rpalace.exception.UserDoesNotExistException;
import org.kushtrimhajrizi.rpalace.security.user.User;
import org.kushtrimhajrizi.rpalace.security.user.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Kushtrim Hajrizi
 */
@RequestMapping("/auth/client")
@Controller
public class ClientRegistrationTokenController {

    private ClientRegistrationTokenService clientRegistrationTokenService;
    private UserService userService;
    private OAuth2AuthorizedClientService oAuth2AuthorizedClientService;
    private UserDetailsService userDetailsService;
    private ClientRegistrationRepository clientRegistrationRepository;

    public ClientRegistrationTokenController(ClientRegistrationTokenService clientRegistrationTokenService,
                                             UserService userService,
                                             OAuth2AuthorizedClientService oAuth2AuthorizedClientService,
                                             UserDetailsService userDetailsService,
                                             ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationTokenService = clientRegistrationTokenService;
        this.userService = userService;
        this.oAuth2AuthorizedClientService = oAuth2AuthorizedClientService;
        this.userDetailsService = userDetailsService;
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @GetMapping("/token")
    @ResponseBody
    public ClientRegistrationTokenDTO getClientRegistrationToken(Authentication authentication)
            throws UserDoesNotExistException {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = jwt.getSubject();
        User user = userService.getById(userId).orElseThrow(UserDoesNotExistException::new);
        ClientRegistrationTokenDTO clientRegistrationTokenDTO =
                clientRegistrationTokenService.createClientRegistrationToken(user);
        return clientRegistrationTokenDTO;
    }

    @GetMapping("/register")
    public String registerClient(Model model,
                                 @RequestParam(name = "registrationToken") String registrationToken,
                                 @RequestParam(name = "client") String client) {

        ClientRegistrationToken clientRegistrationToken = clientRegistrationTokenService
                .getClientRegistrationToken(registrationToken)
                .orElseThrow(() -> new ClientRegistrationTokenException(
                        "Client registration token not found " + registrationToken));
        User user = clientRegistrationToken.getUser();
        if (clientRegistrationRepository.findByRegistrationId(client) == null) {
            throw new UnsupportedClientException("Client is not supported " + client);
        }
        model.addAttribute("client", client);

        // clientRegistrationTokenService.delete(clientRegistrationToken);
        // TODO: Check if user is already registered with client
        // if (oAuth2AuthorizedClientService.loadAuthorizedClient(client, user.getEmail()) == null) {
        //
        // }

        UserDetails principal = userDetailsService.loadUserByUsername(user.getEmail());
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                principal, principal.getPassword(), principal.getAuthorities());
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        return "register-client";
    }
}
