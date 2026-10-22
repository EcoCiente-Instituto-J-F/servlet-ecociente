package com.example.ecociente.controller;

import com.example.ecociente.dao.PainelDAO;
import com.example.ecociente.util.DadoInvalido;
import com.example.ecociente.util.FormPainel;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

//Servlet usada para o CRUD de vinculos (parcerias entre condominios e cooperativas) do painel.
@WebServlet(urlPatterns = {"/vinculos", "/coletas-e-vinculos"})
public class VinculoServlet extends HttpServlet {
    PainelDAO painelDAO = new PainelDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        mostrarVinculos(request, response);
    }

    //Busca os vinculos (com pesquisa opcional) e o resumo dos cards e envia para a JSP.
    private void mostrarVinculos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("vinculos", FormPainel.paginar(request, painelDAO.listarVinculos(request.getParameter("q"))));
        for (Map.Entry<String, Object> dado : painelDAO.resumoVinculos().entrySet()) {
            request.setAttribute(dado.getKey(), dado.getValue());
        }
        request.getRequestDispatcher("/WEB-INF/views/coletas-e-vinculos.jsp").forward(request, response);
    }

    //POST do painel: acao=excluir apaga, id vazio cadastra, id preenchido altera.
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String acao = FormPainel.texto(request, "acao");
        String id = FormPainel.texto(request, "id");
        String flag = acao.equals("excluir") ? "deletado" : id.isEmpty() ? "adicionado" : "alterado";
        request.setAttribute(flag, "false");

        try {
            boolean ok;
            if (acao.equals("excluir")) {
                ok = painelDAO.excluirVinculo(FormPainel.id(id, "vínculo"));
            } else {
                int idCondominio = resolverCondominio(request);
                int idCooperativa = resolverCooperativa(request);
                LocalDate inicio = FormPainel.data(request.getParameter("inicio"), "de início");
                LocalDate fim = FormPainel.data(request.getParameter("fim"), "de fim");
                if (inicio != null && fim != null && fim.isBefore(inicio)) {
                    throw new DadoInvalido("A data de fim não pode ser anterior à data de início.");
                }

                if (id.isEmpty()) {
                    painelDAO.criarVinculo(idCondominio, idCooperativa, inicio, fim);
                    ok = true;
                } else {
                    ok = painelDAO.atualizarVinculo(FormPainel.id(id, "vínculo"), idCondominio, idCooperativa, inicio, fim);
                }
            }
            request.setAttribute(flag, ok ? "true" : "false");
        } catch (DadoInvalido e) {
            request.setAttribute("erro", e.getMessage());
        } catch (RuntimeException e) {
            request.setAttribute("erro", "Operação não concluída. Confira os dados informados.");
        }
        mostrarVinculos(request, response);
    }

    //Aceita idCondominio (select) ou o nome digitado no campo condominio.
    private int resolverCondominio(HttpServletRequest request) {
        String id = FormPainel.texto(request, "idCondominio");
        if (!id.isEmpty()) return FormPainel.id(id, "condomínio");

        String nome = FormPainel.texto(request, "condominio");
        Integer porNome = nome.isEmpty() ? null : painelDAO.idCondominioPorNome(nome);
        if (porNome == null) throw new DadoInvalido("Condomínio não encontrado. Informe o nome exatamente como está cadastrado.");
        return porNome;
    }

    //Aceita idCooperativa (select) ou o nome digitado no campo cooperativa.
    private int resolverCooperativa(HttpServletRequest request) {
        String id = FormPainel.texto(request, "idCooperativa");
        if (!id.isEmpty()) return FormPainel.id(id, "cooperativa");

        String nome = FormPainel.texto(request, "cooperativa");
        Integer porNome = nome.isEmpty() ? null : painelDAO.idCooperativaPorNome(nome);
        if (porNome == null) throw new DadoInvalido("Cooperativa não encontrada. Informe o nome exatamente como está cadastrado.");
        return porNome;
    }
}
