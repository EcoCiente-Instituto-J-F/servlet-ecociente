package com.example.ecociente.controller;

import com.example.ecociente.dao.SindicoDAO;
import com.example.ecociente.model.Sindico;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

//Servlet usada para as ações do CRUD de Síndicos.
@WebServlet(urlPatterns = {"/selectSindico", "/adicionarSindico", "/alterarSindico", "/deletarSindico"})
public class SindicoServlet extends HttpServlet {
    SindicoDAO daoSindicos = new SindicoDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getServletPath().equals("/selectSindico")) {
            mostrarSelects(request, response);
        }
    }

    //Busca os síndicos e envia para a JSP.
    private void mostrarSelects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Sindico> sindicos = daoSindicos.listarTodos();

        request.setAttribute("sindicos", sindicos);
        request.getRequestDispatcher("/WEB-INF/views/sindicos.jsp").forward(request, response);
    }
}