INSERT INTO dbo.usuario (nome, email, senha, roles)
VALUES (
    'admin',
    'admin@admin.com',
    '$2a$12$Hk/QgNlII1W.qzBCKv8ixOe4Shvp5FqikAu8.oS2HGobAvG4S4oye',
    ARRAY['ADMIN']
)
ON CONFLICT (email) DO NOTHING;
