<%@ page pageEncoding="UTF-8" %>
<c:set var="rotuloEtapa" value="Etapa 1 de 2 · Plano" />
<c:set var="tituloEtapa" value="Resumo do plano" />
<c:set var="subtituloEtapa" value="Confira o plano escolhido antes de seguir para o pagamento." />
<%@ include file="_cabecalho.jsp" %>

<form action="${ctx}/cadastro" method="POST">
    <input type="hidden" name="etapa" value="pag-resumo">
    <button type="submit" name="acao" value="recalcular" class="so-leitor" tabindex="-1" aria-hidden="true">Recalcular</button>

    <div class="plano-escolhido">
        <img src="${ctx}/assets/${orc.plano.icone}" alt="" class="plano-icone">
        <div class="plano-info">
            <span class="rotulo-mini">Plano escolhido</span>
            <strong><c:out value="${orc.plano.nome}" /></strong>
            <span><c:out value="${orc.plano.descricao}" /></span>
        </div>
    </div>

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
