package fr.inria.jfbaget.nanoparse.examples;

import java.util.List;
import java.util.Map;

import fr.inria.jfbaget.nanoparse.Parser;
import fr.inria.jfbaget.nanoparse.IMatch;
import fr.inria.jfbaget.nanoparse.IReader;
import fr.inria.jfbaget.nanoparse.readers.*;

public class BasicRulesParser extends Parser {

    private static List<IReader> readers() {
        return List.of(
                new RepetitionReader("main", "item", null, 0, Integer.MAX_VALUE, false),
                new ChoiceReader("item", List.of("fact", "rule"), false),
                new SequenceReader("fact", List.of("ID", "dot"), false, 0),
                new StringReader("dot", ".", true),
                new SequenceReader("rule", List.of("ID", "rulesep", "list", "dot"), false, Map.of(0, "head", 2, "body")),
                new StringReader("rulesep", ":-", true),
                new RepetitionReader("list", "ID", "comma", 0, Integer.MAX_VALUE, false),
                new StringReader("comma", ",", true));
    }

    public BasicRulesParser() {
        super(readers());
    }

    public static void main(String[] args) {

        BasicRulesParser parser =  new BasicRulesParser();
        String input = "a :- b, c, d, e.";
        IMatch match = parser.read(input, 0);
        System.out.println(match.toJSON());
    }
}
