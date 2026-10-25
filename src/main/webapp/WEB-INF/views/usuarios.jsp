<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<c:set var="menuAtivo" value="usuarios" scope="request"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Usuários | EcoCiente</title>
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
        <p class="cabecalho-pagina__etiqueta">Tabela: Usuário</p>
        <h1 class="cabecalho-pagina__titulo">Usuários</h1>
        <p class="cabecalho-pagina__descricao">Visualizar todos os usuários</p>
      </div>
      <div class="cabecalho-pagina__acoes">
        <a class="botao botao--contorno" href="#" data-acao="editar" data-abrir-modal="modal-usuario" data-modo="editar" aria-disabled="true"><img src="${ctx}/assets/icones/editar.svg" alt="">Editar</a>
        <a class="botao botao--contorno" href="#" data-requer-selecao aria-disabled="true" data-abrir-modal="modal-excluir" data-excluir-url="${ctx}/usuarios" data-excluir-acao="excluir">Excluir</a>
        <a class="botao botao--destaque" href="#" data-requer-selecao aria-disabled="true" data-abrir-modal="ficha-usuario"><img src="${ctx}/assets/icones/mais-branco.svg" alt="">Detalhes</a>
        <a class="botao botao--primario" href="#" data-abrir-modal="modal-usuario"><img src="${ctx}/assets/icones/mais-branco.svg" alt="">Cadastrar</a>
      </div>
    </header>
    <jsp:include page="/WEB-INF/views/partes/avisos.jsp"><jsp:param name="reabrirModal" value="modal-usuario"/></jsp:include>
    <section class="tabela-cartao">
      <c:if test="${empty usuarios and not empty param.q}"><p class="aviso aviso--vazio" role="status">Nenhum resultado encontrado para “<c:out value="${param.q}"/>”.</p></c:if>
      <table class="tabela"${ordenacaoServidor ? ' data-ordem-servidor' : ''}>
        <colgroup><col style="width:30%"><col style="width:30%"><col style="width:23%"><col style="width:12%"><col style="width:5%"></colgroup>
        <thead><tr><th scope="col"><button class="tabela__ordem" type="button" data-ordem="nome">Nome do usuário</button></th><th scope="col"><button class="tabela__ordem" type="button" data-ordem="email">E-mail</button></th><th scope="col"><button class="tabela__ordem" type="button" data-ordem="localidade">Localidade</button></th><th scope="col"><button class="tabela__ordem" type="button" data-ordem="tipo">Tipo</button></th><th scope="col"><span class="somente-leitor">Ações</span></th></tr></thead>
        <tbody>
          <c:forEach var="u" items="${usuarios}">
            <tr data-id="<c:out value="${u.idUsuario}"/>" data-nome="<c:out value="${u.nome}"/>" data-email="<c:out value="${u.email}"/>" data-tipo="<c:out value="${u.tipo}"/>" data-telefone="<c:out value="${u.telefone}"/>" data-endereco="<c:out value="${u.endereco}"/>" data-id-tipo-usuario="<c:out value="${u.idTipoUsuario}"/>" data-iniciais="<c:out value="${u.iniciais}"/>" data-cpf="<c:out value="${u.cpf}"/>" data-tag="<c:out value="${u.tag}"/>" data-cep="<c:out value="${u.cep}"/>" data-cidade="<c:out value="${u.cidade}"/>" data-estado="<c:out value="${u.estado}"/>" data-bairro="<c:out value="${u.bairro}"/>" data-rua="<c:out value="${u.rua}"/>" data-numero="<c:out value="${u.numero}"/>" data-complemento="<c:out value="${u.complemento}"/>">
              <td><div class="celula-principal"><input class="tabela__selecionar" type="radio" name="linha" value="${u.idUsuario}" aria-label="Selecionar linha"><span><c:out value="${u.nome}"/></span></div></td>
              <td><c:out value="${u.email}"/></td>
              <td><span class="celula-quebra celula-quebra--localidade"><c:out value="${u.localidade}"/></span></td>
              <td><span class="celula-quebra celula-quebra--tipo"><c:out value="${u.tipo}"/></span></td>
              <td><button class="acoes-linha" type="button" aria-label="Mais ações"></button></td>
            </tr>
          </c:forEach>
          <c:forEach begin="${fn:length(usuarios)}" end="7">
            <tr aria-hidden="true"><td><div class="celula-principal"><span class="tabela__selecionar"></span></div></td><td></td><td></td><td></td><td></td></tr>
          </c:forEach>
        </tbody>
      </table>
      <jsp:include page="/WEB-INF/views/partes/paginacao.jsp"/>
    </section>
  </main>
</div>

<jsp:include page="/WEB-INF/views/partes/modais-comuns.jsp"/>

