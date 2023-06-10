package com.suncloth.suncloth.repository.querydsl;

import com.querydsl.core.QueryFactory;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suncloth.suncloth.model.*;
import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public class ClothRepositoryImpl {

    private final JPAQueryFactory queryFactory;
    public ClothRepositoryImpl(JPAQueryFactory queryFactory){
        this.queryFactory = queryFactory;
    }

    //** 상품 검색 **//
    QCloth cloth = QCloth.cloth;
    public Page<Cloth> findSearchAll(String searchType, String searchInput
            , Brand brand, MainCategory mainCategory, SubCategory subCategory
            , List<String> icons, String firstDay, String lastDay, Pageable pageable) {
        List<Cloth> content = queryFactory
                .select(cloth)
                .from(cloth)
                .where(
                        searchInputContains(searchType, searchInput),
                        brandEq(brand),
                        mainCategoryEq(mainCategory),
                        subCategoryEq(subCategory),
                        iconIn(icons),
                        dateBetween(firstDay, lastDay)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Cloth> contentCount = queryFactory
                .select(cloth)
                .from(cloth)
                .where(
                        searchInputContains(searchType, searchInput),
                        brandEq(brand),
                        mainCategoryEq(mainCategory),
                        subCategoryEq(subCategory),
                        iconIn(icons),
                        dateBetween(firstDay, lastDay)
                );
                // 페이지 관련 추가시
                /*.orderBy(makeOrderSpecifiers(member, pageable));;*/

        // offset : select 된 결과에서 가져올 row 시작지점, 기본은 0
        // limit : 몇개 가져올 것인지
        // fetch() : 리스트를 결과로 반환, fetchOne() : 단건 조회, fetchFirst() : 처음 한건 가져옴
        // fetchResults() : 페이징을 위해 total contents 가져옴<이건 써봐야 알 듯>, fetchCount() : 개수 가져옴 Long 반환
        // orderBy(객체.멤버변수.desc()/asc().nullsLast()/nullsFirst()<널 가장 나중/먼저>, ...) : 정렬
        return PageableExecutionUtils.getPage(content, pageable, contentCount::fetchCount);
    }

    // 검색 Text 비교
    public BooleanExpression searchInputContains(String searchType, String searchInput){
        // like : 지정된 str(clothName)과 같으면 return
        // contains : 지정된 str(clothName)이 포함되는 경우 true를 return
        if(searchType.equals("name")){
            if(StringUtils.isEmpty(searchInput)){
                return null;
            }
            return cloth.clothName.contains(searchInput);
        } else {
            if(StringUtils.isEmpty(searchInput)){
                return null;
            }
            return cloth.clothId.eq(Long.valueOf(searchInput));
        }
    }
    // 브랜드 비교
    public BooleanExpression brandEq(Brand brand){
        if(brand == null){
            return null;
        }
        return cloth.brand.eq(brand);
    }
    // 메인 카테고리 비교
    public BooleanExpression mainCategoryEq(MainCategory mainCategory){
        if(mainCategory == null){
            return null;
        }
        return cloth.subCategory.mainCategory.eq(mainCategory);
    }
    // 서브 카테고리 비교
    public BooleanExpression subCategoryEq(SubCategory subCategory){
        if(subCategory == null){
            return null;
        }
        return cloth.subCategory.eq(subCategory);
    }
    // 아이콘 비교
    public BooleanExpression iconIn(List<String> icons){
        if(icons.size() == 0){
            return null;
        }
        return cloth.icon.in(icons);
    }
    // 날짜 비교
    public BooleanExpression dateBetween(String firstDay, String lastDay){
        if(StringUtils.isEmpty(firstDay) && StringUtils.isEmpty(lastDay)){
            return null;
        } else if(StringUtils.isEmpty(firstDay)) {
            return cloth.regDate.loe(new Date(Date.valueOf(lastDay).getDate() + 1));
        } else if(StringUtils.isEmpty(lastDay)){
            return cloth.regDate.goe(Date.valueOf(firstDay));
        }

        return cloth.regDate.between(Date.valueOf(firstDay), Date.valueOf(lastDay));
    }


    // paging 처리 함수 사용 예시
    public Page<Cloth> search(Pageable pageable) {
        // <Paging 처리
        /* QueryResults<Member> result = queryFactory
            .selectFrom(member)
            .orderBy(member.userName.desc())
            .offset(1)
            .limit(2)
            .fetchResults();
        long total = result.getTotal(); //전체 컨텐츠 갯수
        long limit = result.getLimit(); // 현재 제한한 갯수
        long offset = result.getOffset(); // 조회 시작 위치
        List<Member> results = result.getResults(); // 조회된 컨텐츠 리스트 */ // 이렇게 해도 상관 없음

        QBrand brand = QBrand.brand;

        // cloth와 brand와 조인해서 cloth 내용 가져오기
        // 지정해준 페이지 시작점과 개수 만큼
        List<Cloth> content = queryFactory
                .select(cloth)
                .from(cloth)
                .leftJoin(cloth.brand, brand)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // cloth와 brand와 조인해서 cloth 전체 내용 가져오기
        JPAQuery<Cloth> countQuery = queryFactory
                .select(cloth)
                .from(cloth)
                .leftJoin(cloth.brand, brand);

        // 페이지 지정 개수만큼 내용 가져온거랑, 페이지 관련 객체, 내용 전체 개수 전달해주기
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchCount);
    }

    // queryDsl 그룹 함수 사용 예시
    /*public void groupHavingFunction() {
        List<Tuple> result = queryFactory
                .select(
                        team.name,
                        member.count(),
                        member.age.avg(),
                        member.age.sum(),
                        member.age.min(),
                        member.age.max()
                )
                .from(member)
                .join(member.team, team)
                .groupBy(team.name)
                .having(team.name.eq("a"))
                .fetch();

        Tuple teamA = result.get(0);

        String teamAName = teamA.get(team.name);
        Long teamACount = teamA.get(member.count());
        Double teamAAvgAge = teamA.get(member.age.avg());
        Integer teamASum = teamA.get(member.age.sum());
        Integer teamAMin = teamA.get(member.age.min());
        Integer teamAMax = teamA.get(member.age.max());
    }*/

    /*public void save(Cloth cloth) {
        em.persist(cloth);
    }*/

    /*public Cloth findOne(Long id) { return em.find(Cloth.class, id); }*/

    /*public List<Cloth> findAll() {
        return em.createQuery("select c from Cloth c", Cloth.class).getResultList();
    }*/

    /*public List<Cloth> findByName(String name) {
        return em.createQuery("select c from Cloth c where c.clothName = :name", Cloth.class)
                .setParameter("name", name)
                .getResultList();
    }*/



}
