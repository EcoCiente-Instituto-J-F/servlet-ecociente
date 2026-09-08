package com.example.ecociente.model;

import java.time.LocalDate;

public class Usuario {

    //================ ATRIBUTOS ================

    private int idUsuario;
    private String nome;
    private String email;
    private String senhaHash;
    private LocalDate dataCadastro;
    private boolean status;
    private int idEndereco;
    private int idTipoUsuario;


    //================ METODOS CONSTRUTORES ================

    // Construtor completo

    public Usuario(int idUsuario, String nome, String email, String senhaHash, LocalDate dataCadastro, boolean status, int idEndereco, int idTipoUsuario) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.dataCadastro = dataCadastro;
        this.status = status;
        this.idEndereco = idEndereco;
        this.idTipoUsuario = idTipoUsuario;
    }

    // Construtor sem endereco

    public Usuario(int idUsuario, String nome, String email, String senhaHash, LocalDate dataCadastro, boolean status, int idTipoUsuario) {
        this.idUsuario = idUsuario;
        this.nome = nome;
        this.email = email;
        this.senhaHash = senhaHash;
        this.dataCadastro = dataCadastro;
        this.status = status;
        this.idTipoUsuario = idTipoUsuario;
    }


    //================ METODOS GETTERS ================

    public int getIdUsuario() {
        return idUsuario;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public boolean isStatus() {
        return status;
    }

    public int getIdEndereco() {
        return idEndereco;
    }

    public int getIdTipoUsuario() {
        return idTipoUsuario;
    }


    //================ METODOS SETTERS ================

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setEndereco(int idEndereco) {
        this.idEndereco = idEndereco;
    }

    public void setTipoUsuario(int idTipoUsuario) {
        this.idTipoUsuario = idTipoUsuario;
    }


    //================ TO STRING ================
    @Override

    public String toString(){
        return """
                Usuario { id: %d | nome: %s | email: %s | senha: %s | data de cadastro: %s | ativo? %b | id endereco: %d | id tipo usuario: %d }
                """.formatted(this.idUsuario, this.nome, this.email, this.senhaHash, this.dataCadastro, this.status, this.idEndereco, this.idUsuario);
    }
}
