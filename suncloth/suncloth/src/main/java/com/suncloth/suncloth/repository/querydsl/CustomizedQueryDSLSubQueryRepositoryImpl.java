package com.suncloth.suncloth.repository.querydsl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suncloth.suncloth.model.Cloth;
import com.suncloth.suncloth.model.QBrand;
import com.suncloth.suncloth.model.QCloth;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.querydsl.jpa.JPAExpressions.select;

@Repository
public class CustomizedQueryDSLSubQueryRepositoryImpl {
    @PersistenceContext
    private EntityManager em;

    @Autowired
    JdbcTemplate jdbcTemplate;
    JPAQueryFactory queryFactory = new JPAQueryFactory(em);

    // 기본 사용 법
    public void baseSubQuery(){
        QCloth cloth = QCloth.cloth;
        QCloth subCloth = new QCloth("subCloth");

        List<Cloth> result = queryFactory
                .selectFrom(cloth)
                .where(cloth.clothId.eq(
                        //JPAExpressions.
                        select(subCloth.clothId.min())
                                .from(subCloth) // 이거에 대한 결과가 나올거임
                ))
                .fetch();
    }

    // 수치 비교 서브쿼리
    public void shameSubQuery() {
        QCloth subCloth = new QCloth("subCloth");
        QCloth cloth = QCloth.cloth;

        // goe, loe 등 사용하여 수치 비교하는 서브쿼리 가능
        // goe (크거나 같은) , loe (작거나 같은), gt (큰) , lt (작은) 이런식으로 비교가 가능하다.
        List<Cloth> result = queryFactory
                .selectFrom(cloth)
                .where(cloth.clothId.loe(
                        //JPAExpressions.
                        select(subCloth.clothId.avg())
                                .from(subCloth) // 이거에 대한 결과가 나올거임
                ))
                .fetch();

        //in query도 사용 가능함
        List<Cloth> inResult = queryFactory
                .selectFrom(cloth)
                .where(cloth.clothId.in(
                        //JPAExpressions.
                        select(subCloth.clothId)
                                .from(subCloth)
                                .where(subCloth.clothId.lt(40))// 이거에 대한 결과가 나올거임
                ))
                .fetch();

    }

    // Select 절 서브쿼리(from 절에서는 서브쿼리 사용 불가)
    public void selectSubQuery() {
        QCloth subCloth = new QCloth("subCloth");
        QCloth cloth = QCloth.cloth;

        // 만약에 Projection의 대상이 하나인 경우
        List<String> oneResult = queryFactory
                .select(cloth.clothName)
                .from(cloth)
                .fetch();

        // Projection 대상이 하나 보다 더 많은 경우
        // 1) 튜플 사용
        // 하지만 튜플은 최대한 사용 피함, 쓰면 repository에서만 내보낼때는 수정해줘서 보내줌
        List<Tuple> tupleResult = queryFactory
                .select(cloth.clothName,
                        //JPAExpressions.
                        select(subCloth.clothId.avg())
                                .from(subCloth)
                )
                .from(cloth)
                .fetch();
        for (Tuple tuple : tupleResult) {
            String clothName = tuple.get(cloth.clothName);

            System.out.println("clothName=" + clothName);
        }

        // 2) DTO 사용 ==> 이걸로 주로 사용되고 있음
        // (1) getter, setter 사용
        List<Cloth> gsResult = queryFactory
                .select(Projections.bean(Cloth.class,
                        cloth.clothName,
                        cloth.clothId))
                .from(cloth)
                .fetch();
        // (2) field 접근
        List<Cloth> fieldResult = queryFactory
                .select(Projections.fields(Cloth.class,
                        cloth.clothName,
                        cloth.clothId))
                .from(cloth)
                .fetch();
        // (3) 생성자 사용
        List<Cloth> constructorResult = queryFactory
                .select(Projections.constructor(Cloth.class,
                        cloth.clothName,
                        cloth.clothId))
                .from(cloth)
                .fetch();

        // (**) 필드 이름이 다르다면 별칭 지정
        List<Cloth> fetchResult = queryFactory
                .select(Projections.constructor(Cloth.class,
                        cloth.clothName.as("name"),
                        ExpressionUtils.as(
                                JPAExpressions
                                        .select(subCloth.clothId.max())
                                        .from(subCloth), "id")
                ))
                .from(cloth)
                .fetch();

        // (***) @QueryProjection Model 생성자에 이거 붙여주면 Q파일 만들때 생성자도 생성됨
        /*List<Cloth> QResult = queryFactory
                .select(new QCloth(cloth.clothName, cloth.clothId))
                .from(cloth)
                .fetch();*/

    }
}

