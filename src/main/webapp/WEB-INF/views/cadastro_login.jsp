<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Tela única do cadastro. Mostra uma etapa por vez, escolhida pelo servidor (etapaCadastro); cada etapa fica em cadastro/*.jsp --%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>EcoCiente - Login</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/favicon.png">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Instrument+Sans:ital,wght@0,400..700;1,400..700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;600;700&display=swap" rel="stylesheet">

    <%-- Todos os estilos desta tela ficam aqui dentro (não vêm da pasta css/) --%>
    <style>
        /* Zera as margens e faz o tamanho dos elementos incluir borda e espaço interno */
        * {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        /* Fundo e alinhamento geral da página */
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

        /* Estrutura: logo à esquerda e cartão do cadastro à direita */
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

        /* Moldura de vidro em volta do cartão branco */
        .card-wrapper {
            background: rgba(255, 255, 255, 0.15);
            backdrop-filter: blur(8px);
            padding: 12px;
            border-radius: 20px;
            border: 1px solid rgba(255, 255, 255, 0.2);
            justify-self: end;
            width: 100%;
            max-width: 500px;
        }

        /* Cartão branco onde aparecem as etapas do cadastro */
        .container-login {
            position: relative;
            background-color: #ffffff;
            border-radius: 12px;
            padding: 28px 32px;
            width: 100%;
        }

        /* Caixa invisível que controla o olho de mostrar a senha */
        .ver-senha {
            position: absolute;
            width: 1px;
            height: 1px;
            overflow: hidden;
            clip: rect(0 0 0 0);
            white-space: nowrap;
            opacity: 0;
        }

        /* Título e subtítulo de cada etapa */
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

        /* Campo do formulário: rótulo em cima e caixa embaixo */
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

        /* Linha com campos lado a lado */
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

        /* Caixa de texto */
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

        /* Senha: o olho alterna entre escondida e visível */
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

        /* Link "Esqueceu sua senha?" (tela de login) */
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

        /* Botão principal de cada etapa */
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

        /* Botão Voltar, no canto da página */
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

        /* Mensagem de erro de uma etapa */
        .mensagem-erro {
            font-size: 12px;
            padding: 8px 10px;
            border-radius: 6px;
            margin-bottom: 16px;
            background-color: #FCE7EF;
            color: #9D174D;
            border-left: 3px solid #E05282;
        }


        .form-row {
            margin-bottom: 18px;
        }

        .form-row .form-group {
            margin-bottom: 0;
        }

        /* Variações da linha: 3 colunas, e rua + número */
        .form-row.tres {
            grid-template-columns: 1fr 1fr 1.3fr;
        }

        .form-row.rua-numero {
            grid-template-columns: minmax(0, 1fr) 90px;
        }

        .form-group label .opcional {
            font-weight: 400;
            color: #6B7370;
        }

        /* Texto de ajuda embaixo do campo */
        .dica-campo {
            font-size: 11px;
            color: #6B7370;
            margin-top: 6px;
        }

        .input-field[type="number"] {
            -moz-appearance: textfield;
            appearance: textfield;
        }

        .input-field[type="number"]::-webkit-inner-spin-button,
        .input-field[type="number"]::-webkit-outer-spin-button {
            -webkit-appearance: none;
            margin: 0;
        }

        /* Lista de seleção (estado) */
        select.input-field {
            appearance: none;
            -webkit-appearance: none;
            cursor: pointer;
            padding-right: 18px;
            background: transparent url('${pageContext.request.contextPath}/assets/icone-chevron.svg') right 0 center / 12px 8px no-repeat;
        }

        select.input-field:invalid {
            color: #7A8B84;
        }

        select.input-field option {
            color: #333333;
        }

        /* Botões do fim de cada etapa: avançar e voltar */
        .botoes {
            display: flex;
            gap: 12px;
            margin: 4px 0 16px;
        }

        .botoes .botao-submit {
            flex: 1;
            margin: 0;
            display: flex;
            align-items: center;
            justify-content: center;
            text-decoration: none;
            border: 1px solid #064E3B;
        }

        .botoes .botao-secundario {
            order: -1;
            background-color: #ffffff;
            color: #064E3B;
        }

        .botoes .botao-secundario:hover {
            background-color: #DFEADE;
        }

        .botoes.coluna {
            flex-direction: column;
        }

        .botoes.coluna .botao-secundario {
            order: 0;
        }

        /* Link de apoio embaixo do formulário */
        .link-entrar {
            text-align: center;
            font-size: 12px;
            color: #333333;
        }

        .link-entrar a,
        .link-entrar label {
            color: #064E3B;
            font-weight: 600;
            text-decoration: underline;
            cursor: pointer;
        }

        /* Aviso de proteção de dados (LGPD) */
        .aviso-lgpd {
            display: flex;
            align-items: center;
            gap: 10px;
            background-color: #ECF4F1;
            border-radius: 8px;
            padding: 10px 12px;
            margin: 0 0 14px;
            font-size: 11px;
            line-height: 1.4;
            color: #064E3B;
        }

        .aviso-lgpd img {
            flex-shrink: 0;
        }

        /* Caixa de aceite dos termos de uso */
        .aceite-termos {
            display: flex;
            align-items: center;
            gap: 8px;
            font-size: 12px;
            color: #333333;
            margin-bottom: 18px;
        }

        .aceite-termos a {
            color: #064E3B;
            font-weight: 600;
        }

        .checkbox-termos {
            appearance: none;
            -webkit-appearance: none;
            flex-shrink: 0;
            width: 16px;
            height: 16px;
            border: 1.5px solid #064E3B;
            border-radius: 4px;
            background: #ffffff center / 11px 9px no-repeat;
            cursor: pointer;
            transition: background-color 0.2s ease;
        }

        .checkbox-termos:checked {
            background-color: #064E3B;
            background-image: url('${pageContext.request.contextPath}/assets/icone-check.svg');
        }

        .checkbox-termos:focus-visible {
            outline: 2px solid #E05282;
            outline-offset: 2px;
        }

        /* ----- Cards de escolha (Perfil, Tipo de condomínio, Moradia) ----- */
        .opcoes {
            display: grid;
            grid-template-columns: repeat(3, minmax(0, 1fr));
            gap: 10px;
            margin-bottom: 20px;
        }

        .opcoes.duas {
            grid-template-columns: repeat(2, minmax(0, 1fr));
        }

        .opcao {
            position: relative;
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 6px;
            padding: 16px 10px 14px;
            border: 1px solid #D5E2DA;
            border-radius: 8px;
            text-align: center;
            cursor: pointer;
            transition: border-color 0.2s ease, background-color 0.2s ease;
        }

        .opcao:hover {
            border-color: #8FA29A;
        }

        .opcao input {
            position: absolute;
            opacity: 0;
            width: 1px;
            height: 1px;
            pointer-events: none;
        }

        .opcao:has(input:checked) {
            border: 1.5px solid #064E3B;
            background-color: #ECF4F1;
        }

        .opcao:has(input:focus-visible) {
            outline: 2px solid #E05282;
            outline-offset: 2px;
        }

        .opcao-check {
            display: none;
            position: absolute;
            top: 6px;
            right: 6px;
            width: 16px;
            height: 16px;
            border-radius: 50%;
            background: #064E3B url('${pageContext.request.contextPath}/assets/icone-check.svg') center / 9px 8px no-repeat;
        }

        .opcao:has(input:checked) .opcao-check {
            display: block;
        }

        .opcao-icone {
            height: 38px;
            width: auto;
            margin-bottom: 4px;
        }

        .opcao-titulo {
            font-family: 'Montserrat', sans-serif;
            font-weight: 600;
            font-size: 12px;
            color: #064E3B;
        }

        .opcao-desc {
            font-size: 10px;
            line-height: 1.4;
            color: #6B7370;
        }

        /* Campo do código (token) do condomínio */
        .campo-token {
            --caixa: 40px;
            --espaco: 8px;
            display: block;
            width: calc(6 * (var(--caixa) + var(--espaco)) + (var(--caixa) - 1ch) / 2 + 4px);
            height: 46px;
            border: none;
            outline: none;
            font-family: 'Courier New', monospace;
            font-size: 22px;
            font-weight: 700;
            color: #064E3B;
            text-transform: uppercase;
            letter-spacing: calc(var(--caixa) + var(--espaco) - 1ch);
            padding-left: calc((var(--caixa) - 1ch) / 2);
            background: repeating-linear-gradient(90deg, #ECF4F1 0 var(--caixa), transparent var(--caixa) calc(var(--caixa) + var(--espaco)));
            background-size: calc(6 * (var(--caixa) + var(--espaco)) - var(--espaco)) 100%;
            background-repeat: no-repeat;
            border-bottom: 2px solid transparent;
            overflow: hidden;
        }

        .campo-token:focus {
            background: repeating-linear-gradient(90deg, #DCEBE4 0 var(--caixa), transparent var(--caixa) calc(var(--caixa) + var(--espaco)));
            background-size: calc(6 * (var(--caixa) + var(--espaco)) - var(--espaco)) 100%;
            background-repeat: no-repeat;
        }

        /* Texto de ajuda sobre o token */
        .token-ajuda {
            display: flex;
            flex-direction: column;
            gap: 4px;
            margin: 8px 0 16px;
            font-size: 11px;
            color: #6B7370;
        }

        .token-ajuda a {
            color: #064E3B;
            font-weight: 600;
        }

        .info-token {
            border-left: 3px solid #064E3B;
            background-color: #ECF4F1;
            border-radius: 0 8px 8px 0;
            padding: 10px 12px;
            margin-bottom: 18px;
            font-size: 11px;
            line-height: 1.45;
            color: #064E3B;
        }

        .info-token strong {
            display: block;
            margin-bottom: 2px;
        }

        /* Etiqueta "Etapa 1 de 2" do pagamento */
        .rotulo-etapa {
            font-size: 10px;
            font-weight: 700;
            letter-spacing: 0.06em;
            text-transform: uppercase;
            color: #D64573;
            margin-bottom: 6px;
        }

        /* Texto só para leitores de tela (não aparece na tela) */
        .so-leitor {
            position: absolute;
            width: 1px;
            height: 1px;
            overflow: hidden;
            clip: rect(0 0 0 0);
            white-space: nowrap;
            border: 0;
            padding: 0;
        }

        /* Pagamento: resumo do plano, ciclo de cobrança e quantidade */
        .plano-escolhido {
            display: flex;
            align-items: center;
            gap: 12px;
            border: 1px solid #064E3B;
            border-radius: 10px;
            padding: 12px 14px;
            margin-bottom: 16px;
        }

        .plano-icone {
            height: 36px;
            width: auto;
            flex-shrink: 0;
        }

        .plano-info {
            display: flex;
            flex-direction: column;
            gap: 2px;
            min-width: 0;
            flex: 1;
        }

        .rotulo-mini {
            font-size: 9px;
            font-weight: 700;
            letter-spacing: 0.06em;
            text-transform: uppercase;
            color: #D64573;
        }

        .plano-info strong {
            font-family: 'Montserrat', sans-serif;
            font-size: 14px;
            color: #064E3B;
        }

        .plano-info span:last-child {
            font-size: 11px;
            color: #6B7370;
        }

        .ciclo {
            border: none;
            margin-bottom: 16px;
        }

        .ciclo legend {
            color: #064E3B;
            font-family: 'Montserrat', sans-serif;
            font-weight: 700;
            font-size: 13px;
            margin-bottom: 6px;
        }

        /* Botões Mensal e Anual */
        .seletor-ciclo {
            display: inline-flex;
            border: 1px solid #064E3B;
            border-radius: 50px;
            padding: 2px;
        }

        .seletor-ciclo button {
            border: none;
            background: transparent;
            border-radius: 50px;
            padding: 6px 22px;
            font-family: 'Montserrat', sans-serif;
            font-weight: 600;
            font-size: 12px;
            color: #064E3B;
            cursor: pointer;
        }

        .seletor-ciclo button.ativo {
            background-color: #064E3B;
            color: #ffffff;
        }

        .seletor-ciclo button:focus-visible,
        .stepper button:focus-visible {
            outline: 2px solid #E05282;
            outline-offset: 2px;
        }

        /* Campo de quantidade com botões de mais e menos */
        .quantidade {
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 12px;
            margin-bottom: 14px;
        }

        .quantidade label {
            display: block;
            color: #064E3B;
            font-family: 'Montserrat', sans-serif;
            font-weight: 700;
            font-size: 13px;
        }

        .quantidade .dica-campo {
            margin-top: 2px;
        }

        .stepper {
            display: flex;
            align-items: stretch;
            border: 1px solid #C7D6D1;
            border-radius: 8px;
            overflow: hidden;
            flex-shrink: 0;
        }

        .stepper button {
            width: 34px;
            border: none;
            background: #ffffff;
            cursor: pointer;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .stepper button:hover {
            background-color: #ECF4F1;
        }

        .stepper input {
            width: 64px;
            border: none;
            border-left: 1px solid #C7D6D1;
            border-right: 1px solid #C7D6D1;
            text-align: center;
            font-family: 'Montserrat', sans-serif;
            font-weight: 600;
            font-size: 14px;
            color: #064E3B;
            padding: 8px 4px;
            outline: none;
        }

        .stepper input:focus {
            background-color: #ECF4F1;
        }

        .quantidade-campo {
            display: flex;
            flex-direction: column;
            align-items: flex-end;
            gap: 6px;
            flex-shrink: 0;
        }

        .atualizar {
            border: none;
            background: none;
            padding: 0;
            font-family: 'Instrument Sans', sans-serif;
            font-size: 12px;
            font-weight: 600;
            color: #064E3B;
            text-decoration: underline;
            cursor: pointer;
        }

        .atualizar:focus-visible {
            outline: 2px solid #E05282;
            outline-offset: 2px;
        }

        /* Quadro "como chegamos nesse valor" do plano anual */
        .calculo-anual {
            margin: 14px 0 18px;
            padding: 12px 14px;
            border-radius: 8px;
            background-color: #ECF4F1;
            font-size: 12px;
            color: #064E3B;
        }

        .calculo-anual .calculo-titulo {
            font-family: 'Montserrat', sans-serif;
            font-weight: 700;
            font-size: 12px;
            margin-bottom: 8px;
        }

        .calculo-anual dl {
            display: grid;
            gap: 6px;
        }

        .calculo-anual dl div {
            display: flex;
            justify-content: space-between;
            gap: 12px;
        }

        .calculo-anual dt {
            color: #6B7370;
            white-space: nowrap;
        }

        .calculo-anual dd {
            font-weight: 600;
            text-align: right;
        }

        .calculo-anual .calculo-total {
            padding-top: 6px;
            border-top: 1px solid #C7D6D1;
        }

        .calculo-anual .calculo-total dt,
        .calculo-anual .calculo-total dd {
            color: #064E3B;
            font-weight: 700;
        }

        .calculo-anual .economia {
            margin-top: 8px;
            color: #0A6A50;
        }

        .divisor-resumo {
            border: none;
            border-top: 1px solid #C7D6D1;
            margin-bottom: 14px;
        }

        /* Linha do valor total */
        .total {
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 12px;
        }

        .total strong {
            display: block;
            font-family: 'Montserrat', sans-serif;
            font-size: 13px;
            color: #064E3B;
        }

        .total span {
            font-size: 11px;
            color: #6B7370;
        }

        .total .valor-total {
            font-family: 'Montserrat', sans-serif;
            font-weight: 700;
            font-size: 22px;
            color: #064E3B;
            white-space: nowrap;
        }

        /* Pagamento: resumo do pedido e valor a pagar */
        .resumo-pedido {
            display: flex;
            align-items: center;
            justify-content: space-between;
            gap: 12px;
            background-color: #ECF4F1;
            border-radius: 8px;
            padding: 10px 14px;
            margin-bottom: 14px;
        }

        .resumo-pedido strong {
            display: block;
            font-family: 'Montserrat', sans-serif;
            font-size: 12px;
            color: #064E3B;
        }

        .resumo-pedido span {
            font-size: 11px;
            color: #6B7370;
        }

        .resumo-valor {
            text-align: right;
            white-space: nowrap;
        }

        .resumo-valor strong {
            font-size: 15px;
        }

        /* Botões de forma de pagamento: cartão, Pix e boleto */
        .metodos {
            display: grid;
            grid-template-columns: repeat(3, minmax(0, 1fr));
            gap: 8px;
            margin-bottom: 18px;
        }

        .metodo-radio {
            position: absolute;
            width: 1px;
            height: 1px;
            overflow: hidden;
            clip: rect(0 0 0 0);
            opacity: 0;
        }

        .metodo {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            gap: 6px;
            min-height: 58px;
            padding: 8px 6px;
            border: 1px solid #C7D6D1;
            border-radius: 8px;
            font-family: 'Montserrat', sans-serif;
            font-weight: 600;
            font-size: 11px;
            color: #064E3B;
            text-align: center;
            cursor: pointer;
            transition: border-color 0.2s ease, background-color 0.2s ease;
        }

        .metodo:hover {
            border-color: #8FA29A;
        }

        .metodo-radio:checked + .metodo {
            border: 1.5px solid #064E3B;
            background-color: #ECF4F1;
        }

        .metodo-radio:focus-visible + .metodo {
            outline: 2px solid #E05282;
            outline-offset: 2px;
        }

        /* Só o formulário do método marcado aparece */
        .form-metodo {
            display: none;
        }

        .painel-cadastro:has(#metodo-cartao:checked) .form-cartao,
        .painel-cadastro:has(#metodo-pix:checked) .form-pix,
        .painel-cadastro:has(#metodo-boleto:checked) .form-boleto {
            display: block;
            animation: aparecer 0.25s ease;
        }

        /* Pix: QR code, passos e código copia e cola */
        .pix-qr {
            display: flex;
            align-items: center;
            gap: 18px;
            margin-bottom: 14px;
        }

        .pix-qr img {
            width: 128px;
            height: 128px;
            flex-shrink: 0;
        }

        .passos {
            list-style: none;
            counter-reset: passo;
            display: grid;
            gap: 8px;
            margin-bottom: 8px;
        }

        .passos li {
            counter-increment: passo;
            display: flex;
            gap: 8px;
            font-size: 11px;
            line-height: 1.4;
            color: #333333;
        }

        .passos li::before {
            content: counter(passo);
            flex-shrink: 0;
            width: 18px;
            height: 18px;
            border-radius: 50%;
            background-color: #064E3B;
            color: #ffffff;
            font-size: 10px;
            font-weight: 700;
            display: flex;
            align-items: center;
            justify-content: center;
        }

        .expira {
            font-size: 11px;
            font-weight: 600;
            color: #D64573;
        }

        .copia-cola {
            margin-bottom: 4px;
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
            font-size: 11px;
            color: #333333;
            word-break: break-all;
            user-select: all;
            -webkit-user-select: all;
            cursor: text;
        }

        .copia-cola-codigo:focus-visible {
            outline: 2px solid #E05282;
            outline-offset: 2px;
        }

        /* Mensagens de erro dos campos: aparecem quando a pessoa digita algo inválido */
        .form-group::after,
        .aceite-termos::after {
            display: none;
            margin-top: 6px;
            font-size: 13px;
            line-height: 1.35;
            font-weight: 600;
            color: #B42318;
        }

        .form-group:has(:user-invalid)::after {
            display: block;
            content: "Confira este campo.";
        }

        .form-group[data-erro]:has(:user-invalid)::after {
            content: attr(data-erro);
        }

        .form-group:has(input:user-invalid:placeholder-shown)::after {
            content: "Campo obrigatório: preencha este campo.";
        }

        .form-group:has(select:user-invalid)::after {
            content: attr(data-erro);
        }

        .aceite-termos:has(.checkbox-termos:user-invalid)::after {
            display: block;
            content: "Marque a caixa: é preciso aceitar os Termos de uso e a Política de privacidade.";
        }

        .input-field:user-invalid {
            border-bottom-color: #B42318;
        }

        .campo-token:user-invalid {
            border-bottom-color: #B42318;
        }

        .form-group:has(:user-invalid) > label {
            color: #B42318;
        }

        .mensagem-erro {
            font-size: 14px;
            font-weight: 600;
            padding: 12px 14px;
        }

        /* Telas médias */
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

            .form-row,
            .form-row.tres,
            .form-row.rua-numero {
                grid-template-columns: minmax(0, 1fr);
                gap: 18px;
            }
        }

        /* Celulares */
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

            .opcoes,
            .opcoes.duas {
                grid-template-columns: minmax(0, 1fr);
            }

            .opcao {
                flex-direction: row;
                flex-wrap: wrap;
                justify-content: flex-start;
                column-gap: 12px;
                text-align: left;
                padding: 12px 36px 12px 12px;
            }

            .opcao-icone {
                height: 30px;
                margin: 0;
            }

            .opcao-desc {
                flex-basis: 100%;
            }

            .campo-token {
                --caixa: 36px;
                --espaco: 6px;
            }

            .input-field {
                font-size: 16px;
            }

            .input-field::placeholder {
                font-size: 14px;
            }
        }

        @media (max-width: 700px) {
            .pix-qr {
                flex-direction: column;
                align-items: flex-start;
            }

            .plano-escolhido {
                flex-wrap: wrap;
            }

            .quantidade {
                flex-direction: column;
                align-items: flex-start;
            }

            .stepper button {
                width: 44px;
            }

            .quantidade-campo {
                align-items: flex-start;
            }

            .calculo-anual dl div {
                flex-direction: column;
                gap: 2px;
            }

            .calculo-anual dd {
                text-align: left;
            }

            .stepper input {
                font-size: 16px;
            }
        }
    </style>
</head>
<body>

<%-- Volta para a página inicial --%>
<a href="${pageContext.request.contextPath}/inicio" class="botao-voltar" aria-label="Voltar para a página inicial">
    <span class="seta" aria-hidden="true">&larr;</span>
    <span>Voltar</span>
</a>

<%-- Logo à esquerda e cartão à direita --%>
<div class="container">

    <div class="logotipo">
        <img src="${pageContext.request.contextPath}/assets/logotipo-login.svg" alt="EcoCiente">
    </div>

    <div class="card-wrapper">
        <div class="container-login">

                <c:set var="ctx" value="${pageContext.request.contextPath}" />
                <%-- cad guarda o que a pessoa já preencheu nas etapas anteriores (fica na sessão) --%>
                <c:set var="cad" value="${sessionScope.cadastroPendente}" />
                <div class="painel painel-cadastro">
                    <%-- Escolhe qual etapa mostrar, conforme o servidor mandou em etapaCadastro --%>
                    <c:choose>
                        <%-- Veio da rota /login: mostra o login no lugar do cadastro --%>
                        <c:when test="${requestScope['jakarta.servlet.forward.servlet_path'] == '/login'}">
                            <%@ include file="cadastro/login.jsp" %>
                        </c:when>
                        <c:when test="${etapaCadastro == 'pag-dados'}">
                            <%@ include file="cadastro/pag_dados.jsp" %>
                        </c:when>
                        <c:when test="${etapaCadastro == 'pag-resumo'}">
                            <%@ include file="cadastro/pag_resumo.jsp" %>
                        </c:when>
                        <c:when test="${etapaCadastro == 'pag-pagamento'}">
                            <%@ include file="cadastro/pag_pagamento.jsp" %>
                        </c:when>
                        <c:when test="${etapaCadastro == 'perfil'}">
                            <%@ include file="cadastro/perfil.jsp" %>
                        </c:when>
                        <c:when test="${etapaCadastro == 'tipo'}">
                            <%@ include file="cadastro/tipo.jsp" %>
                        </c:when>
                        <c:when test="${etapaCadastro == 'condominio'}">
                            <%@ include file="cadastro/condominio.jsp" %>
                        </c:when>
                        <c:when test="${etapaCadastro == 'endereco'}">
                            <%@ include file="cadastro/endereco.jsp" %>
                        </c:when>
                        <c:when test="${etapaCadastro == 'cooperativa-endereco'}">
                            <%@ include file="cadastro/cooperativa_endereco.jsp" %>
                        </c:when>
                        <c:when test="${etapaCadastro == 'cooperativa-dados' or etapaCadastro == 'sindico-dados' or etapaCadastro == 'morador-dados'}">
                            <%@ include file="cadastro/dados_pessoais.jsp" %>
                        </c:when>
                        <c:when test="${etapaCadastro == 'moradia'}">
                            <%@ include file="cadastro/moradia.jsp" %>
                        </c:when>
                        <c:when test="${etapaCadastro == 'token'}">
                            <%@ include file="cadastro/token.jsp" %>
                        </c:when>
                        <c:when test="${etapaCadastro == 'morador-endereco'}">
                            <%@ include file="cadastro/morador_endereco.jsp" %>
                        </c:when>
                        <c:when test="${etapaCadastro == 'sindico-token'}">
                            <%@ include file="cadastro/sindico_token.jsp" %>
                        </c:when>
                        <c:when test="${etapaCadastro == 'plano'}">
                            <%@ include file="cadastro/plano.jsp" %>
                        </c:when>
                        <%-- Sem etapa definida: começa pela criação da conta (e-mail e senha) --%>
                        <c:otherwise>
                            <%@ include file="cadastro/conta.jsp" %>
                        </c:otherwise>
                    </c:choose>
                </div>
        </div>
    </div>
</div>

</body>
</html>
