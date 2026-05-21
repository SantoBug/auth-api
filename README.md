# 🔐 Auth API

> REST API de autenticação stateless construída com Spring Boot e Spring Security,
> utilizando JWT para controle de acesso seguro.

---

## 📋 Sobre

Sistemas reais precisam controlar **quem acessa o quê**.
Esta API resolve esse problema com uma implementação completa de autenticação,
desde o registro do usuário até a proteção granular de rotas por perfil de acesso.

Construída do zero com foco em segurança, separação de responsabilidades
e boas práticas de desenvolvimento backend Java.

---

## ⚙️ Tecnologias

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 21 | Linguagem principal |
| Spring Boot | 3.5 | Framework base |
| Spring Security | 6.5 | Camada de segurança |
| JJWT | 0.12.3 | Geração e validação de tokens |
| Spring Data JPA | 3.5 | Persistência de dados |
| Hibernate | 6.6 | ORM |
| MySQL | 8.0 | Banco de dados |
| Docker | - | Ambiente de banco isolado |
| Lombok | 1.18 | Redução de boilerplate |

---

## 🏗️ Arquitetura

```
src/main/java/com/douglas/auth_api/
├── config/
│   ├── JwtAuthFilter.java          # Filtro JWT — intercepta todas as requisições
│   └── SecurityConfig.java         # Configuração de segurança e rotas
├── controller/
│   └── AuthController.java         # Endpoints de autenticação
├── dto/
│   ├── LoginRequest.java           # Dados de entrada do login
│   ├── RegisterRequest.java        # Dados de entrada do registro
│   └── AuthResponse.java           # Resposta com token JWT
├── entity/
│   ├── User.java                   # Entidade de usuário
│   └── Role.java                   # Enum de perfis de acesso
├── repository/
│   └── UserRepository.java         # Acesso ao banco de dados
└── service/
    ├── AuthService.java            # Lógica de registro e login
    ├── JwtService.java             # Geração e validação de tokens
    └── UserDetailsServiceImpl.java # Bridge com Spring Security
```

---

## 🔒 Segurança implementada

- ✅ Senhas armazenadas com **BCrypt** (hash irreversível)
- ✅ Tokens **JWT** assinados com HMAC-SHA256
- ✅ Arquitetura **stateless** — sem sessão no servidor
- ✅ Proteção de rotas por **roles** (USER / ADMIN)
- ✅ Token com **expiração** de 24 horas
- ✅ Separação entre dados públicos e privados via **DTOs**

---

## 🚀 Como rodar localmente

### Pré-requisitos

- [Java 21+](https://adoptium.net)
- [Docker Desktop](https://www.docker.com/products/docker-desktop)

### Passo a passo

```bash
# 1. Clone o repositório
git clone https://github.com/SEU_USUARIO/auth-api.git
cd auth-api

# 2. Suba o banco de dados
docker compose up -d

# 3. Rode a aplicação
./mvnw spring-boot:run
```

A API estará disponível em `http://localhost:8080`

---

## 📡 Endpoints

### Autenticação — público

**Registrar usuário**
```http
POST /api/auth/register
Content-Type: application/json

{
  "name": "Douglas",
  "email": "douglas@email.com",
  "password": "123456"
}
```

**Login**
```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "douglas@email.com",
  "password": "123456"
}
```

**Resposta de ambos:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkb3VnbGFzQGVtYWlsLmNvbSJ9..."
}
```

### Rotas protegidas — requer token

```http
GET /api/users/me
Authorization: Bearer {accessToken}
```

---

## 🔄 Fluxo de autenticação

```
REGISTRO / LOGIN
Cliente ──── { email, senha } ────► API
Cliente ◄─── { accessToken }  ───── API

ROTA PROTEGIDA
Cliente ──── Bearer token ────► Filtro JWT valida ────► Controller responde
Cliente ◄─── { dados }       ────────────────────────────────────────────────
```

---

## 📁 Variáveis de ambiente

| Variável | Descrição | Padrão |
|---|---|---|
| `jwt.secret` | Chave de assinatura do token | definida no application.yml |
| `jwt.expiration` | Tempo de expiração em ms | 86400000 (24h) |
| `spring.datasource.url` | URL do banco | localhost:3307/authdb |

---

## 👨‍💻 Autor

**Douglas**
[![LinkedIn](https://img.shields.io/badge/LinkedIn-blue?style=flat&logo=linkedin)](https://linkedin.com/in/SEU_PERFIL)
[![GitHub](https://img.shields.io/badge/GitHub-black?style=flat&logo=github)](https://github.com/SEU_USUARIO)
