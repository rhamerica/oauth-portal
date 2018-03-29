package com.rhamerica.oauth;

import com.rhamerica.oauth.data.UserRepository;
import com.rhamerica.oauth.data.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    UserRepository userRepository;

    public Optional<User> findUserByEmailAndPassword(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            return Optional.empty();
        }
        User user = userOptional.get();

        boolean matches = bCryptPasswordEncoder.matches(password, user.getPassword());
        if (!matches) {
            return Optional.empty();
        }

        return userOptional;
    }

    public Optional<User> findById(String userId) {
        return userRepository.findById(userId);
    }
}
