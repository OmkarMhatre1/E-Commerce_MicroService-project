package OrderService.External.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;

@Configuration
@RequiredArgsConstructor
public class OAuthRequestInterceptor implements RequestInterceptor {

    private final OAuth2AuthorizedClientManager manager;

    @Override
    public void apply(RequestTemplate template) {
//            template.header("Authorization","Bearer"
//            +oAuth2AuthorizedClientManager.authorize(OAuth2AuthorizeRequest
//                    .withClientRegistrationId("internal-client")
//                    .principal("internal")
//                    .build())
//                    .getAccessToken().getTokenValue());
//    }
        String token = manager.authorize(OAuth2AuthorizeRequest.withClientRegistrationId("internal-client")
                .principal("internal").build())
                .getAccessToken().getTokenValue();
        template.header("Authorization", "Bearer " + token);
    }
}
