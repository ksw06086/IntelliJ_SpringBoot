-- 사용자 계정 생성
CREATE user jsp_pj identified by tiger DEFAULT tablespace users;

-- 사용자 권한 부여
GRANT CONNECT, RESOURCE to jsp_pj;

-- view 생성권한 부여
grant create view to jsp_pj;
  
-- 계정해제
ALTER USER jsp_pj ACCOUNT UNLOCK;

drop table users;
select * from users;
select * from authorities;
create table users(
    id varchar2(20) primary key, -- 사용자 이름(로그인ID)
    pwd varchar2(100) not null, -- 인증 암호
    name varchar2(50) not null,
    phone varchar2(30) not null,
    email varchar2(20) not null,
    authority VARCHAR2(20),
    enabled char(1) DEFAULT 1 -- 계정 사용 가능 여부 (1: 사용가능, 0: 불가)
);

create table users(
    userid VARCHAR2(255) not null, -- 아이디
    passwd VARCHAR2(255) not null, -- 비밀번호
    name   VARCHAR2(255) not null, -- 이름
    enabled number(1) DEFAULT 1, -- 사용가능 여부(1: 사용가능, 0: 사용불가)
    authority VARCHAR2(20) DEFAULT 'ROLE_USER', -- 일반사용자/ ROLE_USER
    PRIMARY key(userid)
);
drop table authorities;
create table authorities(
    username varchar2(20) not null REFERENCES users(username) on delete cascade,
    authority varchar2(50) not null -- 권한
);
-- manager --
insert into authorities(username, authority)
values('hong','USER');

insert into authorities(username, authority)
values('hong','USER_MANAGER');
commit;

-- member --
insert into authorities(username, authority)
values('park','USER');
insert into authorities(username, authority)
values('kim','USER');

create table guest_tbl(
    id varchar2(20) PRIMARY KEY,
    pwd varchar2(30) not null,
    name varchar2(50) not null,
    address varchar2(500) not null,
    address1 varchar2(500) not null,
    address2 varchar2(30),
    homephone varchar2(30) DEFAULT '연락처 없음',
    plus number(7) default 0,
    hp varchar2(30) not null,
    email varchar2(40) not null,
    birth date not null,
    birthtype varchar2(10),
    acchost varchar2(30),
    bank varchar2(30),
    acc varchar2(30),
    visitCnt number(4),
    reg_date TIMESTAMP DEFAULT sysdate,
    hostmemo varchar2(4000)
);

select * from mvc_guest_tbl;
--ALTER TABLE 테이블명 ADD(컬럼명 데이타타입(사이즈));
select TO_CHAR(reg_date, 'YYYY') from mvc_guest_tbl;
select count(*) from mvc_guest_tbl where TO_CHAR(reg_date, 'DD') = '12';
alter table mvc_guest_tbl
add hostmemo varchar2(2000);
drop table mvc_guest_tbl;
create table mvc_guest_tbl_bak(
    id varchar2(20) PRIMARY KEY,
    pwd varchar2(30) not null,
    name varchar2(50) not null,
    address varchar2(30) not null,
    address1 varchar2(30) not null,
    address2 varchar2(30),
    homephone varchar2(30) DEFAULT '연락처 없음',
    hp varchar2(30) not null,
    email varchar2(20) not null,
    birth date not null,
    birthtype varchar2(10),
    acchost varchar2(30),
    bank varchar2(30),
    acc varchar2(30),
    reg_date TIMESTAMP DEFAULT sysdate
);
commit;
insert into mvc_guest_tbl_bak
SELECT
    *
FROM mvc_guest_tbl;
insert into mvc_guest_tbl(id, pwd, name, address, address1, address2, homephone, hp, email, birth, birthtype, acchost, bank, acc, reg_date)
SELECT
    *
FROM mvc_guest_tbl_bak;
drop table mvc_guest_tbl;
update mvc_guest_tbl
set hp = '010-1234-1234'
where pwd = '1';
commit;
create table host_tbl(
    id varchar2(20) constraint hosttab_hostid_pk_excption PRIMARY KEY,
    pwd varchar2(30) constraint hosttab_hostpw_nn_excption not null,
    name varchar2(50) constraint hosttab_hostname_nn_excption not null,
    phone varchar2(30) not null,
    email varchar2(20) not null
);
drop table mvc_guest_tbl;
select * from mvc_host_tbl;

