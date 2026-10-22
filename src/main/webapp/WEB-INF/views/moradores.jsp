<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<c:set var="menuAtivo" value="moradores" scope="request"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Moradores | EcoCiente</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700&family=Nunito:wght@700&display=swap" rel="stylesheet">
  <!-- Aplica o tema salvo antes de pintar a página -->
  <script>document.documentElement.dataset.tema = localStorage.getItem("tema-ecociente") || "claro";</script>
  <link rel="stylesheet" href="${ctx}/css/variaveis.css">
  <link rel="stylesheet" href="${ctx}/css/base.css">
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
        <p class="cabecalho-pagina__etiqueta">Tabela: Morador</p>
        <h1 class="cabecalho-pagina__titulo">Moradores</h1>
        <p class="cabecalho-pagina__descricao">Moradores, apartamentos e nível de engajamento</p>
      </div>
      <div class="cabecalho-pagina__acoes">
        <a class="botao botao--contorno" href="#" data-acao="editar" data-abrir-modal="modal-morador" data-modo="editar" aria-disabled="true"><img src="${ctx}/assets/icones/editar.svg" alt="">Editar</a>
        <a class="botao botao--contorno" href="#" data-requer-selecao aria-disabled="true" data-abrir-modal="modal-excluir" data-excluir-url="${ctx}/deletarMorador" data-excluir-campo="idMorador">Excluir</a>
        <a class="botao botao--destaque" href="#" data-requer-selecao aria-disabled="true" data-abrir-modal="ficha-morador"><img src="${ctx}/assets/icones/mais-branco.svg" alt="">Detalhes</a>
        <a class="botao botao--primario" href="#" data-abrir-modal="modal-morador"><img src="${ctx}/assets/icones/mais-branco.svg" alt="">Cadastrar</a>
      </div>
    </header>
    <jsp:include page="/WEB-INF/views/partes/avisos.jsp"><jsp:param name="reabrirModal" value="modal-morador"/></jsp:include>
    <section class="tabela-cartao">
      <table class="tabela">
        <colgroup><col style="width:30%"><col style="width:20%"><col style="width:25%"><col style="width:20%"><col style="width:5%"></colgroup>
        <thead><tr><th scope="col">Nome do morador</th><th scope="col">Apartamento</th><th scope="col">Torre</th><th scope="col">Engajamento</th><th scope="col"><span class="tabela__ordenar"><img src="${ctx}/assets/icones/ordenar.svg" alt="Ordenar"></span></th></tr></thead>
        <tbody>
          <c:forEach var="m" items="${moradores}">
            <c:set var="nomeTorre" value="---"/>
            <c:forEach var="t" items="${torres}"><c:if test="${t.idTorre == m.idTorre}"><c:set var="nomeTorre" value="${t.nome}"/></c:if></c:forEach>
            <tr data-id="<c:out value="${m.idMorador}"/>" data-nome="<c:out value="${m.nome}"/>" data-apartamento="<c:out value="${m.numeroApartamento}"/>" data-numero-apartamento="<c:out value="${m.numeroApartamento}"/>" data-torre="<c:out value="${nomeTorre}"/>" data-id-torre="<c:out value="${m.idTorre}"/>" data-id-usuario="<c:out value="${m.idUsuario}"/>" data-nivel-engajamento="<c:out value="${m.nivelEngajamento}"/>" data-engajamento="<c:out value="${m.nivelEngajamento}"/>" data-tag="<c:out value="${'Morador'}"/>">
              <td><div class="celula-principal"><input class="tabela__selecionar" type="radio" name="linha" value="${m.idMorador}" aria-label="Selecionar linha"><span><c:out value="${m.nome}"/></span></div></td>
              <td><c:out value="${m.numeroApartamento}"/></td>
              <td><c:out value="${nomeTorre}"/></td>
              <td><c:out value="${m.nivelEngajamento}"/></td>
              <td><button class="acoes-linha" type="button" aria-label="Mais ações"></button></td>
            </tr>
          </c:forEach>
          <c:forEach begin="${fn:length(moradores)}" end="7">
            <tr aria-hidden="true"><td><div class="celula-principal"><span class="tabela__selecionar"></span></div></td><td></td><td></td><td></td><td></td></tr>
          </c:forEach>
        </tbody>
      </table>
    </section>
  </main>
