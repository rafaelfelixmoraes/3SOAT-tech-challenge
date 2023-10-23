package br.com.tech.challenge.utils;

import br.com.tech.challenge.utils.args.CpfFormatArgs;
import br.com.tech.challenge.utils.args.CpfInvalidArgs;
import br.com.tech.challenge.utils.args.CpfValidArgs;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CpfUtilsTest {

    @DisplayName("Deve retornar true quando cpf é válido")
    @ParameterizedTest
    @ArgumentsSource(CpfValidArgs.class)
    void shouldReturnTrueToValidCpfs(String cpf) {
        assertTrue(CpfUtils.isCpfValid(cpf));
    }

    @DisplayName("Deve retornar false quando cpf é inválido")
    @ParameterizedTest
    @ArgumentsSource(CpfInvalidArgs.class)
    void shouldReturnFalseToInvalidCpfs(String cpf) {
        assertFalse(CpfUtils.isCpfValid(cpf));
    }

    @DisplayName("Deve formatar cpfs corretamente")
    @ParameterizedTest
    @ArgumentsSource(CpfFormatArgs.class)
    void shouldFormatCpfCorrectly(String cpf, String formattedCpf) {
        assertEquals(CpfUtils.formatCpf(cpf), formattedCpf);
    }

}