create table mvc_notice_tbl_bak(
    num NUMBER(5) PRIMARY KEY,
    id varchar2(20) not null,
    subject varchar2(400) not null,
    content varchar2(4000) not null,
    reg_date TIMESTAMP DEFAULT sysdate,
    file1 VARCHAR(300),
    readcnt NUMBER(5) default 0,
    fwnum NUMBER(5) default 0,
    nextnum NUMBER(5) default 0
);

create table notice_tbl(
    num NUMBER(5) PRIMARY KEY,
    id varchar2(20) not null,
    subject varchar2(400) not null,
    content varchar2(4000) not null,
    reg_date TIMESTAMP DEFAULT sysdate,
    file1 VARCHAR(300),
    fwnum NUMBER(5) default 0,
    nextnum NUMBER(5) default 0
);
update mvc_notice_tbl SET fwnum = 6, nextnum = 14 where num = 8;
drop table mvc_notice_tbl;
drop table mvc_notice_tbl_bak;
insert into mvc_notice_tbl_bak
SELECT
    *
FROM mvc_notice_tbl;
insert into mvc_notice_tbl(num, id, subject, content, reg_date, file1, readcnt, fwnum, nextnum)
SELECT
    *
FROM mvc_notice_tbl_bak;
select * from mvc_notice_tbl where reg_date between '2019/06/19' and '2019/06/20' and (subject like '%1%' or content like '%ㅇ%');
select * from mvc_notice_tbl order by 1 desc;
select * from (select * from mvc_notice_tbl
where num > 8
order by num asc)
where rownum = 1
UNION ALL
select * from (select * from mvc_notice_tbl
where num < 8
order by num desc)
where rownum = 1;



select * from(
select h.*, a.subject asubject, b.subject bsubject, a.texttype atexttype, b.texttype btexttype FROM
 mvc_QnA_tbl h, mvc_QnA_tbl a, mvc_QnA_tbl b
 where h.nextnum = a.num(+)
 and h.fwnum = b.num(+)
union
select num, id, state, pwd, subject, content,
            file1, textType, ref, ref_step, ref_level,  
            reg_date, ip, writestate, fwnum, nextnum, null asubject, null bsubject, null atexttype, null btexttype FROM
 mvc_QnA_tbl)
 where num = 7
order by asubject desc, bsubject desc, num asc;

select * from mvc_FAQ_tbl;


select count(*) as cnt from mvc_notice_tbl;
select num from (select * from mvc_notice_tbl 
where num < (select max(num) as maxNum from mvc_notice_tbl) order by num desc) where rownum = 1;
update mvc_notice_tbl set nextnum = 38 where num = (select max(num) as maxNum from mvc_notice_tbl);
rollback;

drop sequence notice_seq;
create SEQUENCE notice_seq
  start with 1
  increment by 1
  Maxvalue 99999
  nocache;

create table mvc_FAQ_tbl_bak(
    num NUMBER(5) PRIMARY KEY,
    id varchar2(20) not null,
    state varchar2(50) not null,
    subject varchar2(400) not null,
    content varchar2(4000) not null,
    reg_date TIMESTAMP DEFAULT sysdate
);

create table FAQ_tbl(
    num NUMBER(5) PRIMARY KEY,
    id varchar2(20) not null,
    state varchar2(50) not null,
    subject varchar2(400) not null,
    content varchar2(4000) not null,
    reg_date TIMESTAMP DEFAULT sysdate,
    fwnum NUMBER(5) default 0,
    nextnum NUMBER(5) default 0
);
commit;
drop table mvc_FAQ_tbl;
drop table mvc_FAQ_tbl_bak;
insert into mvc_FAQ_tbl_bak
SELECT
    *
FROM mvc_FAQ_tbl;
insert into mvc_FAQ_tbl(num, id, state, subject, content, reg_date)
SELECT
    *
FROM mvc_FAQ_tbl_bak;
select * from mvc_notice_tbl where reg_date between '2019/06/19' and '2019/06/20' and (subject like '%1%' or content like '%ㅇ%');
select * from mvc_faq_tbl order by num desc;
select * from 
					(select num, id, state, subject, content, reg_date, rownum rNum
						from(SELECT * FROM mvc_FAQ_tbl
							order by num desc
							) 
					)
					where rNum >= 1 and rNum <= 1
