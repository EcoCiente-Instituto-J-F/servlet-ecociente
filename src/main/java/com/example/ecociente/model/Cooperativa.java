package com.example.ecociente.model;

import java.time.LocalDate;

public class Cooperativa extends Usuario{

    //================ ATRIBUTOS ================
    private int idCooperativa;
    private String cnpj;
    private int condominiosVinculados;

    //================ METODO CONSTRUTOR ================

    // Construtor completo
    public Cooperativa(int idUsuario, String nome, String email, String senhaHash, LocalDate dataCadastro, boolean status, int idEndereco, int idTipoUsuario, int idCooperativa, String cnpj) {
        super(idUsuario, nome, email, senhaHash, dataCadastro, status, idEndereco, idTipoUsuario);
        this.idCooperativa = idCooperativa;
        this.cnpj = cnpj;
    }

    // ================ METODOS GETTERS ================

    public int getIdCooperativa() {
        return idCooperativa;
    }

    public String getCnpj() {
        return cnpj;
    }

    // ================ METODOS SETTERS ================


    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }


    // ================ EXIBICAO (usado pelas JSPs) ================

    public String getResponsavel() {
        return getNome();
    }

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

    // ================ TO STRING ================
    @Override

    public String toString(){
        return """
                Cooperativa { id: %d | cnpj: %s}
                """.formatted(this.idCooperativa, this.cnpj);
    }

}
