<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<section class="indicadores" aria-label="Indicadores do mês">
      <c:forEach var="k" items="${kpis}">
        <article class="cartao indicador">
          <h2 class="indicador__titulo"><c:out value="${k.titulo}"/></h2><span class="indicador__icone"><img src="${ctx}/assets/icones/${k.icone}.svg" alt=""></span>
          <p class="indicador__valor"><c:out value="${k.valor}"/></p>
          <p class="indicador__variacao ${k.tendencia == 'baixa' ? 'indicador__variacao--negativa' : ''}"><img src="${ctx}/assets/icones/tendencia-${k.tendencia}.svg" alt=""><c:out value="${k.variacao}"/></p>
        </article>
      </c:forEach>
    </section>

    <section class="paineis">
      <article class="cartao painel">
        <div class="grafico__topo">
          <h2 class="painel__titulo">Cadastros por tipo de conta</h2>
          <ul class="grafico__legenda">
            <li style="--cor:var(--cor-usuario)">Usuário</li><li style="--cor:var(--cor-sindico)">Síndico</li><li style="--cor:var(--cor-legenda-cooperativa)">Cooperativas</li>
          </ul>
        </div>
        <p class="grafico__total"><fmt:formatNumber value="${totalCadastros}" pattern="#,##0"/></p>
        <svg class="grafico__svg" viewBox="160 360 683 328" role="img" aria-label="Cadastros por tipo de conta de fevereiro a agosto">
        <g style="stroke:var(--cor-grade-grafico)" stroke-width="1"><line x1="199" x2="806" y1="442.5" y2="442.5"/><line x1="199" x2="806" y1="476.7" y2="476.7"/><line x1="199" x2="806" y1="510.9" y2="510.9"/><line x1="199" x2="806" y1="545" y2="545"/><line x1="199" x2="806" y1="579.2" y2="579.2"/><line x1="199" x2="806" y1="613.4" y2="613.4"/></g>
        <polygon points="${areaUsuarios}" style="fill:var(--cor-area-grafico)"/>
        <g fill="none" stroke-width="2" stroke-linejoin="round" stroke-linecap="round">
          <polyline style="stroke:var(--cor-usuario)" points="${pontosUsuarios}"/>
          <polyline stroke="#a863da" points="${pontosSindicos}"/>
          <polyline stroke="#d64573" points="${pontosCooperativas}"/>
        </g>
        <c:set var="pu" value="${fn:split(ultimoUsuarios, ',')}"/><c:set var="ps" value="${fn:split(ultimoSindicos, ',')}"/><c:set var="pc" value="${fn:split(ultimoCooperativas, ',')}"/>
        <circle cx="${pu[0]}" cy="${pu[1]}" r="5.35" style="fill:var(--cor-usuario)"/><circle cx="${ps[0]}" cy="${ps[1]}" r="5.35" fill="#a863da"/><circle cx="${pc[0]}" cy="${pc[1]}" r="5.35" fill="#d64573"/>
        <g fill="#acacac" font-size="11" font-family="Montserrat, sans-serif"><text x="211.6" y="659.5" text-anchor="middle">Fev</text><text x="308.6" y="659.5" text-anchor="middle">Mar</text><text x="405.9" y="659.5" text-anchor="middle">Abr</text><text x="502.9" y="659.5" text-anchor="middle">Mai</text><text x="600.1" y="659.5" text-anchor="middle">Jun</text><text x="694.7" y="659.5" text-anchor="middle">Jul</text><text x="790.3" y="659.5" text-anchor="middle">Ago</text></g>
      </svg>
      </article>
      <article class="cartao painel">
        <h2 class="painel__titulo">Volume por categoria</h2>
        <ul class="categorias">
          <c:forEach var="cat" items="${categorias}">
            <li class="categoria" style="--cor:var(--cor-${cat.codigoCor});--porcentagem:${cat.porcentagem}%">
              <div class="categoria__topo"><span class="categoria__ponto"></span><span class="categoria__nome"><c:out value="${cat.nome}"/></span><span class="categoria__valor"><fmt:formatNumber value="${cat.kg}" pattern="#,##0"/> Kg</span></div>
              <div class="categoria__barra"><span></span></div>
            </li>
          </c:forEach>
        </ul>
      </article>
    </section>
