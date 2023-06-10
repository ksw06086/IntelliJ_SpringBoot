package com.suncloth.suncloth.repository;

import com.suncloth.suncloth.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    // 이름을 기준으로 유저 가져오기
    User findByUsername(String username);

    // host 제외 유저 가져오기
    List<User> findByUsernameNot(String username);
}
