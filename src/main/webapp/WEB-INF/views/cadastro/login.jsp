<%@ page pageEncoding="UTF-8" %>
<%-- Login de quem já tem conta, usado para fazer a assinatura --%>
<c:set var="tituloEtapa" value="Acesse sua conta" />
<c:set var="subtituloEtapa" value="Você já tem cadastro. Entre para fazer a assinatura." />
<%@ include file="_cabecalho.jsp" %>

<%-- Envia e-mail e senha para o login; o campo proximo guarda para onde ir depois de entrar --%>
<form action="${ctx}/login" method="POST">
    <input type="hidden" name="proximo" value="<c:out value='${param.proximo}' />">

    <div class="form-group">
        <label for="login-email">E-mail</label>
        <div class="input-wrapper">
            <input class="input-field" type="email" id="login-email" name="email" value="<c:out value='${param.email}' />" placeholder="Digite seu e-mail" autocomplete="email" required>
        </div>
    </div>

    <div class="form-group">
        <label for="login-senha">Senha</label>
        <div class="input-wrapper">
            <input class="input-field campo-senha" type="text" id="login-senha" name="senha" placeholder="Digite sua senha" autocomplete="off" autocapitalize="off" autocorrect="off" spellcheck="false" required>
            <input type="checkbox" id="ver-senha-login" class="ver-senha" aria-label="Mostrar senha">
            <label for="ver-senha-login" class="toggle-password">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                    <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                    <circle cx="12" cy="12" r="3"></circle>
                    <line class="risco" x1="2" y1="2" x2="22" y2="22"></line>
                </svg>
            </label>
        </div>
        <a href="#" class="forgot-password">Esqueceu sua senha?</a>
    </div>

    <%-- Mensagem de erro do login --%>
    <c:if test="${not empty erroLogin}">
        <p class="mensagem-erro" role="alert"><c:out value="${erroLogin}" /></p>
    </c:if>

    <div class="botoes">
        <button type="submit" class="botao-submit">Entrar</button>
    </div>
</form>