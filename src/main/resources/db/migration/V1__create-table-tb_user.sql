CREATE TABLE tb_user (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,

    account_branch VARCHAR(20) NOT NULL,
    account_number INT NOT NULL,
    account_balance NUMERIC(10, 2) NOT NULL,
    pin VARCHAR(4) NOT NULL,

    active BOOLEAN NOT NULL
);