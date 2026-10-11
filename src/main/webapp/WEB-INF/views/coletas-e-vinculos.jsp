<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<c:set var="menuAtivo" value="vinculos" scope="request"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Coletas e vínculos | EcoCiente</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700&family=Nunito:wght@700&display=swap" rel="stylesheet">
  <!-- Aplica o tema salvo antes de pintar a página  -->
  <script>document.documentElement.dataset.tema = localStorage.getItem("tema-ecociente") || "claro";</script>
  <link rel="stylesheet" href="${ctx}/css/variaveis.css">
  <link rel="stylesheet" href="${ctx}/css/base.css">
  <link rel="stylesheet" href="${ctx}/css/painel.css">
  <link rel="stylesheet" href="${ctx}/css/tabela.css">
  <link rel="stylesheet" href="${ctx}/css/interacoes.css">
  <script src="${ctx}/js/interacoes.js" defer></script>
</head>
<body>
<div class="layout">

  <jsp:include page="/WEB-INF/views/partes/cabecalho.jsp"/>
  <jsp:include page="/WEB-INF/views/partes/menu.jsp"/>

  <main class="conteudo">
    <header class="cabecalho-pagina">
      <div>
        <p class="cabecalho-pagina__etiqueta">Tabela: Parceria</p>
        <h1 class="cabecalho-pagina__titulo">Coletas &amp; vínculos</h1>
        <p class="cabecalho-pagina__descricao">Parcerias ativas entre condomínios e cooperativas</p>
      </div>
      <div class="cabecalho-pagina__acoes"><a class="botao botao--primario" href="#" data-abrir-modal="modal-vinculo"><img src="${ctx}/assets/icones/mais-branco.svg" alt="">Novo vínculo</a></div>
    </header>
    <section class="indicadores indicadores--tres" aria-label="Resumo">
        <article class="cartao indicador indicador--simples"><h2 class="indicador__titulo">Vínculos ativos</h2><p class="indicador__valor">${vinculosAtivos}</p></article>
        <article class="cartao indicador indicador--simples"><h2 class="indicador__titulo">Coletas este mês</h2><p class="indicador__valor">${coletasMes}</p></article>
        <article class="cartao indicador indicador--simples"><h2 class="indicador__titulo">Sem cooperativa vinculada</h2><p class="indicador__valor">${semCooperativa} condomínios</p></article>
    </section>
    <div style="margin-top:39px"></div>
    <section class="tabela-cartao">
      <table class="tabela tabela--alta">
        <colgroup><col style="width:30%"><col style="width:26%"><col style="width:23%"><col style="width:16%"><col style="width:5%"></colgroup>
        <thead><tr><th scope="col">Condomínio</th><th scope="col">Cooperativa</th><th scope="col">Início</th><th scope="col">Fim</th><th scope="col"><span class="tabela__ordenar"><img src="${ctx}/assets/icones/ordenar.svg" alt="Ordenar"></span></th></tr></thead>
        <tbody>
          <c:forEach var="v" items="${vinculos}">
            <tr>
              <td><div class="celula-principal"><input class="tabela__selecionar" type="radio" name="linha" value="${v.id}" aria-label="Selecionar linha"><span><c:out value="${v.condominio}"/></span></div></td>
              <td><c:out value="${v.cooperativa}"/></td>
              <td><c:out value="${v.inicio}"/></td>
              <td>${empty v.fim ? '---' : v.fim}</td>
              <td><button class="acoes-linha" type="button" aria-label="Mais ações"></button></td>
            </tr>
          </c:forEach>
          <c:forEach begin="${fn:length(vinculos)}" end="7">
            <tr aria-hidden="true"><td><div class="celula-principal"><span class="tabela__selecionar"></span></div></td><td></td><td></td><td></td><td></td></tr>
          </c:forEach>
        </tbody>
      </table>
      <jsp:include page="/WEB-INF/views/partes/paginacao.jsp"/>
    </section>
  </main>
</div>

<jsp:include page="/WEB-INF/views/partes/modais-comuns.jsp"/>


<dialog class="modal" id="modal-vinculo" style="--largura:649px" aria-labelledby="modal-vinculo-titulo">
  <form action="${ctx}/vinculos" method="post">
    <input type="hidden" name="id"> <%-- vazio = cadastrar; preenchido = editar --%>
    <header class="modal__cabecalho">
      <p class="modal__etiqueta">Nova parceria</p>
      <h2 id="modal-vinculo-titulo" class="modal__titulo">Novo vínculo</h2>
      <button class="modal__fechar" type="button" data-fechar-modal aria-label="Fechar"><img class="" src="${ctx}/assets/icones/fechar.svg" alt=""></button>
    </header>
    <div class="modal__linha"></div>
    <div class="modal__corpo">
      <div class="formulario">
        <label class="campo">Condomínio<input type="text" name="condominio" placeholder="Jadins do Vale"></label>
        <label class="campo">Cooperativa<input type="text" name="cooperativa" placeholder="Verde Viva"></label>
        <label class="campo">Data de início<input type="date" name="inicio" placeholder=""></label>
        <label class="campo">Data de Fim<input type="date" name="fim" placeholder=""></label>
      </div>
    </div>
    <div class="modal__linha"></div>
    <footer class="modal__rodape">
      <button class="botao botao--contorno" type="button" data-fechar-modal>Cancelar</button>
      <button class="botao botao--primario" type="submit">Salvar vínculo</button>
    </footer>
  </form>
</dialog>


</body>
</html>
