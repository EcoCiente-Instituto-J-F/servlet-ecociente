package com.example.ecociente.util;

// Erro de validação com mensagem pronta para mostrar ao usuário
public class DadoInvalido extends RuntimeException {

    //================ METODOS CONSTRUTORES ================

    // Guarda a mensagem que os servlets mostram na tela
    public DadoInvalido(String mensagem) {
        super(mensagem);
    }
}
