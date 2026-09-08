package com.example.ecociente;

public class Main {
    public static void main(String[] args) {
        try (var conn = com.example.ecociente.conexao.ConexaoBD.conectar()) {
            System.out.println("Conectado com sucesso ao Aiven!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
