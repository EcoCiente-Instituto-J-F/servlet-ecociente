package com.example.ecociente.dao;

import com.example.ecociente.conexao.ConexaoBD;
import com.example.ecociente.model.Condominio;

import java.sql.*;
import java.util.ArrayList;

public class CondominioDAO{

    //=======================MÉTODOS CREATE=======================\\
    public boolean inserir(Condominio condominio){
        boolean retorno = false;
        String sql = """
                INSERT INTO condominio
                    (id_condominio, nome, cnpj, status, token, id_endereco, id_tipo_condominio)
                VALUES
                    (?,?,?,?,?,?,?)
                """;
        try(Connection conexao = ConexaoBD.conectar()){
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setInt(1, condominio.getIdCondominio());
            ps.setString(2, condominio.getNome());
            ps.setString(3, condominio.getCnpj());
            ps.setBoolean(4, condominio.isStatus());
            ps.setString(5, condominio.getToken());
            ps.setInt(6, condominio.idEndereco());
            ps.setInt(7, condominio.getIdTipoCondominio());

            retorno = ps.executeUpdate() == 1;
        }
        catch(SQLException sqle){
            System.out.println("!!SQLException ao chamar CondominioDAO.inserir(condominio)!!");
            sqle.printStackTrace();
        }
        return retorno;
    } // Metodo que insere um condomínio
}