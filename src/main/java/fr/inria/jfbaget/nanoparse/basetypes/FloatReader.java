package fr.inria.jfbaget.nanoparse.basetypes;

import fr.inria.jfbaget.nanoparse.matches.FloatMatch;
import fr.inria.jfbaget.nanoparse.readers.RegexReader;

/**
 * Reader for permissive floating-point literals.
		* <p>
 * {@code FloatReader} recognises decimal numeric literals using a relatively
 * flexible regular expression. It accepts:
		* <ul>
 *   <li>pure integers (e.g. {@code 12}),</li>
		*   <li>integers with optional decimal part (e.g. {@code 12.}, {@code 12.3}),</li>
		*   <li>leading-dot forms (e.g. {@code .5}),</li>
		*   <li>optional scientific notation (e.g. {@code 12e3}, {@code -3.14e+2}).</li>
		* </ul>
		* Examples of accepted forms include:
		* <pre>
 *   12
		 *   12.
		 *   12.3
		 *   .5
		 *   -3.14e+2
		 *   +2E-7
		 * </pre>
		* <p>
 * The matched text is parsed into a {@link Float} and wrapped in a
 * {@link FloatMatch}. This reader enables skip ({@code skip = true}), so
 * the global {@code "skip"} rule is applied before matching.
 * <p>
 * If you need a stricter split between integers and floating-point numbers
 * (e.g. where {@code 12} is always treated as an integer), use the
 * combination of {@link IntReader}, {@link StrictFloatReader}, and
 * {@link NumberReader} instead.
 */
public class FloatReader extends RegexReader {

	/**
	 * Creates a permissive floating-point reader.
	 * <p>
	 * Uses identifier {@code "FLOAT"} and the pattern:
	 * <pre>
	 * [-+]?(?:\d+(?:\.\d*)?|\.\d+)(?:[eE][-+]?\d+)?
	 * </pre>
	 */
	public FloatReader() {
		super("FLOAT", "[-+]?(?:\\d+(?:\\.\\d*)?|\\.\\d+)(?:[eE][-+]?\\d+)?", true);
	}

	/**
	 * Produces a {@link FloatMatch} instance for the matched text.
	 */
	@Override
	protected FloatMatch makeMatch(int start, int end, boolean success, String result) {
    	return new FloatMatch(this, start, end, success, result);
    }

}