package fr.inria.jfbaget.nanoparse.matches;

import org.json.JSONObject;

import fr.inria.jfbaget.nanoparse.IMatch;
import fr.inria.jfbaget.nanoparse.IReader;

/**
 * Generic implementation of {@link IMatch} for NanoParse.
 * <p>
 * A {@code Match} represents the result of attempting to apply a single
 * {@link IReader} to an input at a given start position. It records:
 * <ul>
 *   <li>the reader that produced the match,</li>
 *   <li>the start and end indices in the input,</li>
 *   <li>whether the match succeeded,</li>
 *   <li>and the semantic result value.</li>
 * </ul>
 * <p>
 * This class is immutable: all fields are set at construction time and never
 * change afterwards. To adjust the span or success flag while preserving
 * the semantic result, use {@link #copyWith(int, int, boolean)}, which
 * returns a new {@code IMatch} instance.
 * <p>
 * Subclasses (such as {@code StringMatch}, {@code ListMatch}, etc.) are free
 * to override {@link #copyWith(int, int, boolean)} and {@link #toJSON()} in
 * order to:
 * <ul>
 *   <li>preserve their concrete type on copies, and/or</li>
 *   <li>expose structured results in the JSON representation.</li>
 * </ul>
 *
 * @param <ResType> the type of the semantic result produced by this match
 */
public class Match<ResType> implements IMatch {

    private final IReader reader;
    private final int start;
    private final int end;
    private final boolean success;
    private final ResType result;

    /**
     * Creates a new match.
     *
     * @param reader  the reader that produced (or attempted) this match
     * @param start   the start index in the input (inclusive)
     * @param end     the end index in the input (exclusive)
     * @param success {@code true} if the match succeeded, {@code false} otherwise
     * @param result  the semantic result value; typically non-null on success
     *                and {@code null} on failure
     */
    public Match(IReader reader, int start, int end, boolean success, ResType result) {
        this.reader = reader;
        this.start = start;
        this.end = end;
        this.success = success;
        this.result = result;
    }

    /**
     * Returns a new {@code Match} instance with the same reader and result
     * as this match, but with updated {@code start}, {@code end} and
     * {@code success} values.
     * <p>
     * Subclasses that wish to preserve their concrete type should override
     * this method and return a new instance of themselves instead.
     *
     * @param start   the new start index (inclusive)
     * @param end     the new end index (exclusive)
     * @param success the new success flag
     * @return a new {@code IMatch} representing the same semantic result over
     *         the specified span
     */
    @Override
    public Match copyWith(int start, int end, boolean success) {
        return new Match(this.reader(), start, end, success, this.result());
    }

    /**
     * {@inheritDoc}
     * <p>
     * On success, {@code success()} is {@code true} and {@code start() <= end()}.
     * On failure, {@code success()} is {@code false} and {@code start() == end()}.
     */
    @Override
    public boolean success() {
        return this.success;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IReader reader() {
    	return this.reader;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int start() {
    	return this.start;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int end() {
    	return this.end;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResType result() {
    	return this.result;
    }

    /**
     * Returns a JSON representation of this match.
     * <p>
     * The default implementation exposes the following keys:
     * <ul>
     *   <li>{@code "success"} – whether the match succeeded,</li>
     *   <li>{@code "rule"} – the name of the reader that produced the match,</li>
     *   <li>{@code "start"} – the start index (inclusive),</li>
     *   <li>{@code "end"} – the end index (exclusive).</li>
     * </ul>
     * Subclasses may override this method to add a {@code "result"} field or
     * nested JSON structures for sub-matches.
     *
     * @return a {@link JSONObject} describing this match
     */
    @Override
    public JSONObject toJSON()  {
    	JSONObject json = new JSONObject();
    	json.put("success", this.success);
    	json.put("rule", this.reader().getName());
    	json.put("start", this.start);
    	json.put("end", this.end);
    	return json;
    	
    }
    
}
