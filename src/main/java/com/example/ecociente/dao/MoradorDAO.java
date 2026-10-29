package com.example.ecociente.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.example.ecociente.conexao.ConexaoBD;
import com.example.ecociente.model.Endereco;
import com.example.ecociente.model.Morador;
import com.example.ecociente.model.Usuario;

public class MoradorDAO {

    // ======================= MÉTODOS CREATE =======================

    public boolean inserir(Morador m) {
        String sql = """
                INSERT INTO morador
                    (numero_apartamento, torre, id_usuario, id_condominio)
                VALUES
                    (?, ?, ?, ?)
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, m.getNumeroApartamento());
            ps.setString(2, m.getTorre());
            ps.setInt(3, m.getIdUsuario());
            ps.setObject(4, m.getIdCondominio(), Types.INTEGER);

            ps.executeUpdate();
            return true;

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // Cadastro público do morador: grava o endereço (se veio), o usuário, o telefone e o morador na mesma transação.
    // Apartamento, torre e condomínio podem ser nulos: quem ainda não tem o código do condomínio fica sem vínculo.
    public boolean cadastrarComUsuario(Usuario usuario, String telefone, Endereco endereco, String numeroApartamento, String torre, Integer idCondominio) {

        try (Connection conexao = ConexaoBD.conectar()) {
            conexao.setAutoCommit(false);
            try {
                Integer idEndereco = endereco == null ? null : EnderecoDAO.inserirNaTransacao(conexao, endereco);

                int idUsuario;
                String sqlUsuario = """
                        INSERT INTO usuario (nome, email, senha_hash, data_cadastro, status, id_endereco, id_tipo_usuario, cpf)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                        RETURNING id_usuario
                        """;
                try (PreparedStatement ps = conexao.prepareStatement(sqlUsuario)) {
                    ps.setString(1, usuario.getNome());
                    ps.setString(2, usuario.getEmail());
                    ps.setString(3, usuario.getSenhaHash());
                    ps.setDate(4, Date.valueOf(usuario.getDataCadastro()));
                    ps.setBoolean(5, usuario.isStatus());
                    ps.setObject(6, idEndereco, Types.INTEGER);
                    ps.setInt(7, usuario.getIdTipoUsuario());
                    ps.setString(8, usuario.getCpf());
                    try (ResultSet rs = ps.executeQuery()) {
                        rs.next();
                        idUsuario = rs.getInt(1);
                    }
                }
                TelefoneDAO.salvarNaTransacao(conexao, idUsuario, telefone);

                String sqlMorador = """
                        INSERT INTO morador (numero_apartamento, torre, id_usuario, id_condominio)
                        VALUES (?, ?, ?, ?)
                        """;
                try (PreparedStatement ps = conexao.prepareStatement(sqlMorador)) {
                    ps.setString(1, numeroApartamento);
                    ps.setString(2, torre);
                    ps.setInt(3, idUsuario);
                    ps.setObject(4, idCondominio, Types.INTEGER);
                    ps.executeUpdate();
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

    // ======================= MÉTODOS READ =======================

    // Buscar morador pelo ID
    public Morador buscarPorId(int idMorador) {

        String sql = """
                SELECT u.id_usuario,
                       u.nome,
                       u.email,
                       u.senha_hash,
                       u.data_cadastro,
                       u.status,
                       u.id_endereco,
                       u.id_tipo_usuario,
                       m.id_morador,
                       m.numero_apartamento,
                       m.torre,
                       m.id_condominio
                FROM morador m
                JOIN usuario u ON u.id_usuario = m.id_usuario
                WHERE m.id_morador = ?
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, idMorador);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {
                    return new Morador(
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
                            rs.getString(11),
                            rs.getObject(12, Integer.class)
                    );
                }
            }

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }

        return null;
    }

    // Verificar se o usuário já está vinculado a um morador
    public boolean buscarIdUsuario(int idUsuario) {

        String sql = """
                SELECT id_usuario
                FROM morador
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

    // Listar todos os moradores
    public ArrayList<Morador> listarTodos() {

        ArrayList<Morador> moradores = new ArrayList<>();

        String sql = """
                SELECT u.id_usuario,
                       u.nome,
                       u.email,
                       u.senha_hash,
                       u.data_cadastro,
                       u.status,
                       u.id_endereco,
                       u.id_tipo_usuario,
                       m.id_morador,
                       m.numero_apartamento,
                       m.torre,
                       m.id_condominio
                FROM morador m
                JOIN usuario u ON u.id_usuario = m.id_usuario
                ORDER BY m.id_morador
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                moradores.add(new Morador(
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
                        rs.getString(11),
                        rs.getObject(12, Integer.class)
                ));
            }

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }

        return moradores;
    }

    // ======================= MÉTODOS UPDATE =======================

    public boolean atualizar(Morador m) {

        String sql = """
                UPDATE morador
                SET numero_apartamento = ?,
                    torre = ?,
                    id_usuario = ?,
                    id_condominio = ?
                WHERE id_morador = ?
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, m.getNumeroApartamento());
            ps.setString(2, m.getTorre());
            ps.setInt(3, m.getIdUsuario());
            ps.setObject(4, m.getIdCondominio(), Types.INTEGER);
            ps.setInt(5, m.getIdMorador());

            return ps.executeUpdate() == 1;

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // ======================= MÉTODOS DELETE =======================

    public boolean deletar(int idMorador) {

        String sql = """
                DELETE FROM morador
                WHERE id_morador = ?
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, idMorador);

            return ps.executeUpdate() == 1;

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }
}
