/* PAINEL (home) — atualiza os cards e os gráficos sozinhos*/

const areaDoPainel = document.getElementById('area-do-painel');
const INTERVALO_EM_MS = 30000;   // de quanto em quanto tempo atualiza

async function atualizarPainel() {
  if (document.hidden) return;
  try {
    const resposta = await fetch(areaDoPainel.dataset.url);
    if (!resposta.ok) return;
    areaDoPainel.innerHTML = await resposta.text();
  } catch (erro) {
  }
}

setInterval(atualizarPainel, INTERVALO_EM_MS);
