package fr.inria.jfbaget.nanoparse.examples;

import java.util.List;
import java.util.Map;

import fr.inria.jfbaget.nanoparse.Parser;
import fr.inria.jfbaget.nanoparse.IMatch;
import fr.inria.jfbaget.nanoparse.IReader;
import fr.inria.jfbaget.nanoparse.readers.*;


/**
 * Example parser demonstrating how to build a very small logic-program grammar:
 *
 *     a :- b, c, d, e.
 *
 * The grammar recognizes:
 *
 *   - "facts":         ID "."
 *   - "rules":         ID ":-" ID ("," ID)* "."
 *   - a list of facts and rules
 *
 * This illustrates how to combine:
 *   - RepetitionReader
 *   - ChoiceReader
 *   - SequenceReader (with RETURN_ALL, EXTRACT_ONE, MAKE_OBJECT)
 *   - StringReader
 *   - And the base types (ID)
 */
public class BasicRulesParser extends Parser {

    /**
     * Build the list of grammar rules.
     *
     * Each IReader is a “rule” in the grammar.
     * They are linked together when the Parser constructor calls `reader.link(parser)`.
     */
    private static List<IReader> readers() {
        return List.of(
                // main ::= item* (a program is a sequence of items, read but don't keep the commas, no skip)
                new RepetitionReader("main", "item", null, 0, Integer.MAX_VALUE, false),
                // item ::= fact | rule (either a fact or a rule, no skip)
                new ChoiceReader("item", List.of("fact", "rule"), false),
                // fact ::= ID "." (we only keep the ID: EXTRACT_ONE index 0, skip)
                new SequenceReader("fact", List.of("ID", "dot"), true, 0),
                // the dot can be skipped
                new StringReader("dot", ".", true),
                // rule ::= ID ":-" list "."     (we build an object {head: ID, body: list})
                new SequenceReader("rule", List.of("ID", "rulesep", "list", "dot"), true, Map.of(0, "head", 2, "body")),
                // the rulesep ":-" can be skipped
                new StringReader("rulesep", ":-", true),
                //  list ::= ID ("," ID)*         (comma-separated list of IDs, don't keep commas)
                new RepetitionReader("list", "ID", "comma", 0, Integer.MAX_VALUE, false),
                // comma ::= ","                 (comma between list items)
                new StringReader("comma", ",", true));
    }

    /**
     * Constructor: build Parser with all the above readers.
     */
    public BasicRulesParser() {
        super(readers());
    }

    /**
     * Tiny demo:
     *
     * Parse:
     *     a :- b, c, d, e.
     *
     * Print the resulting IMatch in JSON.
     *
     * JSON gives the full parsed structure and is extremely useful for debugging.
     */
    public static void main(String[] args) {

        BasicRulesParser parser =  new BasicRulesParser();
        String input = "a :- b, c, d, e.";
        IMatch match = parser.read(input, 0);
        System.out.println(match.toJSON());
    }
}
