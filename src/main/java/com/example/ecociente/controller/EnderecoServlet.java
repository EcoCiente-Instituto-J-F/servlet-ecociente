package com.example.ecociente.controller;

import com.example.ecociente.dao.EnderecoDAO;
import com.example.ecociente.model.Endereco;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "EnderecoServlet", value = "/enderecos")
public class EnderecoServlet extends HttpServlet {

    private EnderecoDAO enderecoDAO;

    @Override
    public void init() {
        enderecoDAO = new EnderecoDAO();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        List<Endereco> enderecos = enderecoDAO.selecionarTodos();

        request.setAttribute("enderecos", enderecos);

        request.getRequestDispatcher(
                "/WEB-INF/views/lista-enderecos.jsp" // não temos o caminho do jsp ainda
        ).forward(request, response);
    }
}
