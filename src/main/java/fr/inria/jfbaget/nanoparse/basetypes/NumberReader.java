package fr.inria.jfbaget.nanoparse.basetypes;

import java.util.List;

import fr.inria.jfbaget.nanoparse.readers.ChoiceReader;

/**
 * Reader that matches any numeric literal base type.
 * <p>
 * {@code NumberReader} is a convenience wrapper around a
 * {@link ChoiceReader} with the identifier {@code "NUMBER"} and with the
 * following alternatives:
 * <ul>
 *   <li>{@code "STRICTFLOAT"} – floating-point literal (handled by {@link StrictFloatReader})</li>
 *   <li>{@code "INT"} – integer literal (handled by {@link IntReader})</li>
 * </ul>
 * <p>
 * It does not apply skip before matching (i.e. {@code skip = false}), leaving
 * whitespace-handling to the underlying readers.
 */
public class NumberReader extends ChoiceReader {

	/**
	 * Creates a reader that accepts any built-in numeric literal type.
	 * Equivalent to:
	 * <pre>
	 *   CHOICE(STRICTFLOAT | INT)
	 * </pre>
	 */
	public NumberReader() {
		super("NUMBER", List.of("STRICTFLOAT", "INT"), false);
	}
}
