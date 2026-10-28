<%@ page pageEncoding="UTF-8" %>
<%-- Cabeçalho de cada etapa do cadastro: título, subtítulo e mensagem de erro (se houver) --%>
<c:if test="${not empty rotuloEtapa}">
    <p class="rotulo-etapa"><c:out value="${rotuloEtapa}" /></p>
</c:if>
<div class="titulo-container">
    <h3 class="titulo-padrao"><c:out value="${tituloEtapa}" /></h3>
</div>
<p class="subtitulo"><c:out value="${subtituloEtapa}" /></p>

<%-- Erro de validação enviado pelo servidor --%>
<c:if test="${not empty erroCadastro}">
    <p class="mensagem-erro" role="alert"><c:out value="${erroCadastro}" /></p>
</c:if>
