<%@ page pageEncoding="UTF-8" %>
<%-- Opções de UF. Antes do include: c:set ufSelecionada --%>
<option value="" disabled ${empty ufSelecionada ? 'selected' : ''}>UF</option>
<c:forTokens items="AC,AL,AP,AM,BA,CE,DF,ES,GO,MA,MT,MS,MG,PA,PB,PR,PE,PI,RJ,RN,RS,RO,RR,SC,SP,SE,TO" delims="," var="uf">
    <option value="${uf}" ${ufSelecionada == uf ? 'selected' : ''}>${uf}</option>
</c:forTokens>
