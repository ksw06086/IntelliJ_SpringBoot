select * from user_tbl;
select * from role_tbl;
select * from user_role;
select * from refund_acc_tbl;
select * from main_category_tbl;
select * from sub_category_tbl;
select * from brand_tbl;
select * from file_tbl;
select * from cloth_tbl;
select * from color_tbl;
select * from size_tbl;
select * from board_tbl;

update user_role set role_id = 2 where user_id = 2;

delete user_tbl;
delete refund_acc_tbl;
delete main_category_tbl;
delete sub_category_tbl;
delete brand_tbl;
delete file_tbl;
delete cloth_tbl;
delete color_tbl;
delete size_tbl;
delete stock_tbl;
delete board_tbl;
rollback;
commit;