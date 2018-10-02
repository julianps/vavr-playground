package com.github.julianps.vavr.playground.collections;

import io.vavr.control.Option;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author jstuecker
 * <p>
 * Intention is to fully understand the concepts of functional data structures.
 */
public class CollectionTest {

    public static final String FIRST_NAME = "Sebastian";

    @Tag("immutable")
    @Test
    @DisplayName("Provides us with an immutable view on the specific collection."
                 + "Typically it will throw at runtime when we call a mutator method")
    public void testImmutableJavaList() {

        final List<String> names = Arrays.asList("Helmut", "Frank", "Theo");
        final List<String> namesImmutable = Collections.unmodifiableList(names);

        assertThrows(UnsupportedOperationException.class,
                () -> namesImmutable.add(FIRST_NAME));
    }

    @Tag("persistent")
    @Test
    @DisplayName("Proves that javas own collections are NOT persistent")
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

    @Tag("referential transparency")
    @RepeatedTest(100)
    @DisplayName("demonstrates that the expression will always result in the same value."
                 + "referential transparency is given.")
    public void testReferentialTransparent() {
        assertThat(Math.max(1, 2)).isEqualTo(2);
    }

    @Tag("referential transparency")
    @RepeatedTest(100)
    @DisplayName("demonstrates that the referential transparency is NOT given. "
                 + "The chance for a success testrun in nearly 0")
    public void testNOTReferentialTransparent() {
        assertThat(Math.random()).isNotEqualTo(Math.random());
    }

    @Tags({ @Tag("benefits"), @Tag("immutable") })
    @Test
    @DisplayName("Immutable buildin helpers fail at runtime, wheras vavr does not")
    public void testImmutabilityFeatureNotThrowing() {
        final List<String> family = Arrays.asList("julian", "Lisa", "Milla");
        final List<String> immutableList = Collections.unmodifiableList(family);

        Assertions.assertThrows(UnsupportedOperationException.class, () -> immutableList.add("Claudia"));

        io.vavr.collection.List vavrLinkedList = io.vavr.collection.List.of(family);

        Assertions.assertDoesNotThrow(() -> vavrLinkedList.append("Claudia"));

    }

    @Getter
    @Setter
    @AllArgsConstructor(staticName = "of")
    static final class NameWrapper {
        private String name;
    }

    @Getter
    @AllArgsConstructor(staticName = "of")
    static final class FamilyMember {
        private final NameWrapper firstName;
    }

    @Tags({ @Tag("benefits"), @Tag("Persistent") })
    @Test
    @DisplayName("Show that even with vavr, you need to create defensive copies if the "
                 + "internals consists of non-persistent data structures.")
    public void testImmutabilityOfInnerContent() throws Exception {
        final List<FamilyMember> javaFamily = Arrays.asList(
                FamilyMember.of(NameWrapper.of("Lisa")),
                FamilyMember.of(NameWrapper.of("Milla")),
                FamilyMember.of(NameWrapper.of("Julian"))
        );

        final io.vavr.collection.List<FamilyMember> vavrFamily = io.vavr.collection.List.of(
                FamilyMember.of(NameWrapper.of("Lisa")),
                FamilyMember.of(NameWrapper.of("Milla")),
                FamilyMember.of(NameWrapper.of("Julian"))
        );

        final FamilyMember firstVavrMember = vavrFamily.head();
        firstVavrMember.getFirstName().setName("Harald");
        final FamilyMember firstJavaMember = javaFamily.get(0);
        firstJavaMember.getFirstName().setName("Harald");

        assertThat(vavrFamily.get(0).getFirstName().getName()).isEqualTo("Julian");
        assertThat(javaFamily.get(0).getFirstName().getName()).isEqualTo("Julian");
    }

    @Tags({ @Tag("benefits"), @Tag("flat-map") })
    @Test
    @DisplayName("Show that the Option(none) disappear when flat-mapping")
    public void testFlatteningOfOptionNone() throws Exception {

        final io.vavr.collection.List<Option<FamilyMember>> family = io.vavr.collection.List.of(
                Option.of(FamilyMember.of(NameWrapper.of("Lisa"))),
                Option.of(FamilyMember.of(NameWrapper.of("Milla"))),
                Option.none(),
                Option.of(FamilyMember.of(NameWrapper.of("Julian"))));

        // [ Lisa, Milla, Julian ]
        final io.vavr.collection.List<FamilyMember> flattened = family.flatMap(o -> o);
        assertThat(flattened.size()).isEqualTo(3);
    }

}























