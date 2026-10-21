package com.example.ecociente.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

//Bloqueia acesso as telas do painel administrativo sem login.
@WebFilter(urlPatterns = {"/painel", "/usuarios", "/condominios", "/cooperativas", "/coletas-e-vinculos"})
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

        chain.doFilter(request, response); //esta logado, pode passar
    }
}