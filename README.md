# MinhaCarteira - Back-End Write

## Visão Geral

Este projeto é o **Back-End de Write** do sistema distribuído "MinhaCarteira", desenvolvido para a disciplina de Sistemas Distribuídos. O sistema é composto por três partes:

- **Back-End Write** (este repositório): responsável por operações de escrita (criação, atualização e deleção) de dados financeiros e de usuários.
- **Back-End Read**: responsável por consultas e leitura dos dados.
- **Front-End**: interface web para interação dos usuários.

O Back-End Write implementa uma arquitetura orientada a serviços, com autenticação via Keycloak, persistência em PostgreSQL, e integração via APIs REST.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.5.5**
- **Spring Security & OAuth2**
- **Keycloak** (autenticação e autorização)
- **PostgreSQL**
- **Flyway** (migração de banco de dados)
- **Docker & Docker Compose**
- **Swagger/OpenAPI** (documentação de API)

## Arquitetura

O sistema segue o padrão CQRS (Command Query Responsibility Segregation):

- **Write Service**: recebe comandos para criar, atualizar ou deletar entidades (usuário, gasto, categoria, refresh token).
- **Read Service**: consulta dados, sem alterar o estado.

O Write Service expõe endpoints REST protegidos por autenticação JWT (Keycloak), e realiza operações transacionais no banco PostgreSQL.

## Estrutura de Pastas

- `src/main/java/com/minhaCarteira/write/` - Código principal
  - `api/` - Controllers REST
  - `application/` - Serviços de negócio
  - `domain/` - Entidades, DTOs e Repositórios
  - `infra/` - Infraestrutura (segurança, exceções, swagger)
- `src/main/resources/` - Configurações, migrações e propriedades
- `docker-compose-bd.yaml` - Subida do banco PostgreSQL e PgAdmin
- `Dockerfile` - Subida do Keycloak

## Banco de Dados

Migrações automáticas via Flyway:

- Tabelas: `usuario`, `gasto`, `categoria`, `refresh_token`
- Dados de teste para três usuários (Anthony, Alan, Vinicius) e gastos simulados
- Usuário admin padrão

Scripts de migração em `src/main/resources/db/migration/`

## Autenticação e Autorização

- **Keycloak** gerencia usuários, papéis e tokens JWT
- Endpoints protegidos exigem Bearer Token
- Roles: `ADMIN`, `USER`, `VISITANTE`

## Principais Endpoints

### Usuário

- `POST /api/usuario/create` - Criação de usuário
- `DELETE /api/usuario/{id}` - Deleção (apenas ADMIN)
- `PUT /api/usuario/update` - Atualização de dados

### Gasto

- `POST /api/gasto/create` - Criação de gasto
- `DELETE /api/gasto/{id}` - Deleção de gasto
- `PUT /api/gasto/update` - Atualização de gasto

### Autenticação

- `POST /api/auth/authenticate` - Login (retorna access token e refresh token)
- `POST /api/auth/refresh` - Renovação de token

### Swagger

- `GET /swagger-ui/index.html` - Documentação interativa da API

## Como Executar

1. **Suba o banco de dados e PgAdmin**

   ```sh
   docker-compose -f docker-compose-bd.yaml up -d
   ```

   - PostgreSQL: porta 5433
   - PgAdmin: porta 5050

2. **Suba o Keycloak**

   ```sh
   docker build -t keycloak-server .
   docker run -p 8080:8080 keycloak-server
   ```

   - Admin: `admin/admin123`

3. **Configure o Keycloak**

   - Realm: `app_spring_sd`
   - Client: `app_write` (conf. grant types, secret conforme `application-dev.properties`)
   - Usuários: admin, anthony, alan, vinicius

4. **Execute a aplicação Spring Boot**
   ```sh
   ./mvnw spring-boot:run
   ```
   - Porta padrão: 8082

## Testes

Testes unitários em `src/test/java/com/minhaCarteira/crud/CrudApplicationTests.java`

## Integração no Sistema Distribuído

- O Back-End Write recebe comandos do Front-End e do Back-End Read para modificar dados.
- O Back-End Read consulta dados do banco e pode ser atualizado via eventos ou polling.
- O Front-End consome ambos os serviços via REST.

## Exemplos de Uso

### Criar Usuário

```http
POST /api/usuario/create
{
	"nome": "João",
	"email": "joao@email.com",
	"senha": "123456"
}
```

### Autenticar Usuário

```http
POST /api/auth/authenticate
{
	"email": "joao@email.com",
	"senha": "123456"
}
```

### Criar Gasto

```http
POST /api/gasto/create
{
	"valor": 100.0,
	"descricao": "Supermercado",
	"nome": "Compras",
	"data": "2025-09-15T10:00:00",
	"categoriaId": 1,
	"usuarioId": 1
}
```

## Observações

- Todas as operações de escrita exigem autenticação JWT.
- O sistema foi projetado para escalabilidade e separação clara entre leitura e escrita.
- Para dúvidas sobre endpoints, acesse o Swagger: `/swagger-ui/index.html`

---

Projeto desenvolvido para a disciplina de **Sistemas Distribuídos**.
