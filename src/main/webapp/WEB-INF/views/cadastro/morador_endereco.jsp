<%@ page pageEncoding="UTF-8" %>
<%-- Etapa do morador em casa: endereço da moradia --%>
<c:set var="tituloEtapa" value="Sua moradia" />
<c:set var="subtituloEtapa" value="Informe onde você mora para acompanharmos a coleta da sua região." />
<%@ include file="_cabecalho.jsp" %>

<%-- O campo escondido "etapa" diz ao servidor qual etapa do cadastro está sendo enviada --%>
<form action="${ctx}/cadastro" method="POST">
    <input type="hidden" name="etapa" value="morador-endereco">

    <div class="form-row">
        <div class="form-group" data-erro="CEP incompleto: digite os 8 números, ex.: 01310-100.">
            <label for="cad-cep">CEP</label>
            <div class="input-wrapper">
                <input class="input-field" type="text" id="cad-cep" name="cep" value="<c:out value='${cad.cep}' />" placeholder="00000-000" inputmode="numeric" autocomplete="postal-code" required pattern="[0-9]{5}-?[0-9]{3}" title="Use o formato 00000-000">
            </div>
        </div>
        <div class="form-group" data-erro="Selecione o estado.">
            <label for="cad-uf">Estado</label>
            <div class="input-wrapper">
                <select class="input-field" id="cad-uf" name="uf" required>
                    <c:set var="ufSelecionada" value="${cad.uf}" />
                    <%@ include file="_uf.jsp" %>
                </select>
            </div>
        </div>
    </div>

    <div class="form-row">
        <div class="form-group">
            <label for="cad-cidade">Cidade</label>
            <div class="input-wrapper">
                <input class="input-field" type="text" id="cad-cidade" name="cidade" value="<c:out value='${cad.cidade}' />" placeholder="Digite a cidade" autocomplete="address-level2" required maxlength="80">
            </div>
        </div>
        <div class="form-group">
            <label for="cad-bairro">Bairro</label>
            <div class="input-wrapper">
                <input class="input-field" type="text" id="cad-bairro" name="bairro" value="<c:out value='${cad.bairro}' />" placeholder="Digite o bairro" required maxlength="80">
            </div>
        </div>
    </div>

    <%-- Rua e número --%>
    <div class="form-row rua-numero">
        <div class="form-group">
            <label for="cad-rua">Nome da rua</label>
            <div class="input-wrapper">
                <input class="input-field" type="text" id="cad-rua" name="rua" value="<c:out value='${cad.rua}' />" placeholder="Digite o nome da rua" autocomplete="address-line1" required maxlength="150">
            </div>
        </div>
        <div class="form-group" data-erro="Digite só números. Se não houver número, use 0.">
            <label for="cad-numero">Número</label>
            <div class="input-wrapper">
                <input class="input-field" type="text" id="cad-numero" name="numero" value="<c:out value='${cad.numero}' />" placeholder="Nº" inputmode="numeric" pattern="[0-9]{1,9}" title="Somente números" required maxlength="9">
            </div>
        </div>
    </div>

    <div class="form-group">
        <label for="cad-complemento">Complemento <span class="opcional">(opcional)</span></label>
        <div class="input-wrapper">
            <input class="input-field" type="text" id="cad-complemento" name="complemento" value="<c:out value='${cad.complemento}' />" placeholder="Casa, comércios..." autocomplete="address-line2" maxlength="80">
        </div>
    </div>

    <c:set var="textoAvancar" value="Criar conta" />
    <%@ include file="_botoes.jsp" %>
</form>
