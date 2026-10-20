<%@ page pageEncoding="UTF-8" %>
<c:set var="tipo" value="${cad.tipoCondominio}" />
<c:set var="tituloEtapa" value="Dados do condomínio" />
<c:set var="subtituloEtapa" value="Conte um pouco sobre o seu condomínio ${tipo}." />
<%@ include file="_cabecalho.jsp" %>

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
        <c:when test="${tipo == 'residencial'}">
            <c:set var="q1n" value="unidades" /><c:set var="q1l" value="Unidades" /><c:set var="q1p" value="Nº" />
            <c:set var="q2n" value="blocos" /><c:set var="q2l" value="Blocos" /><c:set var="q2p" value="Nº" />
            <c:set var="q3n" value="moradores" /><c:set var="q3l" value="Moradores" /><c:set var="q3p" value="Aprox." />
        </c:when>
        <c:when test="${tipo == 'comercial'}">
            <c:set var="q1n" value="salas" /><c:set var="q1l" value="Salas" /><c:set var="q1p" value="Nº" />
            <c:set var="q2n" value="andares" /><c:set var="q2l" value="Andares" /><c:set var="q2p" value="Nº" />
            <c:set var="q3n" value="empresas" /><c:set var="q3l" value="Empresas" /><c:set var="q3p" value="Nº" />
        </c:when>
    </c:choose>

    <c:if test="${tipo != 'industrial'}">
    <div class="form-row quatro">
        <div class="form-group" data-erro="Digite um número maior que zero.">
            <label for="cad-q1">${q1l}</label>
            <div class="input-wrapper">
                <input class="input-field" type="number" id="cad-q1" name="${q1n}" value="<c:out value='${cad[q1n]}' />" placeholder="${q1p}" min="1" max="100000" inputmode="numeric" required>
            </div>
        </div>
        <div class="form-group" data-erro="Digite um número maior que zero.">
            <label for="cad-q2">${q2l}</label>
            <div class="input-wrapper">
                <input class="input-field" type="number" id="cad-q2" name="${q2n}" value="<c:out value='${cad[q2n]}' />" placeholder="${q2p}" min="1" max="100000" inputmode="numeric" required>
            </div>
        </div>
        <div class="form-group" data-erro="Digite um número válido.">
            <label for="cad-q3">${q3l}</label>
            <div class="input-wrapper">
                <input class="input-field" type="number" id="cad-q3" name="${q3n}" value="<c:out value='${cad[q3n]}' />" placeholder="${q3p}" min="0" max="10000000" inputmode="numeric">
            </div>
        </div>
        <div class="form-group" data-erro="Escolha Sim ou Não.">
            <label for="cad-coleta">Coleta seletiva</label>
            <div class="input-wrapper">
                <select class="input-field" id="cad-coleta" name="coletaSeletiva" required>
                    <option value="" disabled ${empty cad.coletaSeletiva ? 'selected' : ''}>Sim/Não</option>
                    <option value="sim" ${cad.coletaSeletiva == 'sim' ? 'selected' : ''}>Sim</option>
                    <option value="nao" ${cad.coletaSeletiva == 'nao' ? 'selected' : ''}>Não</option>
                </select>
            </div>
        </div>
    </div>
    </c:if>

    <c:set var="textoAvancar" value="Próximo" />
    <%@ include file="_botoes.jsp" %>
</form>
