package org.kushtrimhajrizi.rpalace.oauth.config;

import org.kushtrimhajrizi.rpalace.oauth.interceptor.OAuth2RestTemplateInterceptor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.DefaultRefreshTokenTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2RefreshTokenGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.client.RestTemplate;

import static org.springframework.security.config.Customizer.withDefaults;


@EnableWebSecurity
public class OAuth2SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests(authorize ->  authorize.antMatchers("/login")
                .permitAll()
                .anyRequest()
                .authenticated())
            .oauth2Login(withDefaults());
    }

    @Bean
    public ClientRegistration redditClientRegistration(ClientRegistrationRepository clientRegistrationRepository) {
        return clientRegistrationRepository.findByRegistrationId("reddit");
    }

    @Bean
    public RestTemplate restTemplate(OAuth2AuthorizedClientService clientService,
            OAuth2RestTemplateInterceptor oAuth2RestTemplateInterceptor) {
        return new RestTemplateBuilder().interceptors(oAuth2RestTemplateInterceptor).build();
    }
}