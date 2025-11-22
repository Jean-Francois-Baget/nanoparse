package fr.inria.jfbaget.nanoparse.matches;

import org.json.JSONObject;

import fr.inria.jfbaget.nanoparse.IReader;

/**
 * A match whose semantic result is a {@link Float}.
 * <p>
 * This class typically wraps a numeric token and converts its raw string
 * representation into a {@code float} using {@link Float#parseFloat(String)}.
 * <p>
 * Like all {@code Match} subclasses, this class is immutable. To adjust the
 * span or success flag while preserving the semantic result value, use
 * {@link #copyWith(int, int, boolean)}, which returns a new {@code FloatMatch}
 * instance.
 */
public class FloatMatch extends Match<Float> {

	/**
	 * Creates a {@code FloatMatch} from a raw string. The string is interpreted
	 * by {@link Float#parseFloat(String)}.
	 *
	 * @param reader  the reader that produced this match
	 * @param start   the start index in the input
	 * @param end     the end index in the input
	 * @param success whether the match succeeded
	 * @param result  the raw string to be interpreted as a float
	 * @throws NumberFormatException if {@code result} is not a valid float literal
	 */
	public FloatMatch(IReader reader, int start, int end, boolean success, String result) {
		super(reader, start, end, success, Float.parseFloat(result));
	}

	/**
	 * Internal constructor used to create modified copies of this match
	 * (for example by {@link #copyWith(int, int, boolean)}), when the
	 * semantic result has already been parsed as a {@link Float}.
	 */
	private FloatMatch(IReader reader, int start, int end, boolean success, Float result) {
		super(reader, start, end, success, result);
	}

	/**
	 * Returns a new {@code FloatMatch} with the specified span and success flag.
	 * The semantic result value is preserved.
	 */
	@Override
	public FloatMatch copyWith(int start, int end, boolean success) {
		return new FloatMatch(this.reader(), start, end, success, this.result());
	}

	/**
	 * Extends the JSON representation by adding the float result value.
	 */
	@Override
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("result", this.result());
		return json;
		
	}
}
