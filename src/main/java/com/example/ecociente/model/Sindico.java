package com.example.ecociente.model;

import java.time.LocalDate;

public class Sindico extends Usuario{

    //================ ATRIBUTOS ================

    private int idSindico;
    private String cpf;
    private LocalDate dataInicioMandato;
    private LocalDate dataFimMandato;
    private int idCondominio;

    //================ METODOS CONSTRUTORES ================

    // Construtor completo
    public Sindico(int idUsuario, String nome, String email, String senhaHash, LocalDate dataCadastro, boolean status, int idEndereco, int idTipoUsuario, int idSindico, String cpf, LocalDate dataInicioMandato, LocalDate dataFimMandato, int idCondominio) {
        super(idUsuario, nome, email, senhaHash, dataCadastro, status, idEndereco, idTipoUsuario);
        this.idSindico = idSindico;
        this.cpf = cpf;
        this.dataInicioMandato = dataInicioMandato;
        this.dataFimMandato = dataFimMandato;
        this.idCondominio = idCondominio;
    }

    // Construtor sem endereço
    public Sindico(int idUsuario, String nome, String email, String senhaHash, LocalDate dataCadastro, boolean status, int idTipoUsuario, int idSindico, String cpf, LocalDate dataInicioMandato, LocalDate dataFimMandato, int idCondominio) {
        super(idUsuario, nome, email, senhaHash, dataCadastro, status, idTipoUsuario);
        this.idSindico = idSindico;
        this.cpf = cpf;
        this.dataInicioMandato = dataInicioMandato;
        this.dataFimMandato = dataFimMandato;
        this.idCondominio = idCondominio;
    }

    // Construtor sem data de fim do mandato
    public Sindico(int idUsuario, String nome, String email, String senhaHash, LocalDate dataCadastro, boolean status, int idEndereco, int idTipoUsuario, int idSindico, String cpf, LocalDate dataInicioMandato, int idCondominio) {
        super(idUsuario, nome, email, senhaHash, dataCadastro, status, idEndereco, idTipoUsuario);
        this.idSindico = idSindico;
        this.cpf = cpf;
        this.dataInicioMandato = dataInicioMandato;
        this.idCondominio = idCondominio;
    }

    // Construtor sem data de inicio e de fim do mandato
    public Sindico(int idUsuario, String nome, String email, String senhaHash, LocalDate dataCadastro, boolean status, int idEndereco, int idTipoUsuario, int idSindico, String cpf, int idCondominio) {
        super(idUsuario, nome, email, senhaHash, dataCadastro, status, idEndereco, idTipoUsuario);
        this.idSindico = idSindico;
        this.cpf = cpf;
        this.idCondominio = idCondominio;
    }


    //================ METODOS GETTERS ================

    public int getIdSindico() {
        return idSindico;
    }

    public String getCpf() {
        return cpf;
    }

    public LocalDate getDataInicioMandato() {
        return dataInicioMandato;
    }

    public LocalDate getDataFimMandato() {
        return dataFimMandato;
    }

    public int getIdCondominio() {
        return idCondominio;
    }


    //================ METODOS SETTERS ================

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setDataInicioMandato(LocalDate dataInicioMandato) {
        this.dataInicioMandato = dataInicioMandato;
    }

    public void setDataFimMandato(LocalDate dataFimMandato) {
        this.dataFimMandato = dataFimMandato;
    }

    public void setCondominio(int idCondominio) {
        this.idCondominio = idCondominio;
    }

    //================ TO STRING ================
    @Override

    public String toString(){
        return """
                Sindico { id: %d | cpf: %s | inicio de mandato: %s | fim de mandato: %s | id condominio: %d}
                """.formatted(this.idSindico, this.cpf, this.dataInicioMandato, this.dataFimMandato, this.idCondominio);
    }
}
