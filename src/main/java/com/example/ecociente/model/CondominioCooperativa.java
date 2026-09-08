package com.example.ecociente.model;

import java.time.LocalDate;

public class CondominioCooperativa {
    //================ ATRIBUTOS ================

    private int idCondominioCooperativa;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private int idCondominio;
    private int idCooperativa;

    //================ METODOS CONSTRUTORES ================

    // Construtor completo
    public CondominioCooperativa(int idCondominioCooperativa, LocalDate dataInicio, LocalDate dataFim, int idCondominio, int idCooperativa) {
        this.idCondominioCooperativa = idCondominioCooperativa;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.idCondominio = idCondominio;
        this.idCooperativa = idCooperativa;
    }

    //Construtor sem data de fim
    public CondominioCooperativa(int idCondominioCooperativa, LocalDate dataInicio, int idCondominio, int idCooperativa) {
        this.idCondominioCooperativa = idCondominioCooperativa;
        this.dataInicio = dataInicio;
        this.idCondominio = idCondominio;
        this.idCooperativa = idCooperativa;
    }

    // ================ METODOS GETTERS ================

    public int getIdCondominioCooperativa() {
        return idCondominioCooperativa;
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
                Condominio Cooperativa {id: %d | inicio do acordo: %s | fim do acordo %s | id condominio: %d | id cooperativa: %d}
                """.formatted(this.idCondominioCooperativa, this.dataInicio, this.dataFim, this.idCondominio, this.idCooperativa);
    }

}