and reg_date between '2019/06/19' and '2019/06/20' 
                        and (subject like '%' || 'ㅇ' || '%' or content like '%' || 'ㅇ' || '%');
drop sequence FAQ_seq;
create SEQUENCE FAQ_seq
  start with 1
  increment by 1
  Maxvalue 99999
  nocache;
  
  
  
  
  
create table mvc_QnA_tbl_bak(
    num     NUMBER(5) PRIMARY KEY, -- 글번호
    id     VARCHAR2(20) not null, -- 작성자
    state varchar2(50) not null, -- 문의 상태
    pwd     VARCHAR2(10), -- 비밀번호
    subject     VARCHAR2(100) not null, -- 글제목
    content     VARCHAR2(4000),  -- 글 내용
    file1 VARCHAR(300),
    textType varchar(10) not null,
    ref     NUMBER(5) DEFAULT 0, -- 답변글 그룹화 아이디 -- 답변글번호로서 원글번호와 일치해야 한다.
    ref_step      NUMBER(5) DEFAULT 0, -- 답변글 그룹 스텝(행)
    ref_level     NUMBER(5) DEFAULT 0, -- 답변글 그룹 레벨(들여쓰기)
    reg_date      TIMESTAMP DEFAULT sysdate, -- 작성일
    ip          VARCHAR2(15), -- ip
    fwnum NUMBER(5) default 0,
    nextnum NUMBER(5) default 0
);  
  
create table QnA_tbl(
    num    NUMBER(5) PRIMARY KEY, -- 글번호
    id     VARCHAR2(20) not null, -- 작성자
    state varchar2(50) not null, -- 문의 상태
    pwd     VARCHAR2(30), -- 비밀번호
    subject     VARCHAR2(400) not null, -- 글제목
    content     VARCHAR2(4000),  -- 글 내용
    file1 VARCHAR(500),
    textType varchar(10) not null,
    ref     NUMBER(5) DEFAULT 0, -- 답변글 그룹화 아이디 -- 답변글번호로서 원글번호와 일치해야 한다.
    ref_step      NUMBER(5) DEFAULT 0, -- 답변글 그룹 스텝(행)
    ref_level     NUMBER(5) DEFAULT 0, -- 답변글 그룹 레벨(들여쓰기)
    reg_date      TIMESTAMP DEFAULT sysdate, -- 작성일
    ip          VARCHAR2(15), -- ip
    writestate varchar2(50) default '답변대기',
    fwnum NUMBER(5) default 0,
    nextnum NUMBER(5) default 0
);
commit;
drop table mvc_QnA_tbl;
drop table mvc_QnA_tbl_bak;
insert into mvc_QnA_tbl_bak
SELECT
    *
FROM mvc_QnA_tbl;
insert into mvc_QnA_tbl(num, id, state, pwd, subject, content,
            file1, textType, ref, ref_step, ref_level, 
            reg_date, ip, fwnum, nextnum)
SELECT
    *
FROM mvc_QnA_tbl_bak;
select * from mvc_notice_tbl where reg_date between '2019/06/19' and '2019/06/20' and (subject like '%1%' or content like '%ㅇ%');
select * from mvc_qna_tbl order by num desc;
select max(num) as maxNum from mvc_QnA_tbl where fwnum > -1 and nextnum > -1;
select * from 
					(select num, id, state, subject, content, reg_date, rownum rNum
						from(SELECT * FROM mvc_FAQ_tbl
							order by num desc
							) 
					)
					where rNum >= 1 and rNum <= 1
and reg_date between '2019/06/19' and '2019/06/20' 
                        and (subject like '%' || 'ㅇ' || '%' or content like '%' || 'ㅇ' || '%');
update mvc_qna_tbl
set fwnum = 0
where num = 13;
commit;


drop sequence QnA_seq;
create SEQUENCE QnA_seq
  start with 1
  increment by 1
  Maxvalue 99999
  nocache;
  

