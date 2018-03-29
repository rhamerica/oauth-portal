package com.rhamerica.oauth.data.model;access_token

import lombok.Data;

@Data
public class AuthRequest {
    private String responseType;
    private String clientId;
    private String scope;
    private String redirectUri;
    private String loginHint;
}
