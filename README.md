<p align="center">
  <img src="assets/card-readme.png" alt="Card do repositório" width="100%">
</p>

# servlet-ecociente

![Java](https://img.shields.io/badge/Java-25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Jakarta EE](https://img.shields.io/badge/Jakarta_Servlet-6.1-F89820?style=for-the-badge&logo=jakartaee&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-war-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-JDBC-4169E1?style=for-the-badge&logo=postgresql&logoColor=white)
![GitHub repo size](https://img.shields.io/github/repo-size/EcoCiente-Instituto-J-F/servlet-ecociente?style=for-the-badge)
![GitHub last commit](https://img.shields.io/github/last-commit/EcoCiente-Instituto-J-F/servlet-ecociente?style=for-the-badge)
![License](https://img.shields.io/badge/license-MIT-green?style=for-the-badge)

> Aplicação web em Servlets e JSP do EcoCiente: cadastro de usuários e painel administrativo com CRUDs.

## Sobre o projeto

Aplicação Java (empacotada como WAR) com o padrão `controller` → `dao` → `model` e páginas JSP em `WEB-INF/views`. Usa PostgreSQL por JDBC, com pool HikariCP, e senhas com hash BCrypt (jBCrypt).

O que existe:

- **Cadastro** em etapas (tipo de perfil, dados pessoais, endereço, conta, condomínio/moradia e planos) para moradores, síndicos e cooperativas.
- **Login** (`LoginServlet`) e **painel administrativo** (`/painel`) com cards e gráficos, protegido por filtro.
- **CRUDs** de usuários, condomínios, cooperativas, moradores, síndicos, endereços, telefones e vínculos.

O `AutenticacaoFilter` bloqueia as rotas do painel e dos CRUDs: exige login e que o usuário seja administrador.

```mermaid
flowchart LR
    B["Navegador"] --> F["AutenticacaoFilter<br/>(rotas do painel)"]
    F --> S["Servlets<br/>controller/"]
    S --> D["DAOs<br/>dao/"]
    D --> C["ConexaoBD<br/>(HikariCP)"]
    C --> P[("PostgreSQL")]
    S --> J["JSP<br/>WEB-INF/views"]
```

## Pré-requisitos

- JDK 25 (definido no `pom.xml`)
- Um contêiner de servlets compatível com Jakarta Servlet 6.1 para executar o WAR
- PostgreSQL acessível

## Instalando

```bash
git clone https://github.com/EcoCiente-Instituto-J-F/servlet-ecociente.git
cd servlet-ecociente
cp .env.example src/main/resources/.env   # Windows (PowerShell): Copy-Item .env.example src\main\resources\.env
```

A aplicação lê o `.env` do **classpath** (`src/main/resources/.env`, ignorado pelo Git). Variáveis:

| Variável | Descrição |
| --- | --- |
| `DB_URL` | URL JDBC, por exemplo `jdbc:postgresql://HOST:PORTA/BANCO?sslmode=require` |
| `DB_USER` | Usuário do banco |
| `DB_PASSWORD` | Senha do banco |

## Usando

```bash
./mvnw package        # Windows: .\mvnw.cmd package
```

O comando gera o WAR em `target/`; publique-o no contêiner de servlets. A classe `Main` apenas testa a conexão com o banco.

## Testes

O `pom.xml` já inclui o JUnit 5, mas ainda não há testes em `src/test`. Para compilar e executar os testes existentes:

```bash
./mvnw test
```

## Estrutura do projeto

```
src/main/
├── java/com/example/ecociente/
│   ├── controller/   # Servlets (cadastro, login, painel e CRUDs)
│   ├── dao/          # acesso ao banco (JDBC)
│   ├── model/        # entidades
│   ├── filter/       # AutenticacaoFilter
│   ├── conexao/      # ConexaoBD (HikariCP)
│   └── util/         # Senha (BCrypt), Formulario, DadoInvalido
└── webapp/
    ├── WEB-INF/views/  # páginas JSP
    └── assets/         # imagens
```

## Contribuindo

Leia o [CONTRIBUTING.md](CONTRIBUTING.md) antes de abrir uma Pull Request. Ao participar, você concorda com o [Código de Conduta](CODE_OF_CONDUCT.md).

## Segurança

Encontrou uma vulnerabilidade? Siga o [SECURITY.md](SECURITY.md). **Não abra issue pública.**

## Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE).
