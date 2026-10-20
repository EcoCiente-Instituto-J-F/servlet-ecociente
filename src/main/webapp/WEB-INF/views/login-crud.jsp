<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
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
</head>
<body class="login-crud">

  <main class="login-crud__container">

    <div class="login-crud__marca">
      <img src="${ctx}/assets/logotipo-login.svg" alt="EcoCiente - Dados que despertam consciência">
    </div>

    <div class="login-crud__moldura">
      <section class="login-crud__cartao">

        <h1 class="login-crud__titulo">Acesse sua conta</h1>
        <p class="login-crud__subtitulo">
          Bem-vindo de volta!<br>
          Continue gerenciando seus dados de onde parou.
        </p>

        <form class="login-crud__form" action="${ctx}/login-crud" method="POST">

          <div class="login-crud__grupo">
            <label class="login-crud__rotulo" for="login-email">E-mail ou usuário</label>
            <div class="login-crud__campo">
              <input class="login-crud__input" type="text" id="login-email" name="email"
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
            <a class="login-crud__esqueci" href="#">Esqueceu sua senha?</a>
          </div>

          <c:if test="${not empty erroLogin}">
            <p class="login-crud__erro" role="alert"><c:out value="${erroLogin}" /></p>
          </c:if>

          <button class="login-crud__botao" type="submit">Login</button>
        </form>

      </section>
    </div>

  </main>

  <script src="${ctx}/js/login-crud.js"></script>
</body>
</html>
