package com.example.ecociente.controller;

import com.example.ecociente.dao.UsuarioDAO;
import com.example.ecociente.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.ecociente.util.Senha;
import java.time.LocalDate;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/cadastro", "/selectUsuario"})
public class UsuarioServlet extends HttpServlet {

    private UsuarioDAO usuarioDAO;

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

        if (caminho.equals("/selectUsuario")){
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
            cadastrarUsuario(request, response);
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
        if (email == null || !email.trim().matches("^[\\w.+-]+@[\\w-]+\\.[\\w.]+$")) return "Informe um e-mail válido.";
        if (senha == null || senha.length() < 6) return "A senha precisa ter pelo menos 6 caracteres.";
        if (!senha.equals(confirmaSenha)) return "As senhas não conferem.";
        return null;
    }
}
