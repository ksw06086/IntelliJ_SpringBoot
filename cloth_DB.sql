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
    address_num VARCHAR2(10),
    address_sub VARCHAR2(400),
    address_detail VARCHAR2(400),
    hp VARCHAR2(30),
    email_id_name VARCHAR2(30),
    email_url_code VARCHAR2(30),
    reg_date DATE DEFAULT sysdate,
    birth_day Date,
    birth_type VARCHAR2(20),
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
create table refund_acc_tbl(
    id int GENERATED ALWAYS as IDENTITY,
    user_id int,
    acc_host VARCHAR2(50),
    bank_name VARCHAR2(50),
    acc_number VARCHAR2(50),
    CONSTRAINT pk_refundAcc_id PRIMARY key(id),
    constraint uk_accNumber unique(acc_number),
    CONSTRAINT fk_user_refundAcc FOREIGN key(user_id) REFERENCES user_tbl(id) on delete cascade
);

/************ 상품 관련 Table ***********/
/** 메인 카테고리(main_category_tbl) Table **/
create table main_category_tbl(
    main_code int GENERATED ALWAYS as IDENTITY,
    main_name varchar2(50) constraint maincategory_mainname_nn_excption not null,
    CONSTRAINT maincategory_maincode_pk_excption PRIMARY key(main_code)
);

/** 서브 카테고리(sub_category_tbl) Table **/
create table sub_category_tbl(
    sub_code int GENERATED ALWAYS as IDENTITY,
    sub_name varchar2(50) constraint subcategory_subname_nn_excption not null,
    main_code int constraint subcategory_maincode_fk_exc references main_category_tbl(main_code) on delete cascade,
    CONSTRAINT subcategory_subcode_pk_excption PRIMARY key(sub_code)
);

/** 브랜드(brand_tbl) Table **/
create table brand_tbl(
    brand_id   int GENERATED ALWAYS as IDENTITY,
    brand_name varchar2(50) not null,
    reg_date   date DEFAULT sysdate, -- 등록일
    hp varchar2(50),
    CONSTRAINT brand_brandid_pk_excption PRIMARY key(brand_id)
);

/** 이미지파일(file_tbl) Table **/
create table file_tbl(
    file_id  int GENERATED ALWAYS as IDENTITY,
    org_nm clob,
    saved_nm clob, 
    saved_path clob,
    cloth_id int,
    file_type varchar2(20),
    CONSTRAINT file_id_pk_excption PRIMARY key(file_id),
    FOREIGN KEY(cloth_id) references cloth_tbl(cloth_id) on delete cascade
);

/** 상품(cloths_tbl) Table **/
create table cloth_tbl (
    cloth_id int GENERATED ALWAYS as IDENTITY,
    cloth_name varchar2(50) not null,
    content varchar2(255) DEFAULT '내용 없음',
    sub_code int,
    tex varchar2(30) not null,
    brand_id int,
    icon varchar2(30),
    plus int default 0,
    sale_price int not null,
    buy_price int not null,
    deli_day int not null,
    deli_price int default 2500,
    with_item_ids varchar2(100),
    reg_date Date default sysdate,
    CONSTRAINT cloth_clothid_pk_excption PRIMARY key(cloth_id),
    FOREIGN KEY(sub_code) references sub_category_tbl(sub_code),
    FOREIGN key(brand_id) references brand_tbl(brand_id)
);

