-- adiciona coluna usuario_id na tabela orcamento
ALTER TABLE tb_orcamento
ADD COLUMN usuario_id BIGINT;

-- cria a chave estrangeira ligando orcamento.usuario_id -> usuario.id
ALTER TABLE tb_orcamento
ADD CONSTRAINT fk_orcamento_usuario
FOREIGN KEY (usuario_id) REFERENCES tb_usuario(id);
