<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<c:set var="menuAtivo" value="sindicos" scope="request"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Síndicos | EcoCiente</title>
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
        <p class="cabecalho-pagina__etiqueta">Tabela: Síndico</p>
        <h1 class="cabecalho-pagina__titulo">Síndicos</h1>
        <p class="cabecalho-pagina__descricao">Síndicos responsáveis pelos condomínios e período do mandato</p>
      </div>
      <div class="cabecalho-pagina__acoes">
        <a class="botao botao--contorno" href="#" data-acao="editar" data-abrir-modal="modal-sindico" data-modo="editar" aria-disabled="true"><img src="${ctx}/assets/icones/editar.svg" alt="">Editar</a>
        <a class="botao botao--contorno" href="#" data-requer-selecao aria-disabled="true" data-abrir-modal="modal-excluir" data-excluir-url="${ctx}/deletarSindico" data-excluir-campo="idSindico">Excluir</a>
        <a class="botao botao--destaque" href="#" data-requer-selecao aria-disabled="true" data-abrir-modal="ficha-sindico"><img src="${ctx}/assets/icones/mais-branco.svg" alt="">Detalhes</a>
        <a class="botao botao--primario" href="#" data-abrir-modal="modal-sindico"><img src="${ctx}/assets/icones/mais-branco.svg" alt="">Cadastrar</a>
      </div>
    </header>
    <jsp:include page="/WEB-INF/views/partes/avisos.jsp"><jsp:param name="reabrirModal" value="modal-sindico"/></jsp:include>
    <section class="tabela-cartao">
      <table class="tabela">
        <colgroup><col style="width:26%"><col style="width:19%"><col style="width:25%"><col style="width:17%"><col style="width:8%"><col style="width:5%"></colgroup>
        <thead><tr><th scope="col">Nome do síndico</th><th scope="col">CPF</th><th scope="col">Condomínio</th><th scope="col">Início do mandato</th><th scope="col">Status</th><th scope="col"><span class="tabela__ordenar"><img src="${ctx}/assets/icones/ordenar.svg" alt="Ordenar"></span></th></tr></thead>
        <tbody>
          <jsp:useBean id="agora" class="java.util.Date"/>
          <fmt:formatDate value="${agora}" pattern="yyyy-MM-dd" var="hojeIso"/>
          <c:forEach var="s" items="${sindicos}">
            <c:set var="inicioIso" value="${empty s.dataInicioMandato ? '' : fn:substring(s.dataInicioMandato, 0, 10)}"/>
            <c:set var="fimIso" value="${empty s.dataFimMandato ? '' : fn:substring(s.dataFimMandato, 0, 10)}"/>
            <c:set var="inicioBr" value="${empty inicioIso ? '---' : fn:substring(inicioIso, 8, 10).concat('/').concat(fn:substring(inicioIso, 5, 7)).concat('/').concat(fn:substring(inicioIso, 0, 4))}"/>
            <c:set var="fimBr" value="${empty fimIso ? 'Atual' : fn:substring(fimIso, 8, 10).concat('/').concat(fn:substring(fimIso, 5, 7)).concat('/').concat(fn:substring(fimIso, 0, 4))}"/>
            <c:set var="ativo" value="${empty fimIso or fimIso ge hojeIso}"/>
            <c:set var="cpfBr" value="${fn:length(s.cpf) == 11 ? fn:substring(s.cpf, 0, 3).concat('.').concat(fn:substring(s.cpf, 3, 6)).concat('.').concat(fn:substring(s.cpf, 6, 9)).concat('-').concat(fn:substring(s.cpf, 9, 11)) : s.cpf}"/>
            <c:set var="nomeCondominio" value="---"/>
            <c:forEach var="o" items="${condominios}"><c:if test="${o.id == s.idCondominio}"><c:set var="nomeCondominio" value="${o.nome}"/></c:if></c:forEach>
            <tr data-id="<c:out value="${s.idSindico}"/>" data-nome="<c:out value="${s.nome}"/>" data-cpf="<c:out value="${cpfBr}"/>" data-form-cpf="<c:out value="${s.cpf}"/>" data-condominio="<c:out value="${nomeCondominio}"/>" data-id-condominio="<c:out value="${s.idCondominio}"/>" data-id-usuario="<c:out value="${s.idUsuario}"/>" data-form-data-inicio-mandato="<c:out value="${inicioIso}"/>" data-form-data-fim-mandato="<c:out value="${fimIso}"/>" data-mandato="<c:out value="${inicioBr.concat(' - ').concat(fimBr)}"/>" data-tag="<c:out value="${ativo ? 'Mandato ativo' : 'Mandato encerrado'}"/>">
              <td><div class="celula-principal"><input class="tabela__selecionar" type="radio" name="linha" value="${s.idSindico}" aria-label="Selecionar linha"><span><c:out value="${s.nome}"/></span></div></td>
              <td><c:out value="${cpfBr}"/></td>
              <td><c:out value="${nomeCondominio}"/></td>
              <td><c:out value="${inicioBr}"/></td>
              <td><span class="selo ${ativo ? 'selo--ativa' : 'selo--inativa'}">${ativo ? 'Ativo' : 'Inativo'}</span></td>
              <td><button class="acoes-linha" type="button" aria-label="Mais ações"></button></td>
            </tr>
          </c:forEach>
          <c:forEach begin="${fn:length(sindicos)}" end="7">
            <tr aria-hidden="true"><td><div class="celula-principal"><span class="tabela__selecionar"></span></div></td><td></td><td></td><td></td><td></td><td></td></tr>
          </c:forEach>
        </tbody>
      </table>
    </section>
  </main>
