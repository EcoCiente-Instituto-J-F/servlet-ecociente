package com.example.ecociente.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

// Cálculo do valor da assinatura. Os getters já devolvem o texto pronto para as telas de resumo e pagamento.
public class Orcamento {

    //================ CONSTANTES ================

    // Maior quantidade aceita no cálculo (evita valores absurdos)
    public static final int QUANTIDADE_MAXIMA = 9_999_999;

    // Condomínio residencial ou comercial só assina a partir desta quantidade de unidades (o industrial não tem mínimo)
    public static final int UNIDADES_MINIMAS = 100;

    //================ REGRAS DE ASSINATURA ================

    // Diz se o condomínio chega ao mínimo de unidades para contratar um plano
    public static boolean podeAssinar(String tipoCondominio, int quantidade) {
        return "industrial".equals(tipoCondominio) || quantidade >= UNIDADES_MINIMAS;
    }

    // Única fonte da quantidade cobrada: área em m² arredondada para cima (industrial) ou número de unidades cadastrado
    public static int quantidadeDoCondominio(Condominio condominio) {
        int quantidade;
        if (condominio.getAreaM2() != null) {
            quantidade = condominio.getAreaM2().setScale(0, RoundingMode.CEILING).intValue();
        } else {
            quantidade = condominio.getNumeroUnidades() == null ? 1 : condominio.getNumeroUnidades();
        }
        return Math.min(Math.max(quantidade, 1), QUANTIDADE_MAXIMA);
    }

    private static final Locale BRASIL = Locale.of("pt", "BR");
    private static final BigDecimal MESES = BigDecimal.valueOf(12);

    //================ ATRIBUTOS ================

    private final Plano plano;
    private final boolean anual;
    private final int quantidade;

    //================ METODOS CONSTRUTORES ================

    // Construtor completo
    public Orcamento(Plano plano, boolean anual, int quantidade) {
        this.plano = plano;
        this.anual = anual;
        this.quantidade = quantidade;
    }

    //================ CALCULOS ================

    // Preço de uma unidade por mês, conforme o ciclo escolhido
    private BigDecimal precoUnitario() {
        return anual ? plano.getPrecoAnual() : plano.getPrecoMensal();
    }

    // Preço da unidade vezes a quantidade
    private BigDecimal totalPorMes() {
        return precoUnitario().multiply(BigDecimal.valueOf(quantidade));
    }

    // Mensalidade vezes 12 meses
    private BigDecimal totalDoAno() {
        return totalPorMes().multiply(MESES);
    }

    // Quanto custaria o ano inteiro no ciclo mensal, para comparar com o desconto do anual
    private BigDecimal totalDoAnoNoMensal() {
        return plano.getPrecoMensal().multiply(BigDecimal.valueOf(quantidade)).multiply(MESES);
    }

    // Valor cobrado de fato (vai para assinatura.valor_cobrado): no anual é o ano inteiro de uma vez
    public BigDecimal getValorCobrado() {
        return (anual ? totalDoAno() : totalPorMes()).setScale(2, RoundingMode.HALF_UP);
    }

    // Formata o valor em reais, ex.: R$ 600,00
    private static String moeda(BigDecimal valor) {
        return NumberFormat.getCurrencyInstance(BRASIL).format(valor);
    }

    //================ TEXTOS PARA AS TELAS ================

    public Plano getPlano() { return plano; }
    public boolean isAnual() { return anual; }
    public int getQuantidade() { return quantidade; }

    // Quantidade com separador de milhar, ex.: 1.500
    public String getQuantidadeFormatada() {
        return NumberFormat.getIntegerInstance(BRASIL).format(quantidade);
    }

    public String getPrecoUnitario() {
        return moeda(precoUnitario()) + " por " + plano.getUnidadeTexto() + " / mês" + (anual ? " (plano anual)" : "");
    }

    // Título do total na tela de resumo
    public String getRotuloTotal() {
        return anual ? "Total anual" : "Total mensal";
    }

    // Conta que explica o total, ex.: 150 apartamentos × R$ 4,00 por mês
    public String getDetalhe() {
        return getQuantidadeFormatada() + " " + plano.getUnidadePlural() + " × " + moeda(precoUnitario()) + (anual ? " × 12 meses" : " por mês");
    }

    // Valor cobrado já formatado em reais
    public String getTotal() {
        return moeda(getValorCobrado());
    }

    // Linha de resumo com a quantidade e o ciclo de cobrança
    public String getResumo() {
        return getQuantidadeFormatada() + " " + plano.getUnidadePlural() + " · cobrança " + (anual ? "anual" : "mensal");
    }

    public String getPorPeriodo() {
        return anual ? "por ano" : "por mês";
    }

    // Os três cálculos abaixo e a economia alimentam a comparação entre mensal e anual
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

    // Quanto se economiza em reais no ano ao escolher o ciclo anual
    public String getEconomia() {
        return moeda(totalDoAnoNoMensal().subtract(plano.getPrecoAnual().multiply(BigDecimal.valueOf(quantidade)).multiply(MESES)));
    }

    // Desconto do anual em relação ao mensal, em porcentagem
    public String getEconomiaPercentual() {
        BigDecimal desconto = BigDecimal.ONE.subtract(plano.getPrecoAnual().divide(plano.getPrecoMensal(), 4, RoundingMode.HALF_UP));
        return desconto.multiply(BigDecimal.valueOf(100)).setScale(0, RoundingMode.HALF_UP) + "%";
    }
}
