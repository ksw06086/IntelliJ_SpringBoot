-- ROLE 테이블 --
insert into role_tbl(name)
values ('ROLE_USER');
insert into role_tbl(name)
values ('ROLE_ADMIN');
select * from role_tbl;

-- MainCategory 테이블 --
insert into main_category_tbl(MAIN_NAME)
values ('only suncloth');
insert into main_category_tbl(MAIN_NAME)
values ('outer');
insert into main_category_tbl(MAIN_NAME)
values ('suit');
insert into main_category_tbl(MAIN_NAME)
values ('top');
insert into main_category_tbl(MAIN_NAME)
values ('shirt');
insert into main_category_tbl(MAIN_NAME)
values ('knit');
insert into main_category_tbl(MAIN_NAME)
values ('bottom');
insert into main_category_tbl(MAIN_NAME)
values ('shoes');
insert into main_category_tbl(MAIN_NAME)
values ('acc');

-- SubCategory 테이블 --
REM only Suncloth
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('only suncloth', 1);
REM outer
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Coat', 2);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Jacket', 2);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Cardigan', 2);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Padding', 2);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Leather', 2);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Hood', 2);
REM suit
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('suit', 3);
REM top
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Long sleeve', 4);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Short sleeve', 4);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('3/4 sleeve', 4);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Hoody', 4);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Pola', 4);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Dress', 4);
REM shirt
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Long shirt', 5);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Short shirt', 5);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('base', 5);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Check', 5);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Pattern', 5);
REM knit
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Round', 6);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Cardigan', 6);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Turtle Neck', 6);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Jokky', 6);
REM bottom
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Slacks', 7);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Jeans', 7);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Cotton', 7);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Shorts', 7);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Wide', 7);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Sweat', 7);
REM shoes
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Handmade', 8);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Sneakers', 8);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Oxford', 8);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Sandal', 8);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('only suncloth', 8);
REM acc
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Socks', 9);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Watch', 9);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Muffler/Scarf', 9);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Belt', 9);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Hat', 9);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Bag', 9);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Glasses', 9);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Bracelet', 9);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Tie', 9);
insert into sub_category_tbl(sub_NAME, MAIN_CODE)
values ('Ring', 9);

-- Color 테이블 --
insert into color_tbl(COLOR_NAME)
values ('white');
insert into color_tbl(COLOR_NAME)
values ('black');
insert into color_tbl(COLOR_NAME)
values ('red');
insert into color_tbl(COLOR_NAME)
values ('blue');

-- Size 테이블 --
insert into size_tbl(SIZE_NAME)
values ('S');
insert into size_tbl(SIZE_NAME)
values ('M');
insert into size_tbl(SIZE_NAME)
values ('L');
insert into size_tbl(SIZE_NAME)
values ('XL');


commit;
