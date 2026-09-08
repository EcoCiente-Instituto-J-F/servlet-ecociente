package com.example.ecociente.model;

public class Condominio {

    //================ ATRIBUTOS ================

    private int idCondominio;
    private String nome;
    private String cnpj;
    private boolean status;
    private String token;
    private int idEndereco;
    private int idTipoCondominio;

    //================ METODOS CONSTRUTORES ================

    // Construtor completo
    public Condominio(int idCondominio, String nome, String cnpj, boolean status, String token, int idEndereco, int idTipoCondominio) {
        this.idCondominio = idCondominio;
        this.nome = nome;
        this.cnpj = cnpj;
        this.status = status;
        this.token = token;
        this.idEndereco = idEndereco;
        this.idTipoCondominio = idTipoCondominio;
    }

    // Construtor sem cnpj
    public Condominio(int idCondominio, String nome, boolean status, String token, int idEndereco, int idTipoCondominio) {
        this.idCondominio = idCondominio;
        this.nome = nome;
        this.status = status;
        this.token = token;
        this.idEndereco = idEndereco;
        this.idTipoCondominio = idTipoCondominio;
    }


    //================ METODOS GETTERS ================

    public int getIdCondominio() {
        return idCondominio;
    }

    public String getNome() {
        return nome;
    }

    public String getCnpj() {
        return cnpj;
    }

    public boolean isStatus() {
        return status;
    }

    public String getToken() {
        return token;
    }

    public int idEndereco() {
        return idEndereco;
    }

    public int getIdTipoCondominio() {
        return idTipoCondominio;
    }


    //================ METODOS SETTERS ================

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setIdEndereco(int idEndereco) {
        this.idEndereco = idEndereco;
    }

    public void setIdTipoCondominio(int idTipoCondominio) {
        this.idTipoCondominio = idTipoCondominio;
    }


    //================ TO STRING ================
    @Override

    public String toString(){
        return """
                Condominio { id: %d | nome: %s | cnpj: %s | está ativo? %b | token: %s | id endereco: %d | id tipo condominio: %d }
                """.formatted(this.idCondominio, this.nome, this.cnpj, this.status, this.token, this.idEndereco, this.idTipoCondominio);
    }
}