package fr.inria.jfbaget.nanoparse.readers;

import fr.inria.jfbaget.nanoparse.IMatch;
import fr.inria.jfbaget.nanoparse.IParser;
import fr.inria.jfbaget.nanoparse.IReader;
import fr.inria.jfbaget.nanoparse.Parser;
import fr.inria.jfbaget.nanoparse.matches.Match;

/**
 * Base class for NanoParse readers.
 * <p>
 * {@code AbstractReader} factors out common behaviour shared by all concrete
 * {@link IReader} implementations:
 * <ul>
 *   <li>storage of the rule name,</li>
 *   <li>handling of the {@code skip} flag,</li>
 *   <li>resolution of the global {@code "skip"} rule during
 *       {@link #link(IParser)},</li>
 *   <li>a final {@link #read(CharSequence, int)} implementation that applies
 *       {@code skip} if requested, then delegates to
 *       {@link #simpleread(CharSequence, int)}.</li>
 * </ul>
 * Subclasses only need to:
 * <ul>
 *   <li>implement {@link #simpleread(CharSequence, int)} to perform the
 *       actual matching logic, and</li>
 *   <li>implement {@link #simpleLink(IParser)} to resolve references to
 *       other rules in the grammar.</li>
 * </ul>
 */
abstract class AbstractReader implements IReader{

    private final String name;
    private final boolean skip;
    private IReader skipReader;

    /**
     * Creates a new reader with the given name and skip behaviour.
     *
     * @param name the unique name of this rule in the grammar
     * @param skip {@code true} if the global {@code "skip"} rule should be
     *             applied before this reader is invoked; {@code false} otherwise
     */
    public AbstractReader(String name, boolean skip) {
        this.name = name;
        this.skip = skip;
    }

    /**
     * Returns the name of this rule.
     */
    @Override
    public String getName() {
        return this.name;
    }

    /**
     * Returns whether this reader requests the global {@code "skip"} rule
     * to be applied before matching.
     */
    @Override
    public boolean requiresSkip() {
    	return this.skip;
    }

    /**
     * Applies the optional {@code "skip"} rule, then delegates to
     * {@link #simpleread(CharSequence, int)}.
     * <p>
     * If {@link #requiresSkip()} is {@code true}, this method first calls
     * the {@code "skip"} reader starting at {@code start}, advances the
     * position to the end of that match, and only then invokes
     * {@code simpleread} from that position. Otherwise, {@code simpleread}
     * is called directly with {@code start}.
     *
     * @param input the character sequence to parse
     * @param start the index at which to start matching
     * @return an {@link IMatch} describing a successful or failed match
     */
    @Override
    public IMatch read(CharSequence input, int start) {
    	int position = start;
    	if (this.skip) {
    		IMatch match = this.skipReader.read(input, start);
    		position = match.end();
    	}
        return this.simpleread(input, position);
    }

    /**
     * Implements the actual matching logic for this reader, starting at
     * the given index. This method is called by {@link #read} after any
     * required {@code skip} has been applied.
     *
     * @param input the character sequence to parse
     * @param start the index at which to start matching (after skip)
     * @return an {@link IMatch} describing a successful or failed match
     */
    protected abstract IMatch simpleread(CharSequence input, int start);

    /**
     * Links this reader to the given parser and resolves the global
     * {@code "skip"} rule.
     * <p>
     * This default implementation:
     * <ol>
     *   <li>calls {@link #simpleLink(IParser)} so the subclass can resolve
     *       its own rule references, then</li>
     *   <li>retrieves and stores the {@code "skip"} reader from the parser.</li>
     * </ol>
     * The {@code "skip"} rule is guaranteed to exist, either because it was
     * defined by the user or because the parser injected a default one.
     *
     * @param parser the parser this reader belongs to
     */
    @Override
    public void link(IParser parser) {
    	this.simpleLink(parser);
    	this.skipReader = parser.getReader("skip");
    	
    }

    /**
     * Subclass hook for resolving internal references to other rules in
     * the grammar. Called once from {@link #link(IParser)} before the
     * global {@code "skip"} rule is stored.
     *
     * @param parser the parser this reader belongs to
     */
    protected abstract void simpleLink(IParser parser);
}