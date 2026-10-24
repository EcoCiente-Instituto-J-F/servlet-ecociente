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

    // Dados so de exibicao, preenchidos pelas listagens que fazem JOIN
    private String nomeTipo;
    private String nomeSindico;
    private Endereco enderecoCompleto;

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

    public void setDadosExibicao(String nomeTipo, String nomeSindico, Endereco enderecoCompleto) {
        this.nomeTipo = nomeTipo;
        this.nomeSindico = nomeSindico;
        this.enderecoCompleto = enderecoCompleto;
    }



    //================ EXIBICAO (usado pelas JSPs) ================

    public String getTipo() {
        return nomeTipo == null ? "" : nomeTipo;
    }

    // 14 dígitos viram 12.345.678/0001-90
    public static String formatarCnpj(String cnpj) {
        if (cnpj == null || cnpj.length() != 14) return cnpj == null ? "" : cnpj;
        return cnpj.substring(0, 2) + "." + cnpj.substring(2, 5) + "." + cnpj.substring(5, 8) + "/" + cnpj.substring(8, 12) + "-" + cnpj.substring(12);
    }

    public String getIniciais() {
        return Usuario.iniciaisDe(nome);
    }

    public String getSindico() {
        return nomeSindico == null ? "" : nomeSindico;
    }

    public String getEndereco() {
        return enderecoCompleto == null ? "" : enderecoCompleto.getTextoCompleto();
    }

    public String getCep() {
        return enderecoCompleto == null ? "" : enderecoCompleto.getCepFormatado();
    }

    public String getCidade() {
        return enderecoCompleto == null ? "" : enderecoCompleto.getCidade();
    }

    public String getEstado() {
        return enderecoCompleto == null ? "" : enderecoCompleto.getEstado();
    }

    public String getBairro() {
        return enderecoCompleto == null || enderecoCompleto.getBairro() == null ? "" : enderecoCompleto.getBairro();
    }

    public String getRua() {
        return enderecoCompleto == null ? "" : enderecoCompleto.getRua();
    }

    public String getNumero() {
        return enderecoCompleto == null ? "" : String.valueOf(enderecoCompleto.getNumero());
    }

    public String getComplemento() {
        return enderecoCompleto == null || enderecoCompleto.getComplemento() == null ? "" : enderecoCompleto.getComplemento();
    }

    //================ TO STRING ================
    @Override

    public String toString(){
        return """
                Condominio { id: %d | nome: %s | cnpj: %s | está ativo? %b | token: %s | id endereco: %d | id tipo condominio: %d }
                """.formatted(this.idCondominio, this.nome, this.cnpj, this.status, this.token, this.idEndereco, this.idTipoCondominio);
    }
}