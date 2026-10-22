package com.example.ecociente.controller;

import com.example.ecociente.dao.MoradorDAO;
import com.example.ecociente.dao.TorreDAO;
import com.example.ecociente.dao.UsuarioDAO;
import com.example.ecociente.model.Morador;
import com.example.ecociente.model.Torre;
import com.example.ecociente.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

//Servlet usada para as ações do CRUD de Moradores.
@WebServlet(urlPatterns = {"/moradores", "/adicionarMorador", "/alterarMorador", "/deletarMorador"})
public class MoradorServlet extends HttpServlet {
    MoradorDAO daoMoradores = new MoradorDAO();
    UsuarioDAO daoUsuarios = new UsuarioDAO();
    TorreDAO daoTorres = new TorreDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getServletPath().equals("/moradores")) {
            mostrarSelects(request, response);
        }
    }

    //Busca os moradores, usuários e torres (para os selects) e envia para a JSP.
    private void mostrarSelects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Morador> moradores = daoMoradores.listarTodos();
        List<Usuario> usuarios = daoUsuarios.selecionarTodos();
        List<Torre> torres = daoTorres.listarTodas();

        request.setAttribute("moradores", moradores);
        request.setAttribute("usuarios", usuarios);
        request.setAttribute("torres", torres);
        request.getRequestDispatcher("/WEB-INF/views/moradores.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            String caminho = request.getServletPath();
            if (caminho.equals("/adicionarMorador")) {
                adicionarMorador(request, response);
            }
            else if (caminho.equals("/alterarMorador")) {
                alterarMorador(request, response);
            }
            else if (caminho.equals("/deletarMorador")) {
                deletarMorador(request, response);
            }
        } catch (RuntimeException e) {
            request.setAttribute("erro", "Operação não concluída. Confira os dados informados e se o registro já existe ou possui vínculos.");
            mostrarSelects(request, response);
        }
    }

    private void adicionarMorador(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String numeroApartamento = request.getParameter("numeroApartamento");
        String nivelEngajamento = request.getParameter("nivelEngajamento");
        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
        int idTorre = Integer.parseInt(request.getParameter("idTorre"));

        if (daoMoradores.buscarIdUsuario(idUsuario)) {
            request.setAttribute("erro", "Este usuário já está cadastrado como morador.");
            request.setAttribute("adicionado", "false");
            mostrarSelects(request, response);
            return;
        }

        //Dados do usuário vão vazios (a tabela morador não usa). Id 0 porque o banco gera.
        boolean adicionado = daoMoradores.inserir(new Morador(
                idUsuario, null, null, null, null, false, 0, 0,
                0, numeroApartamento, nivelEngajamento, idTorre
        ));

        request.setAttribute("adicionado", adicionado ? "true" : "false");
        mostrarSelects(request, response);
    }

    private void alterarMorador(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("idMorador"));

        //Busca no banco e troca só o que veio do formulário.
        Morador morador = daoMoradores.buscarPorId(id);
        if (morador == null) {
            request.setAttribute("alterado", "false");
            request.setAttribute("erro", "Morador não encontrado.");
            mostrarSelects(request, response);
            return;
        }
        morador.setNumeroApartamento(request.getParameter("numeroApartamento"));
        morador.setNivelEngajamento(request.getParameter("nivelEngajamento"));
        morador.setIdTorre(Integer.parseInt(request.getParameter("idTorre")));

        boolean alterado = daoMoradores.atualizar(morador);

        request.setAttribute("alterado", alterado ? "true" : "false");
        mostrarSelects(request, response);
    }

    private void deletarMorador(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("idMorador"));

        boolean deletado = daoMoradores.deletar(id);

        request.setAttribute("deletado", deletado ? "true" : "false");
        mostrarSelects(request, response);
    }
}
