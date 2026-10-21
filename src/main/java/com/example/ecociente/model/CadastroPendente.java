package com.example.ecociente.model;

// Guarda os dados do cadastro em etapas enquanto a pessoa ainda nao terminou.
// Fica na sessao ate a conta ser efetivamente criada no banco.
public class CadastroPendente {

    private String nome;
    private String email;
    private String senhaHash;
    private String perfil;

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }
}