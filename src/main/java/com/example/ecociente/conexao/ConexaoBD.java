package com.example.ecociente.conexao;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConexaoBD {

    private static final Properties env = new Properties();
    private static volatile HikariDataSource pool;

    static {
        try (InputStream input = ConexaoBD.class.getClassLoader().getResourceAsStream(".env")) {
            if (input != null) {
                env.load(input);
            }
        } catch (IOException ioe) {
            throw new RuntimeException("Erro ao carregar o .env", ioe);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(ConexaoBD::encerrar));
    }

    //Variável de ambiente (usada na nuvem) tem prioridade sobre o .env (usado no computador local).
    private static String configuracao(String nome) {
        String valor = System.getenv(nome);
        return (valor != null && !valor.isBlank()) ? valor : env.getProperty(nome);
    }

    public static Connection conectar() throws SQLException {
        HikariDataSource atual = pool;
        if (atual == null) {
            atual = criarPool();
        }
        return atual.getConnection();
    }

    private static synchronized HikariDataSource criarPool() {
        if (pool == null) {
            HikariConfig config = new HikariConfig();
            config.setDriverClassName("org.postgresql.Driver");
            config.setJdbcUrl(configuracao("DB_URL"));
            config.setUsername(configuracao("DB_USER"));
            config.setPassword(configuracao("DB_PASSWORD"));
            config.setMaximumPoolSize(3);
            config.setMinimumIdle(1);
            config.setConnectionTimeout(10000);
            config.setInitializationFailTimeout(-1);
            pool = new HikariDataSource(config);
        }
        return pool;
    }

    public static synchronized void encerrar() {
        if (pool != null) {
            pool.close();
            pool = null;
        }
    }
}