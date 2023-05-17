package org.nico.quotedserver.util;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestUtilTest {
    public static int countIterable(Iterable<?> iterable) {
        AtomicInteger count = new AtomicInteger();
        iterable.forEach(i -> count.getAndIncrement());
        return count.get();
    }

    @Test
    void testCountIterable() {
        Iterable<Integer> iterable = () -> new java.util.Iterator<>() {
            int count = 0;

            @Override
            public boolean hasNext() {
                return count < 10;
            }

            @Override
            public Integer next() {
                return count++;
            }
        };
        assertEquals(10, countIterable(iterable));
    }
}
