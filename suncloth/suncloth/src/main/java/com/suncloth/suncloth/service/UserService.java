package com.suncloth.suncloth.service;

import com.suncloth.suncloth.model.RefundAccount;
import com.suncloth.suncloth.model.Role;
import com.suncloth.suncloth.model.User;
import com.suncloth.suncloth.repository.RefundAccRepository;
import com.suncloth.suncloth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefundAccRepository refundAccRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // guest insert
    public void save(User user, RefundAccount refundAccount){
        // 패스워드 암호화
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        user.setEnabled(1);

        // role 테이블 값 세팅
        Role role = new Role();
        role.setId(1);
        user.getRoles().add(role);

        // user insert
        User savedUser = userRepository.save(user);

        // refundAcc 테이블에 user 넣기
        refundAccount.setUser(savedUser);
        refundAccRepository.save(refundAccount);
    }
}
