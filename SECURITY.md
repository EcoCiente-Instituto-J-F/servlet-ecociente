# Política de Segurança

O servlet-ecociente é uma aplicação web (Servlets e JSP) que recebe cadastros e mantém o painel administrativo do EcoCiente: trata nome, e-mail, telefone, endereço, data de nascimento e senhas (armazenadas com hash BCrypt) em PostgreSQL.

## Versões suportadas

| Versão | Suporte |
| --- | --- |
| `main` (mais recente) | Sim |
| Demais branches | Não |

## Reportando uma vulnerabilidade

**Não abra issue, Pull Request ou discussão pública** para relatar vulnerabilidades.

Use o relato privado do GitHub: <https://github.com/EcoCiente-Instituto-J-F/servlet-ecociente/security/advisories/new> (Security → Advisories → Report a vulnerability).

> Mantenedores: ative *Private vulnerability reporting* em Settings → Code security.

Inclua no relato:

- o impacto da falha;
- os passos para reproduzir, usando **dados fictícios**;
- o commit testado e a configuração;
- se possível, uma ideia de correção.

Depois do relato: confirmaremos o recebimento, corrigiremos de forma privada e publicaremos um advisory com crédito a quem reportou, se desejado. Pedimos um prazo razoável para a correção antes de qualquer divulgação pública.

## Escopo

**Dentro do escopo**

- Burla de autenticação ou acesso ao painel e aos CRUDs sem ser administrador (`AutenticacaoFilter`)
- Acesso a dados de outro usuário
- Injeção SQL, XSS nas páginas JSP e CSRF nos formulários
- Fixação ou roubo de sessão
- Vazamento de dados pessoais ou de `DB_PASSWORD` em logs, respostas ou repositório
- Segredos commitados

**Fora do escopo**

- Bugs sem impacto de segurança
- Falhas em serviços de terceiros (provedor do PostgreSQL, hospedagem, GitHub)
- Negação de serviço no próprio deploy

## Como o sistema protege os dados

| Camada | Onde | O que faz |
| --- | --- | --- |
| Autorização | `filter/AutenticacaoFilter.java` | Exige login e perfil de administrador nas rotas do painel e dos CRUDs (redireciona ao login ou responde 403) |
| Senhas | `util/Senha.java` | Hash BCrypt com salt aleatório |
| SQL | `dao/` | Consultas com `PreparedStatement` e parâmetros |
| Sessão | `controller/LoginServlet.java` | Invalida a sessão anterior ao autenticar |
| Segredos | `conexao/ConexaoBD.java`, `.gitignore` | Credenciais do banco lidas de `.env` fora do Git |

Observações: não encontrei proteção contra CSRF nem limite de tentativas de login no código, e o `web.xml` não define configurações de cookie de sessão.

## Checklist de deploy

- `DB_PASSWORD` e demais segredos fora do repositório e da imagem; usuário de banco com o mínimo de permissões e `sslmode=require`.
- Servir somente por HTTPS e configurar cookies de sessão como `Secure` e `HttpOnly`.
- Aplicar limite de tentativas no login e proteção contra CSRF nos formulários que alteram dados.
- Não deixar o painel administrativo exposto sem autenticação; manter as rotas novas no `AutenticacaoFilter`.
