-- Procedimento para adicionar colunas apenas se não existirem
-- Tabela tb_orcamento - coluna ativo
SET @exists = (SELECT COUNT(*) FROM information_schema.columns WHERE table_name = 'tb_orcamento' AND column_name = 'ativo' AND table_schema = DATABASE());
SET @query = IF(@exists <= 0, 'ALTER TABLE tb_orcamento ADD COLUMN ativo BOOLEAN DEFAULT TRUE', 'SELECT "Ativo já existe em tb_orcamento"');
PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Tabela tb_usuario - coluna ativo
SET @exists = (SELECT COUNT(*) FROM information_schema.columns WHERE table_name = 'tb_usuario' AND column_name = 'ativo' AND table_schema = DATABASE());
SET @query = IF(@exists <= 0, 'ALTER TABLE tb_usuario ADD COLUMN ativo BOOLEAN DEFAULT TRUE', 'SELECT "Ativo já existe em tb_usuario"');
PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Tabela tb_usuario - coluna role
SET @exists = (SELECT COUNT(*) FROM information_schema.columns WHERE table_name = 'tb_usuario' AND column_name = 'role' AND table_schema = DATABASE());
SET @query = IF(@exists <= 0, 'ALTER TABLE tb_usuario ADD COLUMN role TINYINT DEFAULT 1', 'SELECT "Role já existe em tb_usuario"');
PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Garante que todos os registros existentes sejam marcados como ativos e tenham uma role
UPDATE tb_orcamento SET ativo = TRUE WHERE ativo IS NULL OR ativo = FALSE;
UPDATE tb_usuario SET ativo = TRUE WHERE ativo IS NULL OR ativo = FALSE;
UPDATE tb_usuario SET role = 1 WHERE role IS NULL;

-- Define o usuário admin como ADMIN (role 0)
UPDATE tb_usuario SET role = 0 WHERE login = 'admin' OR login = 'ADMIN';

-- Cria o índice apenas se não existir
SET @exists = (SELECT COUNT(*) FROM information_schema.statistics WHERE table_name = 'tb_orcamento' AND index_name = 'idx_orcamento_nome' AND table_schema = DATABASE());
SET @query = IF(@exists <= 0, 'CREATE INDEX idx_orcamento_nome ON tb_orcamento(nome)', 'SELECT "Índice já existe"');
PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;
