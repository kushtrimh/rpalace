package org.kushtrimhajrizi.rpalace.config;

import org.kushtrimhajrizi.rpalace.oauth.client.reddit.OAuth2Interceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final OAuth2Interceptor oAuth2Interceptor;

    public WebConfig(OAuth2Interceptor oAuth2Interceptor) {
        this.oAuth2Interceptor = oAuth2Interceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(oAuth2Interceptor).addPathPatterns("/**").excludePathPatterns("/login", "/redirect");
    }
}
