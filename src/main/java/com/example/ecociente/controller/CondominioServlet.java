package com.example.ecociente.controller;

import com.example.ecociente.dao.CondominioDAO;
import com.example.ecociente.model.Condominio;
import com.example.ecociente.model.Endereco;
import com.example.ecociente.util.DadoInvalido;
import com.example.ecociente.util.Formulario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

//Servlet usada para as ações do CRUD de Condomínios. Tudo pela mesma rota: GET lista, POST cadastra, altera ou exclui.
@WebServlet(urlPatterns = {"/condominios"})
public class CondominioServlet extends HttpServlet {
    CondominioDAO daoCondominios = new CondominioDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        mostrarSelects(request, response);
    }

    //Busca os condomínios (com pesquisa por nome opcional) e os tipos (para o select) e envia para a JSP.
    private void mostrarSelects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String procura = request.getParameter("q");
        if (procura == null) procura = request.getParameter("search");

        request.setAttribute("condominios", Formulario.paginar(request, daoCondominios.selecionarTodos(procura)));
        request.setAttribute("tiposCondominio", daoCondominios.selecionarTipos());
        request.getRequestDispatcher("/WEB-INF/views/condominios.jsp").forward(request, response);
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
                ok = deletarCondominio(Formulario.id(id, "condomínio"));
            } else if (id.isEmpty()) {
                ok = adicionarCondominio(request);
            } else {
                ok = alterarCondominio(request, Formulario.id(id, "condomínio"));
            }
            request.setAttribute(resultado, ok ? "true" : "false");
        } catch (DadoInvalido e) {
            request.setAttribute("erro", e.getMessage());
        } catch (RuntimeException e) {
            request.setAttribute("erro", "Operação não concluída. Confira os dados informados e se o registro possui vínculos.");
        }
        mostrarSelects(request, response);
    }

    private boolean adicionarCondominio(HttpServletRequest request) {
        String nome = Formulario.exigir(Formulario.texto(request, "nome"), "o nome", 100);
        String cnpj = Formulario.cnpj(request.getParameter("cnpj"));
        int idTipoCondominio = Formulario.id(Formulario.texto(request, "idTipoCondominio"), "tipo de condomínio");
        Endereco endereco = Formulario.endereco(request, true);
        Integer unidades = Formulario.inteiroPositivoOpcional(request.getParameter("numeroUnidades"), "o número de unidades");
        BigDecimal area = Formulario.decimalPositivoOpcional(request.getParameter("areaM2"), "a área");

        //Industrial exige a área em m²; os outros tipos exigem o número de unidades. Nunca os dois.
        boolean industrial = daoCondominios.selecionarTipos().stream()
                .anyMatch(t -> t.getId() == idTipoCondominio && t.getNome().equalsIgnoreCase("Industrial"));
        if (industrial ? (area == null || unidades != null) : (unidades == null || area != null)) {
            throw new DadoInvalido(industrial
                    ? "Condomínio industrial exige a área em m² e não tem número de unidades."
                    : "Informe o número de unidades; a área em m² é só para condomínio industrial.");
        }

        if (daoCondominios.existeCnpj(cnpj, 0)) {
            throw new DadoInvalido("Este CNPJ já está cadastrado em outro condomínio.");
        }

        //Id 0 porque o banco gera. O endereço é criado junto, na mesma transação.
        Condominio condominio = new Condominio(0, nome, cnpj, true, null, 0, idTipoCondominio);
        condominio.setNumeroUnidades(unidades);
        condominio.setAreaM2(area);
        return daoCondominios.inserir(condominio, endereco);
    }

    private boolean alterarCondominio(HttpServletRequest request, int id) {
        String nome = Formulario.exigir(Formulario.texto(request, "nome"), "o nome", 100);
        String cnpj = Formulario.cnpj(request.getParameter("cnpj"));
        int idTipoCondominio = Formulario.id(Formulario.texto(request, "idTipoCondominio"), "tipo de condomínio");
        Endereco endereco = Formulario.endereco(request, true);
        Integer unidades = Formulario.inteiroPositivoOpcional(request.getParameter("numeroUnidades"), "o número de unidades");
        BigDecimal area = Formulario.decimalPositivoOpcional(request.getParameter("areaM2"), "a área");

        //Industrial exige a área em m²; os outros tipos exigem o número de unidades. Nunca os dois.
        boolean industrial = daoCondominios.selecionarTipos().stream()
                .anyMatch(t -> t.getId() == idTipoCondominio && t.getNome().equalsIgnoreCase("Industrial"));
        if (industrial ? (area == null || unidades != null) : (unidades == null || area != null)) {
            throw new DadoInvalido(industrial
                    ? "Condomínio industrial exige a área em m² e não tem número de unidades."
                    : "Informe o número de unidades; a área em m² é só para condomínio industrial.");
        }

        if (daoCondominios.existeCnpj(cnpj, id)) {
            throw new DadoInvalido("Este CNPJ já está cadastrado em outro condomínio.");
        }

        //Busca no banco e troca só o que veio do formulário.
        Condominio condominio = daoCondominios.selecionarPorId(id);
        if (condominio == null) return false;
        condominio.setNome(nome);
        condominio.setCnpj(cnpj);
        condominio.setIdTipoCondominio(idTipoCondominio);
        condominio.setNumeroUnidades(unidades);
        condominio.setAreaM2(area);

        return daoCondominios.atualizarCondominio(condominio, endereco);
    }

    private boolean deletarCondominio(int id) {
        try {
            return daoCondominios.deletarComEndereco(id);
        } catch (RuntimeException e) {
            throw new DadoInvalido("Não foi possível excluir: o condomínio ainda tem moradores, síndico ou parcerias vinculados.");
        }
    }
}
