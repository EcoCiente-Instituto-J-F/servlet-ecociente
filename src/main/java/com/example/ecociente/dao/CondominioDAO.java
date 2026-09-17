package com.example.ecociente.dao;

import com.example.ecociente.conexao.ConexaoBD;
import com.example.ecociente.model.Condominio;

import java.sql.*;
import java.util.ArrayList;

public class CondominioDAO {
    private Connection conexao;


    /=======================MÉTODOS CREATE=======================\\
    public boolean inserir(Condominio condominio){
        boolean retorno = false;
        try{
            conexao = ConexaoBD.conectar();
            PreparedStatement comando = conexao.prepareStatement("INSERT INTO condominio(id_condominio, nome, cnpj, status, token, id_endereco, id_tipo_condominio) VALUES (?,?,?,?,?,?,?)");
            comando.setInt(1, condominio.getIdCondominio());
            comando.setString(2, condominio.getNome());
            comando.setString(3, condominio.getCnpj());
            comando.setBoolean(4, condominio.isStatus());
            comando.setString(5, condominio.getToken());
            comando.setInt(6, condominio.idEndereco());
            comando.setInt(7, condominio.getIdTipoCondominio());

            retorno = comando.executeUpdate() == 1;
        }
        catch(SQLException sqle){
            System.out.println("!!SQLException ao chamar CondominioDAO.inserir(condominio)!!");
            sqle.printStackTrace();
        }
        finally{
            ConexaoBD.desconectar(conexao);
            return retorno;
        }
    } // Método que insere um condomínio

}
