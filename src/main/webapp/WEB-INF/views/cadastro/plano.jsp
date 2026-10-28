<%@ page pageEncoding="UTF-8" %>
<%-- Etapa de escolha do plano, depois que a conta já foi criada --%>
<c:set var="tituloEtapa" value="Escolha seu plano" />
<c:set var="subtituloEtapa" value="Sua conta já está criada. Escolha o plano ideal para o seu condomínio." />
<%@ include file="_cabecalho.jsp" %>

<%-- O campo escondido "etapa" diz ao servidor qual etapa do cadastro está sendo enviada --%>
<form action="${ctx}/cadastro" method="POST">
    <input type="hidden" name="etapa" value="plano">

    <%-- Planos disponíveis --%>
    <div class="opcoes duas" role="radiogroup" aria-label="Plano">
        <label class="opcao">
            <input type="radio" name="plano" value="residencial" required ${cad.plano == 'residencial' ? 'checked' : ''}>
            <span class="opcao-check" aria-hidden="true"></span>
            <img class="opcao-icone" src="${ctx}/assets/icone-tipo-residencial.svg" alt="">
            <span class="opcao-titulo">Empresarial Residencial</span>
            <span class="opcao-desc">R$ 4,00 por apartamento / mês (R$ 3,40 no plano anual).</span>
        </label>
        <label class="opcao">
            <input type="radio" name="plano" value="industrial" ${cad.plano == 'industrial' ? 'checked' : ''}>
            <span class="opcao-check" aria-hidden="true"></span>
            <img class="opcao-icone" src="${ctx}/assets/icone-tipo-industrial.svg" alt="">
            <span class="opcao-titulo">Industrial</span>
            <span class="opcao-desc">R$ 0,50 por m² / mês (R$ 0,42 no plano anual).</span>
        </label>
    </div>

    <div class="botoes">
        <button type="submit" class="botao-submit">Continuar</button>
    </div>
</form>

<%-- Quem não quer assinar agora volta para a página inicial no plano gratuito --%>
<p class="link-entrar link-gratuito"><a href="${ctx}/inicio">Agora não, continuar no plano gratuito</a></p>