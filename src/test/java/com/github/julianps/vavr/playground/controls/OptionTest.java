package com.github.julianps.vavr.playground.controls;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OptionTest {

    @Tags({ @Tag("benefits"), @Tag("option") })
    @DisplayName("shows the benefits of option.")
    @Test
    public void testOption() {

        // In Optional, a call to .map that results in a null will result in an empty Optional.
        java.util.Optional<String> maybeFoo = java.util.Optional.of("foo");
        assertThat(maybeFoo.map(s -> (String) null)
                .map(s -> s.toUpperCase() + "bar")).isEqualTo(java.util.Optional.empty());

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

    @Data
    @AllArgsConstructor(staticName = "of") final static class User {
        private String name;
    }

    @Tags({ @Tag("benefits"), @Tag("option") })
    @DisplayName("shows some syntactic sugar")
    @Test
    public void testOptionSyntacticSugar() {
        final java.util.List<User> usersSequence = Arrays.asList(User.of("Lisa"), User.of("Julian"), User.of("Milla"));
        final User user = User.of("Ralf");

        // both following variants use 4 Lines of code plus comments

        // 1
        final java.util.List<User> users = new ArrayList<>(usersSequence);
        final java.util.Optional<User> optionalUser = java.util.Optional.ofNullable(user);
        // users.add(optionalUser) is not possible (CompileError)
        // instead we need to use:
        optionalUser.map(users::add); // are we actually working on the user?
        assertThat(users.size()).isEqualTo(4);

        // 2
        io.vavr.collection.List<User> usersInVavr = io.vavr.collection.List.ofAll(usersSequence);
        final Option<User> optionalVavrUser = Option.of(user);
        // here we appendAll (because vavr treats Option as a List consisting of Some and None
        final io.vavr.collection.List<User> newUsersInVavr = usersInVavr.appendAll(optionalVavrUser); // perform an action on the list.
        assertThat(newUsersInVavr.size()).isEqualTo(4);

        // Some more syntactic sugar can be found here: https://dev.to/koenighotze/in-praise-of-vavrs-option
    }

}

