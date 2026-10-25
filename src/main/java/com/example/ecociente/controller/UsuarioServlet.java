package com.example.ecociente.controller;

import com.example.ecociente.dao.CooperativaDAO;
import com.example.ecociente.dao.EnderecoDAO;
import com.example.ecociente.dao.TelefoneDAO;
import com.example.ecociente.dao.UsuarioDAO;
import com.example.ecociente.model.CadastroPendente;
import com.example.ecociente.model.Cooperativa;
import com.example.ecociente.model.Endereco;
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
import java.time.LocalDate;

@WebServlet(urlPatterns = {"/cadastro", "/usuarios"})
public class UsuarioServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;
    private CooperativaDAO cooperativaDAO;

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
        cooperativaDAO = new CooperativaDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String caminho = request.getServletPath();

        if (caminho.equals("/usuarios")) {
            mostrarUsuarios(request, response);
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
            } else if ("tipo".equals(etapa) || "token".equals(etapa)) {
                processarEtapaEmConstrucao(request, response, etapa);
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

        concluirCadastro(request, response, sessao, "cooperativa");
    }

    //Limpa o cadastro da sessao e mostra a tela de cadastro concluido.
    private void concluirCadastro(HttpServletRequest request, HttpServletResponse response, HttpSession sessao, String perfilCriado) throws ServletException, IOException {
        sessao.removeAttribute("cadastroPendente");
        sessao.removeAttribute("etapaCadastro");
        sessao.removeAttribute("abaAtiva");

        request.setAttribute("perfilCriado", perfilCriado);
        request.getRequestDispatcher("/WEB-INF/views/cadastro/cadastro_sucesso.jsp").forward(request, response);
    }

    // TODO: telas que ainda não gravam no banco (endereço da cooperativa, tipo do condomínio, token do morador).
    // Por enquanto só deixam voltar ou avisam que a etapa está em construção.
    private void processarEtapaEmConstrucao(HttpServletRequest request, HttpServletResponse response, String etapa) throws ServletException, IOException {
        HttpSession sessao = request.getSession();
        CadastroPendente cad = (CadastroPendente) sessao.getAttribute("cadastroPendente");

        if (cad == null) {
            sessao.setAttribute("etapaCadastro", "conta");
        } else if ("voltar".equals(request.getParameter("acao"))) {
            String anterior;
            if ("cooperativa-endereco".equals(etapa)) {
                anterior = "cooperativa-dados";
            } else if ("tipo".equals(etapa)) {
                anterior = "sindico-dados";
            } else {
                anterior = "morador-dados";
            }
            sessao.setAttribute("etapaCadastro", anterior);
        } else {
            request.setAttribute("erroCadastro", "Esta etapa do cadastro ainda está em construção.");
        }
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
}