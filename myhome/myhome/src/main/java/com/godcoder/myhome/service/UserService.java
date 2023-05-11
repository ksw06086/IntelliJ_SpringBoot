package com.godcoder.myhome.service;

import com.godcoder.myhome.model.Board;
import com.godcoder.myhome.model.Role;
import com.godcoder.myhome.model.User;
import com.godcoder.myhome.repository.BoardRepository;
import com.godcoder.myhome.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // 트랜잭션 어노테이션 -> 예외 발생시 함수의 모든 데이터 SQL 반영 안됨
    // 제약이 있음 - 어노테이션 사용시 주의점
    // 1. 동일한 클래스의 트랜잭션 메소드를 호출시 트랜잭션 적용이 안됨
    // 2. public 메소드에만 적용됨
    // 3. runtimeException만 rollback이 됨, 다른 에러가 나도 rollback이 되게 하고 싶으면 rollbackFor, rollbackForClassName = {}에 해당 예외클래스 추가
    // https://www.baeldung.com/transaction-configuration-with-jpa-and-spring
    // * propagation : 해당 함수 내 트랜잭션 메소드를 호출했을 경우 범위 지정
    // + propagation.require : 이전에 트랜잭션 없었으면 새로 시작하고 있으면 이전거 그대로 가게 함
    // * isolation : 여러명이 같이 사용할 때 데이터베이스의 값을 얼마나 안전하게 보장해줄 것인가?
    // + .DEFAULT - database에 기본값 따라감
    /*
    A table - id, age : 1, 10
    <.READ_UNCOMMITTED>
    tx1 -> select: id - 1, age - 10 -> update: age-20 -> rollback
    tx2 -> select: id - 1, age - 10 -> select: id-1, age-20 -> select: id-1, age-10
    <.READ COMMITTED>
    tx1 -> select: id - 1, age - 10 -> update: age-20 -> commit
    tx2 -> select: id - 1, age - 10 -> select: id-1, age-10 -> select: id-1, age-20
    <.REPEATABLE READ>
    tx1 -> select: id - 1, age - 10 -> update: age-20 -> commit
    tx2 -> select: id - 1, age - 10 -> select: id-1, age-10 -> select: id-1, age-10
    */
    // TIMEOUT : SQL이 일정시간안에 처리가 안되면 에러 발생
    @Transactional
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
