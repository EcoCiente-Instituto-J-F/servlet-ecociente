package com.example.ecociente.model;

public class TipoUsuario {

    //================ ATRIBUTOS ================

    private int idTipoUsuario;
    private String nome;
    private String descricao;


    //================ METODO CONSTRUTOR ================

    // Construtor completo
    public TipoUsuario(int idTipoUsuario, String nome, String descricao) {
        this.idTipoUsuario = idTipoUsuario;
        this.nome = nome;
        this.descricao = descricao;
    }

    //================ METODOS GETTERS ================

    public int getIdTipoUsuario() {
        return idTipoUsuario;
    }

    public String getNomeTipo() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    //================ METODOS SETTERS ================

    public void setNomeTipo(String nome) {
        this.nome= nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    //================ TO STRING ================
    @Override

    public String toString (){
        return """
                Tipo Usuario { id: %d | nome: %s | descricao: %s }
                """.formatted(this.idTipoUsuario, this.nome, this.descricao);
    }
}
