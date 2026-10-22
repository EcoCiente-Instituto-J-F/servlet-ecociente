/* PAINEL (home) — atualiza os cards e os gráficos sozinhos*/

const areaDoPainel = document.getElementById('area-do-painel');
const INTERVALO_EM_MS = 30000;   // de quanto em quanto tempo atualiza

async function atualizarPainel() {
  if (document.hidden) return;
  try {
    const resposta = await fetch(areaDoPainel.dataset.url);
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

setInterval(atualizarPainel, INTERVALO_EM_MS);
