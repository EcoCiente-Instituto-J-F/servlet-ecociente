package com.example.ecociente.conexao;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexaoBD {

    private static final Properties env = new Properties();

    static {
        try (InputStream input = ConexaoBD.class.getClassLoader().getResourceAsStream(".env")) {
            if (input == null) {
                throw new RuntimeException("Arquivo .env não encontrado em src/main/resources");
            }
            env.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar o .env", e);
        }
    }

    private static final String URL = env.getProperty("DB_URL");
    private static final String USUARIO = env.getProperty("DB_USER");
    private static final String SENHA = env.getProperty("DB_PASSWORD");

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
