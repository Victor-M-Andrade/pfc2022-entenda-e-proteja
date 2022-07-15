-- tabela sobre o cadastro de usuários e que é usado em quase todas as demais tabelas
create table usuario(
    id serial primary key,
    nome character varying(100) not null,
    tipo character varying(11) not null,
    email character varying(200) not null,
    senha character varying(30) not null,
    aceite boolean not null,
    data_hora timestamp without time zone not null default now()
);
-- garantindo o que sera armazenado dentro do tipo do usuário
alter table usuario
add constraint ck_user_type check(
        tipo in ('ANONIMO', 'PARCEIRO', 'AUTOR', 'ENCARREGADO')
    );
/**
 TABELAS REFERENTES AO MODULO CONSULTORIA
 */
-- tabela com dados das pessoas que irão prestar consultoria as empresas(clientes) 
create table parceiro(
    id serial primary key,
    cnpj character varying(21) not null,
    site_parceiro character varying(500) not null,
    situacao character varying(10) not null,
    descricao text not null,
    tipo_servico character varying(11) not null,
    nome_empresa character varying(100) not null,
    id_usuario integer not null references usuario(id) on update cascade on delete cascade
);
alter table parceiro
add constraint ck_partiner_situation check(
        situacao in ('APROVADO', 'REPROVADO', 'SOLICITADO', 'EXCLUIDO')
    );
alter table parceiro
add constraint ck_partiner_service_type check(
        tipo_servico in ('TECNICO', 'LEGISLATIVO')
    );
-- table com dados da empresa(cliente) que solicitaram as consultorias
create table cliente(
    id serial primary key,
    nome character varying(100) not null,
    cnpj character varying(21) not null,
    aceite boolean not null,
    data_hora timestamp without time zone not null default now()
);
-- tabela com os dados sobre a solicitações feitas pelas empresas(clientes)
create table solicitacao(
    id serial primary key,
    demanda text not null,
    tipo_servico character varying(50) not null,
    id_cliente integer not null references cliente(id) on update cascade on delete cascade
);
-- relacionamento N:N entre solicitacao - parceiro
create table solicitacao_parceiro(
    id serial primary key,
    id_parceiro integer not null references parceiro(id) on update cascade on delete cascade,
    id_solicitacao integer not null references solicitacao(id) on update cascade on delete cascade
);
alter table solicitacao_parceiro
add constraint uk_client_request unique(id_parceiro, id_solicitacao);
/**
 TABELA REFERENTE AO MODULO NOTICIA
 */
--tabela pra criar, controlar ou manipular a as notícias
create table noticia(
    id serial primary key,
    titulo character varying(100) not null,
    artigo integer not null,
    contexto text not null,
    situacao character varying(9) not null,
    palavra_chave character varying (50) not null,
    data_criacao timestamp without time zone not null default now(),
    data_publicacao timestamp without time zone default now(),
    id_autor integer not null references usuario(id) on update cascade on delete cascade,
    id_publicador integer references usuario(id) on update cascade on delete cascade
);
alter table noticia
add constraint ck_news_situation check(
        situacao in ('CRIADO', 'PUBLICADO', 'RECUSADO')
    );
/**
 TABELAS REFERENTES AO MODULO TESTE DE CONHECIMENTO
 */
-- tabela para armazenar o banco de questões
create table questao(
    id serial primary key,
    pergunta text not null,
    alternativa_a character varying(700) not null,
    alternativa_b character varying(700) not null,
    alternativa_c character varying(700),
    alternativa_d character varying(700),
    resposta character varying(1) not null,
    nivel integer not null
);
-- tabela com informações sobre o teste de conhecimento do usuário
create table teste(
    id serial primary key,
    data_hora timestamp without time zone default now(),
    qtd_acerto integer not null,
    id_usuario integer not null references usuario(id) on update cascade on delete cascade
);
-- relacionamento N:N entre teste - questao
create table teste_questao(
    id serial primary key,
    escolha character varying(1) not null,
    id_questao integer not null references questao(id) on update cascade on delete cascade,
    id_teste integer not null references teste(id) on update cascade on delete cascade
);
alter table teste_questao
add constraint uk_quest_test unique(id_teste, id_questao);