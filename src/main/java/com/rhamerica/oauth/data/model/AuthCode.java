package com.rhamerica.oauth.data.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class AuthCode {
    @Id
    private String authCodeId;
}
