package fr.inria.jfbaget.nanoparse;

import org.json.JSONObject;

/**
 * Represents the result of attempting to match a single {@link IReader}
 * against an input at a given start position.
 * <p>
 * An {@code IMatch} is produced by exactly one reader, and records:
 * <ul>
 *   <li>whether the match succeeded,</li>
 *   <li>the start and end indices in the input,</li>
 *   <li>the semantic result produced by the reader,</li>
 *   <li>and optionally nested sub-matches (accessible through
 *       {@link #result()} or through the JSON form).</li>
 * </ul>
 * <p>
 * Success and failure are encoded explicitly:
 * <ul>
 *   <li>On <strong>success</strong>:
 *     <ul>
 *       <li>{@code success() == true},</li>
 *       <li>{@code start()} is the index where the reader began matching,</li>
 *       <li>{@code end()} is the index immediately after the matched span,</li>
 *       <li>{@code result()} contains either a primitive Java value
 *           (e.g. {@code String}, {@code Integer}, …) or another
 *           {@code IMatch} (for nested rules).</li>
 *     </ul>
 *   </li>
 *   <li>On <strong>failure</strong>:
 *     <ul>
 *       <li>{@code success() == false},</li>
 *       <li>{@code start() == end()} marks the position where matching failed,</li>
 *       <li>{@code result()} is {@code null},</li>
 *       <li>{@code reader()} still identifies the rule that was attempted.</li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * <h3>Immutability and lifecycle</h3>
 * The {@code start} index is fixed at creation time.
 * The {@code end} index is assigned only on successful matches and may be
 * modified internally by the parsing engine via {@link #setEnd(int)}.
 * <p>
 * Client code should treat an {@code IMatch} as effectively immutable after
 * parsing is complete, even though implementations may retain a mutable
 * internal structure for optimisation or memoisation.
 *
 * <h3>JSON form</h3>
 * The {@link #toJSON()} method returns a structured JSON representation of
 * the match for debugging or inspection purposes. It is not intended as a
 * stable serialisation format, but its typical structure includes:
 * <pre>
 * {
 *   "success" : true | false,
 *   "start"   : number,
 *   "end"     : number,
 *   "rule"    : "ruleName",
 *   "result"  : ... nested match or primitive value ...
 * }
 * </pre>
 * Composite rules produce nested JSON objects or arrays corresponding to
 * their sub-matches.
 */
public interface IMatch {

    /**
     * Returns whether the match was successful.
     *
     * @return {@code true} if this match succeeded, {@code false} otherwise
     */
    public boolean success();

    /**
     * Returns the reader that produced this match.
     * Never {@code null}.
     *
     * @return the {@link IReader} that attempted the match
     */
    public IReader reader();

    /**
     * Returns the start index in the input where this match attempted to
     * begin matching.
     * This value is fixed upon match creation.
     *
     * @return the start index (inclusive)
     */
    public int start();

    /**
     * Returns the end index of the matched span.
     * <ul>
     *   <li>On success, {@code end()} is strictly greater than {@code start()}.</li>
     *   <li>On failure, {@code end() == start()}.</li>
     * </ul>
     *
     * @return the end index (exclusive)
     */
    public int end();

    /**
     * Returns a new match with the same semantic result and reader,
     * but possibly different start/end/success flags.
     * Implementations must not mutate this instance.
     */
    IMatch copyWith(int start, int end, boolean success);



    /**
     * Returns the semantic result produced by this match.
     * <p>
     * On success, the result is either:
     * <ul>
     *   <li>a primitive Java value (e.g. {@code String}, {@code Integer}, ...), or</li>
     *   <li>another {@code IMatch} (for nested structures).</li>
     * </ul>
     * On failure, this method returns {@code null}.
     *
     * @return the semantic result or {@code null} on failure
     */
    public Object result();

    /**
     * Returns a JSON representation of this match and its sub-matches.
     * This is intended for debugging and diagnostic use only.
     *
     * @return a {@link JSONObject} describing this match
     */
    public JSONObject toJSON();
    
}
