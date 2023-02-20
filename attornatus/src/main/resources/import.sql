INSERT INTO tb_user (name, birth_date, password, created_at) VALUES ('Bob Brow', TIMESTAMP WITH TIME ZONE '1999-06-13T20:00:00', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', NOW());
INSERT INTO tb_user (name, birth_date, password, created_at) VALUES ('Maria Green', TIMESTAMP WITH TIME ZONE '2003-12-31T11:00:00', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', NOW());
INSERT INTO tb_user (name, birth_date, password, created_at) VALUES ('Jordan Perish', TIMESTAMP WITH TIME ZONE '1998-07-19T20:00:00', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', NOW());
INSERT INTO tb_user (name, birth_date, password, created_at) VALUES ('Alan Tones', TIMESTAMP WITH TIME ZONE '1989-01-12T17:00:00', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', NOW());
INSERT INTO tb_user (name, birth_date, password, created_at) VALUES ('Jason Smith', TIMESTAMP WITH TIME ZONE '2004-05-01T14:00:00', '$2a$10$eACCYoNOHEqXve8aIWT8Nu3PkMXWBaOxJ9aORUYzfMQCbVBIhZ8tG', NOW());

INSERT INTO tb_role (authority) VALUES ('ROLE_CLIENT');
INSERT INTO tb_role (authority) VALUES ('ROLE_ADMIN');

INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (1, 2);
INSERT INTO tb_user_role (user_id, role_id) VALUES (2, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (3, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (4, 1);
INSERT INTO tb_user_role (user_id, role_id) VALUES (5, 1);

INSERT INTO tb_address (public_place, cep, number, city, main_address, user_id, created_at) VALUES ('Avenida Boa Viagem', '77015-002', 800, 'Recife', true, 1, NOW());
INSERT INTO tb_address (public_place, cep, number, city, main_address, user_id, created_at) VALUES ('Rua das Flores', '29010-210', 200, 'Curitiba', false, 1, NOW());
INSERT INTO tb_address (public_place, cep, number, city, main_address, user_id, created_at) VALUES ('Praça da Liberdade', '66055-090', 234, 'Belo Horizonte', false, 1, NOW());
INSERT INTO tb_address (public_place, cep, number, city, main_address, user_id, created_at) VALUES ('Rua Oscar Freire', '50050-120', 900, 'São Paulo', false, 2, NOW());
INSERT INTO tb_address (public_place, cep, number, city, main_address, user_id, created_at) VALUES ('Avenida Atlântica', '80010-030', 1020, 'Rio de Janeiro', false, 2, NOW());
INSERT INTO tb_address (public_place, cep, number, city, main_address, user_id, created_at) VALUES ('Rua Marechal Deodoro', '60160-230', 630, 'Porto Alegre', false, 3, NOW());
INSERT INTO tb_address (public_place, cep, number, city, main_address, user_id, created_at) VALUES ('Rua XV de Novembro', '70002-900', 498, 'Florianópolis', false, 3, NOW());
INSERT INTO tb_address (public_place, cep, number, city, main_address, user_id, created_at) VALUES ('Avenida Paulista', '01310-100', 1374, 'São Paulo', false, 3, NOW());
INSERT INTO tb_address (public_place, cep, number, city, main_address, user_id, created_at) VALUES ('Rua do Carmo', '20010-010', 56, 'Rio de Janeiro', false, 4, NOW());
INSERT INTO tb_address (public_place, cep, number, city, main_address, user_id, created_at) VALUES ('Rua Augusta', '01304-001', 2026, 'São Paulo', false, 4, NOW());