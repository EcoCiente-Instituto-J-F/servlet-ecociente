# Contribuindo com o servlet-ecociente

Ao participar, você concorda em seguir o [Código de Conduta](CODE_OF_CONDUCT.md). Para reportar um problema de segurança, **não** abra uma issue — siga o [SECURITY.md](SECURITY.md).

## Idioma

Documentação, issues, Pull Requests e mensagens ao usuário em português do Brasil.

## Configurando o ambiente

1. Instale o JDK 25.
2. Clone o repositório:
   ```bash
   git clone https://github.com/EcoCiente-Instituto-J-F/servlet-ecociente.git\n   cd servlet-ecociente\n   ```\n3. Copie `.env.example` para `src/main/resources/.env` (ignorado pelo Git) e preencha com um banco de **desenvolvimento**.\n4. Gere o WAR com `./mvnw package` (Windows: `.\\mvnw.cmd package`) e publique-o em um contêiner compatível com Jakarta Servlet 6.1.

## Testando sua mudança

Não há CI neste repositório. Rode antes da PR:

```bash
./mvnw test
./mvnw package
```

Ainda não há testes automatizados (o JUnit 5 está no `pom.xml`); ao adicionar código com lógica, inclua testes em `src/test/java`.

## Regras do projeto

- Nenhum segredo (chaves, tokens, senhas) no repositório.
- Nenhum dado pessoal real em testes, fixtures ou exemplos.
- Nenhuma dependência nova sem discussão prévia.
- Nenhum segredo no repositório: o `.env` fica em `src/main/resources/` e é ignorado pelo Git.
- Padrão em camadas: `controller` (Servlets), `dao` (JDBC), `model`, `filter`, `util`; páginas JSP em `WEB-INF/views`.
- Todo SQL usa `PreparedStatement` com parâmetros; nunca concatene entrada do usuário.
- Senhas somente com `Senha.gerarHash` (BCrypt); nunca grave nem logue senha em texto puro.
- Novas rotas do painel administrativo devem ser incluídas nos `urlPatterns` do `AutenticacaoFilter`.
- Preencha o `.github/pull_request_template.md` ao abrir a PR.

## Pull requests

1. Crie uma branch a partir da `main` (ex.: `feat/nome-da-mudanca`).
2. Faça commits focados com prefixos `feat:`, `fix:`, `chore:`, `docs:`, `test:` ou `refactor:`.
3. Abra a PR para a `main`. Descreva o que mudou e por quê.
4. O CI precisa passar (quando houver) e um mantenedor revisa antes do merge.

## Reportando bugs e propondo funcionalidades

Abra uma issue com o comportamento esperado, o observado, os passos para reproduzir e a versão. Use dados fictícios. Para vulnerabilidades, siga o [SECURITY.md](SECURITY.md).
