package com.example.ecociente.controller;

import com.example.ecociente.dao.CondominioDAO;
import com.example.ecociente.dao.PainelDAO;
import com.example.ecociente.model.Condominio;
import com.example.ecociente.model.Endereco;
import com.example.ecociente.util.DadoInvalido;
import com.example.ecociente.util.FormPainel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

//Servlet usada para as ações do CRUD de Condomínios.
@WebServlet(urlPatterns = {"/condominios", "/adicionarCondominio", "/alterarCondominio", "/deletarCondominio"})
public class CondominioServlet extends HttpServlet {
    CondominioDAO daoCondominios = new CondominioDAO();
    PainelDAO painelDAO = new PainelDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getServletPath().equals("/condominios")) {
            mostrarSelects(request, response);
        }
    }

    //Busca os condomínios (com pesquisa por nome opcional) e os tipos (para o select) e envia para a JSP.
    private void mostrarSelects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String procura = request.getParameter("q");
        if (procura == null) procura = request.getParameter("search");

        request.setAttribute("condominios", FormPainel.paginar(request, painelDAO.listarCondominios(procura)));
        request.setAttribute("tiposCondominio", painelDAO.listarTiposCondominio());
        request.getRequestDispatcher("/WEB-INF/views/condominios.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            String caminho = request.getServletPath();
            if (caminho.equals("/condominios")) {
                tratarPainel(request, response);
            }
            else if (caminho.equals("/adicionarCondominio")) {
                adicionarCondominio(request, response);
            }
            else if (caminho.equals("/alterarCondominio")) {
                alterarCondominio(request, response);
            }
            else if (caminho.equals("/deletarCondominio")) {
                deletarCondominio(request, response);
            }
        } catch (DadoInvalido e) {
            request.setAttribute("erro", e.getMessage());
            mostrarSelects(request, response);
        } catch (RuntimeException e) {
            request.setAttribute("erro", "Operação não concluída. Confira os dados informados e se o registro já existe ou possui vínculos.");
            mostrarSelects(request, response);
        }
    }

    //POST /condominios do painel: acao=excluir apaga, id vazio cadastra, id preenchido altera.
    private void tratarPainel(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String acao = FormPainel.texto(request, "acao");
        String id = FormPainel.texto(request, "id");
        String flag = acao.equals("excluir") ? "deletado" : id.isEmpty() ? "adicionado" : "alterado";
        request.setAttribute(flag, "false");

        boolean ok;
        if (acao.equals("excluir")) {
            ok = excluirPainel(FormPainel.id(id, "condomínio"));
        } else if (id.isEmpty()) {
            criarPainel(request);
            ok = true;
        } else {
            ok = alterarPainel(request, FormPainel.id(id, "condomínio"));
        }
        request.setAttribute(flag, ok ? "true" : "false");
        mostrarSelects(request, response);
    }

    //Tipo vem como id (idTipoCondominio) ou como nome (tipo), igual ao que esta em tipo_condominio.
    private int lerTipo(HttpServletRequest request) {
        String idTipo = FormPainel.texto(request, "idTipoCondominio");
        if (!idTipo.isEmpty()) return FormPainel.id(idTipo, "tipo de condomínio");

        String nome = FormPainel.texto(request, "tipo");
        Integer porNome = nome.isEmpty() ? null : painelDAO.idTipoCondominioPorNome(nome);
        if (porNome == null) throw new DadoInvalido("Tipo de condomínio inválido.");
        return porNome;
    }

    private void criarPainel(HttpServletRequest request) {
        String nome = FormPainel.exigir(FormPainel.texto(request, "nome"), "o nome", 100);
        String cnpj = FormPainel.cnpj(request.getParameter("cnpj"));
        int tipo = lerTipo(request);
        Endereco endereco = FormPainel.endereco(request, true);

        if (painelDAO.cnpjCondominioEmUso(cnpj, 0)) throw new DadoInvalido("Este CNPJ já está cadastrado em outro condomínio.");

        painelDAO.criarCondominio(nome, cnpj, tipo, endereco);
    }

    private boolean alterarPainel(HttpServletRequest request, int id) {
        String nome = FormPainel.exigir(FormPainel.texto(request, "nome"), "o nome", 100);
        String cnpj = FormPainel.cnpj(request.getParameter("cnpj"));
        int tipo = lerTipo(request);
        Endereco endereco = FormPainel.endereco(request, true);

        if (painelDAO.cnpjCondominioEmUso(cnpj, id)) throw new DadoInvalido("Este CNPJ já está cadastrado em outro condomínio.");

        return painelDAO.atualizarCondominio(id, nome, cnpj, tipo, endereco);
    }

    private boolean excluirPainel(int id) {
        try {
            return painelDAO.excluirCondominio(id);
        } catch (RuntimeException e) {
            throw new DadoInvalido("Não foi possível excluir: o condomínio ainda tem torres, síndico ou parcerias vinculados.");
        }
    }

    private void adicionarCondominio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nome = request.getParameter("nome");
        String cnpj = request.getParameter("cnpj").replaceAll("\\D", ""); //banco guarda só os 14 dígitos
        boolean status = request.getParameter("status") != null; //checkbox marcado = true
        String token = request.getParameter("token");
        int idEndereco = Integer.parseInt(request.getParameter("idEndereco"));
        int idTipoCondominio = Integer.parseInt(request.getParameter("idTipoCondominio"));

        //Id 0 porque o banco gera.
        boolean adicionado = daoCondominios.inserir(
                new Condominio(0, nome, cnpj, status, token, idEndereco, idTipoCondominio)
        );

        request.setAttribute("adicionado", adicionado ? "true" : "false");
        mostrarSelects(request, response);
    }

    private void alterarCondominio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("idCondominio"));

        //Busca no banco e troca só o que veio do formulário.
        Condominio condominio = daoCondominios.selecionarPorId(id);
        condominio.setNome(request.getParameter("nome"));
        condominio.setCnpj(request.getParameter("cnpj").replaceAll("\\D", ""));
        condominio.setStatus(request.getParameter("status") != null);
        condominio.setIdEndereco(Integer.parseInt(request.getParameter("idEndereco")));
        condominio.setIdTipoCondominio(Integer.parseInt(request.getParameter("idTipoCondominio")));

        boolean alterado = daoCondominios.atualizarCondominio(condominio);

        request.setAttribute("alterado", alterado ? "true" : "false");
        mostrarSelects(request, response);
    }

    private void deletarCondominio(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("idCondominio"));

        boolean deletado = daoCondominios.deletar(id);

        request.setAttribute("deletado", deletado ? "true" : "false");
        mostrarSelects(request, response);
    }
}