package com.example.ecociente.controller;

import com.example.ecociente.dao.TelefoneDAO;
import com.example.ecociente.dao.UsuarioDAO;
import com.example.ecociente.model.Telefone;
import com.example.ecociente.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/selectTelefone", "/adicionarTelefone", "/alterarTelefone", "/deletarTelefone"})
public class TelefoneServlet extends HttpServlet {
    TelefoneDAO daoTelefones = new TelefoneDAO();
    UsuarioDAO daoUsuarios = new UsuarioDAO();

    //Aceita celular/fixo com DDD, com ou sem traço: (11) 91234-5678 ou (11) 1234-5678.
    private static final String REGEX_CELULAR = "\\(\\d{2}\\)\\s?\\d{4,5}-?\\d{4}";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getServletPath().equals("/selectTelefone")) {
            mostrarSelects(request, response);
        }
    }

    //Busca os telefones e os usuários (para o select) e envia para a JSP.
    private void mostrarSelects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Telefone> telefones = daoTelefones.listarTodos();
        List<Usuario> usuarios = daoUsuarios.selecionarTodos();

        request.setAttribute("telefones", telefones);
        request.setAttribute("usuarios", usuarios);
        request.getRequestDispatcher("/WEB-INF/views/telefones.jsp").forward(request, response);
    }

    private void adicionarTelefone(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String numero = request.getParameter("numero");
        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));

        if (numero == null || !numero.matches(REGEX_CELULAR)) {
            request.setAttribute("erro", "Celular inválido. Use o formato (11) 91234-5678");
            mostrarSelects(request, response);
            return;
        }

        //Id 0 porque o banco gera. Banco guarda só os dígitos (9 a 11).
        boolean adicionado = daoTelefones.inserir(new Telefone(0, numero.replaceAll("\\D", ""), idUsuario));

        request.setAttribute("adicionado", adicionado ? "true" : "false");
        mostrarSelects(request, response);
    }

    private void alterarTelefone(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("idTelefone"));
        String numero = request.getParameter("numero");

        if (numero == null || !numero.matches(REGEX_CELULAR)) {
            request.setAttribute("erro", "Celular inválido. Use o formato (11) 91234-5678");
            mostrarSelects(request, response);
            return;
        }

        Telefone telefone = daoTelefones.buscarPorId(id);
        telefone.setNumero(numero.replaceAll("\\D", ""));
        telefone.setUsuario(Integer.parseInt(request.getParameter("idUsuario")));

        boolean alterado = daoTelefones.atualizar(telefone);

        request.setAttribute("alterado", alterado ? "true" : "false");
        mostrarSelects(request, response);
    }

    private void deletarTelefone(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("idTelefone"));

        boolean deletado = daoTelefones.deletar(id);

        request.setAttribute("deletado", deletado ? "true" : "false");
        mostrarSelects(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
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
        } catch (RuntimeException e) {
            request.setAttribute("erro", "Operação não concluída. Confira os dados informados e se o registro já existe ou possui vínculos.");
            mostrarSelects(request, response);
        }
    }
}