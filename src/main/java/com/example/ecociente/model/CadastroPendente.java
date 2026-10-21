package com.example.ecociente.model;

// Guarda os dados do cadastro em etapas enquanto a pessoa ainda nao terminou.
// Fica na sessao ate a conta ser efetivamente criada no banco.
public class CadastroPendente {

    private String nome;
    private String email;
    private String senhaHash;
    private String perfil;
    private String moradia;
    private String telefone;
    private String cpf;
    private String nascimento;
    private String cep;
    private String uf;
    private String cidade;
    private String rua;
    private String numero;
    private String bairro;
    private String bloco;
    private String apto;
    private String avisos;

    public String getAvisos() { return avisos; }
    public void setAvisos(String avisos) { this.avisos = avisos; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenhaHash() { return senhaHash; }
    public void setSenhaHash(String senhaHash) { this.senhaHash = senhaHash; }

    public String getPerfil() { return perfil; }
    public void setPerfil(String perfil) { this.perfil = perfil; }

    public String getMoradia() { return moradia; }
    public void setMoradia(String moradia) { this.moradia = moradia; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getNascimento() { return nascimento; }
    public void setNascimento(String nascimento) { this.nascimento = nascimento; }

    public String getCep() { return cep; }
    public void setCep(String cep) { this.cep = cep; }

    public String getUf() { return uf; }
    public void setUf(String uf) { this.uf = uf; }

    public String getCidade() { return cidade; }
    public void setCidade(String cidade) { this.cidade = cidade; }

    public String getRua() { return rua; }
    public void setRua(String rua) { this.rua = rua; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public String getBloco() { return bloco; }
    public void setBloco(String bloco) { this.bloco = bloco; }

    public String getApto() { return apto; }
    public void setApto(String apto) { this.apto = apto; }
}