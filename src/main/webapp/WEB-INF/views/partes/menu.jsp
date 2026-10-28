<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%-- Menu lateral do painel. A tela aberta no momento (menuAtivo) fica com o ícone destacado --%>
<nav class="menu-lateral" id="menu-lateral" aria-label="Menu principal">
    <%-- Botão que abre e fecha o menu --%>
    <div class="menu-lateral__secao menu-lateral__secao--topo">
      <button class="menu-lateral__item" id="botao-menu" type="button" aria-expanded="false" aria-controls="menu-lateral" aria-label="Abrir menu">
        <img class="icone-menu" src="${ctx}/assets/icones/menu.svg" alt="">
        <img class="icone-fechar" src="${ctx}/assets/icones/menu-fechar.svg" alt="">
        <span class="menu-lateral__rotulo">Fechar</span>
      </button>
    </div>
    <%-- Início: volta para a home do painel --%>
    <div class="menu-lateral__secao menu-lateral__secao--inicio"><a class="menu-lateral__item ${menuAtivo == 'painel' ? 'menu-lateral__item--ativo' : ''}" href="${ctx}/painel" title="Início" aria-label="Início" ${menuAtivo == 'painel' ? 'aria-current="page"' : ''}><img src="${ctx}/assets/icones/inicio.svg" alt=""><span class="menu-lateral__rotulo">Início</span></a></div>
    <%-- Telas de gerenciamento: usuários, condomínios, cooperativas, síndicos, moradores, endereços, telefones e vínculos --%>
    <div class="menu-lateral__secao menu-lateral__secao--navegacao">
          <a class="menu-lateral__item ${menuAtivo == 'usuarios' ? 'menu-lateral__item--ativo' : ''}" href="${ctx}/usuarios" title="Usuários" aria-label="Usuários" ${menuAtivo == 'usuarios' ? 'aria-current="page"' : ''}><img src="${ctx}/assets/icones/usuarios.svg" alt=""><span class="menu-lateral__rotulo">Usuários</span></a>
          <a class="menu-lateral__item ${menuAtivo == 'condominios' ? 'menu-lateral__item--ativo' : ''}" href="${ctx}/condominios" title="Condomínios" aria-label="Condomínios" ${menuAtivo == 'condominios' ? 'aria-current="page"' : ''}><img src="${ctx}/assets/icones/condominios.svg" alt=""><span class="menu-lateral__rotulo">Condomínios</span></a>
          <a class="menu-lateral__item ${menuAtivo == 'cooperativas' ? 'menu-lateral__item--ativo' : ''}" href="${ctx}/cooperativas" title="Cooperativas" aria-label="Cooperativas" ${menuAtivo == 'cooperativas' ? 'aria-current="page"' : ''}><img src="${ctx}/assets/icones/cooperativas.svg" alt=""><span class="menu-lateral__rotulo">Cooperativas</span></a>
          <a class="menu-lateral__item ${menuAtivo == 'sindicos' ? 'menu-lateral__item--ativo' : ''}" href="${ctx}/selectSindico" title="Síndicos" aria-label="Síndicos" ${menuAtivo == 'sindicos' ? 'aria-current="page"' : ''}><img src="${ctx}/assets/icones/maleta.svg" alt=""><span class="menu-lateral__rotulo">Síndicos</span></a>
          <a class="menu-lateral__item ${menuAtivo == 'moradores' ? 'menu-lateral__item--ativo' : ''}" href="${ctx}/moradores" title="Moradores" aria-label="Moradores" ${menuAtivo == 'moradores' ? 'aria-current="page"' : ''}><img src="${ctx}/assets/icones/pessoas.svg" alt=""><span class="menu-lateral__rotulo">Moradores</span></a>
          <a class="menu-lateral__item ${menuAtivo == 'enderecos' ? 'menu-lateral__item--ativo' : ''}" href="${ctx}/selectEndereco" title="Endereços" aria-label="Endereços" ${menuAtivo == 'enderecos' ? 'aria-current="page"' : ''}><img src="${ctx}/assets/icones/endereco.svg" alt=""><span class="menu-lateral__rotulo">Endereços</span></a>
          <a class="menu-lateral__item ${menuAtivo == 'telefones' ? 'menu-lateral__item--ativo' : ''}" href="${ctx}/selectTelefone" title="Telefones" aria-label="Telefones" ${menuAtivo == 'telefones' ? 'aria-current="page"' : ''}><img src="${ctx}/assets/icones/telefone.svg" alt=""><span class="menu-lateral__rotulo">Telefones</span></a>
          <a class="menu-lateral__item ${menuAtivo == 'vinculos' ? 'menu-lateral__item--ativo' : ''}" href="${ctx}/vinculos" title="Coletas e vínculos" aria-label="Coletas e vínculos" ${menuAtivo == 'vinculos' ? 'aria-current="page"' : ''}><img src="${ctx}/assets/icones/coletas-vinculos.svg" alt=""><span class="menu-lateral__rotulo">Vínculos</span></a>
    </div>
    <%-- Sair: abre a confirmação --%>
    <div class="menu-lateral__secao menu-lateral__secao--sair">
      <button class="menu-lateral__item" type="button" data-abrir-modal="modal-sair" title="Sair" aria-label="Sair"><img src="${ctx}/assets/icones/sair.svg" alt=""><span class="menu-lateral__rotulo">Sair</span></button>
    </div>
  </nav>
