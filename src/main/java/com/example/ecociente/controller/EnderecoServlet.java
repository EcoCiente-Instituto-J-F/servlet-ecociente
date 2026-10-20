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
}
