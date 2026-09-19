package com.example.ecociente.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.example.ecociente.conexao.ConexaoBD;
import com.example.ecociente.model.Telefone;

public class TelefoneDAO {

    // ======================= MÉTODOS CREATE =======================

    public boolean inserir(Telefone t) {
        String sql = """
                INSERT INTO telefone
                    (numero, idUsuario)
                VALUES
                    (?, ?)
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, t.getNumero());
            comando.setInt(2, t.getIdUsuario());

            comando.executeUpdate();
            return true;

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // ======================= MÉTODOS READ =======================

    // Buscar telefone pelo ID
    public Telefone buscarPorId(int idTelefone) {

        String sql = """
                SELECT idTelefone, numero, idUsuario
                FROM telefone
                WHERE idTelefone = ?
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, idTelefone);

            try (ResultSet rs = comando.executeQuery()) {

                if (rs.next()) {
                    return new Telefone(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getInt(3)
                    );
                }
            }

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }

        return null;
    }

    // Buscar telefone pelo número
    public boolean buscarPorNumero(String numero) {

        String sql = """
                SELECT idTelefone, numero, idUsuario
                FROM telefone
                WHERE numero = ?
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, numero);

            try (ResultSet rs = comando.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // Listar todos os telefones
    public ArrayList<Telefone> listarTodos() {

        ArrayList<Telefone> telefones = new ArrayList<>();

        String sql = """
                SELECT idTelefone, numero, idUsuario
                FROM telefone
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet rs = comando.executeQuery()) {

            while (rs.next()) {
                telefones.add(new Telefone(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3)
                ));
            }

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }

        return telefones;
    }

    // ======================= MÉTODOS UPDATE =======================

    public boolean atualizar(Telefone t) {

        String sql = """
                UPDATE telefone
                SET numero = ?,
                    idUsuario = ?
                WHERE idTelefone = ?
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, t.getNumero());
            comando.setInt(2, t.getIdUsuario());
            comando.setInt(3, t.getIdTelefone());

            comando.executeUpdate();
            return true;

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // ======================= MÉTODOS DELETE =======================

    public boolean deletar(int idTelefone) {

        String sql = """
                DELETE FROM telefone
                WHERE idTelefone = ?
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, idTelefone);

            comando.executeUpdate();
            return true;

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }
}