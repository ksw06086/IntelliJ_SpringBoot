package com.godcoder.myhome.controller;

import com.godcoder.myhome.model.Board;
import com.godcoder.myhome.model.QUser;
import com.godcoder.myhome.model.User;
import com.godcoder.myhome.repository.UserRepository;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import java.util.List;

@RestController
@RequestMapping("/api")
@Slf4j // log.debug를 찍어볼 수 있는 어노테이션
class UserApiController {

    @Autowired
    private final UserRepository repository;

    UserApiController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/users")
    Iterable<User> all(@RequestParam(required = false) String method,
                   @RequestParam(required = false, defaultValue = "") String text) {
        // return repository.findAll();
        // : 사용자를 먼저 조회, 사용자에 해당하는 board들을 조회함 => user 100개 있으면 101개의 sql이 조회가 될 것임
        Iterable<User> users = null;
        if("query".equals(method)){
            users = repository.findByUsernameQuery(text);
        } else if("nativeQuery".equals(method)){
            users = repository.findByUsernameNativeQuery(text);
        } else if("querydsl".equals(method)){
            QUser user = QUser.user;
            Predicate predicate = user.username.contains(text);
            users = repository.findAll(predicate);
        } else if("querydslCustom".equals(method)){
            users = repository.findByUsernameCustom(text);
        } else {
            users = repository.findAll(); // => 이건 board를 지금 사용안해서 조회 안함
        }
        return users;
    }
    // end::get-aggregate-root[]

    @PostMapping("/users")
    User newUser(@RequestBody User newUser) {
        return repository.save(newUser);
    }

    // Single item

    @GetMapping("/users/{id}")
    User one(@PathVariable Long id) {

        return repository.findById(id).orElse(null);
    }

    @PutMapping("/users/{id}")
    User replaceUser(@RequestBody User newUser, @PathVariable Long id) {

        return repository.findById(id)
                .map(user -> {
                    /*user.setTitle(newUser.getTitle());
                    user.setContent(newUser.getContent());*/
                    /*user.getBoards().clear();
                    user.getBoards().addAll(newUser.getBoards());*/
                    user.setBoards(newUser.getBoards());
                    for(Board board : user.getBoards()){
                        board.setUser(user);
                    }
                    return repository.save(user);
                })
                .orElseGet(() -> {
                    newUser.setId(id);
                    return repository.save(newUser);
                });
    }

    @DeleteMapping("/users/{id}")
    void deleteUser(@PathVariable Long id) {
        log.debug("여기 옴");
        repository.deleteById(id);
    }
}
