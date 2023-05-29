/** id 값 자동 증가 방법 **/
-- oracle 12c 이전에는 시퀀스나 트리거로만 가능했음
CREATE SEQUENCE user_seq 
START WITH 1 
INCREMENT BY 1 
MAXVALUE 100000 
CYCLE 
NOCACHE;

drop SEQUENCE user_seq;
-- oracle 12c 이후 identity 적용
-- GENERATED [ ALWAYS | BY DEFAULT [ ON NULL ] ] AS IDENTITY [ ( identity_options ) ]
create table test(
id int generated always as IDENTITY ,
name varchar(50)
);
-- identity 초기화
-- ALTER TABLE [테이블명] MODIFY(컬럼명 GENERATED AS IDENTITY (START WITH 초기화값));
ALTER TABLE test MODIFY(id GENERATED AS IDENTITY (START WITH 1));

/** user 테이블 **/
create table user_tbl(
    id int GENERATED ALWAYS as IDENTITY,
    username VARCHAR2(50),
    password VARCHAR2(100),
    name VARCHAR2(50),
    addressNum VARCHAR2(10),
    addressSub VARCHAR2(400),
    addressDetail VARCHAR2(400),
    hp VARCHAR2(30),
    emailIdName VARCHAR2(30),
    emailUrlCode VARCHAR2(30),
    reg_date DATE DEFAULT sysdate,
    birthDay Date,
    birthType VARCHAR2(20),
    enabled number(1),
    CONSTRAINT pk_user_id PRIMARY key(id),
    constraint uk_username unique(username)
);

update user_tbl
set enabled = 2
where id = 81;
INSERT into user_tbl(username, password, enabled)
values ('kim', '1234', 1);
INSERT into role_tbl(name)
values ('ROLE_ADMIN');
select * from role_tbl;
delete from user_tbl;
commit;
/** role 테이블 **/
create table role_tbl(
    id int GENERATED ALWAYS as IDENTITY,
    name VARCHAR2(50),
    CONSTRAINT pk_role_id PRIMARY key(id)
);

/** user_role 테이블(시큐리티 적용을 위해 user와 role 연결) **/
create table user_role(
    user_id int,
    role_id int,
    CONSTRAINT fk_user FOREIGN key(user_id) REFERENCES user_tbl(id) on delete cascade,
    CONSTRAINT fk_role FOREIGN key(role_id) REFERENCES role_tbl(id) on delete cascade
);

/** refundAcc_tbl 테이블(시큐리티 적용을 위해 user와 role 연결) **/
create table refundAcc_tbl(
    id int GENERATED ALWAYS as IDENTITY,
    user_id int,
    accHost VARCHAR2(50),
    bankName VARCHAR2(50),
    accNumber VARCHAR2(50),
    CONSTRAINT pk_refundAcc_id PRIMARY key(id),
    constraint uk_accNumber unique(accNumber),
    CONSTRAINT fk_user_refundAcc FOREIGN key(user_id) REFERENCES user_tbl(id) on delete cascade
);
