package com.example.ecociente.controller;

import com.example.ecociente.dao.CondominioDAO;
import com.example.ecociente.dao.SindicoDAO;
import com.example.ecociente.dao.UsuarioDAO;
import com.example.ecociente.model.Condominio;
import com.example.ecociente.model.Sindico;
import com.example.ecociente.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

//Servlet usada para as ações do CRUD de Síndicos.
@WebServlet(urlPatterns = {"/selectSindico", "/adicionarSindico", "/alterarSindico", "/deletarSindico"})
public class SindicoServlet extends HttpServlet {
    SindicoDAO daoSindicos = new SindicoDAO();
    CondominioDAO daoCondominios = new CondominioDAO();
    UsuarioDAO daoUsuarios = new UsuarioDAO();

    //Aceita CPF com pontuação (000.000.000-00) ou só números (00000000000).
    private static final String REGEX_CPF = "\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}|\\d{11}";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getServletPath().equals("/selectSindico")) {
            mostrarSelects(request, response);
        }
    }

    //Busca os síndicos, os condomínios e os usuários (para os selects) e envia para a JSP.
    private void mostrarSelects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Sindico> sindicos = daoSindicos.listarTodos();
        List<Condominio> condominios = daoCondominios.selecionarTodos();
        List<Usuario> usuarios = daoUsuarios.selecionarTodos();

        request.setAttribute("sindicos", sindicos);
        request.setAttribute("condominios", condominios);
        request.setAttribute("usuarios", usuarios);
        request.getRequestDispatcher("/WEB-INF/views/sindicos.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            String caminho = request.getServletPath();
            if (caminho.equals("/adicionarSindico")) {
                adicionarSindico(request, response);
            }
            else if (caminho.equals("/alterarSindico")) {
                alterarSindico(request, response);
            }
            else if (caminho.equals("/deletarSindico")) {
                deletarSindico(request, response);
            }
        } catch (RuntimeException e) {
            request.setAttribute("erro", "Operação não concluída. Confira os dados informados e se o registro já existe ou possui vínculos.");
            mostrarSelects(request, response);
        }
    }

    private void adicionarSindico(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cpf = request.getParameter("cpf");

        if (cpf == null || !cpf.matches(REGEX_CPF)) {
            request.setAttribute("erro", "CPF inválido. Use o formato 000.000.000-00");
            mostrarSelects(request, response);
            return;
        }

        cpf = cpf.replaceAll("\\D", ""); //banco guarda só os 11 dígitos
        LocalDate dataInicio = LocalDate.parse(request.getParameter("dataInicioMandato"));
        LocalDate dataFim = lerDataFim(request);
        int idCondominio = Integer.parseInt(request.getParameter("idCondominio"));
        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));

        //Dados do usuário vão vazios (a tabela sindico não usa). Id 0 porque o banco gera.
        boolean adicionado = daoSindicos.inserir(new Sindico(
                idUsuario, null, null, null, null, false, 0, 0,
                0, cpf, dataInicio, dataFim, idCondominio
        ));

        request.setAttribute("adicionado", adicionado ? "true" : "false");
        mostrarSelects(request, response);
    }

    private void alterarSindico(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("idSindico"));
        String cpf = request.getParameter("cpf");

        if (cpf == null || !cpf.matches(REGEX_CPF)) {
            request.setAttribute("erro", "CPF inválido. Use o formato 000.000.000-00");
            mostrarSelects(request, response);
            return;
        }

        //Busca no banco e troca só o que veio do formulário.
        Sindico sindico = daoSindicos.buscarPorId(id);
        sindico.setCpf(cpf.replaceAll("\\D", ""));
        sindico.setDataInicioMandato(LocalDate.parse(request.getParameter("dataInicioMandato")));
        sindico.setDataFimMandato(lerDataFim(request));
        sindico.setCondominio(Integer.parseInt(request.getParameter("idCondominio")));

        boolean alterado = daoSindicos.atualizar(sindico);

        request.setAttribute("alterado", alterado ? "true" : "false");
        mostrarSelects(request, response);
    }

    private void deletarSindico(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("idSindico"));

        boolean deletado = daoSindicos.deletar(id);

        request.setAttribute("deletado", deletado ? "true" : "false");
        mostrarSelects(request, response);
    }

    //Data de fim do mandato é opcional: vazia vira null.
    private LocalDate lerDataFim(HttpServletRequest request) {
        String dataFim = request.getParameter("dataFimMandato");
        if (dataFim == null || dataFim.isBlank()) {
            return null;
        }
        return LocalDate.parse(dataFim);
    }
}