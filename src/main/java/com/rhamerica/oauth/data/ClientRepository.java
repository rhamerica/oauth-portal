package com.rhamerica.oauth.data;

import com.rhamerica.oauth.data.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, String> {
}
