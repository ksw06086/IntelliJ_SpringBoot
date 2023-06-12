package com.suncloth.suncloth.repository.querydsl;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suncloth.suncloth.model.*;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public class BoardRepositoryImpl {

    private final JPAQueryFactory queryFactory;
    public BoardRepositoryImpl(JPAQueryFactory queryFactory){
        this.queryFactory = queryFactory;
    }

    //** 상품 검색 **//
    QBoard board = QBoard.board;
    public Page<Board> findSearchAll(String searchType, String searchInput
            , User user, String firstDay, String lastDay, Pageable pageable) {
        List<Board> content = queryFactory
                .select(board)
                .from(board)
                .where(
                        searchInputContains(searchType, searchInput),
                        userEq(user),
                        dateBetween(firstDay, lastDay)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Board> contentCount = queryFactory
                .select(board)
                .from(board)
                .where(
                        searchInputContains(searchType, searchInput),
                        // userEq(user),
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
        if(searchType.equals("subject")){
            if(StringUtils.isEmpty(searchInput)){
                return null;
            }
            return board.subject.contains(searchInput);
        } else {
            if(StringUtils.isEmpty(searchInput)){
                return null;
            }
            return board.content.contains(searchInput);
        }
    }

    // User 비교
    public BooleanExpression userEq(User user){
        if(user == null){
            return null;
        }
        return board.boardUser.eq(user);
    }

    // 날짜 비교
    public BooleanExpression dateBetween(String firstDay, String lastDay){
        if(StringUtils.isEmpty(firstDay) && StringUtils.isEmpty(lastDay)){
            return null;
        } else if(StringUtils.isEmpty(firstDay)) {
            return board.regDate.loe(new Date(Date.valueOf(lastDay).getDate() + 1));
        } else if(StringUtils.isEmpty(lastDay)){
            return board.regDate.goe(Date.valueOf(firstDay));
        }

        return board.regDate.between(Date.valueOf(firstDay), Date.valueOf(lastDay));
    }



}
