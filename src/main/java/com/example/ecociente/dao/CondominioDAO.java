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


    //=======================MÉTODOS READ=======================\\
    public boolean selecionarCnpj(String cnpj){
        boolean retorno = false;
        String sql = """
                SELECT cnpj
                FROM condominio
                WHERE cnpj
                LIKE ?
                """;
        try(Connection conexao = ConexaoBD.conectar()){
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setString(1, cnpj);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                retorno = true;
            }
        }
        catch(SQLException sqle){
            System.out.println("!!SQLException ao chamar CondominioDAO.existeCnpj(cnpj)!!");
            sqle.printStackTrace();
        }
        return retorno;
    } // Metodo que retorna se o cnpj já está sendo usado

    public ArrayList<Condominio> selecionarTodos(){
        ArrayList<Condominio> condominios = new ArrayList<>();
        String sql = """
                SELECT * FROM condominio
                         ORDER BY nome
                """;
        try(Connection conexao = ConexaoBD.conectar()){
            Statement ps = conexao.createStatement();
            ResultSet condominio = ps.executeQuery(sql);

            while(condominio.next()){
                condominios.add(new Condominio(
                        condominio.getInt(1),
                        condominio.getString(2),
                        condominio.getString(3),
                        condominio.getBoolean(4),
                        condominio.getString(5),
                        condominio.getInt(6),
                        condominio.getInt(7)
                ));
            }
        }
        catch(SQLException sqle){
            throw new RuntimeException(sqle.getMessage());
        }
        return condominios;
    } // Metodo que seleciona todos os condomínios

    public ArrayList<Condominio> selecionarPorNomeCondominio(String procura){
        ArrayList<Condominio> condominios = new ArrayList<>();
        String sql = """
                SELECT id_condominio, nome, cnpj, status, token, id_endereco, id_tipo_condominio
                    FROM condominio
                        WHERE nome LIKE ?
                        ORDER BY id_condominio
                """;
        try(Connection conexao = ConexaoBD.conectar()){
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setString(1, "%" + procura + "%");
            ResultSet condominio = ps.executeQuery();

            while(condominio.next()){
                condominios.add(new Condominio(
                        condominio.getInt(1),
                        condominio.getString(2),
                        condominio.getString(3),
                        condominio.getBoolean(4),
                        condominio.getString(5),
                        condominio.getInt(6),
                        condominio.getInt(7)
                ));
            }
        }
        catch(SQLException sqle){
            throw new RuntimeException(sqle.getMessage());
        }
        return condominios;
    } // Metodo que seleciona os condomínios por nome

    public Condominio selecionarPorId(int id_condominio){
        Condominio condominioEncontrado = null;
        String sql = """
                SELECT * FROM condominio
                         WHERE id_condominio = ?
                """;
        try(Connection conexao = ConexaoBD.conectar()){
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setInt(1, id_condominio);
            ResultSet condominio = ps.executeQuery();

            if(condominio.next()){
                condominioEncontrado = new Condominio(
                        condominio.getInt(1),
                        condominio.getString(2),
                        condominio.getString(3),
                        condominio.getBoolean(4),
                        condominio.getString(5),
                        condominio.getInt(6),
                        condominio.getInt(7)
                );
            }
        }
        catch(SQLException sqle){
            throw new RuntimeException(sqle.getMessage());
        }
        return condominioEncontrado;
    } // Metodo que seleciona um condomínio por id

    //=======================MÉTODOS UPDATE=======================\\
    public boolean atualizarCondominio(Condominio condominio){
        boolean retorno = false;
        String sql = """
                UPDATE condominio
                SET nome = ?, cnpj = ?, status = ?, token = ?, id_endereco = ?, id_tipo_condominio = ?
                WHERE id_condominio = ?
                """;
        try(Connection conexao = ConexaoBD.conectar()){
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setString(1, condominio.getNome());
            ps.setString(2, condominio.getCnpj());
            ps.setBoolean(3, condominio.isStatus());
            ps.setString(4, condominio.getToken());
            ps.setInt(5, condominio.idEndereco());
            ps.setInt(6, condominio.getIdTipoCondominio());
            ps.setInt(7, condominio.getIdCondominio());

            retorno = ps.executeUpdate() == 1;
        }
        catch(SQLException sqle){
            throw new RuntimeException(sqle.getMessage());
        }
        return retorno;
    } // Metodo que atualiza os dados do condomínio por id
}