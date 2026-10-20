package com.example.ecociente.controller;

import com.example.ecociente.dao.CondominioDAO;
import com.example.ecociente.dao.EnderecoDAO;
import com.example.ecociente.dao.UsuarioDAO;
import com.example.ecociente.model.Condominio;
import com.example.ecociente.model.Endereco;
import com.example.ecociente.model.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/selectEndereco", "/adicionarEndereco", "/alterarEndereco", "/deletarEndereco"})
public class EnderecoServlet extends HttpServlet {
    EnderecoDAO daoEnderecos = new EnderecoDAO();
    UsuarioDAO daoUsuarios = new UsuarioDAO();
    CondominioDAO daoCondominios = new CondominioDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getServletPath().equals("/selectEndereco")) {
            mostrarSelects(request, response);
        }
    }

    //Busca os enderecos e envia para a JSP.
    private void mostrarSelects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Endereco> enderecos = daoEnderecos.selecionarTodos();

        request.setAttribute("enderecos", enderecos);
        request.getRequestDispatcher("/WEB-INF/views/lista-enderecos.jsp").forward(request, response);

    }

    //Liga o endereco recem-criado ao dono certo (usuario ou condominio).
    private boolean vincularEndereco(String tipoDono, int idDono, int idEndereco) {
        if ("condominio".equals(tipoDono)) {
            Condominio condominio = daoCondominios.selecionarPorId(idDono);
            if (condominio == null) return false;
            condominio.setIdEndereco(idEndereco);
            return daoCondominios.atualizarCondominio(condominio);
        }
        // cobre usuario, morador, sindico e cooperativa -- todos herdam id_endereco de usuario
        Usuario usuario = daoUsuarios.selecionarUsuarioPorId(idDono);

        if (usuario == null) return false;
        usuario.setEndereco(idEndereco);
        return daoUsuarios.atualizar(usuario);
    }

    private void adicionarEndereco(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cep = request.getParameter("cep");
        String cidade = request.getParameter("cidade");
        String estado = request.getParameter("estado");
        String bairro = request.getParameter("bairro");
        String rua = request.getParameter("rua");
        int numero = Integer.parseInt(request.getParameter("numero"));
        String complemento = request.getParameter("complemento");

        //Id 0 porque o banco gera.
        boolean adicionado = daoEnderecos.inserir(
                new Endereco(0, cep, cidade, estado, bairro, rua, numero, complemento)
        );

        request.setAttribute("adicionado", adicionado ? "true" : "false");
    }

    private void alterarEndereco(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("idEndereco"));

        Endereco endereco = daoEnderecos.selecionarPorId(id);
        endereco.setCep(request.getParameter("cep"));
        endereco.setCidade(request.getParameter("cidade"));
        endereco.setEstado(request.getParameter("estado"));
        endereco.setBairro(request.getParameter("bairro"));
        endereco.setRua(request.getParameter("rua"));
        endereco.setNumero(Integer.parseInt(request.getParameter("numero")));
        endereco.setComplemento(request.getParameter("complemento"));

        boolean alterado = daoEnderecos.atualizar(endereco);

        request.setAttribute("alterado", alterado ? "true" : "false");
    }

    private void deletarEndereco(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("idEndereco"));

        Endereco endereco = daoEnderecos.selecionarPorId(id);
        boolean deletado = daoEnderecos.deletar(endereco);

        request.setAttribute("deletado", deletado ? "true" : "false");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String caminho = request.getServletPath();
        if (caminho.equals("/adicionarEndereco")) {
            adicionarEndereco(request, response);
        }
        else if (caminho.equals("/alterarEndereco")) {
            alterarEndereco(request, response);
        }
        else if (caminho.equals("/deletarEndereco")) {
            deletarEndereco(request, response);
        }
    }
}
