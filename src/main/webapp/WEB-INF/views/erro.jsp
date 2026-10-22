<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isErrorPage="true" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<%--
    Tela genérica de erro da aplicação.
    Mapeada no web.xml via <error-page> para os códigos 404 e 500
    (o "505" do design do Figma é tratado aqui como "erro de servidor" genérico,
    já que o container nunca chega a despachar um 505 real para a aplicação).
--%>
<c:set var="codigo" value="${pageContext.errorData.statusCode}" />
<c:set var="eh404" value="${codigo == 404}" />
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>EcoCiente - ${eh404 ? 'Página não encontrada' : 'Erro no servidor'}</title>
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
            <c:choose>

                <%-- ===== 404 - Página não encontrada ===== --%>
                <c:when test="${eh404}">
                    <img class="icone-status" src="${ctx}/assets/status-recusado.svg" width="80" height="80" alt="">
                    <p class="situacao">Algo deu errado</p>
                    <p class="codigo-erro">ERRO: 404</p>
                    <h1 class="titulo-erro" id="titulo-erro">Página <span class="destaque">não encontrada</span></h1>
                    <p class="texto-erro">O endereço que você tentou acessar não existe, foi movido ou está temporariamente indisponível.</p>

                    <div class="botoes">
                        <a href="${ctx}/inicio" class="botao botao-principal">Ir para o início</a>
                    </div>
                </c:when>

                <%-- ===== 500/505 - Erro no servidor ===== --%>
                <c:otherwise>
                    <img class="icone-status" src="${ctx}/assets/status-recusado.svg" width="80" height="80" alt="">
                    <p class="situacao">Algo deu errado</p>
                    <p class="codigo-erro">ERRO: ${not empty codigo ? codigo : 500}</p>
                    <h1 class="titulo-erro" id="titulo-erro">Erro <span class="destaque">no servidor</span></h1>
                    <p class="texto-erro">Estamos com uma instabilidade técnica no nosso servidor. Nossa equipe já foi avisada e está trabalhando para normalizar o quanto antes.</p>

                    <div class="botoes">
                        <a href="${not empty pageContext.errorData.requestURI ? pageContext.errorData.requestURI : ctx.concat('/inicio')}" class="botao botao-principal">Tentar novamente</a>
                    </div>
                </c:otherwise>

            </c:choose>
        </section>
    </main>

</body>
</html>
