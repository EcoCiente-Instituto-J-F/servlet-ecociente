<%@ page pageEncoding="UTF-8" %>
<c:set var="rotuloEtapa" value="Etapa 2 de 2 · Pagamento" />
<c:set var="tituloEtapa" value="Forma de pagamento" />
<c:set var="subtituloEtapa" value="Escolha como prefere pagar sua assinatura." />
<%@ include file="_cabecalho.jsp" %>

<c:set var="metodo" value="${empty cad.metodo ? 'cartao' : cad.metodo}" />
<c:set var="docPadrao" value="${empty cad.docTitular ? cad.documento : cad.docTitular}" />

<div class="resumo-pedido">
    <div>
        <strong>Plano <c:out value="${orc.plano.nome}" /></strong>
        <span><c:out value="${orc.resumo}" /></span>
    </div>
    <div class="resumo-valor">
        <strong><c:out value="${orc.total}" /></strong>
        <span><c:out value="${orc.porPeriodo}" /></span>
    </div>
</div>

<div class="metodos" role="radiogroup" aria-label="Forma de pagamento">
    <input type="radio" name="metodo-escolha" id="metodo-cartao" class="metodo-radio" ${metodo == 'cartao' ? 'checked' : ''}>
    <label for="metodo-cartao" class="metodo"><img src="${ctx}/assets/icone-cartao.svg" width="28" height="20" alt="">Cartão de crédito</label>
    <input type="radio" name="metodo-escolha" id="metodo-pix" class="metodo-radio" ${metodo == 'pix' ? 'checked' : ''}>
    <label for="metodo-pix" class="metodo"><img src="${ctx}/assets/icone-pix.svg" width="20" height="20" alt="">Pix</label>
    <input type="radio" name="metodo-escolha" id="metodo-boleto" class="metodo-radio" ${metodo == 'boleto' ? 'checked' : ''}>
    <label for="metodo-boleto" class="metodo"><img src="${ctx}/assets/icone-boleto.svg" width="28" height="20" alt="">Boleto</label>
</div>

<%-- ===== Cartão de crédito ===== --%>
<form action="${ctx}/cadastro" method="POST" class="form-metodo form-cartao" autocomplete="on">
    <input type="hidden" name="etapa" value="pag-pagamento">
    <input type="hidden" name="metodo" value="cartao">

    <div class="form-group" data-erro="Digite o nome como está impresso no cartão.">
        <label for="cad-nome-cartao">Nome impresso no cartão</label>
        <div class="input-wrapper">
            <input class="input-field" type="text" id="cad-nome-cartao" name="nomeCartao" value="<c:out value='${cad.nomeCartao}' />" placeholder="Como está no cartão" autocomplete="cc-name" required maxlength="80">
        </div>
    </div>

    <div class="form-group" data-erro="Número do cartão incompleto: digite de 13 a 19 números.">
        <label for="cad-numero-cartao">Número do cartão</label>
        <div class="input-wrapper">
            <input class="input-field" type="text" id="cad-numero-cartao" name="numeroCartao" placeholder="0000 0000 0000 0000" autocomplete="cc-number" inputmode="numeric" required pattern="[0-9 ]{13,23}" title="Somente números">
        </div>
    </div>

    <div class="form-row tres">
        <div class="form-group" data-erro="Use o formato MM/AA, ex.: 08/29.">
            <label for="cad-validade">Validade</label>
            <div class="input-wrapper">
                <input class="input-field" type="text" id="cad-validade" name="validade" placeholder="MM/AA" autocomplete="cc-exp" inputmode="numeric" required pattern="(0[1-9]|1[0-2])\s?/\s?[0-9]{2}" title="Mês e ano, ex.: 08/29">
            </div>
        </div>
        <div class="form-group" data-erro="CVV incompleto: são 3 ou 4 números no verso do cartão.">
            <label for="cad-cvv">CVV</label>
            <div class="input-wrapper">
                <input class="input-field" type="text" id="cad-cvv" name="cvv" placeholder="000" autocomplete="cc-csc" inputmode="numeric" required pattern="[0-9]{3,4}" title="3 ou 4 números no verso do cartão">
            </div>
        </div>
        <div class="form-group" data-erro="CPF ou CNPJ incompleto: o CPF tem 11 números e o CNPJ tem 14.">
            <label for="cad-doc-titular">CPF/CNPJ do titular</label>
            <div class="input-wrapper">
                <input class="input-field" type="text" id="cad-doc-titular" name="docTitular" value="<c:out value='${docPadrao}' />" placeholder="000.000.000-00" inputmode="numeric" required pattern="[0-9]{3}\.?[0-9]{3}\.?[0-9]{3}-?[0-9]{2}|[0-9]{2}\.?[0-9]{3}\.?[0-9]{3}/?[0-9]{4}-?[0-9]{2}" title="CPF ou CNPJ">
            </div>
        </div>
    </div>

    <div class="aviso-lgpd" role="note">
        <img src="${ctx}/assets/icone-lock.svg" width="18" height="20" alt="">
        <p><strong>Pagamento seguro.</strong> A cobrança é ${orc.anual ? 'anual' : 'mensal'} e automática. Cancele quando quiser.</p>
    </div>

    <c:set var="textoAvancar" value="Pagar ${orc.total}" />
    <%@ include file="_botoes.jsp" %>