create table mvc_review_tbl_bak(
    num     NUMBER(5) PRIMARY KEY, -- 글번호
    id     VARCHAR2(20) not null, -- 작성자
    subject     VARCHAR2(100) not null, -- 글제목
    content     VARCHAR2(4000),  -- 글 내용
    readcnt NUMBER(5) default 0,
    file1 VARCHAR(300),
    reg_date      TIMESTAMP DEFAULT sysdate, -- 작성일
    ip          VARCHAR2(15), -- ip
    fwnum NUMBER(5) default 0,
    nextnum NUMBER(5) default 0
);  
  
create table review_tbl(
    num     NUMBER(5) PRIMARY KEY, -- 글번호
    id     VARCHAR2(20) not null, -- 작성자
    subject     VARCHAR2(400) not null, -- 글제목
    content     VARCHAR2(4000),  -- 글 
    readcnt NUMBER(5) default 0,
    file1 VARCHAR(400),
    reg_date      TIMESTAMP DEFAULT sysdate, -- 작성일
    ip          VARCHAR2(15), -- ip
    fwnum NUMBER(5) default 0,
    nextnum NUMBER(5) default 0
);
commit;
drop table mvc_review_tbl;
drop table mvc_review_tbl_bak;
insert into mvc_review_tbl_bak
SELECT
    *
FROM mvc_review_tbl;
insert into mvc_review_tbl(num, id, subject, content, readcnt,
            file1, reg_date, ip, fwnum, nextnum)
SELECT
    *
FROM mvc_review_tbl_bak;
commit;
select * from mvc_notice_tbl where reg_date between '2019/06/19' and '2019/06/20' and (subject like '%1%' or content like '%ㅇ%');
select * from mvc_review_tbl order by num desc;
select max(num) as maxNum from mvc_QnA_tbl where fwnum > -1 and nextnum > -1;
select * from 
					(select num, id, state, subject, content, reg_date, rownum rNum
						from(SELECT * FROM mvc_FAQ_tbl
							order by num desc
							) 
					)
					where rNum >= 1 and rNum <= 1
and reg_date between '2019/06/19' and '2019/06/20' 
                        and (subject like '%' || 'ㅇ' || '%' or content like '%' || 'ㅇ' || '%');
update mvc_qna_tbl
set fwnum = 0
where num = 13;
commit;


drop sequence review_seq;
create SEQUENCE review_seq
  start with 1
  increment by 1
  Maxvalue 99999
  nocache;
  
create table mvc_review_tbl_bak(
    num     NUMBER(5) PRIMARY KEY, -- 글번호
    id     VARCHAR2(20) not null, -- 작성자
    content     VARCHAR2(4000),  -- 글 내용
    reg_date      TIMESTAMP DEFAULT sysdate, -- 작성일
    ip          VARCHAR2(15), -- ip
    ref number(5) not null
);  
  
create table reply_tbl(
    num     NUMBER(5) PRIMARY KEY, -- 글번호
    id     VARCHAR2(20) not null, -- 작성자
    content     VARCHAR2(4000),  -- 글 내용
    reg_date      TIMESTAMP DEFAULT sysdate, -- 작성일
    ip          VARCHAR2(15), -- ip
    ref number(5) not null
);
commit;
drop table mvc_review_tbl;
drop table mvc_review_tbl_bak;
insert into mvc_review_tbl_bak
SELECT
    *
FROM mvc_review_tbl;
insert into mvc_review_tbl(num, id, state, pwd, subject, content,
            file1, textType, ref, ref_step, ref_level, 
            reg_date, ip, fwnum, nextnum)
SELECT
    *
FROM mvc_review_tbl_bak;
select count(*) as cnt from mvc_reply_tbl where ref = 1;
select * from mvc_notice_tbl where reg_date between '2019/06/19' and '2019/06/20' and (subject like '%1%' or content like '%ㅇ%');
select * from mvc_reply_tbl order by num desc;
select max(num) as maxNum from mvc_QnA_tbl where fwnum > -1 and nextnum > -1;
select * from 
					(select num, id, state, subject, content, reg_date, rownum rNum
						from(SELECT * FROM mvc_FAQ_tbl
							order by num desc
							) 
					)
					where rNum >= 1 and rNum <= 1
and reg_date between '2019/06/19' and '2019/06/20' 
                        and (subject like '%' || 'ㅇ' || '%' or content like '%' || 'ㅇ' || '%');
