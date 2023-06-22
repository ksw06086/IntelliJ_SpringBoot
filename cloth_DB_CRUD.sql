select * from user_tbl;
select * from role_tbl;
select * from user_role;
select * from refund_acc_tbl;
select * from main_category_tbl;
select * from sub_category_tbl;
select * from brand_tbl;
select * from file_tbl;
select * from cloth_tbl;
select * from stock_tbl;
select * from color_tbl;
select * from size_tbl;
select * from board_tbl;
select * from cart_tbl;

update user_role set role_id = 2 where user_id = 21;
select * from cloth_tbl order by cloth_id desc;

-- ������
SELECT * FROM cloth_tbl
	WHERE cloth_id = (SELECT prev_no FROM (SELECT cloth_id, LAG(cloth_id, 1, -1) OVER(ORDER BY cloth_id) AS prev_no FROM cloth_tbl) B
WHERE cloth_id = 9);

-- ������(partition BY cloth_name -> over �Լ� ������ �׷�ȭ �� �� ��)
SELECT * FROM cloth_tbl
	WHERE cloth_id = (SELECT prev_no FROM (SELECT cloth_id, LEAD(cloth_id, 1, -1) OVER(ORDER BY cloth_id) AS prev_no FROM cloth_tbl) B
WHERE cloth_id = 9);

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