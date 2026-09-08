package com.example.ecociente.model;

public class TipoCondominio {

    //================ ATRIBUTOS ================

    private int idTipoCondominio;
    private String nome;
    private String descricao;

    //================ METODO CONSTRUTOR ================

    // Construtor completo
    public TipoCondominio(int idTipoCondominio, String nome, String descricao) {
        this.idTipoCondominio = idTipoCondominio;
        this.nome = nome;
        this.descricao = descricao;
    }

    //================ METODOS GETTERS ================

    public int getIdTipoCondominio() {
        return idTipoCondominio;
    }

    public String getNomeTipo() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    //================ METODOS SETTERS ================

    public void setNomeTipo(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    //================ TO STRING ================
    @Override

    public String toString(){
        return """
                Tipo Condominio { id: %d | nome: %s | descricao: %s }
                """.formatted(this.idTipoCondominio, this.nome, this.descricao);
    }
}
