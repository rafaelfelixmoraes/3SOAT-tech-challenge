package br.com.tech.challenge.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordUtilsTest {

    @DisplayName("Deve gerar uma senha de 6 dígitos")
    @RepeatedTest(3)
    void shouldGeneratePassword() {
        var senha = PasswordUtils.generatePassword();
        Assertions.assertEquals(6, String.valueOf(senha).length());
        Assertions.assertEquals(Integer.class, senha.getClass());
    }

    @DisplayName("Deve codificar senha")
    @Test
    void shouldEncodePassword() {
        var password = "123456";
        var encodedPassword = PasswordUtils.encodePassword(password);
        assertNotEquals(password, encodedPassword);
    }

    @DisplayName("Deve ter senhas que correspondem")
    @Test
    void shouldMatchPasswords() {
        var password = "12345";
        var passwordEncoded = PasswordUtils.encodePassword(password);
        assertTrue(PasswordUtils.passwordsMatch(password, passwordEncoded));
    }

}
