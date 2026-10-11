<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<header class="cabecalho">
    <a class="cabecalho__logo" href="${ctx}/painel" aria-label="EcoCiente - página inicial"><img src="${ctx}/assets/img/logo-ecociente.svg" alt="EcoCiente"></a>
    <!-- TODO: LINK -> troque action="#" pela rota de busca do seu back-end -->
    <form class="busca" role="search" action="#">
      <img src="${ctx}/assets/icones/busca.svg" alt="">
      <label class="somente-leitor" for="campo-busca">Pesquisar</label>
      <input id="campo-busca" type="search" name="q" placeholder="Pesquisar usuários, condomínios, cooperativas...">
    </form>
    <button class="cabecalho__engrenagem" type="button" data-abrir-modal="modal-configuracoes" aria-label="Configurações"><img src="${ctx}/assets/icones/configuracoes.svg" alt=""></button>
    <button class="usuario-logado" type="button" data-abrir-modal="modal-perfil" aria-label="Abrir perfil">
      <span class="usuario-logado__avatar"><span class="usuario-logado__iniciais">AD</span></span> <!-- TODO: iniciais do usuário logado -->
      Admin <!-- TODO: nome do usuário logado -->
    </button>
  </header>
