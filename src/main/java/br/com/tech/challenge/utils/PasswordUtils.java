package br.com.tech.challenge.utils;

import lombok.experimental.UtilityClass;

import java.util.Random;

import static java.lang.Integer.parseInt;

@UtilityClass
public class PasswordUtils {

    private static final Random RANDOM = new Random();
    private static final int PASSWORD_LENGTH = 6;
    private static final int MIN = 1;
    private static final int MAX = 9;

    public static Integer generatePassword() {
        var sb = new StringBuilder();

        for (int i = 0; i < PASSWORD_LENGTH; i++)
            sb.append(RANDOM.nextInt(MAX) + MIN);

        return parseInt(sb.toString());
    }

}
