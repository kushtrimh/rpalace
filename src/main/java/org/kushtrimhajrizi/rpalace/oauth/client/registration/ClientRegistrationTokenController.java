package org.kushtrimhajrizi.rpalace.oauth.client.registration;

import org.kushtrimhajrizi.rpalace.exception.UserDoesNotExistException;
import org.kushtrimhajrizi.rpalace.exception.ValidationException;
import org.kushtrimhajrizi.rpalace.security.user.User;
import org.kushtrimhajrizi.rpalace.security.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Kushtrim Hajrizi
 */
@RestController
public class ClientRegistrationTokenController {

    private UserService userService;
    private ClientRegistrationTokenService clientRegistrationTokenService;

    public ClientRegistrationTokenController(UserService userService,
                                             ClientRegistrationTokenService clientRegistrationTokenService) {
        this.userService = userService;
        this.clientRegistrationTokenService = clientRegistrationTokenService;
    }

    @PostMapping("/auth/client/token")
    public ClientRegistrationTokenDTO getClientRegistrationToken(Authentication authentication,
                                                                 @Valid ClientRegistrationTokenDTO clientRegistrationTokenDTO,
                                                                 BindingResult bindingResult)
            throws UserDoesNotExistException {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String userId = jwt.getSubject();
        User user = userService.getById(userId).orElseThrow(UserDoesNotExistException::new);
        ClientRegistrationTokenDTO responseDTO =
                clientRegistrationTokenService.createClientRegistrationToken(user, clientRegistrationTokenDTO);
        return responseDTO;
    }
}
