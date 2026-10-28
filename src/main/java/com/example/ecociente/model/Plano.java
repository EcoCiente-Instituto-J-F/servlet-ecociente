package com.example.ecociente.model;

import java.math.BigDecimal;

// Plano de assinatura, lido da tabela plano. Os preços são por unidade, por mês.
public class Plano {

    //================ ATRIBUTOS ================

    private int idPlano;
    private String codigo;
    private String nome;
    private String descricao;
    private String unidade; // apartamento, empresa ou m2
    private BigDecimal precoMensal;
    private BigDecimal precoAnual; // preço por unidade, por mês, no plano anual
    private boolean ativo;

    //================ METODO CONSTRUTOR ================

    // Construtor completo
    public Plano(int idPlano, String codigo, String nome, String descricao, String unidade,
                 BigDecimal precoMensal, BigDecimal precoAnual, boolean ativo) {
        this.idPlano = idPlano;
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.unidade = unidade;
        this.precoMensal = precoMensal;
        this.precoAnual = precoAnual;
        this.ativo = ativo;
    }

    //================ METODOS GETTERS ================

    public int getIdPlano() { return idPlano; }
    public String getCodigo() { return codigo; }
    public String getNome() { return nome; }
    public String getUnidade() { return unidade; }
    public BigDecimal getPrecoMensal() { return precoMensal; }
    public BigDecimal getPrecoAnual() { return precoAnual; }
    public boolean isAtivo() { return ativo; }

    public String getDescricao() {
        if (descricao != null && !descricao.isBlank()) return descricao;
        return "Cobrança por " + getUnidadeTexto() + ", todo mês ou com desconto no plano anual.";
    }

    //================ TEXTOS PARA AS TELAS ================

    // Unidade como aparece para a pessoa (no banco o metro quadrado é "m2")
    public String getUnidadeTexto() {
        return "m2".equals(unidade) ? "m²" : unidade;
    }

    public String getUnidadePlural() {
        return switch (unidade) {
            case "apartamento" -> "apartamentos";
            case "empresa" -> "empresas";
            default -> "m²";
        };
    }

    public String getRotuloQuantidade() {
        return switch (unidade) {
            case "apartamento" -> "Número de apartamentos";
            case "empresa" -> "Número de empresas";
            default -> "Área total em m²";
        };
    }

    public String getIcone() {
        return switch (unidade) {
            case "apartamento" -> "icone-tipo-residencial.svg";
            case "empresa" -> "icone-tipo-comercial.svg";
            default -> "icone-tipo-industrial.svg";
        };
    }

    //================ TO STRING ================
    @Override
    public String toString() {
        return """
                Plano { id: %d | codigo: %s | nome: %s | unidade: %s | mensal: %s | anual: %s | ativo: %b }
                """.formatted(idPlano, codigo, nome, unidade, precoMensal, precoAnual, ativo);
    }
}
