package fr.inria.jfbaget.nanoparse.readers;


import fr.inria.jfbaget.nanoparse.IParser;
import fr.inria.jfbaget.nanoparse.Parser;
import fr.inria.jfbaget.nanoparse.matches.Match;
import fr.inria.jfbaget.nanoparse.matches.StringMatch;

/**
 * Reader that matches a fixed literal string.
 * <p>
 * A {@code StringReader} succeeds if, at the current position, the input
 * contains exactly the configured {@code pattern} (character by character).
 * It does not perform any case conversion or normalisation: matching is
 * strictly character-wise.
 * <p>
 * Skip behaviour is controlled by the {@code skip} flag passed to the
 * constructor. If {@code skip} is {@code true}, the global {@code "skip"}
 * rule is applied before this reader is invoked (via
 * {@link AbstractReader#read(CharSequence, int)}).
 */
public class StringReader extends AbstractReader {

    /** The literal pattern to match. */
    public final String pattern;

    /**
     * Creates a new {@code StringReader}.
     *
     * @param identifier the unique name of this rule in the grammar
     * @param pattern    the literal string to match
     * @param skip       {@code true} if the global {@code "skip"} rule should
     *                   be applied before matching this pattern
     */
    public StringReader(String identifier, String pattern, boolean skip) {
        super(identifier, skip);
        this.pattern = pattern;
    }

    /**
     * Attempts to match {@link #pattern} at the given position.
     * <ul>
     *   <li>On success, returns a {@link StringMatch} with
     *       {@code success == true} and span {@code [start, start + pattern.length())}.</li>
     *   <li>On failure (input too short or mismatch), returns a
     *       {@link StringMatch} with {@code success == false} and
     *       {@code start == end == start}.</li>
     * </ul>
     */
    @Override
    protected StringMatch simpleread(CharSequence input, int start) {
    	int length = this.pattern.length();
        int end = start + length;
        if (input.length() < end) {
        	return this.makeMatch(start, start, false, null);
        			//new StringMatch(this, start, start, false, null);
        }
        for (int i = 0; i < length; i++) {
        	if(input.charAt(start + i) != this.pattern.charAt(i)) {
        		return this.makeMatch(start, start, false, null);
        				//new StringMatch(this, start, start, false, null);
        	}	
        }
        return this.makeMatch(start, end, true, this.pattern);
        		//new StringMatch(this, start, end, true, this.pattern);
    }

    /**
     * {@code StringReader} does not refer to any other rules, so there is
     * nothing to resolve during linking.
     */
    @Override
    protected void simpleLink(IParser parser) {}

    /**
     * Factory method for creating the corresponding {@link StringMatch}.
     * Subclasses could override this if they want to specialise the match type.
     */
    protected StringMatch makeMatch(int start, int end, boolean success, String result) {
    	return new StringMatch(this, start, end, success, result);
    }

}

