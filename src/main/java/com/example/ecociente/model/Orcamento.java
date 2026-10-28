package com.example.ecociente.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

// Cálculo do valor da assinatura. Os getters já devolvem o texto pronto para as telas de resumo e pagamento.
public class Orcamento {

    public static final int QUANTIDADE_MAXIMA = 9_999_999;

    // Condomínio residencial ou comercial só assina a partir desta quantidade de unidades (o industrial não tem mínimo)
    public static final int UNIDADES_MINIMAS = 100;

    public static boolean podeAssinar(String tipoCondominio, int quantidade) {
        return "industrial".equals(tipoCondominio) || quantidade >= UNIDADES_MINIMAS;
    }

    private static final Locale BRASIL = Locale.of("pt", "BR");
    private static final BigDecimal MESES = BigDecimal.valueOf(12);

    private final Plano plano;
    private final boolean anual;
    private final int quantidade;

    public Orcamento(Plano plano, boolean anual, int quantidade) {
        this.plano = plano;
        this.anual = anual;
        this.quantidade = quantidade;
    }

    //================ CALCULOS ================

    private BigDecimal precoUnitario() {
        return anual ? plano.getPrecoAnual() : plano.getPrecoMensal();
    }

    private BigDecimal totalPorMes() {
        return precoUnitario().multiply(BigDecimal.valueOf(quantidade));
    }

    private BigDecimal totalDoAno() {
        return totalPorMes().multiply(MESES);
    }

    private BigDecimal totalDoAnoNoMensal() {
        return plano.getPrecoMensal().multiply(BigDecimal.valueOf(quantidade)).multiply(MESES);
    }

    // Valor cobrado de fato (vai para assinatura.valor_cobrado): no anual é o ano inteiro de uma vez
    public BigDecimal getValorCobrado() {
        return (anual ? totalDoAno() : totalPorMes()).setScale(2, RoundingMode.HALF_UP);
    }

    private static String moeda(BigDecimal valor) {
        return NumberFormat.getCurrencyInstance(BRASIL).format(valor);
    }

    //================ TEXTOS PARA AS TELAS ================

    public Plano getPlano() { return plano; }
    public boolean isAnual() { return anual; }
    public int getQuantidade() { return quantidade; }

    public String getQuantidadeFormatada() {
        return NumberFormat.getIntegerInstance(BRASIL).format(quantidade);
    }

    public String getPrecoUnitario() {
        return moeda(precoUnitario()) + " por " + plano.getUnidadeTexto() + " / mês" + (anual ? " (plano anual)" : "");
    }

    public String getRotuloTotal() {
        return anual ? "Total anual" : "Total mensal";
    }

    public String getDetalhe() {
        return getQuantidadeFormatada() + " " + plano.getUnidadePlural() + " × " + moeda(precoUnitario()) + (anual ? " × 12 meses" : " por mês");
    }

    public String getTotal() {
        return moeda(getValorCobrado());
    }

    public String getResumo() {
        return getQuantidadeFormatada() + " " + plano.getUnidadePlural() + " · cobrança " + (anual ? "anual" : "mensal");
    }

    public String getPorPeriodo() {
        return anual ? "por ano" : "por mês";
    }

    public String getCalculoMensal() {
        BigDecimal mes = plano.getPrecoMensal().multiply(BigDecimal.valueOf(quantidade));
        return "12 × " + moeda(mes) + " = " + moeda(totalDoAnoNoMensal());
    }

    public String getCalculoAnualMes() {
        BigDecimal mes = plano.getPrecoAnual().multiply(BigDecimal.valueOf(quantidade));
        return moeda(mes) + " por mês (" + moeda(plano.getPrecoAnual()) + " por " + plano.getUnidadeTexto() + ")";
    }

    public String getCalculoAnualAno() {
        BigDecimal mes = plano.getPrecoAnual().multiply(BigDecimal.valueOf(quantidade));
        return "12 × " + moeda(mes) + " = " + moeda(mes.multiply(MESES));
    }

    public String getEconomia() {
        return moeda(totalDoAnoNoMensal().subtract(plano.getPrecoAnual().multiply(BigDecimal.valueOf(quantidade)).multiply(MESES)));
    }

    public String getEconomiaPercentual() {
        BigDecimal desconto = BigDecimal.ONE.subtract(plano.getPrecoAnual().divide(plano.getPrecoMensal(), 4, RoundingMode.HALF_UP));
        return desconto.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP) + "%";
    }
}
