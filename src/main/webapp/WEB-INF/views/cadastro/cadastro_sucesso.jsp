<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>EcoCiente - Conta criada</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/favicon.png">
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
            background-image: url('${pageContext.request.contextPath}/assets/tela-fundo-login.png');
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

        .card-sucesso {
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
            border-radius: 50%;
            background-color: #064E3B;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .icone-status img {
            width: 27px;
            height: auto;
        }

        .boas-vindas {
            color: #064E3B;
            font-family: 'Montserrat', sans-serif;
            font-weight: 600;
            font-size: 14px;
        }

        .titulo-sucesso {
            color: #064E3B;
            font-family: 'Montserrat', sans-serif;
            font-weight: 600;
            font-size: 24px;
            line-height: 1.25;
        }

        .titulo-sucesso .destaque {
            color: #D64573;
        }

        .texto-sucesso {
            font-size: 13px;
            line-height: 1.45;
            color: #6B7370;
            max-width: 400px;
        }

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

        .botao:focus-visible {
            outline: 2px solid #E05282;
            outline-offset: 2px;
        }

        @media (max-width: 700px) {
            .card-wrapper {
                padding: 8px;
                border-radius: 16px;
            }

            .card-sucesso {
                padding: 24px 20px;
            }

            .titulo-sucesso {
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
        <section class="card-sucesso" aria-labelledby="titulo-sucesso">

            <div class="icone-status">
                <img src="${pageContext.request.contextPath}/assets/icone-check-big.svg" width="34" height="26" alt="">
            </div>

            <p class="boas-vindas">Seja bem-vindo ao EcoCiente</p>

            <h1 class="titulo-sucesso" id="titulo-sucesso">
                Sua conta foi criada<br>com <span class="destaque">sucesso!</span>
            </h1>

            <p class="texto-sucesso">
                <c:choose>
                    <c:when test="${perfilCriado == 'residencial'}">Agora é só convidar os moradores e acompanhar os dados do condomínio.</c:when>
                    <c:when test="${perfilCriado == 'comercial'}">Agora é só convidar as empresas e acompanhar os dados do condomínio.</c:when>
                    <c:when test="${perfilCriado == 'industrial'}">Agora é só acompanhar os resíduos e indicadores do condomínio.</c:when>
                    <c:when test="${perfilCriado == 'morador'}">Agora é só separar, descartar e acompanhar o seu impacto.</c:when>
                    <c:otherwise>Agora é só aproveitar as vantagens que o EcoCiente tem a oferecer.</c:otherwise>
                </c:choose>
            </p>

            <div class="botoes">
                <c:choose>
                    <c:when test="${oferecerPlano}">
                        <a href="${pageContext.request.contextPath}/login-cadastro" class="botao botao-secundario">Continuar no plano gratuito</a>
                        <a href="${pageContext.request.contextPath}/cadastro/plano" class="botao botao-principal">${planoPendente ? 'Continuar para o pagamento' : 'Escolher meu plano'}</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/login-cadastro" class="botao botao-principal">Voltar para entrar</a>
                    </c:otherwise>
                </c:choose>
            </div>

        </section>
    </main>

</body>
</html>
