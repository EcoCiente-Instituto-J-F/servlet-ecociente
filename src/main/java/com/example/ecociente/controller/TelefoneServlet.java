package com.example.ecociente.controller;

import com.example.ecociente.dao.TelefoneDAO;
import com.example.ecociente.model.Telefone;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "TelefoneServlet", value = "/telefones")
public class TelefoneServlet extends HttpServlet {

    private TelefoneDAO telefoneDAO;

    @Override
    public void init() {
        telefoneDAO = new TelefoneDAO();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        List<Telefone> telefones = telefoneDAO.listarTodos();

        request.setAttribute("telefones", telefones);

        request.getRequestDispatcher(
                "/WEB-INF/views/lista-telefones.jsp" // não temos o caminho do jsp ainda
        ).forward(request, response);
    }
}
