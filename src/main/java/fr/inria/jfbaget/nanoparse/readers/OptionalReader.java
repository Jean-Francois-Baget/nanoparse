package fr.inria.jfbaget.nanoparse.readers;


import fr.inria.jfbaget.nanoparse.IReader;
import fr.inria.jfbaget.nanoparse.Parser;
import fr.inria.jfbaget.nanoparse.matches.Match;
import fr.inria.jfbaget.nanoparse.IMatch;
import fr.inria.jfbaget.nanoparse.IParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Reader that makes another rule optional.
 * <p>
 * {@code OptionalReader} tries to apply a single inner reader. If the inner
 * reader succeeds, its match is returned unchanged. If it fails, the
 * {@code OptionalReader} itself succeeds with an empty match
 * (span {@code [start, start)} and {@code result == null}).
 * <p>
 * Skip behaviour is controlled by the {@code skip} flag passed to the
 * constructor. If {@code skip} is {@code true}, the global {@code "skip"}
 * rule is applied before this reader is invoked (via
 * {@link AbstractReader#read(CharSequence, int)}). The inner reader handles
 * its own skip behaviour independently.
 */
public class OptionalReader extends AbstractReader{

    /** Resolved inner reader. */
    public IReader reader;
    /** Name of the inner rule to make optional (resolved at link time). */
    public final String readerId;

    /**
     * Creates a new {@code OptionalReader}.
     *
     * @param identifier the unique name of this rule in the grammar
     * @param readerId   the name of the inner reader to make optional
     * @param skip       {@code true} if the global {@code "skip"} rule should
     *                   be applied before trying this optional rule
     */
    public OptionalReader(String identifier, String readerId, boolean skip) {
        super(identifier, skip);
        this.readerId = readerId;
    }

    /**
     * Tries the inner reader at {@code start}. If it succeeds, returns its
     * match. If it fails, returns a successful empty match for this reader.
     */
    @Override
    protected IMatch simpleread(CharSequence input, int start) {
    	IMatch match = this.reader.read(input, start);
    	if (match.success()) {
    		return match;
    	} else {
    		return new Match<>(this, start, start, true, null);
    	}
    }


    /**
     * Resolves the inner reader by name.
     */
    @Override
    public void simpleLink(IParser parser) {
    	this.reader = parser.getReader(this.readerId);
    }
    
}
