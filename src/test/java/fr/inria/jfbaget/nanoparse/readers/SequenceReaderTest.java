package fr.inria.jfbaget.nanoparse.readers;

import static org.junit.jupiter.api.Assertions.*;

import fr.inria.jfbaget.nanoparse.IMatch;
import fr.inria.jfbaget.nanoparse.IReader;
import fr.inria.jfbaget.nanoparse.Parser;
import fr.inria.jfbaget.nanoparse.matches.ListMatch;
import fr.inria.jfbaget.nanoparse.matches.ObjectMatch;
import fr.inria.jfbaget.nanoparse.matches.StringMatch;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

class SequenceReaderTest {

    private Parser buildParserForFooBar() {
        IReader rFoo = new StringReader("lit_foo", "foo", false);
        IReader rBar = new StringReader("lit_bar", "bar", false);

        IReader seqAll = new SequenceReader(
                "seq_all",
                List.of("lit_foo", "lit_bar"),
                false
        );

        IReader seqOne = new SequenceReader(
                "seq_one",
                List.of("lit_foo", "lit_bar"),
                false,
                0 // EXTRACT_ONE: take first element
        );

        IReader seqSome = new SequenceReader(
                "seq_some",
                List.of("lit_foo", "lit_bar"),
                false,
                List.of(1) // EXTRACT_SOME: keep only second element
        );

        IReader seqObj = new SequenceReader(
                "seq_obj",
                List.of("lit_foo", "lit_bar"),
                false,
                Map.of(
                        0, "first",
                        1, "second"
                ) // MAKE_OBJECT
        );

        IReader seqFail = new SequenceReader(
                "seq_fail",
                List.of("lit_foo", "lit_bar"),
                false
        );

        return new Parser(List.of(
                rFoo, rBar,
                seqAll, seqOne, seqSome, seqObj, seqFail
        ));
    }

    @Test
    void sequenceReturnAll() {
        Parser parser = buildParserForFooBar();
        ListMatch match = (ListMatch) parser.read("foobar", 0, "seq_all");

        assertTrue(match.success());
        assertEquals(0, match.start());
        assertEquals(6, match.end());

        List<IMatch> items = match.result();
        assertEquals(2, items.size());

        StringMatch m0 = (StringMatch) items.get(0);
        StringMatch m1 = (StringMatch) items.get(1);

        assertEquals("foo", m0.result());
        assertEquals(0, m0.start());
        assertEquals(3, m0.end());

        assertEquals("bar", m1.result());
        assertEquals(3, m1.start());
        assertEquals(6, m1.end());
    }

    @Test
    void sequenceExtractOne() {
        Parser parser = buildParserForFooBar();
        StringMatch match = (StringMatch) parser.read("foobar", 0, "seq_one");

        assertTrue(match.success());
        // we extracted the first sub-match ("foo"), but span is that of full sequence
        assertEquals("foo", match.result());
        assertEquals(0, match.start());
        assertEquals(6, match.end());
    }

    @Test
    void sequenceExtractSome() {
        Parser parser = buildParserForFooBar();
        ListMatch match = (ListMatch) parser.read("foobar", 0, "seq_some");

        assertTrue(match.success());
        assertEquals(0, match.start());
        assertEquals(6, match.end());

        List<IMatch> items = match.result();
        assertEquals(1, items.size());

        StringMatch only = (StringMatch) items.get(0);
        // we kept only the second element ("bar")
        assertEquals("bar", only.result());
        assertEquals(3, only.start());
        assertEquals(6, only.end());
    }

    @Test
    void sequenceMakeObject() {
        Parser parser = buildParserForFooBar();
        ObjectMatch match = (ObjectMatch) parser.read("foobar", 0, "seq_obj");

        assertTrue(match.success());
        assertEquals(0, match.start());
        assertEquals(6, match.end());

        Map<String, IMatch> obj = match.result();
        assertEquals(2, obj.size());

        StringMatch first = (StringMatch) obj.get("first");
        StringMatch second = (StringMatch) obj.get("second");

        assertEquals("foo", first.result());
        assertEquals(0, first.start());
        assertEquals(3, first.end());

        assertEquals("bar", second.result());
        assertEquals(3, second.start());
        assertEquals(6, second.end());
    }

    @Test
    void sequenceFailsWhenOneElementFails() {
        Parser parser = buildParserForFooBar();
        ListMatch match = (ListMatch) parser.read("foXbar", 0, "seq_fail");

        assertFalse(match.success());
        assertEquals(0, match.start());
        assertEquals(0, match.end());
        assertNull(match.result());
    }
}