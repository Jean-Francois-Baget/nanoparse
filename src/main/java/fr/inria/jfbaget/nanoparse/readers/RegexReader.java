package fr.inria.jfbaget.nanoparse.readers;


import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import fr.inria.jfbaget.nanoparse.IMatch;
import fr.inria.jfbaget.nanoparse.IParser;
import fr.inria.jfbaget.nanoparse.Parser;
import fr.inria.jfbaget.nanoparse.matches.Match;
import fr.inria.jfbaget.nanoparse.matches.StringMatch;

/**
 * Reader that matches a regular expression at the current position.
 * <p>
 * A {@code RegexReader} succeeds if the given pattern matches the input
 * starting exactly at the current index. It uses {@link Matcher#lookingAt()},
 * so the match must begin at {@code start} but does not have to consume the
 * whole remaining input.
 * <p>
 * Skip behaviour is controlled by the {@code skip} flag passed to the
 * constructor. If {@code skip} is {@code true}, the global {@code "skip"}
 * rule is applied before this reader is invoked (via
 * {@link AbstractReader#read(CharSequence, int)}).
 */
public class RegexReader extends AbstractReader {

    /** Compiled regular expression pattern. */
    public final Pattern pattern;

    /**
     * Creates a new {@code RegexReader}.
     *
     * @param identifier the unique name of this rule in the grammar
     * @param pattern    the regular expression to compile
     * @param skip       {@code true} if the global {@code "skip"} rule should
     *                   be applied before matching
     * @throws PatternSyntaxException if the pattern is not a valid regex
     */
    public RegexReader(String identifier, String pattern, boolean skip) throws PatternSyntaxException {
        super(identifier, skip);
        this.pattern = Pattern.compile(pattern);
    }

    /**
     * Attempts to match the compiled pattern starting at {@code start}.
     * <ul>
     *   <li>On success, returns a {@link StringMatch} with
     *       {@code success == true} and span {@code [start, matcher.end())},
     *       where the result is {@code matcher.group(0)}.</li>
     *   <li>On failure, returns a {@link StringMatch} with
     *       {@code success == false} and {@code start == end == start}.</li>
     * </ul>
     */
    @Override
    protected IMatch simpleread(CharSequence input, int start) {
    	Matcher matcher = this.pattern.matcher(input).region(start, input.length());
        if (matcher.lookingAt()) {
            return this.makeMatch(start, matcher.end(), true, matcher.group(0));
        }
        else {
            return new StringMatch(this, start, start, false, null);
        }
    }

    /**
     * {@code RegexReader} does not refer to other rules, so there is nothing
     * to resolve during linking.
     */
    @Override
    protected void simpleLink(IParser parser) {
    	
    }

    /**
     * Factory method for creating a successful {@link StringMatch}.
     * Must be called only in the success case.
     */
    protected IMatch makeMatch(int start, int end, boolean success, String result) {
    	return new StringMatch(this, start, end, success, result);
    }
    
}
