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
        <a href="${ctx}/painel" class="botao-voltar" aria-label="Voltar para o painel">
          <span class="seta" aria-hidden="true">&larr;</span>
          <span>Voltar</span>
        </a>
        <p class="cabecalho-pagina__etiqueta">Tabela: Parceria</p>
        <h1 class="cabecalho-pagina__titulo">Coletas &amp; vínculos</h1>
        <p class="cabecalho-pagina__descricao">Parcerias ativas entre condomínios e cooperativas</p>
      </div>
      <div class="cabecalho-pagina__acoes">
        <a class="botao botao--contorno" href="#" data-acao="editar" data-abrir-modal="modal-vinculo" data-modo="editar" aria-disabled="true"><img src="${ctx}/assets/icones/editar.svg" alt="">Editar</a>
        <a class="botao botao--contorno" href="#" data-requer-selecao aria-disabled="true" data-abrir-modal="modal-excluir" data-excluir-url="${ctx}/vinculos" data-excluir-acao="excluir">Excluir</a>
        <a class="botao botao--primario" href="#" data-abrir-modal="modal-vinculo"><img src="${ctx}/assets/icones/mais-branco.svg" alt="">Novo vínculo</a>
      </div>
    </header>
    <jsp:include page="/WEB-INF/views/partes/avisos.jsp"><jsp:param name="reabrirModal" value="modal-vinculo"/></jsp:include>
    <section class="indicadores indicadores--tres" aria-label="Resumo">
        <article class="cartao indicador indicador--simples"><h2 class="indicador__titulo">Vínculos ativos</h2><p class="indicador__valor">${vinculosAtivos}</p></article>
        <article class="cartao indicador indicador--simples"><h2 class="indicador__titulo">Sem cooperativa vinculada</h2><p class="indicador__valor">${semCooperativa} condomínios</p></article>
    </section>
    <div style="margin-top:39px"></div>
    <section class="tabela-cartao">
      <c:if test="${empty vinculos and not empty param.q}"><p class="aviso aviso--vazio" role="status">Nenhum resultado encontrado para “<c:out value="${param.q}"/>”.</p></c:if>
      <table class="tabela tabela--alta"${ordenacaoServidor ? ' data-ordem-servidor' : ''}>
        <colgroup><col style="width:30%"><col style="width:26%"><col style="width:23%"><col style="width:16%"><col style="width:5%"></colgroup>
        <thead><tr><th scope="col"><button class="tabela__ordem" type="button" data-ordem="condominio">Condomínio</button></th><th scope="col"><button class="tabela__ordem" type="button" data-ordem="cooperativa">Cooperativa</button></th><th scope="col"><button class="tabela__ordem" type="button" data-ordem="inicioIso">Início</button></th><th scope="col"><button class="tabela__ordem" type="button" data-ordem="fimIso">Fim</button></th><th scope="col"><span class="somente-leitor">Ações</span></th></tr></thead>
        <tbody>
          <c:forEach var="v" items="${vinculos}">
            <tr data-id="<c:out value="${v.idParceria}"/>" data-nome="<c:out value="${v.condominio}"/>" data-condominio="<c:out value="${v.condominio}"/>" data-cooperativa="<c:out value="${v.cooperativa}"/>" data-form-inicio="<c:out value="${v.inicioIso}"/>" data-form-fim="<c:out value="${v.fimIso}"/>" data-id-condominio="<c:out value="${v.idCondominio}"/>" data-id-cooperativa="<c:out value="${v.idCooperativa}"/>">
              <td><div class="celula-principal"><input class="tabela__selecionar" type="radio" name="linha" value="${v.idParceria}" aria-label="Selecionar linha"><span><c:out value="${v.condominio}"/></span></div></td>
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
    <input type="hidden" name="id" data-chave-id> <%-- vazio = cadastrar; preenchido = editar --%>
    <header class="modal__cabecalho">
      <p class="modal__etiqueta" data-texto-editar="Editar parceria">Nova parceria</p>
      <h2 id="modal-vinculo-titulo" class="modal__titulo" data-texto-editar="Editar vínculo">Novo vínculo</h2>
      <button class="modal__fechar" type="button" data-fechar-modal aria-label="Fechar"><img class="" src="${ctx}/assets/icones/fechar.svg" alt=""></button>
    </header>
    <div class="modal__linha"></div>
    <div class="modal__corpo">
      <div class="formulario">
        <label class="campo">Condomínio<select name="idCondominio" required data-por-rotulo="condominio"><option value="">Selecione</option><c:forEach var="o" items="${condominios}"><option value="${o.idCondominio}"><c:out value="${o.nome}"/></option></c:forEach></select></label>
        <label class="campo">Cooperativa<select name="idCooperativa" required data-por-rotulo="cooperativa"><option value="">Selecione</option><c:forEach var="o" items="${cooperativas}"><option value="${o.idCooperativa}"><c:out value="${o.nomeCooperativa}"/></option></c:forEach></select></label>
        <label class="campo">Data de início<input type="date" name="inicio" required></label>
        <label class="campo">Data de fim (opcional)<input type="date" name="fim"></label>
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
