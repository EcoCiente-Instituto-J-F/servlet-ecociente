<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<c:set var="menuAtivo" value="cooperativas" scope="request"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Cooperativas | EcoCiente</title>
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;600;700&family=Nunito:wght@700&display=swap" rel="stylesheet">
  <!-- Aplica o tema salvo antes de pintar a página  -->
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
        <p class="cabecalho-pagina__etiqueta">Tabela: Cooperativa</p>
        <h1 class="cabecalho-pagina__titulo">Cooperativas</h1>
        <p class="cabecalho-pagina__descricao">Visíveis no mapa de ponto de coleta</p>
      </div>
      <div class="cabecalho-pagina__acoes">
        <a class="botao botao--contorno" href="#" data-acao="editar" data-abrir-modal="modal-cooperativa" data-modo="editar" aria-disabled="true"><img src="${ctx}/assets/icones/editar.svg" alt="">Editar</a>
        <a class="botao botao--destaque" href="#" data-abrir-modal="ficha-cooperativa"><img src="${ctx}/assets/icones/mais-branco.svg" alt="">Detalhes</a>
        <a class="botao botao--primario" href="#" data-abrir-modal="modal-cooperativa"><img src="${ctx}/assets/icones/mais-branco.svg" alt="">Cadastrar</a>
      </div>
    </header>
    <section class="tabela-cartao">
      <table class="tabela">
        <colgroup><col style="width:33%"><col style="width:23%"><col style="width:25%"><col style="width:14%"><col style="width:5%"></colgroup>
        <thead><tr><th scope="col">Responsável</th><th scope="col">CNPJ</th><th scope="col">Condomínios vinculados</th><th scope="col">Status</th><th scope="col"><span class="tabela__ordenar"><img src="${ctx}/assets/icones/ordenar.svg" alt="Ordenar"></span></th></tr></thead>
        <tbody>
          <c:forEach var="c" items="${cooperativas}">
            <tr>
              <td><div class="celula-principal"><input class="tabela__selecionar" type="radio" name="linha" value="${c.id}" aria-label="Selecionar linha"><span><c:out value="${c.responsavel}"/></span></div></td>
              <td><c:out value="${c.cnpj}"/></td>
              <td><c:out value="${c.condominiosVinculados}"/></td>
              <td><span class="selo ${c.ativa ? 'selo--ativa' : 'selo--inativa'}">${c.ativa ? 'Ativa' : 'Inativa'}</span></td>
              <td><button class="acoes-linha" type="button" aria-label="Mais ações"></button></td>
            </tr>
          </c:forEach>
          <c:forEach begin="${fn:length(cooperativas)}" end="7">
            <tr aria-hidden="true"><td><div class="celula-principal"><span class="tabela__selecionar"></span></div></td><td></td><td></td><td></td><td></td></tr>
          </c:forEach>
        </tbody>
      </table>
      <jsp:include page="/WEB-INF/views/partes/paginacao.jsp"/>
    </section>
  </main>
</div>

<jsp:include page="/WEB-INF/views/partes/modais-comuns.jsp"/>


<dialog class="modal" id="modal-cooperativa" style="--largura:649px" aria-labelledby="modal-cooperativa-titulo">
  <form action="${ctx}/cooperativas" method="post">
    <input type="hidden" name="id"> <%-- vazio = cadastrar; preenchido = editar --%>
    <header class="modal__cabecalho">
      <p class="modal__etiqueta" data-texto-editar="Editar registro">Novo registro</p>
      <h2 id="modal-cooperativa-titulo" class="modal__titulo" data-texto-editar="Editar cooperativa">Cadastrar cooperativas</h2>
      <button class="modal__fechar" type="button" data-fechar-modal aria-label="Fechar"><img class="" src="${ctx}/assets/icones/fechar.svg" alt=""></button>
    </header>
    <div class="modal__linha"></div>
    <div class="modal__corpo">
      <div class="formulario">
        <label class="campo">Responsável<input type="text" name="responsavel" placeholder="Carlos Mendes"></label>
        <label class="campo">CNPJ<input type="text" name="cnpj" placeholder="00.000.000/-0001-00"></label>
        <label class="campo">Status<select name="status"><option>Ativa</option><option>Inativa</option></select></label>
        <label class="campo">CEP<input type="text" name="cep" placeholder="00000-000"></label>
        <label class="campo campo--largo">Endereço<input type="text" name="endereco" placeholder="Rua, número, bairro, cidade"></label>
      </div>
    </div>
    <div class="modal__linha"></div>
    <footer class="modal__rodape">
      <button class="botao botao--contorno" type="button" data-fechar-modal>Cancelar</button>
      <button class="botao botao--primario" type="submit">Salvar cooperativa</button>
    </footer>
  </form>
</dialog>

<dialog class="modal" id="ficha-cooperativa" style="--largura:399px" aria-labelledby="ficha-cooperativa-titulo">
    <header class="modal__cabecalho">
      <p class="modal__etiqueta">Ficha da cooperativa</p>
      <h2 id="ficha-cooperativa-titulo" class="modal__titulo">Detalhes</h2>
      <button class="modal__fechar" type="button" data-fechar-modal aria-label="Fechar"><img class="" src="${ctx}/assets/icones/fechar.svg" alt=""></button>
    </header>
    <div class="modal__linha"></div>
    <div class="modal__corpo">
      <!-- TODO: BACK-END -> preencha foto, nome e dados com o registro selecionado -->
      <div class="ficha__perfil">
        <span class="ficha__foto" aria-hidden="true">CM</span>
        <div><p class="ficha__nome">Verde Viva</p><span class="ficha__tag">Ativo</span></div>
      </div>
      <ul>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/maleta.svg" alt=""></span><div><p class="ficha__rotulo">CNPJ</p><p class="ficha__valor">12.345.678/0001-90</p></div></li>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/telefone.svg" alt=""></span><div><p class="ficha__rotulo">Telefone</p><p class="ficha__valor">(11) 96754-9087</p></div></li>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/endereco.svg" alt=""></span><div><p class="ficha__rotulo">Endereço</p><p class="ficha__valor">Rua mágica, 100 - Estados Unidos, EUA</p></div></li>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/folha.svg" alt=""></span><div><p class="ficha__rotulo">Responsável</p><p class="ficha__valor">Carlos Mendes</p></div></li>
      </ul>
    </div>
    <footer class="modal__rodape">
      <button class="botao botao--contorno" type="button" data-fechar-modal>Fechar</button>
      <button class="botao botao--primario" type="button" data-abrir-modal="modal-cooperativa" data-modo="editar"><img class="icone-branco" src="${ctx}/assets/icones/editar.svg" alt="">Editar</button>
    </footer>
</dialog>


</body>
</html>
