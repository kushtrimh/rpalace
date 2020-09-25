package org.kushtrimhajrizi.rpalace.oauth.authserver;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kushtrimhajrizi.rpalace.exception.AccessTokenException;
import org.kushtrimhajrizi.rpalace.exception.RefreshTokenException;
import org.kushtrimhajrizi.rpalace.exception.UnauthorizedException;
import org.kushtrimhajrizi.rpalace.exception.UserDoesNotExistException;
import org.kushtrimhajrizi.rpalace.oauth.authserver.accesstoken.AccessTokenDTO;
import org.kushtrimhajrizi.rpalace.oauth.authserver.accesstoken.AccessTokenResponse;
import org.kushtrimhajrizi.rpalace.oauth.authserver.accesstoken.AccessTokenService;
import org.kushtrimhajrizi.rpalace.oauth.authserver.accesstoken.versioning.AccessTokenVersionService;
import org.kushtrimhajrizi.rpalace.oauth.authserver.refreshtoken.RefreshToken;
import org.kushtrimhajrizi.rpalace.oauth.authserver.refreshtoken.RefreshTokenService;
import org.kushtrimhajrizi.rpalace.security.user.User;
import org.kushtrimhajrizi.rpalace.security.user.UserDTO;
import org.kushtrimhajrizi.rpalace.security.user.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthServerController {

    private static final Logger logger = LogManager.getLogger(AuthServerController.class);

    private UserService userService;
    private PasswordEncoder passwordEncoder;
    private AccessTokenService accessTokenService;
    private RefreshTokenService refreshTokenService;

    public AuthServerController(UserService userService,
                                PasswordEncoder passwordEncoder,
                                AccessTokenService accessTokenService,
                                RefreshTokenService refreshTokenService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.accessTokenService = accessTokenService;
        this.refreshTokenService = refreshTokenService;
    }

    @PostMapping("/auth/token")
    public AccessTokenResponse getToken(UserDTO userDto)
            throws UserDoesNotExistException, AccessTokenException, RefreshTokenException {
        User user = userService.getByEmail(userDto.getEmail())
                .orElseThrow(() -> new UserDoesNotExistException("User does not exist"));
        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new UnauthorizedException("Password does not match");
        }
        AccessTokenDTO accessTokenDto = accessTokenService.createNew(user);
        String refreshToken = refreshTokenService.createNew(user);
        return new AccessTokenResponse(
                accessTokenDto.getAccessToken(), refreshToken, accessTokenDto.getExpirationTime().toEpochMilli());
    }

    @PostMapping("/auth/token/refresh")
    public AccessTokenResponse refreshToken(@RequestParam("token") String refreshToken)
            throws RefreshTokenException, AccessTokenException {
        RefreshToken token = refreshTokenService.getActiveRefreshToken(refreshToken);
        if (token.getActive()) {
            AccessTokenDTO accessTokenDto = accessTokenService.createNew(token.getUser());
            return new AccessTokenResponse(
                    accessTokenDto.getAccessToken(), accessTokenDto.getExpirationTime().toEpochMilli());
        }
        throw new RefreshTokenException("Refresh token is invalid");
    }
}
