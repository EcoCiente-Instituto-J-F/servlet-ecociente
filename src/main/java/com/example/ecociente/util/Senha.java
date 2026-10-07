package com.example.ecociente.util;

import org.mindrot.jbcrypt.BCrypt;

// Metodos para gerar e conferir hash de senha (usado no cadastro e no login)
public class Senha {

    // Gera um hash novo, com salt aleatorio, a partir da senha digitada
    public static String gerarHash(String senha) {
        return BCrypt.hashpw(senha, BCrypt.gensalt());
    }

    // Confere se a senha digitada corresponde ao hash salvo no banco
    public static boolean conferir(String senhaDigitada, String hashSalvo) {
        return BCrypt.checkpw(senhaDigitada, hashSalvo);
    }
}