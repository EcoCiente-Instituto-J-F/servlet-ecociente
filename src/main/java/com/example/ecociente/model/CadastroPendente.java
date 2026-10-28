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
    private String termos;
    private String token;
    private String tipoCondominio;
    private String cnpj;
    private String complemento;
    private String nomeCooperativa;
    private String nomeCondominio;
    private String razaoSocial;
    private String numeroUnidades;
    private String areaM2;
    private String referencia;

    // Telas de pagamento (os JSPs pag_* leem estes campos)
    private String documento;
    private String ciclo;
    private String metodo;
    private String nomeCartao;
    private String docTitular;
    private String docPagador;
    private String emailBoleto;
    private boolean contaCriada; // conta já gravada no banco: só falta (opcionalmente) a assinatura
    private boolean dadosPagamentoOk; // dados de cobrança (pag-dados) preenchidos e validados

    // Assinatura (tabelas plano e assinatura)
    private int idUsuario; // dono da conta criada
    private Integer idCondominio; // condomínio do síndico; nulo para quem não tem condomínio
    private String plano; // código do plano (o mesmo do tipo do condomínio)
    private int quantidade; // apartamentos, empresas ou m² cobrados
    private int idAssinatura; // assinatura pendente já gravada; 0 se ainda não gravou

    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }

    public String getCiclo() { return ciclo; }
    public void setCiclo(String ciclo) { this.ciclo = ciclo; }

    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }

    public String getNomeCartao() { return nomeCartao; }
    public void setNomeCartao(String nomeCartao) { this.nomeCartao = nomeCartao; }

    public String getDocTitular() { return docTitular; }
    public void setDocTitular(String docTitular) { this.docTitular = docTitular; }

    public String getDocPagador() { return docPagador; }
    public void setDocPagador(String docPagador) { this.docPagador = docPagador; }

    public String getEmailBoleto() { return emailBoleto; }
    public void setEmailBoleto(String emailBoleto) { this.emailBoleto = emailBoleto; }

    public boolean isContaCriada() { return contaCriada; }
    public void setContaCriada(boolean contaCriada) { this.contaCriada = contaCriada; }

    public boolean isDadosPagamentoOk() { return dadosPagamentoOk; }
    public void setDadosPagamentoOk(boolean dadosPagamentoOk) { this.dadosPagamentoOk = dadosPagamentoOk; }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public Integer getIdCondominio() { return idCondominio; }
    public void setIdCondominio(Integer idCondominio) { this.idCondominio = idCondominio; }

    public String getPlano() { return plano; }
    public void setPlano(String plano) { this.plano = plano; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public int getIdAssinatura() { return idAssinatura; }
    public void setIdAssinatura(int idAssinatura) { this.idAssinatura = idAssinatura; }

    public String getNomeCondominio() { return nomeCondominio; }
    public void setNomeCondominio(String nomeCondominio) { this.nomeCondominio = nomeCondominio; }

    public String getRazaoSocial() { return razaoSocial; }
    public void setRazaoSocial(String razaoSocial) { this.razaoSocial = razaoSocial; }

    public String getNumeroUnidades() { return numeroUnidades; }
    public void setNumeroUnidades(String numeroUnidades) { this.numeroUnidades = numeroUnidades; }

    public String getAreaM2() { return areaM2; }
    public void setAreaM2(String areaM2) { this.areaM2 = areaM2; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }

    public String getNomeCooperativa() { return nomeCooperativa; }
    public void setNomeCooperativa(String nomeCooperativa) { this.nomeCooperativa = nomeCooperativa; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getTipoCondominio() { return tipoCondominio; }
    public void setTipoCondominio(String tipoCondominio) { this.tipoCondominio = tipoCondominio; }

    public String getCnpj() { return cnpj; }
    public void setCnpj(String cnpj) { this.cnpj = cnpj; }

    public String getComplemento() { return complemento; }
    public void setComplemento(String complemento) { this.complemento = complemento; }

    public String getTermos() { return termos; }
    public void setTermos(String termos) { this.termos = termos; }

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