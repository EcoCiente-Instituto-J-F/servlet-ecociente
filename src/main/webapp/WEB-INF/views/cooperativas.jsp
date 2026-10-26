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
        <a href="${ctx}/painel" class="botao-voltar" aria-label="Voltar para o painel">
          <span class="seta" aria-hidden="true">&larr;</span>
          <span>Voltar</span>
        </a>
        <p class="cabecalho-pagina__etiqueta">Tabela: Cooperativa</p>
        <h1 class="cabecalho-pagina__titulo">Cooperativas</h1>
        <p class="cabecalho-pagina__descricao">Visíveis no mapa de ponto de coleta</p>
      </div>
      <div class="cabecalho-pagina__acoes">
        <a class="botao botao--contorno" href="#" data-acao="editar" data-abrir-modal="modal-cooperativa" data-modo="editar" aria-disabled="true"><img src="${ctx}/assets/icones/editar.svg" alt="">Editar</a>
        <a class="botao botao--contorno" href="#" data-requer-selecao aria-disabled="true" data-abrir-modal="modal-excluir" data-excluir-url="${ctx}/cooperativas" data-excluir-acao="excluir">Excluir</a>
        <a class="botao botao--destaque" href="#" data-requer-selecao aria-disabled="true" data-abrir-modal="ficha-cooperativa"><img src="${ctx}/assets/icones/mais-branco.svg" alt="">Detalhes</a>
        <a class="botao botao--primario" href="#" data-abrir-modal="modal-cooperativa"><img src="${ctx}/assets/icones/mais-branco.svg" alt="">Cadastrar</a>
      </div>
    </header>
    <jsp:include page="/WEB-INF/views/partes/avisos.jsp"><jsp:param name="reabrirModal" value="modal-cooperativa"/></jsp:include>
    <section class="tabela-cartao">
      <c:if test="${empty cooperativas and not empty param.q}"><p class="aviso aviso--vazio" role="status">Nenhum resultado encontrado para “<c:out value="${param.q}"/>”.</p></c:if>
      <table class="tabela"${ordenacaoServidor ? ' data-ordem-servidor' : ''}>
        <colgroup><col style="width:33%"><col style="width:23%"><col style="width:25%"><col style="width:14%"><col style="width:5%"></colgroup>
        <thead><tr><th scope="col"><button class="tabela__ordem" type="button" data-ordem="nomeCooperativa">Cooperativa</button></th><th scope="col"><button class="tabela__ordem" type="button" data-ordem="cnpj">CNPJ</button></th><th scope="col"><button class="tabela__ordem" type="button" data-ordem="condominiosVinculados">Condomínios vinculados</button></th><th scope="col"><button class="tabela__ordem" type="button" data-ordem="status">Status</button></th><th scope="col"><span class="somente-leitor">Ações</span></th></tr></thead>
        <tbody>
          <c:forEach var="c" items="${cooperativas}">
            <tr data-id="<c:out value="${c.idCooperativa}"/>" data-nome="<c:out value="${c.nomeCooperativa}"/>" data-nome-cooperativa="<c:out value="${c.nomeCooperativa}"/>" data-responsavel="<c:out value="${c.responsavel}"/>" data-email="<c:out value="${c.email}"/>" data-cnpj="<c:out value="${c.cnpj}"/>" data-telefone="<c:out value="${c.telefone}"/>" data-endereco="<c:out value="${c.endereco}"/>" data-status="<c:out value="${c.status}"/>" data-iniciais="<c:out value="${c.iniciais}"/>" data-cpf="<c:out value="${c.cpf}"/>" data-tag="<c:out value="${c.tag}"/>" data-cep="<c:out value="${c.cep}"/>" data-cidade="<c:out value="${c.cidade}"/>" data-estado="<c:out value="${c.estado}"/>" data-bairro="<c:out value="${c.bairro}"/>" data-rua="<c:out value="${c.rua}"/>" data-numero="<c:out value="${c.numero}"/>" data-complemento="<c:out value="${c.complemento}"/>">
              <td><div class="celula-principal"><input class="tabela__selecionar" type="radio" name="linha" value="${c.idCooperativa}" aria-label="Selecionar linha"><span><c:out value="${c.nomeCooperativa}"/></span></div></td>
              <td><c:out value="${c.cnpj}"/></td>
              <td><c:out value="${c.condominiosVinculados}"/></td>
              <td><span class="selo ${c.status ? 'selo--ativa' : 'selo--inativa'}">${c.status ? 'Ativa' : 'Inativa'}</span></td>
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

