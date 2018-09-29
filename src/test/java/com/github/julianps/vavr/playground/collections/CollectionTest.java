package com.github.julianps.vavr.playground.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class CollectionTest {

    public static final String FIRST_NAME = "Sebastian";

    @Test
    @DisplayName("Provides us with an immutable view on the specific collection."
                 + "Typically it will throw at runtime when we call a mutator method")
    public void testImmutableJavaList() {
        final List<String> names = Arrays.asList("Helmut", "Frank", "Theo");
        final List<String> namesImmutable = Collections.unmodifiableList(names);
        assertThrows(UnsupportedOperationException.class,
                () -> namesImmutable.add(FIRST_NAME));
    }

    @Test
    @DisplayName("Proves that javas own collections are not persistent data structures")
    public void testPersistantDataStructureAbsent() {
        final java.util.LinkedList<Integer> javaNames = new java.util.LinkedList<>(Arrays.asList(1, 2, 3));
        final io.vavr.collection.List<Integer> vavrNames = io.vavr.collection.List.ofAll(1, 2, 3);
        javaNames.add(4);
        vavrNames.append(4);

        System.out.println("java: " + javaNames.toString());
        System.out.println("vavr: " + vavrNames.toString());

        assertThat(javaNames).containsExactly(1, 2, 3, 4);
        assertThat(vavrNames).containsExactly(1, 2, 3);
    }

    @RepeatedTest(100)
    @DisplayName("demonstrates that the expression will always result in the same value."
                 + "referential transparency is given.")
    public void testReferentialTransparent() {
        assertThat(Math.max(1, 2)).isEqualTo(2);
    }

    @RepeatedTest(100)
    @DisplayName("demonstrates that the referential transparency is NOT given. "
                 + "The chance for a success testrun in nearly 0")
    public void testNOTReferentialTransparent() {
        assertThat(Math.random()).isNotEqualTo(Math.random());
    }

}
