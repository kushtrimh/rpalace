package org.kushtrimhajrizi.rpalace.api.reddit;

import org.kushtrimhajrizi.rpalace.api.reddit.response.UserInfoResponse;
import org.kushtrimhajrizi.rpalace.oauth.client.reddit.OAuth2RedditClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kushtrim Hajrizi
 */
@RequestMapping("/api/v1/reddit")
@RestController
public class ApiControllerForReddit {

    private OAuth2RedditClient oAuth2RedditClient;

    public ApiControllerForReddit(OAuth2RedditClient oAuth2RedditClient) {
        this.oAuth2RedditClient = oAuth2RedditClient;
    }

    @GetMapping("/userinfo")
    public UserInfoResponse getUserInfo() {
        return oAuth2RedditClient.getUserInfo();
    }
}
