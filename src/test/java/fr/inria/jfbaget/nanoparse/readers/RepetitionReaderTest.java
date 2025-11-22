package fr.inria.jfbaget.nanoparse.readers;

import static org.junit.jupiter.api.Assertions.*;

import fr.inria.jfbaget.nanoparse.IMatch;
import fr.inria.jfbaget.nanoparse.Parser;
import fr.inria.jfbaget.nanoparse.matches.ListMatch;
import fr.inria.jfbaget.nanoparse.matches.StringMatch;
import org.junit.jupiter.api.Test;

import java.util.List;

class RepetitionReaderTest {

    @Test
    void zeroOrMoreNoSeparator() {
        Parser parser = new Parser(List.of(
                new StringReader("lit_foo", "foo", false),
                new RepetitionReader("rep_foo", "lit_foo", null, 0, 10, false)
        ));

        // "foofoo" → two matches
        ListMatch match = (ListMatch) parser.read("foofoo", 0, "rep_foo");
        assertTrue(match.success());
        assertEquals(0, match.start());
        assertEquals(6, match.end());
        assertEquals(2, match.result().size());

        // "" → zero matches, still success
        ListMatch emptyMatch = (ListMatch) parser.read("", 0, "rep_foo");
        assertTrue(emptyMatch.success());
        assertEquals(0, emptyMatch.start());
        assertEquals(0, emptyMatch.end());
    }

    @Test
    void oneOrMoreNoSeparator() {
        Parser parser = new Parser(List.of(
                new StringReader("lit_foo", "foo", false),
                new RepetitionReader("rep_foo", "lit_foo", null, 1, 10, false)
        ));

        // "foofoo" → success with 2
        ListMatch match = (ListMatch) parser.read("foofoo", 0, "rep_foo");
        assertTrue(match.success());
        assertEquals(2, match.result().size());

        // "" → failure, min = 1 not reached
        ListMatch fail = (ListMatch) parser.read("", 0, "rep_foo");
        assertFalse(fail.success());
        assertEquals(0, fail.start());
        assertEquals(0, fail.end());
    }

    @Test
    void separatedList() {
        Parser parser = new Parser(List.of(
                new StringReader("lit_foo", "foo", false),
                new StringReader("comma", ",", false),
                new RepetitionReader("rep_foo_comma", "lit_foo", "comma", 1, 10, false)
        ));

        // foo,foo,foo
        ListMatch match = (ListMatch) parser.read("foo,foo,foo", 0, "rep_foo_comma");
        assertTrue(match.success());
        assertEquals(0, match.start());
        assertEquals(11, match.end());
        // elements are just the "foo" matches (separators are not stored)
        assertEquals(3, match.result().size());
    }

    @Test
    void subReaderWhenOneReturnsInnerMatch() {
        Parser parser = new Parser(List.of(
                new StringReader("lit_foo", "foo", false),
                new RepetitionReader("rep_foo_one", "lit_foo", null, 1, 1, false, true, false)
        ));

        IMatch match = parser.read("foo", 0, "rep_foo_one");
        assertTrue(match instanceof StringMatch);
        StringMatch s = (StringMatch) match;

        assertTrue(s.success());
        assertEquals("foo", s.result());
        assertEquals(0, s.start());
        assertEquals(3, s.end());
    }

    @Test
    void separatedListStoresSeparators() {
        Parser parser = new Parser(List.of(
                new StringReader("lit_foo", "foo", false),
                new StringReader("comma", ",", false),
                new RepetitionReader("rep_foo_comma_store",
                        "lit_foo", "comma", 1, 10, false,
                        false, true) // storeSep = true
        ));

        // foo,foo
        ListMatch match = (ListMatch) parser.read("foo,foo", 0, "rep_foo_comma_store");
        assertTrue(match.success());
        // result contains [sep, elem] pairs for each repetition after the first:
        // firstMatch, then ListMatch([sep, elem])
        assertEquals(2, match.result().size());
    }
}