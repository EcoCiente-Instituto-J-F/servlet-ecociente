<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%-- Tela de condomínios do painel: lista, ficha de detalhes e formulário de cadastro e edição --%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<%-- Diz ao menu qual ícone deve ficar destacado --%>
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

  <%-- Cabeçalho e menu lateral, iguais em todas as telas do painel --%>
  <jsp:include page="/WEB-INF/views/partes/cabecalho.jsp"/>
  <jsp:include page="/WEB-INF/views/partes/menu.jsp"/>

  <main class="conteudo">
    <%-- Título da página e botões: Editar, Excluir, Detalhes e Cadastrar (os três primeiros só funcionam com uma linha marcada) --%>
    <header class="cabecalho-pagina">
      <div>
        <p class="cabecalho-pagina__etiqueta">Tabela: Condomínio - Síndico</p>
        <h1 class="cabecalho-pagina__titulo">Condomínios</h1>
        <p class="cabecalho-pagina__descricao">Residenciais, comerciais, industriais e mistos, com o respectivo síndico</p>
      </div>
      <div class="cabecalho-pagina__acoes">
        <a class="botao botao--contorno" href="#" data-acao="editar" data-abrir-modal="modal-condominio" data-modo="editar" aria-disabled="true"><img src="${ctx}/assets/icones/editar.svg" alt="">Editar</a>
        <a class="botao botao--contorno" href="#" data-requer-selecao aria-disabled="true" data-abrir-modal="modal-excluir" data-excluir-url="${ctx}/condominios" data-excluir-acao="excluir">Excluir</a>
        <a class="botao botao--destaque" href="#" data-requer-selecao aria-disabled="true" data-abrir-modal="ficha-condominio"><img src="${ctx}/assets/icones/mais-branco.svg" alt="">Detalhes</a>
        <a class="botao botao--primario" href="#" data-abrir-modal="modal-condominio"><img src="${ctx}/assets/icones/mais-branco.svg" alt="">Cadastrar</a>
      </div>
    </header>
    <%-- Avisos de sucesso ou erro depois de cadastrar, alterar ou excluir --%>
    <jsp:include page="/WEB-INF/views/partes/avisos.jsp"><jsp:param name="reabrirModal" value="modal-condominio"/></jsp:include>
    <%-- Depois de cadastrar, o servidor manda o código (token) do condomínio novo --%>
    <c:if test="${not empty tokenGerado}"><p class="aviso aviso--sucesso" role="status">Código do condomínio: <strong><c:out value="${tokenGerado}"/></strong>. Compartilhe com os moradores.</p></c:if>
    <%-- Tabela de condomínios --%>
    <section class="tabela-cartao">
      <c:if test="${empty condominios and not empty param.q}"><p class="aviso aviso--vazio" role="status">Nenhum resultado encontrado para “<c:out value="${param.q}"/>”.</p></c:if>
      <%-- Cada linha guarda seus dados em atributos data-*; o interacoes.js usa isso para preencher a ficha e o formulário de edição --%>
      <table class="tabela"${ordenacaoServidor ? ' data-ordem-servidor' : ''}>
        <colgroup><col style="width:33%"><col style="width:26%"><col style="width:22%"><col style="width:14%"><col style="width:5%"></colgroup>
        <thead><tr><th scope="col"><button class="tabela__ordem" type="button" data-ordem="nome">Nome do condomínio</button></th><th scope="col"><button class="tabela__ordem" type="button" data-ordem="cnpj">CNPJ</button></th><th scope="col"><button class="tabela__ordem" type="button" data-ordem="tipo">Tipo</button></th><th scope="col"><button class="tabela__ordem" type="button" data-ordem="sindico">Síndico</button></th><th scope="col"><span class="somente-leitor">Ações</span></th></tr></thead>
        <tbody>
          <%-- Uma linha para cada item enviado pelo servidor --%>
          <c:forEach var="c" items="${condominios}">
            <%-- O id do síndico só existe quando o servidor envia essa informação; se não enviar, fica vazio --%>
            <c:set var="idSindicoLinha" value=""/>
            <c:catch var="semIdSindico"><c:set var="idSindicoLinha" value="${c.idUsuarioSindico}"/></c:catch>
            <tr data-id="<c:out value="${c.idCondominio}"/>" data-nome="<c:out value="${c.nome}"/>" data-cnpj="<c:out value="${c.cnpj}"/>" data-tipo="<c:out value="${c.tipo}"/>" data-sindico="<c:out value="${c.sindico}"/>" data-endereco="<c:out value="${c.endereco}"/>" data-id-tipo-condominio="<c:out value="${c.idTipoCondominio}"/>" data-numero-unidades="<c:out value="${c.numeroUnidades}"/>" data-area-m2="<c:out value="${c.areaM2}"/>" data-iniciais="<c:out value="${c.iniciais}"/>" data-tag="<c:out value="${c.tipo}"/>" data-cep="<c:out value="${c.cep}"/>" data-cidade="<c:out value="${c.cidade}"/>" data-estado="<c:out value="${c.estado}"/>" data-bairro="<c:out value="${c.bairro}"/>" data-rua="<c:out value="${c.rua}"/>" data-numero="<c:out value="${c.numero}"/>" data-complemento="<c:out value="${c.complemento}"/>" data-id-usuario-sindico="<c:out value="${idSindicoLinha}"/>" data-token="<c:out value="${c.token}"/>">
              <td><div class="celula-principal"><input class="tabela__selecionar" type="radio" name="linha" value="${c.idCondominio}" aria-label="Selecionar linha"><span><c:out value="${c.nome}"/></span></div></td>
              <td><c:out value="${c.cnpj}"/></td>
              <td><c:out value="${c.tipo}"/></td>
              <td><c:out value="${c.sindico}"/></td>
              <td><button class="acoes-linha" type="button" aria-label="Mais ações"></button></td>
            </tr>
          </c:forEach>
          <%-- Linhas vazias só de enfeite, para a tabela ter sempre 8 linhas --%>
          <c:forEach begin="${fn:length(condominios)}" end="7">
            <tr aria-hidden="true"><td><div class="celula-principal"><span class="tabela__selecionar"></span></div></td><td></td><td></td><td></td><td></td></tr>
          </c:forEach>
        </tbody>
      </table>
      <%-- Números das páginas --%>
      <jsp:include page="/WEB-INF/views/partes/paginacao.jsp"/>
    </section>
  </main>
