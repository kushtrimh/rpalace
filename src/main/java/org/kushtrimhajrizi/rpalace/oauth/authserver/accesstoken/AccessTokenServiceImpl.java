package org.kushtrimhajrizi.rpalace.oauth.authserver.accesstoken;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.kushtrimhajrizi.rpalace.exception.AccessTokenException;
import org.kushtrimhajrizi.rpalace.oauth.JWTClaimParameter;
import org.kushtrimhajrizi.rpalace.oauth.authserver.accesstoken.versioning.AccessTokenVersionService;
import org.kushtrimhajrizi.rpalace.security.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class AccessTokenServiceImpl implements AccessTokenService {

    private static final Logger logger = LoggerFactory.getLogger(AccessTokenServiceImpl.class);

    private AccessTokenVersionService accessTokenVersionService;

    public AccessTokenServiceImpl(AccessTokenVersionService accessTokenVersionService) {
        this.accessTokenVersionService = accessTokenVersionService;
    }

    @Value("${rpalace.jwt.secret-file}")
    private String jwtSecretFilepath;
    private PrivateKey privateKey;

    @PostConstruct
    public void init() throws IOException, InvalidKeySpecException, NoSuchAlgorithmException {
        String privateKeyData = Files.readString(Path.of(jwtSecretFilepath));
        byte[] decodedPrivateKey = Base64.getDecoder().decode(privateKeyData);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decodedPrivateKey));
    }

    @Override
    public AccessTokenDTO createNew(User user) throws AccessTokenException {
        String newVersion = accessTokenVersionService.updateAccessTokenVersion(user);
        user.getAccessTokenVersion().setVersion(newVersion);
        var expirationCalendar = Calendar.getInstance();
        expirationCalendar.add(Calendar.DAY_OF_YEAR, 7);
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .issueTime(Calendar.getInstance().getTime())
                .expirationTime(expirationCalendar.getTime())
                .claim(JWTClaimParameter.AUTHORITIES.getParameterName(), formatAuthorities(user.getAuthorities()))
                .claim(JWTClaimParameter.EMAIL.getParameterName(), user.getEmail())
                .claim(JWTClaimParameter.VERSION.getParameterName(), user.getAccessTokenVersion().getVersion())
                .build();
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.RS256);
        SignedJWT singedJwt = new SignedJWT(jwsHeader, claims);
        try {
            singedJwt.sign(new RSASSASigner(privateKey));
            return new AccessTokenDTO(singedJwt.serialize(), expirationCalendar.toInstant());
        } catch (JOSEException e) {
            logger.error("Could not sign access token", e);
            throw new AccessTokenException("Could not create access token", e);
        }
    }

    private String formatAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return String.join(" ", authorities.stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList()));
    }
}