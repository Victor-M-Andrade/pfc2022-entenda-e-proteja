-- Dados iniciais da tabela usuario
INSERT INTO
    usuario(nome, email, senha, aceite, path_img_profile, isadministrador)
VALUES
    ('Suporte EP', 'suporte_entenda_e_proteja@outlook.com', 'Entenda&Proteja1478.', true,'/resources/img/logo_invertido.png', true),
    ('Contato EP', 'contato_entenda_e_proteja@outlook.com', 'Entenda&Proteja1478.', true,'/resources/img/logo_invertido.png', true),
    ('Victor Andrade', 'victorma2000@live.com', 'admin01', true,'/resources/img/logo_invertido.png', true),
    ('William Silva', 'wprs18@hotmail.com', 'admin02', true,'/resources/img/logo_invertido.png', true),
    ('Alysson Vasconcelos', 'alyssonhenrique720@gmail.com', 'admin03', true,'/resources/img/logo_invertido.png', true),
    ('Lucas Lima', 'lucassouzalima1478@gmail.com', 'admin04', true,'/resources/img/logo_invertido.png', true);

-- Dados iniciais da tabela questao com nivel 1
INSERT INTO
    questao(pergunta, alternativa_a, alternativa_b, alternativa_c, alternativa_d, resposta, nivel)
VALUES
    ('O que significa a sigla LGPD?', 'Lei Geral de Proteção ao Deficiente', 'Lei Geral de Proteção de Dados Pessoais', 'Lei Geral de Proteção aos Direitos', 'Lei Geral de Proteção', 'B', 1),
    ('Segundo a lei, qual é a definição de dado pessoal?', 'Informações que têm o objetivo de identificar, contactar ou localizar uma pessoa', 'Informações que têm o objetivo de caracterizar fisicamente uma pessoa', 'Informações que têm o objetivo de definir a personalidade de uma pessoa', 'Informações que têm o objetivo de definir a personalidade de um grupo', 'A', 1),
    ('Em que ano essa lei entrou em vigor no Brasil?', '2020', '2021', '2022', '2019', 'A', 1),
    ('Qual é o principal objetivo da LGPD?', 'Evitar que as pessoas compartilhem informações pessoais de forma equivocada', 'Impedir que as empresas tenham acesso os dados pessoais dos seus clientes', 'Proteger os direitos fundamentais de liberdade e de privacidade', 'Compartilhar dados dos usuarios ', 'C', 1),
    ('Quem são os agentes no tratamento dos dados pessoais?', 'Regulador e originador', 'Controlador e operador', 'Mediador e fornecedor', 'Mediador e operador', 'B', 1),
    ('Quais dos dados a seguir são considerados pessoais?', 'Gênero, endereço de e-mail e usuário das redes sociais', 'Data de nascimento, número de telefone e endereço de trabalho', 'Nome e sobrenome, endereço residencial e hábitos de consumo', 'Nome e sobrenome, Data de nascimento e Gênero', 'C', 1    ),
    ('Quais são os tipos de dados existentes, segundo a classificação da LGPD?',         'Dados Pessoais, Dados Delicados, Dados Públicos, Dados Compartilháveis',         'Dados Pessoais, Dados Sensíveis, Dados Públicos, Dados Anonimizados',         'Dados Pessoais, Dados Secretos, Dados Públicos, Dados Particulares', 'Dados Pessoais, Dados Secretos, Dados Públicos, Dados Delicados', 'B', 1),
    ('Qual é o órgão responsável por esse tipo de fiscalização?', 'Autoridade Brasileira de Proteção de Dados', 'Autoridade Nacional de Proteção de Dados', 'Autoridade de Proteção de Dados do Brasil', 'Autoridade de Proteção de Dados no Brasil', 'B', 1),
    ('Em que ano foi sancionada a LGPD?', '2012', '2018', '2010', '2017', 'B', 1),
    ('O que é a ANPD', 'Um banco de dados', 'Órgão responsável por acompanha e aplicar sanções na LGPD', 'Uma organização internacional de apoio ao governo', 'Entidade sem fins lucrativos', 'B', 1);

