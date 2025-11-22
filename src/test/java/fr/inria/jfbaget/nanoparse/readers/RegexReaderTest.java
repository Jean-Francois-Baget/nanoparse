package fr.inria.jfbaget.nanoparse.readers;

import static org.junit.jupiter.api.Assertions.*;

import fr.inria.jfbaget.nanoparse.Parser;
import fr.inria.jfbaget.nanoparse.matches.StringMatch;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.PatternSyntaxException;

class RegexReaderTest {

    @Test
    void testSimpleMatchAtStart() throws PatternSyntaxException {
        RegexReader reader = new RegexReader("", "(foo)+", false);
        StringMatch match = (StringMatch) reader.read("foofoobar", 0);

        assertEquals(true, match.success());
        assertEquals("foofoo", match.result());
        assertEquals(0, match.start());
        assertEquals(6, match.end());
    }

    @Test
    void testSimpleMatchLater() throws PatternSyntaxException {
        RegexReader reader = new RegexReader("", "(foo)+", false);
        StringMatch match = (StringMatch) reader.read("barfoofoo", 3);

        assertEquals(true, match.success());
        assertEquals("foofoo", match.result());
        assertEquals(3, match.start());
        assertEquals(9, match.end());
    }

    @Test
    void testFailureTooShort() throws PatternSyntaxException {
        RegexReader reader = new RegexReader("", "foo", false);
        StringMatch match = (StringMatch) reader.read("fo", 0);

        assertEquals(false, match.success());
        assertEquals(0, match.start());
        assertEquals(0, match.end());
        assertNull(match.result());
    }

    @Test
    void testSkipBeforeRegex() throws PatternSyntaxException {
        Parser parser = new Parser(List.of(
                new RegexReader("main", "(foo)+", true)
        ));

        StringMatch match = (StringMatch) parser.read("  foofoo", 0);

        assertEquals(true, match.success());
        // skip should consume the two spaces, so the match starts at index 2
        assertEquals(2, match.start());
        assertEquals(8, match.end());
        assertEquals("foofoo", match.result());
    }

    @Test
    void testRegexWithPattern() throws PatternSyntaxException {
        // \w+ should match "foo123" at the beginning
        RegexReader reader = new RegexReader("", "\\w+", false);
        StringMatch match = (StringMatch) reader.read("foo123 bar", 0);

        assertEquals(true, match.success());
        assertEquals("foo123", match.result());
        assertEquals(0, match.start());
        assertEquals(6, match.end());
    }
}