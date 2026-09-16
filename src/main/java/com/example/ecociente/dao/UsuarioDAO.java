package com.example.ecociente.dao;

import com.example.ecociente.conexao.ConexaoBD;
import com.example.ecociente.model.Usuario;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class UsuarioDAO {

    //=======================MÉTODOS CREATE=======================\\

    public boolean inserir(Usuario usuario){
        String sql = """
            INSERT INTO usuario
                (id_usuario, nome, email, senha_hash, data_cadastro, id_endereco, id_tipo_usuario)
            VALUES 
                (?, ?, ?, ?, ?, ?, ?)
            """;

        try (
            Connection conexao = ConexaoBD.conectar();
            PreparedStatement comando =
                    conexao.prepareStatement(sql);
        ){

            comando.setInt(1, usuario.getIdUsuario());
            comando.setString(2, usuario.getNome());
            comando.setString(3, usuario.getEmail());
            comando.setString(4, usuario.getSenhaHash());
            comando.setDate(
                    5,
                    Date.valueOf(LocalDate.now())
                    );
            comando.setInt(6, usuario.getIdEndereco());
            comando.setInt(7, usuario.getIdTipoUsuario());



            int linhasAfetadas = comando.executeUpdate();

            return linhasAfetadas > 0;
        } catch (SQLException sqle) {
            System.out.println(
                    "Erro ao inserir usuario" +
                            sqle.getMessage()
            );

            return false;
        }
    }

    //=======================MÉTODOS DELETE=======================\\
    public boolean deletar(Usuario usuario){

        String sql = """
                DELETE FROM usuario
                WHERE id_usuario = ?
                """;

        try (
            Connection conexao = ConexaoBD.conectar();
            PreparedStatement comando =
                    conexao.prepareStatement(sql);

        ){
            comando.setInt(1, usuario.getIdUsuario());

            int linhasAfetadas = comando.executeUpdate();

            return linhasAfetadas > 0;
        } catch (SQLException sqle) {
            System.out.println(
                    "Erro ao deletar usuario" +
                            sqle.getMessage()
            );

            return false;
        }


    }

}
