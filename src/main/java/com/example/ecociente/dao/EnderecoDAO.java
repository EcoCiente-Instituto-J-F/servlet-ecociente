package com.example.ecociente.dao;

import com.example.ecociente.conexao.ConexaoBD;
import com.example.ecociente.model.Endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class EnderecoDAO {
    //=======================MÉTODOS CREATE=======================\\

    // Insere novo endereco com complemento
    public boolean inserir(Endereco endereco){
        String sql = """
                INSERT INTO endereco 
                    (id_endereco, cep, cidade, estado, bairro, rua, numero, complemento)
                VALUES
                    (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement comando =
                        conexao.prepareStatement(sql)
        ){
            comando.setInt(1, endereco.getIdEndereco());
            comando.setString(2, endereco.getCep());
            comando.setString(3, endereco.getCidade());
            comando.setString(4, endereco.getEstado());
            comando.setString(5, endereco.getBairro());
            comando.setString(6, endereco.getBairro());
            comando.setInt(7, endereco.getNumero());
            comando.setString(8, endereco.getComplemento());

            int linhasAfetadas = comando.executeUpdate();

            return linhasAfetadas > 0;




        }catch (SQLException sqle){
            System.out.println("Erro ao inserir endereço" + sqle.getMessage());

            return false;
        }
    }

    //=======================MÉTODOS UPDATE=======================\

    public boolean atualizar(Endereco endereco){
        String sql = """
                UPDATE endereco
                    SET cep=?, cidade=?, estado=?, bairro=?, rua=?, numero=?, complemento=? 
                """;

        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement comando =
                        conexao.prepareStatement(sql)
        ){
            comando.setString(1, endereco.getCep());
            comando.setString(2, endereco.getCidade());
            comando.setString(3, endereco.getBairro());
            comando.setString(4, endereco.getRua());
            comando.setInt(5, endereco.getNumero());
            comando.setString(6, endereco.getComplemento());

            int linhasAfetadas = comando.executeUpdate();
            return linhasAfetadas == 1;

        }catch (SQLException sqle){
            System.out.println("Erro ao inserir endereço" + sqle.getMessage());

            return false;
        }

    }


}
