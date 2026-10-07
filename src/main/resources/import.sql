-- This file allow to write SQL commands that will be emitted in test and dev.
-- The commands are commented as their support depends of the database
-- insert into myentity (id, field) values(1, 'field-1');
-- insert into myentity (id, field) values(2, 'field-2');
-- insert into myentity (id, field) values(3, 'field-3');
-- alter sequence myentity_seq restart with 4;

insert into estado (nome, sigla, idregiao) values('Tocantins', 'TO', 3);
insert into estado (nome, sigla, idregiao) values('Goias', 'GO', 1);
insert into estado (nome, sigla, idregiao) values('Rio de Janeiro', 'RJ', 4);
insert into estado (nome, sigla) values('Sao Paulo', 'SP');

insert into marca (nome, paisorigem) values('LS2', 'Espanha');
insert into marca (nome, paisorigem) values('Shoei', 'Japao');
insert into marca (nome, paisorigem) values('Pro Tork', 'Brasil');

insert into categoria (nome, descricao) values('Esportivo', 'Capacetes para pilotagem esportiva');
insert into categoria (nome, descricao) values('Urbano', 'Capacetes para uso no dia a dia na cidade');
insert into categoria (nome, descricao) values('Trilha', 'Capacetes para motocross e trilha');

insert into material (nome, descricao) values('Fibra de Carbono', 'Casco leve e resistente');
insert into material (nome, descricao) values('Policarbonato', 'Casco de plastico injetado');
insert into material (nome, descricao) values('Fibra de Vidro', 'Casco em composito de fibra de vidro');

insert into cor (nome, codigohexadecimal) values('Preto Fosco', '#1C1C1C');
insert into cor (nome, codigohexadecimal) values('Branco', '#FFFFFF');
insert into cor (nome, codigohexadecimal) values('Vermelho', '#C62828');

insert into especificacaocapacete (peso, idtipofechamento) values(1.45, 2);
insert into especificacaocapacete (peso, idtipofechamento) values(1.10, 3);
insert into especificacaocapacete (peso, idtipofechamento) values(1.20, 1);

insert into capacete (modelo, preco, idtamanho, quantidadeestoque, id_marca, id_categoria, id_material, id_cor, id_especificacao) values('FF800 Storm', 1299.90, 3, 10, 1, 1, 2, 1, 1);
insert into capaceteintegral (id, possuiviseirasolar) values(1, true);
insert into capacete (modelo, preco, idtamanho, quantidadeestoque, id_marca, id_categoria, id_material, id_cor, id_especificacao) values('J-Cruise', 3499.00, 4, 5, 2, 2, 3, 2, 2);
insert into capaceteaberto (id, possuiviseira) values(2, true);
insert into capacete (modelo, preco, idtamanho, quantidadeestoque, id_marca, id_categoria, id_material, id_cor, id_especificacao) values('Th1 Vision', 399.90, 3, 20, 3, 3, 2, 3, 3);
insert into capaceteoffroad (id, possuipala) values(3, true);

insert into fornecedor (razaosocial, cnpj, telefone) values('Moto Pecas Distribuidora Ltda', '12345678000190', '63999998888');
insert into fornecedor (razaosocial, cnpj, telefone) values('Capacetes Brasil Importadora S.A.', '98765432000110', '11988887777');

insert into capacete_fornecedor (id_capacete, id_fornecedor) values(1, 1);
insert into capacete_fornecedor (id_capacete, id_fornecedor) values(1, 2);
insert into capacete_fornecedor (id_capacete, id_fornecedor) values(2, 2);
insert into capacete_fornecedor (id_capacete, id_fornecedor) values(3, 1);
