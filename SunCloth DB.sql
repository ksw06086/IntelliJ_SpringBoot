create table mvc_member_tbl(
    id  VARCHAR2(20) PRIMARY KEY,
    pwd VARCHAR2(10) not null,
    name varchar2(20) not null,
    jumin1 VARCHAR2(6) not null,
    jumin2 VARCHAR2(7) not null,
    hp  varchar2(13),
    email varchar2(30) not null,
    reg_date timestamp default sysdate

);