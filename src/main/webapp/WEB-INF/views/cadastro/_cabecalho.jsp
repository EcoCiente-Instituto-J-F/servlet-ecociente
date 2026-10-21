<%@ page pageEncoding="UTF-8" %>
<c:if test="${not empty rotuloEtapa}">
    <p class="rotulo-etapa"><c:out value="${rotuloEtapa}" /></p>
</c:if>
<div class="titulo-container">
    <h3 class="titulo-padrao"><c:out value="${tituloEtapa}" /></h3>
</div>
<p class="subtitulo"><c:out value="${subtituloEtapa}" /></p>

<c:if test="${not empty erroCadastro}">
    <p class="mensagem-erro" role="alert"><c:out value="${erroCadastro}" /></p>
</c:if>
