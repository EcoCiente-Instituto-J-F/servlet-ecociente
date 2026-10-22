<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%-- Data de nascimento. O max (hoje) é calculado aqui para valer em qualquer caminho de renderização. --%>
<jsp:useBean id="agora" class="java.util.Date" />
<fmt:formatDate value="${agora}" pattern="yyyy-MM-dd" var="hoje" />
<div class="form-group" data-erro="Informe uma data de nascimento válida.">
    <label for="cad-nascimento">Data de nascimento</label>
    <div class="input-wrapper">
        <input class="input-field" type="date" id="cad-nascimento" name="nascimento" value="<c:out value='${cad.nascimento}' />" autocomplete="bday" required min="1900-01-01" max="${hoje}">
    </div>
</div>