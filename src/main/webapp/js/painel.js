/* PAINEL (home) — atualiza os cards e os gráficos sozinhos*/

const areaDoPainel = document.getElementById('area-do-painel');
const INTERVALO_EM_MS = 30000;   // de quanto em quanto tempo atualiza
let anoEscolhido = null;         // ano do seletor do gráfico (null = o atual)

async function atualizarPainel(forcar) {
  if (document.hidden && !forcar) return;
  try {
    const endereco = areaDoPainel.dataset.url + (anoEscolhido ? '?ano=' + encodeURIComponent(anoEscolhido) : '');
    const resposta = await fetch(endereco);
    // Sessão expirada: o filtro redireciona para o login (ou responde 401/403)
    if (resposta.redirected || resposta.status === 401 || resposta.status === 403) {
      location.reload();
      return;
    }
    if (!resposta.ok) return;
    areaDoPainel.innerHTML = await resposta.text();
  } catch (erro) {
  }
}

// O seletor de ano fica dentro da área que é recarregada, por isso o evento é escutado na área
areaDoPainel.addEventListener('change', (evento) => {
  if (evento.target.id !== 'seletor-ano') return;
  anoEscolhido = evento.target.value;
  atualizarPainel(true);
});

setInterval(() => atualizarPainel(false), INTERVALO_EM_MS);
