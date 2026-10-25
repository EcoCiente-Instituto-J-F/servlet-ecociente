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
import com.example.ecociente.model.Sindico;

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
