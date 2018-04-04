package com.rhamerica.oauth.web;

import com.rhamerica.oauth.data.model.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/userinfo")
    public ResponseEntity<UserResponse> getUser() {
    }
}
