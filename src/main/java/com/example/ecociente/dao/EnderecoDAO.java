package com.example.ecociente.dao;

import com.example.ecociente.conexao.ConexaoBD;
import com.example.ecociente.model.Endereco;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.Statement;


public class EnderecoDAO {
    //=======================MÉTODOS CREATE=======================\\

    public boolean inserir(Endereco endereco){
        String sql = """
                INSERT INTO endereco 
                    (cep, cidade, estado, bairro, rua, numero, complemento)
                VALUES
                    (?, ?, ?, ?, ?, ?, ?)
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

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;




        }catch (SQLException sqle){
            System.out.println("Erro ao inserir endereço" + sqle.getMessage());

            return false;
        }
    }

    public int inserirERetornarId(Endereco endereco){
        String sql = """
            INSERT INTO endereco (cep, cidade, estado, bairro, rua, numero, complemento)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        ){
            ps.setString(1, endereco.getCep());
            ps.setString(2, endereco.getCidade());
            ps.setString(3, endereco.getEstado());
            ps.setString(4, endereco.getBairro());
            ps.setString(5, endereco.getRua());
            ps.setInt(6, endereco.getNumero());
            ps.setString(7, endereco.getComplemento());
            ps.executeUpdate();

            ResultSet chaves = ps.getGeneratedKeys();
            if (chaves.next()) {
                return chaves.getInt(1);
            }
            return 0;
        }catch (SQLException sqle){
            System.out.println("Erro ao inserir endereço" + sqle.getMessage());
            return 0;
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

    // seleciona todos
    public ArrayList<Endereco> selecionarTodos(){
        ArrayList<Endereco> todosEnderecos = new ArrayList<>();
        String sql = """
            SELECT id_endereco, cep, cidade, estado, bairro, rua, numero, complemento
            FROM endereco
            """;
        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps =
                        conexao.prepareStatement(sql)
        ){
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                todosEnderecos.add(new Endereco(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getString(5),
                        rs.getString(6),
                        rs.getInt(7),
                        rs.getString(8)
                ));
            }



        }catch (SQLException sqle){
            System.out.println(sqle.getMessage());

        }finally {
            return todosEnderecos;
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

    // Conta usuarios e condominios que usam o endereco
    public int contarVinculos(int idEndereco){
        String sql = """
                SELECT (SELECT COUNT(*) FROM usuario WHERE id_endereco = ?)
                     + (SELECT COUNT(*) FROM condominio WHERE id_endereco = ?)
                """;

        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps = conexao.prepareStatement(sql)
        ){
            ps.setInt(1, idEndereco);
            ps.setInt(2, idEndereco);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : 0;
            }
        } catch (SQLException sqle) {
            System.out.println("Erro ao contar vinculos do endereco" + sqle.getMessage());
            return -1;
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
