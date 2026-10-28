<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Faixas de aviso (sucesso ou erro) mostradas no topo das telas depois de cadastrar, alterar ou excluir --%>
<%-- Erro enviado pelo servidor: mostra a mensagem e reabre o modal com o que a pessoa tinha digitado --%>
<c:if test="${not empty erro}">
  <p class="aviso aviso--erro" role="alert"><c:out value="${erro}"/></p>
  <%-- Reabre o modal com os valores enviados: interacoes.js lê estes itens --%>
  <c:if test="${not empty param.reabrirModal}">
    <div id="reabrir-modal" hidden data-modal="<c:out value='${param.reabrirModal}'/>" data-modo="${empty param.id and empty param.idSindico and empty param.idEndereco and empty param.idTelefone and empty param.idMorador ? 'novo' : 'editar'}" data-id="<c:out value='${not empty param.id ? param.id : (not empty param.idSindico ? param.idSindico : (not empty param.idEndereco ? param.idEndereco : (not empty param.idTelefone ? param.idTelefone : param.idMorador)))}'/>">
      <c:forEach var="p" items="${param}">
        <span data-nome="<c:out value='${p.key}'/>" data-valor="<c:out value='${p.value}'/>"></span>
      </c:forEach>
    </div>
  </c:if>
</c:if>
<%-- Avisos de sucesso e de falha de cada ação --%>
<c:if test="${adicionado == 'true'}"><p class="aviso aviso--sucesso" role="status">Cadastro realizado com sucesso.</p></c:if>
<c:if test="${alterado == 'true'}"><p class="aviso aviso--sucesso" role="status">Alterações salvas com sucesso.</p></c:if>
<c:if test="${deletado == 'true'}"><p class="aviso aviso--sucesso" role="status">Registro excluído com sucesso.</p></c:if>
<c:if test="${adicionado == 'false' and empty erro}"><p class="aviso aviso--erro" role="alert">Não foi possível cadastrar.</p></c:if>
<c:if test="${alterado == 'false' and empty erro}"><p class="aviso aviso--erro" role="alert">Não foi possível salvar as alterações.</p></c:if>
<c:if test="${deletado == 'false' and empty erro}"><p class="aviso aviso--erro" role="alert">Não foi possível excluir.</p></c:if>
