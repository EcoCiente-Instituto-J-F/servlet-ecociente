package com.example.ecociente.util;

import com.example.ecociente.model.Endereco;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDate;
import java.util.List;
import java.time.format.DateTimeParseException;

// Leitura e validação dos campos que vêm dos formulários (usado pelos servlets do CRUD de admin)
public class Formulario {

    private static final String REGEX_EMAIL = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*\\.[A-Za-z]{2,}$";

    public static final int POR_PAGINA = 8;

    //Recorta a lista na pagina pedida (?pagina=) e manda paginaAtual/totalPaginas para a JSP de paginacao.
    public static <T> List<T> paginar(HttpServletRequest request, List<T> linhas) {
        int total = Math.max(1, (int) Math.ceil(linhas.size() / (double) POR_PAGINA));
        int pagina = 1;
        try {
            pagina = Integer.parseInt(request.getParameter("pagina"));
        } catch (NumberFormatException e) {
            //sem pagina valida fica na primeira
        }
        pagina = Math.min(Math.max(pagina, 1), total);

        request.setAttribute("paginaAtual", pagina);
        request.setAttribute("totalPaginas", total);
        int de = (pagina - 1) * POR_PAGINA;
        return linhas.subList(de, Math.min(de + POR_PAGINA, linhas.size()));
    }

    public static String texto(HttpServletRequest request, String nome) {
        String valor = request.getParameter(nome);
        return valor == null ? "" : valor.trim();
    }

    public static String digitos(String valor) {
        return valor == null ? "" : valor.replaceAll("\\D", "");
    }

    public static String exigir(String valor, String rotulo, int maximo) {
        if (valor == null || valor.isBlank()) throw new DadoInvalido("Informe " + rotulo + ".");
        if (valor.length() > maximo) throw new DadoInvalido(rotulo.substring(0, 1).toUpperCase() + rotulo.substring(1) + " pode ter no máximo " + maximo + " caracteres.");
        return valor;
    }

    public static int id(String valor, String rotulo) {
        try {
            return Integer.parseInt(valor.trim());
        } catch (NumberFormatException | NullPointerException e) {
            throw new DadoInvalido("Identificador de " + rotulo + " inválido.");
        }
    }

    public static String email(String valor) {
        exigir(valor, "o e-mail", 150);
        if (!valor.matches(REGEX_EMAIL)) throw new DadoInvalido("Informe um e-mail válido.");
        return valor.toLowerCase();
    }

    public static String cnpj(String valor) {
        String digitos = digitos(valor);
        if (digitos.length() != 14) throw new DadoInvalido("CNPJ inválido. Informe os 14 dígitos.");
        return digitos;
    }

    // Opcional: vazio vira null; preenchido precisa ter DDD + 8 ou 9 dígitos
    public static String telefone(String valor) {
        String digitos = digitos(valor);
        if (digitos.isEmpty()) return null;
        if (digitos.length() < 10 || digitos.length() > 11) throw new DadoInvalido("Telefone inválido. Use o formato (11) 91234-5678.");
        return digitos;
    }

    public static LocalDate data(String valor, String rotulo) {
        if (valor == null || valor.isBlank()) return null;
        try {
            return LocalDate.parse(valor.trim());
        } catch (DateTimeParseException e) {
            throw new DadoInvalido("Data " + rotulo + " inválida.");
        }
    }

    // Lê cep, cidade, estado, bairro, rua, numero e complemento. Tudo vazio: null (se não obrigatório).
    public static Endereco endereco(HttpServletRequest request, boolean obrigatorio) {
        String cep = digitos(request.getParameter("cep"));
        String cidade = texto(request, "cidade");
        String estado = texto(request, "estado").toUpperCase();
        String bairro = texto(request, "bairro");
        String rua = texto(request, "rua");
        String numero = texto(request, "numero");
        String complemento = texto(request, "complemento");

        if (cep.isEmpty() && cidade.isEmpty() && estado.isEmpty() && rua.isEmpty() && numero.isEmpty()) {
            if (obrigatorio) throw new DadoInvalido("Informe o endereço completo.");
            return null;
        }

        if (cep.length() != 8) throw new DadoInvalido("CEP inválido. Informe os 8 dígitos.");
        exigir(cidade, "a cidade", 100);
        if (!estado.matches("[A-Z]{2}")) throw new DadoInvalido("Estado inválido. Use a sigla com 2 letras.");
        exigir(rua, "a rua", 100);
        if (bairro.length() > 100) throw new DadoInvalido("O bairro pode ter no máximo 100 caracteres.");
        if (complemento.length() > 100) throw new DadoInvalido("O complemento pode ter no máximo 100 caracteres.");

        int numeroInt;
        try {
            numeroInt = Integer.parseInt(numero);
            if (numeroInt < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            throw new DadoInvalido("Número do endereço inválido.");
        }

        return new Endereco(0, cep, cidade, estado, bairro.isEmpty() ? null : bairro, rua, numeroInt,
                complemento.isEmpty() ? null : complemento);
    }
}
