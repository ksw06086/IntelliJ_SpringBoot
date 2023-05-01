package com.godcoder.myhome.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;
    private String password;
    private boolean enabled;

    @ManyToMany
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

    // cascade = user가 삭제되면 board에서도 해당 user의 글 다 삭제됨
    /* orphanRemoval = true : orphan은 고아라는 뜻인데 부모 없는 값은 다 지움,
       곧 Board 클래스에 부모가 없으면 다 삭제하라는 것인데 부모가 clear를 해주어서 부모가 없는 값이 됨
       user.setBoards(newUser.getBoards());
       => user.getBoards().clear();
          user.getBoards().addAll(newUser.getBoards()); */
    // @OneToMany(mappedBy = "user", cascade = CascadeType.ALL) // Board클래스의 user 변수 사용하겠다.
    /* *** FetchType ***
       + EAGER : 사용자 정보 가져올 때 board까지 가져옴(@OneToOne, @ManyToOne)
       + LAZY : board를 사용할 때만 데이터 조회가 됨(@OneToMany, @ManyToMany) */
    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Board> boards = new ArrayList<>();
}
