package com.example.ecociente.model;

import java.time.LocalDate;

// Cooperativa de reciclagem: um usuário com os dados próprios da tabela cooperativa
public class Cooperativa extends Usuario{

    //================ ATRIBUTOS ================

    private int idCooperativa;
    private String nomeCooperativa;
    private String cnpj;

    // Dado só de exibição, preenchido pela listagem
    private int condominiosVinculados;

    //================ METODOS CONSTRUTORES ================

    // Construtor completo
    public Cooperativa(int idUsuario, String nome, String email, String senhaHash, LocalDate dataCadastro, boolean status, int idEndereco, int idTipoUsuario, int idCooperativa, String cnpj) {
        super(idUsuario, nome, email, senhaHash, dataCadastro, status, idEndereco, idTipoUsuario);
        this.idCooperativa = idCooperativa;
        this.cnpj = cnpj;
    }

    //================ METODOS GETTERS ================

    public int getIdCooperativa() {
        return idCooperativa;
    }

    public String getNomeCooperativa() {
        return nomeCooperativa;
    }

    public String getCnpj() {
        return cnpj;
    }

    //================ METODOS SETTERS ================

    public void setNomeCooperativa(String nomeCooperativa) {
        this.nomeCooperativa = nomeCooperativa;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }


    //================ EXIBICAO (usado pelas JSPs) ================

    // O nome do usuário é o do responsável pela cooperativa
    public String getResponsavel() {
        return getNome();
    }

    // Etiqueta da tabela: mostra se a cooperativa está ativa
    @Override
    public String getTag() {
        return isStatus() ? "Ativa" : "Inativa";
    }

    public int getCondominiosVinculados() {
        return condominiosVinculados;
    }

    public void setCondominiosVinculados(int condominiosVinculados) {
        this.condominiosVinculados = condominiosVinculados;
    }

    //================ TO STRING ================
    @Override

    public String toString(){
        return """
                Cooperativa { id: %d | cnpj: %s}
                """.formatted(this.idCooperativa, this.cnpj);
    }

}
