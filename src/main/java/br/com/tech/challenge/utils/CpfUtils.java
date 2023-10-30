package br.com.tech.challenge.utils;

import br.com.tech.challenge.api.exception.InvalidCpfException;

public class CpfUtils {

    public static boolean isCpfValid(String cpf) {

        try {
            // Remova todos os caracteres não numéricos
            cpf = cpf.replaceAll("[^0-9]", "");

            // Verifique se o CPF possui 11 dígitos
            if (cpf.length() != 11) {
                return false;
            }

            // Verifique se todos os dígitos são iguais (CPF inválido)
            if (cpf.matches("(\\d)\\1{10}")) {
                return false;
            }

            int[] digitos = new int[11];
            for (int i = 0; i < 11; i++) {
                digitos[i] = Integer.parseInt(cpf.substring(i, i + 1));
            }

            int soma1 = 0;
            int soma2 = 0;

            for (int i = 0; i < 9; i++) {
                soma1 += digitos[i] * (10 - i);
                soma2 += digitos[i] * (11 - i);
            }

            int resto1 = soma1 % 11;
            int digito1 = (resto1 < 2) ? 0 : (11 - resto1);

            int resto2 = (soma2 + digito1 * 2) % 11;
            int digito2 = (resto2 < 2) ? 0 : (11 - resto2);

            // Verifique se os dígitos verificadores calculados coincidem com os dígitos reais
           boolean validCpf =  (digitos[9] == digito1) && (digitos[10] == digito2);

           if(!validCpf){
               throw new InvalidCpfException("CPF inválido: " + cpf);
           }
           return true;

        } catch (Exception ex) {
            throw new InvalidCpfException("CPF inválido: " + cpf);
        }

    }

    public static String formatCpf(String cpf) {
        // Remove todos os caracteres não numéricos
        cpf = cpf.replaceAll("[^0-9]", "");

        // Verifica se o CPF possui 11 dígitos
        if (cpf.length() != 11) {
            throw new InvalidCpfException("O CPF deve conter 11 dígitos numéricos.");
        }

        // Formata o CPF no padrão "667.743.160-69"
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-" + cpf.substring(9);
    }
}

