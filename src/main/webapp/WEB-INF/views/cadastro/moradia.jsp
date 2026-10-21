<%@ page pageEncoding="UTF-8" %>
<c:set var="tituloEtapa" value="Moradia" />
<c:set var="subtituloEtapa" value="Você faz parte de um condomínio?" />
<%@ include file="_cabecalho.jsp" %>

<form action="${ctx}/cadastro" method="POST">
    <input type="hidden" name="etapa" value="moradia">

    <div class="opcoes duas" role="radiogroup" aria-label="Você faz parte de um condomínio?">
        <label class="opcao">
            <input type="radio" name="moradia" value="condominio" required ${cad.moradia == 'condominio' ? 'checked' : ''}>
            <span class="opcao-check" aria-hidden="true"></span>
            <img class="opcao-icone" src="${ctx}/assets/icone-tipo-residencial.svg" alt="">
            <span class="opcao-titulo">Sim, moro em condomínio</span>
            <span class="opcao-desc">Você vai precisar do token enviado pelo síndico.</span>
        </label>
        <label class="opcao">
            <input type="radio" name="moradia" value="casa" ${cad.moradia == 'casa' ? 'checked' : ''}>
            <span class="opcao-check" aria-hidden="true"></span>
            <img class="opcao-icone" src="${ctx}/assets/icone-moradia-casa.svg" alt="">
            <span class="opcao-titulo">Não, moro em casa</span>
            <span class="opcao-desc">Sua conta é criada em seguida.</span>
        </label>
    </div>

    <c:set var="textoAvancar" value="Próximo" />
    <%@ include file="_botoes.jsp" %>
</form>
