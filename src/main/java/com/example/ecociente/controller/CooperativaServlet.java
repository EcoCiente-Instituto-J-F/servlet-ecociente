package com.example.ecociente.controller;

import com.example.ecociente.dao.CooperativaDAO;
import com.example.ecociente.dao.PainelDAO;
import com.example.ecociente.model.Cooperativa;
import com.example.ecociente.model.Endereco;
import com.example.ecociente.util.DadoInvalido;
import com.example.ecociente.util.FormPainel;
import com.example.ecociente.util.Senha;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

//Servlet usada para as ações do CRUD de Cooperativas.
@WebServlet(urlPatterns = {"/cooperativas", "/adicionarCooperativa", "/alterarCooperativa", "/deletarCooperativa"})
public class CooperativaServlet extends HttpServlet {
    CooperativaDAO daoCooperativas = new CooperativaDAO();
    PainelDAO painelDAO = new PainelDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getServletPath().equals("/cooperativas")) {
            mostrarSelects(request, response);
        }
    }

    //Busca as cooperativas (com pesquisa opcional) e envia para a JSP.
    private void mostrarSelects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("cooperativas", FormPainel.paginar(request, painelDAO.listarCooperativas(request.getParameter("q"))));
        request.getRequestDispatcher("/WEB-INF/views/cooperativas.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            String caminho = request.getServletPath();
            if (caminho.equals("/cooperativas")) {
                tratarPainel(request, response);
            }
            else if (caminho.equals("/adicionarCooperativa")) {
                adicionarCooperativa(request, response);
            }
            else if (caminho.equals("/alterarCooperativa")) {
                alterarCooperativa(request, response);
            }
            else if (caminho.equals("/deletarCooperativa")) {
                deletarCooperativa(request, response);
            }
        } catch (DadoInvalido e) {
            request.setAttribute("erro", e.getMessage());
            mostrarSelects(request, response);
        } catch (RuntimeException e) {
            request.setAttribute("erro", "Operação não concluída. Confira os dados informados e se o registro já existe ou possui vínculos.");
            mostrarSelects(request, response);
        }
    }

    //POST /cooperativas do painel: acao=excluir apaga, id vazio cadastra, id preenchido altera.
    private void tratarPainel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String acao = FormPainel.texto(request, "acao");
        String id = FormPainel.texto(request, "id");
        String flag = acao.equals("excluir") ? "deletado" : id.isEmpty() ? "adicionado" : "alterado";
        request.setAttribute(flag, "false");

        boolean ok;
        if (acao.equals("excluir")) {
            ok = painelDAO.excluirCooperativa(FormPainel.id(id, "cooperativa"));
        } else if (id.isEmpty()) {
            criarPainel(request);
            ok = true;
        } else {
            ok = alterarPainel(request, FormPainel.id(id, "cooperativa"));
        }
        request.setAttribute(flag, ok ? "true" : "false");
        mostrarSelects(request, response);
    }

    //Status do formulario: "Ativa"/"Inativa" (ou true/false).
    private boolean lerAtiva(HttpServletRequest request) {
        String status = FormPainel.texto(request, "status");
        return status.isEmpty() || status.equalsIgnoreCase("Ativa") || status.equalsIgnoreCase("true");
    }

    private void criarPainel(HttpServletRequest request) {
        String responsavel = FormPainel.exigir(FormPainel.texto(request, "responsavel"), "o responsável", 100);
        String cnpj = FormPainel.cnpj(request.getParameter("cnpj"));
        String email = FormPainel.email(FormPainel.texto(request, "email"));
        String senha = request.getParameter("senha");
        if (senha == null || senha.length() < 6) throw new DadoInvalido("A senha precisa ter pelo menos 6 caracteres.");
        String telefone = FormPainel.telefone(request.getParameter("telefone"));
        Endereco endereco = FormPainel.endereco(request, false);

        if (painelDAO.cnpjCooperativaEmUso(cnpj, 0)) throw new DadoInvalido("Este CNPJ já está cadastrado em outra cooperativa.");
        if (painelDAO.emailEmUso(email, 0)) throw new DadoInvalido("Este e-mail já está cadastrado.");

        Integer tipo = painelDAO.idTipoUsuarioPorNome("Cooperativa");
        if (tipo == null) throw new DadoInvalido("Tipo de usuário Cooperativa não existe na tabela tipo_usuario.");

        painelDAO.criarCooperativa(responsavel, email, Senha.gerarHash(senha), cnpj, lerAtiva(request), tipo, telefone, endereco);
    }

    private boolean alterarPainel(HttpServletRequest request, int id) {
        String responsavel = FormPainel.exigir(FormPainel.texto(request, "responsavel"), "o responsável", 100);
        String cnpj = FormPainel.cnpj(request.getParameter("cnpj"));
        String emailInformado = FormPainel.texto(request, "email");
        String email = emailInformado.isEmpty() ? null : FormPainel.email(emailInformado);
        String senha = request.getParameter("senha");
        if (senha != null && !senha.isEmpty() && senha.length() < 6) throw new DadoInvalido("A senha precisa ter pelo menos 6 caracteres.");
        String telefone = FormPainel.telefone(request.getParameter("telefone"));
        Endereco endereco = FormPainel.endereco(request, false);

        if (painelDAO.cnpjCooperativaEmUso(cnpj, id)) throw new DadoInvalido("Este CNPJ já está cadastrado em outra cooperativa.");
        if (email != null && painelDAO.emailEmUso(email, idUsuarioDaCooperativa(id))) throw new DadoInvalido("Este e-mail já está cadastrado.");

        String senhaHash = (senha == null || senha.isEmpty()) ? null : Senha.gerarHash(senha);
        return painelDAO.atualizarCooperativa(id, responsavel, cnpj, lerAtiva(request), email, senhaHash, telefone, endereco);
    }

    private int idUsuarioDaCooperativa(int idCooperativa) {
        Cooperativa cooperativa = daoCooperativas.buscarPorId(idCooperativa);
        return cooperativa == null ? 0 : cooperativa.getIdUsuario();
    }

    private void adicionarCooperativa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String cnpj = request.getParameter("cnpj").replaceAll("\\D", ""); //banco guarda só os 14 dígitos
        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));

        //Dados do usuário vão vazios (a tabela cooperativa não usa). Id 0 porque o banco gera.
        boolean adicionado = daoCooperativas.inserir(new Cooperativa(
                idUsuario, null, null, null, null, false, 0, 0,
                0, cnpj
        ));

        request.setAttribute("adicionado", adicionado ? "true" : "false");
        mostrarSelects(request, response);
    }

    private void alterarCooperativa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("idCooperativa"));

        //Busca no banco e troca só o CNPJ que veio do formulário.
        Cooperativa cooperativa = daoCooperativas.buscarPorId(id);
        cooperativa.setCnpj(request.getParameter("cnpj").replaceAll("\\D", ""));

        boolean alterado = daoCooperativas.atualizar(cooperativa);

        request.setAttribute("alterado", alterado ? "true" : "false");
        mostrarSelects(request, response);
    }

    private void deletarCooperativa(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("idCooperativa"));

        boolean deletado = daoCooperativas.deletar(id);

        request.setAttribute("deletado", deletado ? "true" : "false");
        mostrarSelects(request, response);
    }
}