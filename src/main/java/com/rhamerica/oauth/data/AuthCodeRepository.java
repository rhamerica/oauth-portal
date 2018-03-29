package com.rhamerica.oauth.data;


import com.rhamerica.oauth.data.model.AuthCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthCodeRepository extends JpaRepository<AuthCode, String> {
}
