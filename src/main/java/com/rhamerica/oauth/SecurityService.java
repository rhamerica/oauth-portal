package com.rhamerica.oauth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.rhamerica.oauth.data.ClientRepository;
import com.rhamerica.oauth.data.UserRepository;
import com.rhamerica.oauth.data.model.Client;
import com.rhamerica.oauth.data.model.TokenResponse;
import com.rhamerica.oauth.data.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
public class SecurityService {

    private static final Logger log = LoggerFactory.getLogger(SecurityService.class);

    private Algorithm algorithm;

    private JWTVerifier jwtVerifier;

    @Value("${jwt.expire.code.minutes:1}")
    private Integer codeExpireMinutes = 1;

    @Value("${jwt.expire.token.minutes:30}")
    private Integer tokenExpireMinutes = 30;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    UserRepository userRepository;

    @PostConstruct
    public void init() throws UnsupportedEncodingException {
        algorithm = Algorithm.HMAC512("ZV45KGX#TdzHP187P!By*hWZf9anq#c^wysHMbwIz&iAIi6D8mYCYM7Dn!AgfIU2y*YIy@J3K39P*4iEuca&sUt4DCrt$Gue8HWW");
        jwtVerifier = JWT.require(algorithm)
                .withIssuer("RHA")
                .build();
    }

    public String generateAuthCode(Client client, User user) {
        Instant now = Instant.now();
        Instant expire = now.plus(codeExpireMinutes, ChronoUnit.MINUTES);

        String code = JWT.create()
                .withIssuer("RHA")
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(expire))
                .withSubject(user.getUserId())
                .withClaim("cid", client.getClientId())
                .sign(algorithm);
        return code;
    }

    public Optional<TokenResponse> verifyCode(String code) {
        Optional<TokenResponse> result = Optional.empty();
        try {
            DecodedJWT decodedJWT = jwtVerifier.verify(code);
            String clientId = decodedJWT.getClaim("cid").asString();
            String userId = decodedJWT.getSubject();
            Optional<Client> optionalClient = clientRepository.findById(clientId);
            if (optionalClient.isPresent()) {
                Optional<User> optionalUser = userRepository.findById(userId);
                if (optionalUser.isPresent()) {
                    TokenResponse accessToken = generateToken(optionalClient.get(), optionalUser.get());
                    result = Optional.of(accessToken);
                }
            }
        } catch (JWTVerificationException e){
            log.error("error decoding token", e);
        }
        return result;
    }


    public TokenResponse generateToken(Client client, User user) {
        Instant now = Instant.now();
        Instant expire = now.plus(tokenExpireMinutes, ChronoUnit.MINUTES);
        String token = JWT.create()
                .withIssuer("RHA")
                .withIssuedAt(Date.from(now))
                .withExpiresAt(Date.from(expire))
                .withSubject(user.getUserId())
                .withClaim("cid", client.getClientId())
                .sign(algorithm);
        TokenResponse tokenResponse = new TokenResponse();

        Long expireInSeconds = (expire.toEpochMilli() - now.toEpochMilli()) / 100;
        tokenResponse.setExpiresIn(expireInSeconds.intValue());
        tokenResponse.setAccessToken(token);
        tokenResponse.setTokenType("Bearer");
        return tokenResponse;
    }

    public Optional<TokenResponse> verifyToken(String token) {
        Optional<TokenResponse> result = Optional.empty();
        try {
            DecodedJWT decodedJWT = jwtVerifier.verify(token);
            String clientId = decodedJWT.getClaim("cid").asString();
            String userId = decodedJWT.getSubject();
            Optional<Client> optionalClient = clientRepository.findById(clientId);
            if (optionalClient.isPresent()) {
                Optional<User> optionalUser = userRepository.findById(userId);
                if (optionalUser.isPresent()) {
                    TokenResponse accessToken = generateToken(optionalClient.get(), optionalUser.get());
                    result = Optional.of(accessToken);
                }
            }
        } catch (JWTVerificationException e){
            log.error("error decoding token", e);
        }
        return result;
    }
}
