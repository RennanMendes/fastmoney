CREATE TABLE tb_statement  (
    id SERIAL PRIMARY KEY,
    financial_transaction VARCHAR(10) NOT NULL,
    transaction_type VARCHAR(6)  NOT NULL,
    transaction_date TIMESTAMP NOT NULL,
    transaction_value  NUMERIC(10, 2) NOT NULL,
    total_after_operation NUMERIC(10, 2) NOT NULL,

    from_account_id INT REFERENCES tb_user(id),
    to_account_id INT REFERENCES tb_user(id)
);