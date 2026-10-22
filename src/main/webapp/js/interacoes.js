/* INTERAÇÕES — menu lateral, seleção de linha, modais, formulários e exclusão */

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

  /* ---------- LINHA SELECIONADA: dados vêm dos data-* do <tr> ---------- */
  function dadosDaLinha() {
    return bolinhaSelecionada ? bolinhaSelecionada.closest('tr').dataset : null;
  }

  function iniciais(nome) {
    const partes = nome.split(/\s+/).filter(Boolean);
    if (partes.length === 0) return '—';
    const ultima = partes.length > 1 ? partes[partes.length - 1][0] : '';
    return (partes[0][0] + ultima).toUpperCase();
  }

  function preencherFicha(modal) {
    const dados = dadosDaLinha();
    if (!modal.hasAttribute('data-ficha') || !dados) return;
    const valores = { ...dados };
    if (!valores.iniciais) valores.iniciais = iniciais(dados.nome || '');
    modal.querySelectorAll('[data-campo]').forEach((elemento) => {
      elemento.textContent = valores[elemento.dataset.campo] || '—';
    });
  }

  /* ---------- FORMULÁRIO: limpa ou preenche conforme cadastrar/editar ---------- */
  const maiuscula = (texto) => texto.charAt(0).toUpperCase() + texto.slice(1);

  function preencherFormulario(modal, modo) {
    const form = modal.querySelector('form');
    if (!form || modal.id === 'modal-excluir') return;
    const editando = modo === 'editar';
    const dados = editando ? dadosDaLinha() : null;
    form.reset();

    // Um mesmo form pode ter rotas diferentes para cadastrar e editar
    if (form.dataset.acaoEditar) {
      form.action = editando ? form.dataset.acaoEditar : form.dataset.acaoNovo;
    }
    const campoId = form.querySelector('[data-chave-id]');
    if (campoId) campoId.value = dados ? (dados.id || '') : '';

    form.querySelectorAll('[name]').forEach((campo) => {
      if (campo === campoId) return;
      if (campo.hasAttribute('data-obrigatorio-novo')) campo.required = !editando;
      if (!dados || campo.type === 'password' || campo.type === 'checkbox') return;
      // data-form-xxx tem prioridade (ex.: data ISO para <input type="date">)
      let valor = dados['form' + maiuscula(campo.name)] ?? dados[campo.name];
      // select sem id na linha: procura a opção pelo texto exibido na tabela
      if (!valor && campo.dataset.porRotulo) {
        const opcao = Array.from(campo.options).find((o) => o.text.trim() === dados[campo.dataset.porRotulo]);
        if (opcao) valor = opcao.value;
      }
      if (valor !== undefined) campo.value = valor;
    });
  }

  /* ---------- EXCLUIR: um único modal de confirmação para todas as telas ---------- */
  function prepararExclusao(modal, gatilho) {
    const dados = dadosDaLinha();
    const form = modal.querySelector('form');
    if (!dados || !form) return false;
    form.action = gatilho.dataset.excluirUrl;
    const campoId = form.querySelector('[data-chave-id]');
    campoId.name = gatilho.dataset.excluirCampo || 'id';
    campoId.value = dados.id || '';
    const campoAcao = form.querySelector('[data-campo-acao]');
    campoAcao.value = gatilho.dataset.excluirAcao || '';
    campoAcao.disabled = !gatilho.dataset.excluirAcao;
    modal.querySelector('[data-excluir-rotulo]').textContent = dados.nome || dados.rotulo || 'este registro';
    return true;
  }

  /* ---------- 3. MODAIS: abrir e fechar ---------- */
  function abrirModal(idDoModal, modo, gatilho) {
    const modal = document.getElementById(idDoModal);
    if (!modal) return;

    if (idDoModal === 'modal-excluir' && !(gatilho && prepararExclusao(modal, gatilho))) return;

    // Textos que mudam no modo "editar"
    modal.querySelectorAll('[data-texto-editar]').forEach((elemento) => {
      elemento.dataset.textoNovo ??= elemento.textContent;
      elemento.textContent = modo === 'editar' ? elemento.dataset.textoEditar : elemento.dataset.textoNovo;
    });
    preencherFicha(modal);
    preencherFormulario(modal, modo);
    modal.showModal();
    return modal;
  }

  document.addEventListener('click', (evento) => {
    const gatilho = evento.target.closest('[data-abrir-modal]');
    if (gatilho) {
      evento.preventDefault();
      if (gatilho.getAttribute('aria-disabled') === 'true') return; // Editar desabilitado
      document.querySelectorAll('dialog[open]').forEach((aberto) => aberto.close());
      abrirModal(gatilho.dataset.abrirModal, gatilho.dataset.modo, gatilho);
      return;
    }
    const fechar = evento.target.closest('[data-fechar-modal]');
    if (fechar) fechar.closest('dialog').close();
    else if (evento.target.tagName === 'DIALOG') evento.target.close();
  });

  /* ---------- Erro do servidor: reabre o modal com o que a pessoa digitou ---------- */
  const reabrir = document.getElementById('reabrir-modal');
  if (reabrir) {
    const modal = abrirModal(reabrir.dataset.modal, reabrir.dataset.modo);
    if (modal) {
      reabrir.querySelectorAll('[data-nome]').forEach((item) => {
        modal.querySelectorAll('[name="' + item.dataset.nome + '"]').forEach((campo) => {
          if (campo.type !== 'password' && campo.type !== 'checkbox' && !campo.hasAttribute('data-chave-id')) {
            campo.value = item.dataset.valor;
          }
        });
      });
      const campoId = modal.querySelector('[data-chave-id]');
      if (campoId && reabrir.dataset.id) campoId.value = reabrir.dataset.id;
    }
  }

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
