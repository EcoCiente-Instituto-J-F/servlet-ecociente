package com.example.ecociente.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.example.ecociente.conexao.ConexaoBD;
import com.example.ecociente.model.Sindico;

public class SindicoDAO {

    // ======================= MÉTODOS CREATE =======================

    // Inserir novo síndico
    public boolean inserir(Sindico s) {

        String sql = """
                INSERT INTO sindico
                    (cpf, data_inicio_mandato, data_fim_mandato, id_condominio, id_usuario)
                VALUES
                    (?, ?, ?, ?, ?)
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, s.getCpf());
            ps.setDate(2, Date.valueOf(s.getDataInicioMandato()));
            ps.setDate(3, Date.valueOf(s.getDataFimMandato()));
            ps.setInt(4, s.getIdCondominio());
            ps.setInt(5, s.getIdUsuario());

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
                       s.cpf,
                       s.data_inicio_mandato,
                       s.data_fim_mandato,
                       s.id_condominio
                FROM sindico s
                JOIN usuario u ON u.id_usuario = s.id_usuario
                WHERE s.id_sindico = ?
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, idSindico);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return new Sindico(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getDate(5).toLocalDate(),
                            rs.getBoolean(6),
                            rs.getInt(7),
                            rs.getInt(8),
                            rs.getInt(9),
                            rs.getString(10),
                            rs.getDate(11).toLocalDate(),
                            rs.getDate(12).toLocalDate(),
                            rs.getInt(13)
                    );
                }
            }

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }

        return null;
    }

    // Verificar se o CPF já está cadastrado
    public boolean buscarCPF(String cpf) {

        String sql = """
                SELECT id_sindico
                FROM sindico
                WHERE cpf = ?
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, cpf);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
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
                       s.cpf,
                       s.data_inicio_mandato,
                       s.data_fim_mandato,
                       s.id_condominio
                FROM sindico s
                JOIN usuario u ON u.id_usuario = s.id_usuario
                WHERE s.id_condominio = ?
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, idCondominio);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return new Sindico(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getDate(5).toLocalDate(),
                            rs.getBoolean(6),
                            rs.getInt(7),
                            rs.getInt(8),
                            rs.getInt(9),
                            rs.getString(10),
                            rs.getDate(11).toLocalDate(),
                            rs.getDate(12).toLocalDate(),
                            rs.getInt(13)
                    );
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
                       s.cpf,
                       s.data_inicio_mandato,
                       s.data_fim_mandato,
                       s.id_condominio
                FROM sindico s
                JOIN usuario u ON u.id_usuario = s.id_usuario
                ORDER BY s.id_sindico
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                sindicos.add(new Sindico(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDate(5).toLocalDate(),
                        rs.getBoolean(6),
                        rs.getInt(7),
                        rs.getInt(8),
                        rs.getInt(9),
                        rs.getString(10),
                        rs.getDate(11).toLocalDate(),
                        rs.getDate(12).toLocalDate(),
                        rs.getInt(13)
                ));
            }

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }

        return sindicos;
    }

    // ======================= MÉTODOS UPDATE =======================

    // Atualizar dados do síndico
    public boolean atualizar(Sindico s) {

        String sql = """
                UPDATE sindico
                SET cpf = ?,
                    data_inicio_mandato = ?,
                    data_fim_mandato = ?,
                    id_condominio = ?,
                    id_usuario = ?
                WHERE id_sindico = ?
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, s.getCpf());
            ps.setDate(2, Date.valueOf(s.getDataInicioMandato()));
            ps.setDate(3, Date.valueOf(s.getDataFimMandato()));
            ps.setInt(4, s.getIdCondominio());
            ps.setInt(5, s.getIdUsuario());
            ps.setInt(6, s.getIdSindico());

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