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
    brand_id   number(5) PRIMARY key,
    brand_name varchar2(50) not null,
    reg_date   TIMESTAMP DEFAULT sysdate, -- �����
    hp varchar2(50)
);

/** ��ǰ(cloths_tbl) Table **/
create table cloths_tbl (
    num number(5) PRIMARY key,
    name varchar2(50) not null,
    content varchar2(255) DEFAULT '���� ����',
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