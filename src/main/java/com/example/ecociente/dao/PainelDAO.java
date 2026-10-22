package com.example.ecociente.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.example.ecociente.conexao.ConexaoBD;
import com.example.ecociente.model.Endereco;

// Consultas e operações do painel administrativo. As listagens voltam como Map para a JSP
// ler direto (${u.nome}, ${c.tipo}...) e as escritas que mexem em mais de uma tabela rodam em transação.
public class PainelDAO {

    private static final DateTimeFormatter DATA_BR = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private interface Operacao<T> {
        T executar(Connection conexao) throws SQLException;
    }

    private static <T> T transacao(Operacao<T> operacao) {
        try (Connection conexao = ConexaoBD.conectar()) {
            conexao.setAutoCommit(false);
            try {
                T resultado = operacao.executar(conexao);
                conexao.commit();
                return resultado;
            } catch (SQLException | RuntimeException e) {
                conexao.rollback();
                throw e;
            }
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // ======================= FORMATAÇÃO =======================

    private static String ou(String valor) {
        return valor == null ? "" : valor;
    }

    private static String iniciais(String nome) {
        if (nome == null || nome.isBlank()) return "—";
        String[] partes = nome.trim().split("\\s+");
        String ultima = partes.length > 1 ? partes[partes.length - 1].substring(0, 1) : "";
        return (partes[0].substring(0, 1) + ultima).toUpperCase();
    }

    private static String formatarTelefone(String digitos) {
        if (digitos == null) return "";
        if (digitos.length() == 11) {
            return "(" + digitos.substring(0, 2) + ") " + digitos.substring(2, 7) + "-" + digitos.substring(7);
        }
        if (digitos.length() == 10) {
            return "(" + digitos.substring(0, 2) + ") " + digitos.substring(2, 6) + "-" + digitos.substring(6);
        }
        return digitos;
    }

    private static String formatarCnpj(String c) {
        if (c == null || c.length() != 14) return ou(c);
        return c.substring(0, 2) + "." + c.substring(2, 5) + "." + c.substring(5, 8) + "/" + c.substring(8, 12) + "-" + c.substring(12);
    }

    private static String formatarCep(String c) {
        if (c == null || c.length() != 8) return ou(c);
        return c.substring(0, 5) + "-" + c.substring(5);
    }

    private static void juntarEndereco(Map<String, Object> linha, ResultSet rs) throws SQLException {
        String cidade = ou(rs.getString("cidade"));
        String estado = ou(rs.getString("estado"));
        String rua = ou(rs.getString("rua"));
        String numero = ou(rs.getString("numero"));

        linha.put("idEndereco", rs.getInt("id_endereco"));
        linha.put("cep", formatarCep(rs.getString("cep")));
        linha.put("cidade", cidade);
        linha.put("estado", estado);
        linha.put("bairro", ou(rs.getString("bairro")));
        linha.put("rua", rua);
        linha.put("numero", numero);
        linha.put("complemento", ou(rs.getString("complemento")));
        linha.put("localidade", cidade.isEmpty() ? "" : cidade + ", " + estado);
        linha.put("endereco", rua.isEmpty() ? "" : rua + ", " + numero + " - " + cidade + ", " + estado);
    }

    private static final String COLUNAS_ENDERECO =
            "e.id_endereco, e.cep, e.cidade, e.estado, e.bairro, e.rua, e.numero, e.complemento";

    // ======================= TIPOS (selects) =======================

    public List<Map<String, Object>> listarTiposUsuario() {
        return listarTipos("SELECT id_tipo_usuario, nome_tipo FROM tipo_usuario ORDER BY id_tipo_usuario");
    }

    public List<Map<String, Object>> listarTiposCondominio() {
        return listarTipos("SELECT id_tipo_condominio, nome_tipo FROM tipo_condominio ORDER BY id_tipo_condominio");
    }

    private List<Map<String, Object>> listarTipos(String sql) {
        List<Map<String, Object>> tipos = new ArrayList<>();
        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Map<String, Object> tipo = new LinkedHashMap<>();
                tipo.put("id", rs.getInt(1));
                tipo.put("nome", rs.getString(2));
                tipos.add(tipo);
            }
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
        return tipos;
    }

    // Descobre o id do tipo pelo nome (comparação sem diferenciar maiúsculas); null se não existir
    public Integer idTipoUsuarioPorNome(String nome) {
        String sql = "SELECT id_tipo_usuario FROM tipo_usuario WHERE LOWER(nome_tipo) LIKE LOWER(?) ORDER BY id_tipo_usuario LIMIT 1";
        return idPorTexto(sql, nome + "%");
    }

    public Integer idTipoCondominioPorNome(String nome) {
        String sql = "SELECT id_tipo_condominio FROM tipo_condominio WHERE LOWER(nome_tipo) = LOWER(?)";
        return idPorTexto(sql, nome);
    }

    private Integer idPorTexto(String sql, String texto) {
        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, texto);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getInt(1) : null;
            }
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // ======================= VERIFICAÇÕES DE DUPLICIDADE =======================

