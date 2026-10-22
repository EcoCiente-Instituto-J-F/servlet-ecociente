package com.example.ecociente.controller;

import com.example.ecociente.dao.PainelDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

//Home do painel (/painel) e so o miolo com cards e graficos (/painel/conteudo), que o painel.js recarrega sozinho.
@WebServlet(urlPatterns = {"/painel", "/painel/conteudo"})
public class PainelServlet extends HttpServlet {
    PainelDAO painelDAO = new PainelDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        for (Map.Entry<String, Object> dado : painelDAO.dadosPainel().entrySet()) {
            request.setAttribute(dado.getKey(), dado.getValue());
        }

        String pagina = request.getServletPath().equals("/painel/conteudo")
                ? "/WEB-INF/views/painel-conteudo.jsp"
                : "/WEB-INF/views/painel.jsp";
        request.getRequestDispatcher(pagina).forward(request, response);
    }
}
