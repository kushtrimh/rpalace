package org.kushtrimhajrizi.rpalace.oauth.resourceserver;

import org.kushtrimhajrizi.rpalace.oauth.JWTClaimParameter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class ResourceServerConfig {

    public static final String AUTHORITY_PREFIX = "SCOPE";

    @Value("${rpalace.jwt.pub-file}")
    private String jwtPublicFilepath;

    private RSAPublicKey publicKey;

    @PostConstruct
    public void init() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        String publicKeyData = Files.readString(Path.of(jwtPublicFilepath));
        byte[] decodedPublicKey = Base64.getDecoder().decode(publicKeyData);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        publicKey = (RSAPublicKey) keyFactory.generatePublic(new X509EncodedKeySpec(decodedPublicKey));
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(publicKey).build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        grantedAuthoritiesConverter.setAuthoritiesClaimName(JWTClaimParameter.AUTHORITIES.getParameterName());
        grantedAuthoritiesConverter.setAuthorityPrefix(AUTHORITY_PREFIX);
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}
