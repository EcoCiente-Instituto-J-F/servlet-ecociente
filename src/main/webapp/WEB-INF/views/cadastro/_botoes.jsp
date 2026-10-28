<%@ page pageEncoding="UTF-8" %>
<%-- Botões do fim de cada etapa: avançar e voltar (semVoltar esconde o Voltar) --%>
<div class="botoes">
    <button type="submit" class="botao-submit"><c:out value="${textoAvancar}" /></button>
    <c:if test="${not semVoltar}">
        <button type="submit" name="acao" value="voltar" class="botao-submit botao-secundario" formnovalidate>Voltar</button>
    </c:if>
</div>
