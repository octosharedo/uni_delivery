insert into clt(id, name, phone)
values(11, 'Nina', 88118876532);
insert into clt(id, name, phone)
values(12, 'Tanya', 88117653979);
insert into clt(id, name, phone)
values(13, 'Maria', 88119871352);
insert into clt(id, name, phone)
values(14, 'Anna', 88110962856);
insert into clt(id, name, phone)
values(15, 'Klava', 88118594738);


insert into dlv (id, name, phone, employment_date, payment_info, free)
values(21, 'Iosif', 89117462056, '2015-01-01 10:01:15', '4716998573225739', true);

insert into dlv (id, name, phone, employment_date, payment_info, free)
values(22, 'Kolya', 89117689834, '2017-02-01 10:01:15', '4532896698742905', true);

insert into dlv (id, name, phone, employment_date, payment_info, free)
values(23, 'Zina', 89118926547, '2020-03-24 9:12:15', '4916965832084304', true);

insert into dlv (id, name, phone, employment_date, payment_info, free)
values(24, 'Polina', 89118876543, '2001-12-01 6:01:15', '4929746864519044', true);

insert into dlv (id, name, phone, employment_date, payment_info, free)
values(25, 'Frosia', 89116657493, '2016-07-05 07:12:59', '4929420584685895', true);





insert into ord (id, cost, address, payment, complete, datetime, deliverytime, dlv_id, clt_id)
values(31, 35, 'street 1, house 1', 'cash', true, '2020-03-25 07:12:59', 40, 21, 11);

insert into ord (id, cost, address, payment, complete, datetime, deliverytime, dlv_id, clt_id)
values(32, 3, 'street 2, house 2', 'cash', true, '2020-03-27 09:12:59', 30, 22, 12);

insert into ord (id, cost, address, payment, complete, datetime, deliverytime, dlv_id, clt_id)
values(33, 50, 'street 3, house 3', 'cash', true, '2020-03-15 12:44:01', 150, 23, 13);

insert into ord (id, cost, address, payment, complete, datetime, deliverytime, dlv_id, clt_id)
values(34, 16, 'street 4, house 4', 'cash', true, '2020-03-17 15:13:43', 90, 24, 14);

insert into ord (id, cost, address, payment, complete, datetime, deliverytime, dlv_id, clt_id)
values(35, 21, 'street 5, house 5', 'cash', true, '2020-03-20 07:12:59', 45, 25, 15);


insert into prd  (id, name, category, price, description)
values(41, 'potato', 'vegetables', 5, 'fresh');

insert into prd  (id, name, category, price, description)
values(42, 'carrot', 'vegetables', 3, 'fresh');

insert into prd  (id, name, category, price, description)
values(43, 'chocolate', 'sweet', 10, 'tasty but bad');

insert into prd  (id, name, category, price, description)
values(44, 'soap', 'household goods', 15, 'always need');

insert into prd  (id, name, category, price, description)
values(45, 'candy', 'sweet', 4, 'tasty but bad');


insert into wrh (id, address, phone)
values(51, 'street 6, house 6', 89116573848);

insert into wrh (id, address, phone)
values(52, 'street 7, house 7', 89113456274);

insert into wrh (id, address, phone)
values(53, 'street 8, house 8', 89116592746);

insert into wrh (id, address, phone)
values(54, 'street 9, house 9', 89116342856);

insert into wrh (id, address, phone)
values(55, 'street 10, house 10', 89116482064);


insert into opr (quantity, ord_id, prd_id)
values(3, 31, 41);

insert into opr (quantity, ord_id, prd_id)
values(2, 31, 43);

insert into opr (quantity, ord_id, prd_id)
values(1, 32, 42);

insert into opr (quantity, ord_id, prd_id)
values(5, 33, 43);

insert into opr (quantity, ord_id, prd_id)
values(4, 34, 45);

insert into opr (quantity, ord_id, prd_id)
values(3, 35, 41);

insert into opr (quantity, ord_id, prd_id)
values(2, 35, 42);


insert into spr (prd_id, wrh_id, quantity)
values(41, 51, 20);

insert into spr (prd_id, wrh_id, quantity)
values(42, 53, 15);

insert into spr (prd_id, wrh_id, quantity)
values(41, 54, 40);

insert into spr (prd_id, wrh_id, quantity)
values(42, 55, 70);

insert into spr (prd_id, wrh_id, quantity)
values(43, 51, 11);

insert into spr (prd_id, wrh_id, quantity)
values(44, 52, 12);

insert into spr (prd_id, wrh_id, quantity)
values(45, 53, 2);

insert into spr (prd_id, wrh_id, quantity)
values(45, 51, 6);