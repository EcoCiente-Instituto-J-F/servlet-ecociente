<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Tela de login do administrador (dá acesso ao painel) --%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Login | EcoCiente</title>
  <link rel="icon" href="${ctx}/assets/favicon.png">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Instrument+Sans:wght@400;500;600;700&family=Montserrat:wght@400;600;700&display=swap" rel="stylesheet">
  <link rel="stylesheet" href="${ctx}/css/variaveis.css">
  <link rel="stylesheet" href="${ctx}/css/login-crud.css">
  <%-- Estilo do botão Voltar (igual ao da tela de cadastro); fica aqui para aparecer mesmo se o css/ estiver em cache --%>
  <style>
    .login-crud__voltar { position: absolute; top: 24px; left: 24px; display: inline-flex; align-items: center; gap: 8px; padding: 8px 16px;
      border-radius: 50px; background-color: #fff; color: #064e3b; font-family: "Montserrat", sans-serif; font-weight: 600; font-size: 13px;
      text-decoration: none; box-shadow: 0 4px 20px rgb(0 0 0 / 15%); transition: background-color .2s ease; }
    .login-crud__voltar:hover { background-color: #dfeade; }
    .login-crud__voltar:focus-visible { outline: 2px solid #e05282; outline-offset: 2px; }
    .login-crud__voltar .seta { font-size: 18px; line-height: 1; }
  </style>
</head>
<body class="login-crud">

<%-- Volta para a página inicial --%>
<a href="${ctx}/inicio" class="login-crud__voltar" aria-label="Voltar para a página inicial">
  <span class="seta" aria-hidden="true">&larr;</span>
  <span>Voltar</span>
</a>

<main class="login-crud__container">

  <%-- Logo, à esquerda --%>
  <div class="login-crud__marca">
    <img src="${ctx}/assets/logotipo-login.svg" alt="EcoCiente - Dados que despertam consciência">
  </div>

  <%-- Cartão de login, à direita --%>
  <div class="login-crud__moldura">
    <section class="login-crud__cartao">

      <h1 class="login-crud__titulo">Acesse sua conta</h1>
      <p class="login-crud__subtitulo">
        Bem-vindo de volta!<br>
        Continue gerenciando seus dados de onde parou.
      </p>

      <%-- Envia e-mail e senha para o servidor; só administradores conseguem entrar --%>
      <form class="login-crud__form" action="${ctx}/login-crud" method="POST">

        <div class="login-crud__grupo">
          <label class="login-crud__rotulo" for="login-email">E-mail</label>
          <div class="login-crud__campo">
            <input class="login-crud__input" type="email" id="login-email" name="email"
                   value="<c:out value='${param.email}' />"
                   placeholder="Digite seu e-mail" autocomplete="username" required>
          </div>
        </div>

        <div class="login-crud__grupo">
          <label class="login-crud__rotulo" for="login-senha">Senha</label>
          <div class="login-crud__campo login-crud__campo--senha">
            <input class="login-crud__input" type="password" id="login-senha" name="senha"
                   placeholder="Digite sua senha" autocomplete="current-password" required>
            <button class="login-crud__olho" type="button" data-mostrar-senha="login-senha"
                    aria-label="Mostrar senha" aria-pressed="false">
              <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                   stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                <circle cx="12" cy="12" r="3"></circle>
                <line class="risco" x1="2" y1="2" x2="22" y2="22"></line>
              </svg>
            </button>
          </div>
        </div>

        <%-- Mensagem de erro do login (dados incorretos, conta sem permissão etc.) --%>
        <c:if test="${not empty erroLogin}">
          <p class="login-crud__erro" role="alert"><c:out value="${erroLogin}" /></p>
        </c:if>

        <button class="login-crud__botao" type="submit">Login</button>
      </form>

    </section>
  </div>

</main>

<%-- Script do olho que mostra ou esconde a senha --%>
<script src="${ctx}/js/login-crud.js"></script>
</body>
</html>