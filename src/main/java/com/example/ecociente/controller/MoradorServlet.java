package com.example.ecociente.controller;

import com.example.ecociente.dao.CondominioDAO;
import com.example.ecociente.dao.MoradorDAO;
import com.example.ecociente.dao.UsuarioDAO;
import com.example.ecociente.model.Condominio;
import com.example.ecociente.model.Morador;
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
    CondominioDAO daoCondominios = new CondominioDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getServletPath().equals("/moradores")) {
            mostrarSelects(request, response);
        }
    }

    //Busca os moradores, usuários e condomínios (para os selects) e envia para a JSP.
    private void mostrarSelects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Morador> moradores = daoMoradores.listarTodos();
        List<Usuario> usuarios = daoUsuarios.selecionarTodos();
        List<Condominio> condominios = daoCondominios.selecionarTodos();

        request.setAttribute("moradores", moradores);
        request.setAttribute("usuarios", usuarios);
        request.setAttribute("condominios", condominios);
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
        String numeroApartamento = textoOpcional(request.getParameter("numeroApartamento")); //apartamento é opcional: vazio vira null
        String torre = textoOpcional(request.getParameter("torre")); //torre é opcional: vazia vira null
        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));
        Integer idCondominio = lerIdCondominio(request);

        if (daoMoradores.buscarIdUsuario(idUsuario)) {
            request.setAttribute("erro", "Este usuário já está cadastrado como morador.");
            request.setAttribute("adicionado", "false");
            mostrarSelects(request, response);
            return;
        }

        //Dados do usuário vão vazios (a tabela morador não usa). Id 0 porque o banco gera.
        boolean adicionado = daoMoradores.inserir(new Morador(
                idUsuario, null, null, null, null, false, 0, 0,
                0, numeroApartamento, torre, idCondominio
        ));

        //Quem vira morador deixa de ser "Comum". Administrador, síndico e cooperativa mantêm o tipo.
        if (adicionado) {
            Usuario usuario = daoUsuarios.selecionarUsuarioPorId(idUsuario);
            int idTipoMorador = daoUsuarios.selecionarIdTipoPorNome("Morador");
            if (usuario != null && idTipoMorador != 0 && usuario.getIdTipoUsuario() == daoUsuarios.selecionarIdTipoPorNome("Comum")) {
                usuario.setTipoUsuario(idTipoMorador);
                daoUsuarios.atualizar(usuario, null, null);
            }
        }

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
        morador.setNumeroApartamento(textoOpcional(request.getParameter("numeroApartamento")));
        morador.setTorre(textoOpcional(request.getParameter("torre")));
        morador.setIdCondominio(lerIdCondominio(request));

        boolean alterado = daoMoradores.atualizar(morador);

        request.setAttribute("alterado", alterado ? "true" : "false");
        mostrarSelects(request, response);
    }

    //Vazio vira null; preenchido volta sem os espaços das pontas.
    private String textoOpcional(String valor) {
        return (valor == null || valor.isBlank()) ? null : valor.trim();
    }

    //O condomínio é opcional: o morador pode ainda não estar ligado a nenhum.
    private Integer lerIdCondominio(HttpServletRequest request) {
        String valor = request.getParameter("idCondominio");
        return (valor == null || valor.isBlank()) ? null : Integer.valueOf(valor.trim());
    }

    private void deletarMorador(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("idMorador"));

        boolean deletado = daoMoradores.deletar(id);

        request.setAttribute("deletado", deletado ? "true" : "false");
        mostrarSelects(request, response);
    }
}
