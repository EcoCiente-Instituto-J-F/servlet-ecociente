package com.example.ecociente.dao;

import com.example.ecociente.conexao.ConexaoBD;
import com.example.ecociente.model.Plano;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PlanoDAO {

    // ======================= MÉTODOS READ =======================

    // O banco tem 2 planos: "empresarial" (condomínios residenciais e comerciais) e "industrial".
    // Buscar o plano ativo do tipo do condomínio (residencial-comercial ou industrial; residencial e comercial continuam aceitos)
    public Plano selecionarPorCodigo(String tipoCondominio) {

        if (tipoCondominio == null) return null;
        String tipo = tipoCondominio.toLowerCase();
        if (!tipo.equals("residencial-comercial") && !tipo.equals("residencial") && !tipo.equals("comercial") && !tipo.equals("industrial")) return null;

        String sql = """
                SELECT id_plano, codigo, nome, descricao, unidade, preco_mensal, preco_anual, ativo
                FROM plano
                WHERE codigo = ? AND ativo = TRUE
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, tipo.equals("industrial") ? "industrial" : "empresarial");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    // No banco a unidade do plano empresarial é só "unidade": nas telas fica "unidade" para residencial-comercial (apartamento ou empresa só nos tipos antigos)
                    String unidade = rs.getString(5);
                    if ("unidade".equals(unidade) && !tipo.equals("residencial-comercial")) unidade = tipo.equals("comercial") ? "empresa" : "apartamento";

                    return new Plano(
                            rs.getInt(1),
                            tipo,
                            rs.getString(3),
                            rs.getString(4),
                            unidade,
                            rs.getBigDecimal(6),
                            rs.getBigDecimal(7),
                            rs.getBoolean(8)
                    );
                }
            }

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }

        return null;
    }
}
