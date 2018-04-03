package com.rhamerica.oauth.data.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class AuthCode {
    @Id
    private String authCodeId;

    public String getAuthCodeId() {
        return authCodeId;
    }

    public void setAuthCodeId(String authCodeId) {
        this.authCodeId = authCodeId;
    }
}
