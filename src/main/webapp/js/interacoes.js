/* INTERAÇÕES — menu lateral, seleção de linha, modais, formulários, ordenação e exclusão */

// Todo o código fica dentro de uma função que roda sozinha, para as variáveis não se misturarem com outros scripts
(() => {
  'use strict';

  /* ---------- 1. MENU LATERAL ---------- */
  // Guarda o menu lateral e o botão que abre e fecha
  const menuLateral = document.querySelector('.menu-lateral');
  const botaoMenu = document.getElementById('botao-menu');

  // Clicar no botão abre ou fecha o menu (mostra ou esconde os nomes)
  botaoMenu.addEventListener('click', () => {
    const estaAberto = menuLateral.classList.toggle('menu-lateral--aberto');
    botaoMenu.setAttribute('aria-expanded', String(estaAberto));
    botaoMenu.setAttribute('aria-label', estaAberto ? 'Fechar menu' : 'Abrir menu');
  });

  /* ---------- 2. SELEÇÃO DE LINHA: habilita o botão Editar ---------- */
  // Botões que só funcionam com uma linha marcada (Editar e, em Usuários, Detalhes)
  const botoesDeSelecao = document.querySelectorAll('[data-acao="editar"], [data-requer-selecao]');
  // Bolinha da linha marcada na tabela (null = nenhuma linha marcada)
  let bolinhaSelecionada = document.querySelector('input[name="linha"]:checked');

  // Liga ou desliga os botões Editar, Excluir e Detalhes conforme exista linha marcada
  function atualizarBotaoEditar() {
    const habilitado = bolinhaSelecionada !== null;
    botoesDeSelecao.forEach((botao) => {
      botao.setAttribute('aria-disabled', String(!habilitado));
      botao.tabIndex = habilitado ? 0 : -1;
    });
  }

  // Clique numa bolinha da tabela: marca a linha (ou desmarca, se ela já estava marcada)
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
  // Devolve os dados da linha marcada (atributos data-* do <tr>) ou null
  function dadosDaLinha() {
    return bolinhaSelecionada ? bolinhaSelecionada.closest('tr').dataset : null;
  }

  // Monta as iniciais do nome (primeira e última palavra). Ex.: Ana Lima vira AL
  function iniciais(nome) {
    const partes = nome.split(/\s+/).filter(Boolean);
    if (partes.length === 0) return '—';
    const ultima = partes.length > 1 ? partes[partes.length - 1][0] : '';
    return (partes[0][0] + ultima).toUpperCase();
  }

  // Preenche o modal de detalhes com os dados da linha marcada
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
  // Deixa a primeira letra maiúscula (nome vira Nome)
  const maiuscula = (texto) => texto.charAt(0).toUpperCase() + texto.slice(1);

  // Cadastrar: limpa o formulário. Editar: preenche com os dados da linha marcada
  function preencherFormulario(modal, modo) {
    const form = modal.querySelector('form');
    if (!form || !form.querySelector('[data-chave-id]') || modal.id === 'modal-excluir') return;
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
      if (campo.dataset.placeholderEditar) {
        campo.dataset.placeholderNovo ??= campo.placeholder;
        campo.placeholder = editando ? campo.dataset.placeholderEditar : campo.dataset.placeholderNovo;
      }
      // A senha sempre volta a ficar escondida ao abrir o modal
      if (campo.type === 'password') campo.type = 'password';
      campo.setCustomValidity('');
      // Campos só do cadastro (data-somente-novo) ficam travados e escondidos ao editar
      if (campo.hasAttribute('data-somente-novo')) {
        campo.disabled = editando;
        campo.closest('label').hidden = editando;
      }
      if (!dados || campo.type === 'password' || campo.type === 'checkbox') return;
      // data-form-xxx tem prioridade (ex.: data ISO para <input type="date">)
      let valor = dados['form' + maiuscula(campo.name)] ?? dados[campo.name];
      // select sem id na linha: procura a opção pelo texto exibido na tabela
      if (!valor && campo.dataset.porRotulo) {
        const opcao = Array.from(campo.options).find((o) => o.text.trim() === dados[campo.dataset.porRotulo]);
        if (opcao) valor = opcao.value;
      }
      if (valor !== undefined) {
        campo.value = valor;
        campo.dispatchEvent(new Event('change', { bubbles: true }));   // avisa quem depende do campo (ex.: tipo do condomínio)
      }
    });
    // Também ao cadastrar (formulário limpo): as listas avisam que voltaram ao valor inicial
    form.querySelectorAll('select').forEach((lista) => lista.dispatchEvent(new Event('change', { bubbles: true })));
  }

  /* ---------- EXCLUIR: um único modal de confirmação para todas as telas ---------- */
  // Configura o modal de exclusão: para onde enviar, qual registro apagar e o nome mostrado na pergunta
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

  /* ---------- MENU DE TRÊS PONTOS DA LINHA: Detalhes, Editar e Excluir ---------- */
  // Menu flutuante aberto agora (null = nenhum)
  let menuDaLinha = null;

  // Tira o menu flutuante da tela
  function fecharMenuDaLinha() {
    if (menuDaLinha) menuDaLinha.remove();
    menuDaLinha = null;
  }

  // Cria o menu (Detalhes, Editar, Excluir) ao lado dos três pontos. Cada item aciona o botão correspondente do topo da página
  function abrirMenuDaLinha(botaoPontos) {
    fecharMenuDaLinha();
    const radio = botaoPontos.closest('tr').querySelector('input[name="linha"]');
    if (!radio) return;
    radio.checked = true;                 // a linha do menu vira a linha selecionada
    bolinhaSelecionada = radio;
    atualizarBotaoEditar();

    const acoes = [
      ['Detalhes', document.querySelector('[data-abrir-modal^="ficha-"]')],
      ['Editar', document.querySelector('[data-acao="editar"]')],
      ['Excluir', document.querySelector('[data-abrir-modal="modal-excluir"]')]
    ].filter(([, gatilho]) => gatilho);

    menuDaLinha = document.createElement('div');
    menuDaLinha.className = 'menu-linha';
    menuDaLinha.setAttribute('role', 'menu');
    acoes.forEach(([rotulo, gatilho]) => {
      const item = document.createElement('button');
      item.type = 'button';
      item.className = 'menu-linha__item';
      item.setAttribute('role', 'menuitem');
      item.textContent = rotulo;
      item.addEventListener('click', () => { fecharMenuDaLinha(); gatilho.click(); });
      menuDaLinha.append(item);
    });
    document.body.append(menuDaLinha);

    // Posiciona o menu perto do botão sem deixar sair da tela
    const caixa = botaoPontos.getBoundingClientRect();
    const largura = menuDaLinha.offsetWidth;
    menuDaLinha.style.top = Math.min(caixa.bottom + 4, window.innerHeight - menuDaLinha.offsetHeight - 8) + 'px';
    menuDaLinha.style.left = Math.max(8, caixa.right - largura) + 'px';
    menuDaLinha.querySelector('button')?.focus();
  }

  // Clique nos três pontos abre ou fecha o menu da linha; clique fora fecha
  document.addEventListener('click', (evento) => {
    const botaoPontos = evento.target.closest('.acoes-linha');
    if (botaoPontos) {
      evento.stopPropagation();
      const jaAberto = menuDaLinha && menuDaLinha.dataset.dono === botaoPontos.closest('tr').rowIndex + '';
      if (jaAberto) { fecharMenuDaLinha(); return; }
      abrirMenuDaLinha(botaoPontos);
      if (menuDaLinha) menuDaLinha.dataset.dono = botaoPontos.closest('tr').rowIndex;
    } else if (!evento.target.closest('.menu-linha')) {
      fecharMenuDaLinha();
    }
  });
  // A tecla Esc fecha o menu
  document.addEventListener('keydown', (evento) => { if (evento.key === 'Escape') fecharMenuDaLinha(); });
  // Rolar a página também fecha o menu
  window.addEventListener('scroll', fecharMenuDaLinha, true);

  /* ---------- ORDENAR: clique no título da coluna ---------- */
  // Tabelas paginadas (data-ordem-servidor) pedem a ordem ao servidor; as outras ordenam as linhas da própria tela.
  // Mostra a setinha de ordem na coluna escolhida e tira das outras
  function marcarOrdenacao(tabela, titulo, direcao) {
    tabela.querySelectorAll('th[aria-sort]').forEach((th) => th.removeAttribute('aria-sort'));
    if (titulo) titulo.setAttribute('aria-sort', direcao === 'desc' ? 'descending' : 'ascending');
  }

  // Valor usado na comparação. Datas dd/mm/aaaa viram aaaammdd para ordenar na ordem certa
  function chaveDeOrdenacao(celula) {
    const texto = celula.textContent.trim();
    const data = texto.match(/^(\d{2})\/(\d{2})\/(\d{4})$/);
    return data ? data[3] + data[2] + data[1] : texto;
  }

  // Ordena pela coluna clicada, alternando entre crescente e decrescente
  function ordenarColuna(botao) {
    const titulo = botao.closest('th');
    const tabela = botao.closest('table');
    const direcao = titulo.getAttribute('aria-sort') === 'ascending' ? 'desc' : 'asc';

    // Tabela paginada: recarrega a página pedindo ao servidor a nova ordem
    if (tabela.hasAttribute('data-ordem-servidor')) {
      const endereco = new URL(window.location.href);
      endereco.searchParams.set('ordem', botao.dataset.ordem);
      endereco.searchParams.set('dir', direcao);
      endereco.searchParams.delete('pagina');
      window.location.href = endereco.toString();
      return;
    }

    // Tabela simples: reordena as linhas aqui mesmo (as linhas vazias de enfeite ficam no fim)
    const corpo = tabela.tBodies[0];
    const linhas = Array.from(corpo.rows).filter((linha) => !linha.hasAttribute('aria-hidden'));
    const vazias = Array.from(corpo.rows).filter((linha) => linha.hasAttribute('aria-hidden'));
    const indice = titulo.cellIndex;
    linhas.sort((a, b) => chaveDeOrdenacao(a.cells[indice]).localeCompare(
      chaveDeOrdenacao(b.cells[indice]), 'pt-BR', { numeric: true, sensitivity: 'base' }) * (direcao === 'asc' ? 1 : -1));
    linhas.concat(vazias).forEach((linha) => corpo.append(linha));
    marcarOrdenacao(tabela, titulo, direcao);
    if (tabela.mostrarPagina) tabela.mostrarPagina(1);   // depois de ordenar, volta para a primeira página
  }

  // Clique no título de uma coluna ordena a tabela
  document.addEventListener('click', (evento) => {
    const botao = evento.target.closest('.tabela__ordem');
    if (botao) ordenarColuna(botao);
  });

  // Ao abrir a página, destaca a coluna que o servidor já ordenou
  const tabelaDoServidor = document.querySelector('table[data-ordem-servidor]');
  if (tabelaDoServidor) {
    const parametros = new URLSearchParams(window.location.search);
    const coluna = tabelaDoServidor.querySelector('.tabela__ordem[data-ordem="' + parametros.get('ordem') + '"]');
    if (coluna) marcarOrdenacao(tabelaDoServidor, coluna.closest('th'), parametros.get('dir'));
  }

  /* ---------- PAGINAÇÃO NA TELA: 8 linhas por vez nas tabelas que o servidor não pagina ---------- */
  // Usuários, condomínios, cooperativas e vínculos já vêm paginados do servidor (partes/paginacao.jsp).
  // As demais tabelas recebem todas as linhas; aqui elas são divididas em páginas de 8, com o mesmo visual.
  const LINHAS_POR_PAGINA = 8;

  function paginarNaTela(tabela) {
    const cartao = tabela.closest('.tabela-cartao');
    if (!cartao || cartao.querySelector('.paginacao')) return;   // já paginada pelo servidor
    const corpo = tabela.tBodies[0];
    const linhas = Array.from(corpo.rows).filter((linha) => !linha.hasAttribute('aria-hidden'));
    const totalPaginas = Math.ceil(linhas.length / LINHAS_POR_PAGINA);
    if (totalPaginas <= 1) return;

    // Linhas vazias de enfeite, para a última página também ter 8 linhas
    const colunas = tabela.tHead.rows[0].cells.length;
    const enfeites = [];
    for (let i = 1; i < LINHAS_POR_PAGINA; i++) {
      const enfeite = document.createElement('tr');
      enfeite.setAttribute('aria-hidden', 'true');
      enfeite.innerHTML = '<td><div class="celula-principal"><span class="tabela__selecionar"></span></div></td>' + '<td></td>'.repeat(colunas - 1);
      corpo.append(enfeite);
      enfeites.push(enfeite);
    }

    // Setas e números, iguais aos da paginação do servidor
    const icones = document.querySelector('img[src*="/assets/icones/"]').getAttribute('src').replace(/[^/]*$/, '');
    const nav = document.createElement('nav');
    nav.className = 'paginacao';
    nav.setAttribute('aria-label', 'Paginação');
    cartao.append(nav);
    let atual = 1;

    function desenharNav() {
      const link = (pagina, conteudo, rotulo) => '<a href="#" data-pagina="' + pagina + '"' + (rotulo ? ' aria-label="' + rotulo + '"' : '')
        + (pagina === atual && !rotulo ? ' aria-current="page"' : '') + '>' + conteudo + '</a>';
      let html = link(Math.max(atual - 1, 1), '<img src="' + icones + 'seta-esquerda.svg" alt="">', 'Página anterior');
      for (let n = 1; n <= Math.min(totalPaginas, 4); n++) html += link(n, n);
      if (totalPaginas > 4) html += '<span>...</span>' + link(totalPaginas, totalPaginas);
      html += link(Math.min(atual + 1, totalPaginas), '<img src="' + icones + 'seta-direita.svg" alt="">', 'Próxima página');
      nav.innerHTML = html;
    }

    tabela.mostrarPagina = (pagina) => {
      atual = Math.min(Math.max(pagina, 1), totalPaginas);
      // Mostra só as linhas da página; as demais ficam escondidas
      const atuais = Array.from(corpo.rows).filter((linha) => !linha.hasAttribute('aria-hidden'));
      const de = (atual - 1) * LINHAS_POR_PAGINA;
      atuais.forEach((linha, i) => { linha.hidden = i < de || i >= de + LINHAS_POR_PAGINA; });
      const faltam = LINHAS_POR_PAGINA - Math.min(LINHAS_POR_PAGINA, atuais.length - de);
      enfeites.forEach((enfeite, i) => { enfeite.hidden = i >= faltam; });
      // Linha marcada que ficou em outra página é desmarcada
      if (bolinhaSelecionada && bolinhaSelecionada.closest('tr').hidden) {
        bolinhaSelecionada.checked = false;
        bolinhaSelecionada = null;
        atualizarBotaoEditar();
      }
      desenharNav();
    };

    nav.addEventListener('click', (evento) => {
      const alvo = evento.target.closest('a[data-pagina]');
      if (!alvo) return;
      evento.preventDefault();
      tabela.mostrarPagina(Number(alvo.dataset.pagina));
    });
    tabela.mostrarPagina(1);
  }

  document.querySelectorAll('table.tabela').forEach(paginarNaTela);

  /* ---------- SENHA: mostrar/ocultar, confirmação e endereço opcional ---------- */
  // Clique no olho mostra ou esconde a senha do campo ao lado
  document.addEventListener('click', (evento) => {
    const olho = evento.target.closest('[data-mostrar-senha]');
    if (!olho) return;
    const campo = olho.parentElement.querySelector('input');
    const vaiMostrar = campo.type === 'password';
    campo.type = vaiMostrar ? 'text' : 'password';
    olho.setAttribute('aria-pressed', String(vaiMostrar));
    olho.setAttribute('aria-label', vaiMostrar ? 'Ocultar senha' : 'Mostrar senha');
  });

  // Confere se "Confirmar senha" é igual à senha e avisa no próprio campo
  function conferirSenhas(form) {
    const senha = form.querySelector('[name="senha"]');
    const confirmacao = form.querySelector('[name="confirma_senha"]');
    if (!senha || !confirmacao) return;
    confirmacao.required = senha.required || senha.value !== '';
    confirmacao.setCustomValidity(senha.value !== confirmacao.value ? 'As senhas não conferem.' : '');
  }

  // Endereço opcional: ou fica todo vazio ou vem completo (cep, cidade, estado, rua e número)
  function conferirEndereco(form) {
    const campos = ['cep', 'cidade', 'estado', 'rua', 'numero'].map((nome) => form.querySelector('[name="' + nome + '"]'));
    campos.forEach((campo) => campo.setCustomValidity(''));
    const preenchidos = campos.filter((campo) => campo.value.trim() !== '');
    if (preenchidos.length === 0 || preenchidos.length === campos.length) return;
    campos.find((campo) => campo.value.trim() === '')
      .setCustomValidity('Preencha o endereço completo ou deixe todos os campos de endereço vazios.');
  }

  // A cada letra digitada, confere de novo a senha e o endereço
  document.addEventListener('input', (evento) => {
    const form = evento.target.closest('form');
    if (!form) return;
    if (evento.target.name === 'senha' || evento.target.name === 'confirma_senha') conferirSenhas(form);
    if (form.hasAttribute('data-endereco-opcional')) conferirEndereco(form);
  });
  // Ao enviar o formulário, confere tudo e impede o envio se houver erro
  document.addEventListener('submit', (evento) => {
    conferirSenhas(evento.target);
    if (evento.target.hasAttribute('data-endereco-opcional')) conferirEndereco(evento.target);
    if (!evento.target.checkValidity()) {
      evento.preventDefault();
      evento.target.reportValidity();
    }
  }, true);

  /* ---------- 3. MODAIS: abrir e fechar ---------- */
  // Abre o modal pelo id e o deixa pronto (textos de editar, ficha e formulário)
  function abrirModal(idDoModal, modo, gatilho) {
    const modal = document.getElementById(idDoModal);
    if (!modal) return;

    if (idDoModal === 'modal-excluir' && !(gatilho && prepararExclusao(modal, gatilho))) return;
    if (idDoModal === 'modal-configuracoes') marcarTema(document.documentElement.dataset.tema);

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

  // Clique em um botão com data-abrir-modal abre o modal; botão de fechar ou clique fora fecham
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
            campo.dispatchEvent(new Event('change', { bubbles: true }));
          }
        });
      });
      const campoId = modal.querySelector('[data-chave-id]');
      if (campoId && reabrir.dataset.id) campoId.value = reabrir.dataset.id;
    }
  }

  /* ---------- 4. TEMA CLARO/ESCURO ---------- */
  const CHAVE_DO_TEMA = 'tema-ecociente';   // nome com que o tema fica salvo no navegador

  // Marca a opção Claro ou Escuro no modal de configurações
  function marcarTema(tema) {
    const opcao = document.querySelector(`input[name="tema"][value="${tema}"]`);
    if (opcao) opcao.checked = true;                        // marca Claro/Escuro nas configurações
  }

  // Aplica o tema na página e guarda a escolha no navegador
  function aplicarTema(tema) {
    document.documentElement.dataset.tema = tema;
    try { localStorage.setItem(CHAVE_DO_TEMA, tema); } catch (erro) { /* navegador sem armazenamento */ }
    marcarTema(tema);
  }

  // O tema só muda e só é salvo ao clicar em "Salvar alterações".
  // Cancelar, X, clique fora ou Esc fecham o modal e voltam a seleção para o tema que está valendo.
  const modalConfiguracoes = document.getElementById('modal-configuracoes');
  if (modalConfiguracoes) {
    modalConfiguracoes.querySelector('form').addEventListener('submit', () => {
      const escolhido = modalConfiguracoes.querySelector('input[name="tema"]:checked');
      if (escolhido) aplicarTema(escolhido.value);
    });
    modalConfiguracoes.addEventListener('close', () => marcarTema(document.documentElement.dataset.tema));
  }

  // Ao carregar a página, aplica o tema que a pessoa salvou (ou o claro)
  let temaSalvo = 'claro';
  try { temaSalvo = localStorage.getItem(CHAVE_DO_TEMA) || 'claro'; } catch (erro) { /* usa o tema claro */ }
  aplicarTema(temaSalvo);
})();
