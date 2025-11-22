package fr.inria.jfbaget.nanoparse.basetypes;

import fr.inria.jfbaget.nanoparse.readers.RegexReader;

/**
 * Reader for double-quoted string literals.
 * <p>
 * {@code QuotedStringReader} recognises strings enclosed in double quotes,
 * with support for escaped characters inside. It uses the pattern:
 * <pre>
 *   "([^"\\]|\\.)*"
 * </pre>
 * which means:
 * <ul>
 *   <li>a leading double quote {@code "},</li>
 *   <li>zero or more characters that are either:
 *     <ul>
 *       <li>any character except {@code "} and {@code \\}, or</li>
 *       <li>an escape sequence starting with {@code \\} followed by any character,</li>
 *     </ul>
 *   </li>
 *   <li>a closing double quote {@code "}.</li>
 * </ul>
 * Examples of accepted literals include:
 * <pre>
 *   ""
 *   "hello"
 *   "a \"quoted\" word"
 *   "line 1\nline 2"
 * </pre>
 * <p>
 * The matched text (including the surrounding quotes) is returned as a
 * {@link fr.inria.jfbaget.nanoparse.matches.StringMatch}. This reader enables
 * skip ({@code skip = true}), so the global {@code "skip"} rule is applied
 * before matching.
 */
public class QuotedStringReader  extends RegexReader {

	/**
	 * Creates a quoted string literal reader with the name {@code "STRING"}.
	 */
	public QuotedStringReader() {
		super("STRING", "\"([^\"\\\\]|\\\\.)*\"", true);
	}

}
