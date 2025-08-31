-- Migration V5: create categoria and gasto tables, and seed essential categories

CREATE TABLE IF NOT EXISTS dbo.categoria (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    cor_identificacao VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS dbo.gasto (
    id SERIAL PRIMARY KEY,
    valor NUMERIC(15,2) NOT NULL,
    descricao VARCHAR(200) NOT NULL,
    nome VARCHAR(50) NOT NULL,
    data TIMESTAMP NOT NULL,
    data_criacao TIMESTAMP DEFAULT now(),
    categoria_id INTEGER NOT NULL REFERENCES dbo.categoria(id) ON DELETE RESTRICT,
    usuario_id INTEGER NOT NULL REFERENCES dbo.usuario(id) ON DELETE RESTRICT
);

-- Insert essential categories commonly used in expense management apps
-- These categories cover essentials and frequent spending buckets
INSERT INTO dbo.categoria (nome, cor_identificacao) VALUES
('Alimentação', '#FF7043'),
('Moradia', '#42A5F5'),
('Transporte', '#66BB6A'),
('Serviços Públicos', '#FFA726'),
('Saúde', '#EF5350'),
('Educação', '#AB47BC'),
('Lazer', '#29B6F6'),
('Poupança', '#8D6E63'),
('Compras', '#7E57C2'),
('Cuidados Pessoais', '#EC407A'),
('Outros', '#90A4AE')
ON CONFLICT DO NOTHING;
