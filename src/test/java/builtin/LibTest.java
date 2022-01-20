package builtin;

import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class LibTest {
    @Test
    public void TestStreamSkip() {
        List<Integer> actual = Stream.of(101, 22, 3, 45, 6)
                .skip(3)
                .collect(Collectors.toList());
        assertEquals(45, (int) actual.get(0));
        assertEquals(6, (int) actual.get(1));
    }
}
