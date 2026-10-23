<%@ page pageEncoding="UTF-8" %>
<c:set var="ehCooperativa" value="${cad.perfil == 'cooperativa'}" />
<c:set var="ehSindico" value="${cad.perfil == 'sindico'}" />
<c:set var="tituloEtapa" value="Dados pessoais" />
<c:set var="subtituloEtapa"
    value="${ehCooperativa
        ? 'Dados do responsável pela cooperativa.'
        : (ehSindico
            ? 'Dados do síndico responsável pelo condomínio.'
            : 'Deixe seu contato para receber os avisos de coleta do seu bairro.')}" />
<%@ include file="_cabecalho.jsp" %>

<form action="${ctx}/cadastro" method="POST">
    <input type="hidden" name="etapa"
        value="${ehCooperativa
            ? 'cooperativa-dados'
            : (ehSindico ? 'sindico-dados' : 'morador-dados')}">

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
                <input class="input-field" type="tel" id="cad-telefone" name="telefone" value="<c:out value='${cad.telefone}' />" placeholder="(00) 00000-0000" autocomplete="tel-national" inputmode="tel" required pattern="\(?[0-9]{2}\)?\s?[0-9]{4,5}-?[0-9]{4}" title="Use o formato (00) 00000-0000">
            </div>
        </div>
        <div class="form-group" data-erro="CPF incompleto: digite os 11 números, ex.: 000.000.000-00.">
            <label for="cad-cpf">CPF</label>
            <div class="input-wrapper">
                <input class="input-field" type="text" id="cad-cpf" name="cpf" value="<c:out value='${cad.cpf}' />" placeholder="000.000.000-00" inputmode="numeric" required pattern="[0-9]{3}\.?[0-9]{3}\.?[0-9]{3}-?[0-9]{2}" title="Use o formato 000.000.000-00">
            </div>
        </div>
    </div>

    <%@ include file="_nascimento.jsp" %>

    <%@ include file="_avisos.jsp" %>

    <c:set var="textoLgpd" value="${ehCooperativa ? 'Usamos esses dados apenas para validar a cooperativa, conforme a LGPD.' : 'Usamos telefone e CPF apenas para identificar sua conta, conforme a LGPD.'}" />
    <%@ include file="_aceite.jsp" %>

    <c:set var="textoAvancar" value="Próximo" />
    <%@ include file="_botoes.jsp" %>
</form>
