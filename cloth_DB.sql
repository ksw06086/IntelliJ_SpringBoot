/** id �� �ڵ� ���� ��� **/
-- oracle 12c �������� �������� Ʈ���ŷθ� ��������
CREATE SEQUENCE user_seq 
START WITH 1 
INCREMENT BY 1 
MAXVALUE 100000 
CYCLE 
NOCACHE;

drop SEQUENCE user_seq;
-- oracle 12c ���� identity ����
-- GENERATED [ ALWAYS | BY DEFAULT [ ON NULL ] ] AS IDENTITY [ ( identity_options ) ]
create table test(
id int generated always as IDENTITY ,
name varchar(50)
);
-- identity �ʱ�ȭ
-- ALTER TABLE [���̺��] MODIFY(�÷��� GENERATED AS IDENTITY (START WITH �ʱ�ȭ��));
ALTER TABLE test MODIFY(id GENERATED AS IDENTITY (START WITH 1));

/** user ���̺� **/
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
/** role ���̺� **/
create table role_tbl(
    id int GENERATED ALWAYS as IDENTITY,
    name VARCHAR2(50),
    CONSTRAINT pk_role_id PRIMARY key(id)
);

/** user_role ���̺�(��ť��Ƽ ������ ���� user�� role ����) **/
create table user_role(
    user_id int,
    role_id int,
    CONSTRAINT fk_user FOREIGN key(user_id) REFERENCES user_tbl(id) on delete cascade,
    CONSTRAINT fk_role FOREIGN key(role_id) REFERENCES role_tbl(id) on delete cascade
);

/** refundAcc_tbl ���̺�(��ť��Ƽ ������ ���� user�� role ����) **/
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

/************ ��ǰ ���� Table ***********/
/** ���� ī�װ�(main_category_tbl) Table **/
create table main_category_tbl(
    main_code int GENERATED ALWAYS as IDENTITY,
    main_name varchar2(50) constraint maincategory_mainname_nn_excption not null,
    CONSTRAINT maincategory_maincode_pk_excption PRIMARY key(main_code)
);

/** ���� ī�װ�(sub_category_tbl) Table **/
create table sub_category_tbl(
    sub_code int GENERATED ALWAYS as IDENTITY,
    sub_name varchar2(50) constraint subcategory_subname_nn_excption not null,
    main_code int constraint subcategory_maincode_fk_exc references main_category_tbl(main_code) on delete cascade,
    CONSTRAINT subcategory_subcode_pk_excption PRIMARY key(sub_code)
);

/** �귣��(brand_tbl) Table **/
create table brand_tbl(
    brand_id   int GENERATED ALWAYS as IDENTITY,
    brand_name varchar2(50) not null,
    reg_date   date DEFAULT sysdate, -- �����
    hp varchar2(50) default '',
    CONSTRAINT brand_brandid_pk_excption PRIMARY key(brand_id)
);

/** �̹�������(file_tbl) Table **/
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

/** ��ǰ(cloth_tbl) Table **/
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

/************ ��� ���� Table ***********/
/** ����(color_tbl) Table **/
create table color_tbl(
    color_code int GENERATED ALWAYS as IDENTITY,
    color_name varchar2(50) constraint color_colorname_nn_excption not null,
    CONSTRAINT color_colorcode_pk_excption PRIMARY key(color_code)
);

/** ������(size_tbl) Table **/
create table size_tbl(
    size_code int GENERATED ALWAYS as IDENTITY,
    size_name varchar2(50) constraint size_sizename_nn_excption not null,
    CONSTRAINT size_sizecode_pk_excption PRIMARY key(size_code)
);

/** ���(stock_tbl) Table **/
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

/************ �Խ��� ���� Table ***********/
create table board_tbl(
    num    int GENERATED ALWAYS as IDENTITY,    -- �۹�ȣ
    user_id int,                                -- �ۼ���
    content_state   varchar2(50),               -- ���� ���� ���� ǥ��
    subject     clob not null,                  -- ������
    content     clob,                           -- �� ����
    text_type varchar2(10),                     -- ��б� ����(close/open)
    pwd     VARCHAR2(30),                       -- ��б� ��й�ȣ
    ref     int DEFAULT 0,                      -- �亯�� �׷�ȭ ���̵� -- �亯�۹�ȣ�μ� ���۹�ȣ�� ��ġ�ؾ� �Ѵ�.
    ref_step      int DEFAULT 0,                -- �亯�� �׷� ����(��)
    ref_level     int DEFAULT 0,                -- �亯�� �׷� ����(�鿩����)
    reg_date      date DEFAULT sysdate,         -- �ۼ���
    ip          clob,                           -- ip
    write_state varchar2(50) default '�亯���',  -- �亯����
    board_state varchar2(50),                   -- �Խ��� ����
    CONSTRAINT board_num_pk_excption PRIMARY key(num),
    FOREIGN KEY(user_id) references user_tbl(id) on delete cascade
);

/** �Խ��� �̹�������(board_file_tbl) Table **/
REM ���� ��ǰ �̹��������� �̸� �������� ���� ���̺��� �������. �̷��� ��ȹ�� �׷��� �߿��ϴ�
REM ** �߿� !! ������ ���� ���� **
REM 1) ���� ���̺� ��ǰ�����̹���, ��ǰ�����̹���, �Խ����̹����� �����ϴ� �÷��� ����� 
REM 2) �ܷ�Ű�� ���� ������ ���� �� ���̺� id�� �־��ִ� �÷��� ���� queryDsl�̳� nativeQuery�� ��� ��������
create table board_file_tbl(
    file_id  int GENERATED ALWAYS as IDENTITY,
    org_nm clob,
    saved_nm clob, 
    saved_path clob,
    board_num int,
    CONSTRAINT board_file_id_pk_excption PRIMARY key(file_id),
    FOREIGN KEY(board_num) references board_tbl(num) on delete cascade
);