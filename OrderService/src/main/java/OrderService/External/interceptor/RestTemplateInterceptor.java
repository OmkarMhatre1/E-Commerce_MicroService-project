package OrderService.External.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class RestTemplateInterceptor implements ClientHttpRequestInterceptor {

    private final OAuth2AuthorizedClientManager manager;


    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
//        request.getHeaders().add("Authorization","Bearer"
//        +manager.authorize(
//                OAuth2AuthorizeRequest
//                        .withClientRegistrationId("internal-client")
//                        .principal("internal")
//                        .build())
//                        .getAccessToken().getTokenValue());
//
//        return execution.execute(request, body);

        String token = manager.authorize(OAuth2AuthorizeRequest.withClientRegistrationId("internal-client")
                .principal("internal").build()).getAccessToken().getTokenValue();

        request.getHeaders().add("Authorization", "Bearer " + token);

        return execution.execute(request, body);


    }
}
