// Tela de login do CRUD: botão do olho mostra/esconde a senha
(function () {
  // Botão do olho (se a página não tiver, o código não faz nada)
  const botao = document.querySelector("[data-mostrar-senha]");
  if (!botao) return;

  const campo = document.getElementById(botao.getAttribute("data-mostrar-senha"));

  // Alterna entre senha escondida e senha visível
  botao.addEventListener("click", function () {
    const vaiMostrar = campo.type === "password";
    campo.type = vaiMostrar ? "text" : "password";
    botao.setAttribute("aria-pressed", String(vaiMostrar));
    botao.setAttribute("aria-label", vaiMostrar ? "Ocultar senha" : "Mostrar senha");
  });
})();
