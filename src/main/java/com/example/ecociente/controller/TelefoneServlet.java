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

@WebServlet(urlPatterns = {"/adicionarTelefone", "/alterarTelefone", "/deletarTelefone"})
public class TelefoneServlet extends HttpServlet {
    TelefoneDAO daoTelefones = new TelefoneDAO();

    private void adicionarTelefone(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String numero = request.getParameter("numero");
        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));

        //Id 0 porque o banco gera.
        boolean adicionado = daoTelefones.inserir(new Telefone(0, numero, idUsuario));

        request.setAttribute("adicionado", adicionado ? "true" : "false");
    }

}
