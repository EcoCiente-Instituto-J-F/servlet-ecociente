package com.example.ecociente.model;

import java.time.LocalDate;

public class Morador extends Usuario{

    //================ ATRIBUTOS ================

    private int idMorador;
    private String numeroApartamento;
    private String torre;
    private int idCondominio;

    //================ METODOS CONSTRUTORES ================

    // Construtor completo

    public Morador(int idUsuario, String nome, String email, String senhaHash, LocalDate dataCadastro, boolean status, int idEndereco, int idTipoUsuario, int idMorador, String numeroApartamento, String torre, int idCondominio) {
        super(idUsuario, nome, email, senhaHash, dataCadastro, status, idEndereco, idTipoUsuario);
        this.idMorador = idMorador;
        this.numeroApartamento = numeroApartamento;
        this.torre = torre;
        this.idCondominio = idCondominio;
    }

    //================ METODOS GETTERS ================

    public int getIdMorador() {
        return idMorador;
    }

    public String getNumeroApartamento() {
        return numeroApartamento;
    }

    public String getTorre() {
        return torre;
    }

    public int getIdCondominio() {
        return idCondominio;
    }

    //================ METODOS SETTERS ================

    public void setNumeroApartamento(String numeroApartamento) {
        this.numeroApartamento = numeroApartamento;
    }

    public void setTorre(String torre) {
        this.torre = torre;
    }

    public void setIdCondominio(int idCondominio) {
        this.idCondominio = idCondominio;
    }

    //================ TO STRING ================
    @Override

    public String toString(){
        return """
                Morador {id: %d | numero do ap: %s | torre: %s | id do condominio: %d }
                """.formatted(this.idMorador, this.numeroApartamento, this.torre, this.idCondominio);
    }


}
