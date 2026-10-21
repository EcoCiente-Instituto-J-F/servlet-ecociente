<%@ page pageEncoding="UTF-8" %>
<c:set var="tituloEtapa" value="Dados pessoais" />
<c:set var="subtituloEtapa" value="Precisamos de alguns dados para emitir a cobrança no seu nome." />
<%@ include file="_cabecalho.jsp" %>

<form action="${ctx}/cadastro" method="POST">
    <input type="hidden" name="etapa" value="pag-dados">

    <div class="form-group" data-erro="Digite o nome completo, com nome e sobrenome.">
        <label for="cad-nome">Nome completo</label>
        <div class="input-wrapper">
            <input class="input-field" type="text" id="cad-nome" name="nome" value="<c:out value='${cad.nome}' />" pattern="\S+(\s+\S+)+" title="Digite nome e sobrenome" placeholder="Digite seu nome completo" autocomplete="name" required minlength="3" maxlength="120">
        </div>
    </div>

    <div class="form-row">
        <div class="form-group" data-erro="Telefone incompleto: digite o DDD e o número, ex.: (11) 98765-4321.">
            <label for="cad-telefone">Telefone</label>
            <div class="input-wrapper">
                <input class="input-field" type="tel" id="cad-telefone" name="telefone" value="<c:out value='${cad.telefone}' />" placeholder="(00) 00000-0000" autocomplete="tel-national" inputmode="tel" required pattern="\(?[0-9]{2}\)?\s?[0-9]{4,5}-?[0-9]{4}" title="Telefone com DDD">
            </div>
        </div>
        <div class="form-group" data-erro="CPF ou CNPJ incompleto: o CPF tem 11 números e o CNPJ tem 14.">
            <label for="cad-documento">CPF ou CNPJ</label>
            <div class="input-wrapper">
                <input class="input-field" type="text" id="cad-documento" name="documento" value="<c:out value='${cad.documento}' />" placeholder="000.000.000-00" inputmode="numeric" required pattern="[0-9]{3}\.?[0-9]{3}\.?[0-9]{3}-?[0-9]{2}|[0-9]{2}\.?[0-9]{3}\.?[0-9]{3}/?[0-9]{4}-?[0-9]{2}" title="CPF (000.000.000-00) ou CNPJ (00.000.000/0000-00)">
            </div>
        </div>
    </div>

    <c:set var="textoLgpd" value="Usamos essas informações apenas para identificar sua conta e emitir a nota fiscal, conforme a LGPD." />
    <%@ include file="_aceite.jsp" %>

    <c:set var="textoAvancar" value="Continuar" />
    <c:set var="semVoltar" value="true" />
    <%@ include file="_botoes.jsp" %>
</form>