</div>

<jsp:include page="/WEB-INF/views/partes/modais-comuns.jsp"/>

<dialog class="modal" id="modal-sindico" style="--largura:640px" aria-labelledby="modal-sindico-titulo">
  <form action="${ctx}/adicionarSindico" data-acao-novo="${ctx}/adicionarSindico" data-acao-editar="${ctx}/alterarSindico" method="post">
    <input type="hidden" name="idSindico" data-chave-id> <%-- vazio = cadastrar; preenchido = editar --%>
    <header class="modal__cabecalho">
      <p class="modal__etiqueta" data-texto-editar="Editar registro">Novo registro</p>
      <h2 id="modal-sindico-titulo" class="modal__titulo" data-texto-editar="Editar síndico">Cadastrar síndico</h2>
      <button class="modal__fechar" type="button" data-fechar-modal aria-label="Fechar"><img class="" src="${ctx}/assets/icones/fechar.svg" alt=""></button>
    </header>
    <div class="modal__linha"></div>
    <div class="modal__corpo">
      <div class="formulario">
        <label class="campo">Usuário<select name="idUsuario" required data-por-rotulo="nome"><option value="">Selecione</option><c:forEach var="u" items="${usuarios}"><option value="${u.id}"><c:out value="${u.nome}"/></option></c:forEach></select></label>
        <label class="campo">CPF<input type="text" name="cpf" placeholder="000.000.000-00" required maxlength="14" inputmode="numeric"></label>
        <label class="campo">Início do mandato<input type="date" name="dataInicioMandato" placeholder="" required></label>
        <label class="campo">Fim do mandato (opcional)<input type="date" name="dataFimMandato" placeholder=""></label>
        <label class="campo">Condomínio<select name="idCondominio" required><option value="">Selecione</option><c:forEach var="o" items="${condominios}"><option value="${o.id}"><c:out value="${o.nome}"/></option></c:forEach></select></label>
      </div>
    </div>
    <div class="modal__linha"></div>
    <footer class="modal__rodape">
      <button class="botao botao--contorno" type="button" data-fechar-modal>Cancelar</button>
      <button class="botao botao--primario" type="submit">Salvar síndico</button>
    </footer>
  </form>
</dialog>

<dialog class="modal" id="ficha-sindico" data-ficha style="--largura:420px" aria-labelledby="ficha-sindico-titulo">
    <header class="modal__cabecalho">
      <p class="modal__etiqueta">Ficha do síndico</p>
      <h2 id="ficha-sindico-titulo" class="modal__titulo">Detalhes</h2>
      <button class="modal__fechar" type="button" data-fechar-modal aria-label="Fechar"><img class="" src="${ctx}/assets/icones/fechar.svg" alt=""></button>
    </header>
    <div class="modal__linha"></div>
    <div class="modal__corpo">
      <div class="ficha__perfil">
        <span class="ficha__foto" aria-hidden="true" data-campo="iniciais">—</span>
        <div><p class="ficha__nome" data-campo="nome">—</p><span class="ficha__tag" data-campo="tag">—</span></div>
      </div>
      <ul>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/maleta.svg" alt=""></span><div><p class="ficha__rotulo">CPF</p><p class="ficha__valor" data-campo="cpf">—</p></div></li>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/condominios.svg" alt=""></span><div><p class="ficha__rotulo">Condomínio</p><p class="ficha__valor" data-campo="condominio">—</p></div></li>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/pessoas.svg" alt=""></span><div><p class="ficha__rotulo">Período do mandato</p><p class="ficha__valor" data-campo="mandato">—</p></div></li>
      </ul>
    </div>
    <footer class="modal__rodape">
      <button class="botao botao--contorno" type="button" data-fechar-modal>Fechar</button>
      <button class="botao botao--primario" type="button" data-abrir-modal="modal-sindico" data-modo="editar"><img class="icone-branco" src="${ctx}/assets/icones/editar.svg" alt="">Editar</button>
    </footer>
</dialog>

</body>
</html>
