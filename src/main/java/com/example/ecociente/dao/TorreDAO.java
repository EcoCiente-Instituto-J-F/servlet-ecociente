package com.example.ecociente.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.example.ecociente.conexao.ConexaoBD;
import com.example.ecociente.model.Torre;

public class TorreDAO {

    // ======================= MÉTODOS READ =======================

    // Listar todas as torres (usado para popular o select do morador)
    public ArrayList<Torre> listarTodas() {

        ArrayList<Torre> torres = new ArrayList<>();

        String sql = """
                SELECT id_torre, nome, numero_unidades, id_condominio
                FROM torre
                ORDER BY id_condominio, nome
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                torres.add(new Torre(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getInt(4)
                ));
            }

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }

        return torres;
    }
}
