package com.github.julianps.vavr.playground.controls;

import io.vavr.control.Option;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OptionTest {

    @Tags({@Tag("benefits"), @Tag("option")})
    @DisplayName("shows the benefits of option.")
    @Test
    public void testOption() {

        // In Optional, a call to .map that results in a null will result in an empty Optional.
        Optional<String> maybeFoo = Optional.of("foo");
        assertThat(maybeFoo.map(s -> (String) null)
                .map(s -> s.toUpperCase() + "bar")).isEqualTo(Optional.empty());

        // Option forces you to pay attention to possible occurrences of null and deal with
        // them accordingly instead of unknowingly accepting them
        Option<String> maybeFooBar = Option.of("foo");
        assertThrows(NullPointerException.class, () ->
                maybeFooBar.map(s -> (String) null)
                        .map(s -> s.toUpperCase() + "bar"));

        // The correct way to deal with occurrences of null is to use flatMap
        assertDoesNotThrow(() ->
                maybeFooBar.flatMap(s -> Option.of((String) null))
                        .map(s -> s.toUpperCase() + "bar"));

    }
}
