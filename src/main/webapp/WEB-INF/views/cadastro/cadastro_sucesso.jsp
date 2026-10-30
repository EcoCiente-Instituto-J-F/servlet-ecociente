<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Tela de cadastro concluído (mostra o código do condomínio só para o síndico) --%>
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

        /* Aviso de que morador e cooperativa não precisam de login */
        .aviso-login {
            width: 100%;
            padding: 10px 14px;
            border-radius: 8px;
            background-color: #F4F8F5;
            color: #064E3B;
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
        /* ----- Token do condomínio (só aparece para o síndico) ----- */
        .bloco-token {
            width: 100%;
            max-width: 420px;
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 8px;
            padding: 16px;
            border: 1px dashed #064E3B;
            border-radius: 10px;
            background-color: #F4F8F5;
        }

        .rotulo-token {
            color: #064E3B;
            font-family: 'Montserrat', sans-serif;
            font-weight: 600;
            font-size: 13px;
        }

        .token-codigo {
            color: #D64573;
            font-family: 'Courier New', monospace;
            font-weight: 700;
            font-size: 28px;
            letter-spacing: 6px;
            user-select: all;
        }

        .dica-token {
            color: #6B7370;
            font-size: 12px;
            line-height: 1.4;
        }

    </style>
</head>
<body>

    <%-- Cartão central com a mensagem de sucesso --%>
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
                <%-- O texto muda conforme o perfil que acabou de ser criado (perfilCriado) --%>
                <c:choose>
                    <c:when test="${perfilCriado == 'residencial'}">Agora é só convidar os moradores e acompanhar os dados do condomínio.</c:when>
                    <c:when test="${perfilCriado == 'comercial'}">Agora é só convidar as empresas e acompanhar os dados do condomínio.</c:when>
                    <c:when test="${perfilCriado == 'residencial-comercial'}">Agora é só convidar os moradores e as empresas e acompanhar os dados do condomínio.</c:when>
                    <c:when test="${perfilCriado == 'industrial'}">Agora é só acompanhar os resíduos e indicadores do condomínio.</c:when>
                    <c:when test="${perfilCriado == 'morador'}">Agora é só separar, descartar e acompanhar o seu impacto.</c:when>
                    <c:otherwise>Agora é só aproveitar as vantagens que o EcoCiente tem a oferecer.</c:otherwise>
                </c:choose>
            </p>

            <%-- Morador e cooperativa não assinam plano: avisa que não precisam de login --%>
            <c:if test="${perfilCriado == 'morador' or perfilCriado == 'cooperativa'}">
                <p class="texto-sucesso aviso-login" role="note"><strong>Você não precisa fazer login.</strong> O login é só para síndicos que vão assinar um plano.</p>
            </c:if>

            <c:if test="${not empty tokenCondominio}">
                <div class="bloco-token">
                    <p class="rotulo-token">Código do seu condomínio</p>
                    <p class="token-codigo" id="token-condominio"><c:out value="${tokenCondominio}" /></p>
                    <button type="button" class="botao botao-secundario" id="copiar-token">Copiar código</button>
                    <p class="dica-token">Compartilhe este código com quem for se vincular ao condomínio. Anote agora: ele só aparece nesta tela.</p>
                </div>
            </c:if>

            <%-- Se o servidor oferece plano, leva à escolha do plano; senão, volta para a tela de entrada --%>
            <div class="botoes">
                <c:choose>
                    <c:when test="${oferecerPlano}">
                        <a href="${pageContext.request.contextPath}/inicio" class="botao botao-secundario">Voltar ao início</a>
                        <a href="${pageContext.request.contextPath}/cadastro/plano" class="botao botao-principal">${planoPendente ? 'Continuar para o pagamento' : 'Fazer assinatura'}</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${pageContext.request.contextPath}/inicio" class="botao botao-principal">Voltar ao início</a>
                    </c:otherwise>
                </c:choose>
            </div>


        </section>
    </main>

    <%-- Botão que copia o código do condomínio --%>
    <script>
        (function () {
            var botao = document.getElementById('copiar-token');
            if (!botao) return;
            botao.addEventListener('click', function () {
                var codigo = document.getElementById('token-condominio').textContent.trim();
                var avisar = function () {
                    botao.textContent = 'Código copiado!';
                    setTimeout(function () { botao.textContent = 'Copiar código'; }, 2000);
                };
                if (navigator.clipboard && navigator.clipboard.writeText) {
                    navigator.clipboard.writeText(codigo).then(avisar);
                }
            });
        })();
    </script>

</body>
</html>
