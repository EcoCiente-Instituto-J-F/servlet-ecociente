<%@ page pageEncoding="UTF-8" %>
<c:set var="tipo" value="${cad.tipoCondominio}" />
<c:set var="tituloEtapa" value="Dados do condomínio" />
<c:set var="subtituloEtapa" value="Conte um pouco sobre o seu condomínio ${tipo}." />
<%@ include file="_cabecalho.jsp" %>
<c:catch var="semUnidades"><c:set var="valorUnidades" value="${cad.numeroUnidades}" /></c:catch>
<c:catch var="semArea"><c:set var="valorArea" value="${cad.areaM2}" /></c:catch>

<form action="${ctx}/cadastro" method="POST">
    <input type="hidden" name="etapa" value="condominio">

    <c:choose>
        <c:when test="${tipo == 'residencial'}">
            <div class="form-group">
                <label for="cad-nome-cond">Nome do condomínio</label>
                <div class="input-wrapper">
                    <input class="input-field" type="text" id="cad-nome-cond" name="nomeCondominio" value="<c:out value='${cad.nomeCondominio}' />" placeholder="Digite o nome do condomínio" required maxlength="120">
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="form-group">
                <label for="cad-razao">Razão social</label>
                <div class="input-wrapper">
                    <input class="input-field" type="text" id="cad-razao" name="razaoSocial" value="<c:out value='${cad.razaoSocial}' />" placeholder="Digite a razão social" autocomplete="organization" required maxlength="150">
                </div>
            </div>
        </c:otherwise>
    </c:choose>

    <div class="form-row">
        <div class="form-group" data-erro="CNPJ incompleto: digite os 14 números, ex.: 00.000.000/0000-00.">
            <label for="cad-cnpj">CNPJ</label>
            <div class="input-wrapper">
                <input class="input-field" type="text" id="cad-cnpj" name="cnpj" value="<c:out value='${cad.cnpj}' />" placeholder="00.000.000/0000-00" inputmode="numeric" required pattern="[0-9]{2}\.?[0-9]{3}\.?[0-9]{3}/?[0-9]{4}-?[0-9]{2}" title="Use o formato 00.000.000/0000-00">
            </div>
        </div>

        <c:choose>
            <c:when test="${tipo == 'industrial'}">
                <div class="form-group">
                    <label for="cad-responsavel">Responsável ambiental</label>
                    <div class="input-wrapper">
                        <input class="input-field" type="text" id="cad-responsavel" name="responsavel" value="<c:out value='${cad.responsavel}' />" placeholder="Digite o nome do responsável" autocomplete="name" required maxlength="120">
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="form-group" data-erro="Telefone incompleto: digite o DDD e o número, ex.: (11) 98765-4321.">
                    <label for="cad-tel-cond">${tipo == 'comercial' ? 'Telefone comercial' : 'Telefone'}</label>
                    <div class="input-wrapper">
                        <input class="input-field" type="tel" id="cad-tel-cond" name="telefone" value="<c:out value='${cad.telefone}' />" placeholder="${tipo == 'comercial' ? '(00) 0000-0000' : '(00) 00000-0000'}" autocomplete="tel-national" inputmode="tel" required pattern="\(?[0-9]{2}\)?\s?[0-9]{4,5}-?[0-9]{4}" title="Telefone com DDD">
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <c:choose>
        <c:when test="${tipo == 'residencial'}">
            <div class="form-group">
                <label for="cad-responsavel">Síndico(a) responsável</label>
                <div class="input-wrapper">
                    <input class="input-field" type="text" id="cad-responsavel" name="responsavel" value="<c:out value='${cad.responsavel}' />" placeholder="Digite o nome do síndico(a)" autocomplete="name" required maxlength="120">
                </div>
            </div>
        </c:when>
        <c:when test="${tipo == 'comercial'}">
            <div class="form-group">
                <label for="cad-responsavel">Administrador(a) responsável</label>
                <div class="input-wrapper">
                    <input class="input-field" type="text" id="cad-responsavel" name="responsavel" value="<c:out value='${cad.responsavel}' />" placeholder="Digite o nome do responsável" autocomplete="name" required maxlength="120">
                </div>
            </div>
        </c:when>
    </c:choose>

    <c:choose>
        <c:when test="${tipo == 'industrial'}">
            <div class="form-group">
                <label for="cad-area">Área do empreendimento (m²)</label>
                <div class="input-wrapper">
                    <input class="input-field" type="number" id="cad-area" name="areaM2" value="<c:out value='${valorArea}' />" placeholder="Ex.: 1500.00" inputmode="decimal" step="0.01" min="0.01" required>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="form-group">
                <label for="cad-unidades">Número de unidades</label>
                <div class="input-wrapper">
                    <input class="input-field" type="number" id="cad-unidades" name="numeroUnidades" value="<c:out value='${valorUnidades}' />" placeholder="Ex.: 120" inputmode="numeric" min="1" required>
                </div>
            </div>
        </c:otherwise>
    </c:choose>

    <c:set var="textoAvancar" value="Próximo" />
    <%@ include file="_botoes.jsp" %>
</form>
