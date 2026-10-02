package com.example.ecociente.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.example.ecociente.dao.SindicoDAO;
import com.example.ecociente.model.Sindico;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet(name = "SindicoServlet", value = "/sindicos")
public class SindicoServlet extends HttpServlet {

    private SindicoDAO SindicoDAO;


    @Override
    public void init() {
        SindicoDAO = new SindicoDAO();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {
        List<Sindico> sindicos = SindicoDAO.listarTodos();

        request.setAttribute("sindicos", sindicos);


        //Não sabemos o nome do caminho
        request.getRequestDispatcher(
                "WEB-INF/views/lista-sindicos.jsp"
        ).forward(request,response);
    }

}