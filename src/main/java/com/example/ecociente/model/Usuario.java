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
    private String cpf;

    // Dados so de exibicao, preenchidos pelas listagens que fazem JOIN
    private String nomeTipo;
    private String telefone;
    private Endereco enderecoCompleto;


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

    public String getCpf() {
        return cpf;
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

    public void setIdEndereco(int idEndereco) {
        this.idEndereco = idEndereco;
    }

    public void setTipoUsuario(int idTipoUsuario) {
        this.idTipoUsuario = idTipoUsuario;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setDadosExibicao(String nomeTipo, String telefone, Endereco enderecoCompleto) {
        this.nomeTipo = nomeTipo;
        this.telefone = telefone;
        this.enderecoCompleto = enderecoCompleto;
    }



    //================ EXIBICAO (usado pelas JSPs) ================

    public String getTipo() {
        return nomeTipo == null ? "" : nomeTipo;
    }

    public String getTag() {
        return getTipo().split(" ")[0];
    }

    public String getIniciais() {
        return iniciaisDe(nome);
    }

    public String getTelefone() {
        return Telefone.formatar(telefone);
    }

    public String getEndereco() {
        return enderecoCompleto == null ? "" : enderecoCompleto.getTextoCompleto();
    }

    public String getLocalidade() {
        return enderecoCompleto == null ? "" : enderecoCompleto.getLocalidade();
    }

    public String getCep() {
        return enderecoCompleto == null ? "" : enderecoCompleto.getCepFormatado();
    }

    public String getCidade() {
        return enderecoCompleto == null ? "" : enderecoCompleto.getCidade();
    }

    public String getEstado() {
        return enderecoCompleto == null ? "" : enderecoCompleto.getEstado();
    }

    public String getBairro() {
        return enderecoCompleto == null || enderecoCompleto.getBairro() == null ? "" : enderecoCompleto.getBairro();
    }

    public String getRua() {
        return enderecoCompleto == null ? "" : enderecoCompleto.getRua();
    }

    public String getNumero() {
        return enderecoCompleto == null ? "" : String.valueOf(enderecoCompleto.getNumero());
    }

    public String getComplemento() {
        return enderecoCompleto == null || enderecoCompleto.getComplemento() == null ? "" : enderecoCompleto.getComplemento();
    }

    // "Maria Souza" vira "MS"
    public static String iniciaisDe(String nome) {
        if (nome == null || nome.isBlank()) return "—";
        String[] partes = nome.trim().split("\\s+");
        String ultima = partes.length > 1 ? partes[partes.length - 1].substring(0, 1) : "";
        return (partes[0].substring(0, 1) + ultima).toUpperCase();
    }

    //================ TO STRING ================
    @Override

    public String toString(){
        return """
                Usuario { id: %d | nome: %s | email: %s | senha: %s | data de cadastro: %s | ativo? %b | id endereco: %d | id tipo usuario: %d }
                """.formatted(this.idUsuario, this.nome, this.email, this.senhaHash, this.dataCadastro, this.status, this.idEndereco, this.idUsuario);
    }
}
