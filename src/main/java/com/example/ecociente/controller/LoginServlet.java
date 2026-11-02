package com.example.ecociente.controller;

import com.example.ecociente.dao.CondominioDAO;
import com.example.ecociente.dao.SindicoDAO;
import com.example.ecociente.dao.UsuarioDAO;
import com.example.ecociente.model.CadastroPendente;
import com.example.ecociente.model.Condominio;
import com.example.ecociente.model.Orcamento;
import com.example.ecociente.model.Sindico;
import com.example.ecociente.model.TipoCondominio;
import com.example.ecociente.model.Usuario;
import com.example.ecociente.util.Senha;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

// Servlet dos logins do sistema (comum em /login e painel administrativo em /login-crud) e da saída (/logout)
@WebServlet(urlPatterns = {"/login", "/login-crud", "/logout"})
public class LoginServlet extends HttpServlet {

    //================ ATRIBUTOS ================

    // Id 5 = Administrador. Só a tela do painel exige esse tipo
    public static final int TIPO_ADMINISTRADOR = 5;

    UsuarioDAO daoUsuarios = new UsuarioDAO();

    //================ METODOS GET ================

    // /logout encerra a sessão; nos outros caminhos abre a tela de login (admin já logado vai direto ao painel)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getServletPath().equals("/logout")) {
            HttpSession sessao = request.getSession(false);
            if (sessao != null) sessao.invalidate();
            response.sendRedirect(request.getContextPath() + "/inicio");
            return;
        }

        // Admin já logado não precisa ver o login de novo
        if (request.getServletPath().equals("/login-crud")) {
            HttpSession sessao = request.getSession(false);
            if (sessao != null && sessao.getAttribute("idUsuarioLogado") != null
                    && Integer.valueOf(TIPO_ADMINISTRADOR).equals(sessao.getAttribute("tipoUsuarioLogado"))) {
                response.sendRedirect(request.getContextPath() + "/painel");
                return;
            }
        }
        mostrarTela(request, response);
    }

    //================ METODOS POST ================

    // Confere e-mail e senha, aplica as regras de cada tela de login e abre a sessão do usuário
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String email = request.getParameter("email");
        String senha = request.getParameter("senha");
        boolean telaAdmin = request.getServletPath().equals("/login-crud");

        Usuario usuario = (email == null || email.isBlank()) ? null : daoUsuarios.selecionarPorEmail(email.trim());

        if (usuario == null || !senhaConfere(senha, usuario.getSenhaHash())) {
            request.setAttribute("erroLogin", "E-mail ou senha incorretos.");
            mostrarTela(request, response);
            return;
        }

        if (telaAdmin && usuario.getIdTipoUsuario() != TIPO_ADMINISTRADOR) {
            request.setAttribute("erroLogin", "Esse login e exclusivo para administradores.");
            mostrarTela(request, response);
            return;
        }

        if (!usuario.isStatus()) {
            request.setAttribute("erroLogin", "Conta desativada.");
            mostrarTela(request, response);
            return;
        }

        // Só o síndico usa o login comum (para assinar o plano). Morador, comum e cooperativa não precisam entrar.
        int idSindico = daoUsuarios.selecionarIdTipoPorNome("Sindico");
        if (!telaAdmin && idSindico != 0
                && usuario.getIdTipoUsuario() != idSindico
                && usuario.getIdTipoUsuario() != TIPO_ADMINISTRADOR) {
            request.setAttribute("erroLogin", "Você não precisa fazer login. O login é só para síndicos que vão assinar um plano.");
            mostrarTela(request, response);
            return;
        }

        // "Fazer assinatura" (proximo=plano): só o síndico assina, pelo condomínio dele
        CadastroPendente assinatura = null;
        if (!telaAdmin && "plano".equals(request.getParameter("proximo"))) {
            assinatura = assinaturaDoSindico(usuario);
            if (assinatura == null) {
                request.setAttribute("erroLogin", "A assinatura é feita pela conta do síndico do condomínio.");
                mostrarTela(request, response);
                return;
            }
            if (!Orcamento.podeAssinar(assinatura.getPlano(), assinatura.getQuantidade())) {
                request.setAttribute("erroLogin", "A assinatura é para condomínios com " + Orcamento.UNIDADES_MINIMAS + " unidades ou mais.");
                mostrarTela(request, response);
                return;
            }
        }

        // Sessão nova a cada login (evita aproveitar uma sessão antiga)
        HttpSession antiga = request.getSession(false);
        if (antiga != null) antiga.invalidate();
        HttpSession sessao = request.getSession(true);
        sessao.setAttribute("idUsuarioLogado", usuario.getIdUsuario());
        sessao.setAttribute("nomeUsuarioLogado", usuario.getNome());
        sessao.setAttribute("iniciaisUsuarioLogado", iniciais(usuario.getNome()));
        sessao.setAttribute("tipoUsuarioLogado", usuario.getIdTipoUsuario());

        if (assinatura != null) {
            sessao.setAttribute("cadastroPendente", assinatura);
            response.sendRedirect(request.getContextPath() + "/cadastro/plano");
            return;
        }

        response.sendRedirect(request.getContextPath() + (telaAdmin ? "/painel" : "/inicio"));
    }

    //================ METODOS AUXILIARES ================

    // Monta o que as telas de pagamento usam (o mesmo que o UsuarioServlet guarda ao criar a conta do síndico).
    // Null se o usuário não for síndico.
    private CadastroPendente assinaturaDoSindico(Usuario usuario) {
        Sindico sindico = new SindicoDAO().buscarPorUsuario(usuario.getIdUsuario());
        if (sindico == null) return null;
        CondominioDAO condominioDAO = new CondominioDAO();
        Condominio condominio = condominioDAO.selecionarPorId(sindico.getIdCondominio());
        if (condominio == null) return null;

        // O código do plano vem do nome do tipo do condomínio (residencial, comercial, residencial/comercial ou industrial)
        String plano = null;
        for (TipoCondominio tipo : condominioDAO.selecionarTipos()) {
            if (tipo.getId() == condominio.getIdTipoCondominio()) plano = tipo.getNome().trim().toLowerCase().replace("/", "-").replace(" ", "");
        }


        CadastroPendente cad = new CadastroPendente();
        cad.setNome(usuario.getNome());
        cad.setEmail(usuario.getEmail());
        cad.setCpf(sindico.getCpf());
        cad.setDocumento(sindico.getCpf());
        cad.setContaCriada(true);
        cad.setOrigemLogin(true);
        cad.setIdUsuario(usuario.getIdUsuario());
        cad.setIdCondominio(condominio.getIdCondominio());
        cad.setPlano(plano);
        cad.setQuantidade(Orcamento.quantidadeDoCondominio(condominio));
        cad.setCiclo("mensal");
        return cad;
    }

    // Hash inválido no banco não pode derrubar o login
    private boolean senhaConfere(String senha, String hash) {
        if (senha == null || hash == null) return false;
        try {
            return Senha.conferir(senha, hash);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    // Iniciais do nome, mostradas no avatar do painel
    private String iniciais(String nome) {
        if (nome == null || nome.isBlank()) return "?";
        String[] partes = nome.trim().split("\\s+");
        String ultima = partes.length > 1 ? partes[partes.length - 1].substring(0, 1) : "";
        return (partes[0].substring(0, 1) + ultima).toUpperCase();
    }

    // Abre a tela de login do painel ou a do login comum, conforme o caminho
    private void mostrarTela(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pagina = request.getServletPath().equals("/login-crud")
                ? "/WEB-INF/views/login-crud.jsp"
                : "/WEB-INF/views/cadastro_login.jsp";
        request.getRequestDispatcher(pagina).forward(request, response);
    }
}
