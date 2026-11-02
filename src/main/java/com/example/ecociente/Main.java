package com.example.ecociente;

// Teste rápido de conexão com o banco, rodado fora do Tomcat
public class Main {

    //================ EXECUCAO ================

    // Abre e fecha uma conexão; se der erro, mostra o motivo no console
    public static void main(String[] args) {
        try (var conn = com.example.ecociente.conexao.ConexaoBD.conectar()) {
            System.out.println("Conectado com sucesso ao Aiven!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
