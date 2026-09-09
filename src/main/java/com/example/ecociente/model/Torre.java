package com.example.ecociente.model;

public class Torre {

    //================ ATRIBUTOS ================

    private int idTorre;
    private String nome;
    private int numeroUnidades;
    private int idCondominio;

    //================ METODOS CONSTRUTORES ================

    // Construtor completo
    public Torre(int idTorre, String nome, int numeroUnidades, int idCondominio) {
        this.idTorre = idTorre;
        this.nome = nome;
        this.numeroUnidades = numeroUnidades;
        this.idCondominio = idCondominio;
    }

    //================ METODOS GETTERS ================

    public int getIdTorre() {
        return idTorre;
    }

    public String getNome() {
        return nome;
    }

    public int getNumeroUnidades() {
        return numeroUnidades;
    }

    public int getIdCondominio() {
        return idCondominio;
    }


    //================ METODOS SETTERS ================

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setNumeroUnidades(int numeroUnidades) {
        this.numeroUnidades = numeroUnidades;
    }

    public void setIdCondominio(int idCondominio) {
        this.idCondominio = idCondominio;
    }

    //================ TO STRING ================

    @Override

    public String toString(){
        return """
                Torre {id: %d | nome: %s | numero de unidades: %d | id do condominio: %d}
                """.formatted(this.idTorre, this.nome, this.numeroUnidades, this.idCondominio);
    }

}