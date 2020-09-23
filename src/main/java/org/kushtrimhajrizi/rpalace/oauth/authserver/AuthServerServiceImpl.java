package org.kushtrimhajrizi.rpalace.oauth.authserver;

import com.nimbusds.jose.EncryptionMethod;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWEAlgorithm;
import com.nimbusds.jose.JWEHeader;
import com.nimbusds.jose.JWEObject;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jwt.JWTClaimsSet;
import org.kushtrimhajrizi.rpalace.exception.AccessTokenException;
import org.kushtrimhajrizi.rpalace.security.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Calendar;

@Service
public class AuthServerServiceImpl implements AuthServerService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServerServiceImpl.class);

    @Value("${rpalace.jwt.secret-file}")
    private String jwtSecretFilepath;
    private byte[] jwtSecret;

    @PostConstruct
    public void init() throws IOException {
        jwtSecret = Files.readAllBytes(Path.of(jwtSecretFilepath));
    }

    @Override
    public AccessTokenDTO createAccessToken(User user) throws AccessTokenException {
        Calendar expirationCalendar = Calendar.getInstance();
        expirationCalendar.add(Calendar.DAY_OF_YEAR, 7);
        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(user.getId())
                .issueTime(Calendar.getInstance().getTime())
                .expirationTime(expirationCalendar.getTime())
                .claim(JWTClaimParameter.AUTHORITIES.getParameterName(), user.getAuthorities())
                .claim(JWTClaimParameter.EMAIL.getParameterName(), user.getEmail())
                .build();
        Payload payload = new Payload(claims.toJSONObject());
        JWEHeader jweHeader = new JWEHeader(JWEAlgorithm.DIR, EncryptionMethod.A256GCM);
        JWEObject jweObject = new JWEObject(jweHeader, payload);
        try {
            DirectEncrypter directEncrypter = new DirectEncrypter(jwtSecret);
            jweObject.encrypt(directEncrypter);
            return new AccessTokenDTO(jweObject.serialize(), expirationCalendar.toInstant());
        } catch (JOSEException e) {
            logger.error("Could not create direct encrypter", e);
            throw new AccessTokenException("Could not create access token", e);
        }
    }

    @Override
    @Transactional
    public String createRefreshToken(User user) {
        return null;
    }
}
