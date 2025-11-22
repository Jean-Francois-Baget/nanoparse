package fr.inria.jfbaget.nanoparse.matches;


import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;

import fr.inria.jfbaget.nanoparse.IReader;
import fr.inria.jfbaget.nanoparse.IMatch;

/**
 * A match whose semantic result is a list of {@link IMatch} instances.
 * <p>
 * This class is typically used for repetition rules (e.g. {@code item*},
 * {@code item+}) or for patterns that conceptually return a sequence of
 * sub-matches. The list is expected to be treated as read-only after parsing.
 * <p>
 * Like all {@code Match} subclasses, this class is immutable. To adjust the
 * span or success flag while preserving the semantic list of sub-matches,
 * use {@link #copyWith(int, int, boolean)}, which returns a new
 * {@code ListMatch} instance.
 */
public class ListMatch extends Match<List<IMatch>>{

	/**
	 * Creates a {@code ListMatch} with the given list of sub-matches.
	 *
	 * @param reader  the reader that produced this match
	 * @param start   the start index in the input
	 * @param end     the end index in the input
	 * @param success whether the match succeeded
	 * @param result  the list of sub-matches; should be treated as read-only
	 */
	public ListMatch(IReader reader, int start, int end, boolean success, List<IMatch> result) {
		super(reader, start, end, success, result);
	}

	/**
	 * Returns a new {@code ListMatch} with the specified span and success flag.
	 * The list of sub-matches is preserved (the reference is reused).
	 */
	@Override
	public ListMatch copyWith(int start, int end, boolean success) {
		return new ListMatch(this.reader(), start, end, success, this.result());
	}

	/**
	 * Extends the JSON representation by adding a {@code "result"} field
	 * containing the JSON form of each sub-match.
	 * <ul>
	 *   <li>If {@code result()} is {@code null}, an empty JSON array is used.</li>
	 *   <li>Otherwise, each {@link IMatch} in the list is converted via
	 *       {@link IMatch#toJSON()} and collected into a {@link JSONArray}.</li>
	 * </ul>
	 */
	@Override
	public JSONObject toJSON() {
		JSONObject json = super.toJSON();
		
		if (this.result() == null) {
			json.put("result", new JSONArray());
		} else {
			List<Object> list = this.result().stream()
                .map(IMatch::toJSON)
                .collect(Collectors.toList());
			JSONArray jsonresult = new JSONArray(list);
			json.put("result", jsonresult);
		}
		return json;
	}

}