-- Dados iniciais da tabela questao com nivel 2
INSERT INTO
    questao(pergunta, alternativa_a, alternativa_b, alternativa_c, alternativa_d, resposta, nivel)
VALUES
    ('Quem é o controlador de dados?', 'Um profissional de TI', 'Um advogado', 'Profissional responsável pelo tratamentos de dados pessoais', 'CEO da empresa, que em determinado momento irá responder pela segurança dos dados', 'C', 2),
    ('Obrigatoriamente as empresas terão de ter um encarregado de tratamento de dados ou a legislação é flexível?', 'Apenas empresas de tecnologia', 'Empresas peqeunas não precisam', 'Todas precisam', 'Empresas de grande porte ficam fora da regra', 'C', 2),
    ('Qual é o profissional que vai interagir com a Autoridade Nacional de Proteção de Dados?', 'O CEO', 'O diretor Regulatório', 'O diretor de Assuntos Institucionais', 'O encarregado de proteção de dados', 'D', 2),
    ('O que é tratamento de dados pessoais?', 'Organização dos dados pessoais em pastas', 'Proteção dos dados pessoais contra vazamentos', 'Filtragem de dados para saber quais são os mais adequados a serem protegidos', 'Todas as alternativas corretas', 'D', 2),
    ('Quantas são as hipóteses que a lei autoriza a realização do tratamento de dados pessoais (bases legais)?', '5', '10', '15', '12', 'B', 2),
    ('Dentre as bases legais que autorizam o tratamento dos dados pessoais, qual ou quais dessas NÃO estão previstas na lei?', 'Consentimento pelo titular', 'Cumprimento de obrigação legal',         'Execução de contrato', 'Principio da impessoalidade', 'D', 2),
    ('A LGPD estabelece a Autoridade Nacional de Proteção de Dados (ANPD). Quais das respostas abaixo melhor define a ANPD?', 'Uma agência reguladora que acompanhará os gastos das empresas com tratamento de dados pessoais', 'Um Órgão responsável por acompanhar e aplicar sanções descritas na Lei Geral de Proteção de Dados', 'O Ministro da Segurança, responsável por aplicar sanções previstas por vazamento de dados pessoais', 'Um órgão internacional de apoio aos governos para definir o tratamento de dados pessoais em todos os países', 'B', 2),
    ('Com relação ao Encarregado de tratamento de dados pessoais nas empresas, podemos afirmar:', 'Todas as empresas estão obrigadas a nomear um encarregado de tratamento de dados', 'As empresas com menos de R$ 500 mil de faturamento anual estão desobrigadas a nomear um encarregado de tratamento de dados', 'Todas as empresas devem nomear quantos encarregados de tratamento de dados quantos forem necessários', 'As empresas não estão obrigadas a ter um encarregado de tratamento de dados, entretanto a LGPD sugere que haja a nomeação de um profissional', 'A', 2),
    ('Segundo a LGPD, como podemos caracterizar um dado pessoal?', 'Somente o nome, CPF e qualquer outro número de documento de identidade do cidadão', 'Somente aqueles sensíveis como a raça, etnia, religião, posição política', 'Qualquer informação que identifique ou possa identificar uma pessoa natural', 'Somente os dados de crianças e adolescentes', 'C', 2),
    ('A Lei Geral de Proteção de Dados prevê sanções em caso de descumprimento da lei, como em uma situação de vazamento de dados pessoais. Qual a resposta correta abaixo sobre sanções previstas na LGPD?', 'Proibição total ou parcial de atividades relacionadas a tratamento de dados pessoais', 'Multa simples de até 2% do faturamento da Empresa, limitado ao valor total de R$50 (cinquenta) milhões', 'Dar publicidade às sanções impostas à empresa', 'Todas respostas acima', 'D', 2);