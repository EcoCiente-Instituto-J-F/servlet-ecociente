package com.example.ecociente.controller;

import com.example.ecociente.dao.UsuarioDAO;
import com.example.ecociente.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
}
