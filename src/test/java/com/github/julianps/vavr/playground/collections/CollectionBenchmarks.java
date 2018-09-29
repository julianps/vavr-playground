package com.github.julianps.vavr.playground.collections;

import org.junit.jupiter.api.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CollectionBenchmarks {

    @State(Scope.Thread)
    public static class BenchmarkState {

        List<Integer> list;

        @Setup(Level.Trial)
        public void initialize() {
            Random rand = new Random();
            list = new ArrayList<>();
            for (int i = 0; i < 1000; i++) {
                list.add(rand.nextInt());
            }
        }
    }

    @Benchmark
    public void benchmark1 (BenchmarkState state, Blackhole bh) {
        List<Integer> list = state.list;
        for (int i = 0; i < 1000; i++)
            bh.consume (list.get (i));
    }

    @Test
    public void initBenchmarkTest() throws RunnerException {
        Options options = new OptionsBuilder()
                .include(this.getClass().getName() + ".*")
                .mode(Mode.AverageTime)
                .timeUnit(TimeUnit.MICROSECONDS)
                .warmupTime(TimeValue.seconds(1))
                .warmupIterations(2)
                .measurementTime(TimeValue.seconds(1))
                .measurementIterations(2)
                .threads(2)
                .forks(1)
                .shouldFailOnError(true)
                .shouldDoGC(true)
                .build();
        new Runner(options).run();
    }
}