</form>

<%-- ===== Pix ===== --%>
<form action="${ctx}/cadastro" method="POST" class="form-metodo form-pix">
    <input type="hidden" name="etapa" value="pag-pagamento">
    <input type="hidden" name="metodo" value="pix">

    <div class="pix-qr">
        <img src="${ctx}/assets/pix-qrcode-exemplo.svg" width="168" height="168" alt="QR Code do Pix">
        <div>
            <ol class="passos">
                <li>Abra o app do seu banco e escolha pagar com Pix.</li>
                <li>Escaneie o QR Code ou cole o código abaixo.</li>
                <li>Confirme o pagamento. A ativação é imediata.</li>
            </ol>
            <p class="expira">O código expira às <c:out value="${pixExpira}" />.</p>
        </div>
    </div>

    <div class="copia-cola">
        <span class="copia-cola-rotulo" id="rotulo-pix">Pix copia e cola</span>
        <code class="copia-cola-codigo" tabindex="0" aria-labelledby="rotulo-pix"><c:out value="${pixCodigo}" /></code>
        <span class="dica-campo">Clique no código para selecionar e copie com Ctrl+C.</span>
    </div>

    <c:set var="textoAvancar" value="Já fiz o pagamento" />
    <%@ include file="_botoes.jsp" %>
</form>

<%-- ===== Boleto ===== --%>
<form action="${ctx}/cadastro" method="POST" class="form-metodo form-boleto">
    <input type="hidden" name="etapa" value="pag-pagamento">
    <input type="hidden" name="metodo" value="boleto">

    <div class="form-row">
        <div class="form-group" data-erro="CPF ou CNPJ incompleto: o CPF tem 11 números e o CNPJ tem 14.">
            <label for="cad-doc-pagador">CPF ou CNPJ do pagador</label>
            <div class="input-wrapper">
                <input class="input-field" type="text" id="cad-doc-pagador" name="docPagador" value="<c:out value='${empty cad.docPagador ? cad.documento : cad.docPagador}' />" placeholder="000.000.000-00" inputmode="numeric" required pattern="[0-9]{3}\.?[0-9]{3}\.?[0-9]{3}-?[0-9]{2}|[0-9]{2}\.?[0-9]{3}\.?[0-9]{3}/?[0-9]{4}-?[0-9]{2}" title="CPF ou CNPJ">
            </div>
        </div>
        <div class="form-group" data-erro="Digite um e-mail válido, como nome@email.com.">
            <label for="cad-email-boleto">E-mail para envio</label>
            <div class="input-wrapper">
                <input class="input-field" type="email" id="cad-email-boleto" name="emailBoleto" value="<c:out value='${empty cad.emailBoleto ? cad.email : cad.emailBoleto}' />" placeholder="Digite seu e-mail" autocomplete="email" required>
            </div>
        </div>
    </div>

    <div class="aviso-lgpd" role="note">
        <img src="${ctx}/assets/icone-boleto.svg" width="28" height="20" alt="">
        <p><strong>Compensação em até 3 dias úteis.</strong> A assinatura é ativada assim que o pagamento for confirmado. O boleto vence em 3 dias.</p>
    </div>

    <c:set var="textoAvancar" value="Gerar boleto" />
    <%@ include file="_botoes.jsp" %>
</form>
