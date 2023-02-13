--liquibase formatted sql

--changeset romajan:test-data-currency-rate
insert into currency_rate(id, coefficient) values
('GBP', 1),
('EUR', 1.1193),
('RUB', 85.9989),
('USD', 1.2015);