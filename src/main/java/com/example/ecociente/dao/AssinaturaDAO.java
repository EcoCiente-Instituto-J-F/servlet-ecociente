package com.example.ecociente.dao;

import com.example.ecociente.conexao.ConexaoBD;
import com.example.ecociente.model.Assinatura;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class AssinaturaDAO {

    // ======================= MÉTODOS CREATE / UPDATE =======================

    // Grava a assinatura como pendente e devolve o id. Se a pessoa voltou e mudou ciclo ou quantidade,
    // atualiza a pendente que já existe (o banco só aceita uma pendente ou ativa por condomínio ou por usuário sem condomínio).
    public int salvarPendente(Assinatura a) {

        String sqlAtualizar = """
                UPDATE assinatura
                SET ciclo = ?, quantidade = ?, valor_cobrado = ?, id_plano = ?
                WHERE id_usuario = ? AND id_condominio IS NOT DISTINCT FROM ? AND status = 'pendente'
                RETURNING id_assinatura
                """;

        String sqlInserir = """
                INSERT INTO assinatura (ciclo, quantidade, valor_cobrado, status, data_cadastro, id_usuario, id_condominio, id_plano)
                VALUES (?, ?, ?, 'pendente', ?, ?, ?, ?)
                RETURNING id_assinatura
                """;

        try (Connection conexao = ConexaoBD.conectar()) {
            conexao.setAutoCommit(false);
            try {
                int idAssinatura = 0;
                try (PreparedStatement ps = conexao.prepareStatement(sqlAtualizar)) {
                    ps.setString(1, a.getCiclo());
                    ps.setBigDecimal(2, a.getQuantidade());
                    ps.setBigDecimal(3, a.getValorCobrado());
                    ps.setInt(4, a.getIdPlano());
                    ps.setInt(5, a.getIdUsuario());
                    ps.setObject(6, a.getIdCondominio(), Types.INTEGER);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) idAssinatura = rs.getInt(1);
                    }
                }

                if (idAssinatura == 0) {
                    try (PreparedStatement ps = conexao.prepareStatement(sqlInserir)) {
                        ps.setString(1, a.getCiclo());
                        ps.setBigDecimal(2, a.getQuantidade());
                        ps.setBigDecimal(3, a.getValorCobrado());
                        ps.setDate(4, Date.valueOf(a.getDataCadastro()));
                        ps.setInt(5, a.getIdUsuario());
                        ps.setObject(6, a.getIdCondominio(), Types.INTEGER);
                        ps.setInt(7, a.getIdPlano());
                        try (ResultSet rs = ps.executeQuery()) {
                            rs.next();
                            idAssinatura = rs.getInt(1);
                        }
                    }
                }

                conexao.commit();
                a.setIdAssinatura(idAssinatura);
                return idAssinatura;
            } catch (SQLException sqle) {
                conexao.rollback();
                throw sqle;
            }
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }
}
