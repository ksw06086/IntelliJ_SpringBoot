package com.suncloth.suncloth.service;

import com.suncloth.suncloth.model.Role;
import com.suncloth.suncloth.model.User;
import com.suncloth.suncloth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User save(User user){
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        user.setEnabled(1);
        Role role = new Role();
        role.setId(1);
        user.getRoles().add(role);

        return userRepository.save(user);
    }
}
