package com.suncloth.suncloth.repository.querydsl;

import com.querydsl.jpa.impl.JPAQuery;
import com.suncloth.suncloth.model.QUser;
import com.suncloth.suncloth.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomizedUserRepositoryImpl implements CustomizedUserRepository {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findByUsernameCustom(String username) {
        // QUser qUser = new QUser("q");
        QUser quser = QUser.user;
        JPAQuery<?> query = new JPAQuery<Void>(em);
        List<User> users = query.select(quser)
                .from(quser)
                .where(quser.username.contains(username))
                .fetch();
        return users;
    }

    @Override
    public List<User> findByUsernameJdbc(String username) {
        // 단일 조회 쿼리
        /*String sql = "SELECT * FROM CUSTOMER WHERE ID = ?";

        return (Customer) jdbcTemplate.queryForObject(
                sql,
                new Object[]{id},
                new BeanPropertyRowMapper(Customer.class));*/

        String sql = "select * from user_tbl where username like ?";

        return jdbcTemplate.query(
                sql,
                new Object[]{"%" + username + "%"},
                new BeanPropertyRowMapper<>(User.class));
    }
}

