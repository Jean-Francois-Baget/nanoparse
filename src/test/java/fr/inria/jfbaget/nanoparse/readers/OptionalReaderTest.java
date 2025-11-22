package fr.inria.jfbaget.nanoparse.readers;

import static org.junit.jupiter.api.Assertions.*;

import fr.inria.jfbaget.nanoparse.IMatch;
import fr.inria.jfbaget.nanoparse.Parser;
import fr.inria.jfbaget.nanoparse.matches.StringMatch;
import org.junit.jupiter.api.Test;

import java.util.List;

class OptionalReaderTest {

    @Test
    void optionalPresent() {
        Parser parser = new Parser(List.of(
                new StringReader("lit_foo", "foo", false),
                new OptionalReader("opt_foo", "lit_foo", false)
        ));

        StringMatch match = (StringMatch) parser.read("foobar", 0, "opt_foo");

        assertTrue(match.success());
        assertEquals("foo", match.result());
        assertEquals(0, match.start());
        assertEquals(3, match.end());
    }

    @Test
    void optionalAbsent() {
        Parser parser = new Parser(List.of(
                new StringReader("lit_foo", "foo", false),
                new OptionalReader("opt_foo", "lit_foo", false)
        ));

        IMatch match = parser.read("bar", 0, "opt_foo");

        assertTrue(match.success());
        assertEquals(0, match.start());
        assertEquals(0, match.end());
        assertNull(match.result());
    }

    @Test
    void optionalWithSkip() {
        Parser parser = new Parser(List.of(
                new StringReader("lit_foo", "foo", false),
                new OptionalReader("opt_foo", "lit_foo", true)
        ));

        IMatch match = parser.read("   foo", 0, "opt_foo");

        assertTrue(match.success());
        // skip should consume 3 spaces before matching "foo"
        assertEquals(3, match.start());
        assertEquals(6, match.end());
    }
}
