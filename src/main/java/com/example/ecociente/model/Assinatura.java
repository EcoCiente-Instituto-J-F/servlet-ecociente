package com.example.ecociente.model;

import java.math.BigDecimal;
import java.time.LocalDate;

// Assinatura de um plano. Do síndico fica ligada ao condomínio; sem condomínio (idCondominio nulo), só ao usuário.
public class Assinatura {

    //================ ATRIBUTOS ================

    private int idAssinatura;
    private String ciclo; // mensal ou anual
    private BigDecimal quantidade;
    private BigDecimal valorCobrado;
    private String status; // pendente, ativa ou cancelada
    private LocalDate dataInicio;
    private LocalDate proximaCobranca;
    private LocalDate dataCadastro;
    private int idUsuario;
    private Integer idCondominio;
    private int idPlano;

    //================ METODO CONSTRUTOR ================

    // Construtor da assinatura nova: começa pendente, sem data de início nem de cobrança
    public Assinatura(String ciclo, BigDecimal quantidade, BigDecimal valorCobrado, int idUsuario, Integer idCondominio, int idPlano) {
        this.ciclo = ciclo;
        this.quantidade = quantidade;
        this.valorCobrado = valorCobrado;
        this.status = "pendente";
        this.dataCadastro = LocalDate.now();
        this.idUsuario = idUsuario;
        this.idCondominio = idCondominio;
        this.idPlano = idPlano;
    }

    // Construtor completo
    public Assinatura(int idAssinatura, String ciclo, BigDecimal quantidade, BigDecimal valorCobrado, String status,
                      LocalDate dataInicio, LocalDate proximaCobranca, LocalDate dataCadastro,
                      int idUsuario, Integer idCondominio, int idPlano) {
        this.idAssinatura = idAssinatura;
        this.ciclo = ciclo;
        this.quantidade = quantidade;
        this.valorCobrado = valorCobrado;
        this.status = status;
        this.dataInicio = dataInicio;
        this.proximaCobranca = proximaCobranca;
        this.dataCadastro = dataCadastro;
        this.idUsuario = idUsuario;
        this.idCondominio = idCondominio;
        this.idPlano = idPlano;
    }

    //================ METODOS GETTERS ================

    public int getIdAssinatura() { return idAssinatura; }
    public String getCiclo() { return ciclo; }
    public BigDecimal getQuantidade() { return quantidade; }
    public BigDecimal getValorCobrado() { return valorCobrado; }
    public String getStatus() { return status; }
    public LocalDate getDataInicio() { return dataInicio; }
    public LocalDate getProximaCobranca() { return proximaCobranca; }
    public LocalDate getDataCadastro() { return dataCadastro; }
    public int getIdUsuario() { return idUsuario; }
    public Integer getIdCondominio() { return idCondominio; }
    public int getIdPlano() { return idPlano; }

    //================ METODOS SETTERS ================

    public void setIdAssinatura(int idAssinatura) { this.idAssinatura = idAssinatura; }
    public void setStatus(String status) { this.status = status; }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio = dataInicio; }
    public void setProximaCobranca(LocalDate proximaCobranca) { this.proximaCobranca = proximaCobranca; }

    //================ TO STRING ================
    @Override
    public String toString() {
        return """
                Assinatura { id: %d | plano: %d | usuario: %d | condominio: %s | ciclo: %s | quantidade: %s | valor: %s | status: %s }
                """.formatted(idAssinatura, idPlano, idUsuario, idCondominio, ciclo, quantidade, valorCobrado, status);
    }
}
