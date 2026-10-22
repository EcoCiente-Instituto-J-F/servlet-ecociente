package com.example.ecociente.util;

// Erro de validação com mensagem pronta para mostrar ao usuário
public class DadoInvalido extends RuntimeException {

    public DadoInvalido(String mensagem) {
        super(mensagem);
    }
}
