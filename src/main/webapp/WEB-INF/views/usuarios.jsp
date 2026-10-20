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
        <a class="botao botao--destaque" href="#" data-abrir-modal="ficha-usuario"><img src="${ctx}/assets/icones/mais-branco.svg" alt="">Detalhes</a>
        <a class="botao botao--primario" href="#" data-abrir-modal="modal-usuario"><img src="${ctx}/assets/icones/mais-branco.svg" alt="">Cadastrar</a>
      </div>
    </header>
    <section class="tabela-cartao">
      <table class="tabela">
        <colgroup><col style="width:30%"><col style="width:30%"><col style="width:23%"><col style="width:12%"><col style="width:5%"></colgroup>
        <thead><tr><th scope="col">Nome do usuário</th><th scope="col">E-mail</th><th scope="col">Localidade</th><th scope="col">Tipo</th><th scope="col"><span class="tabela__ordenar"><img src="${ctx}/assets/icones/ordenar.svg" alt="Ordenar"></span></th></tr></thead>
        <tbody>
          <c:forEach var="u" items="${usuarios}">
            <tr>
              <td><div class="celula-principal"><input class="tabela__selecionar" type="radio" name="linha" value="${u.id}" aria-label="Selecionar linha"><c:if test="${not empty u.foto}"><img class="avatar" src="${ctx}/${u.foto}" alt=""></c:if><span><c:out value="${u.nome}"/></span></div></td>
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
  <form action="${ctx}/usuarios" method="post">
    <input type="hidden" name="id"> <%-- vazio = cadastrar; preenchido = editar --%>
    <header class="modal__cabecalho">
      <p class="modal__etiqueta" data-texto-editar="Editar registro">Novo registro</p>
      <h2 id="modal-usuario-titulo" class="modal__titulo" data-texto-editar="Editar usuário">Cadastrar usuário</h2>
      <button class="modal__fechar" type="button" data-fechar-modal aria-label="Fechar"><img class="" src="${ctx}/assets/icones/fechar.svg" alt=""></button>
    </header>
    <div class="modal__linha"></div>
    <div class="modal__corpo">
      <div class="formulario">
        <label class="campo">Nome completo<input type="text" name="nome" placeholder="Nome do usuário"></label>
        <label class="campo">E-mail<input type="email" name="email" placeholder="email@exemplo.com"></label>
        <label class="campo">Telefone<input type="tel" name="telefone" placeholder="(11) 90000-0000"></label>
        <label class="campo">Tipo de usuário<select name="tipo"><option>Usuário comum</option><option>Usuário Comercial</option><option>Síndico Residencial</option><option>Cooperativa</option></select></label>
        <label class="campo">CEP<input type="text" name="cep" placeholder="00000-000"></label>
        <label class="campo">Cidade<input type="text" name="cidade" placeholder="São Paulo / SP"></label>
        <label class="campo campo--largo">Rua e número<input type="text" name="endereco" placeholder="Rua, número, complemento"></label>
      </div>
    </div>
    <div class="modal__linha"></div>
    <footer class="modal__rodape">
      <button class="botao botao--contorno" type="button" data-fechar-modal>Cancelar</button>
      <button class="botao botao--primario" type="submit">Salvar Usuário</button>
    </footer>
  </form>
</dialog>

<dialog class="modal" id="ficha-usuario" style="--largura:403px" aria-labelledby="ficha-usuario-titulo">
    <header class="modal__cabecalho">
      <p class="modal__etiqueta">Ficha do usuário</p>
      <h2 id="ficha-usuario-titulo" class="modal__titulo">Detalhes</h2>
      <button class="modal__fechar" type="button" data-fechar-modal aria-label="Fechar"><img class="" src="${ctx}/assets/icones/fechar.svg" alt=""></button>
    </header>
    <div class="modal__linha"></div>
    <div class="modal__corpo">
      <div class="ficha__perfil">
        <span class="ficha__foto" aria-hidden="true">MJ</span>
        <div><p class="ficha__nome">Michael Jackson</p><span class="ficha__tag">Síndico</span></div>
      </div>
      <ul>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/email.svg" alt=""></span><div><p class="ficha__rotulo">E-mail</p><p class="ficha__valor">michaelson.ofc@gmail.com</p></div></li>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/telefone.svg" alt=""></span><div><p class="ficha__rotulo">Telefone</p><p class="ficha__valor">(11) 98877-1122</p></div></li>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/endereco.svg" alt=""></span><div><p class="ficha__rotulo">Endereço</p><p class="ficha__valor">Rua mágica, 100 - Estados Unidos, EUA</p></div></li>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/pessoas.svg" alt=""></span><div><p class="ficha__rotulo">Tipo de usuário</p><p class="ficha__valor">Síndico residencial</p></div></li>
      </ul>
    </div>
    <footer class="modal__rodape">
      <button class="botao botao--contorno" type="button" data-fechar-modal>Fechar</button>
      <button class="botao botao--primario" type="button" data-abrir-modal="modal-usuario" data-modo="editar"><img class="icone-branco" src="${ctx}/assets/icones/editar.svg" alt="">Editar</button>
    </footer>
</dialog>


</body>
</html>
