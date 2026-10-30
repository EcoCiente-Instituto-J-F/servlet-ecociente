package com.example.ecociente.controller;

import com.example.ecociente.dao.CondominioDAO;
import com.example.ecociente.dao.MoradorDAO;
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
    MoradorDAO daoMoradores = new MoradorDAO();

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

        String problema = validarMandato(dataInicio, dataFim);
        if (problema == null && daoUsuarios.existeCpf(cpf, idUsuario)) problema = "Este CPF já está cadastrado em outro usuário.";
        if (problema == null && daoSindicos.buscarIdUsuario(idUsuario)) problema = "Este usuário já é síndico.";
        if (problema == null && daoSindicos.buscarPorCondominio(idCondominio) != null) problema = "Este condomínio já tem síndico.";
        if (problema != null) {
            request.setAttribute("erro", problema);
            request.setAttribute("adicionado", "false");
            mostrarSelects(request, response);
            return;
        }

        //O CPF fica no cadastro do usuário, que passa a ser do tipo Síndico.
        Usuario usuario = daoUsuarios.selecionarUsuarioPorId(idUsuario);
        usuario.setCpf(cpf);
        int idTipoSindico = daoUsuarios.selecionarIdTipoPorNome("Sindico");
        if (idTipoSindico != 0) usuario.setTipoUsuario(idTipoSindico);
        daoUsuarios.atualizar(usuario, null, null);

        //Dados do usuário vão vazios (a tabela sindico não usa). Id 0 porque o banco gera.
        boolean adicionado = daoSindicos.inserir(new Sindico(
                idUsuario, null, null, null, null, false, 0, 0,
                0, dataInicio, dataFim, idCondominio
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
        if (sindico == null) {
            request.setAttribute("erro", "Síndico não encontrado.");
            request.setAttribute("alterado", "false");
            mostrarSelects(request, response);
            return;
        }

        String cpfDigitos = cpf.replaceAll("\\D", "");
        LocalDate dataInicio = LocalDate.parse(request.getParameter("dataInicioMandato"));
        LocalDate dataFim = lerDataFim(request);

        String problema = validarMandato(dataInicio, dataFim);
        if (problema == null && daoUsuarios.existeCpf(cpfDigitos, sindico.getIdUsuario())) {
            problema = "Este CPF já está cadastrado em outro usuário.";
        }
        Sindico doCondominio = daoSindicos.buscarPorCondominio(Integer.parseInt(request.getParameter("idCondominio")));
        if (problema == null && doCondominio != null && doCondominio.getIdSindico() != id) problema = "Este condomínio já tem síndico.";
        if (problema != null) {
            request.setAttribute("erro", problema);
            request.setAttribute("alterado", "false");
            mostrarSelects(request, response);
            return;
        }

        //O CPF fica no cadastro do usuário.
        Usuario usuario = daoUsuarios.selecionarUsuarioPorId(sindico.getIdUsuario());
        usuario.setCpf(cpfDigitos);
        daoUsuarios.atualizar(usuario, null, null);

        sindico.setDataInicioMandato(dataInicio);
        sindico.setDataFimMandato(dataFim);
        sindico.setCondominio(Integer.parseInt(request.getParameter("idCondominio")));

        boolean alterado = daoSindicos.atualizar(sindico);

        request.setAttribute("alterado", alterado ? "true" : "false");
        mostrarSelects(request, response);
    }

    private void deletarSindico(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("idSindico"));

        Sindico sindico = daoSindicos.buscarPorId(id);
        boolean deletado = daoSindicos.deletar(id);
        if (deletado && sindico != null) acertarTipoAposExcluir(sindico.getIdUsuario());

        request.setAttribute("deletado", deletado ? "true" : "false");
        mostrarSelects(request, response);
    }

    //Quem deixa de ser síndico volta a ser Morador (se ainda mora num condomínio) ou Comum. Outros tipos ficam como estão.
    private void acertarTipoAposExcluir(int idUsuario) {
        Usuario usuario = daoUsuarios.selecionarUsuarioPorId(idUsuario);
        if (usuario == null || usuario.getIdTipoUsuario() != daoUsuarios.selecionarIdTipoPorNome("Sindico")) return;

        int idTipo = daoUsuarios.selecionarIdTipoPorNome(daoMoradores.buscarIdUsuario(idUsuario) ? "Morador" : "Comum");
        if (idTipo == 0) return;
        usuario.setTipoUsuario(idTipo);
        daoUsuarios.atualizar(usuario, null, null);
    }

    private String validarMandato(LocalDate inicio, LocalDate fim) {
        if (fim != null && fim.isBefore(inicio)) return "O fim do mandato não pode ser anterior ao início.";
        return null;
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