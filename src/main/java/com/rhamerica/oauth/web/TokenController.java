package com.rhamerica.oauth.web;

import com.rhamerica.oauth.SecurityService;
import com.rhamerica.oauth.data.ClientRepository;
import com.rhamerica.oauth.data.model.Client;
import com.rhamerica.oauth.data.model.TokenRequest;
import com.rhamerica.oauth.data.model.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class TokenController {

    @Autowired
    SecurityService securityService;

    @Autowired
    ClientRepository clientRepository;

    @RequestMapping("/token")
    public ResponseEntity token(@ModelAttribute("token") TokenRequest tokenRequest) {
        /*
        grant_type=authorization_code
        &code=SplxlOBeZQQYbYS6WxSbIA
        &redirect_uri=https%3A%2F%2Fclient.example.org%2Fcb
        */
        if ("authorization_code".equals(tokenRequest.getGrantType())) {
            return authorizationCode(tokenRequest);
        }

        return ResponseEntity.badRequest().body(Error.INVALID_REQUEST);
    }

    private ResponseEntity authorizationCode(TokenRequest tokenRequest) {

        Optional<Client> optClient = clientRepository.findById(tokenRequest.getClientId());
        if (!optClient.isPresent()) {
            return ResponseEntity.badRequest().body(Error.INVALID_REQUEST);
        }
        Client client = optClient.get();
        if (!client.getClientSecret().equals(tokenRequest.getClientSecret())) {
            return ResponseEntity.badRequest().body(Error.INVALID_REQUEST);
        }

        Optional<TokenResponse> optionalToken = securityService.verifyCode(tokenRequest.getCode());
        if (!optionalToken.isPresent()) {
            ResponseEntity.badRequest().body(Error.INVALID_REQUEST);
        }
        return ResponseEntity.ok(optionalToken.get());
    }

    @ModelAttribute("token")
    protected TokenRequest generateTokenRequest(
            @RequestParam("grant_type") String grant_type,
            @RequestParam("code") String code,
            @RequestParam("redirect_uri") String redirect_uri,
            @RequestParam("client_id") String client_id,
            @RequestParam("client_secret") String client_secret
    ) {
        TokenRequest tokenRequest = new TokenRequest();
        tokenRequest.setGrantType(grant_type);
        tokenRequest.setCode(code);
        tokenRequest.setRedirectUri(redirect_uri);
        tokenRequest.setClientSecret(client_secret);
        tokenRequest.setClientId(client_id);
        return tokenRequest;
    }

    public static class Error {
        public static final Error INVALID_REQUEST = new Error("invalid_request");
        private final String error;
        public Error(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }
}
