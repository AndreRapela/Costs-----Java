ALTER TABLE tb_usuario MODIFY COLUMN role VARCHAR(20);

UPDATE tb_usuario SET role = 'USER' WHERE role = '0';
UPDATE tb_usuario SET role = 'ADMIN' WHERE role = '1';