package fr.inria.jfbaget.nanoparse.basetypes;

import fr.inria.jfbaget.nanoparse.matches.IntMatch;
import fr.inria.jfbaget.nanoparse.readers.RegexReader;

/**
 * Reader for integer literals.
 * <p>
 * {@code IntReader} recognises signed or unsigned decimal integers using the
 * pattern {@code [+-]?[0-9]+}. Examples of accepted forms include:
 * <pre>
 *   0
 *   42
 *   -7
 *   +12345
 * </pre>
 * <p>
 * The matched text is parsed into an {@link Integer} and wrapped in an
 * {@link IntMatch}. Skip behaviour is enabled ({@code skip = true}), so
 * the global {@code "skip"} rule is applied before matching.
 * <p>
 * If you need a permissive numeric reader that also accepts decimals or
 * scientific notation, see {@link FloatReader} or {@link NumberReader}.
 */
public class IntReader extends RegexReader {

	/**
	 * Creates an integer literal reader with the name {@code "INT"}.
	 */
	public IntReader() {
		super("INT", "[+-]?[0-9]+", true);
	}

	/**
	 * Produces an {@link IntMatch} for the matched integer literal.
	 */
	@Override
	protected IntMatch makeMatch(int start, int end, boolean success, String result) {
    	return new IntMatch(this, start, end, success, result);
    }

}
