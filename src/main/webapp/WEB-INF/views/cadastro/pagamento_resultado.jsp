<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Resultado do pagamento: aprovado, boleto gerado ou não aprovado (conforme pagamento.status) --%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%-- Situação do pagamento enviada pelo servidor --%>
<c:set var="st" value="${pagamento.status}" />
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>EcoCiente - ${st == 'aprovado' ? 'Pagamento confirmado' : (st == 'boleto' ? 'Boleto gerado' : 'Pagamento não aprovado')}</title>
    <link rel="icon" href="${ctx}/assets/favicon.png">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Instrument+Sans:ital,wght@0,400..700;1,400..700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;600;700&display=swap" rel="stylesheet">

    <style>
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            background-image: url('${ctx}/assets/tela-fundo-login.png');
            background-size: cover;
            background-position: center;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 24px 16px;
            font-family: 'Instrument Sans', sans-serif;
        }

        .card-wrapper {
            background: rgba(255, 255, 255, 0.15);
            backdrop-filter: blur(8px);
            padding: 12px;
            border-radius: 20px;
            border: 1px solid rgba(255, 255, 255, 0.2);
            width: 100%;
            max-width: 540px;
            animation: aparecer 0.4s ease;
        }

        .card-resultado {
            background-color: #ffffff;
            border-radius: 12px;
            padding: 28px 32px;
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 14px;
            text-align: center;
        }

        @keyframes aparecer {
            from { opacity: 0; transform: translateY(8px); }
            to   { opacity: 1; transform: translateY(0); }
        }

        .icone-status {
            width: 64px;
            height: 64px;
        }

        .situacao {
            color: #064E3B;
            font-family: 'Montserrat', sans-serif;
            font-weight: 600;
            font-size: 14px;
        }

        .titulo-resultado {
            color: #064E3B;
            font-family: 'Montserrat', sans-serif;
            font-weight: 600;
            font-size: 24px;
            line-height: 1.25;
        }

        .texto-resultado {
            font-size: 13px;
            line-height: 1.45;
            color: #6B7370;
            max-width: 420px;
        }

        /* ----- Detalhes da assinatura ----- */
        .detalhes {
            width: 100%;
            background-color: #ECF4F1;
            border-radius: 8px;
            padding: 12px 14px;
            display: grid;
            gap: 8px;
            text-align: left;
            font-size: 12px;
        }

        .detalhes div {
            display: flex;
            justify-content: space-between;
            gap: 12px;
        }

        .detalhes dt {
            color: #6B7370;
        }

        .detalhes dd {
            color: #064E3B;
            font-weight: 600;
            text-align: right;
        }

        /* ----- Boleto ----- */
        .codigo-barras {
            width: 100%;
            height: auto;
        }

        .copia-cola {
            width: 100%;
            text-align: left;
        }

        .copia-cola-rotulo {
            display: block;
            color: #064E3B;
            font-family: 'Montserrat', sans-serif;
            font-weight: 700;
            font-size: 13px;
            margin-bottom: 6px;
        }

        .copia-cola-codigo {
            display: block;
            background-color: #ECF4F1;
            border-radius: 6px;
            padding: 10px 12px;
            font-size: 12px;
            color: #333333;
            word-break: break-all;
            user-select: all;
            -webkit-user-select: all;
            cursor: text;
        }

        .copia-cola-dica {
            display: block;
            margin-top: 4px;
            font-size: 11px;
            color: #6B7370;
        }

        /* ----- Botões ----- */
        .botoes {
            display: flex;
            gap: 12px;
            width: 100%;
            margin-top: 6px;
        }

        .botao {
            flex: 1;
            display: flex;
            align-items: center;
            justify-content: center;
            text-align: center;
            min-height: 42px;
            padding: 10px 16px;
            border-radius: 6px;
            border: 1px solid #064E3B;
            font-family: 'Montserrat', sans-serif;
            font-weight: 600;
            font-size: 14px;
            text-decoration: none;
            transition: background-color 0.2s ease;
        }

        .botao-principal {
            background-color: #064E3B;
            color: #ffffff;
        }

        .botao-principal:hover {
            background-color: #0A6A50;
        }

        .botao-secundario {
            background-color: #ffffff;
            color: #064E3B;
        }

        .botao-secundario:hover {
            background-color: #DFEADE;
        }

        .botao[aria-disabled="true"] {
            opacity: 0.5;
            cursor: not-allowed;
        }

        .botao:focus-visible {
            outline: 2px solid #E05282;
            outline-offset: 2px;
        }

        @media (max-width: 700px) {
            .card-wrapper {
                padding: 8px;
                border-radius: 16px;
            }

            .card-resultado {
                padding: 24px 20px;
            }

            .titulo-resultado {
                font-size: 20px;
            }

            .botoes {
                flex-direction: column-reverse;
            }

            .botao {
                min-height: 44px;
            }
        }
    </style>
