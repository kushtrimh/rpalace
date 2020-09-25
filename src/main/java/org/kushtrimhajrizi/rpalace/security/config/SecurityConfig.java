package org.kushtrimhajrizi.rpalace.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final DefaultUserDetailsService defaultUserDetailsService;
    private final OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> oAuth2AccessTokenResponseClient;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    public SecurityConfig(DefaultUserDetailsService defaultUserDetailsService,
                          OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> oAuth2AccessTokenResponseClient,
                          JwtAuthenticationConverter jwtAuthenticationConverter) {
        this.defaultUserDetailsService = defaultUserDetailsService;
        this.oAuth2AccessTokenResponseClient = oAuth2AccessTokenResponseClient;
        this.jwtAuthenticationConverter = jwtAuthenticationConverter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeRequests(authorize ->
                    authorize
                    .antMatchers("/register", "/auth/token", "/auth/token/refresh")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
            .oauth2Client(oauth2Client -> oauth2Client
                .authorizationCodeGrant(codeGrant ->
                        codeGrant.accessTokenResponseClient(oAuth2AccessTokenResponseClient)))
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(jwtAuthenticationConverter)));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(defaultUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
}