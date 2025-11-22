package fr.inria.jfbaget.nanoparse;

/**
 * A single grammar rule in NanoParse.
 * <p>
 * An {@code IReader} knows how to attempt a match of its rule against an
 * input {@link CharSequence}, starting at a given index. It returns an
 * {@link IMatch} describing either a successful or failed match, and it
 * can optionally request that the global {@code skip} rule be applied
 * before it runs.
 * <p>
 * Readers are typically created as part of a grammar, then {@link #link(IParser)}
 * is called once to resolve references to other rules in the same parser.
 */
public interface IReader {

    /**
     * Attempts to match this rule against {@code input} starting at
     * position {@code start}.
     * <p>
     * The returned {@link IMatch} always reflects the outcome:
     * <ul>
     *   <li>On success:
     *     <ul>
     *       <li>{@code success() == true},</li>
     *       <li>{@code start()} is equal to the {@code start} argument,</li>
     *       <li>{@code end()} is the index immediately after the last
     *           consumed character.</li>
     *     </ul>
     *   </li>
     *   <li>On failure:
     *     <ul>
     *       <li>{@code success() == false},</li>
     *       <li>{@code start() == end()} is typically equal to the
     *           {@code start} argument.</li>
     *     </ul>
     *   </li>
     * </ul>
     * Normal parse failure must be signalled through the returned match,
     * not by throwing an exception. Exceptions should be reserved for
     * configuration or programming errors (e.g. unresolved references).
     *
     * @param input the character sequence to parse
     * @param start the index at which to start matching
     * @return an {@link IMatch} describing a successful or failed match
     */
    public IMatch read(CharSequence input, int start);

    /**
     * Returns the name of this rule.
     * <p>
     * The name is used as the key under which the reader is registered
     * in a {@link IParser}, and it must be unique within a single parser
     * (no two readers may share the same name in the same grammar).
     *
     * @return the unique name of this reader; never {@code null}
     */
    public String getName();

    /**
     * Indicates whether the global {@code "skip"} rule should be applied
     * before this reader is invoked.
     * <p>
     * When this method returns {@code true}, the parsing engine is expected
     * to run the {@code "skip"} rule (if present) starting at the current
     * position, then pass the resulting position to {@link #read(CharSequence, int)}.
     * Composite readers (such as sequences or repetitions) typically rely
     * on their children to express their own skipping behaviour and do not
     * apply {@code skip} around every sub-rule automatically.
     *
     * @return {@code true} if {@code skip} should be applied before this rule,
     *         {@code false} otherwise
     */
    public boolean requiresSkip();

    /**
     * Links this reader to a given parser instance.
     * <p>
     * This method is called once, after all readers have been registered
     * in the parser. It allows the reader to:
     * <ul>
     *   <li>resolve references to other rules (e.g. from rule names to
     *       {@link IReader} instances), and</li>
     *   <li>validate that all referenced rules exist.</li>
     * </ul>
     * Unlike some parser generators, NanoParse does not require a rule
     * to be defined before it is referenced: {@code link} is precisely
     * the phase where such references are resolved.
     *
     * @param parser the parser this reader belongs to
     */
    public void link(IParser parser);
    
}
