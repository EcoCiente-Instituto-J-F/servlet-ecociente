package com.example.ecociente.controller;

import com.example.ecociente.dao.SindicoDAO;
import com.example.ecociente.model.Sindico;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String caminho = request.getServletPath();
        if (caminho.equals("/adicionarSindico")) {
            adicionarSindico(request, response);
        }
    }

    private void adicionarSindico(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cpf = request.getParameter("cpf");
        LocalDate dataInicio = LocalDate.parse(request.getParameter("dataInicioMandato"));
        LocalDate dataFim = lerDataFim(request);
        int idCondominio = Integer.parseInt(request.getParameter("idCondominio"));
        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));

        //Dados do usuário vão vazios (a tabela sindico não usa). Id 0 porque o banco gera.
        boolean adicionado = daoSindicos.inserir(new Sindico(
                idUsuario, null, null, null, null, false, 0, 0,
                0, cpf, dataInicio, dataFim, idCondominio
        ));

        request.setAttribute("adicionado", adicionado ? "true" : "false");
        mostrarSelects(request, response);
    }

    //Data de fim do mandato é opcional: vazia vira null.
    private LocalDate lerDataFim(HttpServletRequest request) {
        String dataFim = request.getParameter("dataFimMandato");
        if (dataFim == null || dataFim.isBlank()) {
            return null;
        }
        return LocalDate.parse(dataFim);
    }
}