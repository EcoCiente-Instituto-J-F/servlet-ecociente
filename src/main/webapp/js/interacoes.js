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
  // Botões que só funcionam com uma linha marcada (Editar e, em Usuários, Detalhes)
  const botoesDeSelecao = document.querySelectorAll('[data-acao="editar"], [data-requer-selecao]');
  let bolinhaSelecionada = document.querySelector('input[name="linha"]:checked');

  function atualizarBotaoEditar() {
    const habilitado = bolinhaSelecionada !== null;
    botoesDeSelecao.forEach((botao) => {
      botao.setAttribute('aria-disabled', String(!habilitado));
      botao.tabIndex = habilitado ? 0 : -1;
    });
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

  /* ---------- FICHA: mostra os dados da linha selecionada ---------- */

  const TELEFONES_FICTICIOS = ['(11) 98877-1122', '(11) 97654-3210', '(21) 99123-4567', '(31) 98456-7788', '(41) 99888-1020'];
  const ENDERECOS_FICTICIOS = ['Rua das Acácias, 120', 'Av. Brasil, 1500', 'Rua do Comércio, 45', 'Rua das Palmeiras, 300', 'Av. Paulista, 900'];

  function iniciais(nome) {
    const partes = nome.split(/\s+/).filter(Boolean);
    if (partes.length === 0) return '—';
    const ultima = partes.length > 1 ? partes[partes.length - 1][0] : '';
    return (partes[0][0] + ultima).toUpperCase();
  }

  function preencherFicha(modal) {
    if (!modal.hasAttribute('data-ficha') || !bolinhaSelecionada) return;
    const linha = bolinhaSelecionada.closest('tr');
    const celulas = linha.children;                                   // 0 nome, 1 e-mail, 2 localidade, 3 tipo
    const posicao = Array.from(linha.parentElement.children).indexOf(linha);
    const nome = celulas[0].textContent.trim();
    const localidade = celulas[2].textContent.trim();
    const tipo = celulas[3].textContent.trim();
    const dados = {
      iniciais: iniciais(nome),
      nome: nome,
      tag: tipo.split(' ')[0],
      email: celulas[1].textContent.trim(),
      telefone: linha.dataset.telefone || TELEFONES_FICTICIOS[posicao % TELEFONES_FICTICIOS.length],
      endereco: linha.dataset.endereco || ENDERECOS_FICTICIOS[posicao % ENDERECOS_FICTICIOS.length] + ' - ' + localidade,
      tipo: tipo
    };
    modal.querySelectorAll('[data-campo]').forEach((elemento) => {
      elemento.textContent = dados[elemento.dataset.campo] || '—';
    });
  }

  /* ---------- 3. MODAIS: abrir e fechar ---------- */
  function abrirModal(idDoModal, modo) {
    const modal = document.getElementById(idDoModal);
    if (!modal) return;

    // Textos que mudam no modo "editar"
    modal.querySelectorAll('[data-texto-editar]').forEach((elemento) => {
      elemento.dataset.textoNovo ??= elemento.textContent;
      elemento.textContent = modo === 'editar' ? elemento.dataset.textoEditar : elemento.dataset.textoNovo;
    });
    preencherFicha(modal);
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
