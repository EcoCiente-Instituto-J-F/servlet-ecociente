package com.example.ecociente.dao;

import com.example.ecociente.conexao.ConexaoBD;
import com.example.ecociente.model.Endereco;
import com.example.ecociente.model.TipoUsuario;
import com.example.ecociente.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.sql.Types;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

public class UsuarioDAO {

    //=======================MÉTODOS CREATE=======================\\

    // Metodo para inserir um usuário no banco
    public boolean inserir(Usuario usuario){
        String sql = """
        INSERT INTO usuario
            (nome, email, senha_hash, data_cadastro, status, id_endereco, id_tipo_usuario, cpf)
        VALUES
            (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps = conexao.prepareStatement(sql)
        ){
            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getSenhaHash());
            ps.setDate(4, Date.valueOf(usuario.getDataCadastro()));
            ps.setBoolean(5, usuario.isStatus());

            if (usuario.getIdEndereco() == 0) {
                ps.setNull(6, Types.INTEGER);
            } else {
                ps.setInt(6, usuario.getIdEndereco());
            }

            ps.setInt(7, usuario.getIdTipoUsuario());
            ps.setString(8, usuario.getCpf());

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException sqle) {
            System.out.println("Erro ao inserir usuario" + sqle.getMessage());
            return false;
        }
    }

    //=======================MÉTODOS READ=======================\

    // Metodo para selecionar um usuário por ID
    public Usuario selecionarUsuarioPorId(int id){
        String sql = """
                SELECT id_usuario, nome, email, senha_hash, data_cadastro, status, id_endereco, id_tipo_usuario, cpf
                FROM usuario
                WHERE id_usuario = ?
                """;
        Usuario retorno = null;
        try (
            Connection conexao = ConexaoBD.conectar();
            PreparedStatement ps =
                    conexao.prepareStatement(sql)
        ){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                retorno = new Usuario(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDate(5).toLocalDate(),
                        rs.getBoolean(6),
                        rs.getInt(7),
                        rs.getInt(8)
                );
                retorno.setCpf(rs.getString(9));
            }


        }catch (SQLException sqle){
            System.out.println("Erro ao selecionar usuario" + sqle.getMessage());

        }finally {
            return retorno;
        }


    }

    // selecionar usuario por email

    public Usuario selecionarPorEmail(String email){
        String sql = """
            SELECT id_usuario, nome, email, senha_hash, data_cadastro, status, id_endereco, id_tipo_usuario
            FROM usuario
            WHERE LOWER(email) = LOWER(?)
            """;
        Usuario retorno = null;
        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps = conexao.prepareStatement(sql)
        ){
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                retorno = new Usuario(
                        rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getDate(5).toLocalDate(), rs.getBoolean(6), rs.getInt(7), rs.getInt(8)
                );
            }

        }catch (SQLException sqle){
            System.out.println("Erro ao selecionar usuario por email" + sqle.getMessage());

        }finally {
            return retorno;
        }
    }

    //seleciona todos

    public ArrayList<Usuario> selecionarTodos(){
        ArrayList<Usuario> todosUsuarios = new ArrayList<>();
        String sql = """
                SELECT id_usuario, nome, email, senha_hash, data_cadastro, status, id_endereco, id_tipo_usuario
                FROM usuario
                """;
        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps =
                        conexao.prepareStatement(sql)
        ){
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                todosUsuarios.add(new Usuario(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDate(5).toLocalDate(),
                        rs.getBoolean(6),
                        rs.getInt(7),
                        rs.getInt(8)
                ));
            }



        }catch (SQLException sqle){
            System.out.println(sqle.getMessage());

        }finally {
            return todosUsuarios;
        }
    }

    public ArrayList<Usuario> selecionarPorNomeOuEmail(String procura){
        ArrayList<Usuario> encontrados = new ArrayList<>();
        String sql = """
            SELECT id_usuario, nome, email, senha_hash, data_cadastro, status, id_endereco, id_tipo_usuario
            FROM usuario
            WHERE LOWER(nome) LIKE LOWER(?) OR LOWER(email) LIKE LOWER(?)
            ORDER BY nome
            """;
        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps = conexao.prepareStatement(sql)
        ){
            ps.setString(1, "%" + procura + "%");
            ps.setString(2, "%" + procura + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()){
                    encontrados.add(new Usuario(
                            rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                            rs.getDate(5).toLocalDate(), rs.getBoolean(6), rs.getInt(7), rs.getInt(8)
                    ));
                }
            }
        } catch (SQLException sqle){
            System.out.println("Erro ao selecionar usuarios por nome ou email" + sqle.getMessage());
        }
        return encontrados;
    }

    // Verifica se um email ja esta cadastrado
    public boolean existeEmail(String email){
        String sql = "SELECT 1 FROM usuario WHERE email = ?";
        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps = conexao.prepareStatement(sql)
        ){
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException sqle){
            System.out.println("Erro ao verificar email" + sqle.getMessage());
            return false;
        }
    }

    //=======================MÉTODOS UPDATE=======================\
    public boolean atualizar(Usuario usuario){
        boolean retorno = false;
        String sql = """
                UPDATE usuario 
                    SET nome=?, email=?, senha_hash=?, id_endereco=?
                WHERE id_usuario = ?
                """;
        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps =
                        conexao.prepareStatement(sql)
        ){

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getSenhaHash());
            if (usuario.getIdEndereco() == 0) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, usuario.getIdEndereco());
            }
            ps.setInt(5, usuario.getIdUsuario());

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas == 1;



        }catch (SQLException sqle){
            System.out.println(sqle.getMessage());
            return retorno;
        }
    }

    //=======================MÉTODOS DELETE=======================\\
    public boolean deletar(Usuario usuario){
        int idUsuario = usuario.getIdUsuario();

        String[] sqls = {
                "DELETE FROM telefone WHERE id_usuario = ?",
                "DELETE FROM parceira WHERE id_cooperativa IN (SELECT id_cooperativa FROM cooperativa WHERE id_usuario = ?)",
                "DELETE FROM cooperativa WHERE id_usuario = ?",
                "DELETE FROM sindico WHERE id_usuario = ?",
                "DELETE FROM morador WHERE id_usuario = ?",
                "DELETE FROM usuario WHERE id_usuario = ?"
        };

        try (Connection conexao = ConexaoBD.conectar()){
            conexao.setAutoCommit(false);
            try {
                int idEndereco = 0;
                try (PreparedStatement ps = conexao.prepareStatement("SELECT id_endereco FROM usuario WHERE id_usuario = ?")) {
                    ps.setInt(1, idUsuario);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) idEndereco = rs.getInt(1);
                    }
                }

                int linhasUsuario = 0;
                for (String sql : sqls) {
                    try (PreparedStatement ps = conexao.prepareStatement(sql)) {
                        ps.setInt(1, idUsuario);
                        linhasUsuario = ps.executeUpdate();
                    }
                }

                //endereço que ficou sem dono também sai
                if (linhasUsuario > 0 && idEndereco != 0) {
                    String apagarEndereco = """
                            DELETE FROM endereco
                            WHERE id_endereco = ?
                              AND NOT EXISTS (SELECT 1 FROM usuario WHERE id_endereco = ?)
                              AND NOT EXISTS (SELECT 1 FROM condominio WHERE id_endereco = ?)
                            """;
                    try (PreparedStatement ps = conexao.prepareStatement(apagarEndereco)) {
                        ps.setInt(1, idEndereco);
                        ps.setInt(2, idEndereco);
                        ps.setInt(3, idEndereco);
                        ps.executeUpdate();
                    }
                }
                conexao.commit();
                return linhasUsuario > 0;
            } catch (SQLException sqle) {
                conexao.rollback();
                System.out.println("Erro ao deletar usuario" + sqle.getMessage());
                return false;
            }
        } catch (SQLException sqle) {
            System.out.println("Erro ao deletar usuario" + sqle.getMessage());
            return false;
        }
    }

    //=======================MÉTODOS DO CRUD DE ADMIN (JOIN e transação)=======================\\

    // Lista para a tela de usuários: traz o nome do tipo, o primeiro telefone e o endereço
    public ArrayList<Usuario> selecionarTodos(String busca){
        ArrayList<Usuario> usuarios = new ArrayList<>();
        String sql = """
                SELECT u.id_usuario, u.nome, u.email, u.data_cadastro, u.status, u.id_endereco, u.id_tipo_usuario, t.nome_tipo,
                       (SELECT numero FROM telefone WHERE id_usuario = u.id_usuario ORDER BY id_telefone LIMIT 1),
                       e.cep, e.cidade, e.estado, e.bairro, e.rua, e.numero, e.complemento,
                       u.cpf
                FROM usuario u
                JOIN tipo_usuario t ON t.id_tipo_usuario = u.id_tipo_usuario
                LEFT JOIN endereco e ON e.id_endereco = u.id_endereco
                WHERE u.nome ILIKE ? OR u.email ILIKE ?
                ORDER BY u.nome
                """;
        String padrao = "%" + (busca == null ? "" : busca.trim()) + "%";

        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps = conexao.prepareStatement(sql)
        ){
            ps.setString(1, padrao);
            ps.setString(2, padrao);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()){
                    Usuario usuario = new Usuario(
                            rs.getInt(1), rs.getString(2), rs.getString(3), null,
                            rs.getDate(4).toLocalDate(), rs.getBoolean(5), rs.getInt(6), rs.getInt(7)
                    );
                    usuario.setDadosExibicao(rs.getString(8), rs.getString(9), EnderecoDAO.deLinha(rs, rs.getInt(6), 10));
                    usuario.setCpf(rs.getString(17));
                    usuarios.add(usuario);
                }
            }
        } catch (SQLException sqle){
            throw new RuntimeException(sqle.getMessage());
        }
        return usuarios;
    }

    // Tipos de usuário para o select do formulário
    public ArrayList<TipoUsuario> selecionarTipos(){
        ArrayList<TipoUsuario> tipos = new ArrayList<>();
        String sql = "SELECT id_tipo_usuario, nome_tipo, descricao FROM tipo_usuario ORDER BY id_tipo_usuario";
        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps = conexao.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ){
            while (rs.next()){
                tipos.add(new TipoUsuario(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
        } catch (SQLException sqle){
            throw new RuntimeException(sqle.getMessage());
        }
        return tipos;
    }

    // Id do tipo cujo nome começa com o texto (ex.: "Cooperativa"); 0 se não existe
    public int selecionarIdTipoPorNome(String nome){
        String sql = "SELECT id_tipo_usuario FROM tipo_usuario WHERE LOWER(nome_tipo) LIKE LOWER(?) ORDER BY id_tipo_usuario LIMIT 1";
        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps = conexao.prepareStatement(sql)
        ){
            ps.setString(1, nome + "%");
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException sqle){
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // Verifica se o e-mail já é de outro usuário (o exceptoId é o próprio usuário na edição, 0 no cadastro)
    public boolean existeEmail(String email, int exceptoId){
        String sql = "SELECT 1 FROM usuario WHERE LOWER(email) = LOWER(?) AND id_usuario <> ?";
        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps = conexao.prepareStatement(sql)
        ){
            ps.setString(1, email);
            ps.setInt(2, exceptoId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException sqle){
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // Verifica se o CPF já é de outro usuário (o exceptoId é o próprio usuário na edição, 0 no cadastro)
    public boolean existeCpf(String cpf, int exceptoId){
        String sql = "SELECT 1 FROM usuario WHERE cpf = ? AND id_usuario <> ?";
        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps = conexao.prepareStatement(sql)
        ){
            ps.setString(1, cpf);
            ps.setInt(2, exceptoId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException sqle){
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // Cadastra endereço (se veio), usuário e telefone (se veio) na mesma transação
    public boolean inserir(Usuario usuario, String telefone, Endereco endereco){
        try (Connection conexao = ConexaoBD.conectar()) {
            conexao.setAutoCommit(false);
            try {
                int idEndereco = endereco == null ? 0 : EnderecoDAO.inserirNaTransacao(conexao, endereco);

                String sql = """
                        INSERT INTO usuario (nome, email, senha_hash, data_cadastro, status, id_endereco, id_tipo_usuario, cpf)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                        RETURNING id_usuario
                        """;
                int idUsuario;
                try (PreparedStatement ps = conexao.prepareStatement(sql)) {
                    ps.setString(1, usuario.getNome());
                    ps.setString(2, usuario.getEmail());
                    ps.setString(3, usuario.getSenhaHash());
                    ps.setDate(4, Date.valueOf(usuario.getDataCadastro()));
                    ps.setBoolean(5, usuario.isStatus());
                    if (idEndereco == 0) ps.setNull(6, Types.INTEGER); else ps.setInt(6, idEndereco);
                    ps.setInt(7, usuario.getIdTipoUsuario());
                    ps.setString(8, usuario.getCpf());
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        idUsuario = rs.getInt(1);
                    }
                }
                TelefoneDAO.salvarNaTransacao(conexao, idUsuario, telefone);
                conexao.commit();
                return true;
            } catch (SQLException sqle) {
                conexao.rollback();
                throw sqle;
            }
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // Atualiza usuário (inclusive tipo), endereço (se veio) e telefone (se veio) na mesma transação
    public boolean atualizar(Usuario usuario, String telefone, Endereco endereco){
        try (Connection conexao = ConexaoBD.conectar()) {
            conexao.setAutoCommit(false);
            try {
                int idEndereco = usuario.getIdEndereco();
                if (endereco != null) {
                    if (idEndereco == 0) {
                        idEndereco = EnderecoDAO.inserirNaTransacao(conexao, endereco);
                    } else {
                        EnderecoDAO.atualizarNaTransacao(conexao, idEndereco, endereco);
                    }
                }

                String sql = """
                        UPDATE usuario
                        SET nome = ?, email = ?, senha_hash = ?, id_tipo_usuario = ?, id_endereco = ?, cpf = ?
                        WHERE id_usuario = ?
                        """;
                int linhas;
                try (PreparedStatement ps = conexao.prepareStatement(sql)) {
                    ps.setString(1, usuario.getNome());
                    ps.setString(2, usuario.getEmail());
                    ps.setString(3, usuario.getSenhaHash());
                    ps.setInt(4, usuario.getIdTipoUsuario());
                    if (idEndereco == 0) ps.setNull(5, Types.INTEGER); else ps.setInt(5, idEndereco);
                    ps.setString(6, usuario.getCpf());
                    ps.setInt(7, usuario.getIdUsuario());
                    linhas = ps.executeUpdate();
                }
                TelefoneDAO.salvarNaTransacao(conexao, usuario.getIdUsuario(), telefone);
                conexao.commit();
                return linhas == 1;
            } catch (SQLException sqle) {
                conexao.rollback();
                throw sqle;
            }
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    //=======================MÉTODOS DE CONTAGEM (home do painel)=======================\\

    public int contar(){
        return contarLinhas("SELECT COUNT(*) FROM usuario");
    }

    // mesesAtras: 0 = mês atual, 1 = mês anterior
    public int contarCadastradosNoMes(int mesesAtras){
        return contarLinhas("SELECT COUNT(*) FROM usuario WHERE date_trunc('month', data_cadastro) = date_trunc('month', CURRENT_DATE - INTERVAL '" + mesesAtras + " month')");
    }

    // Metodo utilizado no grafico do painel servlet
    // Cadastros dos últimos 7 meses (posição 6 = mês atual). papel: "sindico", "cooperativa" ou qualquer outro valor para os demais
    public int[] contarCadastrosPorMes(String papel){
        String filtro = switch (papel) {
            case "sindico" -> "AND EXISTS (SELECT 1 FROM sindico s WHERE s.id_usuario = u.id_usuario)";
            case "cooperativa" -> "AND EXISTS (SELECT 1 FROM cooperativa c WHERE c.id_usuario = u.id_usuario)";
            default -> "AND NOT EXISTS (SELECT 1 FROM sindico s WHERE s.id_usuario = u.id_usuario) "
                    + "AND NOT EXISTS (SELECT 1 FROM cooperativa c WHERE c.id_usuario = u.id_usuario)";
        };
        String sql = "SELECT to_char(u.data_cadastro, 'YYYY-MM'), COUNT(*) FROM usuario u "
                + "WHERE u.data_cadastro >= date_trunc('month', CURRENT_DATE) - INTERVAL '6 months' " + filtro
                + " GROUP BY 1";
        int[] valores = new int[7];
        YearMonth atual = YearMonth.now();
        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps = conexao.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ){
            while (rs.next()){
                YearMonth mes = YearMonth.parse(rs.getString(1));
                int indice = 6 - (int) ChronoUnit.MONTHS.between(mes, atual);
                if (indice >= 0 && indice < 7) valores[indice] = rs.getInt(2);
            }
        } catch (SQLException sqle){
            throw new RuntimeException(sqle.getMessage());
        }
        return valores;
    }

    private int contarLinhas(String sql){
        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps = conexao.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ){
            rs.next();
            return rs.getInt(1);
        } catch (SQLException sqle){
            throw new RuntimeException(sqle.getMessage());
        }
    }
}
