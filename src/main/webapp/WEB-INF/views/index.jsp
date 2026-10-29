<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Página inicial (landing page) do EcoCiente: tela pública, sem login --%>
<!DOCTYPE html>
<html lang="pt-BR">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>EcoCiente</title>
    <link rel="icon" href="${pageContext.request.contextPath}/assets/favicon.png">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
    <link href="https://fonts.googleapis.com/css2?family=Instrument+Sans:ital,wght@0,400..700;1,400..700&display=swap" rel="stylesheet">
    <link href="https://fonts.googleapis.com/css2?family=Montserrat:wght@400;500;600;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.7.2/css/all.min.css">
</head>
<body>
<div class="site-ajuste">
    <%-- Topo da página: logo e links de navegação (levam às seções desta mesma página) --%>
    <header class="header">
        <nav class="navbar">
            <div class="logotipo-navbar">
                <img src="${pageContext.request.contextPath}/assets/logotipo-preto.svg" alt="Logo do EcoCiente">
            </div>

            <input type="checkbox" id="abrir-menu" class="entrada-menu">
            <label for="abrir-menu" class="botao-menu" aria-label="Abrir menu">
                <span></span><span></span><span></span>
            </label>

            <ul class="links-navbar">
                <li><a href="#funcionalidades">Funcionalidades</a></li>
                <li><a href="#beneficios">Benefícios</a></li>
                <li><a href="#planos">Planos</a></li>
                <li><a href="#nossa-missao">Nossa Missão</a></li>
                <li><a href="#duvidas">Dúvidas</a></li>
                <li><a href="${pageContext.request.contextPath}/login-cadastro?aba=cadastro" class="botao-navbar">Começar agora <svg class="seta" width="16" height="16" viewBox="-1 -1 16 16" fill="none" aria-hidden="true"><path d="M0 7H14M7 0L14 7L7 14" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg></a></li>
            </ul>
        </nav>
    </header>

    <%-- Faixa de abertura com a chamada principal --%>
    <section class="hero">
        <div class="container-hero">
            <div class="conteudo-hero">
                <div class="logotipo-hero">
                    <img src="${pageContext.request.contextPath}/assets/logotipo.svg" alt="logotipo EcoCiente">
                </div>
                <h1 class="titulo-rosa titulo-hero">
                    Conectamos condomínios e cooperativas
                    <span class="titulo-hero-verde">em uma única plataforma.</span>
                </h1>
                <p class="descricao-hero">
                    Facilitamos a coleta seletiva, reciclagem e fortalecemos a economia circular.
                </p>
                <a href="${pageContext.request.contextPath}/login-cadastro?aba=cadastro" class="botao-hero">
                    <span>Começar agora</span>
                    <svg class="seta" width="16" height="16" viewBox="-1 -1 16 16" fill="none" aria-hidden="true"><path d="M0 7H14M7 0L14 7L7 14" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>
                </a>
            </div>
            <div class="mockups-hero">
                <img src="${pageContext.request.contextPath}/assets/celularesHero.png" alt="Aplicativo EcoCiente em celulares">
            </div>
        </div>
    </section>

    <%-- Conteúdo principal: o que o EcoCiente oferece e o que a pessoa ganha usando --%>
    <div class="container">
        <h2 class="titulo-secao">O EcoCiente oferece</h2>
        <div class="oferece">
            <div class="card-oferece">
                <div class="icone-oferece">
                    <img src="${pageContext.request.contextPath}/assets/icone-grafico.svg?v=2" alt="Icone Redução de Custos">
                </div>
                <span class="titulo-card-oferece">Redução de Custos</span>
                <p class="texto-oferece">Conectamos você a cooperativas parceiras, reduzindo custos com descarte e promovendo uma gestão mais sustentável e eficiente.</p>
            </div>
            <div class="card-oferece">
                <div class="icone-oferece">
                    <img src="${pageContext.request.contextPath}/assets/icone-dinheiro.svg?v=2" alt="Icone valorização">
                </div>
                <span class="titulo-card-oferece">Valorização do Imóvel</span>
                <p class="texto-oferece">Contribuímos para o meio ambiente, práticas sustentáveis aumentam a percepção de qualidade e agregam valor ao imóvel.</p>
            </div>
            <div class="card-oferece">
                <div class="icone-oferece">
                    <img src="${pageContext.request.contextPath}/assets/icone-arvore.svg?v=2" alt="Icone Gestão Ambiental">
                </div>
                <span class="titulo-card-oferece">Gestão Ambiental</span>
                <p class="texto-oferece">Facilitamos a gestão dos resíduos recicláveis e a adoção de práticas alinhadas às normas ambientais, promovendo mais organização e sustentabilidade para o seu condomínio.</p>
            </div>
        </div>

        <div class="card-cond-coop">
            <h2 class="titulo-branco titulo-cond-coop">Com o EcoCiente você ganha:</h2>
            <input type="radio" id="entrada-abas-condominio" name="modo-card" checked>
            <input type="radio" id="entrada-abas-cooperativa" name="modo-card">

            <div class="chave-alterar">
                <label for="entrada-abas-condominio" class="botao-alterar">Condomínio</label>
                <label for="entrada-abas-cooperativa" class="botao-alterar">Cooperativa</label>
            </div>

            <ul class="lista-check lista-vantagens-condominio">
                <li class="item-vantagem">Redução de custos com descarte de resíduos.</li>
                <li class="item-vantagem">Valorização de imóvel.</li>
                <li class="item-vantagem">Cumprimento de leis ambientais.</li>
                <li class="item-vantagem">Impacto positivo na comunidade.</li>
            </ul>

            <ul class="lista-check lista-vantagens-cooperativa">
                <li class="item-vantagem">Aumento do volume de materiais recicláveis.</li>
                <li class="item-vantagem">Conexão com novos condomínios e parceiros.</li>
                <li class="item-vantagem">Fortalecimento da atividade de cooperativas.</li>
                <li class="item-vantagem">Maior organização e eficiência nas coletas.</li>
            </ul>
        </div>

        <%-- Funcionalidades: o que o condomínio precisa para reciclar melhor --%>
        <div class="container-verde" id="funcionalidades">
            <h2 class="titulo-branco titulo-funcionalidades">Tudo que seu condomínio precisa para reciclar melhor</h2>
            <p class="subtitulo-funcionalidades">Conectamos tecnologia, pessoas e cooperativas.</p>

            <ul class="lista-funcionalidades">
                <li class="card-funcionalidades">
                    <div class="imagem-funcionalidades">
                        <img src="${pageContext.request.contextPath}/assets/informe.png" alt="Informe e engaje">
                    </div>
                    <div class="conteudo-card-func">
                        <h3 class="titulo-verde">Informe e engaje</h3>
                        <span class="linha-rosa"></span>
                        <p class="texto-card-func">Preencha seus dados, nos informe sobre a geração de resíduos e solicite um dia fixo disponível na semana.</p>
                    </div>
                </li>

                <li class="card-funcionalidades">
                    <div class="imagem-funcionalidades">
                        <img src="${pageContext.request.contextPath}/assets/coleta.png" alt="Coleta responsável">
                    </div>
                    <div class="conteudo-card-func">
                        <h3 class="titulo-verde">Coleta responsável</h3>
                        <span class="linha-rosa"></span>
                        <p class="texto-card-func">A cooperativa realiza a coleta com compromisso, juntamente de segurança e responsabilidade.</p>
                    </div>
                </li>

                <li class="card-funcionalidades">
                    <div class="imagem-funcionalidades">
                        <img src="${pageContext.request.contextPath}/assets/acompanhe-resultados.png" alt="Acompanhe resultados">
                    </div>
                    <div class="conteudo-card-func">
                        <h3 class="titulo-verde">Acompanhe resultados</h3>
                        <span class="linha-rosa"></span>
                        <p class="texto-card-func">Visualize os impactos positivos do seu condomínio.</p>
                    </div>
                </li>

                <li class="card-funcionalidades">
                    <div class="imagem-funcionalidades">
                        <img src="${pageContext.request.contextPath}/assets/conecte.png" alt="Conecte-se">
                    </div>
                    <div class="conteudo-card-func">
                        <h3 class="titulo-verde">Conecte-se</h3>
                        <span class="linha-rosa"></span>
                        <p class="texto-card-func">Fazemos a conexão com as cooperativas da sua região</p>
                    </div>
                </li>
            </ul>
        </div>

        <%-- Benefícios --%>
        <section class="beneficios" id="beneficios">
            <div class="descricao-beneficios">
                <div class="badge-beneficios">
                    <span class="icone-check"></span>
                    <span>Benefícios</span>
                </div>
                <div class="titulo-branco titulo-beneficios">
                    <p>Seu condomínio <span class="titulo-rosa">merece o melhor</span></p>
                </div>
                <p class="texto-beneficios">Nosso app foi criado para simplificar a gestão de resíduos em condomínios e conectar você às cooperativas de reciclagem. Com ele, você acompanha coletas, registros e parcerias com praticidade, segurança e transparência.</p>

                <div class="grid-icones-beneficios">
                    <div class="item-icone-beneficio">
                        <img src="${pageContext.request.contextPath}/assets/icone-folha-rosa.svg?v=2" alt="Sustentabilidade">
                        <span>Sustentabilidade na prática</span>
                    </div>
                    <div class="item-icone-beneficio">
                        <img src="${pageContext.request.contextPath}/assets/icone-shield-rosa.svg?v=2" alt="Gestão">
                        <span>Gestão que transforma</span>
                    </div>
                    <div class="item-icone-beneficio">
                        <img src="${pageContext.request.contextPath}/assets/people-icon-rosa.svg?v=2" alt="Parcerias">
                        <span>Parcerias que fazem a diferença</span>
                    </div>
                    <div class="item-icone-beneficio">
                        <img src="${pageContext.request.contextPath}/assets/grafico-icon-rosa.svg?v=2" alt="Resultados">
                        <span>Resultados que geram impactos</span>
                    </div>
                </div>
            </div>
        </section>


        <%-- Planos de assinatura: Comunidade (gratuito), Empresarial Residencial e Industrial --%>
        <section class="planos" id="planos">
            <h2 class="titulo-planos">Planos de assinatura</h2>

            <input type="radio" id="mensal-planos" name="periodo-planos" checked>
            <input type="radio" id="anual-planos" name="periodo-planos">

            <div class="chave-planos">
                <label for="mensal-planos" class="botao-periodo">Mensal</label>
                <label for="anual-planos" class="botao-periodo">Anual</label>
            </div>

            <div class="grid-planos">

                <!-- CARD 2: EMPRESARIAL RESIDENCIAL -->
                <article class="card-plano">
                    <div class="cabecalho-card">
                        <img src="${pageContext.request.contextPath}/assets/icone-empresarial-residencial.svg?v=2" alt="Ícone Empresarial" class="icone-plano">
                        <div>
                            <span class="subtitulo-card">PLANO</span>
                            <h3 class="nome-plano">Empresarial Residencial</h3>
                            <p class="descricao-plano">Ideal para condomínios empresariais e residenciais.</p>
                        </div>
                    </div>

                    <div class="preco-container preco-mensal">
                        <span class="valor">R$ 4,00</span>
                        <span class="periodo">Por apartamento ou unidade / Mensal</span>
                    </div>

                    <div class="preco-container preco-anual">
                        <div class="header-preco-anual">
                            <span class="valor">R$ 3,40</span>
                            <span class="badge-desconto-card">-15%</span>
                        </div>
                        <span class="periodo">Por apartamento ou unidade / Anual</span>
                    </div>

                    <hr class="divisor-card">

                    <ul class="lista-recursos">
                        <li><span class="check-icone">✓</span> Coleta programada e sob demanda</li>
                        <li><span class="check-icone">✓</span> Gestão completa de resíduos</li>
                        <li><span class="check-icone">✓</span> Painéis do síndico ou do gestor</li>
                        <li><span class="check-icone">✓</span> Relatórios e histórico de coleta</li>
                    </ul>

                </article>

                <!-- CARD 3: INDUSTRIAL -->
                <article class="card-plano">
                    <div class="cabecalho-card">
                        <img src="${pageContext.request.contextPath}/assets/icone-industrial.svg?v=2" alt="Ícone Industrial" class="icone-plano">
                        <div>
                            <span class="subtitulo-card">PLANO</span>
                            <h3 class="nome-plano">Industrial</h3>
                            <p class="descricao-plano">Ideal para grandes empresas e grandes áreas.</p>
                        </div>
                    </div>

                    <div class="preco-container preco-mensal">
                        <span class="valor">R$ 0,50</span>
                        <span class="periodo">Por m² / Mensal</span>
                    </div>

                    <div class="preco-container preco-anual">
                        <div class="header-preco-anual">
                            <span class="valor">R$ 0,42</span>
                            <span class="badge-desconto-card">-15%</span>
                        </div>
                        <span class="periodo">Por m² / Anual</span>
                    </div>

                    <hr class="divisor-card">

                    <ul class="lista-recursos">
                        <li><span class="check-icone">✓</span> Solução completa para indústrias</li>
                        <li><span class="check-icone">✓</span> Coleta personalizada</li>
                        <li><span class="check-icone">✓</span> Gestão de grandes volumes</li>
                        <li><span class="check-icone">✓</span> Painéis e relatórios avançados</li>
                    </ul>

                </article>
            </div>

            <div class="acao-planos">
                <a href="${pageContext.request.contextPath}/login-cadastro?aba=cadastro" class="botao-plano">
                    <span>Começar agora</span>
                    <svg class="seta" width="16" height="16" viewBox="-1 -1 16 16" fill="none" aria-hidden="true"><path d="M0 7H14M7 0L14 7L7 14" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>
                </a>
            </div>
        </section>

        <%-- Seção para cooperativas --%>
        <section class="cooperativa">
            <div class="container-cooperativa">
                <div class="conteudo-cooperativa">
                    <div class="badge-cooperativa">
                                <span class="icone-folha-badge">
                                    <img src="${pageContext.request.contextPath}/assets/icone-folha.svg?v=2" alt="icone folha">
                                </span>
                        <span>Torne-se parceiro</span>
                    </div>

                    <h2 class="titulo-cooperativa">
                        <span class="titulo-verde">Mais eficiência para</span> <span class="titulo-rosa">sua cooperativa</span>
                    </h2>

                    <p class="descricao-cooperativa">
                        Simplifique a gestão das coletas, aumente suas parcerias e tenha mais visibilidade para transformar reciclagem em novas oportunidades.
                    </p>

                    <ul class="lista-recursos-cooperativa">
                        <li class="item-recurso-cooperativa">
                            <div class="icone-recurso-cooperativa">
                                <img src="${pageContext.request.contextPath}/assets/icone-celular.svg?v=2" alt="Receba solicitações">
                            </div>
                            <span>Receba novas solicitações de coleta</span>
                        </li>

                        <li class="item-recurso-cooperativa">
                            <div class="icone-recurso-cooperativa">
                                <img src="${pageContext.request.contextPath}/assets/icone-caminhao.svg?v=2" alt="Organize coletas">
                            </div>
                            <span>Organize e otimize suas coletas</span>
                        </li>

                        <li class="item-recurso-cooperativa">
                            <div class="icone-recurso-cooperativa">
                                <img src="${pageContext.request.contextPath}/assets/grafico-verde-icone.svg?v=2" alt="Acompanhe materiais">
                            </div>
                            <span>Acompanhe materiais e resultados</span>
                        </li>

                        <li class="item-recurso-cooperativa">
                            <div class="icone-recurso-cooperativa">
                                <img src="${pageContext.request.contextPath}/assets/icone-maos.svg?v=2" alt="Conecte-se">
                            </div>
                            <span>Conecte-se com novos parceiros</span>
                        </li>
                    </ul>
                </div>

                <div class="imagem-cooperativa">
                    <img src="${pageContext.request.contextPath}/assets/caminhaoCooperativa.svg" alt="Caminhão da cooperativa de reciclagem EcoCiente">
                </div>
            </div>
        </section>
        <%-- Destaque do aplicativo --%>
        <section class="baixe-aqui">
            <div class="container-baixe">
                <div class="mockup-baixe">
                    <img src="${pageContext.request.contextPath}/assets/celularesHero.png" alt="Aplicativo EcoCiente em celulares">
                </div>
                <div class="conteudo-baixe">
                    <span class="etiqueta-baixe">Tudo na palma da sua mão</span>
                    <h2 class="titulo-baixe">Tenha o controle<br>ao seu alcance</h2>
                    <p class="descricao-baixe">
                        Nosso app oferece uma experiência completa para condomínios e cooperativas gerenciarem coletas, registros e parcerias de forma prática
                    </p>
                    <ul class="lista-baixe">
                        <li>Agendamentos online</li>
                        <li>Histórico de coleta</li>
                        <li>Notificações em tempo real</li>
                        <li>Relatórios e indicadores de sustentabilidade</li>
                    </ul>
                </div>
            </div>
        </section>

        <%-- Nossa missão --%>
        <section class="nossa-missao" id="nossa-missao">
            <div class="container-missao">
                <div class="conteudo-missao">
                    <h2 class="titulo-rosa titulo-missao">Nossa Missão</h2>
                    <p class="descricao-missao">
                        Nossa missão é fazer com que a sustentabilidade não seja algo complexo.
                    </p>

                    <div class="grid-estatisticas-missao">
                        <div class="item-estatistica">
                            <div class="icone-estatistica">
                                <img src="${pageContext.request.contextPath}/assets/icon-pessoa.svg?v=2" alt="Barreiras de informação">
                            </div>
                            <span class="numero-estatistica">39% das pessoas</span>
                            <p class="texto-estatistica">Enfrentam barreiras de informação ou motivação</p>
                        </div>

                        <div class="item-estatistica">
                            <div class="icone-estatistica">
                                <img src="${pageContext.request.contextPath}/assets/icon-recicle.svg?v=2" alt="Símbolo de reciclagem">
                            </div>
                            <span class="numero-estatistica">8,3% resíduos</span>
                            <p class="texto-estatistica">É tudo o que hoje é efetivamente reciclado.</p>
                        </div>

                        <div class="item-estatistica">
                            <div class="icone-estatistica">
                                <img src="${pageContext.request.contextPath}/assets/icon-infinito.svg?v=2" alt="ODS 12.8">
                            </div>
                            <span class="numero-estatistica">ODS 12.8</span>
                            <p class="texto-estatistica">É a meta da ONU que guia o projeto.</p>
                        </div>
                    </div>
                </div>

                <div class="imagem-missao">
                    <img src="${pageContext.request.contextPath}/assets/ods-12.svg" alt="ODS 12 - Consumo e Produção Responsáveis">
                    <p class="legenda-ods">ODS 12: Consumo e Produção Responsáveis, um dos 17 Objetivos de Desenvolvimento Sustentável da ONU. A Meta 12.8 busca garantir que as pessoas tenham informação e conscientização para o desenvolvimento sustentável.</p>
                </div>
            </div>
        </section>
        <%-- Quem somos --%>
        <section class="quem-somos" id="quem-somos">
            <div class="container-quem-somos">
                <div class="imagem-quem-somos">
                    <img src="${pageContext.request.contextPath}/assets/equipe-prototipo.svg" alt="Estudantes idealizadores do EcoCiente">
                </div>

                <div class="conteudo-quem-somos">
                    <h2 class="titulo-verde titulo-quem-somos">
                        Quem está por trás <br>do <span class="titulo-rosa">EcoCiente?</span>
                    </h2>
                    <p class="descricao-quem-somos">
                        Somos os estudantes que idealizaram e desenvolveram o EcoCiente. Unimos conhecimento, criatividade e colaboração para transformar uma ideia em uma solução real. O nosso diferencial é simples: nós vivemos o projeto, construímos cada etapa e acreditamos no impacto que ele pode gerar.
                    </p>

                    <div class="grid-diferenciais-quem-somos">
                        <div class="item-diferencial">
                            <div class="icone-diferencial">
                                <img src="${pageContext.request.contextPath}/assets/icon-estudantes.svg?v=2" alt="Somos estudantes">
                            </div>
                            <span class="titulo-diferencial">Somos estudantes</span>
                            <p class="texto-diferencial">Movidos pelo propósito de fazer a diferença.</p>
                        </div>

                        <div class="item-diferencial">
                            <div class="icone-diferencial">
                                <img src="${pageContext.request.contextPath}/assets/icon-ideia.svg?v=2" alt="Transformamos ideias">
                            </div>
                            <span class="titulo-diferencial">Transformamos ideias</span>
                            <p class="texto-diferencial">Criamos soluções práticas para desafios reais.</p>
                        </div>

                        <div class="item-diferencial">
                            <div class="icone-diferencial">
                                <img src="${pageContext.request.contextPath}/assets/icon-planet.svg?v=2" alt="Geramos impacto">
                            </div>
                            <span class="titulo-diferencial">Geramos impacto</span>
                            <p class="texto-diferencial">Acreditamos em um futuro mais sustentável.</p>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <%-- Perguntas frequentes --%>
        <section class="duvidas" id="duvidas">
            <h2 class="titulo-duvidas">Perguntas frequentes</h2>

            <div class="lista-duvidas">
                <div class="item-duvida">
                    <input type="checkbox" id="faq-5" class="entrada-faq">
                    <label for="faq-5" class="pergunta-faq">
                        <span>Como funciona o painel do síndico e do gestor?</span>
                        <span class="icone-plus"></span>
                    </label>
                    <div class="resposta-faq">
                        <p>É o dashboard onde o síndico monitora os indicadores de reciclagem do prédio, baixa relatórios de impacto ambiental para prestar contas e acompanha a rotina de coletas com as cooperativas, tudo de forma automática e digital.</p>
                    </div>
                </div>

                <div class="item-duvida">
                    <input type="checkbox" id="faq-1" class="entrada-faq">
                    <label for="faq-1" class="pergunta-faq">
                        <span>Preciso ter coleta seletiva no meu bairro para usar o app?</span>
                        <span class="icone-plus"></span>
                    </label>
                    <div class="resposta-faq">
                        <p>Não. O EcoCiente permite consultar cooperativas, pontos de entrega voluntária e demais locais de destinação de recicláveis disponíveis na sua região, mesmo quando não há coleta seletiva porta a porta.</p>
                    </div>
                </div>

                <div class="item-duvida">
                    <input type="checkbox" id="faq-2" class="entrada-faq">
                    <label for="faq-2" class="pergunta-faq">
                        <span>Como funciona a cobrança para condomínios industriais?</span>
                        <span class="icone-plus"></span>
                    </label>
                    <div class="resposta-faq">
                        <p>Para condomínios industriais, a cobrança é calculada com base na área do empreendimento, em metros quadrados (m²). Esse modelo considera a maior geração e o volume potencial de resíduos associados a áreas industriais de maior extensão.</p>
                    </div>
                </div>

                <div class="item-duvida">
                    <input type="checkbox" id="faq-3" class="entrada-faq">
                    <label for="faq-3" class="pergunta-faq">
                        <span>Cooperativas pagam para utilizar o EcoCiente?</span>
                        <span class="icone-plus"></span>
                    </label>
                    <div class="resposta-faq">
                        <p>Não. O cadastro e a utilização da plataforma pelas cooperativas são gratuitos. O objetivo é ampliar a visibilidade dessas organizações, fortalecer sua atuação e contribuir para o aumento do volume de materiais recicláveis destinados à coleta e à triagem.</p>
                    </div>
                </div>

                <div class="item-duvida">
                    <input type="checkbox" id="faq-4" class="entrada-faq">
                    <label for="faq-4" class="pergunta-faq">
                        <span>O plano gratuito tem quais funcionalidades?</span>
                        <span class="icone-plus"></span>
                    </label>
                    <div class="resposta-faq">
                        <p>O plano gratuito oferece acesso ao mapa de pontos de coleta, ao guia de separação de resíduos e aos quizzes educativos. É uma opção completa para utilização individual e para quem deseja conhecer melhor as práticas de descarte e reciclagem.</p>
                    </div>
                </div>
            </div>
        </section>
    </div>

