package com.example.ecociente.filter;

import com.example.ecociente.controller.LoginServlet;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

//Bloqueia as telas e os CRUDs do painel administrativo: exige login e que o usuario seja administrador.
@WebFilter(urlPatterns = {
        "/painel", "/painel/conteudo", "/usuarios", "/condominios", "/cooperativas", "/vinculos", "/coletas-e-vinculos",
        "/moradores", "/adicionarMorador", "/alterarMorador", "/deletarMorador",
        "/selectSindico", "/adicionarSindico", "/alterarSindico", "/deletarSindico",
        "/selectEndereco", "/adicionarEndereco", "/alterarEndereco", "/deletarEndereco",
        "/selectTelefone", "/adicionarTelefone", "/alterarTelefone", "/deletarTelefone"
})
public class AutenticacaoFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession sessao = req.getSession(false); //false = nao cria sessao nova so para checar

        if (sessao == null || sessao.getAttribute("idUsuarioLogado") == null) {
            resp.sendRedirect(req.getContextPath() + "/login-crud");
            return; //interrompe -- nao deixa a requisicao seguir pro servlet de destino
        }

        //logado como usuario comum nao entra no painel
        Object tipo = sessao.getAttribute("tipoUsuarioLogado");
        if (tipo == null || (Integer) tipo != LoginServlet.TIPO_ADMINISTRADOR) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso restrito a administradores.");
            return;
        }

        chain.doFilter(request, response); //e administrador, pode passar
    }
}
