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

//Servlet responsavel pelo login da tela administrativa (login-crud.jsp).
@WebServlet(urlPatterns = {"/login-crud"})
public class LoginServlet extends HttpServlet {
    UsuarioDAO daoUsuarios = new UsuarioDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/login-crud.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String senha = request.getParameter("senha");

        Usuario usuario = daoUsuarios.selecionarPorEmail(email);

        if (usuario == null || !Senha.conferir(senha, usuario.getSenhaHash())) {
            request.setAttribute("erroLogin", "E-mail ou senha incorretos.");
            doGet(request, response);
            return;
        }

        //id 5 = Administrador na tabela tipo_usuario
        if (usuario.getIdTipoUsuario() != 5) {
            request.setAttribute("erroLogin", "Esse login e exclusivo para administradores.");
            doGet(request, response);
            return;
        }

        HttpSession sessao = request.getSession();
        sessao.setAttribute("idUsuarioLogado", usuario.getIdUsuario());
        sessao.setAttribute("nomeUsuarioLogado", usuario.getNome());

        response.sendRedirect(request.getContextPath() + "/painel");
    }
}