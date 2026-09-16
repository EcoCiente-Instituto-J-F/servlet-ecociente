package com.example.ecociente.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.example.ecociente.conexao.ConexaoBD;
import com.example.ecociente.model.Cooperativa;

public class CooperativaDAO {

    // INSERIR
    public boolean inserir(Cooperativa c) {
        String sql = """
                INSERT INTO cooperativa 
                    (cnpj, id_usuario) 
                VALUES 
                    (?, ?)
                """;
        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) { // prepara e transforma a string na linguagem do sql

            comando.setString(1, c.getCnpj());
            comando.setInt(2, c.getIdUsuario());
            comando.executeUpdate(); // Dispara o comanfo
            return true;
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // UPTADE
    public void atualizar(Cooperativa c) {
        String sql = """
                       UPDATE cooperativa 
                       SET cnpj = ?, id_usuario = ? 
                       WHERE id_cooperativa = ?
                       """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setString(1, c.getCnpj());
            comando.setInt(2, c.getIdUsuario());
            comando.setInt(3, c.getIdCooperativa());
            comando.executeUpdate();

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // DELETE
    public boolean deletar(int idCooperativa) {
        String sql = """
                DELETE FROM cooperativa 
                WHERE id_cooperativa = ?
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, idCooperativa);
            comando.executeUpdate();
            return true;
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // SELECT
    public Cooperativa buscarPorId(int idCooperativa) {
        String sql = """
        SELECT id_cooperativa, cnpj, id_usuario 
        FROM cooperativa 
        WHERE id_cooperativa = ?
        """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)) {

            comando.setInt(1, idCooperativa);

            try (ResultSet rs = comando.executeQuery()) {
                if (rs.next()) {
                    return montar(rs);
                }
                return null; // não achou
            }
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // SELECT ALL
    public List<Cooperativa> listarTodas() {
        String sql = """
        SELECT id_cooperativa, cnpj, id_usuario 
        FROM cooperativa 
        ORDER BY id_cooperativa
        """;
        List<Cooperativa> lista = new ArrayList<>();

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet rs = comando.executeQuery()) {

            while (rs.next()) {
                lista.add(montar(rs));
            }
            return lista;

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }
    private Cooperativa montar(ResultSet rs) throws SQLException {
        Cooperativa c = new Cooperativa();
        c.setIdCooperativa(rs.getInt("id_cooperativa"));
        c.setCnpj(rs.getString("cnpj"));
        c.setIdUsuario(rs.getInt("id_usuario"));
        return c;
    }
}