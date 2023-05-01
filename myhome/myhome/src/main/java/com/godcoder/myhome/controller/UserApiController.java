package com.godcoder.myhome.controller;

import com.godcoder.myhome.model.Board;
import com.godcoder.myhome.model.User;
import com.godcoder.myhome.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    List<User> all() {
        // return repository.findAll();
        // : 사용자를 먼저 조회, 사용자에 해당하는 board들을 조회함 => user 100개 있으면 101개의 sql이 조회가 될 것임
        List<User> users = repository.findAll(); // => 이건 board를 지금 사용안해서 조회 안함
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
