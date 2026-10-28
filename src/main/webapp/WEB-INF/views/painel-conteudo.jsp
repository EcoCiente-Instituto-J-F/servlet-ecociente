<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%-- Miolo do painel (cartões + gráfico). O painel.js busca este trecho de novo a cada 30 segundos --%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<%-- Cartões do topo: usuários, condomínios, cooperativas e parcerias (vêm do servidor em kpis) --%>
<section class="indicadores" aria-label="Indicadores do mês">
      <c:forEach var="k" items="${kpis}">
        <article class="cartao indicador">
          <h2 class="indicador__titulo"><c:out value="${k.titulo}"/></h2><span class="indicador__icone"><img src="${ctx}/assets/icones/${k.icone}.svg" alt=""></span>
          <p class="indicador__valor"><c:out value="${k.valor}"/></p>
          <p class="indicador__variacao ${k.tendencia == 'baixa' ? 'indicador__variacao--negativa' : ''}"><img src="${ctx}/assets/icones/tendencia-${k.tendencia}.svg" alt=""><c:out value="${k.variacao}"/></p>
        </article>
      </c:forEach>
    </section>

    <%-- Se o servidor enviou a lista de anos, mostra o gráfico novo (12 meses, com seletor de ano); senão, o gráfico antigo de 7 meses --%>
    <c:choose>
    <c:when test="${not empty anosDisponiveis}">
    <%-- Gráfico novo: 12 meses, uma linha por tipo de conta e o número em cada ponto --%>
    <section class="paineis paineis--unico">
      <article class="cartao painel">
        <div class="grafico__topo">
          <h2 class="painel__titulo">Cadastros por tipo de conta</h2>
          <label class="somente-leitor" for="seletor-ano">Ano do gráfico</label>
          <%-- Troca o ano do gráfico (o painel.js recarrega os dados) --%>
          <select class="grafico__ano" id="seletor-ano" aria-label="Ano do gráfico">
            <c:forEach var="a" items="${anosDisponiveis}"><option value="${a}" ${a == anoSelecionado ? 'selected' : ''}>${a}</option></c:forEach>
          </select>
          <ul class="grafico__legenda">
            <li style="--cor:var(--cor-usuario)">Usuário</li><li style="--cor:var(--cor-sindico)">Síndico</li><li style="--cor:var(--cor-legenda-cooperativa)">Cooperativas</li>
          </ul>
        </div>
        <p class="grafico__total"><fmt:formatNumber value="${totalCadastros}" pattern="#,##0"/></p>
        <svg class="grafico__svg" viewBox="0 0 1163 328" role="img" aria-label="Cadastros por tipo de conta em ${anoSelecionado}, de janeiro a dezembro">
        <%-- Linhas horizontais de fundo do gráfico --%>
        <g style="stroke:var(--cor-grade-grafico)" stroke-width="1"><line x1="40" x2="1123" y1="110" y2="110"/><line x1="40" x2="1123" y1="139" y2="139"/><line x1="40" x2="1123" y1="168" y2="168"/><line x1="40" x2="1123" y1="197" y2="197"/><line x1="40" x2="1123" y1="226" y2="226"/><line x1="40" x2="1123" y1="255" y2="255"/></g>
        <%-- Área clara embaixo da linha de usuários --%>
        <c:if test="${not empty areaUsuarios}"><polygon points="${areaUsuarios}" style="fill:var(--cor-area-grafico)"/></c:if>
        <%-- As três linhas do gráfico: usuário, síndico e cooperativa --%>
        <g fill="none" stroke-width="2" stroke-linejoin="round" stroke-linecap="round">
          <polyline style="stroke:var(--cor-usuario)" points="${pontosUsuarios}"/>
          <polyline stroke="#a863da" points="${pontosSindicos}"/>
          <polyline stroke="#d64573" points="${pontosCooperativas}"/>
        </g>
        <%-- Bolinha e número de cada mês, para cada tipo de conta --%>
        <g font-size="10" font-weight="700" font-family="Montserrat, sans-serif" text-anchor="middle">
          <c:forEach var="m" items="${marcasUsuarios}"><circle cx="${m.x}" cy="${m.y}" r="4" style="fill:var(--cor-usuario)"/><text x="${m.x}" y="${m.y - 9}" style="fill:var(--cor-usuario)">${m.valor}</text></c:forEach>
          <c:forEach var="m" items="${marcasSindicos}"><circle cx="${m.x}" cy="${m.y}" r="4" fill="#a863da"/><text x="${m.x}" y="${m.y - 9}" fill="#a863da">${m.valor}</text></c:forEach>
          <c:forEach var="m" items="${marcasCooperativas}"><circle cx="${m.x}" cy="${m.y}" r="4" fill="#d64573"/><text x="${m.x}" y="${m.y + 18}" fill="#d64573">${m.valor}</text></c:forEach>
        </g>
        <%-- Nomes dos meses, embaixo do gráfico --%>
        <g fill="#acacac" font-size="11" font-family="Montserrat, sans-serif"><c:forEach var="mes" items="${mesesGrafico}"><text x="${mes.x}" y="300" text-anchor="middle"><c:out value="${mes.nome}"/></text></c:forEach></g>
      </svg>
      </article>
    </section>
    </c:when>
    <c:otherwise>
    <%-- Gráfico antigo (últimos 7 meses), usado enquanto o servidor não envia os dados do gráfico novo --%>
    <section class="paineis paineis--unico">
      <article class="cartao painel">
        <div class="grafico__topo">
          <h2 class="painel__titulo">Cadastros por tipo de conta</h2>
          <ul class="grafico__legenda">
            <li style="--cor:var(--cor-usuario)">Usuário</li><li style="--cor:var(--cor-sindico)">Síndico</li><li style="--cor:var(--cor-legenda-cooperativa)">Cooperativas</li>
          </ul>
        </div>
        <p class="grafico__total"><fmt:formatNumber value="${totalCadastros}" pattern="#,##0"/></p>
        <svg class="grafico__svg" viewBox="160 360 683 328" role="img" aria-label="Cadastros por tipo de conta nos últimos meses">
        <%-- Linhas horizontais de fundo do gráfico --%>
        <g style="stroke:var(--cor-grade-grafico)" stroke-width="1"><line x1="199" x2="806" y1="442.5" y2="442.5"/><line x1="199" x2="806" y1="476.7" y2="476.7"/><line x1="199" x2="806" y1="510.9" y2="510.9"/><line x1="199" x2="806" y1="545" y2="545"/><line x1="199" x2="806" y1="579.2" y2="579.2"/><line x1="199" x2="806" y1="613.4" y2="613.4"/></g>
        <%-- Área clara embaixo da linha de usuários --%>
        <polygon points="${areaUsuarios}" style="fill:var(--cor-area-grafico)"/>
        <%-- As três linhas do gráfico: usuário, síndico e cooperativa --%>
        <g fill="none" stroke-width="2" stroke-linejoin="round" stroke-linecap="round">
          <polyline style="stroke:var(--cor-usuario)" points="${pontosUsuarios}"/>
          <polyline stroke="#a863da" points="${pontosSindicos}"/>
          <polyline stroke="#d64573" points="${pontosCooperativas}"/>
        </g>
        <c:set var="pu" value="${fn:split(ultimoUsuarios, ',')}"/><c:set var="ps" value="${fn:split(ultimoSindicos, ',')}"/><c:set var="pc" value="${fn:split(ultimoCooperativas, ',')}"/>
        <circle cx="${pu[0]}" cy="${pu[1]}" r="5.35" style="fill:var(--cor-usuario)"/><circle cx="${ps[0]}" cy="${ps[1]}" r="5.35" fill="#a863da"/><circle cx="${pc[0]}" cy="${pc[1]}" r="5.35" fill="#d64573"/>
        <c:set var="posicoesMeses" value="${fn:split('211.6,308.6,405.9,502.9,600.1,694.7,790.3', ',')}"/>
        <%-- Nomes dos meses, embaixo do gráfico --%>
        <g fill="#acacac" font-size="11" font-family="Montserrat, sans-serif"><c:forEach var="mes" items="${mesesGrafico}" varStatus="m"><text x="${posicoesMeses[m.index]}" y="659.5" text-anchor="middle"><c:out value="${mes}"/></text></c:forEach></g>
      </svg>
      </article>
    </section>
    </c:otherwise>
    </c:choose>
