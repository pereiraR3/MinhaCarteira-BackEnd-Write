CREATE TABLE IF NOT EXISTS dbo.refresh_token
(
   id SERIAL PRIMARY KEY,
   email TEXT NOT NULL,
   refresh_token TEXT NOT NULL UNIQUE,
   expires_at TIMESTAMP NOT NULL
);