    public boolean emailEmUso(String email, int exceptoIdUsuario) {
        return existe("SELECT 1 FROM usuario WHERE LOWER(email) = LOWER(?) AND id_usuario <> ?", email, exceptoIdUsuario);
    }

    public boolean cnpjCondominioEmUso(String cnpj, int exceptoIdCondominio) {
        return existe("SELECT 1 FROM condominio WHERE cnpj = ? AND id_condominio <> ?", cnpj, exceptoIdCondominio);
    }

    public boolean cnpjCooperativaEmUso(String cnpj, int exceptoIdCooperativa) {
        return existe("SELECT 1 FROM cooperativa WHERE cnpj = ? AND id_cooperativa <> ?", cnpj, exceptoIdCooperativa);
    }

    private boolean existe(String sql, String texto, int id) {
        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, texto);
            ps.setInt(2, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    // ======================= ENDEREÇO / TELEFONE (auxiliares) =======================

    private static int inserirEndereco(Connection conexao, Endereco e) throws SQLException {
        String sql = """
                INSERT INTO endereco (cep, cidade, estado, bairro, rua, numero, complemento)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                RETURNING id_endereco
                """;
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, e.getCep());
            ps.setString(2, e.getCidade());
            ps.setString(3, e.getEstado());
            ps.setString(4, e.getBairro());
            ps.setString(5, e.getRua());
            ps.setString(6, String.valueOf(e.getNumero()));
            ps.setString(7, e.getComplemento());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private static void atualizarEndereco(Connection conexao, int idEndereco, Endereco e) throws SQLException {
        String sql = """
                UPDATE endereco
                SET cep = ?, cidade = ?, estado = ?, bairro = ?, rua = ?, numero = ?, complemento = ?
                WHERE id_endereco = ?
                """;
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, e.getCep());
            ps.setString(2, e.getCidade());
            ps.setString(3, e.getEstado());
            ps.setString(4, e.getBairro());
            ps.setString(5, e.getRua());
            ps.setString(6, String.valueOf(e.getNumero()));
            ps.setString(7, e.getComplemento());
            ps.setInt(8, idEndereco);
            ps.executeUpdate();
        }
    }

    // Apaga o endereço só se ninguém mais usa
    private static void apagarEnderecoSolto(Connection conexao, Integer idEndereco) throws SQLException {
        if (idEndereco == null || idEndereco == 0) return;
        String sql = """
                DELETE FROM endereco
                WHERE id_endereco = ?
                  AND NOT EXISTS (SELECT 1 FROM usuario WHERE id_endereco = ?)
                  AND NOT EXISTS (SELECT 1 FROM condominio WHERE id_endereco = ?)
                """;
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, idEndereco);
            ps.setInt(2, idEndereco);
            ps.setInt(3, idEndereco);
            ps.executeUpdate();
        }
    }

    // Atualiza o primeiro telefone do usuário ou cria um se ele não tiver nenhum
    private static void salvarTelefone(Connection conexao, int idUsuario, String digitos) throws SQLException {
        if (digitos == null || digitos.isBlank()) return;
        String atualizar = """
                UPDATE telefone SET numero = ?
                WHERE id_telefone = (SELECT MIN(id_telefone) FROM telefone WHERE id_usuario = ?)
                """;
        try (PreparedStatement ps = conexao.prepareStatement(atualizar)) {
            ps.setString(1, digitos);
            ps.setInt(2, idUsuario);
            if (ps.executeUpdate() > 0) return;
        }
        try (PreparedStatement ps = conexao.prepareStatement("INSERT INTO telefone (numero, id_usuario) VALUES (?, ?)")) {
            ps.setString(1, digitos);
            ps.setInt(2, idUsuario);
            ps.executeUpdate();
        }
    }

    private static Integer idEnderecoDe(Connection conexao, String sql, int id) throws SQLException {
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                int idEndereco = rs.getInt(1);
                return rs.wasNull() ? 0 : idEndereco;
            }
        }
    }

    // ======================= USUÁRIOS =======================

    public List<Map<String, Object>> listarUsuarios(String busca) {
        String sql = """
                SELECT u.id_usuario, u.nome, u.email, u.status, u.id_tipo_usuario, t.nome_tipo,
                       (SELECT numero FROM telefone WHERE id_usuario = u.id_usuario ORDER BY id_telefone LIMIT 1) AS telefone,
                """ + COLUNAS_ENDERECO + """

                FROM usuario u
                JOIN tipo_usuario t ON t.id_tipo_usuario = u.id_tipo_usuario
                LEFT JOIN endereco e ON e.id_endereco = u.id_endereco
                WHERE u.nome ILIKE ? OR u.email ILIKE ?
                ORDER BY u.nome
                """;
        List<Map<String, Object>> linhas = new ArrayList<>();
        String padrao = "%" + ou(busca).trim() + "%";

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, padrao);
            ps.setString(2, padrao);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String nome = rs.getString("nome");
                    String tipo = ou(rs.getString("nome_tipo"));

                    Map<String, Object> linha = new LinkedHashMap<>();
                    linha.put("id", rs.getInt("id_usuario"));
                    linha.put("nome", nome);
                    linha.put("email", rs.getString("email"));
                    linha.put("tipo", tipo);
                    linha.put("tag", tipo.split(" ")[0]);
                    linha.put("idTipoUsuario", rs.getInt("id_tipo_usuario"));
                    linha.put("ativo", rs.getBoolean("status"));
                    linha.put("iniciais", iniciais(nome));
                    linha.put("telefone", formatarTelefone(rs.getString("telefone")));
                    juntarEndereco(linha, rs);
                    linhas.add(linha);
                }
            }
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
        return linhas;
    }

    public void criarUsuario(String nome, String email, String senhaHash, int idTipoUsuario, String telefone, Endereco endereco) {
        transacao(conexao -> {
            Integer idEndereco = endereco == null ? null : inserirEndereco(conexao, endereco);

            String sql = """
                    INSERT INTO usuario (nome, email, senha_hash, id_endereco, id_tipo_usuario)
                    VALUES (?, ?, ?, ?, ?)
                    RETURNING id_usuario
                    """;
            int idUsuario;
            try (PreparedStatement ps = conexao.prepareStatement(sql)) {
                ps.setString(1, nome);
                ps.setString(2, email);
                ps.setString(3, senhaHash);
                if (idEndereco == null) ps.setNull(4, Types.INTEGER); else ps.setInt(4, idEndereco);
                ps.setInt(5, idTipoUsuario);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    idUsuario = rs.getInt(1);
                }
            }
            salvarTelefone(conexao, idUsuario, telefone);
            return null;
        });
    }

    // senhaHash e endereco nulos mantêm o que já está salvo
    public boolean atualizarUsuario(int idUsuario, String nome, String email, String senhaHash, int idTipoUsuario, String telefone, Endereco endereco) {
        return transacao(conexao -> {
            Integer idEndereco = idEnderecoDe(conexao, "SELECT id_endereco FROM usuario WHERE id_usuario = ?", idUsuario);
            if (idEndereco == null) return false;

            if (endereco != null) {
                if (idEndereco == 0) {
                    idEndereco = inserirEndereco(conexao, endereco);
                } else {
                    atualizarEndereco(conexao, idEndereco, endereco);
                }
            }

            String sql = """
                    UPDATE usuario
                    SET nome = ?, email = ?, senha_hash = COALESCE(?, senha_hash), id_tipo_usuario = ?, id_endereco = ?
                    WHERE id_usuario = ?
                    """;
            try (PreparedStatement ps = conexao.prepareStatement(sql)) {
                ps.setString(1, nome);
                ps.setString(2, email);
                ps.setString(3, senhaHash);
                ps.setInt(4, idTipoUsuario);
                if (idEndereco == 0) ps.setNull(5, Types.INTEGER); else ps.setInt(5, idEndereco);
                ps.setInt(6, idUsuario);
                ps.executeUpdate();
            }
            salvarTelefone(conexao, idUsuario, telefone);
            return true;
        });
    }

    // ======================= CONDOMÍNIOS =======================

    public List<Map<String, Object>> listarCondominios(String busca) {
        String sql = """
                SELECT c.id_condominio, c.nome, c.cnpj, c.status, c.id_tipo_condominio, tc.nome_tipo,
                       (SELECT u.nome FROM sindico s JOIN usuario u ON u.id_usuario = s.id_usuario
                        WHERE s.id_condominio = c.id_condominio ORDER BY s.id_sindico LIMIT 1) AS sindico,
                """ + COLUNAS_ENDERECO + """

                FROM condominio c
                LEFT JOIN tipo_condominio tc ON tc.id_tipo_condominio = c.id_tipo_condominio
                JOIN endereco e ON e.id_endereco = c.id_endereco
                WHERE c.nome ILIKE ?
                ORDER BY c.nome
                """;
        List<Map<String, Object>> linhas = new ArrayList<>();

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, "%" + ou(busca).trim() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String nome = rs.getString("nome");
                    String tipo = ou(rs.getString("nome_tipo"));

                    Map<String, Object> linha = new LinkedHashMap<>();
                    linha.put("id", rs.getInt("id_condominio"));
                    linha.put("nome", nome);
                    linha.put("cnpj", formatarCnpj(rs.getString("cnpj")));
                    linha.put("tipo", tipo);
                    linha.put("tag", tipo);
                    linha.put("idTipoCondominio", rs.getInt("id_tipo_condominio"));
                    linha.put("sindico", ou(rs.getString("sindico")));
                    linha.put("ativo", rs.getBoolean("status"));
                    linha.put("iniciais", iniciais(nome));
                    juntarEndereco(linha, rs);
                    linhas.add(linha);
                }
            }
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
        return linhas;
    }

    public void criarCondominio(String nome, String cnpj, int idTipoCondominio, Endereco endereco) {
        transacao(conexao -> {
            int idEndereco = inserirEndereco(conexao, endereco);

            String sql = """
                    INSERT INTO condominio (nome, cnpj, status, id_endereco, id_tipo_condominio)
                    VALUES (?, ?, TRUE, ?, ?)
                    """;
            try (PreparedStatement ps = conexao.prepareStatement(sql)) {
                ps.setString(1, nome);
                ps.setString(2, cnpj);
                ps.setInt(3, idEndereco);
                ps.setInt(4, idTipoCondominio);
                ps.executeUpdate();
            }
            return null;
        });
    }

    public boolean atualizarCondominio(int idCondominio, String nome, String cnpj, int idTipoCondominio, Endereco endereco) {
        return transacao(conexao -> {
            Integer idEndereco = idEnderecoDe(conexao, "SELECT id_endereco FROM condominio WHERE id_condominio = ?", idCondominio);
            if (idEndereco == null) return false;

            atualizarEndereco(conexao, idEndereco, endereco);

            String sql = """
                    UPDATE condominio
                    SET nome = ?, cnpj = ?, id_tipo_condominio = ?
                    WHERE id_condominio = ?
                    """;
            try (PreparedStatement ps = conexao.prepareStatement(sql)) {
                ps.setString(1, nome);
                ps.setString(2, cnpj);
                ps.setInt(3, idTipoCondominio);
                ps.setInt(4, idCondominio);
                ps.executeUpdate();
            }
            return true;
        });
    }

    // Falha (RuntimeException) se o condomínio ainda tem torre, síndico ou parceria
    public boolean excluirCondominio(int idCondominio) {
        return transacao(conexao -> {
            Integer idEndereco = idEnderecoDe(conexao, "SELECT id_endereco FROM condominio WHERE id_condominio = ?", idCondominio);
            if (idEndereco == null) return false;

            try (PreparedStatement ps = conexao.prepareStatement("DELETE FROM condominio WHERE id_condominio = ?")) {
                ps.setInt(1, idCondominio);
                ps.executeUpdate();
            }
            apagarEnderecoSolto(conexao, idEndereco);
            return true;
        });
    }

    // ======================= COOPERATIVAS =======================

    public List<Map<String, Object>> listarCooperativas(String busca) {
        String sql = """
                SELECT c.id_cooperativa, c.cnpj, u.id_usuario, u.nome, u.email, u.status,
                       (SELECT numero FROM telefone WHERE id_usuario = u.id_usuario ORDER BY id_telefone LIMIT 1) AS telefone,
                       (SELECT COUNT(*) FROM parceira p
                        WHERE p.id_cooperativa = c.id_cooperativa
                          AND (p.data_fim IS NULL OR p.data_fim >= CURRENT_DATE)) AS vinculados,
                """ + COLUNAS_ENDERECO + """

                FROM cooperativa c
                JOIN usuario u ON u.id_usuario = c.id_usuario
                LEFT JOIN endereco e ON e.id_endereco = u.id_endereco
                WHERE u.nome ILIKE ?
                ORDER BY u.nome
                """;
        List<Map<String, Object>> linhas = new ArrayList<>();

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, "%" + ou(busca).trim() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String nome = rs.getString("nome");
                    boolean ativa = rs.getBoolean("status");

                    Map<String, Object> linha = new LinkedHashMap<>();
                    linha.put("id", rs.getInt("id_cooperativa"));
                    linha.put("idUsuario", rs.getInt("id_usuario"));
                    linha.put("nome", nome);
                    linha.put("responsavel", nome);
                    linha.put("email", rs.getString("email"));
                    linha.put("cnpj", formatarCnpj(rs.getString("cnpj")));
                    linha.put("condominiosVinculados", rs.getInt("vinculados"));
                    linha.put("ativa", ativa);
                    linha.put("tag", ativa ? "Ativa" : "Inativa");
                    linha.put("iniciais", iniciais(nome));
                    linha.put("telefone", formatarTelefone(rs.getString("telefone")));
                    juntarEndereco(linha, rs);
                    linhas.add(linha);
                }
            }
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
        return linhas;
    }

    public void criarCooperativa(String responsavel, String email, String senhaHash, String cnpj, boolean ativa,
                                 int idTipoUsuario, String telefone, Endereco endereco) {
        transacao(conexao -> {
            Integer idEndereco = endereco == null ? null : inserirEndereco(conexao, endereco);

            String sqlUsuario = """
                    INSERT INTO usuario (nome, email, senha_hash, status, id_endereco, id_tipo_usuario)
                    VALUES (?, ?, ?, ?, ?, ?)
                    RETURNING id_usuario
                    """;
            int idUsuario;
            try (PreparedStatement ps = conexao.prepareStatement(sqlUsuario)) {
                ps.setString(1, responsavel);
                ps.setString(2, email);
                ps.setString(3, senhaHash);
                ps.setBoolean(4, ativa);
                if (idEndereco == null) ps.setNull(5, Types.INTEGER); else ps.setInt(5, idEndereco);
                ps.setInt(6, idTipoUsuario);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    idUsuario = rs.getInt(1);
                }
            }

            try (PreparedStatement ps = conexao.prepareStatement("INSERT INTO cooperativa (cnpj, id_usuario) VALUES (?, ?)")) {
                ps.setString(1, cnpj);
                ps.setInt(2, idUsuario);
                ps.executeUpdate();
            }
            salvarTelefone(conexao, idUsuario, telefone);
            return null;
        });
    }

    // email, senhaHash, telefone e endereco nulos mantêm o que já está salvo
    public boolean atualizarCooperativa(int idCooperativa, String responsavel, String cnpj, boolean ativa,
                                        String email, String senhaHash, String telefone, Endereco endereco) {
        return transacao(conexao -> {
            int idUsuario;
            try (PreparedStatement ps = conexao.prepareStatement("SELECT id_usuario FROM cooperativa WHERE id_cooperativa = ?")) {
                ps.setInt(1, idCooperativa);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) return false;
                    idUsuario = rs.getInt(1);
                }
            }

            Integer idEndereco = idEnderecoDe(conexao, "SELECT id_endereco FROM usuario WHERE id_usuario = ?", idUsuario);
            if (endereco != null) {
                if (idEndereco == null || idEndereco == 0) {
                    idEndereco = inserirEndereco(conexao, endereco);
                } else {
                    atualizarEndereco(conexao, idEndereco, endereco);
                }
            }

            String sqlUsuario = """
                    UPDATE usuario
                    SET nome = ?, status = ?, email = COALESCE(?, email), senha_hash = COALESCE(?, senha_hash), id_endereco = ?
                    WHERE id_usuario = ?
                    """;
            try (PreparedStatement ps = conexao.prepareStatement(sqlUsuario)) {
                ps.setString(1, responsavel);
                ps.setBoolean(2, ativa);
                ps.setString(3, email);
                ps.setString(4, senhaHash);
                if (idEndereco == null || idEndereco == 0) ps.setNull(5, Types.INTEGER); else ps.setInt(5, idEndereco);
                ps.setInt(6, idUsuario);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = conexao.prepareStatement("UPDATE cooperativa SET cnpj = ? WHERE id_cooperativa = ?")) {
                ps.setString(1, cnpj);
                ps.setInt(2, idCooperativa);
                ps.executeUpdate();
            }
            salvarTelefone(conexao, idUsuario, telefone);
            return true;
        });
    }

    // Remove a cooperativa, as parcerias dela e o usuário dono, se ele não tiver outro papel (síndico/morador)
    public boolean excluirCooperativa(int idCooperativa) {
        return transacao(conexao -> {
            int idUsuario;
            try (PreparedStatement ps = conexao.prepareStatement("SELECT id_usuario FROM cooperativa WHERE id_cooperativa = ?")) {
                ps.setInt(1, idCooperativa);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) return false;
                    idUsuario = rs.getInt(1);
                }
            }
            Integer idEndereco = idEnderecoDe(conexao, "SELECT id_endereco FROM usuario WHERE id_usuario = ?", idUsuario);

            executar(conexao, "DELETE FROM parceira WHERE id_cooperativa = ?", idCooperativa);
            executar(conexao, "DELETE FROM cooperativa WHERE id_cooperativa = ?", idCooperativa);

            String semOutroPapel = """
                    NOT EXISTS (SELECT 1 FROM sindico WHERE id_usuario = ?)
                    AND NOT EXISTS (SELECT 1 FROM morador WHERE id_usuario = ?)
                    AND NOT EXISTS (SELECT 1 FROM cooperativa WHERE id_usuario = ?)
                    """;
            boolean removeUsuario;
            try (PreparedStatement ps = conexao.prepareStatement("SELECT " + semOutroPapel.replace("\n", " "))) {
                ps.setInt(1, idUsuario);
                ps.setInt(2, idUsuario);
                ps.setInt(3, idUsuario);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    removeUsuario = rs.getBoolean(1);
                }
            }
            if (removeUsuario) {
                executar(conexao, "DELETE FROM telefone WHERE id_usuario = ?", idUsuario);
                executar(conexao, "DELETE FROM usuario WHERE id_usuario = ?", idUsuario);
                apagarEnderecoSolto(conexao, idEndereco);
            }
            return true;
        });
    }

    private static void executar(Connection conexao, String sql, int id) throws SQLException {
        try (PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // ======================= VÍNCULOS (tabela parceira) =======================

    public List<Map<String, Object>> listarVinculos(String busca) {
        String sql = """
                SELECT p.id_parceira, p.data_inicio, p.data_fim, p.id_condominio, p.id_cooperativa,
                       c.nome AS condominio, u.nome AS cooperativa
                FROM parceira p
                JOIN condominio c ON c.id_condominio = p.id_condominio
                JOIN cooperativa co ON co.id_cooperativa = p.id_cooperativa
                JOIN usuario u ON u.id_usuario = co.id_usuario
                WHERE c.nome ILIKE ? OR u.nome ILIKE ?
                ORDER BY p.id_parceira
                """;
        List<Map<String, Object>> linhas = new ArrayList<>();
        String padrao = "%" + ou(busca).trim() + "%";

        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            ps.setString(1, padrao);
            ps.setString(2, padrao);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Date inicio = rs.getDate("data_inicio");
                    Date fim = rs.getDate("data_fim");

                    Map<String, Object> linha = new LinkedHashMap<>();
                    linha.put("id", rs.getInt("id_parceira"));
                    linha.put("idCondominio", rs.getInt("id_condominio"));
                    linha.put("idCooperativa", rs.getInt("id_cooperativa"));
                    linha.put("condominio", rs.getString("condominio"));
                    linha.put("cooperativa", rs.getString("cooperativa"));
                    linha.put("inicio", inicio == null ? "" : inicio.toLocalDate().format(DATA_BR));
                    linha.put("fim", fim == null ? "" : fim.toLocalDate().format(DATA_BR));
                    linha.put("inicioIso", inicio == null ? "" : inicio.toString());
                    linha.put("fimIso", fim == null ? "" : fim.toString());
                    linhas.add(linha);
                }
            }
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
        return linhas;
    }

    public Map<String, Object> resumoVinculos() {
        String sql = """
                SELECT (SELECT COUNT(*) FROM parceira WHERE data_fim IS NULL OR data_fim >= CURRENT_DATE),
                       (SELECT COUNT(*) FROM condominio c WHERE NOT EXISTS (
                            SELECT 1 FROM parceira p WHERE p.id_condominio = c.id_condominio
                              AND (p.data_fim IS NULL OR p.data_fim >= CURRENT_DATE)))
                """;
        Map<String, Object> resumo = new LinkedHashMap<>();
        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            resumo.put("vinculosAtivos", rs.getInt(1));
            resumo.put("semCooperativa", rs.getInt(2));
            resumo.put("coletasMes", 0);
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
        return resumo;
    }

    // Procura pelo nome exato (sem diferenciar maiúsculas); null se não achar
    public Integer idCondominioPorNome(String nome) {
        return idPorTexto("SELECT id_condominio FROM condominio WHERE LOWER(nome) = LOWER(?) ORDER BY id_condominio LIMIT 1", nome);
    }

    public Integer idCooperativaPorNome(String nome) {
        String sql = """
                SELECT c.id_cooperativa FROM cooperativa c
                JOIN usuario u ON u.id_usuario = c.id_usuario
                WHERE LOWER(u.nome) = LOWER(?) ORDER BY c.id_cooperativa LIMIT 1
                """;
        return idPorTexto(sql, nome);
    }

    public void criarVinculo(int idCondominio, int idCooperativa, LocalDate inicio, LocalDate fim) {
        String sql = """
                INSERT INTO parceira (data_inicio, data_fim, id_condominio, id_cooperativa)
                VALUES (COALESCE(?, CURRENT_DATE), ?, ?, ?)
                """;
        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            setData(ps, 1, inicio);
            setData(ps, 2, fim);
            ps.setInt(3, idCondominio);
            ps.setInt(4, idCooperativa);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    public boolean atualizarVinculo(int idVinculo, int idCondominio, int idCooperativa, LocalDate inicio, LocalDate fim) {
        String sql = """
                UPDATE parceira
                SET data_inicio = COALESCE(?, data_inicio), data_fim = ?, id_condominio = ?, id_cooperativa = ?
                WHERE id_parceira = ?
                """;
        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql)) {
            setData(ps, 1, inicio);
            setData(ps, 2, fim);
            ps.setInt(3, idCondominio);
            ps.setInt(4, idCooperativa);
            ps.setInt(5, idVinculo);
            return ps.executeUpdate() == 1;
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    public boolean excluirVinculo(int idVinculo) {
        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement("DELETE FROM parceira WHERE id_parceira = ?")) {
            ps.setInt(1, idVinculo);
            return ps.executeUpdate() == 1;
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    private static void setData(PreparedStatement ps, int indice, LocalDate data) throws SQLException {
        if (data == null) ps.setNull(indice, Types.DATE); else ps.setDate(indice, Date.valueOf(data));
    }

    // ======================= PAINEL (home) =======================

    private static final double[] X_MESES = {211.6, 308.6, 405.9, 502.9, 600.1, 694.7, 790.3};
    private static final double Y_TOPO = 442.5;
    private static final double Y_BASE = 613.4;

    private int contar(String sql) {
        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            rs.next();
            return rs.getInt(1);
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
    }

    private Map<String, Object> kpi(String titulo, String icone, int valor, Integer atual, Integer anterior) {
        Map<String, Object> k = new LinkedHashMap<>();
        k.put("titulo", titulo);
        k.put("icone", icone);
        k.put("valor", valor);
        if (atual == null || anterior == null) {
            k.put("tendencia", "alta");
            k.put("variacao", "—");
        } else {
            int pct = anterior == 0 ? (atual > 0 ? 100 : 0) : (atual - anterior) * 100 / anterior;
            k.put("tendencia", pct >= 0 ? "alta" : "baixa");
            k.put("variacao", Math.abs(pct) + "% vs mês anterior");
        }
        return k;
    }

    // Cadastros por mês (últimos 7) em cada tipo de conta
    private int[] cadastrosPorMes(String filtro) {
        String sql = "SELECT to_char(u.data_cadastro, 'YYYY-MM') AS mes, COUNT(*) FROM usuario u "
                + "WHERE u.data_cadastro >= date_trunc('month', CURRENT_DATE) - INTERVAL '6 months' " + filtro
                + " GROUP BY 1";
        int[] valores = new int[7];
        YearMonth atual = YearMonth.now();
        try (Connection conexao = ConexaoBD.conectar();
             PreparedStatement ps = conexao.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                YearMonth mes = YearMonth.parse(rs.getString(1));
                int indice = 6 - (int) java.time.temporal.ChronoUnit.MONTHS.between(mes, atual);
                if (indice >= 0 && indice < 7) valores[indice] = rs.getInt(2);
            }
        } catch (SQLException sqle) {
            throw new RuntimeException(sqle.getMessage());
        }
        return valores;
    }

    private static String pontos(int[] valores, int maximo) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < valores.length; i++) {
            if (i > 0) sb.append(' ');
            sb.append(X_MESES[i]).append(',').append(y(valores[i], maximo));
        }
        return sb.toString();
    }

    private static String y(int valor, int maximo) {
        return String.format(java.util.Locale.ROOT, "%.1f", Y_BASE - (Y_BASE - Y_TOPO) * valor / maximo);
    }

    // Rótulos dos 7 meses do gráfico, do mais antigo ao atual (ex.: Abr, Mai ... Out)
    private static List<String> mesesGrafico() {
        String[] nomes = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
        List<String> meses = new ArrayList<>();
        YearMonth atual = YearMonth.now();
        for (int i = 6; i >= 0; i--) {
            meses.add(nomes[atual.minusMonths(i).getMonthValue() - 1]);
        }
        return meses;
    }

    public Map<String, Object> dadosPainel() {
        Map<String, Object> dados = new LinkedHashMap<>();

        String mesAtual = "date_trunc('month', %s) = date_trunc('month', CURRENT_DATE)";
        String mesAnterior = "date_trunc('month', %s) = date_trunc('month', CURRENT_DATE - INTERVAL '1 month')";

        int usuariosAtual = contar("SELECT COUNT(*) FROM usuario WHERE " + mesAtual.formatted("data_cadastro"));
        int usuariosAnterior = contar("SELECT COUNT(*) FROM usuario WHERE " + mesAnterior.formatted("data_cadastro"));
        int coopAtual = contar("SELECT COUNT(*) FROM cooperativa c JOIN usuario u ON u.id_usuario = c.id_usuario WHERE " + mesAtual.formatted("u.data_cadastro"));
        int coopAnterior = contar("SELECT COUNT(*) FROM cooperativa c JOIN usuario u ON u.id_usuario = c.id_usuario WHERE " + mesAnterior.formatted("u.data_cadastro"));
        int parcAtual = contar("SELECT COUNT(*) FROM parceira WHERE " + mesAtual.formatted("data_inicio"));
        int parcAnterior = contar("SELECT COUNT(*) FROM parceira WHERE " + mesAnterior.formatted("data_inicio"));

        List<Map<String, Object>> kpis = new ArrayList<>();
        kpis.add(kpi("Usuários", "kpi-usuarios", contar("SELECT COUNT(*) FROM usuario"), usuariosAtual, usuariosAnterior));
        kpis.add(kpi("Condomínios", "kpi-condominios", contar("SELECT COUNT(*) FROM condominio"), null, null));
        kpis.add(kpi("Cooperativas", "kpi-cooperativas", contar("SELECT COUNT(*) FROM cooperativa"), coopAtual, coopAnterior));
        kpis.add(kpi("Parcerias ativas", "kpi-parcerias",
                contar("SELECT COUNT(*) FROM parceira WHERE data_fim IS NULL OR data_fim >= CURRENT_DATE"), parcAtual, parcAnterior));
        dados.put("kpis", kpis);

        String ehSindico = "AND EXISTS (SELECT 1 FROM sindico s WHERE s.id_usuario = u.id_usuario)";
        String ehCooperativa = "AND EXISTS (SELECT 1 FROM cooperativa c WHERE c.id_usuario = u.id_usuario)";
        int[] sindicos = cadastrosPorMes(ehSindico);
        int[] cooperativas = cadastrosPorMes(ehCooperativa);
        int[] usuarios = cadastrosPorMes("AND NOT EXISTS (SELECT 1 FROM sindico s WHERE s.id_usuario = u.id_usuario) "
                + "AND NOT EXISTS (SELECT 1 FROM cooperativa c WHERE c.id_usuario = u.id_usuario)");

        int maximo = 1;
        int total = 0;
        for (int i = 0; i < 7; i++) {
            maximo = Math.max(maximo, Math.max(usuarios[i], Math.max(sindicos[i], cooperativas[i])));
            total += usuarios[i] + sindicos[i] + cooperativas[i];
        }

        String linhaUsuarios = pontos(usuarios, maximo);
        dados.put("totalCadastros", total);
        dados.put("pontosUsuarios", linhaUsuarios);
        dados.put("pontosSindicos", pontos(sindicos, maximo));
        dados.put("pontosCooperativas", pontos(cooperativas, maximo));
        dados.put("areaUsuarios", X_MESES[0] + "," + Y_BASE + " " + linhaUsuarios + " " + X_MESES[6] + "," + Y_BASE);
        dados.put("ultimoUsuarios", X_MESES[6] + "," + y(usuarios[6], maximo));
        dados.put("ultimoSindicos", X_MESES[6] + "," + y(sindicos[6], maximo));
        dados.put("ultimoCooperativas", X_MESES[6] + "," + y(cooperativas[6], maximo));
        dados.put("mesesGrafico", mesesGrafico());
        dados.put("categorias", new ArrayList<>());
        return dados;
    }
}