</div>

<%-- Janelas compartilhadas: configurações, perfil, excluir e sair --%>
<jsp:include page="/WEB-INF/views/partes/modais-comuns.jsp"/>

<%-- Modal de cadastro e de edição: o mesmo formulário serve para os dois --%>
<dialog class="modal" id="modal-condominio" style="--largura:640px" aria-labelledby="modal-condominio-titulo">
  <%-- Uma só rota para tudo: o campo id vazio cadastra e preenchido altera --%>
  <form action="${ctx}/condominios" method="post">
    <input type="hidden" name="id" data-chave-id> <%-- vazio = cadastrar; preenchido = editar --%>
    <header class="modal__cabecalho">
      <p class="modal__etiqueta" data-texto-editar="Editar registro">Novo registro</p>
      <h2 id="modal-condominio-titulo" class="modal__titulo" data-texto-editar="Editar condomínio">Cadastrar condomínio</h2>
      <button class="modal__fechar" type="button" data-fechar-modal aria-label="Fechar"><img class="" src="${ctx}/assets/icones/fechar.svg" alt=""></button>
    </header>
    <div class="modal__linha"></div>
    <div class="modal__corpo">
      <%-- Campos do formulário: o name de cada um é o nome que o servidor lê --%>
      <div class="formulario">
        <label class="campo">Nome<input type="text" name="nome" placeholder="Nome do condomínio" required maxlength="100"></label>
        <label class="campo">CNPJ<input type="text" name="cnpj" placeholder="00.000.000/0001-00" required maxlength="18" inputmode="numeric"></label>
        <%-- Os tipos vêm da lista tiposCondominio enviada pelo servidor --%>
        <label class="campo">Tipo<select name="idTipoCondominio" required><option value="">Selecione</option><c:forEach var="t" items="${tiposCondominio}"><option value="${t.id}" data-industrial="${t.nome == 'Industrial'}"><c:out value="${t.nome}"/></option></c:forEach></select></label>
        <%-- Unidades (residencial e comercial) ou área em m² (industrial): nunca os dois --%>
        <label class="campo" id="campo-unidades">Nº de unidades (exceto industrial)<input type="number" name="numeroUnidades" placeholder="120" min="1"></label>
        <label class="campo" id="campo-area">Área em m² (só industrial)<input type="number" name="areaM2" placeholder="1500.00" step="0.01" min="0.01"></label>
        <%-- Endereço do condomínio: obrigatório --%>
        <label class="campo">CEP<input type="text" name="cep" placeholder="00000-000" maxlength="9" inputmode="numeric" autocomplete="postal-code" required></label>
        <label class="campo">Cidade<input type="text" name="cidade" placeholder="São Paulo" required></label>
        <label class="campo">Estado<input type="text" name="estado" placeholder="SP" maxlength="2" required></label>
        <label class="campo">Bairro<input type="text" name="bairro" placeholder="Nome do bairro"></label>
        <label class="campo">Rua<input type="text" name="rua" placeholder="Nome da rua" required></label>
        <label class="campo">Número<input type="number" name="numero" placeholder="123" min="0" required></label>
        <label class="campo">Complemento<input type="text" name="complemento" placeholder="Apto, bloco, casa (opcional)"></label>
        <%-- Síndico (opcional): só aparece como escolha ao cadastrar; ao editar os campos ficam travados e escondidos (data-somente-novo) --%>
        <label class="campo">Síndico (opcional)<select name="idUsuarioSindico" data-somente-novo><option value="">Sem síndico por enquanto</option><c:forEach var="u" items="${usuariosSindico}"><option value="${u.idUsuario}"><c:out value="${u.nome}"/></option></c:forEach></select></label>
        <label class="campo">Início do mandato<input type="date" name="dataInicioMandato" data-somente-novo></label>
      </div>
    </div>
    <div class="modal__linha"></div>
    <footer class="modal__rodape">
      <button class="botao botao--contorno" type="button" data-fechar-modal>Cancelar</button>
      <button class="botao botao--primario" type="submit">Salvar condomínio</button>
    </footer>
  </form>
