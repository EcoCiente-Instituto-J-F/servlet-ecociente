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

    //=======================MÉTODOS CREATE=======================\

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
    //=======================MÉTODOS READ=======================\

    public Cooperativa buscarPorId(int idCooperativa) {
        String sql = """
        SELECT id_cooperativa, cnpj, id_usuario 
        FROM cooperativa 
        WHERE id_cooperativa = ?
        """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql)
        ) {
            comando.setInt(1, idCooperativa);

            try (ResultSet rs = comando.executeQuery()) {
                if (rs.next()) {
                    return new Cooperativa(
                            rs.getInt(1),
                            rs.getString(2),
                            rs.getString(3),
                            rs.getString(4),
                            rs.getDate(5).toLocalDate(),
                            rs.getBoolean(6),
                            rs.getInt(7),
                            rs.getInt(8),
                            rs.getInt(9),
                            rs.getString(10)
                    );
                }
            }

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
        return null;
    }

    public boolean buscarCNPJ(String cnpj){
        String sql = """
                SELECT c.id_cooperativa, c.cnpj, c.id_usuario
                FROM cooperativa c
                JOIN usuario u ON u.id_usuario = c.id_usuario
                ORDER BY c.id_cooperativa;
                """;

        try(Connection conexao = ConexaoBD.conectar();
            PreparedStatement comando = conexao.prepareStatement(sql);
            ResultSet rs = comando.executeQuery();
        ){
            comando.setString(1, cnpj);
            while (rs.next()){
                return true;
            }

        }catch (SQLException sqle){
            System.out.println(sqle.getMessage());
        }
        return false;
    }

    public ArrayList<Cooperativa> listarTodas() {
        ArrayList<Cooperativa> cooperativas = new ArrayList<>();

        String sql = """
         SELECT u.id_usuario, u.nome, u.email, u.senha_hash, u.data_cadastro,
               u.status, u.id_endereco, u.id_tipo_usuario,
               c.id_cooperativa, c.cnpj
        FROM cooperativa c
        JOIN usuario u ON u.id_usuario = c.id_usuario
        ORDER BY c.id_cooperativa
        """;

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet rs = comando.executeQuery()) {

            while (rs.next()) {
                cooperativas.add(new Cooperativa(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDate(5).toLocalDate(),
                        rs.getBoolean(6),
                        rs.getInt(7),
                        rs.getInt(8),
                        rs.getInt(9),
                        rs.getString(10)
                ));
            }

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }

        return cooperativas;
    }
}


    //=======================MÉTODOS UPDATE=======================\

    public boolean atualizar(Cooperativa c) {
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
            return true;

        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    //=======================MÉTODOS DELETE=======================\

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

