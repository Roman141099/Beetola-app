--liquibase formatted sql

--changeset romajan:test-data-currency-rate
insert into currency_rate(id, coefficient) values
('GBP', 1),
('EUR', 1.1349),
('RUB', 88.5809),
('USD', 1.2169);