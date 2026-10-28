<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%-- Tela de telefones do painel: lista, ficha de detalhes e formulário de cadastro e edição --%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<%-- Diz ao menu qual ícone deve ficar destacado --%>
<c:set var="menuAtivo" value="telefones" scope="request"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Telefones | EcoCiente</title>
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

  <%-- Cabeçalho e menu lateral, iguais em todas as telas do painel --%>
  <jsp:include page="/WEB-INF/views/partes/cabecalho.jsp"/>
  <jsp:include page="/WEB-INF/views/partes/menu.jsp"/>

  <main class="conteudo">
    <%-- Título da página e botões: Editar, Excluir, Detalhes e Cadastrar (os três primeiros só funcionam com uma linha marcada) --%>
    <header class="cabecalho-pagina">
      <div>
        <a href="${ctx}/painel" class="botao-voltar" aria-label="Voltar para o painel">
          <span class="seta" aria-hidden="true">&larr;</span>
          <span>Voltar</span>
        </a>
        <p class="cabecalho-pagina__etiqueta">Tabela: Telefone</p>
        <h1 class="cabecalho-pagina__titulo">Telefones</h1>
        <p class="cabecalho-pagina__descricao">Telefones de contato vinculados aos usuários</p>
      </div>
      <div class="cabecalho-pagina__acoes">
        <a class="botao botao--contorno" href="#" data-acao="editar" data-abrir-modal="modal-telefone" data-modo="editar" aria-disabled="true"><img src="${ctx}/assets/icones/editar.svg" alt="">Editar</a>
        <a class="botao botao--contorno" href="#" data-requer-selecao aria-disabled="true" data-abrir-modal="modal-excluir" data-excluir-url="${ctx}/deletarTelefone" data-excluir-campo="idTelefone">Excluir</a>
        <a class="botao botao--destaque" href="#" data-requer-selecao aria-disabled="true" data-abrir-modal="ficha-telefone"><img src="${ctx}/assets/icones/mais-branco.svg" alt="">Detalhes</a>
        <a class="botao botao--primario" href="#" data-abrir-modal="modal-telefone"><img src="${ctx}/assets/icones/mais-branco.svg" alt="">Cadastrar</a>
      </div>
    </header>
    <%-- Avisos de sucesso ou erro depois de cadastrar, alterar ou excluir --%>
    <jsp:include page="/WEB-INF/views/partes/avisos.jsp"><jsp:param name="reabrirModal" value="modal-telefone"/></jsp:include>
    <%-- Tabela de telefones --%>
    <section class="tabela-cartao">
      <%-- Cada linha guarda seus dados em atributos data-*; o interacoes.js usa isso para preencher a ficha e o formulário de edição --%>
      <table class="tabela">
        <colgroup><col style="width:47%"><col style="width:48%"><col style="width:5%"></colgroup>
        <thead><tr><th scope="col"><button class="tabela__ordem" type="button" data-ordem="c1">Número</button></th><th scope="col"><button class="tabela__ordem" type="button" data-ordem="c2">Usuário</button></th><th scope="col"><span class="somente-leitor">Ações</span></th></tr></thead>
        <tbody>
        <%-- Uma linha para cada item enviado pelo servidor --%>
        <c:forEach var="t" items="${telefones}">
          <%-- Formata o número: celular (11 dígitos), fixo (10 dígitos) ou como está --%>
          <c:choose>
            <c:when test="${fn:length(t.numero) == 11}"><c:set var="numeroBr" value="(${fn:substring(t.numero, 0, 2)}) ${fn:substring(t.numero, 2, 7)}-${fn:substring(t.numero, 7, 11)}"/></c:when>
            <c:when test="${fn:length(t.numero) == 10}"><c:set var="numeroBr" value="(${fn:substring(t.numero, 0, 2)}) ${fn:substring(t.numero, 2, 6)}-${fn:substring(t.numero, 6, 10)}"/></c:when>
            <c:otherwise><c:set var="numeroBr" value="${t.numero}"/></c:otherwise>
          </c:choose>
          <c:set var="nomeUsuario" value="---"/>
          <%-- ALTERADO: u.id -> u.idUsuario --%>
          <c:forEach var="u" items="${usuarios}"><c:if test="${u.idUsuario == t.idUsuario}"><c:set var="nomeUsuario" value="${u.nome}"/></c:if></c:forEach>
          <tr data-id="<c:out value="${t.idTelefone}"/>" data-nome="<c:out value="${numeroBr}"/>" data-numero="<c:out value="${numeroBr}"/>" data-form-numero="<c:out value="${t.numero}"/>" data-id-usuario="<c:out value="${t.idUsuario}"/>" data-usuario="<c:out value="${nomeUsuario}"/>" data-tag="<c:out value="${'Telefone'}"/>">
            <td><div class="celula-principal"><input class="tabela__selecionar" type="radio" name="linha" value="${t.idTelefone}" aria-label="Selecionar linha"><span><c:out value="${numeroBr}"/></span></div></td>
            <td><c:out value="${nomeUsuario}"/></td>
            <td><button class="acoes-linha" type="button" aria-label="Mais ações"></button></td>
          </tr>
        </c:forEach>
        <%-- Linhas vazias só de enfeite, para a tabela ter sempre 8 linhas --%>
        <c:forEach begin="${fn:length(telefones)}" end="7">
          <tr aria-hidden="true"><td><div class="celula-principal"><span class="tabela__selecionar"></span></div></td><td></td><td></td></tr>
        </c:forEach>
        </tbody>
      </table>
    </section>
  </main>
