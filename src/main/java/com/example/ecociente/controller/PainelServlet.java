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
import java.time.LocalDate;
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

    //Limites do desenho do gráfico (coordenadas do SVG da JSP, viewBox 0 0 1163 328): x de janeiro e de dezembro, y do topo e da base.
    private static final double X_INICIO = 70, X_FIM = 1093;
    private static final double Y_TOPO = 110, Y_BASE = 255;
    private static final String[] NOMES_MESES = {"Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez"};

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

    //Cadastros de cada mês do ano escolhido (?ano=) de cada tipo de conta, já em coordenadas do SVG.
    private void mostrarGrafico(HttpServletRequest request) {
        List<Integer> anos = daoUsuarios.listarAnosComCadastro();

        //Ano ausente, inválido ou sem cadastro volta para o ano atual.
        int ano = LocalDate.now().getYear();
        try {
            int pedido = Integer.parseInt(request.getParameter("ano"));
            if (anos.contains(pedido)) ano = pedido;
        } catch (NumberFormatException nfe) {
            //sem ano válido fica no ano atual
        }

        Integer[] usuarios = daoUsuarios.contarCadastrosPorMes("usuario", ano);
        Integer[] sindicos = daoUsuarios.contarCadastrosPorMes("sindico", ano);
        Integer[] cooperativas = daoUsuarios.contarCadastrosPorMes("cooperativa", ano);

        //Meses que ainda não chegaram vêm null e ficam de fora da escala e do total.
        int maximo = 1;
        int total = 0;
        for (Integer[] serie : new Integer[][]{usuarios, sindicos, cooperativas}) {
            for (Integer valor : serie) {
                if (valor == null) continue;
                maximo = Math.max(maximo, valor);
                total += valor;
            }
        }

        String linhaUsuarios = montarPontos(usuarios, maximo);
        request.setAttribute("anosDisponiveis", anos);
        request.setAttribute("anoSelecionado", ano);
        request.setAttribute("totalCadastros", total);
        request.setAttribute("pontosUsuarios", linhaUsuarios);
        request.setAttribute("pontosSindicos", montarPontos(sindicos, maximo));
        request.setAttribute("pontosCooperativas", montarPontos(cooperativas, maximo));
        request.setAttribute("areaUsuarios", montarArea(usuarios, linhaUsuarios));
        request.setAttribute("marcasUsuarios", montarMarcas(usuarios, maximo));
        request.setAttribute("marcasSindicos", montarMarcas(sindicos, maximo));
        request.setAttribute("marcasCooperativas", montarMarcas(cooperativas, maximo));
        request.setAttribute("mesesGrafico", nomesDosMeses());
    }

    //Pontos "x,y x,y ..." da linha, só dos meses que já chegaram.
    private String montarPontos(Integer[] valores, int maximo) {
        StringBuilder pontos = new StringBuilder();
        for (int i = 0; i < valores.length; i++) {
            if (valores[i] == null) continue;
            if (pontos.length() > 0) pontos.append(' ');
            pontos.append(formatar(posicaoHorizontal(i))).append(',').append(posicaoVertical(valores[i], maximo));
        }
        return pontos.toString();
    }

    //Área sob a linha: desce até a base no primeiro e no último mês com dado. Vazia se não houver pontos.
    private String montarArea(Integer[] valores, String pontos) {
        int primeiro = -1;
        int ultimo = -1;
        for (int i = 0; i < valores.length; i++) {
            if (valores[i] == null) continue;
            if (primeiro < 0) primeiro = i;
            ultimo = i;
        }
        if (primeiro < 0) return "";
        return formatar(posicaoHorizontal(primeiro)) + "," + formatar(Y_BASE) + " " + pontos + " "
                + formatar(posicaoHorizontal(ultimo)) + "," + formatar(Y_BASE);
    }

    //Um círculo com o número em cada mês que já chegou.
    private List<Map<String, Object>> montarMarcas(Integer[] valores, int maximo) {
        List<Map<String, Object>> marcas = new ArrayList<>();
        for (int i = 0; i < valores.length; i++) {
            if (valores[i] == null) continue;
            Map<String, Object> marca = new LinkedHashMap<>();
            marca.put("x", formatar(posicaoHorizontal(i)));
            marca.put("y", posicaoVertical(valores[i], maximo));
            marca.put("valor", valores[i]);
            marcas.add(marca);
        }
        return marcas;
    }

    //Os 12 meses (Jan a Dez) com o x de cada rótulo.
    private List<Map<String, Object>> nomesDosMeses() {
        List<Map<String, Object>> meses = new ArrayList<>();
        for (int i = 0; i < NOMES_MESES.length; i++) {
            Map<String, Object> mes = new LinkedHashMap<>();
            mes.put("nome", NOMES_MESES[i]);
            mes.put("x", formatar(posicaoHorizontal(i)));
            meses.add(mes);
        }
        return meses;
    }

    //O mês 0 (janeiro) fica em X_INICIO e o mês 11 (dezembro) em X_FIM.
    private double posicaoHorizontal(int mes) {
        return X_INICIO + mes * (X_FIM - X_INICIO) / 11;
    }

    //No SVG o y cresce para baixo: o maior valor fica no topo e o zero na base.
    private String posicaoVertical(int valor, int maximo) {
        return formatar(Y_BASE - (Y_BASE - Y_TOPO) * valor / maximo);
    }

    //Uma casa decimal e ponto (não vírgula), como o SVG exige.
    private String formatar(double numero) {
        return String.format(Locale.ROOT, "%.1f", numero);
    }
}
