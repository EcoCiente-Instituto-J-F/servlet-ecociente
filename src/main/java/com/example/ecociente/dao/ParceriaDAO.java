package com.example.ecociente.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.example.ecociente.conexao.ConexaoBD;
import com.example.ecociente.model.Parceria;

public class ParceriaDAO {

    // ======================= MÉTODOS CREATE =======================

    // Sem data de início usa a data de hoje
    public boolean inserir(Parceria p) {

        String sql = """
                INSERT INTO parceira (data_inicio, data_fim, id_condominio, id_cooperativa)
                VALUES (COALESCE(?, CURRENT_DATE), ?, ?, ?)
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            setData(ps, 1, p.getDataInicio());
            setData(ps, 2, p.getDataFim());
            ps.setInt(3, p.getIdCondominio());
            ps.setInt(4, p.getIdCooperativa());

            return ps.executeUpdate() == 1;

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // ======================= MÉTODOS READ =======================

    // Lista os vínculos com o nome do condomínio e da cooperativa (a busca vale para os dois nomes)
    public ArrayList<Parceria> selecionarTodos(String busca) {

        ArrayList<Parceria> parcerias = new ArrayList<>();

        String sql = """
                SELECT p.id_parceira, p.data_inicio, p.data_fim, p.id_condominio, p.id_cooperativa,
                       c.nome, u.nome
                FROM parceira p
                JOIN condominio c ON c.id_condominio = p.id_condominio
                JOIN cooperativa co ON co.id_cooperativa = p.id_cooperativa
                JOIN usuario u ON u.id_usuario = co.id_usuario
                WHERE c.nome ILIKE ? OR u.nome ILIKE ?
                ORDER BY p.id_parceira
                """;
        String padrao = "%" + (busca == null ? "" : busca.trim()) + "%";

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setString(1, padrao);
            ps.setString(2, padrao);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Date inicio = rs.getDate(2);
                    Date fim = rs.getDate(3);

                    Parceria parceria = new Parceria(
                            rs.getInt(1),
                            inicio == null ? null : inicio.toLocalDate(),
                            fim == null ? null : fim.toLocalDate(),
                            rs.getInt(4),
                            rs.getInt(5)
                    );
                    parceria.setDadosExibicao(rs.getString(6), rs.getString(7));
                    parcerias.add(parceria);
                }
            }

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }

        return parcerias;
    }

    // Vínculos sem data de fim, ou com fim a partir de hoje
    public int contarAtivas() {
        return contarLinhas("SELECT COUNT(*) FROM parceira WHERE data_fim IS NULL OR data_fim >= CURRENT_DATE");
    }

    public int contarCondominiosSemParceria() {
        return contarLinhas("""
                SELECT COUNT(*) FROM condominio c
                WHERE NOT EXISTS (
                    SELECT 1 FROM parceira p
                    WHERE p.id_condominio = c.id_condominio
                      AND (p.data_fim IS NULL OR p.data_fim >= CURRENT_DATE))
                """);
    }

    // mesesAtras: 0 = mês atual, 1 = mês anterior (pela data de início)
    public int contarIniciadasNoMes(int mesesAtras) {
        return contarLinhas("SELECT COUNT(*) FROM parceira "
                + "WHERE date_trunc('month', data_inicio) = date_trunc('month', CURRENT_DATE - INTERVAL '" + mesesAtras + " month')");
    }

    // ======================= MÉTODOS UPDATE =======================

    public boolean atualizar(Parceria p) {

        String sql = """
                UPDATE parceira
                SET data_inicio = COALESCE(?, data_inicio), data_fim = ?, id_condominio = ?, id_cooperativa = ?
                WHERE id_parceira = ?
                """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            setData(ps, 1, p.getDataInicio());
            setData(ps, 2, p.getDataFim());
            ps.setInt(3, p.getIdCondominio());
            ps.setInt(4, p.getIdCooperativa());
            ps.setInt(5, p.getIdParceria());

            return ps.executeUpdate() == 1;

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // ======================= MÉTODOS DELETE =======================

    public boolean deletar(int idParceria) {

        String sql = "DELETE FROM parceira WHERE id_parceira = ?";

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {

            ps.setInt(1, idParceria);

            return ps.executeUpdate() == 1;

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    private void setData(PreparedStatement ps, int indice, java.time.LocalDate data) throws SQLException {
        if (data == null) {
            ps.setNull(indice, Types.DATE);
        } else {
            ps.setDate(indice, Date.valueOf(data));
        }
    }

    private int contarLinhas(String sql) {
        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }
}
