package com.godcoder.myhome.service;

import com.godcoder.myhome.model.Board;
import com.godcoder.myhome.model.Role;
import com.godcoder.myhome.model.User;
import com.godcoder.myhome.repository.BoardRepository;
import com.godcoder.myhome.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User save(User user){
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        user.setEnabled(true);

        Role role = new Role();
        role.setId(1);
        user.getRoles().add(role);

        User savedUser = userRepository.save(user);

        // 사용자 가입인사글 자동작성
        Board board = new Board();
        board.setTitle("안녕하세요.");
        board.setContent("반갑습니다.");
        board.setUser(savedUser);
        boardRepository.save(board);


        return savedUser;
    }
}
