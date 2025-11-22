package fr.inria.jfbaget.nanoparse.basetypes;

import fr.inria.jfbaget.nanoparse.readers.RegexReader;

/**
 * Reader for identifier tokens.
 * <p>
 * {@code IDReader} recognises identifiers using the pattern
 * {@code [a-zA-Z_$][a-zA-Z0-9_$]*}, i.e.:
 * <ul>
 *   <li>a leading ASCII letter, underscore, or dollar sign,</li>
 *   <li>followed by zero or more letters, digits, underscores, or dollar signs.</li>
 * </ul>
 * Examples of accepted identifiers include:
 * <pre>
 *   foo
 *   _bar
 *   $baz123
 *   My_Var
 * </pre>
 * <p>
 * The matched text is returned as a {@link fr.inria.jfbaget.nanoparse.matches.StringMatch}.
 * This reader enables skip ({@code skip = true}), so the global {@code "skip"} rule
 * is applied before matching.
 */
public class IDReader extends RegexReader {

	/**
	 * Creates an identifier reader with the name {@code "ID"}.
	 */
	public IDReader() {
		super("ID", "[a-zA-Z_$][a-zA-Z0-9_$]*", true);
	}

}
