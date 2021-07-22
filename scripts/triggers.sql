delimiter $$
create trigger prd_price before insert on PRD
for each row
begin
if new.price <= 0 then
CALL raise_error;
end if;
end;$$

create trigger prd_price_upd before update on PRD
for each row
begin
if new.price <= 0 then
CALL raise_error;
end if;
end;$$

CREATE TRIGGER ord_cost before insert on ORD
for each row
begin
if new.cost <= 0 then
CALL raise_error;
end if;
end;$$

CREATE TRIGGER ord_cost_upd before update on ORD
for each row
begin
if new.cost <= 0 then
CALL raise_error;
end if;
end;$$

create trigger spr_quantity before insert on spr
for each row
begin
if new.quantity < 0 then 
CALL raise_error;
end if;
end;$$

create trigger spr_quantity_upd before update on spr
for each row
begin
if new.quantity < 0 then 
CALL raise_error;
end if;
end;$$

create trigger opr_quantity before insert on opr
for each row
begin
if new.quantity <= 0 then 
CALL raise_error;
end if;
end;$$

create trigger opr_quantity_upd before update on opr
for each row
begin
if new.quantity <= 0 then 
CALL raise_error;
end if;
end;$$

create trigger clt_phone before insert on clt
for each row
begin
if (new.phone REGEXP '8[0-9]{10}' = 0) then 
CALL raise_error;
end if;
end;$$

create trigger clt_phone_upd before update on clt
for each row
begin
if (new.phone REGEXP '8[0-9]{10}' = 0) then 
CALL raise_error;
end if;
end;$$

delimiter ;