<dialog class="modal" id="modal-cooperativa" style="--largura:640px" aria-labelledby="modal-cooperativa-titulo">
  <form action="${ctx}/cooperativas" method="post">
    <input type="hidden" name="id" data-chave-id> <%-- vazio = cadastrar; preenchido = editar --%>
    <header class="modal__cabecalho">
      <p class="modal__etiqueta" data-texto-editar="Editar registro">Novo registro</p>
      <h2 id="modal-cooperativa-titulo" class="modal__titulo" data-texto-editar="Editar cooperativa">Cadastrar cooperativa</h2>
      <button class="modal__fechar" type="button" data-fechar-modal aria-label="Fechar"><img class="" src="${ctx}/assets/icones/fechar.svg" alt=""></button>
    </header>
    <div class="modal__linha"></div>
    <div class="modal__corpo">
      <div class="formulario">
        <label class="campo">Nome da cooperativa<input type="text" name="nomeCooperativa" placeholder="Verde Viva" required maxlength="100"></label>
        <label class="campo">Nome do responsável<input type="text" name="responsavel" placeholder="Carlos Mendes" required maxlength="100"></label>
        <label class="campo">CPF do responsável<input type="text" name="cpf" placeholder="000.000.000-00" required maxlength="14" inputmode="numeric"></label>
        <label class="campo">Telefone<input type="tel" name="telefone" placeholder="(11) 90000-0000" maxlength="15" inputmode="tel"></label>
        <label class="campo">E-mail<input type="email" name="email" placeholder="contato@cooperativa.com" data-obrigatorio-novo maxlength="150"></label>
        <label class="campo">CNPJ<input type="text" name="cnpj" placeholder="00.000.000/0001-00" required maxlength="18" inputmode="numeric"></label>
        <label class="campo">Senha<input type="password" name="senha" placeholder="Mínimo de 6 caracteres" data-obrigatorio-novo minlength="6" autocomplete="new-password"></label>
        <label class="campo">Status<select name="status"><option value="true">Ativa</option><option value="false">Inativa</option></select></label>
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
      <button class="botao botao--primario" type="submit">Salvar cooperativa</button>
    </footer>
  </form>
</dialog>

<dialog class="modal" id="ficha-cooperativa" data-ficha style="--largura:420px" aria-labelledby="ficha-cooperativa-titulo">
    <header class="modal__cabecalho">
      <p class="modal__etiqueta">Ficha da cooperativa</p>
      <h2 id="ficha-cooperativa-titulo" class="modal__titulo">Detalhes</h2>
      <button class="modal__fechar" type="button" data-fechar-modal aria-label="Fechar"><img class="" src="${ctx}/assets/icones/fechar.svg" alt=""></button>
    </header>
    <div class="modal__linha"></div>
    <div class="modal__corpo">
      <div class="ficha__perfil">
        <span class="ficha__foto" aria-hidden="true" data-campo="iniciais">—</span>
        <div><p class="ficha__nome" data-campo="nome">—</p><span class="ficha__tag" data-campo="tag">—</span></div>
      </div>
      <ul>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/maleta.svg" alt=""></span><div><p class="ficha__rotulo">CNPJ</p><p class="ficha__valor" data-campo="cnpj">—</p></div></li>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/telefone.svg" alt=""></span><div><p class="ficha__rotulo">Telefone</p><p class="ficha__valor" data-campo="telefone">—</p></div></li>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/endereco.svg" alt=""></span><div><p class="ficha__rotulo">Endereço</p><p class="ficha__valor" data-campo="endereco">—</p></div></li>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/maleta.svg" alt=""></span><div><p class="ficha__rotulo">CPF do responsável</p><p class="ficha__valor" data-campo="cpf">—</p></div></li>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/folha.svg" alt=""></span><div><p class="ficha__rotulo">Responsável</p><p class="ficha__valor" data-campo="responsavel">—</p></div></li>
      </ul>
    </div>
    <footer class="modal__rodape">
      <button class="botao botao--contorno" type="button" data-fechar-modal>Fechar</button>
      <button class="botao botao--primario" type="button" data-abrir-modal="modal-cooperativa" data-modo="editar"><img class="icone-branco" src="${ctx}/assets/icones/editar.svg" alt="">Editar</button>
    </footer>
</dialog>

</body>
</html>
