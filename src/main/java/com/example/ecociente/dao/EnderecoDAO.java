package com.example.ecociente.dao;

import com.example.ecociente.conexao.ConexaoBD;
import com.example.ecociente.model.Endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class EnderecoDAO {
    //=======================MÉTODOS CREATE=======================\\

    public boolean inserir(Endereco endereco){
        String sql = """
                INSERT INTO endereco 
                    (id_endereco, cep, cidade, estado, bairro, rua, numero, complemento)
                VALUES
                    (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps =
                        conexao.prepareStatement(sql)
        ){
            ps.setInt(1, endereco.getIdEndereco());
            ps.setString(2, endereco.getCep());
            ps.setString(3, endereco.getCidade());
            ps.setString(4, endereco.getEstado());
            ps.setString(5, endereco.getBairro());
            ps.setString(6, endereco.getRua());
            ps.setInt(7, endereco.getNumero());
            ps.setString(8, endereco.getComplemento());

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;




        }catch (SQLException sqle){
            System.out.println("Erro ao inserir endereço" + sqle.getMessage());

            return false;
        }
    }

    //=======================MÉTODOS READ=======================\
    public Endereco selecionarPorId(int id){
        String sql = """
            SELECT id_endereco, cep, cidade, estado, bairro, rua, numero, complemento
            FROM endereco  
            WHERE id_endereco = ?
            """;
        Endereco retorno = null;
        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps =
                        conexao.prepareStatement(sql)
        ){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                retorno = new Endereco(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getInt(7),
                        rs.getString(8)
                );
            }


        }catch (SQLException sqle){
            System.out.println("Erro ao selecionar endereço" + sqle.getMessage());

        }finally {
            return retorno;
        }


    }

    //=======================MÉTODOS UPDATE=======================\

    public boolean atualizar(Endereco endereco){
        String sql = """
                UPDATE endereco
                    SET cep=?, cidade=?, estado=?, bairro=?, rua=?, numero=?, complemento=? 
                WHERE id_endereco=?
                """;

        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps =
                        conexao.prepareStatement(sql)
        ){
            ps.setString(1, endereco.getCep());
            ps.setString(2, endereco.getCidade());
            ps.setString(3, endereco.getEstado());
            ps.setString(4, endereco.getBairro());
            ps.setString(5, endereco.getRua());
            ps.setInt(6, endereco.getNumero());
            ps.setString(7, endereco.getComplemento());
            ps.setInt(8, endereco.getIdEndereco());

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas == 1;

        }catch (SQLException sqle){
            System.out.println("Erro ao atualizar endereço" + sqle.getMessage());

            return false;
        }

    }

    //=======================MÉTODOS DELETE=======================\
    public boolean deletar(Endereco endereco){
        String sql = """
                DELETE FROM endereco
                    WHERE id_endereco =?
                """;

        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps =
                        conexao.prepareStatement(sql)
        ){
            ps.setInt(1, endereco.getIdEndereco());

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;

        }catch (SQLException sqle){
            System.out.println(
                    "Erro ao deletar endereco" +
                            sqle.getMessage()
            );

            return false;
        }

    }


}
