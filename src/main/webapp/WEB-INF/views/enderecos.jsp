<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<c:set var="menuAtivo" value="enderecos" scope="request"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Endereços | EcoCiente</title>
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
        <p class="cabecalho-pagina__etiqueta">Tabela: Endereço</p>
        <h1 class="cabecalho-pagina__titulo">Endereços</h1>
        <p class="cabecalho-pagina__descricao">Endereços usados por usuários e condomínios</p>
      </div>
      <div class="cabecalho-pagina__acoes">
        <a class="botao botao--contorno" href="#" data-acao="editar" data-abrir-modal="modal-endereco" data-modo="editar" aria-disabled="true"><img src="${ctx}/assets/icones/editar.svg" alt="">Editar</a>
        <a class="botao botao--contorno" href="#" data-requer-selecao aria-disabled="true" data-abrir-modal="modal-excluir" data-excluir-url="${ctx}/deletarEndereco" data-excluir-campo="idEndereco">Excluir</a>
        <a class="botao botao--destaque" href="#" data-requer-selecao aria-disabled="true" data-abrir-modal="ficha-endereco"><img src="${ctx}/assets/icones/mais-branco.svg" alt="">Detalhes</a>
        <a class="botao botao--primario" href="#" data-abrir-modal="modal-endereco"><img src="${ctx}/assets/icones/mais-branco.svg" alt="">Cadastrar</a>
      </div>
    </header>
    <jsp:include page="/WEB-INF/views/partes/avisos.jsp"><jsp:param name="reabrirModal" value="modal-endereco"/></jsp:include>
    <section class="tabela-cartao">
      <table class="tabela">
        <colgroup><col style="width:34%"><col style="width:22%"><col style="width:22%"><col style="width:17%"><col style="width:5%"></colgroup>
        <thead><tr><th scope="col"><button class="tabela__ordem" type="button" data-ordem="c1">Rua e número</button></th><th scope="col"><button class="tabela__ordem" type="button" data-ordem="c2">Bairro</button></th><th scope="col"><button class="tabela__ordem" type="button" data-ordem="c3">Cidade / UF</button></th><th scope="col"><button class="tabela__ordem" type="button" data-ordem="c4">CEP</button></th><th scope="col"><span class="somente-leitor">Ações</span></th></tr></thead>
        <tbody>
          <c:forEach var="e" items="${enderecos}">
            <c:set var="cepBr" value="${fn:length(e.cep) == 8 ? fn:substring(e.cep, 0, 5).concat('-').concat(fn:substring(e.cep, 5, 8)) : e.cep}"/>
            <c:set var="ruaNumero" value="${e.rua}, ${e.numero}"/>
            <c:set var="completo" value="${ruaNumero}${empty e.complemento ? '' : ' - '.concat(e.complemento)}${empty e.bairro ? '' : ' - '.concat(e.bairro)} - ${e.cidade}/${e.estado}"/>
            <tr data-id="<c:out value="${e.idEndereco}"/>" data-nome="<c:out value="${ruaNumero}"/>" data-cep="<c:out value="${cepBr}"/>" data-form-cep="<c:out value="${e.cep}"/>" data-bairro="<c:out value="${e.bairro}"/>" data-cidade="<c:out value="${e.cidade}"/>" data-estado="<c:out value="${e.estado}"/>" data-rua="<c:out value="${e.rua}"/>" data-numero="<c:out value="${e.numero}"/>" data-complemento="<c:out value="${e.complemento}"/>" data-completo="<c:out value="${completo}"/>" data-tag="<c:out value="${e.cidade.concat('/').concat(e.estado)}"/>">
              <td><div class="celula-principal"><input class="tabela__selecionar" type="radio" name="linha" value="${e.idEndereco}" aria-label="Selecionar linha"><span><c:out value="${ruaNumero}"/></span></div></td>
              <td><c:out value="${e.bairro}"/></td>
              <td><c:out value="${e.cidade}"/>/<c:out value="${e.estado}"/></td>
              <td><c:out value="${cepBr}"/></td>
              <td><button class="acoes-linha" type="button" aria-label="Mais ações"></button></td>
            </tr>
          </c:forEach>
          <c:forEach begin="${fn:length(enderecos)}" end="7">
            <tr aria-hidden="true"><td><div class="celula-principal"><span class="tabela__selecionar"></span></div></td><td></td><td></td><td></td><td></td></tr>
          </c:forEach>
        </tbody>
      </table>
    </section>
  </main>
