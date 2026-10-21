package com.example.ecociente.controller;

import com.example.ecociente.dao.UsuarioDAO;
import com.example.ecociente.model.Usuario;
import com.example.ecociente.util.Senha;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

//Servlet responsavel pelos dois logins do sistema: usuario comum (/login) e painel administrativo (/login-crud).
@WebServlet(urlPatterns = {"/login", "/login-crud", "/logout"})
public class LoginServlet extends HttpServlet {
    UsuarioDAO daoUsuarios = new UsuarioDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getServletPath().equals("/logout")) {
            HttpSession sessao = request.getSession(false);
            if (sessao != null) sessao.invalidate();
            response.sendRedirect(request.getContextPath() + "/inicio");
            return;
        }
        mostrarTela(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        boolean telaAdmin = request.getServletPath().equals("/login-crud");

        Usuario usuario = daoUsuarios.selecionarPorEmail(email);

        if (usuario == null || !Senha.conferir(senha, usuario.getSenhaHash())) {
            request.setAttribute("erroLogin", "E-mail ou senha incorretos.");
            mostrarTela(request, response);
            return;
        }

        //id 5 = Administrador. So a tela do painel exige esse tipo.
        if (telaAdmin && usuario.getIdTipoUsuario() != 5) {
            request.setAttribute("erroLogin", "Esse login e exclusivo para administradores.");
            mostrarTela(request, response);
            return;
        }

        HttpSession sessao = request.getSession();
        sessao.setAttribute("idUsuarioLogado", usuario.getIdUsuario());
        sessao.setAttribute("nomeUsuarioLogado", usuario.getNome());

        response.sendRedirect(request.getContextPath() + (telaAdmin ? "/painel" : "/inicio"));
    }

    private void mostrarTela(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pagina = request.getServletPath().equals("/login-crud")
                ? "/WEB-INF/views/login-crud.jsp"
                : "/WEB-INF/views/cadastro_login.jsp";
        request.getRequestDispatcher(pagina).forward(request, response);
    }
}