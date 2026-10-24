package com.example.ecociente.controller;

import com.example.ecociente.dao.CondominioDAO;
import com.example.ecociente.dao.CooperativaDAO;
import com.example.ecociente.dao.ParceriaDAO;
import com.example.ecociente.dao.UsuarioDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

//Home do painel (/painel) e só o miolo com cards e gráficos (/painel/conteudo), que o painel.js recarrega sozinho.
@WebServlet(urlPatterns = {"/painel", "/painel/conteudo"})
public class PainelServlet extends HttpServlet {
    UsuarioDAO daoUsuarios = new UsuarioDAO();
    CondominioDAO daoCondominios = new CondominioDAO();
    CooperativaDAO daoCooperativas = new CooperativaDAO();
    ParceriaDAO daoParcerias = new ParceriaDAO();

    //Posições horizontais dos 7 meses do gráfico e limites verticais do desenho (coordenadas do SVG da JSP).
    private static final double[] X_MESES = {211.6, 308.6, 405.9, 502.9, 600.1, 694.7, 790.3};
    private static final double Y_TOPO = 442.5;
    private static final double Y_BASE = 613.4;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        mostrarCards(request);
        mostrarGrafico(request);

        String pagina = request.getServletPath().equals("/painel/conteudo")
                ? "/WEB-INF/views/painel-conteudo.jsp"
                : "/WEB-INF/views/painel.jsp";
        request.getRequestDispatcher(pagina).forward(request, response);
    }

    private void mostrarCards(HttpServletRequest request) {
        List<Map<String, Object>> kpis = new ArrayList<>();
        kpis.add(criarKpi("Usuários", "kpi-usuarios", daoUsuarios.contar(),
                daoUsuarios.contarCadastradosNoMes(0), daoUsuarios.contarCadastradosNoMes(1)));
        kpis.add(criarKpi("Condomínios", "kpi-condominios", daoCondominios.contar(), null, null));
        kpis.add(criarKpi("Cooperativas", "kpi-cooperativas", daoCooperativas.contar(),
                daoCooperativas.contarCadastradasNoMes(0), daoCooperativas.contarCadastradasNoMes(1)));
        kpis.add(criarKpi("Parcerias ativas", "kpi-parcerias", daoParcerias.contarAtivas(),
                daoParcerias.contarIniciadasNoMes(0), daoParcerias.contarIniciadasNoMes(1)));
        request.setAttribute("kpis", kpis);
    }

    //Card do painel. Sem dados do mês atual e do anterior, a variação fica "—".
    private Map<String, Object> criarKpi(String titulo, String icone, int valor, Integer atual, Integer anterior) {
        Map<String, Object> kpi = new LinkedHashMap<>();
        kpi.put("titulo", titulo);
        kpi.put("icone", icone);
        kpi.put("valor", valor);
        if (atual == null || anterior == null) {
            kpi.put("tendencia", "alta");
            kpi.put("variacao", "—");
        } else {
            int porcentagem = anterior == 0 ? (atual > 0 ? 100 : 0) : (atual - anterior) * 100 / anterior;
            kpi.put("tendencia", porcentagem >= 0 ? "alta" : "baixa");
            kpi.put("variacao", Math.abs(porcentagem) + "% vs mês anterior");
        }
        return kpi;
    }

    //Cadastros por mês (últimos 7) de cada tipo de conta, já em coordenadas do SVG.
    private void mostrarGrafico(HttpServletRequest request) {
        int[] usuarios = daoUsuarios.contarCadastrosPorMes("usuario");
        int[] sindicos = daoUsuarios.contarCadastrosPorMes("sindico");
        int[] cooperativas = daoUsuarios.contarCadastrosPorMes("cooperativa");

        int maximo = 1;
        int total = 0;
        for (int i = 0; i < 7; i++) {
            maximo = Math.max(maximo, Math.max(usuarios[i], Math.max(sindicos[i], cooperativas[i])));
            total += usuarios[i] + sindicos[i] + cooperativas[i];
        }

        String linhaUsuarios = montarPontos(usuarios, maximo);
        request.setAttribute("totalCadastros", total);
        request.setAttribute("pontosUsuarios", linhaUsuarios);
        request.setAttribute("pontosSindicos", montarPontos(sindicos, maximo));
        request.setAttribute("pontosCooperativas", montarPontos(cooperativas, maximo));
        request.setAttribute("areaUsuarios", X_MESES[0] + "," + Y_BASE + " " + linhaUsuarios + " " + X_MESES[6] + "," + Y_BASE);
        request.setAttribute("ultimoUsuarios", X_MESES[6] + "," + posicaoVertical(usuarios[6], maximo));
        request.setAttribute("ultimoSindicos", X_MESES[6] + "," + posicaoVertical(sindicos[6], maximo));
        request.setAttribute("ultimoCooperativas", X_MESES[6] + "," + posicaoVertical(cooperativas[6], maximo));
        request.setAttribute("mesesGrafico", nomesDosMeses());
        request.setAttribute("categorias", new ArrayList<>());
    }

    private String montarPontos(int[] valores, int maximo) {
        StringBuilder pontos = new StringBuilder();
        for (int i = 0; i < valores.length; i++) {
            if (i > 0) pontos.append(' ');
            pontos.append(X_MESES[i]).append(',').append(posicaoVertical(valores[i], maximo));
        }
        return pontos.toString();
    }

    //No SVG o y cresce para baixo: o maior valor fica no topo e o zero na base.
    private String posicaoVertical(int valor, int maximo) {
        return String.format(Locale.ROOT, "%.1f", Y_BASE - (Y_BASE - Y_TOPO) * valor / maximo);
    }

    //Nomes dos 7 meses do gráfico, do mais antigo ao atual (ex.: Abr, Mai ... Out).
    private List<String> nomesDosMeses() {
        String[] nomes = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};
        List<String> meses = new ArrayList<>();
        YearMonth atual = YearMonth.now();
        for (int i = 6; i >= 0; i--) {
            meses.add(nomes[atual.minusMonths(i).getMonthValue() - 1]);
        }
        return meses;
    }
}
