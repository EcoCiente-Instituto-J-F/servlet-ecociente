package com.example.ecociente.model;

public class Endereco {

    //================ ATRIBUTOS ================

    private int idEndereco;
    private String cep;
    private String cidade;
    private String estado;
    private String bairro;
    private String rua;
    private int numero;
    private String complemento;


    //================ METODOS CONSTRUTORES ================

    // Construtor completo

    public Endereco(int idEndereco, String cep, String cidade, String estado, String bairro, String rua, int numero, String complemento) {
        this.idEndereco = idEndereco;
        this.cep = cep;
        this.cidade = cidade;
        this.estado = estado;
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
        this.complemento = complemento;
    }


    // Construtor sem complemento

    public Endereco(int idEndereco, String cep, String cidade, String estado, String bairro, String rua, int numero) {
        this.idEndereco = idEndereco;
        this.cep = cep;
        this.cidade = cidade;
        this.estado = estado;
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
    }

    // Construtor sem cep

    public Endereco(int idEndereco, String cidade, String estado, String bairro, String rua, int numero, String complemento) {
        this.idEndereco = idEndereco;
        this.cidade = cidade;
        this.estado = estado;
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
        this.complemento = complemento;
    }

    // Construtor sem cep e sem complemento

    public Endereco(int idEndereco, String cidade, String estado, String bairro, String rua, int numero) {
        this.idEndereco = idEndereco;
        this.cidade = cidade;
        this.estado = estado;
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
    }


    // ================ METODOS GETTERS ================

    public int getIdEndereco() {
        return idEndereco;
    }

    public String getCep() {
        return cep;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public String getBairro() {
        return bairro;
    }

    public String getRua() {
        return rua;
    }

    public int getNumero() {
        return numero;
    }

    public String getComplemento() {
        return complemento;
    }


    // ================ METODOS SETTERS ================

    public void setCep(String cep) {
        this.cep = cep;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }


    // ================ TO STRING ================
    @Override

    public String toString(){
        return """
                Endereco { id: %d | cep: %s | cidade: %s | estado: %s | bairro: %s | rua: %s | numero: %s | complemento %s }
                """.formatted(this.idEndereco, this.cep, this.cidade, this.estado, this.bairro, this.rua, this.numero, this.complemento);
    }


}