update mvc_qna_tbl
set fwnum = 0
where num = 13;
commit;


drop sequence reply_seq;
create SEQUENCE reply_seq
  start with 1
  increment by 1
  Maxvalue 99999
  nocache;

update mvc_guest_tbl
set hp = '010-1234-1234'
where pwd = '1';
commit;
create table brand_tbl(
    num number(5) PRIMARY key,
    name varchar2(50) not null,
    reg_date      TIMESTAMP DEFAULT sysdate, -- 작성일
    hp varchar2(30)
);
drop table mvc_brand_tbl;
select * from mvc_brand_tbl;

drop sequence brand_seq;
create SEQUENCE brand_seq
  start with 1
  increment by 1
  Maxvalue 99999
  nocache;

-- 대분류
create table bigpart(
    bigcode number(5) constraint bigpart_bigcode_pk_excption PRIMARY KEY,
    bigname varchar2(50) constraint bigpart_bigname_nn_excption not null
);
drop table bigpart;
select * from bigpart where bigname = 'asclo only';
select max(num) as maxNum from mvc_QnA_tbl where fwnum > -1 and nextnum > -1;
update mvc_qna_tbl
set fwnum = 0
where num = 13;
commit;

drop sequence bigpart_seq;
create SEQUENCE bigpart_seq
  start with 1
  increment by 1
  Maxvalue 99
  nocache;

create table smallpart(
    smallcode number(5) constraint smallpart_smallcode_pk_exc PRIMARY KEY,
    smallname varchar2(50) constraint smallpart_smallname_nn_exc not null,
    bigcode number(5) constraint smallpart_bigcode_fk_exc references bigpart(bigcode)
);

select * from mediumpart;
drop sequence bigpart_seq;
create SEQUENCE mediumpart_seq
  start with 1
  increment by 1
  Maxvalue 99
  nocache;

create table color_tbl(
    colorcode number(5) constraint color_colorcode_pk_excption PRIMARY KEY,
    colorname varchar2(50) constraint color_colorname_nn_excption not null
);
drop table color;

drop sequence color_seq;
create SEQUENCE color_seq
  start with 1
  increment by 1
  Maxvalue 99999
  nocache;

create table size_tbl(
    sizecode number(5) PRIMARY KEY,
    sizename varchar2(50) not null
);
drop table bigpart;

drop sequence size_seq;
create SEQUENCE size_seq
  start with 1
  increment by 1
  Maxvalue 99999
  nocache;

-- 상품
create table cloths_tbl (
    num number(5) PRIMARY key,
    name varchar2(50) not null,
    content varchar2(255) DEFAULT '내용 없음',
    smallcode number(5),
    tex varchar2(30) not null,
    brandnum number(5),
    icon varchar2(30),
    plus number(7) default 0,
    saleprice number(7) not null,
    buyprice number(7) not null,
    deliday number(5) not null,
    deliprice number(7) default 2500,
    mainfile varchar2(50) not null,
    file1 varchar2(50),
    file2 varchar2(50),
    file3 varchar2(50),
    file4 varchar2(50),
    file5 varchar2(50),
    withprdnum number(5),
    reg_date TIMESTAMP default sysdate,
    FOREIGN KEY(smallcode) references smallpart(smallcode),
    FOREIGN key(brandnum) references brand_tbl(num)
);



-- 상품
create table stock_tbl (
    num number(5) PRIMARY key,
    prdnum number(5) REFERENCES cloths_tbl(num),
    colorcode number(5) REFERENCES color_tbl(colorcode),
    sizecode number(5) REFERENCES size_tbl(sizecode),
    state varchar2(50) not null,
    maxcount number(4) default 1,
    count number(4) not null
);

