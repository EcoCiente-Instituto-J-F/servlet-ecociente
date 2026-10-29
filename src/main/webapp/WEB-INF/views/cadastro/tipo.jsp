<%@ page pageEncoding="UTF-8" %>
<%-- Etapa do síndico: tipo do condomínio (residencial/comercial ou industrial) --%>
<c:set var="tituloEtapa" value="Tipo de condomínio" />
<c:set var="subtituloEtapa" value="Selecione o tipo do seu condomínio." />
<%@ include file="_cabecalho.jsp" %>

<%-- O campo escondido "etapa" diz ao servidor qual etapa do cadastro está sendo enviada --%>
<form action="${ctx}/cadastro" method="POST">
    <input type="hidden" name="etapa" value="tipo">

    <%-- Opções em cartões; a que a pessoa já escolheu antes vem marcada --%>
    <div class="opcoes duas" role="radiogroup" aria-label="Tipo de condomínio">
        <label class="opcao">
            <input type="radio" name="tipoCondominio" value="residencial-comercial" required ${cad.tipoCondominio == 'residencial-comercial' ? 'checked' : ''}>
            <span class="opcao-check" aria-hidden="true"></span>
            <img class="opcao-icone" src="${ctx}/assets/icone-tipo-comercial.svg" alt="">
            <span class="opcao-titulo">Residencial / Comercial</span>
            <span class="opcao-desc">Prédios, conjuntos, salas, escritórios e lojas.</span>
        </label>
        <label class="opcao">
            <input type="radio" name="tipoCondominio" value="industrial" ${cad.tipoCondominio == 'industrial' ? 'checked' : ''}>
            <span class="opcao-check" aria-hidden="true"></span>
            <img class="opcao-icone" src="${ctx}/assets/icone-tipo-industrial.svg" alt="">
            <span class="opcao-titulo">Industrial</span>
            <span class="opcao-desc">Galpões e polos industriais.</span>
        </label>
    </div>

    <c:set var="textoAvancar" value="Próximo" />
    <%@ include file="_botoes.jsp" %>
</form>