</div>

<%-- Janelas compartilhadas: configurações, perfil, excluir e sair --%>
<jsp:include page="/WEB-INF/views/partes/modais-comuns.jsp"/>

<%-- Modal de cadastro e de edição: o mesmo formulário serve para os dois --%>
<dialog class="modal" id="modal-telefone" style="--largura:640px" aria-labelledby="modal-telefone-titulo">
  <%-- Cadastrar e editar usam rotas diferentes; o interacoes.js troca o destino conforme o modo --%>
  <form action="${ctx}/adicionarTelefone" data-acao-novo="${ctx}/adicionarTelefone" data-acao-editar="${ctx}/alterarTelefone" method="post">
    <input type="hidden" name="idTelefone" data-chave-id> <%-- vazio = cadastrar; preenchido = editar --%>
    <header class="modal__cabecalho">
      <p class="modal__etiqueta" data-texto-editar="Editar registro">Novo registro</p>
      <h2 id="modal-telefone-titulo" class="modal__titulo" data-texto-editar="Editar telefone">Cadastrar telefone</h2>
      <button class="modal__fechar" type="button" data-fechar-modal aria-label="Fechar"><img class="" src="${ctx}/assets/icones/fechar.svg" alt=""></button>
    </header>
    <div class="modal__linha"></div>
    <div class="modal__corpo">
      <%-- Campos do formulário: o name de cada um é o nome que o servidor lê --%>
      <div class="formulario">
        <%-- Só os números; o servidor guarda de 9 a 11 dígitos --%>
        <label class="campo">Número<input type="tel" name="numero" placeholder="(11) 91234-5678" required maxlength="15" inputmode="tel"></label>
        <%-- ALTERADO: u.id -> u.idUsuario --%>
        <%-- Dono do telefone (um usuário que já existe) --%>
        <label class="campo">Usuário<select name="idUsuario" required><option value="">Selecione</option><c:forEach var="u" items="${usuarios}"><option value="${u.idUsuario}"><c:out value="${u.nome}"/></option></c:forEach></select></label>
      </div>
    </div>
    <div class="modal__linha"></div>
    <footer class="modal__rodape">
      <button class="botao botao--contorno" type="button" data-fechar-modal>Cancelar</button>
      <button class="botao botao--primario" type="submit">Salvar telefone</button>
    </footer>
  </form>
</dialog>

<%-- Ficha (detalhes) da linha marcada; os textos são preenchidos pelo interacoes.js --%>
<dialog class="modal" id="ficha-telefone" data-ficha style="--largura:420px" aria-labelledby="ficha-telefone-titulo">
  <header class="modal__cabecalho">
    <p class="modal__etiqueta">Ficha do telefone</p>
    <h2 id="ficha-telefone-titulo" class="modal__titulo">Detalhes</h2>
    <button class="modal__fechar" type="button" data-fechar-modal aria-label="Fechar"><img class="" src="${ctx}/assets/icones/fechar.svg" alt=""></button>
  </header>
  <div class="modal__linha"></div>
  <div class="modal__corpo">
    <div class="ficha__perfil">
      <span class="ficha__foto" aria-hidden="true" data-campo="iniciais">—</span>
      <div><p class="ficha__nome" data-campo="nome">—</p><span class="ficha__tag" data-campo="tag">—</span></div>
    </div>
    <ul>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/telefone.svg" alt=""></span><div><p class="ficha__rotulo">Número</p><p class="ficha__valor" data-campo="numero">—</p></div></li>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/pessoas.svg" alt=""></span><div><p class="ficha__rotulo">Usuário vinculado</p><p class="ficha__valor" data-campo="usuario">—</p></div></li>
    </ul>
  </div>
  <footer class="modal__rodape">
    <button class="botao botao--contorno" type="button" data-fechar-modal>Fechar</button>
    <button class="botao botao--primario" type="button" data-abrir-modal="modal-telefone" data-modo="editar"><img class="icone-branco" src="${ctx}/assets/icones/editar.svg" alt="">Editar</button>
  </footer>
</dialog>

</body>
</html>