</dialog>

<%-- Ficha (detalhes) da linha marcada; os textos são preenchidos pelo interacoes.js --%>
<dialog class="modal" id="ficha-condominio" data-ficha style="--largura:420px" aria-labelledby="ficha-condominio-titulo">
    <header class="modal__cabecalho">
      <p class="modal__etiqueta">Ficha do condomínio</p>
      <h2 id="ficha-condominio-titulo" class="modal__titulo">Detalhes</h2>
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
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/endereco.svg" alt=""></span><div><p class="ficha__rotulo">Endereço</p><p class="ficha__valor" data-campo="endereco">—</p></div></li>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/condominios.svg" alt=""></span><div><p class="ficha__rotulo">Nº de unidades</p><p class="ficha__valor" data-campo="numeroUnidades">—</p></div></li>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/endereco.svg" alt=""></span><div><p class="ficha__rotulo">Área em m²</p><p class="ficha__valor" data-campo="areaM2">—</p></div></li>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/folha.svg" alt=""></span><div><p class="ficha__rotulo">Código do condomínio</p><p class="ficha__valor" data-campo="token">—</p></div></li>
      <li class="ficha__item"><span class="ficha__icone"><img class="" src="${ctx}/assets/icones/pessoas.svg" alt=""></span><div><p class="ficha__rotulo">Síndico responsável</p><p class="ficha__valor" data-campo="sindico">—</p></div></li>
      </ul>
    </div>
    <footer class="modal__rodape">
      <button class="botao botao--contorno" type="button" data-fechar-modal>Fechar</button>
      <button class="botao botao--primario" type="button" data-abrir-modal="modal-condominio" data-modo="editar"><img class="icone-branco" src="${ctx}/assets/icones/editar.svg" alt="">Editar</button>
    </footer>
</dialog>

<%-- Ao mudar o tipo do condomínio, mostra só o campo certo: área (industrial) ou número de unidades (os outros) --%>
<script>
  (function () {
    var tipo = document.querySelector('#modal-condominio select[name="idTipoCondominio"]');
    var campoUnidades = document.getElementById('campo-unidades');
    var campoArea = document.getElementById('campo-area');
    if (!tipo || !campoUnidades || !campoArea) return;

    // Mostra ou esconde o campo e, se escondido, não envia nem exige o valor
    function mostrar(campo, visivel) {
      var entrada = campo.querySelector('input');
      campo.hidden = !visivel;
      entrada.disabled = !visivel;
      entrada.required = visivel;
    }

    function atualizar() {
      var opcao = tipo.options[tipo.selectedIndex];
      var escolheu = opcao && opcao.value !== '';
      var industrial = escolheu && opcao.dataset.industrial === 'true';
      mostrar(campoUnidades, escolheu && !industrial);
      mostrar(campoArea, industrial);
    }

    tipo.addEventListener('change', atualizar);
    atualizar();
  })();
</script>

</body>
</html>
