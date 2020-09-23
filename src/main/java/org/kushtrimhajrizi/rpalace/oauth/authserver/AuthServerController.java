package org.kushtrimhajrizi.rpalace.oauth.authserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kushtrimhajrizi.rpalace.exception.AccessTokenException;
import org.kushtrimhajrizi.rpalace.exception.UnauthorizedException;
import org.kushtrimhajrizi.rpalace.exception.UserDoesNotExistException;
import org.kushtrimhajrizi.rpalace.security.user.User;
import org.kushtrimhajrizi.rpalace.security.user.UserDTO;
import org.kushtrimhajrizi.rpalace.security.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthServerController {

    private static final Logger logger = LogManager.getLogger(AuthServerController.class);

    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private AuthServerService authServerService;

    public AuthServerController(UserService userService,
                                PasswordEncoder passwordEncoder,
                                AuthServerService authServerService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authServerService = authServerService;
    }

    @PostMapping("/auth/token")
    public AuthTokenResponse getToken(UserDTO userDto)
            throws UserDoesNotExistException, AccessTokenException {
        User user = userService.findByEmail(userDto.getEmail())
                .orElseThrow(() -> new UserDoesNotExistException("User does not exist"));
        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Password does not match");
        }
        AccessTokenDTO accessTokenDto = authServerService.createAccessToken(user);
        // String refreshToken = authServerService.createRefreshToken();
        return new AuthTokenResponse(
                accessTokenDto.getAccessToken(), "", accessTokenDto.getExpirationTime().toEpochMilli());
    }
}
