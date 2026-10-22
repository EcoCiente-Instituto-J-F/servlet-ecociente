package com.example.ecociente.controller;

import com.example.ecociente.dao.PainelDAO;
import com.example.ecociente.dao.UsuarioDAO;
import com.example.ecociente.model.CadastroPendente;
import com.example.ecociente.model.Endereco;
import com.example.ecociente.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.ecociente.util.DadoInvalido;
import com.example.ecociente.util.FormPainel;
import com.example.ecociente.util.Senha;
import jakarta.servlet.http.HttpSession;

import java.time.LocalDate;


import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/cadastro", "/usuarios"})
public class UsuarioServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;
    private final PainelDAO painelDAO = new PainelDAO();

    @Override
    public void init() {
        usuarioDAO = new UsuarioDAO();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        String caminho = request.getServletPath();

        if (caminho.equals("/usuarios")){
            mostrarUsuarios(request, response);
        } else if (caminho.equals("/cadastro")) {
            response.sendRedirect(request.getContextPath() + "/login-cadastro?aba=cadastro");
        }

    }

    //Busca os usuarios (com pesquisa opcional) e os tipos (para o select) e envia para a JSP do painel.
    private void mostrarUsuarios(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String busca = request.getParameter("q");
        if (busca == null) busca = request.getParameter("search");

        request.setAttribute("usuarios", FormPainel.paginar(request, painelDAO.listarUsuarios(busca)));
        request.setAttribute("tiposUsuario", painelDAO.listarTiposUsuario());
        request.getRequestDispatcher("/WEB-INF/views/usuarios.jsp").forward(request, response);
    }

    //POST /usuarios do painel: acao=excluir apaga, id vazio cadastra, id preenchido altera.
    private void tratarPainel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String acao = FormPainel.texto(request, "acao");
        String id = FormPainel.texto(request, "id");
        String flag = acao.equals("excluir") ? "deletado" : id.isEmpty() ? "adicionado" : "alterado";
        request.setAttribute(flag, "false");

        try {
            boolean ok;
            if (acao.equals("excluir")) {
                ok = excluirUsuarioPainel(request, FormPainel.id(id, "usuário"));
            } else if (id.isEmpty()) {
                criarUsuarioPainel(request);
                ok = true;
            } else {
                ok = alterarUsuarioPainel(request, FormPainel.id(id, "usuário"));
            }
            request.setAttribute(flag, ok ? "true" : "false");
        } catch (DadoInvalido e) {
            request.setAttribute("erro", e.getMessage());
        } catch (RuntimeException e) {
            request.setAttribute("erro", "Operação não concluída. Confira os dados informados e se o registro possui vínculos.");
        }
        mostrarUsuarios(request, response);
    }

    //Tipo vem como id (idTipoUsuario) ou como nome (tipo), igual ao que esta em tipo_usuario.
    private int lerTipoUsuario(HttpServletRequest request) {
        String idTipo = FormPainel.texto(request, "idTipoUsuario");
        if (!idTipo.isEmpty()) return FormPainel.id(idTipo, "tipo de usuário");

        String nome = FormPainel.texto(request, "tipo");
        Integer porNome = nome.isEmpty() ? null : painelDAO.idTipoUsuarioPorNome(nome);
        if (porNome == null) throw new DadoInvalido("Tipo de usuário inválido.");
        return porNome;
    }

    private void criarUsuarioPainel(HttpServletRequest request) {
        String nome = FormPainel.exigir(FormPainel.texto(request, "nome"), "o nome", 100);
        String email = FormPainel.email(FormPainel.texto(request, "email"));
        String senha = request.getParameter("senha");
        if (senha == null || senha.length() < 6) throw new DadoInvalido("A senha precisa ter pelo menos 6 caracteres.");
        int tipo = lerTipoUsuario(request);
        String telefone = FormPainel.telefone(request.getParameter("telefone"));
        Endereco endereco = FormPainel.endereco(request, false);

        if (painelDAO.emailEmUso(email, 0)) throw new DadoInvalido("Este e-mail já está cadastrado.");

        painelDAO.criarUsuario(nome, email, Senha.gerarHash(senha), tipo, telefone, endereco);
    }

    private boolean alterarUsuarioPainel(HttpServletRequest request, int id) {
        String nome = FormPainel.exigir(FormPainel.texto(request, "nome"), "o nome", 100);
        String email = FormPainel.email(FormPainel.texto(request, "email"));
        String senha = request.getParameter("senha");
        if (senha != null && !senha.isEmpty() && senha.length() < 6) throw new DadoInvalido("A senha precisa ter pelo menos 6 caracteres.");
        int tipo = lerTipoUsuario(request);
        String telefone = FormPainel.telefone(request.getParameter("telefone"));
        Endereco endereco = FormPainel.endereco(request, false);

        if (painelDAO.emailEmUso(email, id)) throw new DadoInvalido("Este e-mail já está cadastrado.");

        String senhaHash = (senha == null || senha.isEmpty()) ? null : Senha.gerarHash(senha);
        return painelDAO.atualizarUsuario(id, nome, email, senhaHash, tipo, telefone, endereco);
    }

    private boolean excluirUsuarioPainel(HttpServletRequest request, int id) {
        Object logado = request.getSession().getAttribute("idUsuarioLogado");
        if (logado != null && (Integer) logado == id) throw new DadoInvalido("Você não pode excluir o próprio usuário.");

        Usuario usuario = usuarioDAO.selecionarUsuarioPorId(id);
        if (usuario == null) return false;
        return usuarioDAO.deletar(usuario);
    }

    private void mostrarCadastro(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getSession().setAttribute("abaAtiva", "cadastro");
        request.getRequestDispatcher("/WEB-INF/views/cadastro_login.jsp").forward(request, response);
    }

    private void processarEtapaConta(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String confirmaSenha = request.getParameter("confirma_senha");

        String erro = validarCadastro(nome, email, senha, confirmaSenha);
        if (erro == null && usuarioDAO.existeEmail(email.trim().toLowerCase())) {
            erro = "Este e-mail já está cadastrado.";
        }
        if (erro != null) {
            request.setAttribute("erroCadastro", erro);
            mostrarCadastro(request, response);
            return;
        }

        CadastroPendente cad = new CadastroPendente();
        cad.setNome(nome.trim());
        cad.setEmail(email.trim().toLowerCase());
        cad.setSenhaHash(Senha.gerarHash(senha));

        HttpSession sessao = request.getSession();
        sessao.setAttribute("cadastroPendente", cad);
        sessao.setAttribute("etapaCadastro", "perfil");

        mostrarCadastro(request, response);
    }

    private void processarEtapaPerfil(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession sessao = request.getSession();

        if ("voltar".equals(request.getParameter("acao"))) {
            sessao.setAttribute("etapaCadastro", "conta");
            mostrarCadastro(request, response);
            return;
        }

        CadastroPendente cad = (CadastroPendente) sessao.getAttribute("cadastroPendente");
        if (cad == null) {
            // sessao expirou ou a etapa foi acessada fora de ordem -- volta pro inicio
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

        // TODO: a etapa seguinte (depois de "perfil") ainda nao esta conectada.
        request.setAttribute("erroCadastro", "Perfil \"" + perfil + "\" selecionado. A próxima etapa ainda está em construção.");
        mostrarCadastro(request, response);
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
            } else {
                processarEtapaConta(request, response);
            }
        }
    }

    private void cadastrarUsuario(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nome = request.getParameter("nome");
        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        String confirmaSenha = request.getParameter("confirma_senha");

        String erro = validarCadastro(nome, email, senha, confirmaSenha);
        if (erro == null && usuarioDAO.existeEmail(email.trim().toLowerCase())) {
            erro = "Este e-mail já está cadastrado.";
        }
        if (erro != null) {
            request.setAttribute("erroCadastro", erro);
            request.getRequestDispatcher("/WEB-INF/views/cadastro_login.jsp").forward(request, response);
            return;
        }

        Usuario usuario = new Usuario(
                0,
                nome.trim(),
                email.trim().toLowerCase(),
                Senha.gerarHash(senha),
                LocalDate.now(),
                true,
                1
        );

        if (!usuarioDAO.inserir(usuario)) {
            request.setAttribute("erroCadastro", "Não foi possível concluir o cadastro. Tente novamente.");
            request.getRequestDispatcher("/WEB-INF/views/cadastro_login.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/inicio");
    }

    private String validarCadastro(String nome, String email, String senha, String confirmaSenha) {
        if (nome == null || nome.isBlank()) return "Informe seu nome.";
        if (nome.trim().length() > 150) return "O nome pode ter no máximo 150 caracteres.";
        if (email == null || !email.trim().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$")) return "Informe um e-mail válido.";
        if (senha == null || senha.length() < 6) return "A senha precisa ter pelo menos 6 caracteres.";
        if (!senha.equals(confirmaSenha)) return "As senhas não conferem.";
        return null;
    }
}
