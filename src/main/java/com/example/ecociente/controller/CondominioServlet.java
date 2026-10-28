package com.example.ecociente.controller;

import com.example.ecociente.dao.CondominioDAO;
import com.example.ecociente.dao.SindicoDAO;
import com.example.ecociente.dao.UsuarioDAO;
import com.example.ecociente.model.Condominio;
import com.example.ecociente.model.Endereco;
import com.example.ecociente.model.Usuario;
import com.example.ecociente.util.DadoInvalido;
import com.example.ecociente.util.Formulario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;

//Servlet usada para as ações do CRUD de Condomínios. Tudo pela mesma rota: GET lista, POST cadastra, altera ou exclui.
@WebServlet(urlPatterns = {"/condominios"})
public class CondominioServlet extends HttpServlet {
    CondominioDAO daoCondominios = new CondominioDAO();
    UsuarioDAO daoUsuarios = new UsuarioDAO();
    SindicoDAO daoSindicos = new SindicoDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        mostrarSelects(request, response);
    }

    //Busca os condomínios (com pesquisa por nome opcional) e os tipos (para o select) e envia para a JSP.
    private void mostrarSelects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String procura = request.getParameter("q");
        if (procura == null) procura = request.getParameter("search");

        request.setAttribute("condominios", Formulario.paginar(request, daoCondominios.selecionarTodos(procura)));
        request.setAttribute("tiposCondominio", daoCondominios.selecionarTipos());
        request.setAttribute("usuariosSindico", daoUsuarios.selecionarDisponiveisParaSindico(LoginServlet.TIPO_ADMINISTRADOR));
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

        //O síndico é opcional. Se veio, precisa poder assumir o condomínio.
        String idSindico = Formulario.texto(request, "idUsuarioSindico");
        Integer idUsuarioSindico = null;
        int idTipoSindico = 0;
        LocalDate inicioMandato = Formulario.data(request.getParameter("dataInicioMandato"), "do início do mandato");
        if (inicioMandato == null) inicioMandato = LocalDate.now();
        if (!idSindico.isEmpty()) {
            idUsuarioSindico = Formulario.id(idSindico, "usuário");
            validarSindico(idUsuarioSindico);
            idTipoSindico = daoUsuarios.selecionarIdTipoPorNome("Sindico");
            if (idTipoSindico == 0) throw new DadoInvalido("O tipo de usuário Síndico não existe no banco.");
        }

        //Id 0 porque o banco gera. O endereço, o condomínio e o síndico são criados juntos, na mesma transação.
        Condominio condominio = new Condominio(0, nome, cnpj, true, daoCondominios.gerarTokenUnico(), 0, idTipoCondominio);
        condominio.setNumeroUnidades(unidades);
        condominio.setAreaM2(area);
        boolean ok = daoCondominios.inserir(condominio, endereco, idUsuarioSindico, idTipoSindico, inicioMandato);
        if (ok) request.setAttribute("tokenGerado", condominio.getToken());
        return ok;
    }

    //O usuário precisa existir, não ser administrador nem síndico e ter CPF cadastrado.
    private void validarSindico(int idUsuario) {
        Usuario usuario = daoUsuarios.selecionarUsuarioPorId(idUsuario);
        if (usuario == null) throw new DadoInvalido("Usuário do síndico não encontrado.");
        if (usuario.getIdTipoUsuario() == LoginServlet.TIPO_ADMINISTRADOR) throw new DadoInvalido("O administrador não pode ser síndico.");
        if (daoSindicos.buscarIdUsuario(idUsuario)) throw new DadoInvalido("Este usuário já é síndico.");
        if (usuario.getCpf() == null || usuario.getCpf().isBlank()) {
            throw new DadoInvalido("Este usuário não tem CPF. Cadastre o CPF dele na tela de Usuários antes de torná-lo síndico.");
        }
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
        //Condomínios criados antes do token existir ganham um aqui.
        if (condominio.getToken() == null || condominio.getToken().isBlank()) condominio.setToken(daoCondominios.gerarTokenUnico());

        return daoCondominios.atualizarCondominio(condominio, endereco);
    }

    private boolean deletarCondominio(int id) {
        try {
            return daoCondominios.deletarComEndereco(id);
        } catch (RuntimeException e) {
            throw new DadoInvalido("Não foi possível excluir: o condomínio ainda tem moradores ou parcerias vinculados.");
        }
    }
}
