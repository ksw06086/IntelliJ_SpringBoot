package com.suncloth.suncloth.repository.querydsl;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suncloth.suncloth.model.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomizedQueryDSLJoinRepositoryImpl {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    JdbcTemplate jdbcTemplate;
    JPAQueryFactory queryFactory = new JPAQueryFactory(em);

    public void join_innerJoin() {
        QCloth cloth = QCloth.cloth;
        QBrand brand = QBrand.brand;

        List<Cloth> result = queryFactory
                .selectFrom(cloth)
                .join(cloth.brand, brand) // innerJoin 과 같음
                //.rightJoin(cloth.brand, brand) // right outer join
                //.leftJoin(cloth.brand, brand) // left outer join
                .where(brand.brandName.eq("a"))
                //.on(brand.brandName.eq("a")) // 이것과 where 절은 결과가 같음.. inner join이라서
                .fetch();
    }

    public void thetaJoin() {
        /*
        아예 연관관계가 없는 테이블 끼리 join이 가능하다.
        예시로 만든건 회원 정보를 저장하는 member 테이블과 팀정보를 저장하는 team 테이블 에서
        회원이름과 팀이름이 같은 경우 join을 하는 방법을 작성해 놓았다.
        즉 이렇게 from 절에서 여러 엔티티를 선택하는 경우를 세타 조인이라고 한다.
        (엄밀히 말해서 세타 조인을 정의해보면 조인(join) 연산은
        카타시안 곱 (cartesian product) + 선택 연산 (select operation)
        의 형태로 이루어 지는데 선택연산의 비교 연산자가 = , < , > 등이 사용되는 연산이 세타조인이다.
        여기서 비교연산자가 = 이면 동등 조인 (equi join) 이라고 한다. )

        세타 조인에서 left outer join이나 right outer join 같은 외부 조인은 불가능하다.
         */
        QCloth cloth = QCloth.cloth;
        QBrand brand = QBrand.brand;

        List<Cloth> result = queryFactory
                .select(cloth)
                .from(cloth, brand)
                .where(cloth.clothName.eq(brand.brandName))
                .fetch();
    }

    public void On() {
        // 1) 필터링 기능
        /*List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                .leftJoin(member.team, team).on(team.name.eq("a"))
                .fetch();*/
        // 2) 연관관계 없는 테이블끼리 외부 조인
        /*List<Tuple> result = queryFactory
                .select(member, team)
                .from(member)
                //.leftJoin(member.team, team) // 기존에 leftJoin을 할때 member의 team을 꺼내서 함 , 이렇게하면 id 값으로 매칭
                .leftJoin(team).on(member.userName.eq(team.name)) // 이름으로만 매칭
                .fetch();*/
    }

    // 페치 조인은 jpa 사용시 가장 기본적으로 사용하는 성능 최적화 방식이다. 연관된 엔티티나 컬렉션을 한번에 같이 조회해온다.
    public void fetchJoin() {
        QCloth cloth = QCloth.cloth;
        QBrand brand = QBrand.brand;
        Cloth findMember = queryFactory
                .selectFrom(cloth)
                .join(cloth.brand, brand).fetchJoin()
                .where(cloth.clothName.eq("kang"))
                .fetchOne();
    }
}

