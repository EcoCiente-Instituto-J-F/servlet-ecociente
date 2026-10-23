package com.example.ecociente.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.example.ecociente.conexao.ConexaoBD;
import com.example.ecociente.model.Condominio;
import com.example.ecociente.model.Cooperativa;
import com.example.ecociente.model.Endereco;

public class CooperativaDAO {

    // ======================= MÉTODOS CREATE =======================

    public boolean inserir(Cooperativa c) {
        String sql = """
                INSERT INTO cooperativa
                    (cnpj, id_usuario)
                VALUES
                    (?, ?)
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, c.getCnpj());
            ps.setInt(2, c.getIdUsuario());

            ps.executeUpdate();
            return true;

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // ======================= MÉTODOS READ =======================

    // Buscar cooperativa pelo ID
    public Cooperativa buscarPorId(int idCooperativa) {

        String sql = """
                SELECT u.id_usuario,
                       u.nome,
                       u.email,
                       u.senha_hash,
                       u.data_cadastro,
                       u.status,
                       u.id_endereco,
                       u.id_tipo_usuario,
                       c.id_cooperativa,
                       c.cnpj
                FROM cooperativa c
                JOIN usuario u ON u.id_usuario = c.id_usuario
                WHERE c.id_cooperativa = ?
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, idCooperativa);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return new Cooperativa(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getDate(5).toLocalDate(),
                            rs.getBoolean(6),
                            rs.getInt(7),
                            rs.getInt(8),
                            rs.getInt(9),
                            rs.getString(10)
                    );
                }
            }

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }

        return null;
    }

    // Buscar pelo CNPJ
    public boolean buscarCNPJ(String cnpj) {

        String sql = """
                SELECT id_cooperativa
                FROM cooperativa
                WHERE cnpj = ?
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, cnpj);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // Buscar pelo ID do usuário
    public boolean buscarIdUsuario(int idUsuario) {

        String sql = """
                SELECT id_usuario
                FROM cooperativa
                WHERE id_usuario = ?
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // Listar todas as cooperativas
    public ArrayList<Cooperativa> listarTodas() {

        ArrayList<Cooperativa> cooperativas = new ArrayList<>();

        String sql = """
                SELECT u.id_usuario,
                       u.nome,
                       u.email,
                       u.senha_hash,
                       u.data_cadastro,
                       u.status,
                       u.id_endereco,
                       u.id_tipo_usuario,
                       c.id_cooperativa,
                       c.cnpj
                FROM cooperativa c
                JOIN usuario u ON u.id_usuario = c.id_usuario
                ORDER BY c.id_cooperativa
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                cooperativas.add(new Cooperativa(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDate(5).toLocalDate(),
                        rs.getBoolean(6),
                        rs.getInt(7),
                        rs.getInt(8),
                        rs.getInt(9),
                        rs.getString(10)
                ));
            }

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }

        return cooperativas;
    }

    // ======================= MÉTODOS UPDATE =======================

    public boolean atualizar(Cooperativa c) {

        String sql = """
                UPDATE cooperativa
                SET cnpj = ?,
                    id_usuario = ?
                WHERE id_cooperativa = ?
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, c.getCnpj());
            ps.setInt(2, c.getIdUsuario());
            ps.setInt(3, c.getIdCooperativa());

            ps.executeUpdate();
            return true;

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // ======================= MÉTODOS DELETE =======================

    public boolean deletar(int idCooperativa) {

        String sql = """
                DELETE FROM cooperativa
                WHERE id_cooperativa = ?
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, idCooperativa);

            ps.executeUpdate();
            return true;

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // ======================= MÉTODOS DO CRUD DE ADMIN (JOIN e transação) =======================

    // Lista para a tela de cooperativas: traz telefone, endereço e quantos condomínios estão vinculados
    public ArrayList<Cooperativa> listarTodas(String busca) {

        ArrayList<Cooperativa> cooperativas = new ArrayList<>();

        String sql = """
                SELECT u.id_usuario, u.nome, u.email, u.data_cadastro, u.status, u.id_endereco, u.id_tipo_usuario,
                       c.id_cooperativa, c.cnpj,
                       (SELECT numero FROM telefone WHERE id_usuario = u.id_usuario ORDER BY id_telefone LIMIT 1),
                       (SELECT COUNT(*) FROM parceira p
                        WHERE p.id_cooperativa = c.id_cooperativa
                          AND (p.data_fim IS NULL OR p.data_fim >= CURRENT_DATE)),
                       e.cep, e.cidade, e.estado, e.bairro, e.rua, e.numero, e.complemento
                FROM cooperativa c
                JOIN usuario u ON u.id_usuario = c.id_usuario
                LEFT JOIN endereco e ON e.id_endereco = u.id_endereco
                WHERE u.nome ILIKE ?
                ORDER BY u.nome
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, "%" + (busca == null ? "" : busca.trim()) + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cooperativa cooperativa = new Cooperativa(
                            rs.getInt(1), rs.getString(2), rs.getString(3), null,
                            rs.getDate(4).toLocalDate(), rs.getBoolean(5), rs.getInt(6), rs.getInt(7),
                            rs.getInt(8), Condominio.formatarCnpj(rs.getString(9))
                    );
                    cooperativa.setDadosExibicao("Cooperativa", rs.getString(10), EnderecoDAO.deLinha(rs, rs.getInt(6), 12));
                    cooperativa.setCondominiosVinculados(rs.getInt(11));
                    cooperativas.add(cooperativa);
                }
            }

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }

        return cooperativas;
    }

    // Verifica se o CNPJ já é de outra cooperativa (o exceptoId é a própria cooperativa na edição, 0 no cadastro)
    public boolean existeCnpj(String cnpj, int exceptoId) {

        String sql = "SELECT 1 FROM cooperativa WHERE cnpj = ? AND id_cooperativa <> ?";

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, cnpj);
            ps.setInt(2, exceptoId);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // Cadastra endereço (se veio), usuário dono, cooperativa e telefone (se veio) na mesma transação
    public boolean inserir(Cooperativa c, String telefone, Endereco endereco) {

        try (Connection conexao = ConexaoBD.conectar()) {
            conexao.setAutoCommit(false);
            try {
                int idEndereco = endereco == null ? 0 : EnderecoDAO.inserirNaTransacao(conexao, endereco);

                String sqlUsuario = """
                        INSERT INTO usuario (nome, email, senha_hash, status, id_endereco, id_tipo_usuario)
                        VALUES (?, ?, ?, ?, ?, ?)
                        RETURNING id_usuario
                        """;
                int idUsuario;
                try (PreparedStatement ps = conexao.prepareStatement(sqlUsuario)) {
                    ps.setString(1, c.getNome());
                    ps.setString(2, c.getEmail());
                    ps.setString(3, c.getSenhaHash());
                    ps.setBoolean(4, c.isStatus());
                    if (idEndereco == 0) ps.setNull(5, Types.INTEGER); else ps.setInt(5, idEndereco);
                    ps.setInt(6, c.getIdTipoUsuario());
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        idUsuario = rs.getInt(1);
                    }
                }

                try (PreparedStatement ps = conexao.prepareStatement("INSERT INTO cooperativa (cnpj, id_usuario) VALUES (?, ?)")) {
                    ps.setString(1, c.getCnpj());
                    ps.setInt(2, idUsuario);
                    ps.executeUpdate();
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

    // Atualiza usuário dono, cooperativa, endereço (se veio) e telefone (se veio) na mesma transação
    public boolean atualizar(Cooperativa c, String telefone, Endereco endereco) {

        try (Connection conexao = ConexaoBD.conectar()) {
            conexao.setAutoCommit(false);
            try {
                int idEndereco = c.getIdEndereco();
                if (endereco != null) {
                    if (idEndereco == 0) {
                        idEndereco = EnderecoDAO.inserirNaTransacao(conexao, endereco);
                    } else {
                        EnderecoDAO.atualizarNaTransacao(conexao, idEndereco, endereco);
                    }
                }

                String sqlUsuario = """
                        UPDATE usuario
                        SET nome = ?, email = ?, senha_hash = ?, status = ?, id_endereco = ?
                        WHERE id_usuario = ?
                        """;
                try (PreparedStatement ps = conexao.prepareStatement(sqlUsuario)) {
                    ps.setString(1, c.getNome());
                    ps.setString(2, c.getEmail());
                    ps.setString(3, c.getSenhaHash());
                    ps.setBoolean(4, c.isStatus());
                    if (idEndereco == 0) ps.setNull(5, Types.INTEGER); else ps.setInt(5, idEndereco);
                    ps.setInt(6, c.getIdUsuario());
                    ps.executeUpdate();
                }

                int linhas;
                try (PreparedStatement ps = conexao.prepareStatement("UPDATE cooperativa SET cnpj = ? WHERE id_cooperativa = ?")) {
                    ps.setString(1, c.getCnpj());
                    ps.setInt(2, c.getIdCooperativa());
                    linhas = ps.executeUpdate();
                }
                TelefoneDAO.salvarNaTransacao(conexao, c.getIdUsuario(), telefone);
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

    // Apaga a cooperativa, as parcerias dela e o usuário dono (se ele não tem outro papel de síndico ou morador)
    public boolean deletarComUsuario(int idCooperativa) {

        try (Connection conexao = ConexaoBD.conectar()) {
            conexao.setAutoCommit(false);
            try {
                int idUsuario;
                int idEndereco;
                String sqlDono = """
                        SELECT c.id_usuario, COALESCE(u.id_endereco, 0)
                        FROM cooperativa c
                        JOIN usuario u ON u.id_usuario = c.id_usuario
                        WHERE c.id_cooperativa = ?
                        """;
                try (PreparedStatement ps = conexao.prepareStatement(sqlDono)) {
                    ps.setInt(1, idCooperativa);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (!rs.next()) return false;
                        idUsuario = rs.getInt(1);
                        idEndereco = rs.getInt(2);
                    }
                }

                executar(conexao, "DELETE FROM parceira WHERE id_cooperativa = ?", idCooperativa);
                executar(conexao, "DELETE FROM cooperativa WHERE id_cooperativa = ?", idCooperativa);

                String sqlOutroPapel = """
                        SELECT EXISTS (SELECT 1 FROM sindico WHERE id_usuario = ?)
                            OR EXISTS (SELECT 1 FROM morador WHERE id_usuario = ?)
                            OR EXISTS (SELECT 1 FROM cooperativa WHERE id_usuario = ?)
                        """;
                boolean temOutroPapel;
                try (PreparedStatement ps = conexao.prepareStatement(sqlOutroPapel)) {
                    ps.setInt(1, idUsuario);
                    ps.setInt(2, idUsuario);
                    ps.setInt(3, idUsuario);
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        temOutroPapel = rs.getBoolean(1);
                    }
                }
                if (!temOutroPapel) {
                    executar(conexao, "DELETE FROM telefone WHERE id_usuario = ?", idUsuario);
                    executar(conexao, "DELETE FROM usuario WHERE id_usuario = ?", idUsuario);
                    EnderecoDAO.apagarSeSolto(conexao, idEndereco);
                }
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

    private void executar(Connection conexao, String sql, int id) throws SQLException {
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int contar() {
        return contarLinhas("SELECT COUNT(*) FROM cooperativa");
    }

    // mesesAtras: 0 = mês atual, 1 = mês anterior (pela data de cadastro do usuário dono)
    public int contarCadastradasNoMes(int mesesAtras) {
        return contarLinhas("SELECT COUNT(*) FROM cooperativa c JOIN usuario u ON u.id_usuario = c.id_usuario "
                + "WHERE date_trunc('month', u.data_cadastro) = date_trunc('month', CURRENT_DATE - INTERVAL '" + mesesAtras + " month')");
    }

    private int contarLinhas(String sql) {
        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }
}
