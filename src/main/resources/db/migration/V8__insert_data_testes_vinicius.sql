INSERT INTO dbo.usuario (nome, email, senha, roles)
VALUES (
    'vinicius',
    'viniciuspadilhavieira@hotmail.com',
    '$2a$12$Hk/QgNlII1W.qzBCKv8ixOe4Shvp5FqikAu8.oS2HGobAvG4S4oye',
    ARRAY['USER', 'ADMIN']
)
ON CONFLICT (email) DO NOTHING;

INSERT INTO dbo.gasto (valor, descricao, nome, data, categoria_id, usuario_id) VALUES
-- Agosto 2024
(
    75.50,
    'Compras no Supermercado A',
    'Supermercado',
    '2024-08-01 10:30:00',
    (SELECT id FROM dbo.categoria WHERE nome = 'Alimentação'),
    (SELECT id FROM dbo.usuario WHERE email = 'viniciuspadilhavieira@hotmail.com')
),
(
    1200.00,
    'Aluguel do mês',
    'Aluguel',
    '2024-08-05 08:00:00',
    (SELECT id FROM dbo.categoria WHERE nome = 'Moradia'),
    (SELECT id FROM dbo.usuario WHERE email = 'viniciuspadilhavieira@hotmail.com')
),
(
    50.00,
    'Passe de ônibus mensal',
    'Transporte Público',
    '2024-08-02 12:00:00',
    (SELECT id FROM dbo.categoria WHERE nome = 'Transporte'),
    (SELECT id FROM dbo.usuario WHERE email = 'viniciuspadilhavieira@hotmail.com')
),
(
    150.25,
    'Conta de luz',
    'Energia Elétrica',
    '2024-08-10 14:00:00',
    (SELECT id FROM dbo.categoria WHERE nome = 'Serviços Públicos'),
    (SELECT id FROM dbo.usuario WHERE email = 'viniciuspadilhavieira@hotmail.com')
),
(
    45.00,
    'Ingresso para o cinema',
    'Cinema',
    '2024-08-15 20:00:00',
    (SELECT id FROM dbo.categoria WHERE nome = 'Lazer'),
    (SELECT id FROM dbo.usuario WHERE email = 'viniciuspadilhavieira@hotmail.com')
),
(
    99.90,
    'Camisa nova',
    'Loja de Roupas',
    '2024-08-20 17:45:00',
    (SELECT id FROM dbo.categoria WHERE nome = 'Compras'),
    (SELECT id FROM dbo.usuario WHERE email = 'viniciuspadilhavieira@hotmail.com')
),
(
    35.75,
    'Remédio para dor de cabeça',
    'Farmácia',
    '2024-08-22 11:10:00',
    (SELECT id FROM dbo.categoria WHERE nome = 'Saúde'),
    (SELECT id FROM dbo.usuario WHERE email = 'viniciuspadilhavieira@hotmail.com')
),
(
    180.00,
    'Jantar com amigos',
    'Restaurante Italiano',
    '2024-08-25 21:00:00',
    (SELECT id FROM dbo.categoria WHERE nome = 'Alimentação'),
    (SELECT id FROM dbo.usuario WHERE email = 'viniciuspadilhavieira@hotmail.com')
),
(
    65.00,
    'Corte de cabelo e barba',
    'Barbearia',
    '2024-08-18 16:00:00',
    (SELECT id FROM dbo.categoria WHERE nome = 'Cuidados Pessoais'),
    (SELECT id FROM dbo.usuario WHERE email = 'viniciuspadilhavieira@hotmail.com')
),
(
    250.00,
    'Mensalidade do curso de inglês',
    'Curso de Idiomas',
    '2024-08-10 09:00:00',
    (SELECT id FROM dbo.categoria WHERE nome = 'Educação'),
    (SELECT id FROM dbo.usuario WHERE email = 'viniciuspadilhavieira@hotmail.com')
),
-- Julho 2024
(
    450.80,
    'Compras do mês no Atacadão',
    'Supermercado',
    '2024-07-03 18:00:00',
    (SELECT id FROM dbo.categoria WHERE nome = 'Alimentação'),
    (SELECT id FROM dbo.usuario WHERE email = 'viniciuspadilhavieira@hotmail.com')
),
(
    1200.00,
    'Aluguel de Julho',
    'Aluguel',
    '2024-07-05 08:15:00',
    (SELECT id FROM dbo.categoria WHERE nome = 'Moradia'),
    (SELECT id FROM dbo.usuario WHERE email = 'viniciuspadilhavieira@hotmail.com')
),
(
    200.00,
    'Gasolina para o carro',
    'Posto de Gasolina',
    '2024-07-15 17:30:00',
    (SELECT id FROM dbo.categoria WHERE nome = 'Transporte'),
    (SELECT id FROM dbo.usuario WHERE email = 'viniciuspadilhavieira@hotmail.com')
),
(
    88.50,
    'Conta de Internet',
    'Provedor de Internet',
    '2024-07-12 10:00:00',
    (SELECT id FROM dbo.categoria WHERE nome = 'Serviços Públicos'),
    (SELECT id FROM dbo.usuario WHERE email = 'viniciuspadilhavieira@hotmail.com')
),
(
    300.00,
    'Aporte mensal na poupança',
    'Investimento',
    '2024-07-28 11:00:00',
    (SELECT id FROM dbo.categoria WHERE nome = 'Poupança'),
    (SELECT id FROM dbo.usuario WHERE email = 'viniciuspadilhavieira@hotmail.com')
),
-- Setembro 2024
(
    32.50,
    'Almoço executivo',
    'Restaurante Kilo',
    '2024-09-02 12:30:00',
    (SELECT id FROM dbo.categoria WHERE nome = 'Alimentação'),
    (SELECT id FROM dbo.usuario WHERE email = 'viniciuspadilhavieira@hotmail.com')
),
(
    1200.00,
    'Aluguel de Setembro',
    'Aluguel',
    '2024-09-05 08:05:00',
    (SELECT id FROM dbo.categoria WHERE nome = 'Moradia'),
    (SELECT id FROM dbo.usuario WHERE email = 'viniciuspadilhavieira@hotmail.com')
),
(
    25.80,
    'Corrida de Uber para o aeroporto',
    'Uber',
    '2024-09-01 06:00:00',
    (SELECT id FROM dbo.categoria WHERE nome = 'Transporte'),
    (SELECT id FROM dbo.usuario WHERE email = 'viniciuspadilhavieira@hotmail.com')
),
(
    180.00,
    'Consulta com dermatologista',
    'Clínica Médica',
    '2024-09-03 15:00:00',
    (SELECT id FROM dbo.categoria WHERE nome = 'Saúde'),
    (SELECT id FROM dbo.usuario WHERE email = 'viniciuspadilhavieira@hotmail.com')
),
(
    75.00,
    'Presente de aniversário para colega',
    'Loja de Presentes',
    '2024-09-04 18:00:00',
    (SELECT id FROM dbo.categoria WHERE nome = 'Outros'),
    (SELECT id FROM dbo.usuario WHERE email = 'viniciuspadilhavieira@hotmail.com')
);