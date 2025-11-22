package fr.inria.jfbaget.nanoparse.basetypes;

import java.util.List;

import fr.inria.jfbaget.nanoparse.readers.ChoiceReader;

/**
 * Reader that matches any of the built-in primitive types.
 * <p>
 * {@code BaseTypeReader} is a thin convenience wrapper around a
 * {@link ChoiceReader} with the identifier {@code "BASETYPE"} and with the
 * following alternatives:
 * <ul>
 *   <li>{@code "BOOL"} – boolean literal (handled by {@link BoolReader})</li>
 *   <li>{@code "ID"} – identifier (handled by {@link IDReader})</li>
 *   <li>{@code "STRING"} – quoted string literal (handled by {@link QuotedStringReader})</li>
 *   <li>{@code "NUMBER"} – number literal (handled by {@link NumberReader})</li>
 * </ul>
 * <p>
 * It does not apply skip before matching (i.e. {@code skip = false}), leaving
 * whitespace-handling to the underlying readers.
 */
public class BaseTypeReader extends ChoiceReader {

	/**
	 * Creates a reader that accepts any of the primitive built-in types.
	 * Equivalent to:
	 * <pre>
	 *   CHOICE(BOOL | ID | STRING | NUMBER)
	 * </pre>
	 */
	public BaseTypeReader() {
		super("BASETYPE", List.of("BOOL", "ID", "STRING", "NUMBER"), false);
	}
}