<dialog class="modal" id="modal-usuario" style="--largura:640px" aria-labelledby="modal-usuario-titulo">
  <form action="${ctx}/usuarios" method="post" data-endereco-opcional>
    <input type="hidden" name="id" data-chave-id> <%-- vazio = cadastrar; preenchido = editar --%>
    <header class="modal__cabecalho">
      <p class="modal__etiqueta" data-texto-editar="Editar registro">Novo registro</p>
      <h2 id="modal-usuario-titulo" class="modal__titulo" data-texto-editar="Editar usuário">Cadastrar usuário</h2>
      <button class="modal__fechar" type="button" data-fechar-modal aria-label="Fechar"><img class="" src="${ctx}/assets/icones/fechar.svg" alt=""></button>
    </header>
    <div class="modal__linha"></div>
    <div class="modal__corpo">
      <div class="formulario">
        <label class="campo">Nome completo<input type="text" name="nome" placeholder="Nome do usuário" required maxlength="100"></label>
        <label class="campo">E-mail<input type="email" name="email" placeholder="email@exemplo.com" required maxlength="150"></label>
        <label class="campo">Senha<span class="campo__senha"><input type="password" name="senha" placeholder="Mínimo de 6 caracteres" data-placeholder-editar="Deixe em branco para manter" data-obrigatorio-novo minlength="6" autocomplete="new-password"><button class="campo__olho" type="button" data-mostrar-senha aria-label="Mostrar senha" aria-pressed="false"><svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path><circle cx="12" cy="12" r="3"></circle><line class="risco" x1="2" y1="2" x2="22" y2="22"></line></svg></button></span></label>
        <label class="campo">Confirmar senha<span class="campo__senha"><input type="password" name="confirma_senha" placeholder="Repita a senha" data-placeholder-editar="Repita só se trocar a senha" data-obrigatorio-novo autocomplete="new-password"><button class="campo__olho" type="button" data-mostrar-senha aria-label="Mostrar senha" aria-pressed="false"><svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path><circle cx="12" cy="12" r="3"></circle><line class="risco" x1="2" y1="2" x2="22" y2="22"></line></svg></button></span></label>
        <label class="campo">Telefone<input type="tel" name="telefone" placeholder="(11) 90000-0000" maxlength="15" inputmode="tel"></label>
        <label class="campo">CPF<input type="text" name="cpf" placeholder="000.000.000-00" maxlength="14" inputmode="numeric"></label>
        <label class="campo">Tipo de usuário<select name="idTipoUsuario" required><option value="">Selecione</option><c:forEach var="t" items="${tiposUsuario}"><option value="${t.id}"><c:out value="${t.nome}"/></option></c:forEach></select></label>
        <label class="campo">CEP (opcional)<input type="text" name="cep" placeholder="00000-000" maxlength="9" inputmode="numeric" autocomplete="postal-code"></label>
        <label class="campo">Cidade (opcional)<input type="text" name="cidade" placeholder="São Paulo"></label>
        <label class="campo">Estado (opcional)<input type="text" name="estado" placeholder="SP" maxlength="2"></label>
        <label class="campo">Bairro (opcional)<input type="text" name="bairro" placeholder="Nome do bairro"></label>
        <label class="campo">Rua (opcional)<input type="text" name="rua" placeholder="Nome da rua"></label>
        <label class="campo">Número (opcional)<input type="number" name="numero" placeholder="123" min="0"></label>
        <label class="campo campo--largo">Complemento (opcional)<input type="text" name="complemento" placeholder="Apto, bloco, casa (opcional)"></label>
      </div>
    </div>
    <div class="modal__linha"></div>
    <footer class="modal__rodape">
      <button class="botao botao--contorno" type="button" data-fechar-modal>Cancelar</button>
      <button class="botao botao--primario" type="submit">Salvar usuário</button>
    </footer>
  </form>
</dialog>

<dialog class="modal" id="ficha-usuario" data-ficha style="--largura:420px" aria-labelledby="ficha-usuario-titulo">
    <header class="modal__cabecalho">
      <p class="modal__etiqueta">Ficha do usuário</p>
      <h2 id="ficha-usuario-titulo" class="modal__titulo">Detalhes</h2>
      <button class="modal__fechar" type="button" data-fechar-modal aria-label="Fechar"><img class="" src="${ctx}/assets/icones/fechar.svg" alt=""></button>
    </header>
    <div class="modal__linha"></div>
    <div class="modal__corpo">
      <div class="ficha__perfil">
        <span class="ficha__foto" aria-hidden="true" data-campo="iniciais">—</span>
        <div><p class="ficha__nome" data-campo="nome">—</p><span class="ficha__tag" data-campo="tag">—</span></div>
      </div>
      <ul>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/email.svg" alt=""></span><div><p class="ficha__rotulo">E-mail</p><p class="ficha__valor" data-campo="email">—</p></div></li>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/maleta.svg" alt=""></span><div><p class="ficha__rotulo">CPF</p><p class="ficha__valor" data-campo="cpf">—</p></div></li>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/telefone.svg" alt=""></span><div><p class="ficha__rotulo">Telefone</p><p class="ficha__valor" data-campo="telefone">—</p></div></li>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/endereco.svg" alt=""></span><div><p class="ficha__rotulo">Endereço</p><p class="ficha__valor" data-campo="endereco">—</p></div></li>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/pessoas.svg" alt=""></span><div><p class="ficha__rotulo">Tipo de usuário</p><p class="ficha__valor" data-campo="tipo">—</p></div></li>
      </ul>
    </div>
    <footer class="modal__rodape">
      <button class="botao botao--contorno" type="button" data-fechar-modal>Fechar</button>
      <button class="botao botao--primario" type="button" data-abrir-modal="modal-usuario" data-modo="editar"><img class="icone-branco" src="${ctx}/assets/icones/editar.svg" alt="">Editar</button>
    </footer>
</dialog>

</body>
</html>
