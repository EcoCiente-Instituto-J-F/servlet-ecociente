package com.example.ecociente.controller;

import com.example.ecociente.dao.EnderecoDAO;
import com.example.ecociente.dao.TelefoneDAO;
import com.example.ecociente.dao.UsuarioDAO;
import com.example.ecociente.model.CadastroPendente;
import com.example.ecociente.model.Endereco;
import com.example.ecociente.model.Telefone;
import com.example.ecociente.model.Usuario;
import com.example.ecociente.util.Senha;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet(urlPatterns = {"/cadastro", "/usuarios"})
public class UsuarioServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String caminho = request.getServletPath();

        if (caminho.equals("/usuarios")) {
            List<Usuario> usuarios = usuarioDAO.selecionarTodos();
            request.setAttribute("usuarios", usuarios);
            request.getRequestDispatcher(
                    "/WEB-INF/views/lista-usuarios.jsp" // não temos o caminho do jsp ainda
            ).forward(request, response);
        } else if (caminho.equals("/cadastro")) {
            response.sendRedirect(request.getContextPath() + "/login-cadastro?aba=cadastro");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        if (request.getServletPath().equals("/cadastro")) {
            String etapa = request.getParameter("etapa");
            if ("perfil".equals(etapa)) {
                processarEtapaPerfil(request, response);
            } else if ("moradia".equals(etapa)) {
                processarEtapaMoradia(request, response);
            } else if ("morador-dados".equals(etapa)) {
                processarEtapaMoradorDados(request, response);
            } else if ("morador-endereco".equals(etapa)) {
                processarEtapaMoradorEndereco(request, response);
            } else {
                processarEtapaConta(request, response);
            }
        }
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
        if (email == null || !email.trim().matches("^[\\w.+-]+@[\\w-]+\\.[\\w.]+$")) return "Informe um e-mail válido.";
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

        cad.setPerfil(perfil);
        sessao.setAttribute("cadastroPendente", cad);

        if ("morador".equals(perfil)) {
            sessao.setAttribute("etapaCadastro", "moradia");
            mostrarCadastro(request, response);
            return;
        }

        // TODO: cooperativa e condominio ainda nao estao conectados.
        request.setAttribute("erroCadastro", "Perfil \"" + perfil + "\" selecionado. A próxima etapa ainda está em construção.");
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
        sessao.setAttribute("cadastroPendente", cad);

        if ("casa".equals(moradia)) {
            sessao.setAttribute("etapaCadastro", "morador-dados");
            mostrarCadastro(request, response);
            return;
        }

        // TODO: moradia="condominio" precisa do token do sindico, ainda nao implementado.
        request.setAttribute("erroCadastro", "O cadastro vinculado a um condomínio ainda está em construção. Tente a opção \"Moro em casa\" por enquanto.");
        mostrarCadastro(request, response);
    }

    //================ ETAPA 4: DADOS PESSOAIS (nome, telefone, cpf, nascimento) ================

    private void processarEtapaMoradorDados(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sessao = request.getSession();

        if ("voltar".equals(request.getParameter("acao"))) {
            sessao.setAttribute("etapaCadastro", "moradia");
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

        sessao.setAttribute("etapaCadastro", "morador-endereco");
        mostrarCadastro(request, response);
    }

    //================ ETAPA 5: ENDERECO (finaliza o cadastro) ================

    private void processarEtapaMoradorEndereco(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        String cep = request.getParameter("cep");
        String uf = request.getParameter("uf");
        String cidade = request.getParameter("cidade");
        String rua = request.getParameter("rua");
        String numeroTexto = request.getParameter("numero");
        String bairro = request.getParameter("bairro");
        String bloco = request.getParameter("bloco");
        String apto = request.getParameter("apto");

        cad.setCep(cep);
        cad.setUf(uf);
        cad.setCidade(cidade);
        cad.setRua(rua);
        cad.setNumero(numeroTexto);
        cad.setBairro(bairro);
        cad.setBloco(bloco);
        cad.setApto(apto);
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

        String complemento = null;
        if (bloco != null && !bloco.isBlank()) complemento = "Bloco " + bloco.trim();
        if (apto != null && !apto.isBlank()) {
            complemento = (complemento == null ? "" : complemento + ", ") + "Apto " + apto.trim();
        }

        EnderecoDAO enderecoDAO = new EnderecoDAO();
        int idEndereco = enderecoDAO.inserirERetornarId(
                new Endereco(0, cep.replaceAll("\\D", ""), cidade.trim(), uf.trim(), bairro.trim(), rua.trim(), numero, complemento)
        );

        if (idEndereco == 0) {
            request.setAttribute("erroCadastro", "Não foi possível salvar o endereço. Tente novamente.");
            mostrarCadastro(request, response);
            return;
        }

        Usuario usuario = new Usuario(0, cad.getNome(), cad.getEmail(), cad.getSenhaHash(), LocalDate.now(), true, idEndereco, 1);

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

        sessao.removeAttribute("cadastroPendente");
        sessao.removeAttribute("etapaCadastro");
        sessao.removeAttribute("abaAtiva");

        response.sendRedirect(request.getContextPath() + "/inicio");
    }
}