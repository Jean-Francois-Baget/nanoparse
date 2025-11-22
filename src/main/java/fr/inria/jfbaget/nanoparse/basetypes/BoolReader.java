package fr.inria.jfbaget.nanoparse.basetypes;


import fr.inria.jfbaget.nanoparse.matches.BoolMatch;
import fr.inria.jfbaget.nanoparse.readers.RegexReader;

/**
 * Reader for boolean literals.
 * <p>
 * {@code BoolReader} recognises the literals {@code "true"} and {@code "false"}
 * using a regular expression pattern. The matched text is converted to a
 * {@link Boolean} and wrapped in a {@link BoolMatch}.
 * <p>
 * Skip behaviour is enabled ({@code skip = true}), so the global {@code "skip"}
 * rule is applied before this reader is invoked.
 */
public class BoolReader extends RegexReader {

	/**
	 * Creates a boolean literal reader.
	 * <p>
	 * Uses the identifier {@code "BOOL"} and the pattern {@code "(true)|(false)"}.
	 */
	public BoolReader() {
		super("BOOL", "(true)|(false)", true);
	}

	/**
	 * Creates a {@link BoolMatch} from the matched text.
	 */
	@Override
	protected BoolMatch makeMatch(int start, int end, boolean success, String result) {
    	return new BoolMatch(this, start, end, success, result);
    }

}
