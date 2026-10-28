<%@ page pageEncoding="UTF-8" %>
<%-- Primeira etapa do cadastro: e-mail e senha (mínimo de 8 caracteres) --%>
<c:set var="tituloEtapa" value="Crie sua conta" />
<c:set var="subtituloEtapa" value="Comece cadastrando seu e-mail e uma senha." />
<%@ include file="_cabecalho.jsp" %>
<%-- Se o e-mail já tem conta, oferece entrar para fazer a assinatura --%>
<c:if test="${emailJaCadastrado}">
    <div class="botoes">
        <a href="${ctx}/login?proximo=plano&amp;email=<c:out value='${param.email}' />" class="botao-submit botao-secundario">Fazer assinatura</a>
    </div>
</c:if>

<%-- O campo escondido "etapa" diz ao servidor qual etapa do cadastro está sendo enviada --%>
<form action="${ctx}/cadastro" method="POST">
    <input type="hidden" name="etapa" value="conta">

    <div class="form-group" data-erro="Digite um e-mail válido, como nome@email.com.">
        <label for="cad-email">E-mail</label>
        <div class="input-wrapper">
            <input class="input-field" type="email" id="cad-email" name="email" value="<c:out value='${cad.email}' />" placeholder="Digite seu e-mail" autocomplete="email" required>
        </div>
    </div>

    <%-- Senha e confirmação, com botão para mostrar ou esconder --%>
    <div class="form-row">
        <div class="form-group" data-erro="Faltam caracteres: a senha precisa ter no mínimo 8.">
            <label for="cad-senha">Senha</label>
            <div class="input-wrapper">
                <input class="input-field campo-senha" type="text" id="cad-senha" name="senha" placeholder="Digite sua senha" autocomplete="off" autocapitalize="off" autocorrect="off" spellcheck="false" required minlength="8" aria-describedby="dica-senha">
                <input type="checkbox" id="ver-senha-cadastro" class="ver-senha" aria-label="Mostrar senha">
                <label for="ver-senha-cadastro" class="toggle-password">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                        <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                        <circle cx="12" cy="12" r="3"></circle>
                        <line class="risco" x1="2" y1="2" x2="22" y2="22"></line>
                    </svg>
                </label>
            </div>
            <p class="dica-campo" id="dica-senha">Mínimo de 8 caracteres.</p>
        </div>

        <div class="form-group" data-erro="Faltam caracteres: repita a senha, com no mínimo 8.">
            <label for="cad-confirma">Confirmar senha</label>
            <div class="input-wrapper">
                <input class="input-field campo-senha" type="text" id="cad-confirma" name="confirma_senha" placeholder="Repita a senha" autocomplete="off" autocapitalize="off" autocorrect="off" spellcheck="false" required minlength="8">
                <input type="checkbox" id="ver-confirma-cadastro" class="ver-senha" aria-label="Mostrar confirmação de senha">
                <label for="ver-confirma-cadastro" class="toggle-password">
                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                        <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                        <circle cx="12" cy="12" r="3"></circle>
                        <line class="risco" x1="2" y1="2" x2="22" y2="22"></line>
                    </svg>
                </label>
            </div>
        </div>
    </div>

    <div class="botoes">
        <button type="submit" class="botao-submit">Próximo</button>
        <a href="${ctx}/inicio" class="botao-submit botao-secundario">Voltar</a>
    </div>
</form>
