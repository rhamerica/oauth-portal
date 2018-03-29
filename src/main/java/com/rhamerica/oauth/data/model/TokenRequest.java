package com.rhamerica.oauth.data.model;

import lombok.Data;

@Data
public class TokenRequest {
    private String grantType;
    private String code;
    private String redirectUri;
    private String clientSecret;
    private String clientId;
}
