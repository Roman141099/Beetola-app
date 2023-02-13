--liquibase formatted sql

--changeset romajan:create-currency-rate
CREATE TABLE IF NOT EXISTS "currency_rate" (
    id VARCHAR(3) PRIMARY KEY,
    coefficient NUMERIC(20, 4) NOT NULL
);