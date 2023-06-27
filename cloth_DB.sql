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
    usable_plus int default 0,
    visit_cnt int default 0,
    host_memo clob,
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
    hp varchar2(50) default '',
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

/** 상품(cloth_tbl) Table **/
create table cloth_tbl (
    cloth_id int GENERATED ALWAYS as IDENTITY,
    cloth_name varchar2(50) not null,
    content varchar2(255) DEFAULT ' ',
    sub_code int,
    brand_id int,
    icon varchar2(30) default ' ',
    tex varchar2(30) not null,
    deli_day int not null,
    deli_price int default 2500,
    base_price int not null,
    with_item_ids varchar2(100) default ' ',
    reg_date Date default sysdate,
    CONSTRAINT cloth_clothid_pk_excption PRIMARY key(cloth_id),
    FOREIGN KEY(sub_code) references sub_category_tbl(sub_code) on delete cascade,
    FOREIGN key(brand_id) references brand_tbl(brand_id) on delete cascade
);

/************ 재고 관련 Table ***********/
/** 색상(color_tbl) Table **/
create table color_tbl(
    color_code int GENERATED ALWAYS as IDENTITY,
    color_name varchar2(50) constraint color_colorname_nn_excption not null,
    CONSTRAINT color_colorcode_pk_excption PRIMARY key(color_code)
);

/** 사이즈(size_tbl) Table **/
create table size_tbl(
    size_code int GENERATED ALWAYS as IDENTITY,
    size_name varchar2(50) constraint size_sizename_nn_excption not null,
    CONSTRAINT size_sizecode_pk_excption PRIMARY key(size_code)
);

/** 재고(stock_tbl) Table **/
create table stock_tbl (
    stock_id int GENERATED ALWAYS as IDENTITY,
    cloth_id int,
    color_code int,
    size_code int,
    stock_count int,
    stock_max_count int,
    plus int default 0,
    sale_price int not null,
    buy_price int not null,
    state varchar2(50) not null,
    reg_date Date default sysdate,
    CONSTRAINT stock_stockid_pk_excption PRIMARY key(stock_id),
    FOREIGN KEY(color_code) references color_tbl(color_code) on delete cascade,
    FOREIGN KEY(size_code) references size_tbl(size_code) on delete cascade,
    FOREIGN key(cloth_id) references cloth_tbl(cloth_id) on delete cascade
);

/************ 게시판 관련 Table ***********/
/** 게시판(board_tbl) Table **/
create table board_tbl(
    num    int GENERATED ALWAYS as IDENTITY,    -- 글번호
    user_id int,                                -- 작성자
    content_state   varchar2(50),               -- 문의 관련 상태 표시
    subject     clob not null,                  -- 글제목
    content     clob,                           -- 글 내용
    text_type varchar2(10),                     -- 비밀글 여부(close/open)
    pwd     VARCHAR2(30),                       -- 비밀글 비밀번호
    ref     int DEFAULT 0,                      -- 답변글 그룹화 아이디 -- 답변글번호로서 원글번호와 일치해야 한다.
    ref_step      int DEFAULT 0,                -- 답변글 그룹 스텝(행)
    ref_level     int DEFAULT 0,                -- 답변글 그룹 레벨(들여쓰기)
    reg_date      date DEFAULT sysdate,         -- 작성일
    ip          clob,                           -- ip
    write_state varchar2(50) default '답변대기',  -- 답변여부
    board_state varchar2(50),                   -- 게시판 상태
    CONSTRAINT board_num_pk_excption PRIMARY key(num),
    FOREIGN KEY(user_id) references user_tbl(id) on delete cascade
);

/** 게시판 이미지파일(board_file_tbl) Table **/
REM 현재 상품 이미지파일을 미리 만들어놔서 새로 테이블을 만들었다. 이래서 계획이 그렇게 중요하다
REM ** 중요 !! 다음에 만들 때는 **
REM 1) 파일 테이블에 상품메인이미지, 상품서브이미지, 게시판이미지를 구분하는 컬럼을 만들고 
REM 2) 외래키를 따로 만들지 말고 각 테이블 id값 넣어주는 컬럼을 만들어서 queryDsl이나 nativeQuery로 결과 가져와줌
create table board_file_tbl(
    file_id  int GENERATED ALWAYS as IDENTITY,
    org_nm clob,
    saved_nm clob, 
    saved_path clob,
    board_num int,
    CONSTRAINT board_file_id_pk_excption PRIMARY key(file_id),
    FOREIGN KEY(board_num) references board_tbl(num) on delete cascade
);

/************ 장바구니 관련 Table ***********/
/** 장바구니(cart_tbl) Table **/
create table cart_tbl(
    cart_id int GENERATED ALWAYS as IDENTITY,
    user_id int not null,
    stock_id int not null,
    count int,
    reg_date Date DEFAULT sysdate,
    CONSTRAINT cart_id_pk_excption PRIMARY key(cart_id),
    FOREIGN KEY(user_id) REFERENCES user_tbl(id) on delete cascade,
    FOREIGN KEY(stock_id) REFERENCES stock_tbl(stock_id) on delete cascade
);

/************ 주문 관련 Table ***********/
/** 주문(order_tbl) Table **/
create table order_tbl(
    order_id int GENERATED ALWAYS as IDENTITY,
    user_id int not null,
    stock_id int not null,
    count int,
    use_plus int,
    real_price int,
    reg_date Date DEFAULT sysdate,
    deposit_name varchar2(100),
    bank_name varchar2(100) constraint orders_bankname_nn_excption not null,
    pay_option varchar2(100),
    user_message clob,
    order_state varchar2(100),
    CONSTRAINT order_id_pk_excption PRIMARY key(order_id),
    FOREIGN KEY(user_id) REFERENCES user_tbl(id) on delete cascade,
    FOREIGN KEY(stock_id) REFERENCES stock_tbl(stock_id) on delete cascade
);
