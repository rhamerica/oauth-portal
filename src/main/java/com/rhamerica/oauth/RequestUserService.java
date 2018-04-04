package com.rhamerica.oauth;

import com.rhamerica.oauth.data.model.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Component
public class RequestUserService {

    @Autowired
    HttpServletRequest request;

    @Autowired
    SecurityService securityService;

    public Optional<TokenResponse> getRequestUser() {

        String authorization = request.getHeader("Authorization");
        if (StringUtils.isEmpty(authorization)) {
            return Optional.empty();
        }
        // Bearer xyz
        if (authorization.length() < 8) {
            return Optional.empty();
        }

        String token = authorization.substring(7);
        return securityService.verifyToken(token);
    }
}
