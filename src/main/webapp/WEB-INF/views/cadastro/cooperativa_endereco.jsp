<%@ page pageEncoding="UTF-8" %>
<c:set var="tituloEtapa" value="Endereço" />
<c:set var="subtituloEtapa" value="Torne sua cooperativa parceira. Informe o CNPJ e o endereço da sede." />
<%@ include file="_cabecalho.jsp" %>
<c:catch var="semNomeCooperativa"><c:set var="valorNomeCooperativa" value="${cad.nomeCooperativa}" /></c:catch>

<form action="${ctx}/cadastro" method="POST">
    <input type="hidden" name="etapa" value="cooperativa-endereco">

    <div class="form-group">
        <label for="cad-nome-cooperativa">Nome da cooperativa</label>
        <div class="input-wrapper">
            <input class="input-field" type="text" id="cad-nome-cooperativa" name="nomeCooperativa" value="<c:out value='${valorNomeCooperativa}' />" placeholder="Digite o nome da cooperativa" autocomplete="organization" required maxlength="100">
        </div>
    </div>

    <div class="form-row tres">
        <div class="form-group" data-erro="CNPJ incompleto: digite os 14 números, ex.: 00.000.000/0000-00.">
            <label for="cad-cnpj">CNPJ</label>
            <div class="input-wrapper">
                <input class="input-field" type="text" id="cad-cnpj" name="cnpj" value="<c:out value='${cad.cnpj}' />" placeholder="00.000.000/0000-00" inputmode="numeric" required pattern="[0-9]{2}\.?[0-9]{3}\.?[0-9]{3}/?[0-9]{4}-?[0-9]{2}" title="Use o formato 00.000.000/0000-00">
            </div>
        </div>
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

    <div class="form-row rua-numero">
        <div class="form-group">
            <label for="cad-rua">Nome da rua</label>
            <div class="input-wrapper">
                <input class="input-field" type="text" id="cad-rua" name="rua" value="<c:out value='${cad.rua}' />" placeholder="Digite o nome da rua" autocomplete="address-line1" required maxlength="150">
            </div>
        </div>
        <div class="form-group">
            <label for="cad-numero">Número</label>
            <div class="input-wrapper">
                <input class="input-field" type="text" id="cad-numero" name="numero" value="<c:out value='${cad.numero}' />" placeholder="Nº" required maxlength="10">
            </div>
        </div>
    </div>

    <div class="form-group">
        <label for="cad-complemento">Complemento <span class="opcional">(opcional)</span></label>
        <div class="input-wrapper">
            <input class="input-field" type="text" id="cad-complemento" name="complemento" value="<c:out value='${cad.complemento}' />" placeholder="Sala, galpão..." autocomplete="address-line2" maxlength="80">
        </div>
    </div>

    <c:set var="textoAvancar" value="Próximo" />
    <%@ include file="_botoes.jsp" %>
</form>