</div>
<%-- Rodapé: links de navegação, contato e documentos (termos de uso e política de privacidade) --%>
<footer class="rodape">
    <div class="container-rodape">
        <div class="coluna-rodape">
            <div class="logo-rodape">
                <img src="${pageContext.request.contextPath}/assets/logo-branco.png" alt="EcoCiente">
            </div>
            <p class="slogan-rodape">Usando tecnologia para um futuro mais sustentável.</p>
            <div class="redes-sociais">
                <a href="#" aria-label="Instagram"><i class="fa-brands fa-instagram"></i></a>
                <a href="#" aria-label="LinkedIn"><i class="fa-brands fa-linkedin-in"></i></a>
                <a href="#" aria-label="Facebook"><i class="fa-brands fa-facebook-f"></i></a>
            </div>
            <p class="copyright">2026 EcoCiente. Todos os direitos reservados</p>
            <a href="${pageContext.request.contextPath}/login-crud" class="acesso-admin" aria-label="Acesso administrativo">.</a>
        </div>

        <div class="divisor-vertical"></div>

        <div class="coluna-rodape">
            <h3 class="titulo-coluna">Navegação:</h3>
            <nav class="links-navegacao">
                <a href="#funcionalidades">Funcionalidades</a>
                <a href="#beneficios">Benefícios</a>
                <a href="#planos">Planos</a>
                <a href="#nossa-missao">Nossa missão</a>
                <a href="#duvidas">Dúvidas</a>
            </nav>
        </div>

        <div class="divisor-vertical"></div>

        <div class="coluna-rodape coluna-contato">
            <div class="info-contato">
                <h3 class="titulo-coluna">Entre em contato:</h3>
                <p>Telefone: (11) 12345-6789</p>
                <p>E-mail: ecociente.jef@gmail.com</p>
            </div>
            <div class="links-termos">
                <a href="${pageContext.request.contextPath}/documentos/termos-de-uso.pdf" target="_blank" rel="noopener">Termos de uso</a> | <a href="${pageContext.request.contextPath}/documentos/politica-de-privacidade.pdf" target="_blank" rel="noopener">Política de privacidade</a>                    </div>
        </div>
    </div>
</footer>
</body>
</html>