package fr.inria.jfbaget.nanoparse.matches;

import java.util.Map;

import org.json.JSONObject;

import fr.inria.jfbaget.nanoparse.IReader;
import fr.inria.jfbaget.nanoparse.IMatch;


/**
 * A match whose semantic result is a map from field names to {@link IMatch}
 * instances.
 * <p>
 * This class is typically used for rules that assemble a structured object
 * from named sub-parts, for example:
 * <ul>
 *   <li>sequences with named components,</li>
 *   <li>record-like patterns,</li>
 *   <li>grammar rules that attach labels to sub-matches.</li>
 * </ul>
 * The map is expected to be treated as read-only after parsing.
 * <p>
 * Like all {@code Match} subclasses, this class is immutable. To adjust the
 * span or success flag while preserving the same map of sub-matches, use
 * {@link #copyWith(int, int, boolean)}, which returns a new
 * {@code ObjectMatch} instance.
 */
public class ObjectMatch extends Match<Map<String, IMatch>>{

	/**
	 * Creates an {@code ObjectMatch} with the given map of named sub-matches.
	 *
	 * @param reader  the reader that produced this match
	 * @param start   the start index in the input
	 * @param end     the end index in the input
	 * @param success whether the match succeeded
	 * @param result  the map from field names to sub-matches; should be treated
	 *                as read-only
	 */
	public ObjectMatch(IReader reader, int start, int end, boolean success, Map<String, IMatch> result) {
		super(reader, start, end, success, result);
	}

	/**
	 * Returns a new {@code ObjectMatch} with the specified span and success flag.
	 * The map of sub-matches is preserved (the reference is reused).
	 */
	@Override
	public ObjectMatch copyWith(int start, int end, boolean success) {
		return new ObjectMatch(this.reader(), start, end, success, this.result());
	}

	/**
	 * Extends the JSON representation by adding a {@code "result"} field
	 * containing a JSON object whose properties are the field names and whose
	 * values are the JSON forms of the corresponding sub-matches.
	 * <ul>
	 *   <li>If {@code result()} is {@code null}, an empty JSON object is used.</li>
	 * </ul>
	 */
	@Override
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		JSONObject result = new JSONObject();
		if (this.result() != null) {
			for (Map.Entry<String, IMatch> entry : this.result().entrySet()) {
				result.put(entry.getKey(), entry.getValue().toJSON());
			}
		}
		json.put("result", result);
		return json;
		
	}

}