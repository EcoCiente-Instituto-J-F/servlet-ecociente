package com.example.ecociente.controller;

import com.example.ecociente.dao.CooperativaDAO;
import com.example.ecociente.model.Cooperativa;
import com.example.ecociente.model.Sindico;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;


    @WebServlet(name = "CooperativaServlet", value = "/cooperativas")
    public class CooperativaServlet extends HttpServlet {

        private CooperativaDAO CooperativaDAO;


        @Override
        public void init() {
            CooperativaDAO = new CooperativaDAO();
        }

        @Override
        protected void doGet(
                HttpServletRequest request,
                HttpServletResponse response
        ) throws ServletException, IOException {
            List<Cooperativa> cooperativa = CooperativaDAO.listarTodas();

            request.setAttribute("cooperativas", cooperativa);


            //Não sabemos o nome do caminho
            request.getRequestDispatcher(
                    "WEB-INF/views/lista-cooperativas.jsp"
            ).forward(request,response);
        }

    }

