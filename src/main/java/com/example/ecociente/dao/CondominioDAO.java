
package com.example.ecociente.dao;

import com.example.ecociente.conexao.ConexaoBD;
import com.example.ecociente.model.Condominio;

import java.sql.*;
import java.util.ArrayList;

public class CondominioDAO{
    private Connection conexao;

    //=======================MÉTODOS CREATE=======================\\
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
        return retorno;
    } // Método que insere um condomínio


    //=======================MÉTODOS READ=======================\\
    public boolean existeCnpj(String cnpj){
        boolean retorno = false;
        try{
            conexao = ConexaoBD.conectar();
            PreparedStatement comando = conexao.prepareStatement("SELECT cnpj FROM condominio WHERE cnpj LIKE ?");
            comando.setString(1, cnpj);
            ResultSet condominio = comando.executeQuery();
            while(condominio.next()){
                retorno = true;
            }
        }
        catch(SQLException sqle){
            System.out.println("!!SQLException ao chamar CondominioDAO.existeCnpj(cnpj)!!");
            sqle.printStackTrace();
        }
        return retorno;
    } // Método que retorna se o cnpj já está sendo usado

    public ArrayList<Condominio> selecionarTodos(){
        ArrayList<Condominio> condominios = new ArrayList<>();
        try{
            conexao = ConexaoBD.conectar();
            Statement comando = conexao.createStatement();
            ResultSet condominio = comando.executeQuery("SELECT * FROM condominio ORDER BY nome");

            while(condominio.next()){
                condominios.add(new Condominio(
                        condominio.getInt("id_condominio"),
                        condominio.getString("nome"),
                        condominio.getString("cnpj"),
                        condominio.getBoolean("status"),
                        condominio.getString("token"),
                        condominio.getInt("id_endereco"),
                        condominio.getInt("id_tipo_condominio")
                ));
            }
        }
        catch(SQLException sqle){
            System.out.println("!!SQLException ao chamar CondominioDAO.selecionarTodos()!!");
            sqle.printStackTrace();
        }
        return condominios;
    } // Método que seleciona todos os condomínios

    public ArrayList<Condominio> selecionarPorNome(String procura){
        ArrayList<Condominio> condominios = new ArrayList<>();
        try{
            conexao = ConexaoBD.conectar();
            PreparedStatement comando = conexao.prepareStatement("SELECT * FROM condominio WHERE lower(nome) LIKE lower(?) ORDER BY nome");
            comando.setString(1, "%" + procura + "%");
            ResultSet condominio = comando.executeQuery();

            while(condominio.next()){
                condominios.add(new Condominio(
                        condominio.getInt("id_condominio"),
                        condominio.getString("nome"),
                        condominio.getString("cnpj"),
                        condominio.getBoolean("status"),
                        condominio.getString("token"),
                        condominio.getInt("id_endereco"),
                        condominio.getInt("id_tipo_condominio")
                ));
            }
        }
        catch(SQLException sqle){
            System.out.println("!!SQLException ao chamar CondominioDAO.selecionarPorNome(procura)!!");
            sqle.printStackTrace();
        }
        return condominios;
    } // Método que seleciona os condomínios por nome

    public Condominio selecionarPorId(int id){

        Condominio condominioEncontrado = null;
        try{
            conexao = ConexaoBD.conectar();
            PreparedStatement comando = conexao.prepareStatement("SELECT * FROM condominio WHERE id_condominio = ?");
            comando.setInt(1, id);
            ResultSet condominio = comando.executeQuery();

            while(condominio.next()){
                condominioEncontrado = new Condominio(
                        condominio.getInt("id_condominio"),
                        condominio.getString("nome"),
                        condominio.getString("cnpj"),
                        condominio.getBoolean("status"),
                        condominio.getString("token"),
                        condominio.getInt("id_endereco"),
                        condominio.getInt("id_tipo_condominio")
                );
            }
        }
        catch(SQLException sqle){
            System.out.println("!!SQLException ao chamar CondominioDAO.selecionarPorId(id)!!");
            sqle.printStackTrace();
        }
        return condominioEncontrado;
    } // Método que seleciona um condomínio por id

    //=======================MÉTODOS UPDATE=======================\\
    public boolean atualizar(Condominio condominio){
        boolean retorno = false;
        try{
            conexao = ConexaoBD.conectar();
            PreparedStatement comando = conexao.prepareStatement("UPDATE condominio SET nome = ?, cnpj = ?, status = ?, token = ?, id_endereco = ?, id_tipo_condominio = ? WHERE id_condominio = ?");
            comando.setString(1, condominio.getNome());
            comando.setString(2, condominio.getCnpj());
            comando.setBoolean(3, condominio.isStatus());
            comando.setString(4, condominio.getToken());
            comando.setInt(5, condominio.idEndereco());
            comando.setInt(6, condominio.getIdTipoCondominio());
            comando.setInt(7, condominio.getIdCondominio());

            retorno = comando.executeUpdate() >= 1;
        }
        catch(SQLException sqle){
            System.out.println("!!SQLException ao chamar CondominioDAO.atualizar(condominio)!!");
            sqle.printStackTrace();
        }
        return retorno;
    } // Método que atualiza os dados do condomínio por id

    public boolean atualizarStatus(int id, boolean status){
        boolean retorno = false;
        try{
            conexao = ConexaoBD.conectar();
            PreparedStatement comando = conexao.prepareStatement("UPDATE condominio SET status = ? WHERE id_condominio = ?");
            comando.setBoolean(1, status);
            comando.setInt(2, id);

            retorno = comando.executeUpdate() >= 1;
        }
        catch(SQLException sqle){
            System.out.println("!!SQLException ao chamar CondominioDAO.atualizarStatus(id, status)!!");
            sqle.printStackTrace();
        }
        return retorno;
    } // Método que ativa ou desativa o condomínio por id

    //=======================MÉTODOS DELETE=======================\\
    public boolean deletar(int id){
        boolean retorno = false;
        try{
            conexao = ConexaoBD.conectar();
            PreparedStatement comando = conexao.prepareStatement("DELETE FROM condominio WHERE id_condominio = ?");
            comando.setInt(1, id);

            retorno = comando.executeUpdate() == 1;
        }
        catch(SQLException sqle){
            System.out.println("!!SQLException ao chamar CondominioDAO.deletar(id)!!");
            sqle.printStackTrace();
        }
        return retorno;
    } // Método que deleta um condomínio por id
}
