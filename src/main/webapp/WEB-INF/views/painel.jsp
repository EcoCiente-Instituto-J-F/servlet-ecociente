<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<c:set var="menuAtivo" value="painel" scope="request"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Painel administrativo | EcoCiente</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700&family=Nunito:wght@700&display=swap" rel="stylesheet">
  <!-- Aplica o tema salvo antes de pintar a página  -->
  <script>document.documentElement.dataset.tema = localStorage.getItem("tema-ecociente") || "claro";</script>
  <link rel="stylesheet" href="${ctx}/css/variaveis.css">
  <link rel="stylesheet" href="${ctx}/css/base.css">
  <link rel="stylesheet" href="${ctx}/css/painel.css">
  <link rel="stylesheet" href="${ctx}/css/interacoes.css">
  <script src="${ctx}/js/interacoes.js" defer></script>
  <script src="${ctx}/js/painel.js" defer></script>
</head>
<body>
<div class="layout">

  <jsp:include page="/WEB-INF/views/partes/cabecalho.jsp"/>
  <jsp:include page="/WEB-INF/views/partes/menu.jsp"/>

  <main class="conteudo">
    <header class="cabecalho-pagina">
      <div>
        <p class="cabecalho-pagina__etiqueta">Visão geral</p>
        <h1 class="cabecalho-pagina__titulo">Painel administrativo</h1>
        <p class="cabecalho-pagina__descricao">Como a plataforma está se comportando esse mês</p>
      </div>
    </header>
    <%-- Cards + gráficos: o painel.js troca o conteúdo desta área a cada 30 segundos --%>
    <div id="area-do-painel" data-url="${ctx}/painel/conteudo">
      <jsp:include page="/WEB-INF/views/painel-conteudo.jsp"/>
    </div>
  </main>
</div>

<jsp:include page="/WEB-INF/views/partes/modais-comuns.jsp"/>

</body>
</html>
