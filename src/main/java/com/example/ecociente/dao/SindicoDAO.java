package com.example.ecociente.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;

import com.example.ecociente.conexao.ConexaoBD;
import com.example.ecociente.model.Condominio;
import com.example.ecociente.model.Endereco;
import com.example.ecociente.model.Sindico;
import com.example.ecociente.model.Usuario;

public class SindicoDAO {

    // Datas do mandato podem ser nulas no banco
    private static LocalDate dataOuNulo(Date data) {
        return data == null ? null : data.toLocalDate();
    }

    private static void setDataOuNulo(PreparedStatement ps, int indice, LocalDate data) throws SQLException {
        if (data == null) {
            ps.setNull(indice, Types.DATE);
        } else {
            ps.setDate(indice, Date.valueOf(data));
        }
    }

    // ======================= MÉTODOS CREATE =======================

    // Inserir novo síndico (o CPF fica na tabela usuario)
    public boolean inserir(Sindico s) {

        String sql = """
                INSERT INTO sindico
                    (data_inicio_mandato, data_fim_mandato, id_condominio, id_usuario)
                VALUES
                    (?, ?, ?, ?)
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            setDataOuNulo(ps, 1, s.getDataInicioMandato());
            setDataOuNulo(ps, 2, s.getDataFimMandato());
            ps.setInt(3, s.getIdCondominio());
            ps.setInt(4, s.getIdUsuario());

            ps.executeUpdate();
            return true;

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // Cadastro do síndico que assina: grava o usuário (sem endereço próprio), o telefone, o endereço e o condomínio
    // (já com o token) e o vínculo de síndico, tudo na mesma transação. O mandato começa hoje, sem data de fim.
    // Devolve o id do condomínio criado (a assinatura fica ligada a ele).
    public int cadastrarComCondominio(Usuario usuario, String telefone, Condominio condominio, Endereco endereco) {

        try (Connection conexao = ConexaoBD.conectar()) {
            conexao.setAutoCommit(false);
            try {
                int idUsuario;
                String sqlUsuario = """
                        INSERT INTO usuario (nome, email, senha_hash, data_cadastro, status, id_tipo_usuario, cpf)
                        VALUES (?, ?, ?, ?, ?, ?, ?)
                        RETURNING id_usuario
                        """;
                try (PreparedStatement ps = conexao.prepareStatement(sqlUsuario)) {
                    ps.setString(1, usuario.getNome());
                    ps.setString(2, usuario.getEmail());
                    ps.setString(3, usuario.getSenhaHash());
                    ps.setDate(4, Date.valueOf(usuario.getDataCadastro()));
                    ps.setBoolean(5, usuario.isStatus());
                    ps.setInt(6, usuario.getIdTipoUsuario());
                    ps.setString(7, usuario.getCpf());
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        idUsuario = rs.getInt(1);
                    }
                }
                TelefoneDAO.salvarNaTransacao(conexao, idUsuario, telefone);

                int idEndereco = EnderecoDAO.inserirNaTransacao(conexao, endereco);

                int idCondominio;
                String sqlCondominio = """
                        INSERT INTO condominio (nome, cnpj, status, token, id_endereco, id_tipo_condominio, numero_unidades, area_m2)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                        RETURNING id_condominio
                        """;
                try (PreparedStatement ps = conexao.prepareStatement(sqlCondominio)) {
                    ps.setString(1, condominio.getNome());
                    ps.setString(2, condominio.getCnpj());
                    ps.setBoolean(3, condominio.isStatus());
                    ps.setString(4, condominio.getToken());
                    ps.setInt(5, idEndereco);
                    ps.setInt(6, condominio.getIdTipoCondominio());
                    ps.setObject(7, condominio.getNumeroUnidades(), Types.INTEGER);
                    ps.setBigDecimal(8, condominio.getAreaM2());
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        idCondominio = rs.getInt(1);
                    }
                }

                String sqlSindico = """
                        INSERT INTO sindico (data_inicio_mandato, id_usuario, id_condominio)
                        VALUES (CURRENT_DATE, ?, ?)
                        """;
                try (PreparedStatement ps = conexao.prepareStatement(sqlSindico)) {
                    ps.setInt(1, idUsuario);
                    ps.setInt(2, idCondominio);
                    ps.executeUpdate();
                }

                conexao.commit();
                return idCondominio;
            } catch (SQLException sqle) {
                conexao.rollback();
                throw sqle;
            }
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // ======================= MÉTODOS READ =======================

    // Buscar síndico pelo ID
    public Sindico buscarPorId(int idSindico) {

        String sql = """
                SELECT u.id_usuario,
                       u.nome,
                       u.email,
                       u.senha_hash,
                       u.data_cadastro,
                       u.status,
                       u.id_endereco,
                       u.id_tipo_usuario,
                       s.id_sindico,
                       s.data_inicio_mandato,
                       s.data_fim_mandato,
                       s.id_condominio,
                       u.cpf
                FROM sindico s
                JOIN usuario u ON u.id_usuario = s.id_usuario
                WHERE s.id_sindico = ?
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, idSindico);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    Sindico sindico = new Sindico(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            dataOuNulo(rs.getDate(5)),
                            rs.getBoolean(6),
                            rs.getInt(7),
                            rs.getInt(8),
                            rs.getInt(9),
                            dataOuNulo(rs.getDate(10)),
                            dataOuNulo(rs.getDate(11)),
                            rs.getInt(12)
                    );
                    sindico.setCpf(rs.getString(13));
                    return sindico;
                }
            }

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }

        return null;
    }

    // Verificar se o usuário já está vinculado a um síndico
    public boolean buscarIdUsuario(int idUsuario) {

        String sql = """
                SELECT id_usuario
                FROM sindico
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

    // Buscar síndico pelo ID do condomínio
    public Sindico buscarPorCondominio(int idCondominio) {

        String sql = """
                SELECT u.id_usuario,
                       u.nome,
                       u.email,
                       u.senha_hash,
                       u.data_cadastro,
                       u.status,
                       u.id_endereco,
                       u.id_tipo_usuario,
                       s.id_sindico,
                       s.data_inicio_mandato,
                       s.data_fim_mandato,
                       s.id_condominio,
                       u.cpf
                FROM sindico s
                JOIN usuario u ON u.id_usuario = s.id_usuario
                WHERE s.id_condominio = ?
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, idCondominio);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    Sindico sindico = new Sindico(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            dataOuNulo(rs.getDate(5)),
                            rs.getBoolean(6),
                            rs.getInt(7),
                            rs.getInt(8),
                            rs.getInt(9),
                            dataOuNulo(rs.getDate(10)),
                            dataOuNulo(rs.getDate(11)),
                            rs.getInt(12)
                    );
                    sindico.setCpf(rs.getString(13));
                    return sindico;
                }
            }

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }

        return null;
    }

    // Buscar síndico pelo ID do usuário (null se o usuário não for síndico)
    public Sindico buscarPorUsuario(int idUsuario) {

        String sql = """
                SELECT u.id_usuario,
                       u.nome,
                       u.email,
                       u.senha_hash,
                       u.data_cadastro,
                       u.status,
                       u.id_endereco,
                       u.id_tipo_usuario,
                       s.id_sindico,
                       s.data_inicio_mandato,
                       s.data_fim_mandato,
                       s.id_condominio,
                       u.cpf
                FROM sindico s
                JOIN usuario u ON u.id_usuario = s.id_usuario
                WHERE s.id_usuario = ?
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    Sindico sindico = new Sindico(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            dataOuNulo(rs.getDate(5)),
                            rs.getBoolean(6),
                            rs.getInt(7),
                            rs.getInt(8),
                            rs.getInt(9),
                            dataOuNulo(rs.getDate(10)),
                            dataOuNulo(rs.getDate(11)),
                            rs.getInt(12)
                    );
                    sindico.setCpf(rs.getString(13));
                    return sindico;
                }
            }

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }

        return null;
    }

    // Listar todos os síndicos
    public ArrayList<Sindico> listarTodos() {

        ArrayList<Sindico> sindicos = new ArrayList<>();

        String sql = """
                SELECT u.id_usuario,
                       u.nome,
                       u.email,
                       u.senha_hash,
                       u.data_cadastro,
                       u.status,
                       u.id_endereco,
                       u.id_tipo_usuario,
                       s.id_sindico,
                       s.data_inicio_mandato,
                       s.data_fim_mandato,
                       s.id_condominio,
                       u.cpf
                FROM sindico s
                JOIN usuario u ON u.id_usuario = s.id_usuario
                ORDER BY s.id_sindico
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Sindico sindico = new Sindico(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        dataOuNulo(rs.getDate(5)),
                        rs.getBoolean(6),
                        rs.getInt(7),
                        rs.getInt(8),
                        rs.getInt(9),
                        dataOuNulo(rs.getDate(10)),
                        dataOuNulo(rs.getDate(11)),
                        rs.getInt(12)
                );
                sindico.setCpf(rs.getString(13));
                sindicos.add(sindico);
            }

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }

        return sindicos;
    }

    // ======================= MÉTODOS UPDATE =======================

    // Atualizar dados do síndico (o CPF fica na tabela usuario)
    public boolean atualizar(Sindico s) {

        String sql = """
                UPDATE sindico
                SET data_inicio_mandato = ?,
                    data_fim_mandato = ?,
                    id_condominio = ?,
                    id_usuario = ?
                WHERE id_sindico = ?
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            setDataOuNulo(ps, 1, s.getDataInicioMandato());
            setDataOuNulo(ps, 2, s.getDataFimMandato());
            ps.setInt(3, s.getIdCondominio());
            ps.setInt(4, s.getIdUsuario());
            ps.setInt(5, s.getIdSindico());

            ps.executeUpdate();
            return true;

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // ======================= MÉTODOS DELETE =======================

    // Deletar síndico pelo ID
    public boolean deletar(int idSindico) {

        String sql = """
                DELETE FROM sindico
                WHERE id_sindico = ?
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, idSindico);

            ps.executeUpdate();
            return true;

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }
}
