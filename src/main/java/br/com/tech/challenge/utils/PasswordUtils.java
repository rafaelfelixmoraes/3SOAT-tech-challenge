package br.com.tech.challenge.utils;

import lombok.experimental.UtilityClass;

import java.util.Random;

@UtilityClass
public class PasswordUtils {

    private static final Random RANDOM = new Random();

    public static Integer generatePassword() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 6; i++)
            sb.append(RANDOM.nextInt(10));

        return Integer.parseInt(sb.toString());
    }

}
