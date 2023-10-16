package br.com.tech.challenge.utils.args;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class CpfValidArgs implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of("865.180.830-50"),
                Arguments.of("446.638.660-93"),
                Arguments.of("915.667.230-60"),
                Arguments.of("901.098.680-21"),
                Arguments.of("601.250.630-91"),
                Arguments.of("240.985.070-72"),
                Arguments.of("523.611.070-07"),
                Arguments.of("538.726.150-25"),
                Arguments.of("019.366.860-23"),
                Arguments.of("640.665.870-11")
        );
    }

}
