package fr.inria.jfbaget.nanoparse.matches;

import org.json.JSONObject;

import fr.inria.jfbaget.nanoparse.IReader;

/**
 * A match whose semantic result is a {@link String}.
 * <p>
 * This class is typically used for token rules that directly return the
 * matched text, without further conversion. For example, an identifier rule
 * or a literal string rule may produce a {@code StringMatch}.
 * <p>
 * Like all {@code Match} subclasses, this class is immutable. To adjust the
 * span or success flag while preserving the semantic result value, use
 * {@link #copyWith(int, int, boolean)}, which returns a new
 * {@code StringMatch} instance.
 */
public class StringMatch extends Match<String> {

	/**
	 * Creates a {@code StringMatch} with the given semantic value.
	 *
	 * @param reader  the reader that produced this match
	 * @param start   the start index in the input
	 * @param end     the end index in the input
	 * @param success whether the match succeeded
	 * @param result  the matched string value
	 */
	public StringMatch(IReader reader, int start, int end, boolean success, String result) {
		super(reader, start, end, success, result);
	}

	/**
	 * Returns a new {@code StringMatch} with the specified span and success flag.
	 * The semantic string result is preserved.
	 */
	@Override
	public StringMatch copyWith(int start, int end, boolean success) {
		return new StringMatch(this.reader(), start, end, success, this.result());
	}

	/**
	 * Extends the JSON representation by adding the string result value.
	 */
	@Override
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		json.put("result", this.result());
		return json;
		
	}
}
