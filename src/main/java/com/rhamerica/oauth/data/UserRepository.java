package com.rhamerica.oauth.data;

import com.rhamerica.oauth.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
}
