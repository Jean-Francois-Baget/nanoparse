package fr.inria.jfbaget.nanoparse.basetypes;

import fr.inria.jfbaget.nanoparse.matches.FloatMatch;
import fr.inria.jfbaget.nanoparse.readers.RegexReader;

/**
 * Reader for stricter floating-point literals.
 * <p>
 * {@code StrictFloatReader} recognises floating-point numbers but, unlike
 * {@link FloatReader}, it does <strong>not</strong> accept bare integers
 * such as {@code 12}. It distinguishes more clearly between integer and
 * floating-point formats and is intended to be used together with
 * {@link IntReader} via {@link NumberReader}.
 * <p>
 * The pattern used is:
 * <pre>
 * [-+]?\d+[eE][-+]?\d+        # integer with mandatory exponent
 *   | \.\d+([eE][+-]?\d+)?    # leading-dot decimal, optional exponent
 *   | \d+\.\d*([eE][+-]?\d+)? # decimal with dot, optional exponent
 * </pre>
 * Examples of accepted forms include:
 * <pre>
 *   1.0
 *   0.5
 *   .5
 *   1e10
 *   -3.14e+2
 * </pre>
 * whereas plain {@code 12} is rejected and left for {@link IntReader}.
 * <p>
 * The matched text is parsed into a {@link Float} and wrapped in a
 * {@link FloatMatch}. This reader enables skip ({@code skip = true}), so
 * the global {@code "skip"} rule is applied before matching.
 */
public class StrictFloatReader extends RegexReader {

	/**
	 * Creates a strict floating-point literal reader with the name
	 * {@code "STRICTFLOAT"}.
	 */
	public StrictFloatReader() {
		super("STRICTFLOAT", "[-+]?\\d+[eE][-+]?\\d+|\\.\\d+([eE][+-]?\\d+)?|\\d+\\.\\d*([eE][+-]?\\d+)?", true);
	}

	/**
	 * Produces a {@link FloatMatch} instance for the matched float literal.
	 */
	@Override
	protected FloatMatch makeMatch(int start, int end, boolean success, String result) {
    	return new FloatMatch(this, start, end, success, result);
    }

}