</div>

<jsp:include page="/WEB-INF/views/partes/modais-comuns.jsp"/>

<dialog class="modal" id="modal-morador" style="--largura:640px" aria-labelledby="modal-morador-titulo">
  <form action="${ctx}/adicionarMorador" data-acao-novo="${ctx}/adicionarMorador" data-acao-editar="${ctx}/alterarMorador" method="post">
    <input type="hidden" name="idMorador" data-chave-id> <%-- vazio = cadastrar; preenchido = editar --%>
    <header class="modal__cabecalho">
      <p class="modal__etiqueta" data-texto-editar="Editar registro">Novo registro</p>
      <h2 id="modal-morador-titulo" class="modal__titulo" data-texto-editar="Editar morador">Cadastrar morador</h2>
      <button class="modal__fechar" type="button" data-fechar-modal aria-label="Fechar"><img class="" src="${ctx}/assets/icones/fechar.svg" alt=""></button>
    </header>
    <div class="modal__linha"></div>
    <div class="modal__corpo">
      <div class="formulario">
        <label class="campo">Usuário<select name="idUsuario" required><option value="">Selecione</option><c:forEach var="u" items="${usuarios}"><option value="${u.id}"><c:out value="${u.nome}"/></option></c:forEach></select></label>
        <label class="campo">Número do apartamento<input type="text" name="numeroApartamento" placeholder="101" required maxlength="10"></label>
        <label class="campo">Torre<select name="idTorre"><option value="">Sem torre</option><c:forEach var="t" items="${torres}"><option value="${t.idTorre}"><c:out value="${t.nome}"/></option></c:forEach></select></label>
        <label class="campo">Nível de engajamento<select name="nivelEngajamento" required><option value="Baixo">Baixo</option><option value="Médio">Médio</option><option value="Alto">Alto</option></select></label>
      </div>
    </div>
    <div class="modal__linha"></div>
    <footer class="modal__rodape">
      <button class="botao botao--contorno" type="button" data-fechar-modal>Cancelar</button>
      <button class="botao botao--primario" type="submit">Salvar morador</button>
    </footer>
  </form>
</dialog>

<dialog class="modal" id="ficha-morador" data-ficha style="--largura:420px" aria-labelledby="ficha-morador-titulo">
    <header class="modal__cabecalho">
      <p class="modal__etiqueta">Ficha do morador</p>
      <h2 id="ficha-morador-titulo" class="modal__titulo">Detalhes</h2>
      <button class="modal__fechar" type="button" data-fechar-modal aria-label="Fechar"><img class="" src="${ctx}/assets/icones/fechar.svg" alt=""></button>
    </header>
    <div class="modal__linha"></div>
    <div class="modal__corpo">
      <div class="ficha__perfil">
        <span class="ficha__foto" aria-hidden="true" data-campo="iniciais">—</span>
        <div><p class="ficha__nome" data-campo="nome">—</p><span class="ficha__tag" data-campo="tag">—</span></div>
      </div>
      <ul>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/endereco.svg" alt=""></span><div><p class="ficha__rotulo">Apartamento</p><p class="ficha__valor" data-campo="apartamento">—</p></div></li>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/condominios.svg" alt=""></span><div><p class="ficha__rotulo">Torre</p><p class="ficha__valor" data-campo="torre">—</p></div></li>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/folha.svg" alt=""></span><div><p class="ficha__rotulo">Engajamento</p><p class="ficha__valor" data-campo="engajamento">—</p></div></li>
      </ul>
    </div>
    <footer class="modal__rodape">
      <button class="botao botao--contorno" type="button" data-fechar-modal>Fechar</button>
      <button class="botao botao--primario" type="button" data-abrir-modal="modal-morador" data-modo="editar"><img class="icone-branco" src="${ctx}/assets/icones/editar.svg" alt="">Editar</button>
    </footer>
</dialog>

</body>
</html>
