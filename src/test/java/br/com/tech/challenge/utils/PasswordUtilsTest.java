package br.com.tech.challenge.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
class PasswordUtilsTest {

    @DisplayName("Deve gerar uma senha de 6 dígitos")
    @Test
    void generatePasswordTest() {
        var senha = PasswordUtils.generatePassword();
        Assertions.assertEquals(6, String.valueOf(senha).length());
        Assertions.assertEquals(Integer.class, senha.getClass());
    }

}
