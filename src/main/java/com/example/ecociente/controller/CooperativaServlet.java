package com.example.ecociente.controller;

import com.example.ecociente.dao.CooperativaDAO;
import com.example.ecociente.dao.UsuarioDAO;
import com.example.ecociente.model.Cooperativa;
import com.example.ecociente.model.Endereco;
import com.example.ecociente.util.DadoInvalido;
import com.example.ecociente.util.Formulario;
import com.example.ecociente.util.Senha;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;

//Servlet usada para as ações do CRUD de Cooperativas. Tudo pela mesma rota: GET lista, POST cadastra, altera ou exclui.
@WebServlet(urlPatterns = {"/cooperativas"})
public class CooperativaServlet extends HttpServlet {
    CooperativaDAO daoCooperativas = new CooperativaDAO();
    UsuarioDAO daoUsuarios = new UsuarioDAO();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        mostrarSelects(request, response);
    }

    //Busca as cooperativas (com pesquisa opcional) e envia para a JSP.
    private void mostrarSelects(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("cooperativas", Formulario.paginar(request, daoCooperativas.listarTodas(request.getParameter("q"))));
        request.getRequestDispatcher("/WEB-INF/views/cooperativas.jsp").forward(request, response);
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
                ok = daoCooperativas.deletarComUsuario(Formulario.id(id, "cooperativa"));
            } else if (id.isEmpty()) {
                ok = adicionarCooperativa(request);
            } else {
                ok = alterarCooperativa(request, Formulario.id(id, "cooperativa"));
            }
            request.setAttribute(resultado, ok ? "true" : "false");
        } catch (DadoInvalido e) {
            request.setAttribute("erro", e.getMessage());
        } catch (RuntimeException e) {
            request.setAttribute("erro", "Operação não concluída. Confira os dados informados e se o registro possui vínculos.");
        }
        mostrarSelects(request, response);
    }

    //O formulário envia o nome da cooperativa/responsável como "nome" (ou "responsavel").
    private String lerNome(HttpServletRequest request) {
        String nome = Formulario.texto(request, "nome");
        if (nome.isEmpty()) nome = Formulario.texto(request, "responsavel");
        return Formulario.exigir(nome, "o nome", 100);
    }

    //Status do formulário: "true"/"Ativa" ou "false"/"Inativa". Sem valor, fica ativa.
    private boolean lerAtiva(HttpServletRequest request) {
        String status = Formulario.texto(request, "status");
        return status.isEmpty() || status.equalsIgnoreCase("true") || status.equalsIgnoreCase("Ativa");
    }

    private boolean adicionarCooperativa(HttpServletRequest request) {
        String nome = lerNome(request);
        String cnpj = Formulario.cnpj(request.getParameter("cnpj"));
        String email = Formulario.email(Formulario.texto(request, "email"));
        String senha = request.getParameter("senha");
        if (senha == null || senha.length() < 6) {
            throw new DadoInvalido("A senha precisa ter pelo menos 6 caracteres.");
        }
        String telefone = Formulario.telefone(request.getParameter("telefone"));
        Endereco endereco = Formulario.endereco(request, true);

        if (daoCooperativas.existeCnpj(cnpj, 0)) {
            throw new DadoInvalido("Este CNPJ já está cadastrado em outra cooperativa.");
        }
        if (daoUsuarios.existeEmail(email, 0)) {
            throw new DadoInvalido("Este e-mail já está cadastrado.");
        }
        int idTipoUsuario = daoUsuarios.selecionarIdTipoPorNome("Cooperativa");
        if (idTipoUsuario == 0) {
            throw new DadoInvalido("Tipo de usuário Cooperativa não existe na tabela tipo_usuario.");
        }

        //Ids 0 porque o banco gera. O usuário dono e o endereço são criados junto, na mesma transação.
        return daoCooperativas.inserir(new Cooperativa(
                0, nome, email, Senha.gerarHash(senha), LocalDate.now(), lerAtiva(request), 0, idTipoUsuario,
                0, cnpj
        ), telefone, endereco);
    }

    private boolean alterarCooperativa(HttpServletRequest request, int id) {
        String nome = lerNome(request);
        String cnpj = Formulario.cnpj(request.getParameter("cnpj"));
        String emailInformado = Formulario.texto(request, "email");
        String senha = request.getParameter("senha");
        if (senha != null && !senha.isEmpty() && senha.length() < 6) {
            throw new DadoInvalido("A senha precisa ter pelo menos 6 caracteres.");
        }
        String telefone = Formulario.telefone(request.getParameter("telefone"));
        Endereco endereco = Formulario.endereco(request, true);

        if (daoCooperativas.existeCnpj(cnpj, id)) {
            throw new DadoInvalido("Este CNPJ já está cadastrado em outra cooperativa.");
        }

        //Busca no banco e troca só o que veio do formulário (e-mail e senha vazios mantêm os atuais).
        Cooperativa cooperativa = daoCooperativas.buscarPorId(id);
        if (cooperativa == null) return false;

        if (!emailInformado.isEmpty()) {
            String email = Formulario.email(emailInformado);
            if (daoUsuarios.existeEmail(email, cooperativa.getIdUsuario())) {
                throw new DadoInvalido("Este e-mail já está cadastrado.");
            }
            cooperativa.setEmail(email);
        }
        if (senha != null && !senha.isEmpty()) {
            cooperativa.setSenhaHash(Senha.gerarHash(senha));
        }
        cooperativa.setNome(nome);
        cooperativa.setCnpj(cnpj);
        cooperativa.setStatus(lerAtiva(request));

        return daoCooperativas.atualizar(cooperativa, telefone, endereco);
    }
}
