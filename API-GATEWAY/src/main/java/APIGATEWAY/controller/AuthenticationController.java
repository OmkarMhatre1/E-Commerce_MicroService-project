package APIGATEWAY.controller;

import APIGATEWAY.model.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authenticate")
public class AuthenticationController {

    @GetMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient client,
            @AuthenticationPrincipal OidcUser user,
            Model model
    ) {

        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .userId(user.getEmail())
                .accessToken(client.getAccessToken().getTokenValue())
                .refreshToken(client.getRefreshToken().getTokenValue())
                .expireAt(client.getAccessToken().getExpiresAt().getEpochSecond())
                .authorityList(user.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .build();
        return new ResponseEntity<>(authenticationResponse, HttpStatus.OK);

//        AuthenticationResponse response=new AuthenticationResponse();
//
//        response.setUserId(user.getEmail());
//
//        response.setAccessToken(client.getAccessToken().getTokenValue());
//
//        response.setRefreshToken(client.getRefreshToken().getTokenValue());
//
//        response.setExpireAt(client.getAccessToken().getExpiresAt().getEpochSecond());
//
//        List<String> authorities = user.getAuthorities().stream().map(grantedAuthority -> {
//            return grantedAuthority.getAuthority();
//        }).collect(Collectors.toList());
//
//        response.setAuthorityList(authorities);
//
//        return new ResponseEntity<>(response,HttpStatus.OK);
   }

}

