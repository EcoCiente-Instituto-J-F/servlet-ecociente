<%@ page pageEncoding="UTF-8" %>
<%-- Etapa: perfil do cadastro (cooperativa, síndico ou morador) --%>
<c:set var="tituloEtapa" value="Perfil" />
<c:set var="subtituloEtapa" value="Selecione o perfil que mais combina com você." />
<%@ include file="_cabecalho.jsp" %>

<%-- O campo escondido "etapa" diz ao servidor qual etapa do cadastro está sendo enviada --%>
<form action="${ctx}/cadastro" method="POST">
    <input type="hidden" name="etapa" value="perfil">

    <%-- Opções em cartões; a que a pessoa já escolheu antes vem marcada (cad.perfil) --%>
    <div class="opcoes" role="radiogroup" aria-label="Perfil">
        <label class="opcao">
            <input type="radio" name="perfil" value="cooperativa" required ${cad.perfil == 'cooperativa' ? 'checked' : ''}>
            <span class="opcao-check" aria-hidden="true"></span>
            <img class="opcao-icone" src="${ctx}/assets/icone-perfil-cooperativa.svg" alt="">
            <span class="opcao-titulo">Cooperativa</span>
            <span class="opcao-desc">Recebe e destina os materiais recicláveis.</span>
        </label>
        <label class="opcao">
            <input type="radio" name="perfil" value="sindico" ${cad.perfil == 'sindico' ? 'checked' : ''}>
            <span class="opcao-check" aria-hidden="true"></span>
            <img class="opcao-icone" src="${ctx}/assets/icones/icone-perfil-sindico.svg" alt="">
            <span class="opcao-titulo">Síndico</span>
            <span class="opcao-desc">Cadastra o condomínio e acompanha a coleta.</span>
        </label>
        <label class="opcao">
            <input type="radio" name="perfil" value="morador" ${cad.perfil == 'morador' ? 'checked' : ''}>
            <span class="opcao-check" aria-hidden="true"></span>
            <img class="opcao-icone" src="${ctx}/assets/icone-perfil-morador.svg" alt="">
            <span class="opcao-titulo">Morador ou Cidadão</span>
            <span class="opcao-desc">Separa o lixo e acompanha seu impacto.</span>
        </label>
    </div>

    <c:set var="textoAvancar" value="Próximo" />
    <%@ include file="_botoes.jsp" %>
</form>
