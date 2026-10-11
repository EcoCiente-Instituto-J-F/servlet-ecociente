<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<c:set var="menuAtivo" value="condominios" scope="request"/>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <title>Condomínios | EcoCiente</title>
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
        <p class="cabecalho-pagina__etiqueta">Tabela: Condomínio - Síndico</p>
        <h1 class="cabecalho-pagina__titulo">Condomínios</h1>
        <p class="cabecalho-pagina__descricao">Residenciais, comerciais, industriais e mistos, com o respectivo síndico</p>
      </div>
      <div class="cabecalho-pagina__acoes">
        <a class="botao botao--contorno" href="#" data-acao="editar" data-abrir-modal="modal-condominio" data-modo="editar" aria-disabled="true"><img src="${ctx}/assets/icones/editar.svg" alt="">Editar</a>
        <a class="botao botao--destaque" href="#" data-abrir-modal="ficha-condominio"><img src="${ctx}/assets/icones/mais-branco.svg" alt="">Detalhes</a>
        <a class="botao botao--primario" href="#" data-abrir-modal="modal-condominio"><img src="${ctx}/assets/icones/mais-branco.svg" alt="">Cadastrar</a>
      </div>
    </header>
    <section class="tabela-cartao">
      <table class="tabela">
        <colgroup><col style="width:33%"><col style="width:26%"><col style="width:22%"><col style="width:14%"><col style="width:5%"></colgroup>
        <thead><tr><th scope="col">Nome do condomínio</th><th scope="col">CNPJ</th><th scope="col">Tipo</th><th scope="col">Síndico</th><th scope="col"><span class="tabela__ordenar"><img src="${ctx}/assets/icones/ordenar.svg" alt="Ordenar"></span></th></tr></thead>
        <tbody>
          <c:forEach var="c" items="${condominios}">
            <tr>
              <td><div class="celula-principal"><input class="tabela__selecionar" type="radio" name="linha" value="${c.id}" aria-label="Selecionar linha"><span><c:out value="${c.nome}"/></span></div></td>
              <td><c:out value="${c.cnpj}"/></td>
              <td><c:out value="${c.tipo}"/></td>
              <td><c:out value="${c.sindico}"/></td>
              <td><button class="acoes-linha" type="button" aria-label="Mais ações"></button></td>
            </tr>
          </c:forEach>
          <c:forEach begin="${fn:length(condominios)}" end="7">
            <tr aria-hidden="true"><td><div class="celula-principal"><span class="tabela__selecionar"></span></div></td><td></td><td></td><td></td><td></td></tr>
          </c:forEach>
        </tbody>
      </table>
      <jsp:include page="/WEB-INF/views/partes/paginacao.jsp"/>
    </section>
  </main>
</div>

<jsp:include page="/WEB-INF/views/partes/modais-comuns.jsp"/>


<dialog class="modal" id="modal-condominio" style="--largura:640px" aria-labelledby="modal-condominio-titulo">
  <form action="${ctx}/condominios" method="post">
    <input type="hidden" name="id"> <%-- vazio = cadastrar; preenchido = editar --%>
    <header class="modal__cabecalho">
      <p class="modal__etiqueta" data-texto-editar="Editar registro">Novo registro</p>
      <h2 id="modal-condominio-titulo" class="modal__titulo" data-texto-editar="Editar condomínio">Cadastrar condomínios</h2>
      <button class="modal__fechar" type="button" data-fechar-modal aria-label="Fechar"><img class="" src="${ctx}/assets/icones/fechar.svg" alt=""></button>
    </header>
    <div class="modal__linha"></div>
    <div class="modal__corpo">
      <div class="formulario">
        <label class="campo">Nome<input type="text" name="nome" placeholder="Nome do condomínio"></label>
        <label class="campo">CNPJ<input type="text" name="cnpj" placeholder="00.000.000/-0001-00"></label>
        <label class="campo">Tipo<select name="tipo"><option>Residencial</option><option>Comercial</option><option>Industrial</option><option>Misto</option></select></label>
        <label class="campo">Síndico<input type="text" name="sindico" placeholder="Renato Souza"></label>
        <label class="campo">CEP<input type="text" name="cep" placeholder="00000-000"></label>
        <span></span>
        <label class="campo campo--largo">Rua e número<input type="text" name="endereco" placeholder="Rua, número, complemento"></label>
      </div>
    </div>
    <div class="modal__linha"></div>
    <footer class="modal__rodape">
      <button class="botao botao--contorno" type="button" data-fechar-modal>Cancelar</button>
      <button class="botao botao--primario" type="submit">Salvar Condomínio</button>
    </footer>
  </form>
</dialog>

<dialog class="modal" id="ficha-condominio" style="--largura:399px" aria-labelledby="ficha-condominio-titulo">
    <header class="modal__cabecalho">
      <p class="modal__etiqueta">Ficha do condomínio</p>
      <h2 id="ficha-condominio-titulo" class="modal__titulo">Detalhes</h2>
      <button class="modal__fechar" type="button" data-fechar-modal aria-label="Fechar"><img class="" src="${ctx}/assets/icones/fechar.svg" alt=""></button>
    </header>
    <div class="modal__linha"></div>
    <div class="modal__corpo">
      <div class="ficha__perfil">
        <img class="ficha__foto" src="${ctx}/assets/img/condominios/jardins-do-vale.jpg" alt="Foto do condomínio Jardins do Vale">
        <div><p class="ficha__nome">Jardins do Vale</p><span class="ficha__tag">Residencial</span></div>
      </div>
      <ul>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/maleta.svg" alt=""></span><div><p class="ficha__rotulo">CNPJ</p><p class="ficha__valor">45.678.912/0001-23</p></div></li>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/endereco.svg" alt=""></span><div><p class="ficha__rotulo">Endereço</p><p class="ficha__valor">Rua das Hortênsias, 340 - São Paulo, SP</p></div></li>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/pessoas.svg" alt=""></span><div><p class="ficha__rotulo">Síndico responsável</p><p class="ficha__valor">Renato Souza</p></div></li>
      </ul>
    </div>
    <footer class="modal__rodape">
      <button class="botao botao--contorno" type="button" data-fechar-modal>Fechar</button>
      <button class="botao botao--primario" type="button" data-abrir-modal="modal-condominio" data-modo="editar"><img class="icone-branco" src="${ctx}/assets/icones/editar.svg" alt="">Editar</button>
    </footer>
</dialog>


</body>
</html>
