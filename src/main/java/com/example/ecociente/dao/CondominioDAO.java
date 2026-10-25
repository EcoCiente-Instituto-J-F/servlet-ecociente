package com.example.ecociente.dao;

import com.example.ecociente.conexao.ConexaoBD;
import com.example.ecociente.model.Condominio;
import com.example.ecociente.model.Endereco;
import com.example.ecociente.model.TipoCondominio;

import java.security.SecureRandom;
import java.sql.*;
import java.util.ArrayList;

public class CondominioDAO{

    //=======================MÉTODOS CREATE=======================\\
    public boolean inserir(Condominio condominio){
        boolean retorno = false;
        String sql = """
                INSERT INTO condominio
                    (nome, cnpj, status, token, id_endereco, id_tipo_condominio)
                VALUES
                    (?,?,?,?,?,?)
                """;
        try(Connection conexao = ConexaoBD.conectar();
            PreparedStatement ps = conexao.prepareStatement(sql)){
            ps.setString(1, condominio.getNome());
            ps.setString(2, condominio.getCnpj());
            ps.setBoolean(3, condominio.isStatus());
            ps.setString(4, condominio.getToken());
            ps.setInt(5, condominio.idEndereco());
            ps.setInt(6, condominio.getIdTipoCondominio());

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
                SELECT id_condominio, nome, cnpj, status, token, id_endereco, id_tipo_condominio
                FROM condominio
                ORDER BY nome
                """;
        try(Connection conexao = ConexaoBD.conectar();
            PreparedStatement ps = conexao.prepareStatement(sql);
            ResultSet condominio = ps.executeQuery()){

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
                SELECT id_condominio, nome, cnpj, status, token, id_endereco, id_tipo_condominio, numero_unidades, area_m2
                FROM condominio
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
                condominioEncontrado.setNumeroUnidades(condominio.getObject(8, Integer.class));
                condominioEncontrado.setAreaM2(condominio.getBigDecimal(9));
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

    public boolean atualizarStatus(int id, boolean status){
        boolean retorno = false;
        String sql = """
                UPDATE condominio SET status = ?
                    WHERE id_condominio = ?
                """;
        try(Connection conexao = ConexaoBD.conectar()){
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setBoolean(1, status);
            ps.setInt(2, id);

            retorno = ps.executeUpdate() >= 1;
        }
        catch(SQLException sqle){
            throw new RuntimeException(sqle.getMessage());
        }
        return retorno;
    } // Metodo que ativa ou desativa o condomínio por id

    //=======================MÉTODOS DELETE=======================\\
    public boolean deletar(int id){
        boolean retorno = false;
        String sql = """
                DELETE FROM condominio
                       WHERE id_condominio = ?
                """;
        try(Connection conexao = ConexaoBD.conectar()){
            PreparedStatement ps = conexao.prepareStatement(sql);
            ps.setInt(1, id);

            retorno = ps.executeUpdate() == 1;
        }
        catch(SQLException sqle){
            throw new RuntimeException(sqle.getMessage());
        }
        return retorno;
    } // Metodo que deleta um condomínio por id

    //=======================MÉTODOS DO CRUD DE ADMIN (JOIN e transação)=======================\\

    // Lista para a tela de condomínios: traz o nome do tipo, o nome do síndico e o endereço
    public ArrayList<Condominio> selecionarTodos(String busca){
        ArrayList<Condominio> condominios = new ArrayList<>();
        String sql = """
                SELECT c.id_condominio, c.nome, c.cnpj, c.status, c.token, c.id_endereco, c.id_tipo_condominio, tc.nome_tipo,
                       (SELECT u.nome FROM sindico s JOIN usuario u ON u.id_usuario = s.id_usuario
                        WHERE s.id_condominio = c.id_condominio ORDER BY s.id_sindico LIMIT 1),
                       e.cep, e.cidade, e.estado, e.bairro, e.rua, e.numero, e.complemento,
                       c.numero_unidades, c.area_m2
                FROM condominio c
                LEFT JOIN tipo_condominio tc ON tc.id_tipo_condominio = c.id_tipo_condominio
                JOIN endereco e ON e.id_endereco = c.id_endereco
                WHERE c.nome ILIKE ?
                ORDER BY c.nome
                """;
        try(
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps = conexao.prepareStatement(sql)
        ){
            ps.setString(1, "%" + (busca == null ? "" : busca.trim()) + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    Condominio condominio = new Condominio(
                            rs.getInt(1), rs.getString(2), Condominio.formatarCnpj(rs.getString(3)), rs.getBoolean(4),
                            rs.getString(5), rs.getInt(6), rs.getInt(7)
                    );
                    condominio.setDadosExibicao(rs.getString(8), rs.getString(9), EnderecoDAO.deLinha(rs, rs.getInt(6), 10));
                    condominio.setNumeroUnidades(rs.getObject(17, Integer.class));
                    condominio.setAreaM2(rs.getBigDecimal(18));
                    condominios.add(condominio);
                }
            }
        }
        catch(SQLException sqle){
            throw new RuntimeException(sqle.getMessage());
        }
        return condominios;
    }

    // Tipos de condomínio para o select do formulário
    public ArrayList<TipoCondominio> selecionarTipos(){
        ArrayList<TipoCondominio> tipos = new ArrayList<>();
        String sql = "SELECT id_tipo_condominio, nome_tipo, descricao FROM tipo_condominio ORDER BY id_tipo_condominio";
        try(
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps = conexao.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ){
            while(rs.next()){
                tipos.add(new TipoCondominio(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
        }
        catch(SQLException sqle){
            throw new RuntimeException(sqle.getMessage());
        }
        return tipos;
    }

    // Verifica se o CNPJ já é de outro condomínio (o exceptoId é o próprio condomínio na edição, 0 no cadastro)
    public boolean existeCnpj(String cnpj, int exceptoId){
        String sql = "SELECT 1 FROM condominio WHERE cnpj = ? AND id_condominio <> ?";
        try(
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps = conexao.prepareStatement(sql)
        ){
            ps.setString(1, cnpj);
            ps.setInt(2, exceptoId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
        catch(SQLException sqle){
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // Gera o token do condomínio: 6 dígitos aleatórios que ainda não são de nenhum outro condomínio
    public String gerarTokenUnico(){
        String caracteres = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
        SecureRandom sorteio = new SecureRandom();
        String sql = "SELECT 1 FROM condominio WHERE token = ?";
        try(
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps = conexao.prepareStatement(sql)
        ){
            while(true){
                StringBuilder token = new StringBuilder();
                for(int i = 0; i < 6; i++){
                    token.append(caracteres.charAt(sorteio.nextInt(caracteres.length())));
                }
                ps.setString(1, token.toString());
                try(ResultSet rs = ps.executeQuery()){
                    if(!rs.next()) return token.toString();
                }
            }
        }
        catch(SQLException sqle){
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // Cadastra o endereço e o condomínio na mesma transação
    public boolean inserir(Condominio condominio, Endereco endereco){
        try(Connection conexao = ConexaoBD.conectar()){
            conexao.setAutoCommit(false);
            try{
                int idEndereco = EnderecoDAO.inserirNaTransacao(conexao, endereco);

                String sql = """
                        INSERT INTO condominio (nome, cnpj, status, token, id_endereco, id_tipo_condominio, numero_unidades, area_m2)
                        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                        """;
                int linhas;
                try(PreparedStatement ps = conexao.prepareStatement(sql)){
                    ps.setString(1, condominio.getNome());
                    ps.setString(2, condominio.getCnpj());
                    ps.setBoolean(3, condominio.isStatus());
                    ps.setString(4, condominio.getToken());
                    ps.setInt(5, idEndereco);
                    ps.setInt(6, condominio.getIdTipoCondominio());
                    ps.setObject(7, condominio.getNumeroUnidades(), Types.INTEGER);
                    ps.setBigDecimal(8, condominio.getAreaM2());
                    linhas = ps.executeUpdate();
                }
                conexao.commit();
                return linhas == 1;
            }
            catch(SQLException sqle){
                conexao.rollback();
                throw sqle;
            }
        }
        catch(SQLException sqle){
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // Atualiza o endereço e o condomínio na mesma transação
    public boolean atualizarCondominio(Condominio condominio, Endereco endereco){
        try(Connection conexao = ConexaoBD.conectar()){
            conexao.setAutoCommit(false);
            try{
                EnderecoDAO.atualizarNaTransacao(conexao, condominio.idEndereco(), endereco);

                String sql = """
                        UPDATE condominio
                        SET nome = ?, cnpj = ?, status = ?, token = ?, id_tipo_condominio = ?, numero_unidades = ?, area_m2 = ?
                        WHERE id_condominio = ?
                        """;
                int linhas;
                try(PreparedStatement ps = conexao.prepareStatement(sql)){
                    ps.setString(1, condominio.getNome());
                    ps.setString(2, condominio.getCnpj());
                    ps.setBoolean(3, condominio.isStatus());
                    ps.setString(4, condominio.getToken());
                    ps.setInt(5, condominio.getIdTipoCondominio());
                    ps.setObject(6, condominio.getNumeroUnidades(), Types.INTEGER);
                    ps.setBigDecimal(7, condominio.getAreaM2());
                    ps.setInt(8, condominio.getIdCondominio());
                    linhas = ps.executeUpdate();
                }
                conexao.commit();
                return linhas == 1;
            }
            catch(SQLException sqle){
                conexao.rollback();
                throw sqle;
            }
        }
        catch(SQLException sqle){
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // Apaga o condomínio e o endereço dele (se ninguém mais usa). Falha se ainda tem morador, síndico ou parceria
    public boolean deletarComEndereco(int id){
        try(Connection conexao = ConexaoBD.conectar()){
            conexao.setAutoCommit(false);
            try{
                int idEndereco = 0;
                try(PreparedStatement ps = conexao.prepareStatement("SELECT id_endereco FROM condominio WHERE id_condominio = ?")){
                    ps.setInt(1, id);
                    try(ResultSet rs = ps.executeQuery()){
                        if(!rs.next()) return false;
                        idEndereco = rs.getInt(1);
                    }
                }

                int linhas;
                try(PreparedStatement ps = conexao.prepareStatement("DELETE FROM condominio WHERE id_condominio = ?")){
                    ps.setInt(1, id);
                    linhas = ps.executeUpdate();
                }
                EnderecoDAO.apagarSeSolto(conexao, idEndereco);
                conexao.commit();
                return linhas == 1;
            }
            catch(SQLException sqle){
                conexao.rollback();
                throw sqle;
            }
        }
        catch(SQLException sqle){
            throw new RuntimeException(sqle.getMessage());
        }
    }

    public int contar(){
        String sql = "SELECT COUNT(*) FROM condominio";
        try(
                Connection conexao = ConexaoBD.conectar();
                PreparedStatement ps = conexao.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ){
            rs.next();
            return rs.getInt(1);
        }
        catch(SQLException sqle){
            throw new RuntimeException(sqle.getMessage());
        }
    }
}