create table mvc_stock_tbl_bak (
    num number(5) PRIMARY key,
    name varchar2(50) not null,
    content varchar2(255) DEFAULT '내용 없음',
    mediumcode number(2),
    colorcode number(5) REFERENCES color_tbl(colorcode),
    sizecode number(5) REFERENCES size_tbl(sizecode),
    state varchar2(50) not null,
    tex varchar2(30) not null,
    brandnum number(5),
    icon varchar2(30),
    maxcount number(5) default -1,
    plus number(7) default 0,
    saleprice number(7) not null,
    buyprice number(7) not null,
    count number(5) not null,
    deliday number(3) not null,
    deliprice number(5) default 2500,
    mainfile varchar2(50) not null,
    file1 varchar2(50),
    file2 varchar2(50),
    file3 varchar2(50),
    file4 varchar2(50),
    file5 varchar2(50),
    withprdnum number(5),
    reg_date TIMESTAMP default sysdate,
    FOREIGN KEY(mediumcode) references mediumpart(mediumcode),
    FOREIGN key(brandnum) references mvc_brand_tbl(num)
);
drop table mvc_cloths_tbl;
select distinct * from mvc_cloths_tbl order by num desc;
select * from mvc_stock_tbl order by num desc;
select * from mvc_order_tbl order by num desc;
update mvc_stock_tbl set count = count + 30, state = '판매중'
where prdnum = (select prdnum from mvc_order_tbl where num = 17)
and colorcode = (select colorcode from mvc_order_tbl where num = 17)
and sizecode = (select sizecode from mvc_order_tbl where num = 17);
commit;
insert into mvc_stock_tbl
SELECT
    STOCK_SEQ.nextval, num, colorcode, sizecode, state, maxcount, count
FROM mvc_stock_tbl_bak;
insert into mvc_cloths_tbl
SELECT
    num, name, content, mediumcode, tex, brandnum, icon, plus, saleprice, buyprice, deliday, deliprice, mainfile, file1, file2, file3, file4, file5, withprdnum, reg_date
FROM mvc_stock_tbl_bak;

drop sequence cloths_seq;
create SEQUENCE cloths_seq
  start with 1
  increment by 1
  Maxvalue 99999
  nocache;
  
drop sequence stock_seq;
create SEQUENCE stock_seq
  start with 1
  increment by 1
  Maxvalue 99999
  nocache;

select num, name, content, mediumcode, colorcode,
sizecode, state, tex, brandnum, icon, maxcount, plus, 
                saleprice, buyprice, count, deliday, deliprice, mainfile, file1, file2, 
                file3, file4, file5, withprdnum, reg_date, bname, mname, cname, sname, brandname, rownum rNum 
                from (SELECT c.*, b.bigname bname, m.mediumname mname, color.colorname cname, si.sizename sname, brand.name brandname  
                FROM mvc_cloths_tbl c, bigpart b, mediumpart m, color_tbl color, size_tbl si, mvc_brand_tbl brand
                where c.mediumcode = m.mediumcode
                and m.bigcode = b.bigcode
                and c.colorcode = color.colorcode
                and c.sizecode = si.sizecode
                and c.brandnum = brand.num
                and c.name like '%' || '' || '%'
                order by 1 desc);

select * from mvc_cloths_tbl where mediumcode in (select m.mediumcode from mediumpart m, bigpart b
where m.bigcode = b.bigcode
and b.bigname = 'only asclo')
and state = '판매중';

select * from color_tbl where colorcode in (select mvc_stock_tbl.colorcode from mvc_stock_tbl where prdnum = 9 and colorcode = 3);

select s.*, si.sizename, c.colorname from mvc_stock_tbl s, color_tbl c, size_tbl si
where s.colorcode = c.colorcode
and s.sizecode = si.sizecode
and s.sizecode = 2 and s.colorcode = 3 and prdnum = 9;

-- 장바구니
create table cart_tbl(
    num number(5) PRIMARY KEY,
    gid varchar2(20) not null,
    prdnum number(5) not null,
    colorcode number(5) REFERENCES color_tbl(colorcode),
    sizecode number(5) REFERENCES size_tbl(sizecode),
    count number(4),
    price number(7),
    reg_date TIMESTAMP DEFAULT sysdate,
    FOREIGN KEY(gid) REFERENCES guest_tbl(id),
    FOREIGN KEY(prdnum) REFERENCES cloths_tbl(num)
);

drop table mvc_cart_tbl;
drop sequence cart_seq;
create SEQUENCE cart_seq
  start with 1
  increment by 1
  Maxvalue 99999
  nocache;
select * from mvc_order_tbl;


