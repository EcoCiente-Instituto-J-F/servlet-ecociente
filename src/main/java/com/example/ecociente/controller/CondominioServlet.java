package com.example.ecociente.controller;

import com.example.ecociente.dao.CondominioDAO;
import com.example.ecociente.model.Condominio;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CondominioServlet", value = "/condominios")
public class CondominioServlet extends HttpServlet {

    private CondominioDAO CondominioDAO;


    @Override
    public void init() {
        CondominioDAO = new CondominioDAO();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        List<Condominio> condominios = CondominioDAO.selecionarTodos();

        request.setAttribute("condominios", condominios);


        //Não sabemos o nome do caminho
        request.getRequestDispatcher(
                "WEB-INF/views/lista-condominios.jsp"
        ).forward(request,response);
    }

}