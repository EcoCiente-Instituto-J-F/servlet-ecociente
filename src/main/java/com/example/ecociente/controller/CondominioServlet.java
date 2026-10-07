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

//Servlet usada para as ações do CRUD de Condomínios.
@WebServlet(urlPatterns = {"/selectCondominio", "/adicionarCondominio", "/alterarCondominio", "/deletarCondominio"})
public class CondominioServlet extends HttpServlet {
    CondominioDAO daoCondominios = new CondominioDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getServletPath().equals("/selectCondominio")) {
            mostrarSelects(request, response);
        }
    }

    //Busca os condomínios (com pesquisa por nome opcional) e envia para a JSP.
    private void mostrarSelects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String procura = request.getParameter("search");

        List<Condominio> condominios;
        if (procura != null && !procura.isBlank()) {
            condominios = daoCondominios.selecionarPorNomeCondominio(procura);
        } else {
            condominios = daoCondominios.selecionarTodos();
        }

        request.setAttribute("condominios", condominios);
        request.getRequestDispatcher("/WEB-INF/views/condominios.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String caminho = request.getServletPath();
        if (caminho.equals("/adicionarCondominio")) {
            adicionarCondominio(request, response);
        }
    }

    private void adicionarCondominio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nome = request.getParameter("nome");
        String cnpj = request.getParameter("cnpj");
        boolean status = request.getParameter("status") != null; //checkbox marcado = true
        String token = request.getParameter("token");
        int idEndereco = Integer.parseInt(request.getParameter("idEndereco"));
        int idTipoCondominio = Integer.parseInt(request.getParameter("idTipoCondominio"));

        //Id 0 porque o banco gera.
        boolean adicionado = daoCondominios.inserir(
                new Condominio(0, nome, cnpj, status, token, idEndereco, idTipoCondominio)
        );

        request.setAttribute("adicionado", adicionado ? "true" : "false");
        mostrarSelects(request, response);
    }
}