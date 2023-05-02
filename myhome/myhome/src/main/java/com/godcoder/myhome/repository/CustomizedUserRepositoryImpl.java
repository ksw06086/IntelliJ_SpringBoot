package com.godcoder.myhome.repository;

import com.godcoder.myhome.model.QUser;
import com.godcoder.myhome.model.User;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class CustomizedUserRepositoryImpl implements CustomizedUserRepository {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public List<User> findByUsernameCustom(String username) {
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

        String sql = "SELECT * FROM USER Where username like ?";

        List<User> users = jdbcTemplate.query(
                sql,
                new Object[]{"%" + username + "%"},
                new BeanPropertyRowMapper<>(User.class));

        return users;
    }
}
