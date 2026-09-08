package com.example.ecociente.model;

import java.time.LocalDate;

public class Morador extends Usuario{

    //================ ATRIBUTOS ================

    private int idMorador;
    private String numeroApartamento;
    private String nivelEngajamento;
    private int idTorre;

    //================ METODOS CONSTRUTORES ================

    // Construtor completo

    public Morador(int idUsuario, String nome, String email, String senhaHash, LocalDate dataCadastro, boolean status, int idEndereco, int idTipoUsuario, int idMorador, String numeroApartamento, String nivelEngajamento, int idTorre) {
        super(idUsuario, nome, email, senhaHash, dataCadastro, status, idEndereco, idTipoUsuario);
        this.idMorador = idMorador;
        this.numeroApartamento = numeroApartamento;
        this.nivelEngajamento = nivelEngajamento;
        this.idTorre = idTorre;
    }

    //================ METODOS GETTERS ================

    public int getIdMorador() {
        return idMorador;
    }

    public String getNumeroApartamento() {
        return numeroApartamento;
    }

    public String getNivelEngajamento() {
        return nivelEngajamento;
    }

    public int getIdTorre() {
        return idTorre;
    }

    //================ METODOS SETTERS ================

    public void setNumeroApartamento(String numeroApartamento) {
        this.numeroApartamento = numeroApartamento;
    }

    public void setNivelEngajamento(String nivelEngajamento) {
        this.nivelEngajamento = nivelEngajamento;
    }

    public void setIdTorre(int idTorre) {
        this.idTorre = idTorre;
    }

    //================ TO STRING ================
    @Override

    public String toString(){
        return """
                Morador {id: %d | numero do ap: %s | nivel de engajamento: %s | id da torre: %d }
                """.formatted(this.idMorador, this.numeroApartamento, this.nivelEngajamento, this.idTorre);
    }


}
