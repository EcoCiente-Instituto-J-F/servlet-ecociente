package com.example.ecociente.controller;

import com.example.ecociente.dao.CondominioDAO;
import com.example.ecociente.dao.CooperativaDAO;
import com.example.ecociente.dao.ParceriaDAO;
import com.example.ecociente.model.Parceria;
import com.example.ecociente.util.DadoInvalido;
import com.example.ecociente.util.Formulario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

//Servlet usada para o CRUD de vínculos (parcerias entre condomínios e cooperativas). Tudo pela mesma rota.
@WebServlet(urlPatterns = {"/vinculos", "/coletas-e-vinculos"})
public class VinculoServlet extends HttpServlet {
    ParceriaDAO daoParcerias = new ParceriaDAO();
    CondominioDAO daoCondominios = new CondominioDAO();
    CooperativaDAO daoCooperativas = new CooperativaDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        mostrarSelects(request, response);
    }

    //Busca os vínculos (com pesquisa opcional), os condomínios e as cooperativas (para os selects) e os números dos cards.
    private void mostrarSelects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("vinculos", Formulario.paginar(request, daoParcerias.selecionarTodos(request.getParameter("q"))));
        request.setAttribute("condominios", daoCondominios.selecionarTodos());
        request.setAttribute("cooperativas", daoCooperativas.listarTodas());
        request.setAttribute("vinculosAtivos", daoParcerias.contarAtivas());
        request.setAttribute("semCooperativa", daoParcerias.contarCondominiosSemParceria());
        request.setAttribute("coletasMes", 0);
        request.getRequestDispatcher("/WEB-INF/views/coletas-e-vinculos.jsp").forward(request, response);
    }

    //acao=excluir apaga, id vazio cadastra e id preenchido altera.
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String acao = Formulario.texto(request, "acao");
        String id = Formulario.texto(request, "id");
        String resultado = acao.equals("excluir") ? "deletado" : id.isEmpty() ? "adicionado" : "alterado";
        request.setAttribute(resultado, "false");

        try {
            boolean ok;
            if (acao.equals("excluir")) {
                ok = daoParcerias.deletar(Formulario.id(id, "vínculo"));
            } else {
                ok = salvarVinculo(request, id);
            }
            request.setAttribute(resultado, ok ? "true" : "false");
        } catch (DadoInvalido e) {
            request.setAttribute("erro", e.getMessage());
        } catch (RuntimeException e) {
            request.setAttribute("erro", "Operação não concluída. Confira os dados informados.");
        }
        mostrarSelects(request, response);
    }

    private boolean salvarVinculo(HttpServletRequest request, String id) {
        int idCondominio = Formulario.id(Formulario.texto(request, "idCondominio"), "condomínio");
        int idCooperativa = Formulario.id(Formulario.texto(request, "idCooperativa"), "cooperativa");
        LocalDate inicio = Formulario.data(request.getParameter("inicio"), "de início");
        LocalDate fim = Formulario.data(request.getParameter("fim"), "de fim");

        if (inicio != null && fim != null && fim.isBefore(inicio)) {
            throw new DadoInvalido("A data de fim não pode ser anterior à data de início.");
        }

        //Id 0 porque o banco gera. Sem data de início, o banco usa a data de hoje.
        if (id.isEmpty()) {
            return daoParcerias.inserir(new Parceria(0, inicio, fim, idCondominio, idCooperativa));
        }
        return daoParcerias.atualizar(new Parceria(Formulario.id(id, "vínculo"), inicio, fim, idCondominio, idCooperativa));
    }
}
