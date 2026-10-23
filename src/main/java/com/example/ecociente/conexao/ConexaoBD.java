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
            if (input == null) {
                throw new RuntimeException("Arquivo .env não encontrado em src/main/resources");
            }
            env.load(input);
        } catch (IOException ioe) {
            throw new RuntimeException("Erro ao carregar o .env", ioe);
        }
        Runtime.getRuntime().addShutdownHook(new Thread(ConexaoBD::encerrar));
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
            config.setJdbcUrl(env.getProperty("DB_URL"));
            config.setUsername(env.getProperty("DB_USER"));
            config.setPassword(env.getProperty("DB_PASSWORD"));
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