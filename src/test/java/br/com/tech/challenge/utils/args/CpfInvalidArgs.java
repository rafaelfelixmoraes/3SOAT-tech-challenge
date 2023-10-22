package br.com.tech.challenge.utils.args;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class CpfInvalidArgs implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of("123.456.789-01"),
                Arguments.of("987.654.321-98"),
                Arguments.of("555.111.888-77"),
                Arguments.of("333.222.111-00"),
                Arguments.of("999.888.777-66"),
                Arguments.of("444.333.222-11"),
                Arguments.of("777.888.999-22"),
                Arguments.of("666.555.444-33"),
                Arguments.of("222.333.444-55"),
                Arguments.of("000.111.222-33")
        );
    }

}
