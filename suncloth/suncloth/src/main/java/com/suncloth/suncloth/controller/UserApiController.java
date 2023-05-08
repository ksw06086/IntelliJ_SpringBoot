package com.suncloth.suncloth.controller;

import com.suncloth.suncloth.model.User;
import com.suncloth.suncloth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@Slf4j
public class UserApiController {
    @Autowired
    private final UserRepository repository;

    public UserApiController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/users")
    Iterable<User> all(@RequestParam(required = false) String method,
                       @RequestParam(required = false, defaultValue = "") String text) {
        // return repository.findAll();
        // : 사용자를 먼저 조회, 사용자에 해당하는 board들을 조회함 => user 100개 있으면 101개의 sql이 조회가 될 것임
        Iterable<User> users = null;
        // nativeQuery  : users = repository.findByUsernameNativeQuery(text);
        // mybatis      : users = userMapper.getUsers(text);
        users = repository.findAll(); // => 이건 board를 지금 사용안해서 조회 안함
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

    @GetMapping("/userCheck/{username}")
    User idCheck(@PathVariable String username) {

        return repository.findByUsername(username);
    }

    @PutMapping("/users/{id}")
    User replaceUser(@RequestBody User newUser, @PathVariable Long id) {

        return repository.findById(id)
                .map(user -> {
                    /*user.setTitle(newUser.getTitle());
                    user.setContent(newUser.getContent());*/
                    /*user.getBoards().clear();
                    user.getBoards().addAll(newUser.getBoards());*/
                    user = newUser;
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