</div>

<jsp:include page="/WEB-INF/views/partes/modais-comuns.jsp"/>

<dialog class="modal" id="modal-endereco" style="--largura:640px" aria-labelledby="modal-endereco-titulo">
  <form action="${ctx}/adicionarEndereco" data-acao-novo="${ctx}/adicionarEndereco" data-acao-editar="${ctx}/alterarEndereco" method="post">
    <input type="hidden" name="idEndereco" data-chave-id> <%-- vazio = cadastrar; preenchido = editar --%>
    <header class="modal__cabecalho">
      <p class="modal__etiqueta" data-texto-editar="Editar registro">Novo registro</p>
      <h2 id="modal-endereco-titulo" class="modal__titulo" data-texto-editar="Editar endereço">Cadastrar endereço</h2>
      <button class="modal__fechar" type="button" data-fechar-modal aria-label="Fechar"><img class="" src="${ctx}/assets/icones/fechar.svg" alt=""></button>
    </header>
    <div class="modal__linha"></div>
    <div class="modal__corpo">
      <div class="formulario">
        <label class="campo">CEP<input type="text" name="cep" placeholder="00000-000" maxlength="9" inputmode="numeric" autocomplete="postal-code" required></label>
        <label class="campo">Cidade<input type="text" name="cidade" placeholder="São Paulo" required></label>
        <label class="campo">Estado<input type="text" name="estado" placeholder="SP" maxlength="2" required></label>
        <label class="campo">Bairro<input type="text" name="bairro" placeholder="Nome do bairro"></label>
        <label class="campo campo--largo">Rua<input type="text" name="rua" placeholder="Nome da rua" required></label>
        <label class="campo">Número<input type="number" name="numero" placeholder="123" min="0" required></label>
        <label class="campo">Complemento<input type="text" name="complemento" placeholder="Apto, bloco, casa (opcional)"></label>
      </div>
    </div>
    <div class="modal__linha"></div>
    <footer class="modal__rodape">
      <button class="botao botao--contorno" type="button" data-fechar-modal>Cancelar</button>
      <button class="botao botao--primario" type="submit">Salvar endereço</button>
    </footer>
  </form>
</dialog>

<dialog class="modal" id="ficha-endereco" data-ficha style="--largura:420px" aria-labelledby="ficha-endereco-titulo">
    <header class="modal__cabecalho">
      <p class="modal__etiqueta">Ficha do endereço</p>
      <h2 id="ficha-endereco-titulo" class="modal__titulo">Detalhes</h2>
      <button class="modal__fechar" type="button" data-fechar-modal aria-label="Fechar"><img class="" src="${ctx}/assets/icones/fechar.svg" alt=""></button>
    </header>
    <div class="modal__linha"></div>
    <div class="modal__corpo">
      <div class="ficha__perfil">
        <span class="ficha__foto" aria-hidden="true" data-campo="iniciais">—</span>
        <div><p class="ficha__nome" data-campo="nome">—</p><span class="ficha__tag" data-campo="tag">—</span></div>
      </div>
      <ul>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/endereco.svg" alt=""></span><div><p class="ficha__rotulo">Endereço completo</p><p class="ficha__valor" data-campo="completo">—</p></div></li>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/endereco.svg" alt=""></span><div><p class="ficha__rotulo">CEP</p><p class="ficha__valor" data-campo="cep">—</p></div></li>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/endereco.svg" alt=""></span><div><p class="ficha__rotulo">Bairro</p><p class="ficha__valor" data-campo="bairro">—</p></div></li>
      </ul>
    </div>
    <footer class="modal__rodape">
      <button class="botao botao--contorno" type="button" data-fechar-modal>Fechar</button>
      <button class="botao botao--primario" type="button" data-abrir-modal="modal-endereco" data-modo="editar"><img class="icone-branco" src="${ctx}/assets/icones/editar.svg" alt="">Editar</button>
    </footer>
</dialog>

</body>
</html>
