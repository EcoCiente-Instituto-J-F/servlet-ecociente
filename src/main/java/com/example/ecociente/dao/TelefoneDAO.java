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
}