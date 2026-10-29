package com.example.ecociente.controller;

import com.example.ecociente.dao.AssinaturaDAO;
import com.example.ecociente.dao.CondominioDAO;
import com.example.ecociente.dao.CooperativaDAO;
import com.example.ecociente.dao.EnderecoDAO;
import com.example.ecociente.dao.MoradorDAO;
import com.example.ecociente.dao.PlanoDAO;
import com.example.ecociente.dao.SindicoDAO;
import com.example.ecociente.dao.TelefoneDAO;
import com.example.ecociente.dao.UsuarioDAO;
import com.example.ecociente.model.Assinatura;
import com.example.ecociente.model.CadastroPendente;
import com.example.ecociente.model.Condominio;
import com.example.ecociente.model.Cooperativa;
import com.example.ecociente.model.Endereco;
import com.example.ecociente.model.Orcamento;
import com.example.ecociente.model.Plano;
import com.example.ecociente.model.TipoCondominio;
import com.example.ecociente.model.Telefone;
import com.example.ecociente.model.Usuario;
import com.example.ecociente.util.DadoInvalido;
import com.example.ecociente.util.Formulario;
import com.example.ecociente.util.Senha;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@WebServlet(urlPatterns = {"/cadastro", "/cadastro/plano", "/usuarios"})
public class UsuarioServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;
    private CooperativaDAO cooperativaDAO;
    private CondominioDAO condominioDAO;
    private SindicoDAO sindicoDAO;
    private MoradorDAO moradorDAO;
    private PlanoDAO planoDAO;
    private AssinaturaDAO assinaturaDAO;

    //Depois disso a sessão não tenta mais o código do condomínio (o código tem 6 caracteres e dá para chutar).
    private static final int MAXIMO_TENTATIVAS_CODIGO = 5;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
        cooperativaDAO = new CooperativaDAO();
        condominioDAO = new CondominioDAO();
        sindicoDAO = new SindicoDAO();
        moradorDAO = new MoradorDAO();
        planoDAO = new PlanoDAO();
        assinaturaDAO = new AssinaturaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String caminho = request.getServletPath();

        if (caminho.equals("/usuarios")) {
            mostrarUsuarios(request, response);
        } else if (caminho.equals("/cadastro/plano")) {
            iniciarAssinatura(request, response);
        } else if (caminho.equals("/cadastro")) {
            response.sendRedirect(request.getContextPath() + "/login-cadastro?aba=cadastro");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        if (request.getServletPath().equals("/usuarios")) {
            tratarPainel(request, response);
        } else if (request.getServletPath().equals("/cadastro")) {
            String etapa = request.getParameter("etapa");
            if ("perfil".equals(etapa)) {
                processarEtapaPerfil(request, response);
            } else if ("moradia".equals(etapa)) {
                processarEtapaMoradia(request, response);
            } else if ("cooperativa-dados".equals(etapa) || "sindico-dados".equals(etapa) || "morador-dados".equals(etapa)) {
                processarEtapaDados(request, response);
            } else if ("cooperativa-endereco".equals(etapa)) {
                processarEtapaEnderecoCooperativa(request, response);
            } else if ("tipo".equals(etapa)) {
                processarEtapaTipo(request, response);
            } else if ("condominio".equals(etapa)) {
                processarEtapaCondominio(request, response);
            } else if ("endereco".equals(etapa)) {
                processarEtapaEnderecoCondominio(request, response);
            } else if ("pag-dados".equals(etapa)) {
                processarEtapaPagDados(request, response);
            } else if ("pag-resumo".equals(etapa)) {
                processarEtapaPagResumo(request, response);
            } else if ("pag-pagamento".equals(etapa)) {
                processarEtapaPagPagamento(request, response);
            } else if ("token".equals(etapa)) {
                processarEtapaToken(request, response);
            } else if ("morador-endereco".equals(etapa)) {
                processarEtapaMoradorEndereco(request, response);
            } else {
                processarEtapaConta(request, response);
            }
        }
    }

    //Busca os usuarios (com pesquisa opcional) e os tipos (para o select) e envia para a JSP do painel.
    private void mostrarUsuarios(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String busca = request.getParameter("q");
        if (busca == null) busca = request.getParameter("search");

        request.setAttribute("usuarios", Formulario.paginar(request, usuarioDAO.selecionarTodos(busca)));
        request.setAttribute("tiposUsuario", usuarioDAO.selecionarTipos());
        request.getRequestDispatcher("/WEB-INF/views/usuarios.jsp").forward(request, response);
    }

    //POST /usuarios do painel: acao=excluir apaga, id vazio cadastra e id preenchido altera.
    private void tratarPainel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String acao = Formulario.texto(request, "acao");
        String id = Formulario.texto(request, "id");
        String resultado = acao.equals("excluir") ? "deletado" : id.isEmpty() ? "adicionado" : "alterado";
        request.setAttribute(resultado, "false");

        try {
            boolean ok;
            if (acao.equals("excluir")) {
                ok = deletarUsuarioPainel(request, Formulario.id(id, "usuário"));
            } else if (id.isEmpty()) {
                ok = adicionarUsuarioPainel(request);
            } else {
                ok = alterarUsuarioPainel(request, Formulario.id(id, "usuário"));
            }
            request.setAttribute(resultado, ok ? "true" : "false");
        } catch (DadoInvalido e) {
            request.setAttribute("erro", e.getMessage());
        } catch (RuntimeException e) {
            request.setAttribute("erro", "Operação não concluída. Confira os dados informados e se o registro possui vínculos.");
        }
        mostrarUsuarios(request, response);
    }

    private boolean adicionarUsuarioPainel(HttpServletRequest request) {
        String nome = Formulario.exigir(Formulario.texto(request, "nome"), "o nome", 100);
        String email = Formulario.email(Formulario.texto(request, "email"));
        String senha = request.getParameter("senha");
        if (senha == null || senha.length() < 6) {
            throw new DadoInvalido("A senha precisa ter pelo menos 6 caracteres.");
        }
        int idTipoUsuario = Formulario.id(Formulario.texto(request, "idTipoUsuario"), "tipo de usuário");
        String telefone = Formulario.telefone(request.getParameter("telefone"));
        Endereco endereco = Formulario.endereco(request, false);
        String cpf = Formulario.cpf(request.getParameter("cpf"), false);

        if (usuarioDAO.existeEmail(email, 0)) {
            throw new DadoInvalido("Este e-mail já está cadastrado.");
        }
        if (cpf != null && usuarioDAO.existeCpf(cpf, 0)) {
            throw new DadoInvalido("Este CPF já está cadastrado.");
        }

        //Id 0 porque o banco gera. Endereço e telefone são criados junto, na mesma transação.
        Usuario usuario = new Usuario(0, nome, email, Senha.gerarHash(senha), LocalDate.now(), true, 0, idTipoUsuario);
        usuario.setCpf(cpf);
        return usuarioDAO.inserir(usuario, telefone, endereco);
    }

    private boolean alterarUsuarioPainel(HttpServletRequest request, int id) {
        String nome = Formulario.exigir(Formulario.texto(request, "nome"), "o nome", 100);
        String email = Formulario.email(Formulario.texto(request, "email"));
        String senha = request.getParameter("senha");
        if (senha != null && !senha.isEmpty() && senha.length() < 6) {
            throw new DadoInvalido("A senha precisa ter pelo menos 6 caracteres.");
        }
        int idTipoUsuario = Formulario.id(Formulario.texto(request, "idTipoUsuario"), "tipo de usuário");
        String telefone = Formulario.telefone(request.getParameter("telefone"));
        Endereco endereco = Formulario.endereco(request, false);
        String cpf = Formulario.cpf(request.getParameter("cpf"), false);

        if (usuarioDAO.existeEmail(email, id)) {
            throw new DadoInvalido("Este e-mail já está cadastrado.");
        }
        if (cpf != null && usuarioDAO.existeCpf(cpf, id)) {
            throw new DadoInvalido("Este CPF já está cadastrado.");
        }

        //Busca no banco e troca só o que veio do formulário (senha vazia mantém a atual).
        Usuario usuario = usuarioDAO.selecionarUsuarioPorId(id);
        if (usuario == null) return false;
        usuario.setNome(nome);
        usuario.setEmail(email);
        usuario.setTipoUsuario(idTipoUsuario);
        usuario.setCpf(cpf);
        if (senha != null && !senha.isEmpty()) {
            usuario.setSenhaHash(Senha.gerarHash(senha));
        }

        return usuarioDAO.atualizar(usuario, telefone, endereco);
    }

    private boolean deletarUsuarioPainel(HttpServletRequest request, int id) {
        Object logado = request.getSession().getAttribute("idUsuarioLogado");
        if (logado != null && (Integer) logado == id) {
            throw new DadoInvalido("Você não pode excluir o próprio usuário.");
        }

        Usuario usuario = usuarioDAO.selecionarUsuarioPorId(id);
        if (usuario == null) return false;
        return usuarioDAO.deletar(usuario);
    }

    private void mostrarCadastro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().setAttribute("abaAtiva", "cadastro");
        request.getRequestDispatcher("/WEB-INF/views/cadastro_login.jsp").forward(request, response);
    }

    //================ ETAPA 1: CONTA (email + senha) ================

    private void processarEtapaConta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String confirmaSenha = request.getParameter("confirma_senha");

        String erro = validarConta(email, senha, confirmaSenha);
        if (erro == null && usuarioDAO.existeEmail(email.trim().toLowerCase())) {
            erro = "Este e-mail já está cadastrado.";
            request.setAttribute("emailJaCadastrado", true); //conta.jsp oferece "Fazer assinatura" pelo login
        }
        if (erro != null) {
            request.setAttribute("erroCadastro", erro);
            mostrarCadastro(request, response);
            return;
        }

        CadastroPendente cad = new CadastroPendente();
        cad.setEmail(email.trim().toLowerCase());
        cad.setSenhaHash(Senha.gerarHash(senha));

        HttpSession sessao = request.getSession();
        sessao.setAttribute("cadastroPendente", cad);
        sessao.setAttribute("etapaCadastro", "perfil");

        mostrarCadastro(request, response);
    }

    private String validarConta(String email, String senha, String confirmaSenha) {
        if (email == null || !email.trim().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$")) return "Informe um e-mail válido.";
        if (senha == null || senha.length() < 8) return "A senha precisa ter pelo menos 8 caracteres.";
        if (!senha.equals(confirmaSenha)) return "As senhas não conferem.";
        return null;
    }

    //================ ETAPA 2: PERFIL ================

    private void processarEtapaPerfil(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sessao = request.getSession();

        if ("voltar".equals(request.getParameter("acao"))) {
            sessao.setAttribute("etapaCadastro", "conta");
            mostrarCadastro(request, response);
            return;
        }

        CadastroPendente cad = (CadastroPendente) sessao.getAttribute("cadastroPendente");
        if (cad == null) {
            sessao.setAttribute("etapaCadastro", "conta");
            mostrarCadastro(request, response);
            return;
        }

        String perfil = request.getParameter("perfil");
        if (perfil == null || perfil.isBlank()) {
            request.setAttribute("erroCadastro", "Selecione um perfil.");
            mostrarCadastro(request, response);
            return;
        }

        if (!"cooperativa".equals(perfil) && !"sindico".equals(perfil) && !"morador".equals(perfil)) {
            request.setAttribute("erroCadastro", "Selecione um perfil válido.");
            mostrarCadastro(request, response);
            return;
        }

        cad.setPerfil(perfil);
        sessao.setAttribute("cadastroPendente", cad);

        // Morador escolhe a moradia primeiro; cooperativa e síndico vão direto para os dados pessoais.
        sessao.setAttribute("etapaCadastro", "morador".equals(perfil) ? "moradia" : perfil + "-dados");
        mostrarCadastro(request, response);
    }

    //================ ETAPA 3: MORADIA (condominio ou casa) ================

    private void processarEtapaMoradia(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sessao = request.getSession();

        if ("voltar".equals(request.getParameter("acao"))) {
            sessao.setAttribute("etapaCadastro", "perfil");
            mostrarCadastro(request, response);
            return;
        }

        CadastroPendente cad = (CadastroPendente) sessao.getAttribute("cadastroPendente");
        if (cad == null) {
            sessao.setAttribute("etapaCadastro", "conta");
            mostrarCadastro(request, response);
            return;
        }

        String moradia = request.getParameter("moradia");
        if (moradia == null || moradia.isBlank()) {
            request.setAttribute("erroCadastro", "Selecione uma opção.");
            mostrarCadastro(request, response);
            return;
        }

        cad.setMoradia(moradia);
        //Quem muda para "casa" não usa código, bloco nem apartamento: limpa o que digitou antes.
        if (!"condominio".equals(moradia)) {
            cad.setToken(null);
            cad.setBloco(null);
            cad.setApto(null);
        }
        sessao.setAttribute("cadastroPendente", cad);

        sessao.setAttribute("etapaCadastro", "morador-dados");
        mostrarCadastro(request, response);
    }

    //================ ETAPA 4: DADOS PESSOAIS (nome, telefone, cpf, nascimento) - todos os perfis ================

    private void processarEtapaDados(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sessao = request.getSession();

        if ("voltar".equals(request.getParameter("acao"))) {
            CadastroPendente atual = (CadastroPendente) sessao.getAttribute("cadastroPendente");
            boolean ehMorador = atual != null && "morador".equals(atual.getPerfil());
            sessao.setAttribute("etapaCadastro", ehMorador ? "moradia" : "perfil");
            mostrarCadastro(request, response);
            return;
        }

        CadastroPendente cad = (CadastroPendente) sessao.getAttribute("cadastroPendente");
        if (cad == null) {
            sessao.setAttribute("etapaCadastro", "conta");
            mostrarCadastro(request, response);
            return;
        }

        String nome = request.getParameter("nome");
        String telefone = request.getParameter("telefone");
        String cpf = request.getParameter("cpf");
        String nascimento = request.getParameter("nascimento");
        String termos = request.getParameter("termos");
        String avisos = request.getParameter("avisos");

        cad.setNome(nome);
        cad.setTelefone(telefone);
        cad.setCpf(cpf);
        cad.setNascimento(nascimento);
        cad.setAvisos(avisos);
        cad.setTermos(termos);
        sessao.setAttribute("cadastroPendente", cad);

        if (nome == null || !nome.trim().matches("\\S+(\\s+\\S+)+")) {
            request.setAttribute("erroCadastro", "Digite o nome completo, com nome e sobrenome.");
            mostrarCadastro(request, response);
            return;
        }
        if (telefone == null || telefone.isBlank()) {
            request.setAttribute("erroCadastro", "Informe um telefone válido.");
            mostrarCadastro(request, response);
            return;
        }
        if (cpf == null || cpf.replaceAll("\\D", "").length() != 11) {
            request.setAttribute("erroCadastro", "CPF inválido, precisa ter 11 dígitos.");
            mostrarCadastro(request, response);
            return;
        }
        if (usuarioDAO.existeCpf(cpf.replaceAll("\\D", ""), 0)) {
            request.setAttribute("erroCadastro", "Este CPF já está cadastrado.");
            mostrarCadastro(request, response);
            return;
        }
        if (nascimento == null || nascimento.isBlank()) {
            request.setAttribute("erroCadastro", "Informe a data de nascimento.");
            mostrarCadastro(request, response);
            return;
        }
        if (!"aceito".equals(termos)) {
            request.setAttribute("erroCadastro", "É preciso aceitar os Termos de uso e a Política de privacidade.");
            mostrarCadastro(request, response);
            return;
        }

        // Próxima tela depende do perfil.
        String proxima;
        if ("cooperativa".equals(cad.getPerfil())) {
            proxima = "cooperativa-endereco";
        } else if ("sindico".equals(cad.getPerfil())) {
            proxima = "tipo";
        } else {
            proxima = "condominio".equals(cad.getMoradia()) ? "token" : "morador-endereco";
        }
        sessao.setAttribute("etapaCadastro", proxima);
        mostrarCadastro(request, response);
    }

    //================ COOPERATIVA: ENDERECO (finaliza o cadastro) ================

    private void processarEtapaEnderecoCooperativa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sessao = request.getSession();

        if ("voltar".equals(request.getParameter("acao"))) {
            sessao.setAttribute("etapaCadastro", "cooperativa-dados");
            mostrarCadastro(request, response);
            return;
        }

        CadastroPendente cad = (CadastroPendente) sessao.getAttribute("cadastroPendente");
        if (cad == null) {
            sessao.setAttribute("etapaCadastro", "conta");
            mostrarCadastro(request, response);
            return;
        }

        String nomeCooperativa = request.getParameter("nomeCooperativa");
        String cnpj = request.getParameter("cnpj");
        String cep = request.getParameter("cep");
        String uf = request.getParameter("uf");
        String cidade = request.getParameter("cidade");
        String bairro = request.getParameter("bairro");
        String rua = request.getParameter("rua");
        String numeroTexto = request.getParameter("numero");
        String complemento = request.getParameter("complemento");

        cad.setNomeCooperativa(nomeCooperativa);
        cad.setCnpj(cnpj);
        cad.setCep(cep);
        cad.setUf(uf);
        cad.setCidade(cidade);
        cad.setBairro(bairro);
        cad.setRua(rua);
        cad.setNumero(numeroTexto);
        cad.setComplemento(complemento);
        sessao.setAttribute("cadastroPendente", cad);

        if (nomeCooperativa == null || nomeCooperativa.isBlank() || nomeCooperativa.trim().length() > 100) {
            request.setAttribute("erroCadastro", "Informe o nome da cooperativa, com até 100 caracteres.");
            mostrarCadastro(request, response);
            return;
        }
        if (cnpj == null || cnpj.replaceAll("\\D", "").length() != 14) {
            request.setAttribute("erroCadastro", "CNPJ inválido, precisa ter 14 dígitos.");
            mostrarCadastro(request, response);
            return;
        }
        if (cep == null || cep.replaceAll("\\D", "").length() != 8) {
            request.setAttribute("erroCadastro", "CEP inválido, precisa ter 8 dígitos.");
            mostrarCadastro(request, response);
            return;
        }
        if (uf == null || uf.isBlank() || cidade == null || cidade.isBlank() || bairro == null || bairro.isBlank()
                || rua == null || rua.isBlank() || numeroTexto == null || numeroTexto.isBlank()) {
            request.setAttribute("erroCadastro", "Preencha todos os campos obrigatórios do endereço.");
            mostrarCadastro(request, response);
            return;
        }

        int numero;
        try {
            numero = Integer.parseInt(numeroTexto.replaceAll("\\D", ""));
        } catch (NumberFormatException nfe) {
            request.setAttribute("erroCadastro", "Número do endereço inválido.");
            mostrarCadastro(request, response);
            return;
        }

        String cnpjLimpo = cnpj.replaceAll("\\D", "");
        int idTipoUsuario = usuarioDAO.selecionarIdTipoPorNome("cooperativa");
        if (idTipoUsuario == 0) {
            request.setAttribute("erroCadastro", "Tipo de usuário cooperativa não encontrado no banco.");
            mostrarCadastro(request, response);
            return;
        }
        if (cooperativaDAO.existeCnpj(cnpjLimpo, 0)) {
            request.setAttribute("erroCadastro", "Este CNPJ já está cadastrado.");
            mostrarCadastro(request, response);
            return;
        }

        Endereco endereco = new Endereco(0, cep.replaceAll("\\D", ""), cidade.trim(), uf.trim(), bairro.trim(), rua.trim(), numero,
                complemento == null || complemento.isBlank() ? null : complemento.trim());

        try {
            //Ids 0 porque o banco gera. Endereço, usuário dono, cooperativa e telefone são gravados na mesma transação.
            Cooperativa cooperativa = new Cooperativa(0, cad.getNome().trim(), cad.getEmail(), cad.getSenhaHash(),
                    LocalDate.now(), true, 0, idTipoUsuario, 0, cnpjLimpo);
            cooperativa.setNomeCooperativa(nomeCooperativa.trim());
            cooperativa.setCpf(Formulario.cpf(cad.getCpf(), true));
            cooperativaDAO.inserir(cooperativa, cad.getTelefone() == null ? null : cad.getTelefone().replaceAll("\\D", ""), endereco);
        } catch (RuntimeException e) {
            request.setAttribute("erroCadastro", "Não foi possível concluir o cadastro. Tente novamente.");
            mostrarCadastro(request, response);
            return;
        }

        //Nenhum plano do banco serve para cooperativa (plano.unidade só aceita apartamento, empresa ou m2): termina sem assinatura.
        concluirCadastro(request, response, sessao, "cooperativa");
    }

    //================ SINDICO: TIPO DO CONDOMINIO ================

    private void processarEtapaTipo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sessao = request.getSession();

        if ("voltar".equals(request.getParameter("acao"))) {
            sessao.setAttribute("etapaCadastro", "sindico-dados");
            mostrarCadastro(request, response);
            return;
        }

        CadastroPendente cad = (CadastroPendente) sessao.getAttribute("cadastroPendente");
        if (cad == null) {
            sessao.setAttribute("etapaCadastro", "conta");
            mostrarCadastro(request, response);
            return;
        }

        String tipoCondominio = request.getParameter("tipoCondominio");
        if (!"residencial".equals(tipoCondominio) && !"comercial".equals(tipoCondominio) && !"industrial".equals(tipoCondominio)) {
            request.setAttribute("erroCadastro", "Selecione o tipo do condomínio.");
            mostrarCadastro(request, response);
            return;
        }

        cad.setTipoCondominio(tipoCondominio);
        sessao.setAttribute("cadastroPendente", cad);
        sessao.setAttribute("etapaCadastro", "condominio");
        mostrarCadastro(request, response);
    }

    //================ SINDICO: DADOS DO CONDOMINIO ================

    private void processarEtapaCondominio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sessao = request.getSession();

        if ("voltar".equals(request.getParameter("acao"))) {
            sessao.setAttribute("etapaCadastro", "tipo");
            mostrarCadastro(request, response);
            return;
        }

        CadastroPendente cad = (CadastroPendente) sessao.getAttribute("cadastroPendente");
        if (cad == null) {
            sessao.setAttribute("etapaCadastro", "conta");
            mostrarCadastro(request, response);
            return;
        }

        boolean industrial = "industrial".equals(cad.getTipoCondominio());
        String nomeCondominio = request.getParameter("nomeCondominio");
        String razaoSocial = request.getParameter("razaoSocial");
        String cnpj = request.getParameter("cnpj");
        String telefone = request.getParameter("telefone");
        String numeroUnidades = request.getParameter("numeroUnidades");
        String areaM2 = request.getParameter("areaM2");

        cad.setNomeCondominio(nomeCondominio);
        cad.setRazaoSocial(razaoSocial);
        cad.setCnpj(cnpj);
        if (telefone != null && !telefone.isBlank()) cad.setTelefone(telefone); //industrial não pede telefone: fica o da etapa de dados
        cad.setNumeroUnidades(numeroUnidades);
        cad.setAreaM2(areaM2);
        sessao.setAttribute("cadastroPendente", cad);

        //Residencial usa o nome do condomínio; comercial e industrial usam a razão social.
        String nome = "residencial".equals(cad.getTipoCondominio()) ? nomeCondominio : razaoSocial;
        if (nome == null || nome.isBlank() || nome.trim().length() > 100) {
            request.setAttribute("erroCadastro", "Informe o nome do condomínio, com até 100 caracteres.");
            mostrarCadastro(request, response);
            return;
        }
        if (cnpj == null || cnpj.replaceAll("\\D", "").length() != 14) {
            request.setAttribute("erroCadastro", "CNPJ inválido, precisa ter 14 dígitos.");
            mostrarCadastro(request, response);
            return;
        }
        if (condominioDAO.existeCnpj(cnpj.replaceAll("\\D", ""), 0)) {
            request.setAttribute("erroCadastro", "Este CNPJ já está cadastrado.");
            mostrarCadastro(request, response);
            return;
        }

        //Industrial informa a área em m²; os outros tipos informam o número de unidades.
        try {
            Integer unidades = Formulario.inteiroPositivoOpcional(numeroUnidades, "o número de unidades");
            BigDecimal area = Formulario.decimalPositivoOpcional(areaM2, "a área");
            if (industrial ? area == null : unidades == null) {
                throw new DadoInvalido(industrial ? "Informe a área do empreendimento em m²." : "Informe o número de unidades.");
            }
        } catch (DadoInvalido e) {
            request.setAttribute("erroCadastro", e.getMessage());
            mostrarCadastro(request, response);
            return;
        }

        sessao.setAttribute("etapaCadastro", "endereco");
        mostrarCadastro(request, response);
    }

    //================ SINDICO: ENDERECO DO CONDOMINIO (finaliza o cadastro e gera o token) ================

    private void processarEtapaEnderecoCondominio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sessao = request.getSession();

        if ("voltar".equals(request.getParameter("acao"))) {
            sessao.setAttribute("etapaCadastro", "condominio");
            mostrarCadastro(request, response);
            return;
        }

        CadastroPendente cad = (CadastroPendente) sessao.getAttribute("cadastroPendente");
        if (cad == null) {
            sessao.setAttribute("etapaCadastro", "conta");
            mostrarCadastro(request, response);
            return;
        }

        String cep = request.getParameter("cep");
        String uf = request.getParameter("uf");
        String cidade = request.getParameter("cidade");
        String bairro = request.getParameter("bairro");
        String rua = request.getParameter("rua");
        String numeroTexto = request.getParameter("numero");
        String complemento = request.getParameter("complemento");
        String referencia = request.getParameter("referencia");

        cad.setCep(cep);
        cad.setUf(uf);
        cad.setCidade(cidade);
        cad.setBairro(bairro);
        cad.setRua(rua);
        cad.setNumero(numeroTexto);
        cad.setComplemento(complemento);
        cad.setReferencia(referencia);
        sessao.setAttribute("cadastroPendente", cad);

        if (cep == null || cep.replaceAll("\\D", "").length() != 8) {
            request.setAttribute("erroCadastro", "CEP inválido, precisa ter 8 dígitos.");
            mostrarCadastro(request, response);
            return;
        }
        if (uf == null || uf.isBlank() || cidade == null || cidade.isBlank() || bairro == null || bairro.isBlank()
                || rua == null || rua.isBlank() || numeroTexto == null || numeroTexto.isBlank()) {
            request.setAttribute("erroCadastro", "Preencha todos os campos obrigatórios do endereço.");
            mostrarCadastro(request, response);
            return;
        }

        int numero;
        try {
            numero = Integer.parseInt(numeroTexto.replaceAll("\\D", ""));
        } catch (NumberFormatException nfe) {
            request.setAttribute("erroCadastro", "Número do endereço inválido.");
            mostrarCadastro(request, response);
            return;
        }

        int idTipoUsuario = usuarioDAO.selecionarIdTipoPorNome("Sindico");
        int idTipoCondominio = 0;
        for (TipoCondominio tipo : condominioDAO.selecionarTipos()) {
            if (tipo.getNome().equalsIgnoreCase(cad.getTipoCondominio())) idTipoCondominio = tipo.getId();
        }
        if (idTipoUsuario == 0 || idTipoCondominio == 0) {
            request.setAttribute("erroCadastro", "Tipo de usuário ou de condomínio não encontrado no banco.");
            mostrarCadastro(request, response);
            return;
        }

        Endereco endereco = new Endereco(0, cep.replaceAll("\\D", ""), cidade.trim(), uf.trim(), bairro.trim(), rua.trim(), numero,
                complemento == null || complemento.isBlank() ? null : complemento.trim());

        int idCondominio;
        int quantidade;
        try {
            boolean industrial = "industrial".equals(cad.getTipoCondominio());
            String nome = "residencial".equals(cad.getTipoCondominio()) ? cad.getNomeCondominio() : cad.getRazaoSocial();

            //O token é gerado na assinatura, junto com a gravação do condomínio.
            String token = condominioDAO.gerarTokenUnico();

            //Ids 0 porque o banco gera. Usuário, telefone, endereço, condomínio e síndico são gravados na mesma transação.
            Usuario usuario = new Usuario(0, cad.getNome().trim(), cad.getEmail(), cad.getSenhaHash(), LocalDate.now(), true, 0, idTipoUsuario);
            usuario.setCpf(Formulario.cpf(cad.getCpf(), true));

            Condominio condominio = new Condominio(0, nome.trim(), cad.getCnpj().replaceAll("\\D", ""), true, token, 0, idTipoCondominio);
            if (industrial) {
                condominio.setAreaM2(Formulario.decimalPositivoOpcional(cad.getAreaM2(), "a área"));
            } else {
                condominio.setNumeroUnidades(Formulario.inteiroPositivoOpcional(cad.getNumeroUnidades(), "o número de unidades"));
            }

            //O código do condomínio só aparece depois da tela de pagamento.
            idCondominio = sindicoDAO.cadastrarComCondominio(usuario, cad.getTelefone() == null ? null : cad.getTelefone().replaceAll("\\D", ""), condominio, endereco);

            //A quantidade cobrada começa pelo que foi informado no condomínio (área arredondada para cima).
            if (industrial) {
                quantidade = condominio.getAreaM2() == null ? 1 : condominio.getAreaM2().setScale(0, RoundingMode.CEILING).intValue();
            } else {
                quantidade = condominio.getNumeroUnidades() == null ? 1 : condominio.getNumeroUnidades();
            }
        } catch (RuntimeException e) {
            request.setAttribute("erroCadastro", "Não foi possível concluir o cadastro. Tente novamente.");
            mostrarCadastro(request, response);
            return;
        }

        seguirParaAssinatura(request, response, sessao, cad, idCondominio, quantidade);
    }

    //Limpa o cadastro da sessao e mostra a tela de cadastro concluido.
    private void concluirCadastro(HttpServletRequest request, HttpServletResponse response, HttpSession sessao, String perfilCriado) throws ServletException, IOException {
        sessao.removeAttribute("cadastroPendente");
        sessao.removeAttribute("etapaCadastro");
        sessao.removeAttribute("abaAtiva");

        request.setAttribute("perfilCriado", perfilCriado);
        request.getRequestDispatcher("/WEB-INF/views/cadastro/cadastro_sucesso.jsp").forward(request, response);
    }

    //================ MORADOR EM CONDOMÍNIO: CÓDIGO, BLOCO E APARTAMENTO ================
    //Com o código do condomínio, a conta já nasce ligada a ele. Sem o código (o condomínio pode não assinar o app),
    //guarda o bloco e o apartamento e segue para o endereço; o vínculo com o condomínio fica para depois.

    private void processarEtapaToken(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sessao = request.getSession();

        if ("voltar".equals(request.getParameter("acao"))) {
            sessao.setAttribute("etapaCadastro", "morador-dados");
            mostrarCadastro(request, response);
            return;
        }

        CadastroPendente cad = (CadastroPendente) sessao.getAttribute("cadastroPendente");
        if (cad == null) {
            sessao.setAttribute("etapaCadastro", "conta");
            mostrarCadastro(request, response);
            return;
        }

        String token = Formulario.texto(request, "token").toUpperCase();
        String bloco = Formulario.texto(request, "bloco");
        String apto = Formulario.texto(request, "apto");

        cad.setToken(token);
        cad.setBloco(bloco);
        cad.setApto(apto);
        sessao.setAttribute("cadastroPendente", cad);

        if (bloco.length() > 30) {
            request.setAttribute("erroCadastro", "O bloco ou a torre pode ter no máximo 30 caracteres.");
            mostrarCadastro(request, response);
            return;
        }
        if (apto.length() > 10) {
            request.setAttribute("erroCadastro", "O apartamento pode ter no máximo 10 caracteres.");
            mostrarCadastro(request, response);
            return;
        }

        //Sem código: o condomínio dela pode não assinar o app. Segue para o endereço.
        if (token.isEmpty()) {
            sessao.setAttribute("etapaCadastro", "morador-endereco");
            mostrarCadastro(request, response);
            return;
        }

        if (!token.matches("[A-Z0-9]{6}")) {
            request.setAttribute("erroCadastro", "O código do condomínio tem 6 letras ou números.");
            mostrarCadastro(request, response);
            return;
        }

        Integer tentativas = (Integer) sessao.getAttribute("tentativasCodigo");
        if (tentativas != null && tentativas >= MAXIMO_TENTATIVAS_CODIGO) {
            request.setAttribute("erroCadastro", "Muitas tentativas com o código. Fale com o síndico ou continue sem o código.");
            mostrarCadastro(request, response);
            return;
        }

        Condominio condominio;
        int idTipoMorador;
        try {
            condominio = condominioDAO.selecionarPorToken(token);
            idTipoMorador = usuarioDAO.selecionarIdTipoPorNome("Morador");
        } catch (RuntimeException e) {
            request.setAttribute("erroCadastro", "Não foi possível conferir o código agora. Tente novamente.");
            mostrarCadastro(request, response);
            return;
        }

        if (condominio == null || !condominio.isStatus()) {
            sessao.setAttribute("tentativasCodigo", tentativas == null ? 1 : tentativas + 1);
            request.setAttribute("erroCadastro", "Código do condomínio não encontrado. Confira com o síndico.");
            mostrarCadastro(request, response);
            return;
        }
        if (idTipoMorador == 0) {
            request.setAttribute("erroCadastro", "Não foi possível concluir o cadastro. Tente novamente.");
            mostrarCadastro(request, response);
            return;
        }

        try {
            //Id 0 porque o banco gera. O morador não tem endereço próprio: vale o do condomínio.
            Usuario usuario = new Usuario(0, cad.getNome().trim(), cad.getEmail(), cad.getSenhaHash(), LocalDate.now(), true, 0, idTipoMorador);
            usuario.setCpf(Formulario.cpf(cad.getCpf(), false));

            moradorDAO.cadastrarComUsuario(usuario, telefoneSomenteNumeros(cad), null,
                    vazioParaNulo(apto), vazioParaNulo(bloco), condominio.getIdCondominio());
        } catch (DadoInvalido e) {
            request.setAttribute("erroCadastro", e.getMessage());
            mostrarCadastro(request, response);
            return;
        } catch (RuntimeException e) {
            request.setAttribute("erroCadastro", "Não foi possível concluir o cadastro. Tente novamente.");
            mostrarCadastro(request, response);
            return;
        }

        sessao.removeAttribute("tentativasCodigo");
        concluirCadastro(request, response, sessao, "morador");
    }

    //Telefone do cadastro só com os números (ou null se não veio).
    private String telefoneSomenteNumeros(CadastroPendente cad) {
        return cad.getTelefone() == null ? null : cad.getTelefone().replaceAll("\\D", "");
    }

    //================ ETAPA 5: ENDERECO (finaliza o cadastro) ================

    private void processarEtapaMoradorEndereco(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sessao = request.getSession();

        if ("voltar".equals(request.getParameter("acao"))) {
            //Quem mora em condomínio passou pela tela do código: volta para ela.
            CadastroPendente atual = (CadastroPendente) sessao.getAttribute("cadastroPendente");
            boolean passouPeloCodigo = atual != null && "condominio".equals(atual.getMoradia());
            sessao.setAttribute("etapaCadastro", passouPeloCodigo ? "token" : "morador-dados");
            mostrarCadastro(request, response);
            return;
        }

        CadastroPendente cad = (CadastroPendente) sessao.getAttribute("cadastroPendente");
        if (cad == null) {
            sessao.setAttribute("etapaCadastro", "conta");
            mostrarCadastro(request, response);
            return;
        }

        String cep = request.getParameter("cep");
        String uf = request.getParameter("uf");
        String cidade = request.getParameter("cidade");
        String rua = request.getParameter("rua");
        String numeroTexto = request.getParameter("numero");
        String bairro = request.getParameter("bairro");
        String complementoDigitado = request.getParameter("complemento");

        //O bloco e o apartamento vêm da tela do código e já estão no cadastro; esta tela não os envia.
        cad.setCep(cep);
        cad.setUf(uf);
        cad.setCidade(cidade);
        cad.setRua(rua);
        cad.setNumero(numeroTexto);
        cad.setBairro(bairro);
        cad.setComplemento(complementoDigitado);
        sessao.setAttribute("cadastroPendente", cad);

        if (cep == null || cep.replaceAll("\\D", "").length() != 8) {
            request.setAttribute("erroCadastro", "CEP inválido, precisa ter 8 dígitos.");
            mostrarCadastro(request, response);
            return;
        }
        if (uf == null || uf.isBlank() || cidade == null || cidade.isBlank()
                || rua == null || rua.isBlank() || numeroTexto == null || numeroTexto.isBlank()
                || bairro == null || bairro.isBlank()) {
            request.setAttribute("erroCadastro", "Preencha todos os campos obrigatórios do endereço.");
            mostrarCadastro(request, response);
            return;
        }

        int numero;
        try {
            numero = Integer.parseInt(numeroTexto.replaceAll("\\D", ""));
        } catch (NumberFormatException nfe) {
            request.setAttribute("erroCadastro", "Número do endereço inválido.");
            mostrarCadastro(request, response);
            return;
        }

        String complemento = vazioParaNulo(complementoDigitado);
        if (complemento != null && complemento.length() > 100) {
            request.setAttribute("erroCadastro", "O complemento pode ter no máximo 100 caracteres.");
            mostrarCadastro(request, response);
            return;
        }

        Endereco endereco = new Endereco(0, cep.replaceAll("\\D", ""), cidade.trim(), uf.trim(), bairro.trim(), rua.trim(), numero, complemento);

        //Mora em condomínio e não informou o código: vira morador sem condomínio. O apartamento e a torre ficam
        //guardados para ligar ao condomínio quando ele passar a usar o app.
        if ("condominio".equals(cad.getMoradia())) {
            concluirMoradorSemCondominio(request, response, sessao, cad, endereco);
            return;
        }

        EnderecoDAO enderecoDAO = new EnderecoDAO();
        int idEndereco = enderecoDAO.inserirERetornarId(endereco);

        if (idEndereco == 0) {
            request.setAttribute("erroCadastro", "Não foi possível salvar o endereço. Tente novamente.");
            mostrarCadastro(request, response);
            return;
        }

        Usuario usuario = new Usuario(0, cad.getNome(), cad.getEmail(), cad.getSenhaHash(), LocalDate.now(), true, idEndereco, 1);
        usuario.setCpf(Formulario.cpf(cad.getCpf(), false));

        if (!usuarioDAO.inserir(usuario)) {
            request.setAttribute("erroCadastro", "Não foi possível concluir o cadastro. Tente novamente.");
            mostrarCadastro(request, response);
            return;
        }

        if (cad.getTelefone() != null && !cad.getTelefone().isBlank()) {
            Usuario criado = usuarioDAO.selecionarPorEmail(cad.getEmail());
            if (criado != null) {
                new TelefoneDAO().inserir(new Telefone(0, cad.getTelefone().replaceAll("\\D", ""), criado.getIdUsuario()));
            }
        }

        concluirCadastro(request, response, sessao, "morador");
    }

    //Grava a conta do morador (tipo Morador) com endereço próprio, apartamento e torre, mas sem condomínio.
    private void concluirMoradorSemCondominio(HttpServletRequest request, HttpServletResponse response, HttpSession sessao,
                                              CadastroPendente cad, Endereco endereco) throws ServletException, IOException {
        int idTipoMorador;
        try {
            idTipoMorador = usuarioDAO.selecionarIdTipoPorNome("Morador");
        } catch (RuntimeException e) {
            idTipoMorador = 0;
        }
        if (idTipoMorador == 0) {
            request.setAttribute("erroCadastro", "Não foi possível concluir o cadastro. Tente novamente.");
            mostrarCadastro(request, response);
            return;
        }

        try {
            Usuario usuario = new Usuario(0, cad.getNome().trim(), cad.getEmail(), cad.getSenhaHash(), LocalDate.now(), true, 0, idTipoMorador);
            usuario.setCpf(Formulario.cpf(cad.getCpf(), false));

            moradorDAO.cadastrarComUsuario(usuario, telefoneSomenteNumeros(cad), endereco,
                    vazioParaNulo(cad.getApto()), vazioParaNulo(cad.getBloco()), null);
        } catch (DadoInvalido e) {
            request.setAttribute("erroCadastro", e.getMessage());
            mostrarCadastro(request, response);
            return;
        } catch (RuntimeException e) {
            request.setAttribute("erroCadastro", "Não foi possível concluir o cadastro. Tente novamente.");
            mostrarCadastro(request, response);
            return;
        }

        concluirCadastro(request, response, sessao, "morador");
    }

    //Vazio vira null; preenchido volta sem os espaços das pontas.
    private String vazioParaNulo(String valor) {
        return (valor == null || valor.isBlank()) ? null : valor.trim();
    }

    //================ ASSINATURA: TELAS DE PAGAMENTO (só a navegação, depois do cadastro do síndico e da cooperativa) ================
    // Fluxo: pag-dados -> pag-resumo -> pag-pagamento
    // O plano é o do tipo do condomínio (tabela plano, codigo = residencial, comercial ou industrial) e o valor é sempre
    // calculado aqui no servidor. "Ir para o pagamento" grava a assinatura como pendente.
    // TODO: sem gateway ainda: o último passo não cobra nada e a assinatura continua pendente.

    //Conta criada: guarda só o que as telas de pagamento aproveitam (nunca a senha) e segue direto para elas.
    //A tela de sucesso (com o código do condomínio) só aparece no fim, depois do pagamento.
    private void seguirParaAssinatura(HttpServletRequest request, HttpServletResponse response, HttpSession sessao, CadastroPendente cad,
                                      int idCondominio, int quantidade) throws ServletException, IOException {
        Usuario criado = usuarioDAO.selecionarPorEmail(cad.getEmail());

        CadastroPendente assinatura = new CadastroPendente();
        assinatura.setNome(cad.getNome());
        assinatura.setEmail(cad.getEmail());
        assinatura.setTelefone(cad.getTelefone());
        assinatura.setCpf(cad.getCpf());
        assinatura.setDocumento(cad.getCpf());
        assinatura.setContaCriada(true);
        assinatura.setIdUsuario(criado == null ? 0 : criado.getIdUsuario());
        assinatura.setIdCondominio(idCondominio);
        assinatura.setPlano(cad.getTipoCondominio());
        assinatura.setQuantidade(Math.min(Math.max(quantidade, 1), Orcamento.QUANTIDADE_MAXIMA));
        assinatura.setCiclo("mensal");

        //Menos de 100 unidades não assina: pula plano e pagamento e mostra o código do condomínio.
        if (!Orcamento.podeAssinar(cad.getTipoCondominio(), quantidade)) {
            concluirAssinatura(request, response, assinatura);
            return;
        }

        sessao.setAttribute("cadastroPendente", assinatura);
        sessao.removeAttribute("etapaCadastro");
        sessao.removeAttribute("abaAtiva");

        //Redirect: atualizar a página não reenvia o endereço (o que gravaria o condomínio de novo).
        response.sendRedirect(request.getContextPath() + "/cadastro/plano");
    }

    //Depois da tela de pagamento: limpa a sessão do cadastro e mostra a tela de sucesso com o código do condomínio.
    private void concluirAssinatura(HttpServletRequest request, HttpServletResponse response, CadastroPendente cad) throws ServletException, IOException {
        HttpSession sessao = request.getSession();
        sessao.removeAttribute("cadastroPendente");
        sessao.removeAttribute("etapaCadastro");
        sessao.removeAttribute("abaAtiva");

        if (cad.getIdCondominio() != null) {
            try {
                Condominio condominio = condominioDAO.selecionarPorId(cad.getIdCondominio());
                if (condominio != null) request.setAttribute("tokenCondominio", condominio.getToken());
            } catch (RuntimeException e) {
                //sem o código a tela de sucesso aparece do mesmo jeito
            }
        }
        request.setAttribute("perfilCriado", cad.getPlano());
        request.getRequestDispatcher("/WEB-INF/views/cadastro/cadastro_sucesso.jsp").forward(request, response);
    }

    //Cadastro que já tem conta criada no banco; null se não houver.
    private CadastroPendente cadastroDaAssinatura(HttpServletRequest request) {
        CadastroPendente cad = (CadastroPendente) request.getSession().getAttribute("cadastroPendente");
        return cad != null && cad.isContaCriada() ? cad : null;
    }

    //Orçamento com o preço atual do banco; null se o plano não existir ou estiver inativo.
    private Orcamento orcamento(CadastroPendente cad) {
        Plano plano = planoDAO.selecionarPorCodigo(cad.getPlano());
        return plano == null ? null : new Orcamento(plano, "anual".equals(cad.getCiclo()), cad.getQuantidade());
    }

    //Resumo e pagamento mostram os valores do orçamento (orc nos JSPs).
    private void mostrarAssinatura(HttpServletRequest request, HttpServletResponse response, String etapa) throws ServletException, IOException {
        if (etapa.equals("pag-resumo") || etapa.equals("pag-pagamento")) {
            Orcamento orc;
            try {
                orc = orcamento(cadastroDaAssinatura(request));
            } catch (RuntimeException e) {
                orc = null;
            }
            if (orc == null) {
                request.setAttribute("erroCadastro", "O plano do seu condomínio não está disponível no momento.");
                etapa = "pag-dados";
            } else {
                request.setAttribute("orc", orc);
            }
        }
        request.getSession().setAttribute("etapaCadastro", etapa);
        mostrarCadastro(request, response);
    }

    //GET /cadastro/plano: botão "Escolher meu plano" da tela de conta criada.
    private void iniciarAssinatura(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CadastroPendente cad = cadastroDaAssinatura(request);
        if (cad == null) {
            response.sendRedirect(request.getContextPath() + "/login-cadastro?aba=cadastro");
            return;
        }
        mostrarAssinatura(request, response, cad.isDadosPagamentoOk() ? "pag-resumo" : "pag-dados");
    }

    //Sem conta criada na sessão: volta para o início do cadastro.
    private void voltarParaInicio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        mostrarAssinatura(request, response, "conta");
    }

    //----- Dados de cobrança -----

    private void processarEtapaPagDados(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CadastroPendente cad = cadastroDaAssinatura(request);
        if (cad == null) {
            voltarParaInicio(request, response);
            return;
        }
        //A conta já está gravada: não há etapa anterior para editar. Sai para o início; o cadastro fica na sessão
        //e /cadastro/plano retoma daqui.
        if ("voltar".equals(request.getParameter("acao"))) {
            response.sendRedirect(request.getContextPath() + "/inicio");
            return;
        }

        String nome = request.getParameter("nome");
        String telefone = request.getParameter("telefone");
        String documento = request.getParameter("documento");
        cad.setNome(nome);
        cad.setTelefone(telefone);
        cad.setDocumento(documento);
        cad.setTermos(request.getParameter("termos"));
        cad.setDadosPagamentoOk(false);

        String doc = Formulario.digitos(documento);
        String erro = null;
        if (nome == null || !nome.trim().matches("\\S+(\\s+\\S+)+")) {
            erro = "Digite o nome completo, com nome e sobrenome.";
        } else if (telefone == null || Formulario.digitos(telefone).length() < 10 || Formulario.digitos(telefone).length() > 11) {
            erro = "Informe um telefone válido.";
        } else if (doc.length() != 11 && doc.length() != 14) {
            erro = "CPF ou CNPJ inválido: o CPF tem 11 números e o CNPJ tem 14.";
        } else if (!"aceito".equals(cad.getTermos())) {
            erro = "É preciso aceitar os Termos de uso e a Política de privacidade.";
        }
        if (erro != null) {
            request.setAttribute("erroCadastro", erro);
            mostrarAssinatura(request, response, "pag-dados");
            return;
        }

        cad.setDadosPagamentoOk(true);
        mostrarAssinatura(request, response, "pag-resumo");
    }

    //----- Resumo -----

    private void processarEtapaPagResumo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CadastroPendente cad = cadastroDaAssinatura(request);
        if (cad == null) {
            voltarParaInicio(request, response);
            return;
        }
        if (!cad.isDadosPagamentoOk()) {
            mostrarAssinatura(request, response, "pag-dados");
            return;
        }

        String acao = Formulario.texto(request, "acao");
        if (acao.equals("voltar")) {
            mostrarAssinatura(request, response, "pag-dados");
            return;
        }

        if (acao.equals("ciclo-mensal")) cad.setCiclo("mensal");
        if (acao.equals("ciclo-anual")) cad.setCiclo("anual");

        //Quantidade digitada vem formatada ("1.250"): só os números valem.
        String digitos = Formulario.digitos(request.getParameter("quantidade"));
        int quantidade = digitos.isEmpty() || digitos.length() > 7 ? 0 : Integer.parseInt(digitos);
        if (acao.equals("mais")) quantidade++;
        if (acao.equals("menos")) quantidade--;
        if (quantidade < 1 || quantidade > Orcamento.QUANTIDADE_MAXIMA) {
            request.setAttribute("erroCadastro", "Informe uma quantidade entre 1 e 9.999.999.");
            mostrarAssinatura(request, response, "pag-resumo");
            return;
        }
        if (!Orcamento.podeAssinar(cad.getPlano(), quantidade)) {
            request.setAttribute("erroCadastro", "A assinatura é para " + Orcamento.UNIDADES_MINIMAS + " unidades ou mais.");
            mostrarAssinatura(request, response, "pag-resumo");
            return;
        }
        cad.setQuantidade(quantidade);

        //Só o botão principal (sem ação) segue para o pagamento; ciclo, +/- e "Atualizar valor" ficam no resumo.
        if (!acao.isEmpty()) {
            mostrarAssinatura(request, response, "pag-resumo");
            return;
        }

        //Grava (ou atualiza) a assinatura pendente com o valor calculado aqui, nunca com valor vindo do formulário.
        try {
            Orcamento orc = orcamento(cad);
            if (orc == null) {
                mostrarAssinatura(request, response, "pag-resumo"); //mostra o aviso de plano indisponível
                return;
            }
            Assinatura assinatura = new Assinatura(cad.getCiclo(), BigDecimal.valueOf(cad.getQuantidade()), orc.getValorCobrado(),
                    cad.getIdUsuario(), cad.getIdCondominio(), orc.getPlano().getIdPlano());
            cad.setIdAssinatura(assinaturaDAO.salvarPendente(assinatura));
        } catch (RuntimeException e) {
            request.setAttribute("erroCadastro", "Não foi possível registrar a assinatura. Confira se já não existe uma assinatura para este condomínio.");
            mostrarAssinatura(request, response, "pag-resumo");
            return;
        }

        mostrarAssinatura(request, response, "pag-pagamento");
    }

    //----- Forma de pagamento (sem cobrança) -----

    private void processarEtapaPagPagamento(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CadastroPendente cad = cadastroDaAssinatura(request);
        if (cad == null) {
            voltarParaInicio(request, response);
            return;
        }
        if (!cad.isDadosPagamentoOk()) {
            mostrarAssinatura(request, response, "pag-dados");
            return;
        }

        if ("voltar".equals(request.getParameter("acao"))) {
            mostrarAssinatura(request, response, "pag-resumo");
            return;
        }
        if (cad.getIdAssinatura() == 0) {
            mostrarAssinatura(request, response, "pag-resumo"); //a assinatura pendente é gravada no resumo
            return;
        }

        //Não lê nem guarda dados de cartão: sem gateway, nada é cobrado nem gerado.
        //TODO: com o gateway, ativar a assinatura (status ativa, data_inicio e proxima_cobranca) quando o pagamento for confirmado.
        String metodo = request.getParameter("metodo");
        cad.setMetodo("pix".equals(metodo) || "boleto".equals(metodo) ? metodo : "cartao");
        concluirAssinatura(request, response, cad);
    }
}