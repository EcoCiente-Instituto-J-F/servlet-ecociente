package com.example.ecociente.model;

// Tipo de condomínio (residencial, comercial ou industrial), lido da tabela tipo_condominio
public class TipoCondominio {

    //================ ATRIBUTOS ================

    private int idTipoCondominio;
    private String nome;
    private String descricao;

    //================ METODOS CONSTRUTORES ================

    // Construtor completo
    public TipoCondominio(int idTipoCondominio, String nome, String descricao) {
        this.idTipoCondominio = idTipoCondominio;
        this.nome = nome;
        this.descricao = descricao;
    }

    //================ METODOS GETTERS ================

    public int getId() {
        return idTipoCondominio;
    }

    public String getNome() {
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
