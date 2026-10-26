<%@ page pageEncoding="UTF-8" %>
<c:set var="tituloEtapa" value="Token do condomínio" />
<c:set var="subtituloEtapa" value="Seu condomínio foi cadastrado! Este é o token dele. Compartilhe com quem for se vincular ao condomínio." />
<%@ include file="_cabecalho.jsp" %>

<form action="${ctx}/cadastro" method="POST">
    <input type="hidden" name="etapa" value="sindico-token">

    <div class="form-group">
        <label for="cad-token-sindico">Código de acesso</label>
        <input class="campo-token" type="text" id="cad-token-sindico" value="<c:out value='${cad.tokenCondominio}' />" readonly spellcheck="false">
    </div>

    <c:set var="textoAvancar" value="Concluir cadastro" />
    <c:set var="semVoltar" value="true" />
    <%@ include file="_botoes.jsp" %>
</form>