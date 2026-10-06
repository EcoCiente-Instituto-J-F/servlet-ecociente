package com.example.ecociente.controller;

import com.example.ecociente.dao.CooperativaDAO;
import com.example.ecociente.model.Cooperativa;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

//Servlet usada para as ações do CRUD de Cooperativas.
@WebServlet(urlPatterns = {"/selectCooperativa", "/adicionarCooperativa", "/alterarCooperativa", "/deletarCooperativa"})
public class CooperativaServlet extends HttpServlet {
    CooperativaDAO daoCooperativas = new CooperativaDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getServletPath().equals("/selectCooperativa")) {
            mostrarSelects(request, response);
        }
    }

    //Busca as cooperativas e envia para a JSP.
    private void mostrarSelects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Cooperativa> cooperativas = daoCooperativas.listarTodas();

        request.setAttribute("cooperativas", cooperativas);
        request.getRequestDispatcher("/WEB-INF/views/cooperativas.jsp").forward(request, response);
    }
}