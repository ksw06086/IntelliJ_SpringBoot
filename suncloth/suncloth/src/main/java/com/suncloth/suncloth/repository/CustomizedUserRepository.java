package com.suncloth.suncloth.repository;

import com.suncloth.suncloth.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomizedUserRepository {
    List<User> findByUsernameCustom(String username);

    List<User> findByUsernameJdbc(String username);
}
