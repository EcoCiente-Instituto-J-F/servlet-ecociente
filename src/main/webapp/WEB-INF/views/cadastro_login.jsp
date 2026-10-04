<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>EcoCiente - Login</title>
    <link rel="icon" href="assets/favicon.png">
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
            font-family: 'Instrument Sans', sans-serif;
        }

        .container {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 40px;
            width: 100%;
            max-width: 1100px;
            padding: 20px;
            align-items: center;
        }

        .logotipo img {
            max-width: 100%;
            height: auto;
            display: block;
        }

        .card-wrapper {
            background: rgba(255, 255, 255, 0.15);
            backdrop-filter: blur(8px);
            padding: 12px;
            border-radius: 20px;
            border: 1px solid rgba(255, 255, 255, 0.2);
            justify-self: end;
            width: 100%;
            max-width: 440px;
        }

        .container-login {
            position: relative;
            background-color: #ffffff;
            border-radius: 12px;
            padding: 28px 32px;
            width: 100%;
        }


        .aba-radio,
        .ver-senha {
            position: absolute;
            width: 1px;
            height: 1px;
            overflow: hidden;
            clip: rect(0 0 0 0);
            white-space: nowrap;
            opacity: 0;
        }

        .tabs {
            display: flex;
            width: 100%;
            background-color: #ffffff;
            border: 1px solid #064E3B;
            padding: 3px;
            border-radius: 50px;
            margin-bottom: 24px;
        }

        .tab-botao {
            flex: 1;
            padding: 7px 12px;
            border-radius: 50px;
            color: #064E3B;
            font-family: 'Montserrat', sans-serif;
            font-weight: 600;
            font-size: 13px;
            cursor: pointer;
            text-align: center;
            user-select: none;
            transition: all 0.25s ease;
        }

        #aba-login:checked ~ .tabs .tab-botao[for="aba-login"],
        #aba-cadastro:checked ~ .tabs .tab-botao[for="aba-cadastro"] {
            background-color: #064E3B;
            color: #ffffff;
        }

        #aba-login:focus-visible ~ .tabs .tab-botao[for="aba-login"],
        #aba-cadastro:focus-visible ~ .tabs .tab-botao[for="aba-cadastro"] {
            outline: 2px solid #E05282;
            outline-offset: -2px;
        }

        .painel {
            display: none;
        }

        #aba-login:checked ~ .painel-login,
        #aba-cadastro:checked ~ .painel-cadastro {
            display: block;
            animation: aparecer 0.3s ease;
        }

        @keyframes aparecer {
            from { opacity: 0; transform: translateY(6px); }
            to   { opacity: 1; transform: translateY(0); }
        }

        .titulo-container {
            margin-bottom: 12px;
        }

        .titulo-padrao {
            color: #064E3B;
            font-family: 'Montserrat', sans-serif;
            font-weight: 700;
            font-size: 20px;
            display: inline-block;
            position: relative;
        }

        .titulo-padrao::after {
            content: '';
            position: absolute;
            left: 0;
            bottom: -3px;
            width: 100%;
            height: 2px;
            background-color: #E05282;
        }

        .subtitulo {
            font-size: 12px;
            color: #333333;
            line-height: 1.4;
            margin-bottom: 20px;
        }

        .form-group {
            margin-bottom: 18px;
            position: relative;
        }

        .form-group label {
            display: block;
            color: #064E3B;
            font-family: 'Montserrat', sans-serif;
            font-weight: 700;
            font-size: 13px;
            margin-bottom: 4px;
        }

        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 16px;
        }

        .input-wrapper {
            position: relative;
            display: flex;
            align-items: center;
        }

        .input-field {
            width: 100%;
            border: none;
            border-bottom: 1.5px solid #8FA29A;
            padding: 6px 0;
            font-size: 13px;
            color: #333;
            outline: none;
            background: transparent;
        }

        .input-field::placeholder {
            color: #7A8B84;
            font-size: 12px;
        }

        .input-field:focus {
            border-bottom-color: #064E3B;
        }

        .campo-senha {
            -webkit-text-security: disc;
        }

        .form-group:has(.ver-senha:checked) .campo-senha {
            -webkit-text-security: none;
        }


        .form-group .toggle-password {
            position: absolute;
            right: 0;
            margin-bottom: 0;
            cursor: pointer;
            color: #064E3B;
            display: flex;
            align-items: center;
        }

        .toggle-password .risco {
            display: none;
        }

        .form-group:has(.ver-senha:checked) .toggle-password .risco {
            display: inline;
        }

        .ver-senha:focus-visible + .toggle-password {
            outline: 2px solid #E05282;
            outline-offset: 2px;
            border-radius: 4px;
        }

        .input-wrapper:has(.toggle-password) .input-field {
            padding-right: 24px;
        }

        .forgot-password {
            display: block;
            text-align: right;
            font-size: 11px;
            color: #064E3B;
            font-weight: 600;
            text-decoration: none;
            margin-top: 6px;
        }

        .forgot-password:hover {
            text-decoration: underline;
        }

        .botao-submit {
            width: 100%;
            background-color: #064E3B;
            color: white;
            border: none;
            padding: 10px;
            border-radius: 6px;
            font-family: 'Montserrat', sans-serif;
            font-weight: 600;
            font-size: 14px;
            cursor: pointer;
            margin-top: 16px;
            margin-bottom: 16px;
        }

        .divider {
            display: flex;
            align-items: center;
            text-align: center;
            color: #717171;
            font-size: 11px;
            margin-bottom: 16px;
        }

        .divider::before,
        .divider::after {
            content: '';
            flex: 1;
            border-bottom: 1px solid #ccc;
        }

        .divider span {
            padding: 0 10px;
            position: relative;
            top: -1px;
        }

        .botao-google {
            width: 100%;
            background-color: #ffffff;
            border: 1px solid #ccc;
            padding: 8px;
            border-radius: 6px;
            font-family: 'Montserrat', sans-serif;
            font-weight: 600;
            font-size: 12px;
            color: #333;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 6px;
        }

        .google-text-g { color: #4285F4; }
        .google-text-o1 { color: #EA4335; }
        .google-text-o2 { color: #FBBC05; }
        .google-text-g2 { color: #4285F4; }
        .google-text-l { color: #34A853; }
        .google-text-e { color: #EA4335; }

        .botao-voltar {
            position: absolute;
            top: 24px;
            left: 24px;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            background-color: #ffffff;
            color: #064E3B;
            padding: 8px 16px;
            border-radius: 50px;
            font-family: 'Montserrat', sans-serif;
            font-weight: 600;
            font-size: 13px;
            text-decoration: none;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
            transition: background-color 0.2s ease;
        }

        .botao-voltar:hover {
            background-color: #DFEADE;
        }

        .botao-voltar:focus-visible {
            outline: 2px solid #E05282;
            outline-offset: 2px;
        }

        .botao-voltar .seta {
            font-size: 18px;
            line-height: 1;
        }

        .mensagem-erro {
           font-size: 12px;
           padding: 8px 10px;
           border-radius: 6px;
           margin-bottom: 16px;
           background-color: #FCE7EF;
           color: #9D174D;
           border-left: 3px solid #E05282;
        }

        @media (max-width: 900px) {
            body {
                align-items: flex-start;
                padding: 72px 16px 24px;
            }

            .botao-voltar {
                top: 16px;
                left: 16px;
            }

            .container {
                grid-template-columns: minmax(0, 1fr);
                gap: 24px;
                max-width: 520px;
                margin: auto;
            }

            .logotipo img {
                width: min(260px, 70%);
                margin: 0 auto;
            }

            .card-wrapper {
                justify-self: center;
            }

            .form-row {
                grid-template-columns: minmax(0, 1fr);
                gap: 18px;
            }
        }

        @media (max-width: 700px) {
            body {
                padding: 64px 12px 16px;
            }

            .botao-voltar {
                top: 12px;
                left: 12px;
            }

            .container {
                gap: 18px;
                padding: 0;
            }

            .logotipo img {
                width: min(220px, 65%);
            }

            .card-wrapper {
                padding: 8px;
                border-radius: 16px;
            }

            .container-login {
                padding: 24px 20px;
            }

            .input-field {
                font-size: 16px;
            }

            .input-field::placeholder {
                font-size: 14px;
            }

            .botao-submit,
            .botao-google {
                min-height: 44px;
            }
        }

    </style>
</head>
<body>

    <a href="${pageContext.request.contextPath}/index.jsp" class="botao-voltar" aria-label="Voltar para a página inicial">
        <span class="seta" aria-hidden="true">&larr;</span>
        <span>Voltar</span>
    </a>

    <div class="container">

        <div class="logotipo">
            <img src="${pageContext.request.contextPath}/assets/logotipo-login.svg" alt="EcoCiente">
        </div>

        <div class="card-wrapper">
            <div class="container-login">

               <input type="radio" name="aba" id="aba-login" class="aba-radio" ${param.aba != 'cadastro' ? 'checked' : ''}>
               <input type="radio" name="aba" id="aba-cadastro" class="aba-radio" ${param.aba == 'cadastro' ? 'checked' : ''}>

                <div class="tabs">
                    <label for="aba-login" class="tab-botao">Login</label>
                    <label for="aba-cadastro" class="tab-botao">Cadastro</label>
                </div>

                <div class="painel painel-login">

                    <div class="titulo-container">
                        <h3 class="titulo-padrao">Acesse sua conta</h3>
                    </div>
                    <p class="subtitulo">
                        Bem-vindo de volta!<br>
                        Continue gerenciando seus dados de onde parou.
                    </p>

                    <form action="${pageContext.request.contextPath}/login" method="POST">
                        <div class="form-group">
                            <label for="email">E-mail ou usuário</label>
                            <div class="input-wrapper">
                                <input class="input-field" type="text" id="email" name="email" value="<c:out value='${param.email}' />" placeholder="Digite seu e-mail">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="senha">Senha</label>
                            <div class="input-wrapper">
                                <input class="input-field campo-senha" type="text" id="senha" name="senha" placeholder="Digite sua senha" autocomplete="off" autocapitalize="off" autocorrect="off" spellcheck="false">

                                <input type="checkbox" id="ver-senha-login" class="ver-senha" aria-label="Mostrar senha">
                                <label for="ver-senha-login" class="toggle-password">
                                    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                                        <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                                        <circle cx="12" cy="12" r="3"></circle>
                                        <line class="risco" x1="2" y1="2" x2="22" y2="22"></line>
                                    </svg>
                                </label>
                            </div>
                            <a href="#" class="forgot-password">Esqueceu sua senha?</a>
                        </div>

                        <c:if test="${not empty erroLogin}">
                            <p class="mensagem-erro"><c:out value="${erroLogin}" /></p>
                        </c:if>

                        <button type="submit" class="botao-submit">Login</button>

                        <div class="divider">
                            <span>ou</span>
                        </div>

                        <button type="button" class="botao-google">
                            Continuar com
                            <span>
                                <strong class="google-text-g">G</strong><strong class="google-text-o1">o</strong><strong class="google-text-o2">o</strong><strong class="google-text-g2">g</strong><strong class="google-text-l">l</strong><strong class="google-text-e">e</strong>
                            </span>
                        </button>
                    </form>

                </div>

                <div class="painel painel-cadastro">

                    <div class="titulo-container">
                        <h3 class="titulo-padrao">Crie sua conta</h3>
                    </div>
                    <p class="subtitulo">
                        Bem-vindo!<br>
                        Comece sua jornada consciente de dados.
                    </p>

                   <c:if test="${not empty erroCadastro}">
                       <p class="mensagem-erro"><c:out value="${erroCadastro}" /></p>
                   </c:if>

                    <form action="${pageContext.request.contextPath}/cadastro" method="POST">
                        <input type="hidden" name="aba" value="cadastro">
                        <div class="form-group">
                            <label for="cad-nome" >Nome</label>
                            <div class="input-wrapper">
                                <input class="input-field" type="text" id="cad-nome" name="nome" value="<c:out value='${param.nome}' />" placeholder="Digite seu nome completo" autocomplete="name">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="cad-email" >E-mail</label>
                            <div class="input-wrapper">
                                <input class="input-field" type="email" id="cad-email" name="email" value="<c:out value='${param.email}' />" placeholder="Digite seu e-mail" autocomplete="email">
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="cad-senha">Senha</label>
                            <div class="form-row">
                                <div class="input-wrapper">
                                    <input class="input-field campo-senha" type="text" id="cad-senha" name="senha" placeholder="Digite sua senha" autocomplete="off" autocapitalize="off" autocorrect="off" spellcheck="false">

                                    <input type="checkbox" id="ver-senha-cadastro" class="ver-senha" aria-label="Mostrar senha">
                                    <label for="ver-senha-cadastro" class="toggle-password">
                                        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
                                            <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                                            <circle cx="12" cy="12" r="3"></circle>
                                            <line class="risco" x1="2" y1="2" x2="22" y2="22"></line>
                                        </svg>
                                    </label>
                                </div>
                                <div class="input-wrapper">
                                    <input class="input-field campo-senha" type="text" id="cad-confirma" name="confirma_senha" placeholder="Confirme sua senha" aria-label="Confirme sua senha" autocomplete="off" autocapitalize="off" autocorrect="off" spellcheck="false">
                                </div>
                            </div>
                        </div>

                        <button type="submit" class="botao-submit">Próximo</button>

                    </form>
                </div>

            </div>
        </div>
    </div>

</body>
</html>