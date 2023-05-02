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
    enabled number(1),
    CONSTRAINT pk_user_id PRIMARY key(id),
    constraint uk_username unique(username)
);

INSERT into user_tbl(username, password, enabled)
values ('kim', '1234', 1);
INSERT into role_tbl(name)
values ('ROLE_ADMIN');
select * from user_tbl;
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


