CREATE TABLE tb_orcamento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    valor DOUBLE,
    categoria VARCHAR(50),
    status VARCHAR(50),
    data_criacao VARCHAR(50)
);