package com.example.ecociente.controller;

import com.example.ecociente.dao.EnderecoDAO;
import com.example.ecociente.model.Endereco;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/adicionarEndereco", "/alterarEndereco", "/deletarEndereco"})
public class EnderecoServlet extends HttpServlet {
    EnderecoDAO daoEnderecos = new EnderecoDAO();

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
}
