package fr.inria.jfbaget.nanoparse.readers;

import java.util.ArrayList;
import java.util.List;


import fr.inria.jfbaget.nanoparse.IReader;
import fr.inria.jfbaget.nanoparse.Parser;
import fr.inria.jfbaget.nanoparse.matches.Match;
import fr.inria.jfbaget.nanoparse.IMatch;
import fr.inria.jfbaget.nanoparse.IParser;


/**
 * Reader that tries several alternative rules in order.
 * <p>
 * A {@code ChoiceReader} attempts each of its inner readers at the same
 * position. The first inner reader that succeeds determines the result:
 * its {@link IMatch} is returned unchanged. If all inner readers fail,
 * the {@code ChoiceReader} itself fails with an empty match
 * (span {@code [start, start)} and {@code result == null}).
 * <p>
 * Skip behaviour is controlled by the {@code skip} flag passed to the
 * constructor. If {@code skip} is {@code true}, the global {@code "skip"}
 * rule is applied before this reader is invoked (via
 * {@link AbstractReader#read(CharSequence, int)}). Each inner reader then
 * handles its own skip behaviour independently.
 */
public class ChoiceReader extends AbstractReader{

    /** Resolved inner readers. */
    public  List<IReader> readers;

    /** Names of the inner readers, to be resolved at link time. */
    public final List<String> readerIds;

    /**
     * Creates a new {@code ChoiceReader}.
     *
     * @param identifier the unique name of this rule in the grammar
     * @param readerIds  the names of the alternative readers to try, in order
     * @param skip       {@code true} if the global {@code "skip"} rule should
     *                   be applied before this choice is evaluated
     */
    public ChoiceReader(String identifier, List<String> readerIds, boolean skip) {
        super(identifier, skip);
        this.readerIds = readerIds;
    }

    /**
     * Tries each inner reader at {@code start}, in order.
     * <ul>
     *   <li>If an inner reader succeeds, its match is returned unchanged.</li>
     *   <li>If all inner readers fail, a failed {@link Match} for this
     *       {@code ChoiceReader} is returned, with {@code start == end}.</li>
     * </ul>
     */
    @Override
    protected IMatch simpleread(CharSequence input, int start) {
    	for (IReader reader : this.readers) {
    		IMatch match = reader.read(input, start);
    		if (match.success()) {
    			return match;
    			//return new Match<>(this, start, match.end(), true, match);
    		}
    	}
        return new Match<>(this, start, start, false, null);
    }

    /**
     * Resolves all inner readers by name.
     */
    @Override
    public void simpleLink(IParser parser) {
    	this.readers = new ArrayList<>();
    	for (String readerId : this.readerIds) {
    		this.readers.add(parser.getReader(readerId));
    		
    	}
    }
    
}
