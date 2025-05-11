-- Altera a coluna 'data_criacao' para permitir NULL e sem valor padrão
ALTER TABLE `Costs`.`tb_orcamento`
CHANGE COLUMN `data_criacao` `data_criacao` DATETIME NULL DEFAULT NULL;

-- Adiciona a coluna 'debitado' do tipo TINYINT
ALTER TABLE `Costs`.`tb_orcamento`
ADD COLUMN `debitado` TINYINT;

-- Atualiza todos os registros para 'debitado = 1'
UPDATE `Costs`.`tb_orcamento`
SET `debitado` = 1;