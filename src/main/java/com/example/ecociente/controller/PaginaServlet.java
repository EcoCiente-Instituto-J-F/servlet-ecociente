package com.example.ecociente.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// Abre as páginas fixas do site (landing page e tela de login/cadastro)
@WebServlet(urlPatterns = {"/inicio", "/login-cadastro"})
public class PaginaServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String caminho = request.getServletPath();

        if (caminho.equals("/login-cadastro")) {
            request.getRequestDispatcher("/WEB-INF/views/cadastro_login.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(request, response);
        }
    }
}