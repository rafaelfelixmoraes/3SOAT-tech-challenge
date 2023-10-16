package br.com.tech.challenge.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

class PasswordUtilsTest {

    @DisplayName("Deve gerar uma senha de 6 dígitos")
    @RepeatedTest(3)
    void shouldGeneratePassword() {
        var senha = PasswordUtils.generatePassword();
        Assertions.assertEquals(6, String.valueOf(senha).length());
        Assertions.assertEquals(Integer.class, senha.getClass());
    }

}
