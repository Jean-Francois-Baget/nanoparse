package fr.inria.jfbaget.nanoparse.readers;

import static org.junit.jupiter.api.Assertions.*;

import fr.inria.jfbaget.nanoparse.IMatch;
import fr.inria.jfbaget.nanoparse.Parser;
import fr.inria.jfbaget.nanoparse.matches.StringMatch;
import org.junit.jupiter.api.Test;

import java.util.List;

class ChoiceReaderTest {

    @Test
    void firstAlternativeMatches() {
        Parser parser = new Parser(List.of(
                new StringReader("lit_foo", "foo", false),
                new StringReader("lit_bar", "bar", false),
                new ChoiceReader("choice", List.of("lit_foo", "lit_bar"), false)
        ));

        StringMatch match = (StringMatch) parser.read("foobar", 0, "choice");

        assertTrue(match.success());
        assertEquals("foo", match.result());
        assertEquals(0, match.start());
        assertEquals(3, match.end());
    }

    @Test
    void secondAlternativeMatches() {
        Parser parser = new Parser(List.of(
                new StringReader("lit_foo", "foo", false),
                new StringReader("lit_bar", "bar", false),
                new ChoiceReader("choice", List.of("lit_foo", "lit_bar"), false)
        ));

        StringMatch match = (StringMatch) parser.read("barbaz", 0, "choice");

        assertTrue(match.success());
        assertEquals("bar", match.result());
        assertEquals(0, match.start());
        assertEquals(3, match.end());
    }

    @Test
    void allAlternativesFail() {
        Parser parser = new Parser(List.of(
                new StringReader("lit_foo", "foo", false),
                new StringReader("lit_bar", "bar", false),
                new ChoiceReader("choice", List.of("lit_foo", "lit_bar"), false)
        ));

        IMatch match = parser.read("xxx", 0, "choice");

        assertFalse(match.success());
        assertEquals(0, match.start());
        assertEquals(0, match.end());
        assertNull(match.result());
    }

    @Test
    void choiceWithSkip() {
        Parser parser = new Parser(List.of(
                new StringReader("lit_foo", "foo", false),
                new StringReader("lit_bar", "bar", false),
                new ChoiceReader("choice", List.of("lit_foo", "lit_bar"), true)
        ));

        StringMatch match = (StringMatch) parser.read("   bar", 0, "choice");

        assertTrue(match.success());
        assertEquals("bar", match.result());
        assertEquals(3, match.start());
        assertEquals(6, match.end());
    }
}