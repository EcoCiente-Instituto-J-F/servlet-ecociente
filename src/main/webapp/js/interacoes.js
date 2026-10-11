/* INTERAÇÕES — menu lateral, seleção de linha e modais */

(() => {
  'use strict';

  /* ---------- 1. MENU LATERAL ---------- */
  const menuLateral = document.querySelector('.menu-lateral');
  const botaoMenu = document.getElementById('botao-menu');

  botaoMenu.addEventListener('click', () => {
    const estaAberto = menuLateral.classList.toggle('menu-lateral--aberto');
    botaoMenu.setAttribute('aria-expanded', String(estaAberto));
    botaoMenu.setAttribute('aria-label', estaAberto ? 'Fechar menu' : 'Abrir menu');
  });

  /* ---------- 2. SELEÇÃO DE LINHA: habilita o botão Editar ---------- */
  const botaoEditar = document.querySelector('[data-acao="editar"]');
  let bolinhaSelecionada = document.querySelector('input[name="linha"]:checked');

  function atualizarBotaoEditar() {
    if (!botaoEditar) return;
    const habilitado = bolinhaSelecionada !== null;
    botaoEditar.setAttribute('aria-disabled', String(!habilitado));
    botaoEditar.tabIndex = habilitado ? 0 : -1;
  }

  document.addEventListener('click', (evento) => {
    const bolinha = evento.target.closest('input[name="linha"]');
    if (!bolinha) return;
    if (bolinha === bolinhaSelecionada) {   // clicar de novo na mesma bolinha faz desmarcar
      bolinha.checked = false;
      bolinhaSelecionada = null;
    } else {
      bolinhaSelecionada = bolinha;
    }
    atualizarBotaoEditar();
  });
  atualizarBotaoEditar();

  /* ---------- 3. MODAIS: abrir e fechar ---------- */
  function abrirModal(idDoModal, modo) {
    const modal = document.getElementById(idDoModal);
    if (!modal) return;

    // Textos que mudam no modo "editar"
    modal.querySelectorAll('[data-texto-editar]').forEach((elemento) => {
      elemento.dataset.textoNovo ??= elemento.textContent;
      elemento.textContent = modo === 'editar' ? elemento.dataset.textoEditar : elemento.dataset.textoNovo;
    });
    modal.showModal();
  }

  document.addEventListener('click', (evento) => {
    const gatilho = evento.target.closest('[data-abrir-modal]');
    if (gatilho) {
      evento.preventDefault();
      if (gatilho.getAttribute('aria-disabled') === 'true') return; // Editar desabilitado
      document.querySelectorAll('dialog[open]').forEach((aberto) => aberto.close());
      abrirModal(gatilho.dataset.abrirModal, gatilho.dataset.modo);
      return;
    }
    const fechar = evento.target.closest('[data-fechar-modal]');
    if (fechar) fechar.closest('dialog').close();
    else if (evento.target.tagName === 'DIALOG') evento.target.close();
  });

  /* ---------- 4. TEMA CLARO/ESCURO ---------- */
  const CHAVE_DO_TEMA = 'tema-ecociente';   // nome com que o tema fica salvo no navegador

  function aplicarTema(tema) {
    document.documentElement.dataset.tema = tema;
    localStorage.setItem(CHAVE_DO_TEMA, tema);
    const opcao = document.querySelector(`input[name="tema"][value="${tema}"]`);
    if (opcao) opcao.checked = true;                        // marca Claro/Escuro nas configurações
  }
  document.querySelectorAll('input[name="tema"]').forEach((opcao) => {
    opcao.addEventListener('change', () => aplicarTema(opcao.value));
  });
  aplicarTema(localStorage.getItem(CHAVE_DO_TEMA) || 'claro');
})();
