package com.example.ecociente.model;

public class Telefone {

    //================ ATRIBUTOS ================

    private int idTelefone;
    private String numero;
    private int idUsuario;

    //================ METODOS CONSTRUTORES ================

    // Construtor completo
    public Telefone(int idTelefone, String numero, int idUsuario) {
        this.idTelefone = idTelefone;
        this.numero = numero;
        this.idUsuario = idUsuario;
    }

    // Construtor sem usuario
    public Telefone(int idTelefone, String numero) {
        this.idTelefone = idTelefone;
        this.numero = numero;
    }

    //================ METODOS GETTERS ================

    public int getIdTelefone() {
        return idTelefone;
    }

    public String getNumero() {
        return numero;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    //================ METODOS SETTERS ================

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public void setUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    //================ TO STRING ================
    @Override

    public String toString(){
        return """
                Telefone { id: %d | numero: %s | id usuario: %d }
                """.formatted(this.idTelefone, this.numero, this.idUsuario);
    }
}
