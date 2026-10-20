<%@ page pageEncoding="UTF-8" %>
<c:set var="tituloEtapa" value="Token do condomínio" />
<c:set var="subtituloEtapa" value="Insira o token de acesso enviado pelo síndico." />
<%@ include file="_cabecalho.jsp" %>

<form action="${ctx}/cadastro" method="POST">
    <input type="hidden" name="etapa" value="token">

    <div class="form-group" data-erro="Faltam caracteres: o token tem 6 letras ou números, sem espaços ou símbolos.">
        <label for="cad-token">Código de acesso</label>
        <input class="campo-token" type="text" id="cad-token" name="token" value="<c:out value='${cad.token}' />" required minlength="6" maxlength="6" pattern="[A-Za-z0-9]{6}" title="6 letras ou números" autocomplete="one-time-code" autocapitalize="characters" spellcheck="false" aria-describedby="token-dica">
    </div>

    <div class="token-ajuda">
        <span id="token-dica">O token tem 6 caracteres, entre letras e números.</span>
        <span>Não tem o token? <a href="#">Fale com o síndico</a></span>
    </div>

    <div class="info-token" role="note">
        <strong>Por que pedimos o token?</strong>
        Ele vincula sua conta ao seu condomínio, para você acompanhar a coleta seletiva e os resultados do seu prédio.
    </div>

    <div class="botoes coluna">
        <button type="submit" class="botao-submit">Validar token</button>
        <button type="submit" name="acao" value="voltar" class="botao-submit botao-secundario" formnovalidate>Voltar</button>
    </div>
</form>
