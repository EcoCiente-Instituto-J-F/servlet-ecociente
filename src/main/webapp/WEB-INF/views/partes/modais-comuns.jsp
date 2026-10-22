<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<!-- ===== Configurações (engrenagem) ===== -->
<dialog class="modal" id="modal-configuracoes" style="--largura:470px" aria-labelledby="modal-configuracoes-titulo">
  <form method="dialog"> <%-- tema fica salvo no navegador (interacoes.js); não há rota de servidor --%>
    <header class="modal__cabecalho">
      <p class="modal__etiqueta">Configurações</p>
      <h2 id="modal-configuracoes-titulo" class="modal__titulo">Detalhes</h2>
      <button class="modal__fechar" type="button" data-fechar-modal aria-label="Fechar"><img class="" src="${ctx}/assets/icones/fechar.svg" alt=""></button>
    </header>
    <div class="modal__linha"></div>
    <div class="modal__corpo">
      <p class="config__secao">Conta</p>
      <div class="formulario">
        <label class="campo">Idioma<select name="idioma"><option>Português - BR</option></select></label>
        <div class="campo">Tema
          <div class="tema">
            <label><img class="" src="${ctx}/assets/icones/sol.svg" alt="">Claro<input type="radio" name="tema" value="claro" checked></label>
            <label><img class="" src="${ctx}/assets/icones/lua.svg" alt="">Escuro<input type="radio" name="tema" value="escuro"></label>
          </div>
        </div>
      </div>
      <p class="config__secao">Notificações</p>
      <label class="config__linha">Novo usuário cadastrado<input class="interruptor" type="checkbox" name="notificar-novo-usuario" checked></label>
      <label class="config__linha">Cancelamento de condomínio<input class="interruptor" type="checkbox" name="notificar-cancelamento" checked></label>
      <label class="config__linha">Coleta agendada<input class="interruptor" type="checkbox" name="notificar-coleta"></label>
    </div>
    <footer class="modal__rodape">
      <button class="botao botao--contorno" type="button" data-fechar-modal>Cancelar</button>
      <button class="botao botao--primario" type="submit">Salvar alterações</button>
    </footer>
  </form>
</dialog>

<!-- ===== Perfil do administrador (clique no nome) ===== -->
<dialog class="modal" id="modal-perfil" style="--largura:450px" aria-labelledby="modal-perfil-titulo">
  <form method="dialog">
    <header class="modal__cabecalho">
      <p class="modal__etiqueta">Perfil do administrador</p>
      <h2 id="modal-perfil-titulo" class="modal__titulo">Detalhes</h2>
      <button class="modal__fechar" type="button" data-fechar-modal aria-label="Fechar"><img class="" src="${ctx}/assets/icones/fechar.svg" alt=""></button>
    </header>
    <div class="modal__linha"></div>
    <div class="modal__corpo">
      <div class="formulario">
        <div class="perfil__foto"><span class="usuario-logado__iniciais"><c:out value="${empty sessionScope.iniciaisUsuarioLogado ? 'AD' : sessionScope.iniciaisUsuarioLogado}"/></span></div>
        <label class="campo campo--largo campo--rotulo-caixa">Nome<input type="text" name="nome" value="<c:out value='${sessionScope.nomeUsuarioLogado}'/>" readonly></label>
        <label class="campo campo--largo campo--rotulo-caixa">Cargo<input type="text" name="cargo" value="Administrador" readonly></label>
      </div>
    </div>
    <footer class="modal__rodape">
      <button class="botao botao--contorno" type="button" data-fechar-modal>Fechar</button>
    </footer>
  </form>
</dialog>

<!-- ===== Excluir (usado por todas as telas: interacoes.js preenche rota e id) ===== -->
<dialog class="modal modal--sair" id="modal-excluir" style="--largura:340px" aria-labelledby="modal-excluir-titulo">
  <form method="post">
    <input type="hidden" name="acao" value="excluir" data-campo-acao>
    <input type="hidden" name="id" data-chave-id>
    <button class="modal__fechar" type="button" data-fechar-modal aria-label="Fechar" style="top:14px;right:16px"><img class="" src="${ctx}/assets/icones/fechar.svg" alt=""></button>
    <div class="modal__corpo">
      <h2 id="modal-excluir-titulo">Excluir <strong data-excluir-rotulo>este registro</strong>?</h2>
      <p>Essa ação não pode ser desfeita</p>
    </div>
    <footer class="modal__rodape">
      <button class="botao botao--contorno" type="button" data-fechar-modal>Cancelar</button>
      <button class="botao botao--destaque" type="submit">Excluir</button>
    </footer>
  </form>
</dialog>

<!-- ===== Sair ===== -->
<dialog class="modal modal--sair" id="modal-sair" style="--largura:324px" aria-labelledby="modal-sair-titulo">
  <button class="modal__fechar" type="button" data-fechar-modal aria-label="Fechar" style="top:14px;right:16px"><img class="" src="${ctx}/assets/icones/fechar.svg" alt=""></button>
  <div class="modal__corpo">
    <img src="${ctx}/assets/icones/sair.svg" alt="" style="width:22px;margin:0 auto">
    <h2 id="modal-sair-titulo">Tem certeza de que deseja sair?</h2>
    <p>Você poderá entrar novamente quando quiser</p>
  </div>
  <footer class="modal__rodape">
    <button class="botao botao--contorno" type="button" data-fechar-modal>Cancelar</button>
    <a class="botao botao--destaque" href="${ctx}/logout">Sair</a>
  </footer>
</dialog>
