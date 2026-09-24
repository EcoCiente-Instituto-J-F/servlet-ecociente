package com.example.ecociente.dao;

import com.example.ecociente.conexao.ConexaoBD;
import com.example.ecociente.model.Usuario;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

public class UsuarioDAO {

    //=======================MÉTODOS CREATE=======================\\

    // Metodo para inserir um usuário no banco
    public boolean inserir(Usuario usuario){
        String sql = """
            INSERT INTO usuario
                (id_usuario, nome, email, senha_hash, data_cadastro, id_endereco, id_tipo_usuario)
            VALUES 
                (?, ?, ?, ?, ?, ?, ?)
            """;

        try (
            Connection conexao = ConexaoBD.conectar();
            PreparedStatement ps =
                    conexao.prepareStatement(sql)
        ){

            ps.setInt(1, usuario.getIdUsuario());
            ps.setString(2, usuario.getNome());
            ps.setString(3, usuario.getEmail());
            ps.setString(4, usuario.getSenhaHash());
            ps.setDate(
                    5,
                    Date.valueOf(LocalDate.now())
                    );
            ps.setInt(6, usuario.getIdEndereco());
            ps.setInt(7, usuario.getIdTipoUsuario());



            int linhasAfetadas = ps.executeUpdate();

            return linhasAfetadas > 0;
        } catch (SQLException sqle) {
            System.out.println(
                    "Erro ao inserir usuario" +
                            sqle.getMessage()
            );

            return false;
        }
    }

    //=======================MÉTODOS READ=======================\

    // Metodo para selecionar um usuário por ID
    public Usuario selecionarUsuarioPorId(int id){
        String sql = """
                SELECT id_usuario, nome, email, senha_hash, data_cadastro, status, id_endereco, id_tipo_usuario
                FROM usuario  
                WHERE id_usuario == ?
                """;
        Usuario retorno = null;
        try (
            Connection conexao = ConexaoBD.conectar();
            PreparedStatement ps =
                    conexao.prepareStatement(sql)
        ){
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                retorno = new Usuario(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDate(5).toLocalDate(),
                        rs.getBoolean(6),
                        rs.getInt(7),
                        rs.getInt(8)
                );
            }


        }catch (SQLException sqle){
            System.out.println("Erro ao selecionar usuario" + sqle.getMessage());

        }finally {
            return retorno;
        }


    }

    //seleciona todos

    ArrayList<Usuario> todosUsuarios = new ArrayList<>();

    public ArrayList<Usuario> selecionarTodos(){
        String sql = """
                SELECT id_usuario, nome, email, senha_hash, data_cadastro, status, id_endereco, id_tipo_usuario
                FROM usuario
                """;
        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps =
                        conexao.prepareStatement(sql)
        ){
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                todosUsuarios.add(new Usuario(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4),
                        rs.getDate(5).toLocalDate(),
                        rs.getBoolean(6),
                        rs.getInt(7),
                        rs.getInt(8)
                ));
            }



        }catch (SQLException sqle){
            System.out.println(sqle.getMessage());

        }finally {
            return todosUsuarios;
        }
    }

    public ArrayList<Usuario> selecionarPorNomeOuEmail(String procura){
        ArrayList<Usuario> encontrados = new ArrayList<>();
        String sql = """
            SELECT id_usuario, nome, email, senha_hash, data_cadastro, status, id_endereco, id_tipo_usuario
            FROM usuario
            WHERE LOWER(nome) LIKE LOWER(?) OR LOWER(email) LIKE LOWER(?)
            ORDER BY nome
            """;
        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps = conexao.prepareStatement(sql)
        ){
            ps.setString(1, "%" + procura + "%");
            ps.setString(2, "%" + procura + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()){
                    encontrados.add(new Usuario(
                            rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                            rs.getDate(5).toLocalDate(), rs.getBoolean(6), rs.getInt(7), rs.getInt(8)
                    ));
                }
            }
        } catch (SQLException sqle){
            System.out.println("Erro ao selecionar usuarios por nome ou email" + sqle.getMessage());
        }
        return encontrados;
    }

    //=======================MÉTODOS UPDATE=======================\
    public boolean atualizar(Usuario usuario){
        boolean retorno = false;
        String sql = """
                UPDATE usuario 
                    SET nome=?, email=?, senha_hash=?, id_endereco=?
                WHERE id_usuario = ?
                """;
        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps =
                        conexao.prepareStatement(sql)
        ){

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getSenhaHash());
            ps.setInt(4, usuario.getIdEndereco());
            ps.setInt(5, usuario.getIdUsuario());

            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas == 1;



        }catch (SQLException sqle){
            System.out.println(sqle.getMessage());
            return retorno;
        }
    }

    //=======================MÉTODOS DELETE=======================\\
    public boolean deletar(Usuario usuario){
        int idUsuario = usuario.getIdUsuario();

        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps = conexao.prepareStatement("DELETE FROM telefone WHERE idUsuario = ?")
        ){
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            System.out.println("Erro ao deletar telefones do usuario" + sqle.getMessage());
            return false;
        }

        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps = conexao.prepareStatement("DELETE FROM cooperativa WHERE id_usuario = ?")
        ){
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            System.out.println("Erro ao deletar cooperativa do usuario" + sqle.getMessage());
            return false;
        }

        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps = conexao.prepareStatement("DELETE FROM sindico WHERE id_usuario = ?")
        ){
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            System.out.println("Erro ao deletar sindico do usuario" + sqle.getMessage());
            return false;
        }

        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps = conexao.prepareStatement("DELETE FROM morador WHERE id_usuario = ?")
        ){
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            System.out.println("Erro ao deletar morador do usuario" + sqle.getMessage());
            return false;
        }

        String sql = """
            DELETE FROM usuario
            WHERE id_usuario = ?
            """;
        try (
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps = conexao.prepareStatement(sql)
        ){
            ps.setInt(1, idUsuario);
            int linhasAfetadas = ps.executeUpdate();
            return linhasAfetadas > 0;
        } catch (SQLException sqle) {
            System.out.println("Erro ao deletar usuario" + sqle.getMessage());
            return false;
        }
    }

}
