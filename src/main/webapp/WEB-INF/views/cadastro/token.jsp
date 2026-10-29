<%@ page pageEncoding="UTF-8" %>
<%-- Etapa do morador em condomínio: código do condomínio, bloco e apartamento (todos opcionais) --%>
<c:set var="tituloEtapa" value="Token e moradia" />
<c:set var="subtituloEtapa" value="Se o seu condomínio usa o EcoCiente, informe o código enviado pelo síndico. Se não usa, deixe em branco." />
<%@ include file="_cabecalho.jsp" %>

<%-- O campo escondido "etapa" diz ao servidor qual etapa do cadastro está sendo enviada --%>
<form action="${ctx}/cadastro" method="POST">
    <input type="hidden" name="etapa" value="token">

    <div class="form-group" data-erro="Faltam caracteres: o token tem 6 letras ou números, sem espaços ou símbolos.">
        <label for="cad-token">Código de acesso <span class="opcional">(opcional)</span></label>
        <input class="campo-token" type="text" id="cad-token" name="token" value="<c:out value='${cad.token}' />" minlength="6" maxlength="6" pattern="[A-Za-z0-9]{6}" title="6 letras ou números" autocomplete="one-time-code" autocapitalize="characters" spellcheck="false" aria-describedby="token-dica">
    </div>

    <div class="token-ajuda">
        <span id="token-dica">O token tem 6 caracteres, entre letras e números.</span>
        <span>Seu condomínio não usa o EcoCiente? Deixe o código em branco e continue.</span>
    </div>

    <div class="form-row">
        <div class="form-group">
            <label for="cad-bloco">Bloco/Torre <span class="opcional">(opcional)</span></label>
            <div class="input-wrapper">
                <input class="input-field" type="text" id="cad-bloco" name="bloco" value="<c:out value='${cad.bloco}' />" placeholder="Opcional" maxlength="30">
            </div>
        </div>
        <div class="form-group">
            <label for="cad-apto">Apartamento <span class="opcional">(opcional)</span></label>
            <div class="input-wrapper">
                <input class="input-field" type="text" id="cad-apto" name="apto" value="<c:out value='${cad.apto}' />" placeholder="Nº" maxlength="10">
            </div>
        </div>
    </div>

    <%-- Explica ao morador por que o token é pedido --%>
    <div class="info-token" role="note">
        <strong>Por que pedimos o token?</strong>
        Ele vincula sua conta ao seu condomínio, para você acompanhar a coleta seletiva e os resultados do seu prédio. Sem o código, você cria a conta normalmente e liga ao condomínio depois.
    </div>

    <div class="botoes coluna">
        <button type="submit" class="botao-submit">Continuar</button>
        <button type="submit" name="acao" value="voltar" class="botao-submit botao-secundario" formnovalidate>Voltar</button>
    </div>
</form>
