package com.rhamerica.oauth.data.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenResponse {
    @JsonProperty("accessToken")
    private String accessToken; //": "SlAV32hkKG",

    @JsonProperty("token_type")
    private String token_type; //": "Bearer",

    @JsonProperty("refresh_token")
    private String refresh_token; //": "8xLOxBtZp8",

    @JsonProperty("expires_in")
    private Integer expires_in; //": 3600,

    @JsonProperty("idToken")
    private String idToken; //

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }
}
