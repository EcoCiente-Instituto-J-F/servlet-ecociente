package com.example.ecociente.model;

import java.time.LocalDate;

public class Parceria {
    //================ ATRIBUTOS ================

    private int idParceria;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private int idCondominio;
    private int idCooperativa;

    //================ METODOS CONSTRUTORES ================

    // Construtor completo
    public Parceria(int idParceria, LocalDate dataInicio, LocalDate dataFim, int idCondominio, int idCooperativa) {
        this.idParceria = idParceria;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.idCondominio = idCondominio;
        this.idCooperativa = idCooperativa;
    }

    //Construtor sem data de fim
    public Parceria(int idParceria, LocalDate dataInicio, int idCondominio, int idCooperativa) {
        this.idParceria = idParceria;
        this.dataInicio = dataInicio;
        this.idCondominio = idCondominio;
        this.idCooperativa = idCooperativa;
    }

    // ================ METODOS GETTERS ================

    public int getIdParceria() {
        return idParceria;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public int getIdCondominio() {
        return idCondominio;
    }

    public int getIdCooperativa() {
        return idCooperativa;
    }

    // ================ METODOS SETTERS ================

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public void setIdCondominio(int idCondominio) {
        this.idCondominio = idCondominio;
    }

    public void setIdCooperativa(int idCooperativa) {
        this.idCooperativa = idCooperativa;
    }

    //================ TO STRING ================
    @Override

    public String toString(){
        return """
                Parceria {id: %d | inicio do acordo: %s | fim do acordo %s | id condominio: %d | id cooperativa: %d}
                """.formatted(this.idParceria, this.dataInicio, this.dataFim, this.idCondominio, this.idCooperativa);
    }

}