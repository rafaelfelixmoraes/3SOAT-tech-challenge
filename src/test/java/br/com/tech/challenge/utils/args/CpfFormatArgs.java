package br.com.tech.challenge.utils.args;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

public class CpfFormatArgs implements ArgumentsProvider {

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
        return Stream.of(
                Arguments.of("86518083050", "865.180.830-50"),
                Arguments.of("44663866093", "446.638.660-93"),
                Arguments.of("91566723060", "915.667.230-60"),
                Arguments.of("90109868021", "901.098.680-21"),
                Arguments.of("60125063091", "601.250.630-91"),
                Arguments.of("24098507072", "240.985.070-72"),
                Arguments.of("52361107007", "523.611.070-07"),
                Arguments.of("53872615025", "538.726.150-25"),
                Arguments.of("01936686023", "019.366.860-23"),
                Arguments.of("64066587011", "640.665.870-11")
        );
    }

}
