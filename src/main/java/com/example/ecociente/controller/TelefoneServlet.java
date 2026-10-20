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

@WebServlet(urlPatterns = {"/selectTelefone", "/adicionarTelefone", "/alterarTelefone", "/deletarTelefone"})public class TelefoneServlet extends HttpServlet {
    TelefoneDAO daoTelefones = new TelefoneDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getServletPath().equals("/selectTelefone")) {
            mostrarSelects(request, response);
        }
    }

    //Busca os telefones e envia para a JSP.
    private void mostrarSelects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Telefone> telefones = daoTelefones.listarTodos();

        request.setAttribute("telefones", telefones);
        request.getRequestDispatcher("/WEB-INF/views/lista-telefones.jsp").forward(request, response);
    }

    private void adicionarTelefone(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String numero = request.getParameter("numero");
        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));

        //Id 0 porque o banco gera.
        boolean adicionado = daoTelefones.inserir(new Telefone(0, numero, idUsuario));

        request.setAttribute("adicionado", adicionado ? "true" : "false");
    }

    private void alterarTelefone(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("idTelefone"));

        Telefone telefone = daoTelefones.buscarPorId(id);
        telefone.setNumero(request.getParameter("numero"));
        telefone.setUsuario(Integer.parseInt(request.getParameter("idUsuario")));

        boolean alterado = daoTelefones.atualizar(telefone);

        request.setAttribute("alterado", alterado ? "true" : "false");
    }

    private void deletarTelefone(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("idTelefone"));

        boolean deletado = daoTelefones.deletar(id);

        request.setAttribute("deletado", deletado ? "true" : "false");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String caminho = request.getServletPath();
        if (caminho.equals("/adicionarTelefone")) {
            adicionarTelefone(request, response);
        }
        else if (caminho.equals("/alterarTelefone")) {
            alterarTelefone(request, response);
        }
        else if (caminho.equals("/deletarTelefone")) {
            deletarTelefone(request, response);
        }
    }

}