</head>
<body>

    <%-- Cartão central com a mensagem do resultado --%>
    <main class="card-wrapper">
        <section class="card-resultado" aria-labelledby="titulo-resultado">
            <c:choose>

                <%-- ===== Pagamento aprovado ===== --%>
                <c:when test="${st == 'aprovado'}">
                    <img class="icone-status" src="${ctx}/assets/status-aprovado.svg" width="80" height="80" alt="">
                    <p class="situacao">Pagamento confirmado</p>
                    <h1 class="titulo-resultado" id="titulo-resultado">Sua assinatura foi ativada!</h1>
                    <p class="texto-resultado">Enviamos o recibo e os próximos passos para o seu e-mail.</p>

                    <dl class="detalhes">
                        <div><dt>Plano</dt><dd><c:out value="${pagamento.plano}" /></dd></div>
                        <div><dt>Valor</dt><dd><c:out value="${pagamento.valor}" /></dd></div>
                        <div><dt>Forma de pagamento</dt><dd><c:out value="${pagamento.forma}" /></dd></div>
                        <div><dt>Próxima cobrança</dt><dd><c:out value="${pagamento.proximaCobranca}" /></dd></div>
                    </dl>

                    <div class="botoes">
                        <a href="${ctx}/inicio" class="botao botao-principal">Ir para o início</a>
                    </div>
                </c:when>

                <%-- ===== Boleto gerado ===== --%>
                <c:when test="${st == 'boleto'}">
                    <img class="icone-status" src="${ctx}/assets/status-aguardando.svg" width="80" height="80" alt="">
                    <p class="situacao">Aguardando pagamento</p>
                    <h1 class="titulo-resultado" id="titulo-resultado">Boleto gerado!</h1>
                    <p class="texto-resultado">Pague até <c:out value="${pagamento.vencimento}" />. Sua assinatura é ativada em até 3 dias úteis após a confirmação.</p>

                    <img class="codigo-barras" src="${ctx}/assets/boleto-codigo-barras-exemplo.svg" width="520" height="64" alt="Código de barras do boleto">

                    <div class="copia-cola">
                        <span class="copia-cola-rotulo" id="rotulo-linha">Linha digitável</span>
                        <code class="copia-cola-codigo" tabindex="0" aria-labelledby="rotulo-linha"><c:out value="${pagamento.linhaDigitavel}" /></code>
                        <span class="copia-cola-dica">Clique no código para selecionar e copie com Ctrl+C.</span>
                    </div>

                    <div class="botoes">
                        <span class="botao botao-secundario" aria-disabled="true" title="Disponível quando o gateway de pagamento estiver integrado">Baixar boleto (PDF)</span>
                        <a href="${ctx}/inicio" class="botao botao-principal">Ir para o início</a>
                    </div>
                </c:when>

                <%-- ===== Pagamento recusado ===== --%>
                <c:otherwise>
                    <img class="icone-status" src="${ctx}/assets/status-recusado.svg" width="80" height="80" alt="">
                    <p class="situacao">Algo deu errado</p>
                    <h1 class="titulo-resultado" id="titulo-resultado">Pagamento não aprovado</h1>
                    <p class="texto-resultado">A operadora recusou o cartão. Confira os dados ou escolha outra forma de pagamento.</p>

                    <div class="botoes">
                        <a href="${ctx}/cadastro?metodo=pix" class="botao botao-secundario">Trocar forma de pagamento</a>
                        <a href="${ctx}/cadastro?metodo=cartao" class="botao botao-principal">Tentar novamente</a>
                    </div>
                </c:otherwise>

            </c:choose>
        </section>
    </main>

</body>
</html>
