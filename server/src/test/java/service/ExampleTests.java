package java.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ExampleTests {
    @Test
    public void simpleAssertionTest() {
        assertEquals(200, 100 + 100);
        assertTrue(100 == 2 * 50);
        assertNotNull(new Object(), "Response did not return authentication String");
    }
}