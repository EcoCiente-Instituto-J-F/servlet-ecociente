<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%--
    Tela de erro da aplicação (400, 404 e erro de servidor).
    Mapeada no web.xml via <error-page>: 400, 404 e 500.
    Qualquer outro código recebido cai no layout de erro de servidor, mostrando o próprio código.
--%>
<c:set var="codigo" value="${pageContext.errorData.statusCode}" />
<c:choose>
    <c:when test="${codigo == 400}">
        <c:set var="variante" value="400" />
        <c:set var="tituloPagina" value="Requisição inválida" />
    </c:when>
    <c:when test="${codigo == 404}">
        <c:set var="variante" value="404" />
        <c:set var="tituloPagina" value="Página não encontrada" />
    </c:when>
    <c:otherwise>
        <c:set var="variante" value="500" />
        <c:set var="tituloPagina" value="Erro no servidor" />
    </c:otherwise>
</c:choose>
<c:set var="urlTentarNovamente" value="${not empty pageContext.errorData.requestURI ? pageContext.errorData.requestURI : ctx.concat('/inicio')}" />
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>EcoCiente - ${tituloPagina}</title>
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
            width: 700px;
            max-width: 100%;
            animation: aparecer 0.4s ease;
        }

        .card-erro {
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
            to { opacity: 1; transform: translateY(0); }
        }

        .icone-status {
            width: 200px;
            height: auto;
            max-width: 100%;
        }

        .codigo-erro {
            color: #D64573;
            font-family: 'Montserrat', sans-serif;
            font-weight: 600;
            font-size: 16px;
        }

        .titulo-erro {
            color: #064E3B;
            font-family: 'Montserrat', sans-serif;
            font-weight: 600;
            font-size: 24px;
            line-height: 1.25;
        }

        .titulo-erro .destaque {
            color: #D64573;
        }

        .texto-erro {
            font-size: 13px;
            line-height: 1.45;
            color: #6B7370;
            max-width: 420px;
        }

        /* ----- Botões ----- */
        .botoes {
            display: flex;
            gap: 12px;
            width: 520px;
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

        .botao:focus-visible {
            outline: 2px solid #E05282;
            outline-offset: 2px;
        }

        @media (max-width: 700px) {
            .card-wrapper {
                padding: 8px;
                border-radius: 16px;
            }

            .card-erro {
                padding: 24px 20px;
            }

            .titulo-erro {
                font-size: 20px;
            }

            .botoes {
                flex-direction: column-reverse;
                max-width: 100%;
            }

            .botao {
                min-height: 44px;
            }
        }
    </style>
</head>
<body>

    <main class="card-wrapper">
        <section class="card-erro" aria-labelledby="titulo-erro">

            <img class="icone-status"
                 src="${ctx}/assets/eco-${variante}.svg"
                 alt="">

            <p class="codigo-erro">ERRO: ${variante == '500' and not empty codigo ? codigo : variante}</p>

            <c:choose>

                <%-- ===== 400 - Requisição inválida ===== --%>
                <c:when test="${variante == '400'}">
                    <h1 class="titulo-erro" id="titulo-erro">
                        Requisição <span class="destaque">inválida</span>
                    </h1>
                    <p class="texto-erro">
                        Não foi possível processar o pedido porque alguns dados estão incorretos ou incompletos. Volte e tente novamente.
                    </p>
                    <div class="botoes">
                        <a href="${ctx}/inicio" class="botao botao-principal"
                           onclick="if (history.length > 1) { history.back(); return false; }">
                            Voltar
                        </a>
                    </div>
                </c:when>

                <%-- ===== 404 - Página não encontrada ===== --%>
                <c:when test="${variante == '404'}">
                    <h1 class="titulo-erro" id="titulo-erro">
                        Página <span class="destaque">não encontrada</span>
                    </h1>
                    <p class="texto-erro">
                        O endereço que você tentou acessar não existe, foi movido ou está temporariamente indisponível.
                    </p>
                    <div class="botoes">
                        <a href="${ctx}/inicio" class="botao botao-principal">
                            Ir para o início
                        </a>
                    </div>
                </c:when>

                <%-- ===== 500 e demais códigos - Erro no servidor ===== --%>
                <c:otherwise>
                    <h1 class="titulo-erro" id="titulo-erro">
                        Erro <span class="destaque">no servidor</span>
                    </h1>
                    <p class="texto-erro">
                        Estamos com uma instabilidade técnica no nosso servidor. Nossa equipe já foi avisada e está trabalhando para normalizar o quanto antes.
                    </p>
                    <div class="botoes">
                        <a href="<c:out value='${urlTentarNovamente}' />" class="botao botao-principal">
                            Tentar novamente
                        </a>
                    </div>
                </c:otherwise>

            </c:choose>

        </section>
    </main>

</body>
</html>
