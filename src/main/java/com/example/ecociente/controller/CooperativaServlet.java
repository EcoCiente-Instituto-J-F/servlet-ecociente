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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String caminho = request.getServletPath();
        if (caminho.equals("/adicionarCooperativa")) {
            adicionarCooperativa(request, response);
        }
    }

    private void adicionarCooperativa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cnpj = request.getParameter("cnpj");
        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));

        //Dados do usuário vão vazios (a tabela cooperativa não usa). Id 0 porque o banco gera.
        boolean adicionado = daoCooperativas.inserir(new Cooperativa(
                idUsuario, null, null, null, null, false, 0, 0,
                0, cnpj
        ));

        request.setAttribute("adicionado", adicionado ? "true" : "false");
        mostrarSelects(request, response);
    }
}