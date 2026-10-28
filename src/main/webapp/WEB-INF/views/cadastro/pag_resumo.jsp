<%@ page pageEncoding="UTF-8" %>
<%-- Pagamento (etapa 1 de 2): resumo do plano, ciclo de cobrança e quantidade --%>
<c:set var="rotuloEtapa" value="Etapa 1 de 2 · Plano" />
<c:set var="tituloEtapa" value="Resumo do plano" />
<c:set var="subtituloEtapa" value="Confira o plano escolhido antes de seguir para o pagamento." />
<%@ include file="_cabecalho.jsp" %>

<%-- O campo escondido "etapa" diz ao servidor qual etapa do cadastro está sendo enviada --%>
<form action="${ctx}/cadastro" method="POST">
    <input type="hidden" name="etapa" value="pag-resumo">
    <button type="submit" name="acao" value="recalcular" class="so-leitor" tabindex="-1" aria-hidden="true">Recalcular</button>

    <%-- Plano escolhido --%>
    <div class="plano-escolhido">
        <img src="${ctx}/assets/${orc.plano.icone}" alt="" class="plano-icone">
        <div class="plano-info">
            <span class="rotulo-mini">Plano escolhido</span>
            <strong><c:out value="${orc.plano.nome}" /></strong>
            <span><c:out value="${orc.plano.descricao}" /></span>
        </div>
    </div>

    <%-- Alterna entre cobrança mensal e anual (recalcula o valor) --%>
    <fieldset class="ciclo">
        <legend>Ciclo de cobrança</legend>
        <div class="seletor-ciclo">
            <button type="submit" name="acao" value="ciclo-mensal" formnovalidate class="${orc.anual ? '' : 'ativo'}" aria-pressed="${not orc.anual}">Mensal</button>
            <button type="submit" name="acao" value="ciclo-anual" formnovalidate class="${orc.anual ? 'ativo' : ''}" aria-pressed="${orc.anual}">Anual</button>
        </div>
    </fieldset>

    <div class="quantidade">
        <div>
            <label for="cad-quantidade"><c:out value="${orc.plano.rotuloQuantidade}" /></label>
            <span class="dica-campo" id="dica-quantidade"><c:out value="${orc.precoUnitario}" /></span>
        </div>
        <div class="quantidade-campo">
            <div class="stepper">
                <button type="submit" name="acao" value="menos" aria-label="Diminuir" formnovalidate>
                    <img src="${ctx}/assets/icone-menos.svg" width="14" height="2" alt="">
                </button>
                <input type="text" id="cad-quantidade" name="quantidade" value="${orc.quantidadeFormatada}" inputmode="numeric" required pattern="[0-9.]{1,10}" title="Digite somente números" aria-describedby="dica-quantidade">
                <button type="submit" name="acao" value="mais" aria-label="Aumentar" formnovalidate>
                    <img src="${ctx}/assets/icone-mais.svg" width="14" height="14" alt="">
                </button>
            </div>
            <button type="submit" name="acao" value="recalcular" class="atualizar" formnovalidate>Atualizar valor</button>
        </div>
    </div>

    <hr class="divisor-resumo">

    <div class="total">
        <div>
            <strong><c:out value="${orc.rotuloTotal}" /></strong>
            <span><c:out value="${orc.detalhe}" /></span>
        </div>
        <span class="valor-total"><c:out value="${orc.total}" /></span>
    </div>

    <%-- Só no plano anual: mostra como o valor foi calculado e quanto a pessoa economiza --%>
    <c:if test="${orc.anual}">
        <div class="calculo-anual">
            <p class="calculo-titulo">Como chegamos nesse valor</p>
            <dl>
                <div><dt>Plano mensal</dt><dd><c:out value="${orc.calculoMensal}" /></dd></div>
                <div><dt>Plano anual</dt><dd><c:out value="${orc.calculoAnualMes}" /></dd></div>
                <div class="calculo-total"><dt>Total do ano</dt><dd><c:out value="${orc.calculoAnualAno}" /></dd></div>
            </dl>
            <p class="economia">Você economiza <strong><c:out value="${orc.economia}" /></strong> por ano (<c:out value="${orc.economiaPercentual}" />) em relação ao plano mensal.</p>
        </div>
    </c:if>

    <c:set var="textoAvancar" value="Ir para o pagamento" />
    <%@ include file="_botoes.jsp" %>
</form>

<%-- Atualiza só o valor ao mexer na quantidade ou no ciclo, sem recarregar a página.
     O servidor devolve a etapa inteira; o script troca apenas os blocos que mudam.
     "Ir para o pagamento" e "Voltar" não estão em ACOES, então seguem o envio normal. --%>
<script>
(function () {
    var form = document.querySelector('input[name="etapa"][value="pag-resumo"]').form;
    var ACOES = ['recalcular', 'ciclo-mensal', 'ciclo-anual', 'mais', 'menos'];
    var BLOCOS = ['.seletor-ciclo', '.quantidade', '.total', '.calculo-anual'];

    // Envio normal que preserva a ação clicada (form.submit() não envia o botão)
    function enviarNormal(acao) {
        var campo = document.createElement('input');
        campo.type = 'hidden';
        campo.name = 'acao';
        campo.value = acao;
        form.appendChild(campo);
        form.submit();
    }

    // Devolve o foco ao controle usado, já que o bloco original foi trocado
    function restaurarFoco(botao) {
        var alvo = botao.classList.contains('so-leitor')
            ? form.querySelector('#cad-quantidade')
            : form.querySelector('button[name="acao"][value="' + botao.value + '"]:not(.so-leitor)');
        if (alvo) alvo.focus();
    }

    form.addEventListener('submit', function (e) {
        var botao = e.submitter;
        if (!botao || ACOES.indexOf(botao.value) === -1 || botao.name !== 'acao') return;

        e.preventDefault();
        if (form.getAttribute('aria-busy') === 'true') return;

        var acao = botao.value;
        var dados = new URLSearchParams(new FormData(form));
        dados.set('acao', acao);

        form.setAttribute('aria-busy', 'true');
        fetch(form.action, {
            method: 'POST',
            body: dados,
            credentials: 'same-origin'
        })
        .then(function (r) { return r.text(); })
        .then(function (html) {
            var doc = new DOMParser().parseFromString(html, 'text/html');
            var campoEtapa = doc.querySelector('input[name="etapa"][value="pag-resumo"]');
            if (!campoEtapa) { enviarNormal(acao); return; } // resposta inesperada (ex.: sessão expirou)
            var formNovo = campoEtapa.form;

            BLOCOS.forEach(function (seletor) {
                var novo = formNovo.querySelector(seletor);
                var atual = form.querySelector(seletor);
                if (novo && atual) atual.replaceWith(novo);
                else if (novo) form.querySelector('.total').insertAdjacentElement('afterend', novo); // .calculo-anual aparece no ciclo anual
                else if (atual) atual.remove(); // .calculo-anual some no ciclo mensal
            });

            // Mensagem de erro (ex.: quantidade fora do limite): fica logo acima do formulário
            var erroAtual = form.parentNode.querySelector('.mensagem-erro');
            var erroNovo = formNovo.parentNode.querySelector('.mensagem-erro');
            if (erroAtual) erroAtual.remove();
            if (erroNovo) form.insertAdjacentElement('beforebegin', erroNovo);

            restaurarFoco(botao);
        })
        .catch(function () { enviarNormal(acao); }) // sem rede: cai no envio normal
        .finally(function () { form.removeAttribute('aria-busy'); });
    });
})();
</script>
