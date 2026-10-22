<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:if test="${not empty totalPaginas}">
<c:if test="${not empty param.q}">
  <c:url var="qCodificado" value="/"><c:param name="q" value="${param.q}"/></c:url>
  <c:set var="sufixoBusca" value="&amp;q=${fn:substringAfter(qCodificado, '?q=')}"/>
</c:if>
<nav class="paginacao" aria-label="Paginação">
  <a href="?pagina=${paginaAtual > 1 ? paginaAtual - 1 : 1}${sufixoBusca}" aria-label="Página anterior"><img src="${ctx}/assets/icones/seta-esquerda.svg" alt=""></a>
  <c:forEach var="n" begin="1" end="${totalPaginas < 4 ? totalPaginas : 4}">
    <a href="?pagina=${n}${sufixoBusca}" ${n == paginaAtual ? 'aria-current="page"' : ''}>${n}</a>
  </c:forEach>
  <c:if test="${totalPaginas > 4}"><span>...</span><a href="?pagina=${totalPaginas}${sufixoBusca}" ${totalPaginas == paginaAtual ? 'aria-current="page"' : ''}>${totalPaginas}</a></c:if>
  <a href="?pagina=${paginaAtual < totalPaginas ? paginaAtual + 1 : totalPaginas}${sufixoBusca}" aria-label="Próxima página"><img src="${ctx}/assets/icones/seta-direita.svg" alt=""></a>
</nav>
</c:if>
