package fr.inria.jfbaget.nanoparse.matches;


import org.json.JSONObject;

import fr.inria.jfbaget.nanoparse.IReader;

/**
 * A match whose semantic result is an {@link Integer}.
 * <p>
 * This class typically wraps a numeric token and converts its raw string
 * representation into an {@code int} using {@link Integer#parseInt(String)}.
 * <p>
 * Like all {@code Match} subclasses, this class is immutable. To adjust the
 * span or success flag while preserving the semantic result value, use
 * {@link #copyWith(int, int, boolean)}, which returns a new {@code IntMatch}
 * instance.
 */
public class IntMatch extends Match<Integer> {

	/**
	 * Creates an {@code IntMatch} from a raw string. The string is interpreted
	 * by {@link Integer#parseInt(String)}.
	 *
	 * @param reader  the reader that produced this match
	 * @param start   the start index in the input
	 * @param end     the end index in the input
	 * @param success whether the match succeeded
	 * @param result  the raw string to be interpreted as an integer
	 * @throws NumberFormatException if {@code result} is not a valid integer literal
	 */
	public IntMatch(IReader reader, int start, int end, boolean success, String result) {
		super(reader, start, end, success, Integer.parseInt(result));
	}

	/**
	 * Internal constructor used to create modified copies of this match
	 * (for example by {@link #copyWith(int, int, boolean)}), when the
	 * semantic result has already been parsed as an {@link Integer}.
	 */
	private IntMatch(IReader reader, int start, int end, boolean success, Integer result) {
		super(reader, start, end, success, result);
	}

	/**
	 * Returns a new {@code IntMatch} with the specified span and success flag.
	 * The semantic integer result is preserved.
	 */
	@Override
	public IntMatch copyWith(int start, int end, boolean success) {
		return new IntMatch(this.reader(), start, end, success, this.result());
	}

	/**
	 * Extends the JSON representation by adding the integer result value.
	 */
	@Override
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("result", this.result());
		return json;
		
	}
}