-- 주문정보    
create table order_tbl(
    num number(6) constraint orders_ordernum_pk_excption  PRIMARY KEY,
    gid varchar2(20) constraint orders_guestid_nn_excption not null,
    prdnum number(5) constraint orders_clothid_nn_excption not null,
    colorcode number(5) constraint orders_colorcode_fk_excption REFERENCES color_tbl(colorcode),
    sizecode number(5) constraint orders_sizecode_fk_excption REFERENCES size_tbl(sizecode),
    count number(4),
    price number(7),
    useplus number(7),
    realprice number(7),
    reg_date TIMESTAMP DEFAULT sysdate,
    depositname varchar2(50),
    bankname varchar2(50) constraint orders_bankname_nn_excption not null,
    pay_option varchar2(50),
    usermessege varchar2(500),
    state varchar2(50),
    constraint orders_guestid_fk_excption FOREIGN KEY(gid) REFERENCES guest_tbl(id),
    constraint orders_clothid_fk_excption FOREIGN KEY(prdnum) REFERENCES cloths_tbl(num)
);
drop table mvc_order_tbl;
select count(*) from (select DISTINCT gid from mvc_order_tbl where TO_CHAR(reg_date, 'YYYY') = '2019');
select count(*) from mvc_order_tbl where TO_CHAR(reg_date, '2019') = '12';

drop sequence order_seq;
create SEQUENCE order_seq
  start with 1
  increment by 1
  Maxvalue 999999
  nocache;
select * from mvc_order_tbl;
select count - 125 as cnt from mvc_stock_tbl where colorcode = 2 and sizecode = 3 and prdnum = 7;

-- 장바구니
create table wishlist_tbl(
    num number(5) PRIMARY KEY,
    gid varchar2(20) not null,
    prdnum number(5) not null,
    colorcode number(5) REFERENCES color_tbl(colorcode),
    sizecode number(5) REFERENCES size_tbl(sizecode),
    count number(4),
    price number(7),
    reg_date TIMESTAMP DEFAULT sysdate,
    FOREIGN KEY(gid) REFERENCES guest_tbl(id),
    FOREIGN KEY(prdnum) REFERENCES cloths_tbl(num)
);

drop table mvc_wishlist_tbl;
drop sequence wishlist_seq;
create SEQUENCE wishlist_seq
  start with 1
  increment by 1
  Maxvalue 99999
  nocache;

--방문 토탈    
create table clicktotal_tbl(
    num number(20) PRIMARY KEY,
    gid varchar2(20) not null,
    reg_date timestamp default sysdate,
    FOREIGN KEY(gid) REFERENCES guest_tbl(id)
);
drop table mvc_clicktotal_tbl;

drop sequence clicktotal_seq;
create SEQUENCE clicktotal_seq
  start with 1
  increment by 1
  Maxvalue 9999999999999999999999
  nocache;
select * from mvc_clicktotal_tbl;


select * from(select g.name gname, g.id gid, c.cnt, c.plus, g.plus gplus
					from (SELECT gid, count(*) as cnt , sum(useplus) as plus
                    FROM mvc_order_tbl
                    where state != '환불확인' and TO_CHAR(reg_date, 'YYYY') = '2019'
                    group by gid) c, mvc_guest_tbl g where c.gid = g.id and rownum >= 1 and rownum <= 10);

select * from mvc_order_tbl where to_char(reg_date, 'YYYY-MM') = to_char(sysdate, 'YYYY-MM') and state = '주문취소';
select * from mvc_QnA_tbl where ref in (select num from mvc_QnA_tbl where id = 'k-sunwo') order by ref desc, ref_step;

LAST_DAY(hire_date) "입사한 달의 마지막 날"
TRUNC(hire_date, 'MONTH')

select to_char(sysdate, 'YYYY-MM') from dual;
select sysdate from dual;

select sum(price) as cnt from mvc_order_tbl where reg_date between sysdate - 6 and sysdate + 1;

select * from(select * from(
select num, id, '문의글' state, subject, 0 readcnt, file1, texttype, reg_date, writestate, fwnum, nextnum from mvc_QnA_tbl where id = 'k-sunwo'
union all
select num, id, '후기글' state, subject, readcnt, file1, null texttype, reg_date, null writestate, fwnum, nextnum from mvc_review_tbl where id = 'k-sunwo')
order by reg_date desc)
where rownum >= 1 and rownum <= 5;
