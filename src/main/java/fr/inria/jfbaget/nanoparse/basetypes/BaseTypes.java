package fr.inria.jfbaget.nanoparse.basetypes;


import fr.inria.jfbaget.nanoparse.IReader;

import java.util.List;

/**
 * Factory and registry for built-in "base type" readers.
 * <p>
 * {@code BaseTypes} defines a fixed set of reserved reader names such as
 * {@code "INT"}, {@code "FLOAT"}, {@code "STRING"}, etc. These names are:
 * <ul>
 *   <li>registered automatically in every {@link fr.inria.jfbaget.nanoparse.Parser}, and</li>
 *   <li>forbidden for user-defined rules (to avoid name clashes).</li>
 * </ul>
 * <p>
 * The {@link #create(String)} method constructs an appropriate {@link IReader}
 * instance for each known base type.
 */
public class BaseTypes {

	/**
	 * Names of all built-in base type readers.
	 */
	private static List<String> readerIds = List.of("BASETYPE", "BOOL", "FLOAT", "STRICTFLOAT", 
			                                        "ID", "INT", "NUMBER", "STRING");

	/**
	 * Returns the list of all built-in base type names.
	 * <p>
	 * These names are reserved: user-defined readers must not use them.
	 *
	 * @return an immutable list of base type rule names
	 */
	public static List<String> getNames() {
		return readerIds;
	}

	/**
	 * Creates a base type reader for the given identifier.
	 * <p>
	 * The following identifiers are recognised:
	 * <ul>
	 *   <li>{@code "BASETYPE"} → {@link BaseTypeReader}</li>
	 *   <li>{@code "BOOL"} → {@link BoolReader}</li>
	 *   <li>{@code "FLOAT"} → {@link FloatReader}</li>
	 *   <li>{@code "STRICTFLOAT"} → {@link StrictFloatReader}</li>
	 *   <li>{@code "ID"} → {@link IDReader}</li>
	 *   <li>{@code "INT"} → {@link IntReader}</li>
	 *   <li>{@code "NUMBER"} → {@link NumberReader}</li>
	 *   <li>{@code "STRING"} → {@link QuotedStringReader}</li>
	 * </ul>
	 *
	 * @param readerId the base type name
	 * @return a new {@link IReader} instance for that base type
	 * @throws IllegalArgumentException if {@code readerId} is not a known base type
	 */
	public static IReader create(String readerId) {
		switch (readerId) {
			case "BASETYPE":
				return new BaseTypeReader();
			case "BOOL":
				return new BoolReader();
			case "FLOAT":
				return new FloatReader();
			case "STRICTFLOAT":
				return new StrictFloatReader();
			case "ID":
				return new IDReader();
			case "INT":
				return new IntReader();
			case "NUMBER":
				return new NumberReader();
			case "STRING":
				return new QuotedStringReader();
			default:
				throw new IllegalArgumentException("Unknown base type: " + readerId);
		}
	}

}
