package fr.inria.jfbaget.nanoparse.matches;

import fr.inria.jfbaget.nanoparse.IMatch;
import org.json.JSONObject;

import fr.inria.jfbaget.nanoparse.IReader;

/**
 * A match whose semantic result is a {@link Boolean}.
 * <p>
 * This class converts the raw string value produced by the underlying reader
 * using {@link Boolean#parseBoolean(String)} and stores the resulting boolean.
 * <p>
 * Like all {@code Match} subclasses, this class is immutable. To adjust the
 * span or success flag while preserving the semantic result value, use
 * {@link #copyWith(int, int, boolean)}, which returns a new {@code BoolMatch}
 * instance.
 */
public class BoolMatch extends Match<Boolean> {

	/**
	 * Creates a {@code BoolMatch} from a raw string. The string is interpreted
	 * by {@link Boolean#parseBoolean(String)}.
	 *
	 * @param reader  the reader that produced this match
	 * @param start   the start index in the input
	 * @param end     the end index in the input
	 * @param success whether the match succeeded
	 * @param result  the raw string to be interpreted as a boolean
	 */
	public BoolMatch(IReader reader, int start, int end, boolean success, String result) {
		super(reader, start, end, success, Boolean.parseBoolean(result));
	}

	/**
	 * Internal constructor used by {@link #copyWith(int, int, boolean)}.
	 */
	private BoolMatch(IReader reader, int start, int end, boolean success, Boolean result) {
		super(reader, start, end, success, result);
	}

	/**
	 * Returns a new {@code BoolMatch} with the specified span and success flag.
	 * The semantic result is preserved.
	 */
	@Override
	public BoolMatch copyWith(int start, int end, boolean success) {
		return new BoolMatch(this.reader(), start, end, success, this.result());
	}

	/**
	 * Extends the JSON representation by adding the boolean result value.
	 */
	@Override
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("result", this.result());
		return json;
		
	}
}
