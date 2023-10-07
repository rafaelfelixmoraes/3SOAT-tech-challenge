package br.com.tech.challenge.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class PasswordUtils {
    public static Integer generatePasswordByDate() {
        return Integer.valueOf(LocalDateTime.now()
                .format(DateTimeFormatter
                        .ofPattern("dd/MM/yyyy HH:mm:ss"))
                .replaceAll("[/:\\s]",""));
    }

}
